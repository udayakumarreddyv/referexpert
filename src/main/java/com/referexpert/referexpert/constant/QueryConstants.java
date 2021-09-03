package com.referexpert.referexpert.constant;

public class QueryConstants {

    public static String SELECT_USER_TYPE = "select user_type from user_type where user_type <> 'ADMIN' order by user_type";

    public static String SELECT_USER_TYPE_BY_USER_TYPE = "select user_type from user_type where user_type = ?";

    public static String SELECT_USER_TYPE_ID = "select user_type_id from user_type where user_type = ?";

    public static String SELECT_USER_SPL_ID = "select us.user_speciality_id user_speciality_id "
            + " from user_speciality us, user_type ut " + "where us.user_type_id = ut.user_type_id "
            + " and ut.user_type_id = ? and us.user_speciality = ? order by us.user_speciality";

    public static String SELECT_USER_SPECIALITY_BY_USER_TYPE = "select us.user_speciality user_speciality "
            + " from user_speciality us, user_type ut " + "where us.user_type_id = ut.user_type_id "
            + " and ut.user_type <> 'ADMIN' and ut.user_type = ? order by us.user_speciality";

    public static String SELECT_USER_SPECIALITY = "select user_type from user_type where user_type <> 'ADMIN'";

    public static String INSERT_USER_PROFILE = "insert into user_profile (user_id, first_name, last_name, email, password, user_type_id, "
            + "user_speciality_id, address, city, state, zip, phone, fax, is_active, lattitude, longitude, created_timestamp, updated_timestamp, service, insurance) "
            + "values (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    public static String UPDATE_USER_ACTIVATION = "update user_profile set is_active = ?, updated_timestamp = ? where email = ?";

    public static String UPDATE_USER_PASSWORD = "update user_profile set password = ?, updated_timestamp = ? where email = ?";

    public static String UPDATE_USER_PROFILE = "update user_profile set first_name = ?, last_name = ?, user_type_id  = ?, user_speciality_id  = ?, "
            + " address  = ?, city = ?, state = ?, zip = ?, phone  = ?, fax  = ?, lattitude =?, longitude = ?, updated_timestamp = ?, service = ?, insurance = ? where email = ?";

    public static String SELECT_USER_PROFILE = "select user_id, first_name, last_name, email, password, user_type_id, "
            + " user_speciality_id, address, city, state, zip, phone, fax, is_active, lattitude, longitude, created_timestamp, updated_timestamp, service, insurance "
            + " from user_profile where ";

    public static String INSERT_CONFIRMATION_TOKEN = "insert into confirmation_token (token_id, confirmation_token, created_timestamp, user_id) "
            + "values (?,?,?,?)";

    public static String SELECT_CONFIRMATION_TOKEN = "select token_id, confirmation_token, created_timestamp, user_id "
            + " from confirmation_token where confirmation_token = ?";

    public static String DELETE_CONFIRMATION_TOKEN = "delete from confirmation_token where confirmation_token = ?";

    public static String INSERT_USER_REFERRAL = "insert into user_referral (user_referral_id, user_email, doc_email, is_registered, "
            + "created_timestamp, updated_timestamp) values (?,?,?,?,?,?)";

    public static String SELECT_USER_REFERRAL = "select user_referral_id, user_email, doc_email, is_registered,created_timestamp, updated_timestamp "
            + " from user_referral where user_referral_id = ? and doc_email = ?";

    public static String UPDATE_USER_REFERRAL = "update user_referral set is_registered = ?, updated_timestamp = ? where doc_email = ?";

    public static String SELECT_ACTIVE_USER = "select up.user_id, up.first_name, up.last_name, up.email, ut.user_type, us.user_speciality, up.address, "
            + "up.city, up.state, up.zip, up.phone, up.fax, up.is_active, up.password, up.lattitude, up.longitude, up.service, up.insurance from user_profile up, user_type ut, user_speciality us "
            + " where up.user_type_id = ut.user_type_id and up.user_speciality_id = us.user_speciality_id and up.is_active = 'Y' and ";
    
    public static String SELECT_ACTIVE_USERS = "select up.user_id, up.first_name, up.last_name, up.email, ut.user_type, us.user_speciality, up.address, "
            + "up.city, up.state, up.zip, up.phone, up.fax, up.is_active, up.password, up.lattitude, up.longitude, up.service, up.insurance from user_profile up, user_type ut, user_speciality us "
            + " where up.user_type_id = ut.user_type_id and up.user_speciality_id = us.user_speciality_id and user_type <> 'ADMIN' and ";

    public static String INSERT_APPOINTMENT = "insert into appointment (appointment_id, appointment_from, appointment_to, date_time, subject, reason, "
            + "is_accepted, is_served, created_timestamp, updated_timestamp) values (?,?,?,?,?,?,?,?,?,?)";

    public static String UPDATE_APPOINTMENT_STATUS = "update appointment set is_accepted = ?, updated_timestamp = ? where appointment_id = ?";

    public static String UPDATE_SERVED_STATUS = "update appointment set is_served = ?, updated_timestamp = ? where appointment_id = ?";

    public static String SELECT_APPOINTMENT = "select re.appointment_id, f.email, f.first_name, f.last_name, t.email, t.first_name, t.last_name, "
            + "re.date_time, re.is_accepted, re.is_served, re.subject, re.reason from appointment re, user_profile f, user_profile t "
            + "where re.appointment_from = f.user_id and re.appointment_to = t.user_id and ";
    
    public static String SELECT_REFRESH_TOKEN = "select refresh_token_id, user_id, token, expiry_date from refresh_token where ";
    
    public static String INSERT_REFRESH_TOKEN = "insert into refresh_token (refresh_token_id, user_id, token, expiry_date) values (?,?,?,?)";
    
    public static String DELETE_REFRESH_TOKEN = "delete from refresh_token where token = ?";
    
    public static String DELETE_REFRESH_TOKEN_BYUSER = "delete from refresh_token where user_id = ?";
    
    public static String SELECT_USER_COUNTS = "select count(0) from user_profile where is_active = ? and user_type_id <> 10";
    
    public static String SELECT_USER_NOTIFICATION = "select un.user_id, un.notification_email, un.notification_mobile from user_profile up, user_notification un where up.user_id = un.user_id and ";
    
    public static String INSERT_USER_NOTIFICATION = "insert into user_notification (user_id, notification_email, notification_mobile, created_timestamp, updated_timestamp) values(?, ?, ?, ?, ?)";
    
    public static String UPDATE_USER_NOTIFICATION = "update user_notification set notification_email = ?, notification_mobile = ?, updated_timestamp = ? where user_id = ?";
}
