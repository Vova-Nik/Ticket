#Hillel Java Enterprize course
Spring Framework
Hybernate
e.t.c.
##Ticket project

##Сущности
###Vehicle
 Транспортное средство, не конкретное без VIN
 
 Типа
* поезд
* скоростной
* 100 мест бизнес класса
* 500 мест эконом класса

или
* самолет
* Боинг 737
* 10 мест Бизнес класса
* 40 мест эконом

создается на этапе планирования системы или по запросу перевозчика

###Station
Станция

содержит
* Город
* координаты
* название
* тип (транзитная - возможна пересадка на маршрути других направлений или нетранзитная)

создается на этапе планирования системы

###Route
Абстрактный маршрут на уровне расписания движения.

Содержит 
* начальную станцию (Пока String) переделать на Station
* конечную станцию (Пока String) переделать на Station
* промежуточные станции 
* время (чч.мм) отпрправления прибытия на каждую станцию
* Vehicle OneToMany (Vehicle не знает о маршрутах. пока по крайней мере)
* некий шаблон периодичности (Ежедневно, по четным числам месяца, (понедельник, среда, пятница)) и т.п. (пока только daily)

создается на этапе планировани

###Trip
Реализация Route

Содержит 
* Route но с конкретными датами отправления и прибытия
* количество мест
* количество проданных/свободных мест

Создается когда заказан первый билет конкретно на этот маршрут с конкретной датой

###Journey
Поездка
содержит
* Trip
* станцию посадки
* станцию высадки
* класс билета (to do)
* номер места (to do)
* номер билета (to do)
* Данные о пассажире (ClientEntity)