DROP DATABASE IF EXISTS db_pattanasin;
CREATE DATABASE db_pattanasin;
USE db_pattanasin;




    
    
CREATE TABLE Employees (
	employeeID INT UNSIGNED NOT NULL AUTO_INCREMENT PRIMARY KEY,
    displayName VARCHAR(20) NOT NULL,
    Active bit NOT NULL,
    Password VARCHAR(20) NOT NULL,
    User_Role VARCHAR(20) NOT NULL,	       --  "Manager" (Can change food prices) or "Sale" (cannot change prices)			
    firstName VARCHAR(50) NOT NULL,
    lastName VARCHAR(50) NOT NULL,
    email VARCHAR(50),
    phoneNumber VARCHAR(20)
    );    





insert into Employees (displayName, ACTIVE, PASSWORD, USER_ROLE, firstName, lastName) values ('displayName_Beer', 1, '123', 'EMPLOYEE', 'Beer_1', 'BeerLastName');
insert into Employees (displayName, ACTIVE, PASSWORD, USER_ROLE, firstName, lastName) values ('displayName_B', 1, '123', 'MANAGER', 'B_1', 'BLastName');

