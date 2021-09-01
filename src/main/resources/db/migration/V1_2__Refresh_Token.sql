CREATE TABLE refresh_token
(
   refresh_token_id varchar(50) PRIMARY KEY NOT NULL,
   user_id varchar(50) NOT NULL,
   token varchar(100),
   expiry_date timestamp
);
ALTER TABLE refresh_token
ADD CONSTRAINT FK_rfhtkn_usrid
FOREIGN KEY (user_id)
REFERENCES user_profile(user_id);