# Руководство по публикации в Maven Central

## Настройка учетных данных

### 1. Создайте файл ~/.gradle/gradle.properties
Создайте файл `~/.gradle/gradle.properties` в домашней директории с реальными учетными данными:

```properties
# Sonatype OSSRH credentials
ossrhUsername=your_sonatype_username
ossrhPassword=your_sonatype_password

# GPG signing
signing.keyId=YOUR_GPG_KEY_ID
signing.password=your_gpg_password
signing.secretKeyRingFile=C:\\Users\\YourUsername\\.gnupg\\secring.gpg
```

### 2. Получение учетных данных Sonatype
1. Зайдите на https://central.sonatype.com/
2. Войдите в систему
3. Перейдите в профиль → View User Tokens
4. Создайте токен для публикации

### 3. Настройка GPG
1. Сгенерируйте GPG ключ для подписи артефактов (при генерации использовать passphrase)

- В Linux / Mac - при помощи консоли: `gpg --generate-key`
- В Windows - можно использовать `Kleopatra`, входящую в состав `gpg4win`

2. Убедитесь, что ваш GPG ключ экспортирован в файл
3. Укажите правильный путь к файлу секретного ключа
4. `signing.keyId` - последние 8 символов вашего GPG ключа

Проверка PGP ключа -
по [ссылке](https://keyserver.ubuntu.com/pks/lookup?search=82D90366AA81E56F4B3FFA06030AC339FCA11168&fingerprint=on&op=index)

## Публикация версий (на примере v.1.0.2)

### 1. Переключиться на тег
```
git checkout v.1.0.2
```

### 2. Опубликовать в локальный Maven репозиторий
```
./gradlew publishToMavenLocal
```

Проверить артефакт в ~/.m2/repository/io/github/andrei-punko/pde-solvers

### 3. Опубликовать в Maven Central
```
./gradlew publishMavenJavaPublicationToOSSRHRepository
```

У меня не сработало, поэтому - использовал `Publishing By Uploading a Bundle` по
[инструкции](https://central.sonatype.org/publish/publish-portal-upload)

### 4. Подготовил структуру папок вида
`io/github/andrei-punko/pde-solvers/1.0.2`, куда переписал из локального Maven репозитория файлы

### 5. Удалил файл `maven-metadata-local.xml`

### 6. Перешел в папку:
```
cd io/github/andrei-punko/pde-solvers/1.0.2
```

### 7. Сгенерировал чек-суммы для всех файлов, используя Powershell console:
```
Get-ChildItem -File | Where-Object { $_.Extension -ne '.md5' -and $_.Extension -ne '.sha1' } | ForEach-Object {
    # MD5
    $md5 = (Get-FileHash -Path $_.FullName -Algorithm MD5).Hash.ToLower()
    $md5 | Out-File -FilePath ($_.FullName + ".md5") -NoNewline -Encoding ASCII

    # SHA1
    $sha1 = (Get-FileHash -Path $_.FullName -Algorithm SHA1).Hash.ToLower()
    $sha1 | Out-File -FilePath ($_.FullName + ".sha1") -NoNewline -Encoding ASCII
}
```

### 8. Запаковал папку `io` с содержимым в zip-архив

### 9. Загрузил его в Sonatype [deployments](https://central.sonatype.com/publishing/deployments)

## Проверка публикации

1. После публикации зайдите на https://central.sonatype.com/
2. Проверить статус в [deployments](https://central.sonatype.com/publishing/deployments)
3. Можно попробовать найти артефакт в поиске по artifactId `pde-solvers`

## Использование в проектах

После публикации артефакт можно использовать в других проектах:

```xml

<dependency>
  <groupId>io.github.andrei-punko</groupId>
  <artifactId>pde-solvers</artifactId>
  <version>1.0.2</version>
</dependency>
```

Или в Gradle:

```gradle
implementation 'io.github.andrei-punko:pde-solvers:1.0.2'
```

## Важные замечания

1. **Версии**: Убедитесь, что версии в тегах соответствуют версиям в build.gradle
2. **GPG подпись**: Все артефакты должны быть подписаны GPG
3. **Метаданные**: Проверьте, что все метаданные POM заполнены корректно
4. **Тестирование**: Все тесты должны проходить перед публикацией

## Структура артефактов

После публикации будут доступны следующие артефакты:
- `pde-solvers-1.0.2.jar` - основной JAR
- `pde-solvers-1.0.2-sources.jar` - исходный код
- `pde-solvers-1.0.2-javadoc.jar` - JavaDoc документация
- `pde-solvers-1.0.2.pom` - метаданные Maven
