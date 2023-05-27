CREATE TABLE if not exists Vehicle(
  VID INTEGER AUTO_INCREMENT PRIMARY KEY,
  title VARCHAR(250) not NULL,
  buildYear INTEGER not NULL,
  description VARCHAR(250),
  seatingCapacity INTEGER,
  licensePlate VARCHAR(10),
  powerUnit VARCHAR(20) NOT NULL,
  power INTEGER NOT NULL,
  price NUMBER (20,2) NOT NULL,
  createDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
  imageUrl VARCHAR(100),
  updatedate TIMESTAMP,
  isDelete BOOLEAN DEFAULT FALSE
);

CREATE TABLE IF NOT EXISTS LicenceCategory(
  LID INTEGER,
);

CREATE TABLE IF NOT EXISTS VehicleLicence(
  VID INTEGER REFERENCES Vehicle(VID) ,
  LID INTEGER REFERENCES LicenceCategory(LID)
);

CREATE TABLE IF NOT EXISTS Booking(
  BID INTEGER AUTO_INCREMENT PRIMARY KEY,
  cname VARCHAR(50),
  status int,
  IBAN VARCHAR(22),
  Cnumber VARCHAR(16),
  startDate TIMESTAMP,
  endDate TIMESTAMP,
  createDate TIMESTAMP DEFAULT CURRENT_TIMESTAMP(),
  price NUMBER(15,2),
);

CREATE TABLE IF NOT EXISTS Bookedvehicle(
  BID INTEGER REFERENCES Booking(BID),
  VID INTEGER REFERENCES Vehicle(VID),
  DID INTEGER,
  dday TIMESTAMP
);

INSERT INTO Vehicle SELECT * FROM (
  SELECT * FROM Vehicle WHERE FALSE
  UNION SELECT 1,'BMW X6', 2018, 'PKW', 6,'W09229L', 'Motorized', 205, 45.23,'2013-12-20 17:45:12.175161',null,'2013-12-20 17:45:12.175161' ,FALSE
  UNION SELECT 2,'Motorad', 2018, 'OK', 4,'PKW', 'Motorized', 77, 25.23,'2015-12-20 17:45:12.175161',null,'2015-12-20 17:45:12.175161' ,FALSE
  UNION SELECT 3,'Mercedes E200', 2018, 'PKW', 5,'WE2323O', 'Motorized', 180, 35.23,'2016-12-20 17:45:12.175161',null,'2016-12-20 17:45:12.175161' ,FALSE
  UNION SELECT 4,'Bike X', 2018, 'BIKE', 1,NULL , 'Brawn', 0, 9.23,'2013-12-20 17:45:12.175161',null,'2013-12-20 17:45:12.175161' ,FALSE
  UNION SELECT 5,'Mazda X', 2018, 'PKW', 4,'D233OK', 'Motorized', 122, 12.23,'2017-12-20 17:45:12.175161',null,'2017-12-24 17:45:12.175161' ,FALSE
  UNION SELECT 6,'BMW X5', 2018, 'PKW', 6,'W04229L', 'Motorized', 205, 38.23,'2018-01-11 17:45:12.175161',null,'2018-01-12 17:45:12.175161' ,FALSE
  UNION SELECT 7,'Motorad 2', 2018, 'OK', 4,'PKW', 'Motorized', 77, 15.23,'2018-01-20 17:45:12.175161',null,'2018-02-01 17:45:12.175161' ,FALSE
  UNION SELECT 8,'Mercedes A200', 2018, 'PKW', 5,'WE2123O', 'Motorized', 120, 25.23,'2016-12-20 17:45:12.175161',null,'2016-12-20 17:45:12.175161' ,FALSE
  UNION SELECT 9,'Bike Y', 2018, 'BIKE', 1,NULL , 'Brawn', 0, 9.23,'2017-12-20 17:45:12.175161',null,'2017-12-21 17:45:12.175161' ,FALSE
  UNION SELECT 10,'Tesla Model S', 2018, 'PKW', 4,'D2314OK', 'Motorized', 200, 45.23,'2017-12-22 17:45:12.175161',null,'2017-12-26 17:45:12.175161' ,FALSE
)
WHERE NOT EXISTS(SELECT * FROM Vehicle);

INSERT INTO LicenceCategory SELECT * FROM (
  SELECT * FROM LicenceCategory WHERE FALSE
  UNION SELECT 1
  UNION SELECT 2
  UNION SELECT 3
  UNION SELECT 4
)
WHERE NOT EXISTS(SELECT * FROM LicenceCategory);

INSERT INTO VehicleLicence SELECT * FROM (
  SELECT * FROM VehicleLicence WHERE FALSE
  UNION SELECT 2,1
  UNION SELECT 2,2
  UNION SELECT 1,2
  UNION SELECT 1,3
  UNION SELECT 3,2
  UNION SELECT 3,3
  UNION SELECT 5,2
  UNION SELECT 5,3
  UNION SELECT 4,4
  UNION SELECT 9,4
  UNION SELECT 6,2
  UNION SELECT 6,3
  UNION SELECT 7,1
  UNION SELECT 7,2
  UNION SELECT 8,2
  UNION SELECT 8,3
  UNION SELECT 10,2
  UNION SELECT 10,3

)
WHERE NOT EXISTS(SELECT * FROM VehicleLicence);

INSERT INTO Booking SELECT * FROM (
  SELECT * FROM Booking WHERE FALSE
  UNION SELECT 1,'Alex', 3,'DE02370100500001651508', null, '2017-12-20 17:45:12.175161','2017-12-21 17:45:12.175161','2013-12-24 17:45:12.175161',12
  UNION SELECT 2,'Martin', 3,'DE02370100502332651508', null, '2017-12-20 17:45:12.175161','2017-12-28 17:45:12.175161','2015-12-20 17:45:12.175161',14
  UNION SELECT 3,'Melly', 2,'AT02370100502332651508', null, '2017-12-20 04:45:12.175161','2018-01-03 17:45:12.175161','2018-01-07 17:45:12.175161',54
  UNION SELECT 4,'John', 1,'AS02323400502332651508', null, '2018-04-12 17:45:12.175161','2018-04-13 17:45:12.175161','2013-12-20 17:45:12.175161',154
  UNION SELECT 5,'Hans', 1,'TR03470100502332651508', null, '2018-05-01 17:45:12.175161','2018-12-07 17:45:12.175161','2015-12-20 17:45:12.175161',1445
)
WHERE NOT EXISTS(SELECT * FROM Booking);

INSERT INTO Bookedvehicle SELECT * FROM (
  SELECT * FROM Bookedvehicle WHERE FALSE
  UNION SELECT 1,4,1823819,'2013-12-20 17:45:12.175161'
  UNION SELECT 2,1,123234,'2013-12-20 17:45:12.175161'
  UNION SELECT 3,3,1234231,'2013-12-20 17:45:12.175161'
  UNION SELECT 4,2,1232141,'2013-12-20 17:45:12.175161'
  UNION SELECT 4,5,21382937,'2013-12-20 17:45:12.175161'
  UNION SELECT 5,1,1323023, '2013-12-20 17:45:12.175161'
)
WHERE NOT EXISTS(SELECT * FROM Bookedvehicle);