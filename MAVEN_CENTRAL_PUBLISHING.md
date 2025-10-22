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
1. Зайдите на https://s01.oss.sonatype.org/
2. Войдите в систему
3. Перейдите в профиль → User Token Access
4. Создайте токен для публикации

### 3. Настройка GPG
1. Убедитесь, что ваш GPG ключ экспортирован в файл
2. Укажите правильный путь к файлу секретного ключа
3. `signing.keyId` - последние 8 символов вашего GPG ключа

## Публикация версий

### Публикация версии v.1.0.0
```bash
# Переключиться на тег
git checkout v.1.0.0

# Опубликовать в Maven Central
./gradlew publishMavenJavaPublicationToOSSRHRepository

# Проверить статус на https://s01.oss.sonatype.org/
```

### Публикация версии v.1.0.1
```bash
# Переключиться на тег
git checkout v.1.0.1

# Опубликовать в Maven Central
./gradlew publishMavenJavaPublicationToOSSRHRepository

# Проверить статус на https://s01.oss.sonatype.org/
```

## Проверка публикации

1. После публикации зайдите на https://s01.oss.sonatype.org/
2. Найдите ваш артефакт в разделе "Staging Repositories"
3. Закройте и освободите репозиторий
4. Через несколько часов артефакт появится в Maven Central

## Использование в проектах

После публикации артефакт можно будет использовать в других проектах:

```xml
<dependency>
    <groupId>io.github.andreipunko</groupId>
    <artifactId>pde-solvers</artifactId>
    <version>1.0.0</version>
</dependency>
```

Или в Gradle:

```gradle
implementation 'io.github.andreipunko:pde-solvers:1.0.0'
```

## Важные замечания

1. **Версии**: Убедитесь, что версии в тегах соответствуют версиям в build.gradle
2. **GPG подпись**: Все артефакты должны быть подписаны GPG
3. **Метаданные**: Проверьте, что все метаданные POM заполнены корректно
4. **Тестирование**: Все тесты должны проходить перед публикацией

## Структура артефактов

После публикации будут доступны следующие артефакты:
- `pde-solvers-1.0.0.jar` - основной JAR
- `pde-solvers-1.0.0-sources.jar` - исходный код
- `pde-solvers-1.0.0-javadoc.jar` - JavaDoc документация
- `pde-solvers-1.0.0.pom` - метаданные Maven
