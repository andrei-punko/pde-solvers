# How to make new release on GitHub?

Объяснено на примере "Мы имеем `1.0.1-SNAPSHOT`"

## Сначала нужно обновить версию в build.gradle на не-SNAPSHOT:
`1.0.1-SNAPSHOT -> 1.0.1`

## Собрать артефакт с генерацией документации:

```
./gradlew clean build javadoc
```

## Создать тег для релиза и отправить его в репозиторий:

```
git tag -a v1.0.1 -m "Release version 1.0.1"
git push origin v1.0.1
```

## В .env в корне проекта добавьте свои credentials для доступа в GitHub:

- Как их получить? Для получения Personal Access Token (PAT) на GitHub:
  - Перейдите в `Settings -> Developer settings -> Personal access tokens -> Tokens (classic)`
  - Нажмите `Generate new token (classic)`
  - Дайте токену название (например, `pde-solvers-publish`)
  - Выберите следующие разрешения:
  - write:packages
  - delete:packages
  - repo (для доступа к репозиторию)
  - Нажмите `Generate token`
  - Скопируйте сгенерированный токен
- После этого:
  - Замените your-github-username на ваше имя пользователя GitHub (`andrei-punko`)
  - Замените your-github-personal-access-token на сгенерированный токен
  - Добавьте .env в .gitignore, чтобы не публиковать учетные данные в репозиторий

## Опубликуйте артефакт в GitHub Packages:

```
./gradlew publish
```

## Создайте релиз на GitHub:

- Перейдите на страницу репозитория на GitHub
- Нажмите на `Releases` в правой части
- Нажмите `Create a new release`
- Выберите тег v1.0.1
- Заполните заголовок (например, `Release 1.0.1`)
- Добавьте описание изменений
- Прикрепите следующие файлы:
- build/libs/pde-solvers-1.0.1.jar
- build/libs/pde-solvers-1.0.1-javadoc.jar
- Нажмите `Publish release`

## После релиза верните версию в build.gradle обратно к SNAPSHOT (следующей версии):
`version = '1.0.2-SNAPSHOT'`

## Отправьте изменения в репозиторий:

```
git push origin master
```
