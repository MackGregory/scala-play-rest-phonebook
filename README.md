Реализованные запросы:
GET         /phones
Возвращает список всех телефонов 

GET         /phones/searchByNumberSub/ ? q_?"number=$number"
Возвращает список телефонов, содержащих подстроку в номере
Пример: localhost:9000/phones/searchByNumberSub?number=001

GET         /phones/searchByNameSub ? q_?"name=$name"
Возвращает список телефонов, содержащих подстроку в имени
Пример: localhost:9000/phones/searchByNameSub?name=ivan

DELETE      /phones/delete/$number
Удалить телефон

POST        /phones/addPhone
Добавить телефон. 
В теле запроса Json вида: {"name": "<имя>", "number": "<номер телефона>"}

POST        /phones/update/$number
Изменить данные телефона. 
В теле запроса Json вида: {"name": "<имя>", "number": "<номер телефона>"}

Все запросы отвечают Json-ом вида: 
{
    "errorCode": <0 - успешное выполнение \ 1 - ошибки при выполнении>,
    "message": "<сообщение>",
    "data": [{Json в случае успешного выполнения, Null - в случае ошибки}]
}

Параметры подключения к БД /app/configs/DBParameters