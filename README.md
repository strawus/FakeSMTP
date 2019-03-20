# FakeSMTP
SMTP сервер для тестирования приложений с функционалом отправки email.
Отправленные через этот сервер письма будут перенаправляться в Telegram чат через бота

## Как пользоваться
1. Запустить сервер. По умолчанию SMTP сервер будет работать на 8080 порту (можно изменить в fake_smtp.properties файле)
2. Настроить почтовый клиент, или тестируемое приложение для отправки писем через запущенный SMTP сервер
3. В приложении Telegram найти бота по имени FakeSMTP, или fake_smtp_bot и начать с ним работу
4. Команда /register \<email\> добавит указанный email в список адресов для прослушивания. /deregister \<email\> Уберет email из списка

## Как запускать
```sh
./gradlew run
```
