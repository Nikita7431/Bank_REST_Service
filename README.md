# Система управления банковскими картами
com.example.bankcards

.config - пакет с конфигурациями
    .SecurityConfig - конфигурирует цепочку фильтров
        + объявлены бины для ModelMapper и PasswordEncoder
.controller - пакет с контроллерами
    .AdminController - содержит эндпоинты специфичные для пользователей с ролью ADMIN
    .AuthController - содержит эндпоинты для регистрации, аутентификации, для отзыва токена и обновления
    .CardController - содержит эндпоинты для выполнения операций с картами
.dto - пакет с dto - для обмена данными с клиентом
    .request - dto для получаемых данных
    .response - dto для отдаваемых данных
.entity - пакет для сущностей БД
    .Card - представляет сузность Card 
    .Role - представляет сузность Role
    .StatusCard - представляет сузность StatusCard
    .Token - представляет сузность Token
    .User - представляет сузность User
.exception - пакет для обработки исключений 
    .CommandExceptionAdvise - перехватчик исключений. 
            Перехватывает исключения и отправляет клиенту сообщение этого исключения.
.repository - пакет для репозиториев для работы с сущностями бд 
    .CardRepository - для работы с сущностью Card
    .PeopleRepository - для работы с сущностью People
    .StatusCardRepository - для работы с сущностью StatusCard
    .TokenRepository - для работы с сущностью Token
.security - пакет для Security сущностей(Фильтры и UserDetails)
    .JWTFilter - фильтер для работы с jwt(валидация токена)
    .PersonDetails - implements UserDetails
.service - сервисы приложения(бизнес слой)
    .JWTService - отвечает за генерацию, и валидацию токена, и его содержимого
    .RegistrationServiceImpl - implements RegistrationService - отвечает за логику аутентификации, авторизации пользователя, 
        а также отзыв токена, обновление токенов, 
        предоставление PublicKey для валидации токена на стороне клиента.
    .UserCardService implements CardService - отвечает за логику добавления, 
        обновления данных карты, поиску карты по id и number
        и постраничное предоставление карт пользователя
    .StatusUserCardService - implements StatusCardService - отвечает за логику работы с сущьностью над StatusCard
        (поиск статуса)
.util - утилитарные классы
    .AesUtil - Aes шифрование (для jwt)
    .CardMaskUtil - маскирует номер карты
    .StatusСardSheduler - Sheduler - для изменения статуса карты по истечению её срока