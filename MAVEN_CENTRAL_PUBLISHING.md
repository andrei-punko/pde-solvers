# Руководство по публикации в Maven Central

В проекте используется Sonatype **Central** (через совместимый OSSRH Staging API) и плагин
[gradle-nexus/publish-plugin](https://github.com/gradle-nexus/publish-plugin): он создаёт staging-репозиторий,
загружает артефакты и по **close/release** передаёт их в [Central Publisher](https://central.sonatype.com/publishing).

## Основной способ: Gradle

Убедитесь, что в `build.gradle` указана **релизная** версия (не `-SNAPSHOT`), настроены учётные данные и GPG (ниже).

Одна команда — загрузка в Sonatype staging, закрытие и релиз в Maven Central:

```bash
./gradlew publishToSonatype closeAndReleaseSonatypeStagingRepository
```

Пошагово (сначала только заливка и закрытие staging, релиз из UI при необходимости):

```bash
./gradlew publishToSonatype closeSonatypeStagingRepository
```

После успешного релиза проверьте [Deployments](https://central.sonatype.com/publishing/deployments). Появление
артефакта в поиске ([central.sonatype.com](https://central.sonatype.com/) / зеркала) может занять от нескольких минут
до нескольких часов.

### Почему не «голый» maven-publish в OSSRH URL

Публикация только через `maven-publish` в URL вида `.../staging/deploy/maven2/` **не** переносит деплой в портал
автоматически: нужен либо вызов **Manual API** после загрузки (например
`POST .../manual/upload/defaultRepository/<namespace>` с тем же IP, что и при upload), либо плагин со **staging**
(**close**), как у `publish-plugin`. Подробности: [OSSRH Staging API](https://central.sonatype.org/publish/publish-portal-ossrh-staging-api/).

## Настройка учетных данных

### 1. Создайте файл ~/.gradle/gradle.properties

```properties
# Sonatype Central: User Token (username + password из «View User Tokens» на central.sonatype.com)
ossrhUsername=your_token_username
ossrhPassword=your_token_password

# GPG signing
signing.keyId=YOUR_GPG_KEY_ID
signing.password=your_gpg_password
signing.secretKeyRingFile=C:\\Users\\YourUsername\\.gnupg\\secring.gpg
```

Имена `ossrhUsername` / `ossrhPassword` заданы в `build.gradle` явно; плагин по умолчанию ожидает
`sonatypeUsername` / `sonatypePassword` — у нас используются эти свойства для совместимости с уже настроенным файлом.

### 2. Получение токена Sonatype

1. Зайдите на https://central.sonatype.com/
2. Войдите в систему
3. Профиль → **View User Tokens**
4. Создайте токен для публикации (нужны **Central Portal** токены, не устаревшие чисто «OSSRH» без портала)

### 3. Настройка GPG

1. Сгенерируйте GPG ключ для подписи артефактов (задайте passphrase).

- Linux / macOS: `gpg --generate-key`
- Windows: **Kleopatra** (состав [Gpg4win](https://www.gpg4win.org/))

2. Если используете `signing.secretKeyRingFile`, убедитесь, что секретный ключ **экспортирован в файл**
   (классический `secring.gpg` или экспорт из Kleopatra), и путь в `gradle.properties` верный.

3. `signing.keyId` — обычно **последние 8 символов** hex-идентификатора ключа (`gpg --list-secret-keys`).

Публичный ключ должен быть доступен для проверки подписи; при необходимости загрузите его на keyserver. Пример
проверки ключа автора этого репозитория на keyserver:
[Ubuntu Keyserver](https://keyserver.ubuntu.com/pks/lookup?search=82D90366AA81E56F4B3FFA06030AC339FCA11168&fingerprint=on&op=index).
Для своего ключа ищите по fingerprint на [keyserver.ubuntu.com](https://keyserver.ubuntu.com/).

## Публикация версий (чеклист)

### 0. Версия и тег

Поменяйте в `build.gradle` версию с `x.y.z-SNAPSHOT` на `x.y.z`, закоммитьте при необходимости, создайте тег `v.x.y.z`.

### 1. Переключиться на тег (опционально)

```bash
git checkout v.x.y.z
```

### 2. Локальная проверка артефактов

```bash
./gradlew publishToMavenLocal
```

Проверка: `~/.m2/repository/io/github/andrei-punko/pde-solvers/<version>/` (путь подставьте свою версию).

### 3. Публикация в Maven Central

```bash
./gradlew publishToSonatype closeAndReleaseSonatypeStagingRepository
```

## Альтернатива: загрузка bundle в портал

Если по каким-то причинам Gradle-публикация недоступна, можно использовать **Publishing By Uploading a Bundle** по
[инструкции Sonatype](https://central.sonatype.org/publish/publish-portal-upload).

Кратко (на примере координат `io.github.andrei-punko` / `pde-solvers` / `<version>`):

1. Скопируйте файлы из локального Maven в дерево `io/github/andrei-punko/pde-solvers/<version>/` (как после
   `publishToMavenLocal`: `~/.m2/repository/io/github/andrei-punko/pde-solvers/<version>/`).
2. Удалите `maven-metadata-local.xml`.
3. Перейдите в каталог версии (все артефакты лежат в одной папке):

   ```bash
   cd io/github/andrei-punko/pde-solvers/<version>
   ```

   Сгенерируйте `.md5` и `.sha1` для **каждого** файла (PowerShell из этой папки):

   ```powershell
   Get-ChildItem -File | Where-Object { $_.Extension -ne '.md5' -and $_.Extension -ne '.sha1' } | ForEach-Object {
       $md5 = (Get-FileHash -Path $_.FullName -Algorithm MD5).Hash.ToLower()
       $md5 | Out-File -FilePath ($_.FullName + ".md5") -NoNewline -Encoding ASCII
       $sha1 = (Get-FileHash -Path $_.FullName -Algorithm SHA1).Hash.ToLower()
       $sha1 | Out-File -FilePath ($_.FullName + ".sha1") -NoNewline -Encoding ASCII
   }
   ```

4. Вернитесь к корню дерева (родитель каталога `io`) и упакуйте каталог **`io`** целиком в zip (внутри архива должен
   сохраняться путь `io/github/...`). Загрузите архив в [Deployments](https://central.sonatype.com/publishing/deployments).

## Проверка публикации

1. https://central.sonatype.com/ → [Deployments](https://central.sonatype.com/publishing/deployments)
2. Поиск по `groupId` / `artifactId` (`pde-solvers`)

## Использование в проектах

Maven:

```xml
<dependency>
  <groupId>io.github.andrei-punko</groupId>
  <artifactId>pde-solvers</artifactId>
  <version>x.y.z</version>
</dependency>
```

Gradle:

```gradle
implementation 'io.github.andrei-punko:pde-solvers:x.y.z'
```

Подставьте опубликованную версию вместо `x.y.z`.

## Важные замечания

1. **Версии**: теги Git и поле `version` в `build.gradle` должны совпадать с публикуемым релизом.
2. **GPG**: артефакты для Maven Central должны быть подписаны.
3. **Метаданные POM**: проверьте лицензию, разработчиков, SCM и прочие поля в `build.gradle`.
4. **Тесты**: перед публикацией все тесты должны проходить; локально удобно `./gradlew build` (или ваш CI).

## Структура артефактов

Для версии `x.y.z` типичный набор:

- `pde-solvers-x.y.z.jar`
- `pde-solvers-x.y.z-sources.jar`
- `pde-solvers-x.y.z-javadoc.jar`
- `pde-solvers-x.y.z.pom`
- подписи и чек-суммы (`.asc`, `.md5`, `.sha1` и т.д., в зависимости от процесса)
