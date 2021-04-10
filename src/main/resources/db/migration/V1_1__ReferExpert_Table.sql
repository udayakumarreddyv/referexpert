CREATE TABLE appointment
(
   appointment_id varchar(255) PRIMARY KEY NOT NULL,
   appointment_from varchar(255) NOT NULL,
   appointment_to varchar(255) NOT NULL,
   date_time varchar(100),
   subject varchar(100),
   reason varchar(500),
   is_accepted varchar(1),
   is_served varchar(1),
   created_timestamp timestamp,
   updated_timestamp timestamp
);
ALTER TABLE appointment
ADD CONSTRAINT FK_appfrm_usrid
FOREIGN KEY (appointment_from)
REFERENCES user_profile(user_id);
ALTER TABLE appointment
ADD CONSTRAINT FK_appto_usrid
FOREIGN KEY (appointment_to)
REFERENCES user_profile(user_id);