


CREATE TABLE USER_TO_ACCOUNT(
  USER_ID NUMBER,
  ACCOUNT_ID NUMBER
);

---------------------------------------------------------------------------
drop table ACCOUNTS;
drop SEQUENCE ACCOUNT_ID_SEQ;
drop TRIGGER ACCOUNT_INCREMENT;

create table ACCOUNTS
(
  ACCOUNT_ID NUMBER not null
    primary key,
  BALANCE NUMBER(8,2),
  STATUS VARCHAR2(20),
  TYPE VARCHAR2(20)
)
/

CREATE SEQUENCE ACCOUNT_ID_SEQ MINVALUE 0 INCREMENT BY 1;
CREATE OR REPLACE
TRIGGER ACCOUNT_INCREMENT
  BEFORE INSERT
  ON ACCOUNTS
  FOR EACH ROW
  BEGIN
      SELECT ACCOUNT_ID_SEQ.nextval INTO :NEW.ACCOUNT_ID FROM DUAL;
  END;
---------------------------------------------------------------------------

CREATE SEQUENCE USER_ID_SEQ MINVALUE 0 INCREMENT BY 1;

CREATE OR REPLACE
TRIGGER USER_INCREMENT
	BEFORE INSERT
	ON USERS
	FOR EACH ROW
	BEGIN
		SELECT USER_ID_SEQ.NEXTVAL INTO :new.USER_ID FROM DUAL;
	END;
/

DROP TABLE USERS;
DROP SEQUENCE USER_ID_SEQ;
DROP TRIGGER USER_INCREMENT;

create table USERS
(
	USER_ID NUMBER not null
		primary key,
	USERNAME VARCHAR2(20)
		unique,
	PASSWORD VARCHAR2(20)
)
/
