CREATE TABLE IF NOT EXISTS public.routes
(
    number       integer PRIMARY KEY,
    station_from varchar(24) NOT NULL,
    station_to   varchar(24) NOT NULL,
    dep_period   varchar(8) default 'daily',
    departure    time        NOT NULL,
    duration integer  NOT NULL,
    info         varchar(256)
);

TRUNCATE public.routes RESTART IDENTITY;

INSERT INTO public.routes (number, station_from, station_to, dep_period, departure, duration, info)
VALUES (10, 'odessa', 'kiev', 'daily', '20:00', '500', 'express'),
       (11, 'kiev', 'odessa', 'daily', '21:00', '500', 'express'),
       (92, 'odessa', 'lviv', 'daily', '20:00', '500', 'express'),
       (93, 'lviv', 'odessa', 'daily', '20:00', '500', 'express'),
       (96, 'kiev', 'lviv', 'daily', '21:00', '500', 'express'),
       (97, 'lviv', 'kiev', 'daily', '20:00', '500', 'express'),
       (121, 'kiev', 'odessa', 'daily', '22:30', '600', 'ordinal'),
       (120, 'odessa', 'kiev', 'daily', '21:45', '540', 'ordinal'),
       (146, 'kiev', 'lviv', 'daily', '21:00', '500', 'ordinal'),
       (147, 'lviv', 'kiev', 'daily', '20:00', '500', 'ordinal'),
       (151, 'kiev', 'dnepr', 'daily', '21:00', '330', 'ordinal'),
       (152, 'dnepr', 'kiev', 'daily', '20:00', '340', 'ordinal');

