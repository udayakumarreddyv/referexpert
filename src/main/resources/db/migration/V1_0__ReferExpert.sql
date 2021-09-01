CREATE TABLE user_type
(
   user_type_id bigint PRIMARY KEY NOT NULL,
   user_type varchar(50),
   created_timestamp timestamp,
   updated_timestamp timestamp
);

insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(10, 'ADMIN',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(11, 'Allergy-Immunology',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(12, 'Anesthesiology',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(13, 'Cardiology',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(14, 'Dermatology',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(15, 'Endocrinology-Diabetes-Metabolism',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(16, 'Emergency Medicine',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(17, 'Family-General Practice',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(18, 'Geriatrics',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(19, 'Internal Medicine',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(20, 'Medical Genetics',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(21, 'Neurological Surgery',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(22, 'Neurology',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(23, 'Obstetrics-Gynecology',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(24, 'Oncology (Cancer)',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(25, 'Ophthalmology',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(26, 'Orthopedics',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(27, 'Otolaryngology',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(28, 'Pathology',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(29, 'Pediatrics',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(30, 'Physical Medicine And Rehab',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(31, 'Plastic Surgery',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(32, 'Preventive Medicine',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(33, 'Psychiatry',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(34, 'Radiology',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(35, 'Surgery',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(36, 'Urology',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_type (user_type_id, user_type, created_timestamp, updated_timestamp) values(37, 'Other',CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
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

insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(10, 'ADMIN',(select user_type_id from user_type where user_type = 'ADMIN'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(11, 'Allergy',(select user_type_id from user_type where user_type = 'Allergy-Immunology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(12, 'Allergy-Immunology',(select user_type_id from user_type where user_type = 'Allergy-Immunology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(13, 'Clinical Lab Immunology And Allergy',(select user_type_id from user_type where user_type = 'Allergy-Immunology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(14, 'Immunology',(select user_type_id from user_type where user_type = 'Allergy-Immunology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(15, 'Anesthesiology',(select user_type_id from user_type where user_type = 'Anesthesiology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(16, 'Critical Care Medicine',(select user_type_id from user_type where user_type = 'Anesthesiology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(17, 'Pain Management',(select user_type_id from user_type where user_type = 'Anesthesiology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(18, 'Cardiac Electrophysiology',(select user_type_id from user_type where user_type = 'Cardiology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(19, 'Cardiovascular Diseases',(select user_type_id from user_type where user_type = 'Cardiology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(20, 'Interventional Cardiology',(select user_type_id from user_type where user_type = 'Cardiology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(21, 'Nuclear Cardiology',(select user_type_id from user_type where user_type = 'Cardiology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(22, 'Pediatric Cardiology',(select user_type_id from user_type where user_type = 'Cardiology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(23, 'Clinical And Lab Derm Immunology',(select user_type_id from user_type where user_type = 'Dermatology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(24, 'Dermatology',(select user_type_id from user_type where user_type = 'Dermatology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(25, 'Dermatopathology',(select user_type_id from user_type where user_type = 'Dermatology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(26, 'Pediatric Dermatology',(select user_type_id from user_type where user_type = 'Dermatology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(27, 'Diabetes',(select user_type_id from user_type where user_type = 'Endocrinology-Diabetes-Metabolism'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(28, 'Endocrinology',(select user_type_id from user_type where user_type = 'Endocrinology-Diabetes-Metabolism'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(29, 'Emergency Medicine',(select user_type_id from user_type where user_type = 'Emergency Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(30, 'Medical Toxicology',(select user_type_id from user_type where user_type = 'Emergency Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(31, 'Pediatric Emergency Medicine',(select user_type_id from user_type where user_type = 'Emergency Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(32, 'Sports Medicine- EM',(select user_type_id from user_type where user_type = 'Emergency Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(33, 'Adolescent Medicine',(select user_type_id from user_type where user_type = 'Family-General Practice'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(34, 'Family Practice',(select user_type_id from user_type where user_type = 'Family-General Practice'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(35, 'General Practice',(select user_type_id from user_type where user_type = 'Family-General Practice'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(36, 'Sports Medicine- FP',(select user_type_id from user_type where user_type = 'Family-General Practice'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(37, 'Geriatric Medicine-FP',(select user_type_id from user_type where user_type = 'Geriatrics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(38, 'Geriatric Medicine-IM',(select user_type_id from user_type where user_type = 'Geriatrics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(39, 'Geriatric Psychiatry',(select user_type_id from user_type where user_type = 'Geriatrics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(40, 'Adolescent Medicine',(select user_type_id from user_type where user_type = 'Internal Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(41, 'Clinical-Laboratory Immunology',(select user_type_id from user_type where user_type = 'Internal Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(42, 'Critical Care Medicine',(select user_type_id from user_type where user_type = 'Internal Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(43, 'Gastroenterology',(select user_type_id from user_type where user_type = 'Internal Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(44, 'Hematology',(select user_type_id from user_type where user_type = 'Internal Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(45, 'Hepatology',(select user_type_id from user_type where user_type = 'Internal Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(46, 'Infectious Disease',(select user_type_id from user_type where user_type = 'Internal Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(47, 'Internal Medicine',(select user_type_id from user_type where user_type = 'Internal Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(48, 'IM-Pediatrics',(select user_type_id from user_type where user_type = 'Internal Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(49, 'Nephrology',(select user_type_id from user_type where user_type = 'Internal Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(50, 'Nutrition',(select user_type_id from user_type where user_type = 'Internal Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(51, 'Pulmonary Critical Care Medicine',(select user_type_id from user_type where user_type = 'Internal Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(52, 'Pulmonary Disease',(select user_type_id from user_type where user_type = 'Internal Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(53, 'Rheumatology',(select user_type_id from user_type where user_type = 'Internal Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(54, 'Sports Medicine- IM',(select user_type_id from user_type where user_type = 'Internal Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(55, 'Vascular Medicine',(select user_type_id from user_type where user_type = 'Internal Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(56, 'Clinical Biochemical Genetics',(select user_type_id from user_type where user_type = 'Medical Genetics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(57, 'Clinical Cytogenetics',(select user_type_id from user_type where user_type = 'Medical Genetics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(58, 'Clinical Genetics',(select user_type_id from user_type where user_type = 'Medical Genetics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(59, 'Clinical Molecular Genetics',(select user_type_id from user_type where user_type = 'Medical Genetics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(60, 'Medical Genetics',(select user_type_id from user_type where user_type = 'Medical Genetics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(61, 'Molecular Genetic Pathology',(select user_type_id from user_type where user_type = 'Medical Genetics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(62, 'Neurological Surgery',(select user_type_id from user_type where user_type = 'Neurological Surgery'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(63, 'Pediatric Surgery- Neurological',(select user_type_id from user_type where user_type = 'Neurological Surgery'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(64, 'Child Neurology',(select user_type_id from user_type where user_type = 'Neurology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(65, 'Clinical Neurophysiology',(select user_type_id from user_type where user_type = 'Neurology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(66, 'Neurology',(select user_type_id from user_type where user_type = 'Neurology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(67, 'Neurology-Diag Rad-Neuroradiology',(select user_type_id from user_type where user_type = 'Neurology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(68, 'Neurodevelopmental Disabilities',(select user_type_id from user_type where user_type = 'Neurology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(69, 'Vascular Neurology',(select user_type_id from user_type where user_type = 'Neurology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(70, 'Gynecology',(select user_type_id from user_type where user_type = 'Obstetrics-Gynecology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(71, 'Obstetrics',(select user_type_id from user_type where user_type = 'Obstetrics-Gynecology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(72, 'Obstetrics And Gynecology',(select user_type_id from user_type where user_type = 'Obstetrics-Gynecology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(73, 'Obstetric Critical Care Medicine',(select user_type_id from user_type where user_type = 'Obstetrics-Gynecology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(74, 'Maternal And Fetal Medicine',(select user_type_id from user_type where user_type = 'Obstetrics-Gynecology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(75, 'Reproductive Endocrinology',(select user_type_id from user_type where user_type = 'Obstetrics-Gynecology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(76, 'Gynecological Oncology',(select user_type_id from user_type where user_type = 'Oncology (Cancer)'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(77, 'Hematology-Oncology',(select user_type_id from user_type where user_type = 'Oncology (Cancer)'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(78, 'Medical Oncology',(select user_type_id from user_type where user_type = 'Oncology (Cancer)'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(79, 'Musculoskeletal Oncology',(select user_type_id from user_type where user_type = 'Oncology (Cancer)'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(80, 'Pediatric Hematology-Oncology',(select user_type_id from user_type where user_type = 'Oncology (Cancer)'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(81, 'Surgical Oncology',(select user_type_id from user_type where user_type = 'Oncology (Cancer)'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(82, 'Ophthalmology',(select user_type_id from user_type where user_type = 'Ophthalmology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(83, 'Pediatric Ophthalmology',(select user_type_id from user_type where user_type = 'Ophthalmology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(84, 'Adult Reconstructive Orthopedics',(select user_type_id from user_type where user_type = 'Orthopedics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(85, 'Foot And Ankle Orthopedics',(select user_type_id from user_type where user_type = 'Orthopedics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(86, 'Orthopedic Surgery',(select user_type_id from user_type where user_type = 'Orthopedics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(87, 'Orthopedic Trauma Surgery',(select user_type_id from user_type where user_type = 'Orthopedics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(88, 'Pediatric Orthopedics',(select user_type_id from user_type where user_type = 'Orthopedics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(89, 'Spine Surgery',(select user_type_id from user_type where user_type = 'Orthopedics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(90, 'Sports Medicine- Orthopedics',(select user_type_id from user_type where user_type = 'Orthopedics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(91, 'Otology-Neurotology',(select user_type_id from user_type where user_type = 'Otolaryngology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(92, 'Otolaryngology',(select user_type_id from user_type where user_type = 'Otolaryngology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(93, 'Pediatric Otolaryngology',(select user_type_id from user_type where user_type = 'Otolaryngology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(94, 'Plastic Surgery within Head And Neck',(select user_type_id from user_type where user_type = 'Otolaryngology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(95, 'Anatomic Pathology',(select user_type_id from user_type where user_type = 'Pathology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(96, 'Anatomic-Clinical Pathology',(select user_type_id from user_type where user_type = 'Pathology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(97, 'Bloodbanking-Transfusion Medicine',(select user_type_id from user_type where user_type = 'Pathology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(98, 'Chemical Pathology',(select user_type_id from user_type where user_type = 'Pathology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(99, 'Clinical Pathology',(select user_type_id from user_type where user_type = 'Pathology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(100, 'Cytopathology',(select user_type_id from user_type where user_type = 'Pathology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(101, 'Dermatopathology',(select user_type_id from user_type where user_type = 'Pathology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(102, 'Forensic Pathology',(select user_type_id from user_type where user_type = 'Pathology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(103, 'Hematology',(select user_type_id from user_type where user_type = 'Pathology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(104, 'Medical Microbiology',(select user_type_id from user_type where user_type = 'Pathology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(105, 'Molecular Genetic Pathology',(select user_type_id from user_type where user_type = 'Pathology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(106, 'Neuropathology',(select user_type_id from user_type where user_type = 'Pathology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(107, 'Pediatric Pathology',(select user_type_id from user_type where user_type = 'Pathology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(108, 'Selective Pathology',(select user_type_id from user_type where user_type = 'Pathology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(109, 'Adolescent Medicine',(select user_type_id from user_type where user_type = 'Pediatrics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(110, 'Developmental-Behavioral Pediatrics',(select user_type_id from user_type where user_type = 'Pediatrics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(111, 'IM-Pediatrics',(select user_type_id from user_type where user_type = 'Pediatrics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(112, 'Neonatal-Perinatal Medicine',(select user_type_id from user_type where user_type = 'Pediatrics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(113, 'Neurodevelopmental Disablilities',(select user_type_id from user_type where user_type = 'Pediatrics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(114, 'Pediatric Allergy',(select user_type_id from user_type where user_type = 'Pediatrics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(115, 'Pediatric Anesthesiology',(select user_type_id from user_type where user_type = 'Pediatrics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(116, 'Pediatric Clinical And Lab Immunology',(select user_type_id from user_type where user_type = 'Pediatrics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(117, 'Pediatric Critical Care Medicine',(select user_type_id from user_type where user_type = 'Pediatrics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(118, 'Pediatric Emergency Medicine',(select user_type_id from user_type where user_type = 'Pediatrics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(119, 'Pediatric Endocrinology',(select user_type_id from user_type where user_type = 'Pediatrics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(120, 'Pediatric Gastroenterology',(select user_type_id from user_type where user_type = 'Pediatrics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(121, 'Pediatric Infectious Disease',(select user_type_id from user_type where user_type = 'Pediatrics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(122, 'Pediatric Medical Toxicology',(select user_type_id from user_type where user_type = 'Pediatrics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(123, 'Pediatric Nephrology',(select user_type_id from user_type where user_type = 'Pediatrics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(124, 'Pediatric Pulmonology',(select user_type_id from user_type where user_type = 'Pediatrics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(125, 'Pediatric Rheumatology',(select user_type_id from user_type where user_type = 'Pediatrics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(126, 'Pediatrics',(select user_type_id from user_type where user_type = 'Pediatrics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(127, 'Sports Medicine- Pediatric',(select user_type_id from user_type where user_type = 'Pediatrics'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(128, 'Pediatric Rehabilitation Medicine',(select user_type_id from user_type where user_type = 'Physical Medicine And Rehab'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(129, 'Physical Medicine And Rehabilitation',(select user_type_id from user_type where user_type = 'Physical Medicine And Rehab'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(130, 'Spinal Cord Injury',(select user_type_id from user_type where user_type = 'Physical Medicine And Rehab'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(131, 'Sports Medicine- (PM And R)',(select user_type_id from user_type where user_type = 'Physical Medicine And Rehab'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(132, 'Craniofacial Surgery',(select user_type_id from user_type where user_type = 'Plastic Surgery'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(133, 'Facial Plastic Surgery',(select user_type_id from user_type where user_type = 'Plastic Surgery'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(134, 'Plastic Surgery',(select user_type_id from user_type where user_type = 'Plastic Surgery'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(135, 'Aerospace Medicine',(select user_type_id from user_type where user_type = 'Preventive Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(136, 'General Preventive Medicine',(select user_type_id from user_type where user_type = 'Preventive Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(137, 'Medical Toxicology',(select user_type_id from user_type where user_type = 'Preventive Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(138, 'Occupational Medicine',(select user_type_id from user_type where user_type = 'Preventive Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(139, 'Public Health And General Prev. Med',(select user_type_id from user_type where user_type = 'Preventive Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(140, 'Undersea And Hyperbaric Medicine',(select user_type_id from user_type where user_type = 'Preventive Medicine'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(141, 'Addiction Psychiatry',(select user_type_id from user_type where user_type = 'Psychiatry'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(142, 'Child And Adolescent Psychiatry',(select user_type_id from user_type where user_type = 'Psychiatry'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(143, 'Forensic Psychiatry',(select user_type_id from user_type where user_type = 'Psychiatry'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(144, 'Psychiatry',(select user_type_id from user_type where user_type = 'Psychiatry'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(145, 'Psychoanalysis',(select user_type_id from user_type where user_type = 'Psychiatry'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(146, 'Abdominal Radiology',(select user_type_id from user_type where user_type = 'Radiology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(147, 'Cardiothoracic Radiology',(select user_type_id from user_type where user_type = 'Radiology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(148, 'Diagnostic Radiology',(select user_type_id from user_type where user_type = 'Radiology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(149, 'Musculoskeletal Radiology',(select user_type_id from user_type where user_type = 'Radiology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(150, 'Neurology-DiagRad-Neuroradiology',(select user_type_id from user_type where user_type = 'Radiology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(151, 'Neuroradiology',(select user_type_id from user_type where user_type = 'Radiology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(152, 'Nuclear Radiology',(select user_type_id from user_type where user_type = 'Radiology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(153, 'Pediatric Radiology',(select user_type_id from user_type where user_type = 'Radiology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(154, 'Radiation Oncology',(select user_type_id from user_type where user_type = 'Radiology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(155, 'Radiological Physics',(select user_type_id from user_type where user_type = 'Radiology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(156, 'Radiology',(select user_type_id from user_type where user_type = 'Radiology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(157, 'Vascular-Interventional Radiology',(select user_type_id from user_type where user_type = 'Radiology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(158, 'Abdominal Surgery',(select user_type_id from user_type where user_type = 'Surgery'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(159, 'Colon And Rectal Surgery',(select user_type_id from user_type where user_type = 'Surgery'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(160, 'Cosmetic Surgery',(select user_type_id from user_type where user_type = 'Surgery'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(161, 'Dermatologic Surgery',(select user_type_id from user_type where user_type = 'Surgery'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(162, 'General Surgery',(select user_type_id from user_type where user_type = 'Surgery'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(163, 'Hand Surgery',(select user_type_id from user_type where user_type = 'Surgery'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(164, 'Head And Neck Surgery',(select user_type_id from user_type where user_type = 'Surgery'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(165, 'Oral And Maxillofacial Surgery',(select user_type_id from user_type where user_type = 'Surgery'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(166, 'Pediatric Cardiothoracic Surgery',(select user_type_id from user_type where user_type = 'Surgery'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(167, 'Pediatric Surgery',(select user_type_id from user_type where user_type = 'Surgery'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(168, 'Proctology',(select user_type_id from user_type where user_type = 'Surgery'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(169, 'Surgical Critical Care',(select user_type_id from user_type where user_type = 'Surgery'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(170, 'Thoracic Surgery',(select user_type_id from user_type where user_type = 'Surgery'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(171, 'Transplant Surgery',(select user_type_id from user_type where user_type = 'Surgery'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(172, 'Trauma Surgery',(select user_type_id from user_type where user_type = 'Surgery'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(173, 'Vascular Surgery',(select user_type_id from user_type where user_type = 'Surgery'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(174, 'Pediatric Urology',(select user_type_id from user_type where user_type = 'Urology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(175, 'Urology',(select user_type_id from user_type where user_type = 'Urology'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(176, 'Addiction Medicine',(select user_type_id from user_type where user_type = 'Other'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(177, 'Clinical Pharmacology',(select user_type_id from user_type where user_type = 'Other'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(178, 'Epidemiology',(select user_type_id from user_type where user_type = 'Other'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(179, 'Hospitalist',(select user_type_id from user_type where user_type = 'Other'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(180, 'Legal Medicine',(select user_type_id from user_type where user_type = 'Other'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(181, 'Medical Management',(select user_type_id from user_type where user_type = 'Other'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(182, 'Nuclear Medicine',(select user_type_id from user_type where user_type = 'Other'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(183, 'Osteopathic Manipulative Medicine',(select user_type_id from user_type where user_type = 'Other'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(184, 'Other Specialty',(select user_type_id from user_type where user_type = 'Other'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(185, 'Pain Medicine',(select user_type_id from user_type where user_type = 'Other'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(186, 'Palliative Medicine',(select user_type_id from user_type where user_type = 'Other'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(187, 'Pharmaceutical Medicine',(select user_type_id from user_type where user_type = 'Other'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(188, 'Sleep Medicine',(select user_type_id from user_type where user_type = 'Other'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
insert into user_speciality (user_speciality_id, user_speciality, user_type_id, created_timestamp, updated_timestamp) values(189, 'Unspecified Specialty',(select user_type_id from user_type where user_type = 'Other'),CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
COMMIT;

CREATE TABLE user_profile
(
   user_id varchar(50) PRIMARY KEY NOT NULL,
   first_name varchar(25),
   last_name varchar(25),
   email varchar(100),
   password varchar(255),
   user_type_id bigint,
   user_speciality_id bigint,
   address varchar(255),
   city varchar(50),
   state varchar(25),
   zip varchar(15),
   phone varchar(15),
   fax varchar(15),
   is_active varchar(1),
   lattitude decimal(20,17),
   longitude decimal(20,17),
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

INSERT INTO user_profile (user_id,first_name,last_name,email,password,user_type_id,user_speciality_id,address,city,state,zip,phone,fax,is_active,lattitude,longitude,created_timestamp,updated_timestamp) VALUES ('manual_insert','Uday','Reddy','udayakumarreddyv@gmail.com','test*987',10,10,'1234 AND LN','Atlanta','Georgia','33333','999999999','999999999','Y',34.09085883350354, -84.27777246068659,CURRENT_TIMESTAMP(),CURRENT_TIMESTAMP());
COMMIT;

CREATE TABLE confirmation_token
(
   token_id varchar(50) PRIMARY KEY NOT NULL,
   confirmation_token varchar(50),
   created_timestamp timestamp,
   user_id varchar(50) NOT NULL
);
ALTER TABLE confirmation_token
ADD CONSTRAINT FK_cnf_usrid
FOREIGN KEY (user_id)
REFERENCES user_profile(user_id);
CREATE INDEX IDX_CNF_USR_ID ON confirmation_token(user_id);

CREATE TABLE user_referral
(
   user_referral_id varchar(50) PRIMARY KEY NOT NULL,
   user_email varchar(100),
   doc_email varchar(100),
   is_registered varchar(1),
   created_timestamp timestamp,
   updated_timestamp timestamp
);

CREATE INDEX IDX_REF_USR_EML ON user_referral(user_email);