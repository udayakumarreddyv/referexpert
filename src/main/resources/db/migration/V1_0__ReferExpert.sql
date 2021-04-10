CREATE TABLE user_type
(
   user_type_id bigint PRIMARY KEY NOT NULL,
   user_type varchar(50),
   created_timestamp timestamp,
   updated_timestamp timestamp
);

insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(1, 'ADMIN',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(2, 'PHYSICIAN',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(3, 'SPECIALIST',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
COMMIT;

CREATE TABLE user_speciality
(
   user_speciality_id bigint PRIMARY KEY NOT NULL,
   user_speciality varchar(50),
   user_type_id bigint,
   created_timestamp timestamp,
   updated_timestamp timestamp
);

ALTER TABLE user_speciality
ADD CONSTRAINT fk_us_spl_usr_id
FOREIGN KEY (user_type_id)
REFERENCES user_type(user_type_id);

insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(1, 'ADMIN',(select user_type_id from user_type where user_type = 'ADMIN'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(2, 'PHYSICIAN1',(select user_type_id from user_type where user_type = 'PHYSICIAN'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(3, 'PHYSICIAN2',(select user_type_id from user_type where user_type = 'PHYSICIAN'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(4, 'SPECIALIST1',(select user_type_id from user_type where user_type = 'SPECIALIST'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(5, 'SPECIALIST2',(select user_type_id from user_type where user_type = 'SPECIALIST'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
COMMIT;

CREATE TABLE user_profile
(
   user_id varchar(255) PRIMARY KEY NOT NULL,
   first_name varchar(255),
   last_name varchar(255),
   email varchar(255),
   password varchar(255),
   user_type_id bigint,
   user_speciality_id bigint,
   address varchar(255),
   city varchar(50),
   state varchar(25),
   zip varchar(15),
   phone varchar(20),
   fax varchar(20),
   is_active varchar(1),
   created_timestamp timestamp,
   updated_timestamp timestamp
);
CREATE UNIQUE INDEX UK_USR_EML ON user_profile(email);
ALTER TABLE user_profile
ADD CONSTRAINT fk_up_usr_typ_id
FOREIGN KEY (user_type_id)
REFERENCES user_type(user_type_id);
ALTER TABLE user_profile
ADD CONSTRAINT fk_up_usr_spl_id
FOREIGN KEY (user_speciality_id)
REFERENCES user_speciality(user_speciality_id);

INSERT INTO user_profile (user_id,first_name,last_name,email,password,user_type_id,user_speciality_id,address,city,state,zip,phone,fax,is_active,created_timestamp,updated_timestamp) VALUES ('manual_insert','Uday','Reddy','udayakumarreddyv@gmail.com','test*987',1,1,'1234 AND LN','Atlanta','Georgia','33333','999999999','999999999','Y',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
INSERT INTO user_profile (user_id,first_name,last_name,email,password,user_type_id,user_speciality_id,address,city,state,zip,phone,fax,is_active,created_timestamp,updated_timestamp) VALUES ('manual_insert1','Andrew','Elick','your_gmail@gmail.com','test*987',2,2,'1234 AND LN','Atlanta','Georgia','33333','999999999','999999999','Y',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
INSERT INTO user_profile (user_id,first_name,last_name,email,password,user_type_id,user_speciality_id,address,city,state,zip,phone,fax,is_active,created_timestamp,updated_timestamp) VALUES ('manual_insert2','Yuvaraj','Thakur','your_gmail1@gmail.com','test*987',3,4,'1234 AND LN','Atlanta','Georgia','33333','999999999','999999999','Y',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
COMMIT;

CREATE TABLE confirmation_token
(
   token_id varchar(255) PRIMARY KEY NOT NULL,
   confirmation_token varchar(255),
   created_timestamp timestamp,
   user_id varchar(255) NOT NULL
);
ALTER TABLE confirmation_token
ADD CONSTRAINT FK_cnf_usrid
FOREIGN KEY (user_id)
REFERENCES user_profile(user_id);
CREATE INDEX IDX_CNF_USR_ID ON confirmation_token(user_id);

CREATE TABLE user_referral
(
   user_referral_id varchar(255) PRIMARY KEY NOT NULL,
   user_email varchar(255),
   doc_email varchar(255),
   is_registered varchar(1),
   created_timestamp timestamp,
   updated_timestamp timestamp
);

CREATE INDEX IDX_REF_USR_EML ON user_referral(user_email);