-- Generated by Oracle SQL Developer Data Modeler 4.0.2.840
--   at:        2014-09-22 16:27:14 EDT
--   site:      Oracle Database 11g
--   type:      Oracle Database 11g
-- this code is in oracle format, needs to be converted to match MySQL syntax.

-- see user table for example.




CREATE TABLE Blog 
    ( 
     BlogID           NUMBER  NOT NULL , 
     Title            VARCHAR2 (100 CHAR)  NOT NULL , 
     CreationDateTime DATE  NOT NULL 
    ) 
;



ALTER TABLE Blog 
    ADD CONSTRAINT Blog_PK PRIMARY KEY ( BlogID ) ;




CREATE TABLE Post 
    ( 
     PostID           NUMBER  NOT NULL , 
     BlogID           NUMBER  NOT NULL , 
     Title            VARCHAR2 (100 CHAR)  NOT NULL , 
     Content          CLOB  NOT NULL , 
     CreationDateTime DATE  NOT NULL 
    ) 
;



ALTER TABLE Post 
    ADD CONSTRAINT Post_PK PRIMARY KEY ( PostID ) ;




CREATE TABLE PostEdit 
    ( 
     EditID            NUMBER  NOT NULL , 
     PostID            NUMBER , 
     EditDateTime      DATE  NOT NULL , 
     ContentBeforeEdit CLOB  NOT NULL 
    ) 
;



ALTER TABLE PostEdit 
    ADD CONSTRAINT PostEdit_PK PRIMARY KEY ( EditID ) ;




CREATE TABLE PostEditPrivilege 
    ( 
     PostEditPrivilegeID NUMBER  NOT NULL 
    ) 
;



ALTER TABLE PostEditPrivilege 
    ADD CONSTRAINT PostEditPrivilege_PK PRIMARY KEY ( PostEditPrivilegeID ) ;




CREATE TABLE Post_PostEditPrivilege 
    ( 
     PostID              NUMBER  NOT NULL , 
     PostEditPrivilegeID NUMBER  NOT NULL 
    ) 
;



ALTER TABLE Post_PostEditPrivilege 
    ADD CONSTRAINT Post_PostEditPrivilege_PK PRIMARY KEY ( PostID, PostEditPrivilegeID ) ;




CREATE TABLE User 
    ( 
     UserID         INT  NOT NULL , 
     Username       VARCHAR (100)  NOT NULL , 
     Password       VARCHAR (100)  NOT NULL , 
     DateRegistered DATE  NOT NULL 
    ) 
;



ALTER TABLE User
    ADD CONSTRAINT User_PK PRIMARY KEY ( UserID );
	
ALTER TABLE User
ADD CONSTRAINT User_Unique_Username UNIQUE(Username);

ALTER TABLE User MODIFY COLUMN userid INT auto_increment;




CREATE TABLE User_Blog 
    ( 
     UserID NUMBER  NOT NULL , 
     BlogID NUMBER  NOT NULL 
    ) 
;



ALTER TABLE User_Blog 
    ADD CONSTRAINT Users_Blogs_PK PRIMARY KEY ( UserID, BlogID ) ;




CREATE TABLE User_Post 
    ( 
     UserID NUMBER  NOT NULL , 
     PostID NUMBER  NOT NULL 
    ) 
;



ALTER TABLE User_Post 
    ADD CONSTRAINT User_Post_PK PRIMARY KEY ( UserID, PostID ) ;




CREATE TABLE User_PostEdit 
    ( 
     UserID     NUMBER  NOT NULL , 
     PostEditID NUMBER  NOT NULL 
    ) 
;



ALTER TABLE User_PostEdit 
    ADD CONSTRAINT User_PostEdit_PK PRIMARY KEY ( UserID, PostEditID ) ;




CREATE TABLE User_PostEditPrivilege 
    ( 
     UserID              NUMBER  NOT NULL , 
     PostEditPrivilegeID NUMBER  NOT NULL 
    ) 
;



ALTER TABLE User_PostEditPrivilege 
    ADD CONSTRAINT User_PostEditPrivilege_PK PRIMARY KEY ( UserID, PostEditPrivilegeID ) ;





ALTER TABLE User_Blog 
    ADD CONSTRAINT Blog_UserBlog_BlogID_FK FOREIGN KEY 
    ( 
     BlogID
    ) 
    REFERENCES Blog 
    ( 
     BlogID
    ) 
    ON DELETE CASCADE 
;




