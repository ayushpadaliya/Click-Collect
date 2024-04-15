package com.example.ecommerce.utils;

import com.example.ecommerce.Responces.CategoryResponse.GetCategoryResponse;

import java.util.List;

public class VariableBag {

    public static final String PREF_NAME = "MyPref";
    public static final String USER_ID = "1";
    public static final String ID_COL = "id";
    public static final String EVENT_NAME = "name";
    public static final String EVENT_TIME = "time";

    public static String BASE_URL = "https://192.168.1.146/CHPL/mobileApi/";
    public static String IMG_URL = "https://192.168.1.140/CHPL/products/";

    public static String API_KEY="";

    public static String SUCCESS_CODE = "200";
    public static String FAIL_CODE="201";
    public static String user_id="1";
    public static String BASE_URL_Weather = "https://api.openweathermap.org/data/2.5/weather?q=delhi&appid=cdc6ebe91ce1de5eb987003a786b0bdb";
    public static String API_KEY1="cdc6ebe91ce1de5eb987003a786b0bdb";
    public static String KEY_IS_LOGGED_IN_User="key_user";
    public static String Vendor_id="vendor";
    public static String KEY_IS_LOGGED_IN_Vendor="key_vendor";

    public static String category_name="";

    public static String cart_id="";

    public static String isEdit="false";
    public static String Edit_Category_name="";

    public static String category_id="";
    public static String sub_category_id="";
    public static String edit_category_id="";
    public static List<GetCategoryResponse.Message> ListCategory;
    public static boolean isSet=false;

    public static String product_id="";
    public static String Vendor_product_id="vendor";
    public static String User_product_id="user";
    public static String mobileNo="954656258";
    public static final String DB_NAME="EventBook";
    public static final int DB_VERSION=1;
    public static final String TABLE_NAME="UserEvent";
    public static  Integer cart_items=0;



}
