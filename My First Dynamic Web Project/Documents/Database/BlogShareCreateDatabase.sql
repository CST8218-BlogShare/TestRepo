-- password creation for root adapted from tutorial @ 
--  http://www.ntu.edu.sg/home/ehchua/programming/sql/MySQL_HowTo.html
/*
select
  TABLE_NAME,COLUMN_NAME,CONSTRAINT_NAME, REFERENCED_TABLE_NAME,REFERENCED_COLUMN_NAME
from INFORMATION_SCHEMA.KEY_COLUMN_USAGE
where
  REFERENCED_TABLE_NAME = '<table>';
  
  from http://stackoverflow.com/questions/201621/how-do-i-see-all-foreign-keys-to-a-table-or-column

*/

-- need to be logged in as blogshareuser.

create database blogsharedata;

use blogsharedata;

-- creating user table
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

ALTER TABLE User MODIFY COLUMN userid INT AUTO_INCREMENT;

-- displaying the attributes of  the user table. 
desc user;


-- creating blog table
CREATE TABLE Blog 
    ( 
     BlogID           INT  NOT NULL , 
     Title            VARCHAR(100)  NOT NULL , 
     CreationDateTime DATETIME  NOT NULL , 
	 isPublic		  BOOL NOT NULL
    ) 
;

ALTER TABLE Blog 
    ADD CONSTRAINT Blog_PK PRIMARY KEY ( BlogID ) ;

ALTER TABLE Blog
ADD CONSTRAINT Blog_Unique_Title UNIQUE(Title); 
    
ALTER TABLE Blog MODIFY COLUMN BlogID INT AUTO_INCREMENT;

-- displaying the attributes of  the blog table. 
desc blog;

-- creating User_Blog table
CREATE TABLE User_Blog 
    ( 
     UserID INT NOT NULL , 
     BlogID INT NOT NULL 
    ) 
;

ALTER TABLE User_Blog 
    ADD CONSTRAINT Users_Blogs_PK PRIMARY KEY ( UserID, BlogID ) ;
	
ALTER TABLE User_Blog 
    ADD CONSTRAINT User_UserBlog_UserID_FK 
	FOREIGN KEY ( UserID ) 
    REFERENCES User( UserID ) 
    ON DELETE CASCADE 
	ON UPDATE CASCADE;
	
ALTER TABLE User_Blog 
    ADD CONSTRAINT Blog_UserBlog_BlogID_FK 
	FOREIGN KEY ( BlogID ) 
    REFERENCES Blog( BlogID ) 
    ON DELETE CASCADE 
	ON UPDATE CASCADE;
	
-- displaying the attributes of the User_Blog table.
desc User_Blog;

CREATE TABLE BlogDeleted
	(
	BlogDeletedId    INT  NOT NULL,
	BlogId           INT  NOT NULL 
    )
;
	
ALTER TABLE BlogDeleted	
	ADD CONSTRAINT BlogDeleted_PK PRIMARY KEY ( BlogDeletedId );

-- creating post table
CREATE TABLE Post 
    ( 
     PostID           INT  NOT NULL , 
     BlogID           INT  NOT NULL , 
     Title            VARCHAR(100)  NOT NULL , 
     Content          LONGTEXT  NOT NULL ,  -- LONGTEXT is equivalent to oracle's CLOB
     CreationDateTime DATETIME  NOT NULL ,
	 isPublic		  BOOL NOT NULL
    ) 
;

ALTER TABLE Post 
    ADD CONSTRAINT Post_PK PRIMARY KEY ( PostID );
	
ALTER TABLE Post MODIFY COLUMN PostID INT AUTO_INCREMENT;

-- displaying the attributes of  the post table. 
ALTER TABLE Post
    ADD CONSTRAINT Blog_Post_BlogID_FK 
	FOREIGN KEY ( BlogID ) 
    REFERENCES Blog( BlogID ) 
    ON DELETE CASCADE 
	ON UPDATE CASCADE;

desc post;

-- creating User_Post table
CREATE TABLE User_Post 
    ( 
     UserID INT  NOT NULL , 
     PostID INT  NOT NULL 
    ) 
;

ALTER TABLE User_Post 
    ADD CONSTRAINT User_Post_PK PRIMARY KEY ( UserID, PostID ) ;
	
ALTER TABLE User_Post
    ADD CONSTRAINT User_UserPost_UserID_FK 
	FOREIGN KEY ( UserID ) 
    REFERENCES User( UserID ) 
    ON DELETE CASCADE 
	ON UPDATE CASCADE;
	
ALTER TABLE User_Post
    ADD CONSTRAINT Post_UserPost_PostID_FK 
	FOREIGN KEY ( PostID ) 
    REFERENCES Post( PostID ) 
    ON DELETE CASCADE 
	ON UPDATE CASCADE;

-- displaying the attributes of the User_Post table.
desc User_Post;

CREATE TABLE PostDeleted
	(
	PostDeletedId    INT  NOT NULL,
	PostID           INT  NOT NULL 
    )
;
	
ALTER TABLE PostDeleted	
	ADD CONSTRAINT PostDeleted_PK PRIMARY KEY ( PostDeletedId );

desc PostDeleted;
	