--  ERROR: FK name length exceeds maximum allowed length(30) 
ALTER TABLE Post_PostEditPrivilege 
    ADD CONSTRAINT PostEditPrivilege_Post_PostEditPrivilegeID_FK FOREIGN KEY 
    ( 
     PostEditPrivilegeID
    ) 
    REFERENCES PostEditPrivilege 
    ( 
     PostEditPrivilegeID
    ) 
    ON DELETE CASCADE 
;




--  ERROR: FK name length exceeds maximum allowed length(30) 
ALTER TABLE User_PostEditPrivilege 
    ADD CONSTRAINT PostEditPrivilege_User_PostEditPrivilegeID_FK FOREIGN KEY 
    ( 
     PostEditPrivilegeID
    ) 
    REFERENCES PostEditPrivilege 
    ( 
     PostEditPrivilegeID
    ) 
    ON DELETE CASCADE 
;



ALTER TABLE User_PostEdit 
    ADD CONSTRAINT PostEdit_User_PostEditID_FK FOREIGN KEY 
    ( 
     PostEditID
    ) 
    REFERENCES PostEdit 
    ( 
     EditID
    ) 
    ON DELETE CASCADE 
;



ALTER TABLE Post 
    ADD CONSTRAINT Post_Blog_FK FOREIGN KEY 
    ( 
     BlogID
    ) 
    REFERENCES Blog 
    ( 
     BlogID
    ) 
;




--  ERROR: FK name length exceeds maximum allowed length(30) 
ALTER TABLE Post_PostEditPrivilege 
    ADD CONSTRAINT Post_PostEditPrivilege_PostID_FK FOREIGN KEY 
    ( 
     PostID
    ) 
    REFERENCES Post 
    ( 
     PostID
    ) 
    ON DELETE CASCADE 
;



ALTER TABLE PostEdit 
    ADD CONSTRAINT Post_PostEdit_PostID_FK FOREIGN KEY 
    ( 
     PostID
    ) 
    REFERENCES Post 
    ( 
     PostID
    ) 
    ON DELETE CASCADE 
;



ALTER TABLE User_Post 
    ADD CONSTRAINT Post_UserPost_PostID_FK FOREIGN KEY 
    ( 
     PostID
    ) 
    REFERENCES Post 
    ( 
     PostID
    ) 
    ON DELETE CASCADE 
;




--  ERROR: FK name length exceeds maximum allowed length(30) 
ALTER TABLE User_PostEditPrivilege 
    ADD CONSTRAINT User_PostEditPrivilege_UserID_FK FOREIGN KEY 
    ( 
     UserID
    ) 
    REFERENCES "User" 
    ( 
     UserID
    ) 
    ON DELETE CASCADE 
;



ALTER TABLE User_PostEdit 
    ADD CONSTRAINT User_PostEdit_UserID_FK FOREIGN KEY 
    ( 
     UserID
    ) 
    REFERENCES "User" 
    ( 
     UserID
    ) 
    ON DELETE CASCADE 
;



ALTER TABLE User_Blog 
    ADD CONSTRAINT User_UserBlog_UserID_FK FOREIGN KEY 
    ( 
     UserID
    ) 
    REFERENCES "User" 
    ( 
     UserID
    ) 
    ON DELETE CASCADE 
;



ALTER TABLE User_Post 
    ADD CONSTRAINT User_UserPost_UserID_FK FOREIGN KEY 
    ( 
     UserID
    ) 
    REFERENCES "User" 
    ( 
     UserID
    ) 
    ON DELETE CASCADE 
;


CREATE OR REPLACE TRIGGER FKNTM_Post 
BEFORE UPDATE OF BlogID 
ON Post 
BEGIN 
  raise_application_error(-20225,'Non Transferable FK constraint  on table Post is violated'); 
END; 
/


CREATE OR REPLACE TRIGGER FKNTO_PostEdit 
BEFORE UPDATE OF PostID 
ON PostEdit 
FOR EACH ROW 
BEGIN 
 IF :old.PostID IS NOT NULL THEN 
  raise_application_error(-20225,'Non Transferable FK constraint Post_PostEdit_PostID_FK on table PostEdit is violated'); 
 END IF; 
END; 
/


CREATE OR REPLACE TRIGGER FKNTM_Post_PostEditPrivilege 
BEFORE UPDATE OF PostEditPrivilegeID, PostID 
ON Post_PostEditPrivilege 
BEGIN 
  raise_application_error(-20225,'Non Transferable FK constraint  on table Post_PostEditPrivilege is violated'); 
END; 
/


CREATE OR REPLACE TRIGGER FKNTM_User_Blog 
BEFORE UPDATE OF UserID, BlogID 
ON User_Blog 
BEGIN 
  raise_application_error(-20225,'Non Transferable FK constraint  on table User_Blog is violated'); 
