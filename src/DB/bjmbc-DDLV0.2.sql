DROP SCHEMA IF EXISTS `bjmbc` ;

CREATE SCHEMA IF NOT EXISTS `bjmbc` DEFAULT CHARACTER SET utf8 ;
USE `bjmbc` ;

-- bjmbc.ACCESS definition

CREATE TABLE `ACCESS` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `EMAIL` varchar(50) NOT NULL,
  `PASSWORD` varchar(50) NOT NULL,
  `FAILED_ATTEMPTS` tinyint(4) DEFAULT NULL,
  `ACCOUNT_LOCKED` tinyint(4) DEFAULT NULL,
  `LOCK_TIME` datetime DEFAULT NULL,
  `ACCESS_TYPE` varchar(15) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;


-- bjmbc.CENTRAL_ACCOUNT definition

CREATE TABLE `CENTRAL_ACCOUNT` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `ACCOUNT_NAME` varchar(50) NOT NULL,
  `CENTRAL_ACCOUNT_HASH` varchar(64) NOT NULL,
  `REVENUE_ACCOUNT_HASH` varchar(64) DEFAULT NULL,
  `AMOUNT` decimal(8,2) NOT NULL,
  `EXPENSE_ACCOUNT_HASH` varchar(64) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;


-- bjmbc.EXPENSE_ALLOCATION definition

