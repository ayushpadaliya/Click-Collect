package com.example.ecommerce.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceManager {

        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        String BASE_URL_Weather;

    public PreferenceManager(Context context) {
        this.sharedPreferences = context.getSharedPreferences(VariableBag.PREF_NAME,Context.MODE_PRIVATE);
        this.editor = sharedPreferences.edit();

    }


    public void setIsEdit(boolean isEdit)
    {
        editor.putBoolean(VariableBag.isEdit,isEdit);
        editor.apply();
    }
    public boolean getIsEdit()
    {
        return sharedPreferences.getBoolean(VariableBag.isEdit,false);
    }

        public void setVendorID(String key,String value)
        {
            editor.putString(key,value);
            editor.commit();
        }
        public String getVendorID()
        {
            return sharedPreferences.getString(VariableBag.Vendor_id,"1");
        }
    public void setSubCategoryId(String key,String value)
    {
        editor.putString(key,value);
        editor.commit();
    }
    public String getSubCategoryID()
    {
        return sharedPreferences.getString(VariableBag.sub_category_id,"1");
    }
    public void loginUser(Boolean login_user)
    {
        editor.putBoolean(VariableBag.KEY_IS_LOGGED_IN_User,login_user);
        editor.commit();
    }
    public void loginVendor(Boolean login_vendor)
    {
        editor.putBoolean(VariableBag.KEY_IS_LOGGED_IN_Vendor,login_vendor);
        editor.commit();
    }
    public Boolean getLoginUser()
    {
       return sharedPreferences.getBoolean(VariableBag.KEY_IS_LOGGED_IN_User,false);
    }
    public Boolean getLoginVendor()
    {
        return sharedPreferences.getBoolean(VariableBag.KEY_IS_LOGGED_IN_Vendor,false);
    }
    public void setVendorProduct(String key,String value)
    {
        editor.putString(key,value);
        editor.commit();
    }
    public String getVendorProduct()
    {
        return sharedPreferences.getString(VariableBag.Vendor_product_id,"1");
    }
    public void setUserProduct(String key,String value)
    {
        editor.putString(key,value);
        editor.commit();
    }
    public String getUserProduct()
    {
        return sharedPreferences.getString(VariableBag.User_product_id,"1");
    }
    public void setCArtID(String key,String value)
    {
        editor.putString(key,value);
        editor.commit();
    }
    public String getCartID()
    {
        return sharedPreferences.getString(VariableBag.cart_id,"1");
    }
}

