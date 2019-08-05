CREATE DATABASE `LibraryManagementSystem` /*!40100 DEFAULT CHARACTER SET latin1 */;

use LibraryManagementSystem;

CREATE TABLE IF NOT EXISTS userLibrary (
  idUser int(11) NOT NULL AUTO_INCREMENT,
  nickName nvarchar(255) NOT NULL,
  firstName nvarchar(255) NOT NULL,
  LastName nvarchar(255) NOT NULL,
  isAdmin boolean NOT NULL,
  pass nvarchar(255) NOT NULL,
  PRIMARY KEY (idUser)
);

insert into userLibrary (nickName, firstName, LastName, isAdmin, pass) values ("DGARY", "GARY", "DONOGHUE", TRUE, "123");
insert into userLibrary (nickName, firstName, LastName, isAdmin, pass) values ("IREGIANE", "REGIANE", "IVO", FALSE, "123");


CREATE TABLE IF NOT EXISTS book (
    idBook INT AUTO_INCREMENT,
    title VARCHAR(255) NOT NULL,
    subTitle VARCHAR(255) NOT NULL,
    quantity INT NOT NULL,
    PRIMARY KEY (idBook)
);

insert into book (title, subTitle, quantity) values ('BOOK 1', 'SUB 1', 0);
insert into book (title, subTitle, quantity) values ('BOOK 2', 'SUB 2', 10);
insert into book (title, subTitle, quantity) values ('BOOK 3', 'SUB 3', 20);


CREATE TABLE IF NOT EXISTS bookBorrowed (
    idBookBorrowed INT AUTO_INCREMENT,
    idBook INT NOT NULL,
    idUser INT NOT NULL,
    PRIMARY KEY (idBookBorrowed)
);

insert into bookBorrowed (idBook, idUser) values (1, 1);
insert into bookBorrowed (idBook, idUser) values (2, 1);
insert into bookBorrowed (idBook, idUser) values (3, 1);
insert into bookBorrowed (idBook, idUser) values (1, 2);
insert into bookBorrowed (idBook, idUser) values (2, 2);

select * from userLibrary;

select * from book;
