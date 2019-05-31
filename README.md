# REST
Все запросы отвечают Json-ом вида:  
```json
{
    "errorCode": <0 - успешное выполнение \ 1 - ошибки при выполнении>,
    "message": "<сообщение>",
    "data": [{Json в случае успешного выполнения, Null - в случае ошибки}]
}
```

Реализованные запросы:
###GET         /phones
Возвращает список всех телефонов  
Запрос:  
`localhost:9000/phones`  
Ответ:  
```json

```

###GET         /phones/searchByNumberSub?number=
Возвращает список телефонов, содержащих подстроку в номере
Запрос:  
`localhost:9000/phones/searchByNumberSub?number=001`  
Ответ:  
```json

```

###GET         /phones/searchByNameSub?name=
Возвращает список телефонов, содержащих подстроку в имени  
Запрос:  
`localhost:9000/phones/searchByNameSub?name=ivan`  
Ответ:  
```json

```

###DELETE      /phones/delete/$number
Удалить телефон  
Запрос:  
`localhost:9000/phones/delete/$number`  
Ответ:  
```json

```

###POST        /phones/addPhone 
Добавить телефон.   
Запрос:  
`/phones/addPhone`  
Тело запроса:  
```json
{
  "name": "<имя>",
  "number": "<номер телефона>"
}
```
Ответ:  
```json

```

###POST        /phones/update/$number
Изменить данные телефона.
Если указанного номера в базе не существует, то создастся телефон, с данными из тела запроса.
Запрос:  
`localhost:9000/phones/update/+7000000001`  
Тело запроса:  
```json
{
  "name": "<имя>",
  "number": "<номер телефона>"
}
```
Ответ:  
```json

```

#Конфигурация

Параметры подключения к БД /app/configs/DBParameters
