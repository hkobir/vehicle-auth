package com.hk.vehicleauth.utils;

import android.content.Context;
import android.content.SharedPreferences;

public class DataPreference {
    public final static String PREFS_NAME = "app_prefs";


    public static boolean isPreferenceExist(Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, context.MODE_PRIVATE);
        if (preferences.contains(key)) {
            return true;
        } else {
            return false;
        }
    }

    public static void saveString(Context context, String key, String value) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static String getString(Context context, String key) {
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        return sharedPref.getString(key, "");
    }

    public static void clearData(Context context, String key) {

        if (isPreferenceExist(context, key)) {
            SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
            sharedPref.edit().remove(key).commit();
        }
    }
    public static void clearAllData(Context context){
        SharedPreferences sharedPref = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        sharedPref.edit().clear().commit();
    }

}
