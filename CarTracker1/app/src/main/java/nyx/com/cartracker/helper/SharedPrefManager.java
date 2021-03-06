package nyx.com.cartracker.helper;

/**
 * Created by Luminance on 1/18/2018.
 */

import android.content.Context;
import android.content.SharedPreferences;


import org.json.JSONObject;


public class SharedPrefManager {

    private static SharedPrefManager mInstance;
    private static Context mCtx;


    private static final String SHARED_PREF_NAME = "nyx.com.cattracker.memory";
    private static final String KEY_USER_ID = "keyuser_id";
    private static final String KEY_USER_NAME = "keyusername";
    private static final String KEY_USER_TYPE = "keyusertokentype";
    private static final String KEY_USER_PHONE= "keyusephoneype";
    private static final String KEY_USER_EMAIL= "keyuseremail";
    private static final String KEY_USER_PASSWORD = "keyusertokenpassword";





    private SharedPrefManager(Context context) {
        mCtx = context;
    }

    public static synchronized SharedPrefManager getInstance(Context context) {
        if (mInstance == null) {
            mInstance = new SharedPrefManager(context);
        }
        return mInstance;
    }

    public boolean userLogin(JSONObject user , String password)throws Exception {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, user.getString("id"));
        editor.putString(KEY_USER_NAME, user.getString("name"));
        editor.putString(KEY_USER_TYPE, user.getString("user_type"));
        editor.putString(KEY_USER_PHONE, user.getString("phone"));
        editor.putString(KEY_USER_PASSWORD, password);
        editor.putString(KEY_USER_EMAIL, user.getString("email"));
        editor.apply();
        return true;
    }
    public User getUser() {
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        return new User(
                sharedPreferences.getString(KEY_USER_ID, null),
                sharedPreferences.getString(KEY_USER_NAME, null),
                sharedPreferences.getString(KEY_USER_TYPE, null),
                sharedPreferences.getString(KEY_USER_PHONE, null),
                sharedPreferences.getString(KEY_USER_PASSWORD, null) ,
                sharedPreferences.getString(KEY_USER_EMAIL, null)

        );
    }

    public  boolean IsUserLoggedIn (){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
       String o = sharedPreferences.getString(KEY_USER_ID, null);
        return o!=null && !o.trim().equals("");

    }


    public  void Logout (){
        SharedPreferences sharedPreferences = mCtx.getSharedPreferences(SHARED_PREF_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString(KEY_USER_ID, "");
        editor.putString(KEY_USER_NAME, "");
        editor.putString(KEY_USER_PHONE, "");
        editor.putString(KEY_USER_PASSWORD, "");
        editor.putString(KEY_USER_TYPE, "");
        editor.apply();
    }






}