END; 
/


CREATE OR REPLACE TRIGGER FKNTM_User_Post 
BEFORE UPDATE OF PostID, UserID 
ON User_Post 
BEGIN 
  raise_application_error(-20225,'Non Transferable FK constraint  on table User_Post is violated'); 
END; 
/


CREATE OR REPLACE TRIGGER FKNTM_User_PostEdit 
BEFORE UPDATE OF UserID, PostEditID 
ON User_PostEdit 
BEGIN 
  raise_application_error(-20225,'Non Transferable FK constraint  on table User_PostEdit is violated'); 
END; 
/


CREATE OR REPLACE TRIGGER FKNTM_User_PostEditPrivilege 
BEFORE UPDATE OF PostEditPrivilegeID, UserID 
ON User_PostEditPrivilege 
BEGIN 
  raise_application_error(-20225,'Non Transferable FK constraint  on table User_PostEditPrivilege is violated'); 
END; 
/


CREATE SEQUENCE Blog_BlogID_SEQ 
START WITH 1 
    NOCACHE 
    ORDER ;

CREATE OR REPLACE TRIGGER Blog_BlogID_TRG 
BEFORE INSERT ON Blog 
FOR EACH ROW 
WHEN (NEW.BlogID IS NULL) 
BEGIN 
    :NEW.BlogID := Blog_BlogID_SEQ.NEXTVAL; 
END;
/


CREATE SEQUENCE Post_PostID_SEQ 
START WITH 1 
    NOCACHE 
    ORDER ;

CREATE OR REPLACE TRIGGER Post_PostID_TRG 
BEFORE INSERT ON Post 
FOR EACH ROW 
WHEN (NEW.PostID IS NULL) 
BEGIN 
    :NEW.PostID := Post_PostID_SEQ.NEXTVAL; 
END;
/


CREATE SEQUENCE PostEdit_EditID_SEQ 
START WITH 1 
    NOCACHE 
    ORDER ;

CREATE OR REPLACE TRIGGER PostEdit_EditID_TRG 
BEFORE INSERT ON PostEdit 
FOR EACH ROW 
WHEN (NEW.EditID IS NULL) 
BEGIN 
    :NEW.EditID := PostEdit_EditID_SEQ.NEXTVAL; 
END;
/


CREATE SEQUENCE PostEditPrivilege_PostEditPriv 
START WITH 1 
    NOCACHE 
    ORDER ;

CREATE OR REPLACE TRIGGER PostEditPrivilege_PostEditPriv 
BEFORE INSERT ON PostEditPrivilege 
FOR EACH ROW 
WHEN (NEW.PostEditPrivilegeID IS NULL) 
BEGIN 
    :NEW.PostEditPrivilegeID := PostEditPrivilege_PostEditPriv.NEXTVAL; 
END;
/


CREATE SEQUENCE User_UserID_SEQ 
START WITH 1 
    NOCACHE 
    ORDER ;

CREATE OR REPLACE TRIGGER User_UserID_TRG 
BEFORE INSERT ON "User" 
FOR EACH ROW 
WHEN (NEW.UserID IS NULL) 
BEGIN 
    :NEW.UserID := User_UserID_SEQ.NEXTVAL; 
END;
/



-- Oracle SQL Developer Data Modeler Summary Report: 
-- 
-- CREATE TABLE                            10
-- CREATE INDEX                             0
-- ALTER TABLE                             22
-- CREATE VIEW                              0
-- CREATE PACKAGE                           0
-- CREATE PACKAGE BODY                      0
-- CREATE PROCEDURE                         0
-- CREATE FUNCTION                          0
-- CREATE TRIGGER                          12
-- ALTER TRIGGER                            0
-- CREATE COLLECTION TYPE                   0
-- CREATE STRUCTURED TYPE                   0
-- CREATE STRUCTURED TYPE BODY              0
-- CREATE CLUSTER                           0
-- CREATE CONTEXT                           0
-- CREATE DATABASE                          0
-- CREATE DIMENSION                         0
-- CREATE DIRECTORY                         0
-- CREATE DISK GROUP                        0
-- CREATE ROLE                              0
-- CREATE ROLLBACK SEGMENT                  0
-- CREATE SEQUENCE                          5
-- CREATE MATERIALIZED VIEW                 0
-- CREATE SYNONYM                           0
-- CREATE TABLESPACE                        0
-- CREATE USER                              0
-- 
-- DROP TABLESPACE                          0
-- DROP DATABASE                            0
-- 
-- REDACTION POLICY                         0
-- 
-- ERRORS                                   4
-- WARNINGS                                 0
