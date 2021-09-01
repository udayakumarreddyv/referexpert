CREATE TABLE user_notification
(
   user_id varchar(50) PRIMARY KEY NOT NULL,
   notification_email varchar(255),
   notification_mobile varchar(50),
   created_timestamp timestamp,
   updated_timestamp timestamp
);
ALTER TABLE user_notification
ADD CONSTRAINT FK_usrntf_usrid
FOREIGN KEY (user_id)
REFERENCES user_profile(user_id);
CREATE INDEX IDX_USRNTF_USR_ID ON user_notification(user_id);