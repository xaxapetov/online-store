# Статья на тему SSL: https://hackware.ru/?p=12982
# Команды ниже действительный для OpenSSL ≥ 1.1.1
# Создание корневого приватного ключа
openssl genpkey -algorithm RSA -out rootCA.key -aes-128-cbc -pkeyopt rsa_keygen_bits:4096
# Создание самоподписанного корневого сертификата
openssl req -x509 -new -nodes -key rootCA.key -subj "/C=RU/ST=Crimea/O=Store, Inc./CN=localhost" -addext "subjectAltName=DNS:localhost,IP:127.0.0.1" -sha256 -days 3650 -out rootCA.crt
# Создание приватного ключа сертификата
openssl genpkey -algorithm RSA -out store.ru.key
# Создание файла с запросом на подпись сертификата (CSR)
openssl req -new -sha256 -key store.ru.key -subj "/C=RU/ST=Crimea/O=Store, Inc./CN=localhost" -addext "subjectAltName=DNS:localhost,IP:127.0.0.1" -out store.ru.csr
# Проверка содержания CSR
openssl req -in store.ru.csr -noout -text
# Создание сертификата
openssl x509 -req -in store.ru.csr -CA rootCA.crt -CAkey rootCA.key -CAcreateserial -out store.ru.crt -days 3650 -sha256
# Объединение сертификата и приватного ключа в файл .p12
openssl pkcs12 -export -in store.ru.crt -inkey store.ru.key -out store.ru.p12
# Создание хранилища ключей java ( *.keystore *.jks)
keytool -importkeystore -v -srckeystore store.ru.p12 -srcstoretype pkcs12 -destkeystore server.keystore


# Созданные файлы (пароль для запароленных сертификатов: "qwerty")
- rootCA.key — приватный ключ Центра Сертификации, должен храниться в секрете в CA
- rootCA.crt — публичный корневой сертификат Центра Сертификации — должен быть установлен на всех пользовательских устройствах
- store.ru.key — приватный ключ веб-сайта, должен храниться в секрете на сервере. Указывает в конфигурации виртуального хоста при настройке веб-сервера
- store.ru.csr — запрос на подпись сертификата, после создания сертификата этот файл не нужен, его можно удалить
- store.ru.crt — сертификат сайта. Указывает в конфигурации виртуального хоста при настройке веб-сервера, не является секретным.
- store.ru.p12 — промежуточный файл .p12 для преобразования в *.keystore (объединяет сертификат и приватный ключ)
- server.keystore — хранилище ключей java (понадобится для keycloak)