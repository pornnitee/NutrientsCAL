package com.example.starius.project;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

/**
 * Created by STARIUS on 1/20/2018.
 */

public class SharedPrefManager {
    private static final String SHARED_PREF_NAME = "simplifiedcodingsharedpref";
    private static final String KEY_USERNAME = "keyusername";
    private static final String KEY_PASSWORD = "keypassword";
    private static final String KEY_EMAIL = "keyemail";
    private static final String KEY_WEIGHT = "keyweight";
    private static final String KEY_HEIGHT = "keyheight";
    private static final String KEY_AGE = "keyage";
    private static final String KEY_GENDER = "keygender";

    private static final String KEY_date = "keydate";
    private static final String KEY_energy = "keyenergy";
    private static final String KEY_protein = "keyprotein";
    private static final String KEY_fat = "keyfat";
    private static final String KEY_carbohydrate = "keycarbohydrate";
    private static final String KEY_calcium = "keycalcium";
    private static final String KEY_iron = "keygiron";
    private static final String KEY_meal = "keymeal";
    private static final String KEY_name_menu = "keyname_menu";

    private static SharedPrefManager mInstance;
    private static Context mCtx;

    private SharedPrefManager(Context context){
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context){
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }
    //method to let the user login
    //this method will store the user data in shared preferences
    public void userLogin(User user){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USERNAME, user.getUsername());
        editor.putString(KEY_PASSWORD, user.getPassword());
        editor.putString(KEY_EMAIL, user.getEmail());
        editor.putString(KEY_WEIGHT, user.getWeight());
        editor.putString(KEY_HEIGHT, user.getHeight());
        editor.putString(KEY_AGE, user.getAge());
        editor.putString(KEY_GENDER, user.getGender());
        editor.apply();
    }
    //this method will checker whether user is already logged in or not
    public  boolean isLoggedIn(){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  sharedPreferences.getString(KEY_USERNAME,null) != null;
    }
    //this method will give the logged in user
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return  new User(
             // sharedPreferences.getInt(KEY_ID,-1),
                sharedPreferences.getString(KEY_USERNAME,null),
                sharedPreferences.getString(KEY_PASSWORD,null),
                sharedPreferences.getString(KEY_EMAIL,null),
               sharedPreferences.getString(KEY_WEIGHT,null),
                sharedPreferences.getString(KEY_HEIGHT,null),
                sharedPreferences.getString(KEY_AGE,null),
                sharedPreferences.getString(KEY_GENDER,null)
        );
    }
    //this method will logout the user
    public void logout() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
        mCtx.startActivity(new Intent(mCtx, LoginActivity.class));
    }
}
