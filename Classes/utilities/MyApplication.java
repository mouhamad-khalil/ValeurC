package com.isae.mohamad.mahallat.Classes.utilities;

/**
 * Created by mohamad on 10/30/2018.
 */

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;

public class MyApplication extends Application {

    private static Context mContext;

    public void onCreate() {
        super.onCreate();
        mContext = getApplicationContext();
    }

    public static Context GetAppContext() {
        return mContext;
    }

    public static boolean SaveCredentials(String credentials)
    {
        SharedPreferences sp;

        try {
            if (mContext != null) {
                sp = mContext.getSharedPreferences(Constants.SP_NAME, MODE_PRIVATE);
                sp.edit().putString(Constants.SP_CREDENTIALS, credentials).apply();
                sp.edit().putBoolean(Constants.SP_LOGGEDIN, true).apply();
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public static String GetSavedCredentials()
    {
        SharedPreferences sp;
        String s =  null;
        try {
            if (mContext != null) {
                sp = mContext.getSharedPreferences(Constants.SP_NAME, MODE_PRIVATE);
                s = sp.getString(Constants.SP_CREDENTIALS, null);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return s;
    }

    public  static boolean IsLoggedIn(){

        SharedPreferences sp;

        try {
            if (mContext != null) {
                sp = mContext.getSharedPreferences(Constants.SP_NAME, MODE_PRIVATE);
                return sp.getBoolean(Constants.SP_LOGGEDIN, false);
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
        return false;
    }

    public static boolean EraseCredentials(){

        SharedPreferences sp;

        try {
            if (mContext != null) {
                sp = mContext.getSharedPreferences(Constants.SP_NAME, MODE_PRIVATE);
                sp.edit().remove(Constants.SP_CREDENTIALS).apply();
                sp.edit().remove(Constants.SP_LOGGEDIN).apply();
            }
        }
        catch(Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
}