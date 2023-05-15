-- MySQL Script generated by MySQL Workbench
-- Mon Aug  1 13:28:21 2022
-- Model: New Model    Version: 1.0
SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema bjmbc
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `bjmbc` ;

-- -----------------------------------------------------
-- Schema bjmbc
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `bjmbc` DEFAULT CHARACTER SET utf8 ;
USE `bjmbc` ;

-- -----------------------------------------------------
-- Table `bjmbc`.`REVENUE_PARTY`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjmbc`.`REVENUE_PARTY` ;

CREATE TABLE IF NOT EXISTS `bjmbc`.`REVENUE_PARTY` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(75) NOT NULL,
  `MEMORABLE_DATE` DATE NOT NULL,
  `ORGANISATION` VARCHAR(75) NOT NULL,
  `OWNER_ADHAAR_NUMBER` CHAR(20) NOT NULL,
  `EMAIL` VARCHAR(50) NOT NULL,
  `PASSWORD` VARCHAR(50) NOT NULL,
  `PARTY_HASH` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bjmbc`.`USER`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjmbc`.`USER` ;

CREATE TABLE IF NOT EXISTS `bjmbc`.`USER` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `EMAIL` VARCHAR(50) NOT NULL,
  `PASSWORD` VARCHAR(50) NOT NULL,
  `FAILED_ATTEMPTS` TINYINT NULL,
  `ACCOUNT_LOCKED` TINYINT NULL,
  `LOCK_TIME` DATETIME NULL,
  `USER_TYPE` VARCHAR(15) NOT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bjmbc`.`REVENUE_CATEGORY`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjmbc`.`REVENUE_CATEGORY` ;

CREATE TABLE IF NOT EXISTS `bjmbc`.`REVENUE_CATEGORY` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `CATEGORY` VARCHAR(25) NOT NULL,
  `DESCRIPTION` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bjmbc`.`REVENUE_ACCOUNT`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjmbc`.`REVENUE_ACCOUNT` ;

CREATE TABLE IF NOT EXISTS `bjmbc`.`REVENUE_ACCOUNT` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(45) NULL,
  `BALANCE` DECIMAL(10,2) NOT NULL DEFAULT 0.00 COMMENT '	',
  `REVENUE_ACCOUNT_HASH` VARCHAR(64) NOT NULL,
  `REVENUE_CATEGORY_ID` INT NOT NULL,
  `REVENUE_PARTY_ID` BIGINT NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_REVENUE_ACCOUNT_REVENUE_CATEGORY1_idx` (`REVENUE_CATEGORY_ID` ASC) VISIBLE,
  INDEX `fk_REVENUE_ACCOUNT_REVENUE_PARTY1_idx` (`REVENUE_PARTY_ID` ASC) VISIBLE,
  CONSTRAINT `fk_REVENUE_ACCOUNT_REVENUE_CATEGORY1`
    FOREIGN KEY (`REVENUE_CATEGORY_ID`)
    REFERENCES `bjmbc`.`REVENUE_CATEGORY` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_REVENUE_ACCOUNT_REVENUE_PARTY1`
    FOREIGN KEY (`REVENUE_PARTY_ID`)
    REFERENCES `bjmbc`.`REVENUE_PARTY` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bjmbc`.`EXPENSE_PARTY`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjmbc`.`EXPENSE_PARTY` ;

CREATE TABLE IF NOT EXISTS `bjmbc`.`EXPENSE_PARTY` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(90) NOT NULL,
  `ORGANISATION` VARCHAR(45) NOT NULL,
  `MEMORABLE_DATE` DATE NOT NULL,
  `OWNER_ADHAAR_NUMBER` VARCHAR(20) NULL,
  `EMAIL` VARCHAR(50) NOT NULL,
  `PASSWORD` VARCHAR(50) NOT NULL,
  `PARTY_HASH` VARCHAR(64) NOT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bjmbc`.`EXPENSE_CATEGORY`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjmbc`.`EXPENSE_CATEGORY` ;

CREATE TABLE IF NOT EXISTS `bjmbc`.`EXPENSE_CATEGORY` (
  `ID` INT NOT NULL AUTO_INCREMENT,
  `CATEGORY` VARCHAR(25) NOT NULL,
  `DESCRIPTION` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bjmbc`.`EXPENSE_ACCOUNT`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjmbc`.`EXPENSE_ACCOUNT` ;

CREATE TABLE IF NOT EXISTS `bjmbc`.`EXPENSE_ACCOUNT` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `NAME` VARCHAR(45) NOT NULL,
  `EXPENSE_PARTY_ID` BIGINT NOT NULL,
  `BALANCE` DECIMAL(10,2) NOT NULL DEFAULT 0.00,
  `EXPENSE_ACCOUNT_HASH` VARCHAR(64) NOT NULL,
  `EXPENSE_CATEGORY_ID` INT NOT NULL,
  PRIMARY KEY (`ID`),
  INDEX `fk_EXPENSE_ACCOUNT_EXPENSE_PARTY1_idx` (`EXPENSE_PARTY_ID` ASC) VISIBLE,
  INDEX `fk_EXPENSE_ACCOUNT_EXPENSE_CATEGORY1_idx` (`EXPENSE_CATEGORY_ID` ASC) VISIBLE,
  CONSTRAINT `fk_EXPENSE_ACCOUNT_EXPENSE_PARTY1`
    FOREIGN KEY (`EXPENSE_PARTY_ID`)
    REFERENCES `bjmbc`.`EXPENSE_PARTY` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_EXPENSE_ACCOUNT_EXPENSE_CATEGORY1`
    FOREIGN KEY (`EXPENSE_CATEGORY_ID`)
    REFERENCES `bjmbc`.`EXPENSE_CATEGORY` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bjmbc`.`CENTRAL_ACCOUNT`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjmbc`.`CENTRAL_ACCOUNT` ;