-- creating PostEdit table
CREATE TABLE PostEdit 
    ( 
     PostEditID         INT  NOT NULL , 
     PostID             INT , 
     EditDateTime       DATETIME  NOT NULL , 
     TitleBeforeEdit    VARCHAR(100) NOT NULL ,  
     ContentBeforeEdit  LONGTEXT  NOT NULL -- LONGTEXT is equivalent to oracle's CLOB
    ) 
;

ALTER TABLE PostEdit 
    ADD CONSTRAINT PostEdit_PK PRIMARY KEY ( PostEditID ) ;
	
ALTER TABLE PostEdit MODIFY COLUMN PostEditID INT AUTO_INCREMENT;

ALTER TABLE PostEdit
    ADD CONSTRAINT Post_PostEdit_PostID_FK 
	FOREIGN KEY ( PostID ) 
    REFERENCES Post( PostID ) 
    ON DELETE CASCADE 
	ON UPDATE CASCADE;

-- displaying the attributes of the PostEdit table.
desc PostEdit;

-- creating User_PostEdit table
CREATE TABLE User_PostEdit 
    ( 
     UserID     INT  NOT NULL , 
     PostEditID INT  NOT NULL 
    ) 
;

ALTER TABLE User_PostEdit 
    ADD CONSTRAINT User_PostEdit_PK PRIMARY KEY ( UserID, PostEditID ) ;
	
ALTER TABLE User_PostEdit
    ADD CONSTRAINT User_PostEdit_UserID_FK 
	FOREIGN KEY ( UserID ) 
    REFERENCES User( UserID ) 
    ON DELETE CASCADE 
	ON UPDATE CASCADE;
	
ALTER TABLE User_PostEdit
    ADD CONSTRAINT PostEdit_User_PostEditID_FK 
	FOREIGN KEY ( PostEditID ) 
    REFERENCES PostEdit( PostEditID ) 
    ON DELETE CASCADE 
	ON UPDATE CASCADE;
	

-- displaying the attributes of the User_Post table.
desc User_PostEdit;

-- creating PostEditPrivilege table
CREATE TABLE PostEditPrivilege 
    ( 
      PostEditPrivilegeID INT  NOT NULL 
    ) 
;

ALTER TABLE PostEditPrivilege 
    ADD CONSTRAINT PostEditPrivilege_PK PRIMARY KEY ( PostEditPrivilegeID ) ;
	
ALTER TABLE PostEditPrivilege MODIFY COLUMN PostEditPrivilegeID  INT AUTO_INCREMENT;

-- displaying the attributes of the PostEditPrivilege table.
desc PostEditPrivilege;


-- creating User_PostEditPrivilege table
CREATE TABLE User_PostEditPrivilege 
    ( 
     UserID              INT  NOT NULL , 
     PostEditPrivilegeID INT  NOT NULL 
    ) 
;

ALTER TABLE User_PostEditPrivilege 
    ADD CONSTRAINT User_PostEditPrivilege_PK PRIMARY KEY ( UserID, PostEditPrivilegeID );
	
ALTER TABLE User_PostEditPrivilege
    ADD CONSTRAINT User_PostEditPrivilege_UserID_FK 
	FOREIGN KEY ( UserID ) 
    REFERENCES User( UserID ) 
    ON DELETE CASCADE 
	ON UPDATE CASCADE;
	
ALTER TABLE User_PostEditPrivilege
    ADD CONSTRAINT PostEditPrivilege_User_PostEditPrivilegeID_FK 
	FOREIGN KEY ( PostEditPrivilegeID ) 
    REFERENCES PostEditPrivilege( PostEditPrivilegeID ) 
    ON DELETE CASCADE 
	ON UPDATE CASCADE;	
	
-- displaying the attributes of the User_PostEditPrivilege table.
desc User_PostEditPrivilege;


-- creating Post_PostEditPrivilege table
 CREATE TABLE Post_PostEditPrivilege 
    ( 
     PostID              INT  NOT NULL , 
     PostEditPrivilegeID INT  NOT NULL 
    ) 
;

ALTER TABLE Post_PostEditPrivilege 
    ADD CONSTRAINT Post_PostEditPrivilege_PK PRIMARY KEY ( PostID, PostEditPrivilegeID ) ;
	
ALTER TABLE Post_PostEditPrivilege
    ADD CONSTRAINT Post_PostEditPrivilege_PostID_FK 
	FOREIGN KEY ( PostID ) 
    REFERENCES Post( PostID ) 
    ON DELETE CASCADE 
	ON UPDATE CASCADE;
	
ALTER TABLE Post_PostEditPrivilege
    ADD CONSTRAINT PostEditPrivilege_Post_PostEditPrivilegeID_FK 
	FOREIGN KEY ( PostEditPrivilegeID ) 
    REFERENCES PostEditPrivilege( PostEditPrivilegeID ) 
    ON DELETE CASCADE 
	ON UPDATE CASCADE;	

-- displaying the attributes of the Post_PostEditPrivilege table.	
desc Post_PostEditPrivilege;

-- displaying all tables within current database (should be 10)
show tables;

-- displaying foreign keys. (there should be 12)
select
  CONSTRAINT_NAME, TABLE_NAME
from INFORMATION_SCHEMA.KEY_COLUMN_USAGE
where
  CONSTRAINT_NAME LIKE '%fk%';


