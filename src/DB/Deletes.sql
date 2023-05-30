use `bjmbc`;

UPDATE EXPENSE_ACCOUNT SET YTD_BALANCE=1000;

DELETE from EXPENSE_ACCOUNT_TRANSACTION;

alter table EXPENSE_ACCOUNT_TRANSACTION auto_increment = 1;

delete from EXPENSE_ACCOUNT;

alter table EXPENSE_ACCOUNT auto_increment = 1;

delete from EXPENSE_PARTY;

alter table EXPENSE_PARTY auto_increment = 1;

DELETE from REVENUE_ACCOUNT_TRANSACTION;

alter table REVENUE_ACCOUNT_TRANSACTION auto_increment = 1;

delete from REVENUE_ACCOUNT;

alter table REVENUE_ACCOUNT auto_increment = 1;

delete from REVENUE_PARTY;

alter table REVENUE_PARTY auto_increment = 1;

DELETE FROM ACCESS;

alter table ACCESS auto_increment = 1;

delete from REVENUE_PARTY where PASSWORD is NULL;