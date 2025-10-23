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
   1. В Linux / Mac - при помощи консоли: `gpg --generate-key`
   2. В Windows - можно использовать `Kleopatra`, входящую в состав `gpg4win`
2. Убедитесь, что ваш GPG ключ экспортирован в файл
3. Укажите правильный путь к файлу секретного ключа
4. `signing.keyId` - последние 8 символов вашего GPG ключа

## Публикация версий

### Публикация версии v.1.0.2
```bash
# Переключиться на тег
git checkout v.1.0.2

# Опубликовать в локальный Maven репозиторий
./gradlew publishToMavenLocal

# Проверить артефакт в ~/.m2/repository/io/github/andrei-punko/pde-solvers

# Опубликовать в Maven Central
./gradlew publishMavenJavaPublicationToOSSRHRepository

# Проверить статус на https://central.sonatype.com/ в профиле → View Deployments
```

## Проверка публикации

1. После публикации зайдите на https://central.sonatype.com/
2. Можно попробовать найти артефакт в поиске по artifactId `pde-solvers`

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
implementation 'io.github.andreipunko:pde-solvers:1.0.2'
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