CREATE TABLE IF NOT EXISTS `bjmbc`.`CENTRAL_ACCOUNT` (
  `ID` BIGINT NOT NULL AUTO_INCREMENT,
  `ACCOUNT_NAME` VARCHAR(50) NOT NULL,
  `CENTRAL_ACCOUNT_HASH` VARCHAR(64) NOT NULL,
  `REVENUE_ACCOUNT_HASH` VARCHAR(64) NULL,
  `AMOUNT` DECIMAL(8,2) NOT NULL,
  `EXPENSE_ACCOUNT_HASH` VARCHAR(64) NULL,
  PRIMARY KEY (`ID`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bjmbc`.`REVENUE_ACCOUNT_TRANSACTION`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjmbc`.`REVENUE_ACCOUNT_TRANSACTION` ;

CREATE TABLE IF NOT EXISTS `bjmbc`.`REVENUE_ACCOUNT_TRANSACTION` (
  `REVENUE_ACCOUNT_ID` BIGINT NOT NULL,
  `CENTRAL_ACCOUNT_ID` BIGINT NOT NULL,
  `AMOUNT` DECIMAL(8,2) NOT NULL,
  `TRANSATION_TIME` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`REVENUE_ACCOUNT_ID`, `CENTRAL_ACCOUNT_ID`),
  INDEX `fk_REVENUE_ACCOUNT_has_CENTRAL_ACCOUNT_CENTRAL_ACCOUNT1_idx` (`CENTRAL_ACCOUNT_ID` ASC) VISIBLE,
  INDEX `fk_REVENUE_ACCOUNT_has_CENTRAL_ACCOUNT_REVENUE_ACCOUNT1_idx` (`REVENUE_ACCOUNT_ID` ASC) VISIBLE,
  CONSTRAINT `fk_REVENUE_ACCOUNT_has_CENTRAL_ACCOUNT_REVENUE_ACCOUNT1`
    FOREIGN KEY (`REVENUE_ACCOUNT_ID`)
    REFERENCES `bjmbc`.`REVENUE_ACCOUNT` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_REVENUE_ACCOUNT_has_CENTRAL_ACCOUNT_CENTRAL_ACCOUNT1`
    FOREIGN KEY (`CENTRAL_ACCOUNT_ID`)
    REFERENCES `bjmbc`.`CENTRAL_ACCOUNT` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bjmbc`.`EXPENSE_ACCOUNT_TRANSACTION`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjmbc`.`EXPENSE_ACCOUNT_TRANSACTION` ;

CREATE TABLE IF NOT EXISTS `bjmbc`.`EXPENSE_ACCOUNT_TRANSACTION` (
  `CENTRAL_ACCOUNT_ID` BIGINT NOT NULL,
  `EXPENSE_ACCOUNT_ID` BIGINT NOT NULL,
  `AMOUNT` DECIMAL(8,2) NOT NULL,
  `TRANSACTION_TIME` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`CENTRAL_ACCOUNT_ID`, `EXPENSE_ACCOUNT_ID`),
  INDEX `fk_CENTRAL_ACCOUNT_has_EXPENSE_ACCOUNT_EXPENSE_ACCOUNT1_idx` (`EXPENSE_ACCOUNT_ID` ASC) VISIBLE,
  INDEX `fk_CENTRAL_ACCOUNT_has_EXPENSE_ACCOUNT_CENTRAL_ACCOUNT1_idx` (`CENTRAL_ACCOUNT_ID` ASC) VISIBLE,
  CONSTRAINT `fk_CENTRAL_ACCOUNT_has_EXPENSE_ACCOUNT_CENTRAL_ACCOUNT1`
    FOREIGN KEY (`CENTRAL_ACCOUNT_ID`)
    REFERENCES `bjmbc`.`CENTRAL_ACCOUNT` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_CENTRAL_ACCOUNT_has_EXPENSE_ACCOUNT_EXPENSE_ACCOUNT1`
    FOREIGN KEY (`EXPENSE_ACCOUNT_ID`)
    REFERENCES `bjmbc`.`EXPENSE_ACCOUNT` (`ID`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `bjmbc`.`EXPENSE_ALLOCATION`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `bjmbc`.`EXPENSE_ALLOCATION` ;

CREATE TABLE IF NOT EXISTS `bjmbc`.`EXPENSE_ALLOCATION` (
  `YEAR` INT NOT NULL,
  `CATEGORY` VARCHAR(45) NOT NULL,
  `ALLOCATION` DECIMAL(8,2) NOT NULL,
  `PERCENT_ALLOCATION` DECIMAL(2,2) NOT NULL,
  PRIMARY KEY (`YEAR`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
