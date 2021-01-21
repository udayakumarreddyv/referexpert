package com.referexpert.referexpert.constant;

public class QueryConstants {

    public static String SELECT_USER_TYPE = "select user_type from user_type order by user_type";

    public static String SELECT_USER_TYPE_BY_USER_TYPE = "select user_type from user_type where user_type = ?";

    public static String SELECT_USER_TYPE_ID = "select user_type_id from user_type where user_type = ?";

    public static String SELECT_USER_SPL_ID = "select us.user_speciality_id user_speciality_id "
            + " from user_speciality us, user_type ut " + "where us.user_type_id = ut.user_type_id "
            + "and ut.user_type_id = ? and us.user_speciality = ? order by us.user_speciality";

    public static String SELECT_USER_SPECIALITY_BY_USER_TYPE = "select us.user_speciality user_speciality "
            + " from user_speciality us, user_type ut " + "where us.user_type_id = ut.user_type_id "
            + "and ut.user_type = ? order by us.user_speciality";

    public static String SELECT_USER_SPECIALITY = "select user_type from user_type";

    public static String INSERT_USER_PROFILE = "insert into user_profile (user_id, first_name, last_name, email, password, user_type_id, "
            + "user_speciality_id, address, phone, fax, is_active, created_timestamp, updated_timestamp) "
            + "values (?,?,?,?,?,?,?,?,?,?,?,?,?)";
    
    public static String UPDATE_USER_PROFILE = "update user_profile set is_active = ?, updated_timestamp = ? where email = ?";

    public static String SELECT_USER_PROFILE = "select user_id, first_name, last_name, email, password, user_type_id, "
            + " user_speciality_id, address, phone, fax, is_active, created_timestamp, updated_timestamp "
            + " from user_profile where ";

    public static String INSERT_CONFIRMATION_TOKEN = "insert into confirmation_token (token_id, confirmation_token, created_timestamp, user_id) "
            + "values (?,?,?,?)";

    public static String SELECT_CONFIRMATION_TOKEN = "select token_id, confirmation_token, created_timestamp, user_id "
            + " from confirmation_token where confirmation_token = ?";
    
    public static String DELETE_CONFIRMATION_TOKEN = "delete from confirmation_token where confirmation_token = ?";
    
    public static String INSERT_USER_REFERRAL = "insert into user_referral (user_referral_id, user_email, doc_email, is_registered, "
            + "created_timestamp, updated_timestamp) values (?,?,?,?,?,?)";
    
    public static String SELECT_USER_REFERRAL = "select user_referral_id, user_email, doc_email, is_registered,created_timestamp, updated_timestamp "
            + " from user_referral where user_referral_id = ?";
    
    public static String UPDATE_USER_REFERRAL = "update user_referral set is_registered = ?, updated_timestamp = ? where doc_email = ?";
    
    public static String SELECT_ACTIVE_USER = "select up.user_id, up.first_name, up.last_name, up.email, ut.user_type, us.user_speciality, up.address, "
            + "up.phone, up.fax, up.is_active from user_profile up, user_type ut, user_speciality us where up.user_type_id = ut.user_type_id "
            + "and up.user_speciality_id = us.user_speciality_id and up.is_active = 'Y' and ";
}