CREATE TABLE `EXPENSE_ALLOCATION` (
  `YEAR` int(11) NOT NULL,
  `CATEGORY` varchar(45) NOT NULL,
  `ALLOCATION` decimal(8,2) NOT NULL,
  `PERCENT_ALLOCATION` decimal(2,2) NOT NULL,
  PRIMARY KEY (`YEAR`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;


-- bjmbc.EXPENSE_CATEGORY definition

CREATE TABLE `EXPENSE_CATEGORY` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `EXPENSE_CATEGORY` varchar(45) NOT NULL,
  `YEAR` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=105 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;


-- bjmbc.EXPENSE_PARTY definition

CREATE TABLE `EXPENSE_PARTY` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(90) NOT NULL,
  `ORGANISATION` varchar(45) NOT NULL,
  `MEMORABLE_DATE` date NOT NULL,
  `OWNER_ADHAAR_NUMBER` varchar(20) DEFAULT NULL,
  `EMAIL` varchar(50) NOT NULL,
  `PASSWORD` varchar(50) DEFAULT NULL,
  `PARTY_HASH` varchar(64) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;


-- bjmbc.REVENUE_CATEGORY definition

CREATE TABLE `REVENUE_CATEGORY` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `YEAR` int(11) DEFAULT NULL,
  `REVENUE_CATEGORY` varchar(50) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=73 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;


-- bjmbc.REVENUE_PARTY definition

CREATE TABLE `REVENUE_PARTY` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(75) NOT NULL,
  `MEMORABLE_DATE` date NOT NULL,
  `ORGANISATION` varchar(75) NOT NULL,
  `OWNER_ADHAAR_NUMBER` char(20) NOT NULL,
  `EMAIL` varchar(50) NOT NULL,
  `PASSWORD` varchar(50) DEFAULT NULL,
  `PARTY_HASH` varchar(64) NOT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;


-- bjmbc.REVENUE_ACCOUNT_TXS definition

CREATE TABLE `REVENUE_ACCOUNT_TXS` (
  `YEAR` int(11) NOT NULL,
  `MONEY_IN` decimal(10,0) DEFAULT NULL,
  `MONEY_OUT` decimal(10,0) DEFAULT NULL,
  `YTD_BALANCE` decimal(10,0) DEFAULT NULL,
  `REV_ACCOUNT_ID` int(11) NOT NULL,
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;


-- bjmbc.EXPENSE_ACCOUNT_TXS definition

CREATE TABLE `EXPENSE_ACCOUNT_TXS` (
  `ID` int(11) NOT NULL AUTO_INCREMENT,
  `MONEY_IN` decimal(10,0) DEFAULT NULL,
  `MONEY_OUT` decimal(10,0) DEFAULT NULL,
  `YEAR` int(11) DEFAULT NULL,
  `YTD_BALANCE` decimal(10,0) DEFAULT NULL,
  `EXPENSE_ACCOUNT_ID` int(11) DEFAULT NULL,
  PRIMARY KEY (`ID`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;


-- bjmbc.EXPENSE_ACCOUNT definition

CREATE TABLE `EXPENSE_ACCOUNT` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) NOT NULL,
  `EXPENSE_PARTY_ID` bigint(20) NOT NULL,
  `EXPENSE_ACCOUNT_HASH` varchar(64) NOT NULL,
  `EXPENSE_CATEGORY_ID` int(11) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_EXPENSE_ACCOUNT_EXPENSE_PARTY1_idx` (`EXPENSE_PARTY_ID`),
  KEY `fk_EXPENSE_ACCOUNT_EXPENSE_CATEGORY1_idx` (`EXPENSE_CATEGORY_ID`),
  CONSTRAINT `fk_EXPENSE_ACCOUNT_EXPENSE_CATEGORY1` FOREIGN KEY (`EXPENSE_CATEGORY_ID`) REFERENCES `EXPENSE_CATEGORY` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_EXPENSE_ACCOUNT_EXPENSE_PARTY1` FOREIGN KEY (`EXPENSE_PARTY_ID`) REFERENCES `EXPENSE_PARTY` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;


-- bjmbc.EXPENSE_ACCOUNT_TRANSACTION definition

CREATE TABLE `EXPENSE_ACCOUNT_TRANSACTION` (
  `CENTRAL_ACCOUNT_ID` bigint(20) NOT NULL,
  `EXPENSE_ACCOUNT_ID` bigint(20) NOT NULL,
  `AMOUNT` decimal(8,2) NOT NULL,
  `TRANSACTION_TIME` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`CENTRAL_ACCOUNT_ID`,`EXPENSE_ACCOUNT_ID`),
  KEY `fk_CENTRAL_ACCOUNT_has_EXPENSE_ACCOUNT_EXPENSE_ACCOUNT1_idx` (`EXPENSE_ACCOUNT_ID`),
  KEY `fk_CENTRAL_ACCOUNT_has_EXPENSE_ACCOUNT_CENTRAL_ACCOUNT1_idx` (`CENTRAL_ACCOUNT_ID`),
  CONSTRAINT `fk_CENTRAL_ACCOUNT_has_EXPENSE_ACCOUNT_CENTRAL_ACCOUNT1` FOREIGN KEY (`CENTRAL_ACCOUNT_ID`) REFERENCES `CENTRAL_ACCOUNT` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_CENTRAL_ACCOUNT_has_EXPENSE_ACCOUNT_EXPENSE_ACCOUNT1` FOREIGN KEY (`EXPENSE_ACCOUNT_ID`) REFERENCES `EXPENSE_ACCOUNT` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;


-- bjmbc.REVENUE_ACCOUNT definition

CREATE TABLE `REVENUE_ACCOUNT` (
  `ID` bigint(20) NOT NULL AUTO_INCREMENT,
  `NAME` varchar(45) DEFAULT NULL,
  `REVENUE_ACCOUNT_HASH` varchar(64) NOT NULL,
  `REVENUE_CATEGORY_ID` int(11) NOT NULL,
  `REVENUE_PARTY_ID` bigint(20) NOT NULL,
  PRIMARY KEY (`ID`),
  KEY `fk_REVENUE_ACCOUNT_REVENUE_CATEGORY1_idx` (`REVENUE_CATEGORY_ID`),
  KEY `fk_REVENUE_ACCOUNT_REVENUE_PARTY1_idx` (`REVENUE_PARTY_ID`),
  CONSTRAINT `fk_REVENUE_ACCOUNT_REVENUE_CATEGORY1` FOREIGN KEY (`REVENUE_CATEGORY_ID`) REFERENCES `REVENUE_CATEGORY` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_REVENUE_ACCOUNT_REVENUE_PARTY1` FOREIGN KEY (`REVENUE_PARTY_ID`) REFERENCES `REVENUE_PARTY` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;


-- bjmbc.REVENUE_ACCOUNT_TRANSACTION definition

CREATE TABLE `REVENUE_ACCOUNT_TRANSACTION` (
  `REVENUE_ACCOUNT_ID` bigint(20) NOT NULL,
  `CENTRAL_ACCOUNT_ID` bigint(20) NOT NULL,
  `MONEY_IN` decimal(8,2) NOT NULL,
  `TRANSATION_TIME` datetime NOT NULL DEFAULT current_timestamp(),
  PRIMARY KEY (`REVENUE_ACCOUNT_ID`,`CENTRAL_ACCOUNT_ID`),
  KEY `fk_REVENUE_ACCOUNT_has_CENTRAL_ACCOUNT_CENTRAL_ACCOUNT1_idx` (`CENTRAL_ACCOUNT_ID`),
  KEY `fk_REVENUE_ACCOUNT_has_CENTRAL_ACCOUNT_REVENUE_ACCOUNT1_idx` (`REVENUE_ACCOUNT_ID`),
  CONSTRAINT `fk_REVENUE_ACCOUNT_has_CENTRAL_ACCOUNT_CENTRAL_ACCOUNT1` FOREIGN KEY (`CENTRAL_ACCOUNT_ID`) REFERENCES `CENTRAL_ACCOUNT` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_REVENUE_ACCOUNT_has_CENTRAL_ACCOUNT_REVENUE_ACCOUNT1` FOREIGN KEY (`REVENUE_ACCOUNT_ID`) REFERENCES `REVENUE_ACCOUNT` (`ID`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb3 COLLATE=utf8mb3_general_ci;