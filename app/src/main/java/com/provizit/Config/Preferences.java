package com.provizit.Config;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

public class Preferences {
    private static final String PREFERENCE_NAME ="Provizit" ;

    public final static String LOGINCHECK="LOGINCHECK";
    public final static String AdOnline="AdOnline";
    public final static String latitude="latitude";
    public final static String longitude="longitude";
    public final static String Profile_Url="Profile_Url";
    public final static String contacts_list="contacts_list";
    public final static String contacts_contactsLi="contacts_contactsLi";
    public final static String trd_access="trd_access";
    public final static String Cancel_access="Cancel_access";
    public final static String m_ID="m_ID";
    public final static String pushnotification="pushnotification";

    public static void saveFloatValue(Context context, String key, float value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putFloat(key, value);
        editor.apply();
    }

    public static float loadFloatValue(Context mContext, String from, float defValue) {
        return mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getFloat(from, defValue);
    }

    public static void saveStringValue(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String loadStringValue(Context mContext, String from, String defValue) {
        return mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getString(from, defValue);
    }

    public static void saveLongValue(Context context, String key, long value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putLong(key, value);
        editor.apply();
    }

    public static long loadLongValue(Context mContext, String from, long defValue) {
        return mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getLong(from, defValue);
    }

    public static void saveIntegerValue(Context context, String key, int value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putInt(key, value);
        editor.apply();
    }

    public static int loadIntegerValue(Context mContext, String from, int defValue) {
        return mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getInt(from, defValue);
    }

    public static void saveBooleanValue(Context context, String key, boolean value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.putBoolean(key, value);
        editor.apply();
    }
    public static void deleteSharedPreferences(Context context){
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        editor.clear().commit();
    }

    public static void saveContactAsynParser(Context context, String key, String value) {
        SharedPreferences.Editor editor = context.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        String json = gson.toJson(value);
        editor.putString(key,json);
        editor.apply();
    }

    public static String loadContactAsynParser(Context mContext, String from, String defValue) {
        return mContext.getSharedPreferences(PREFERENCE_NAME, Context.MODE_PRIVATE).getString(from, defValue);
    }
}

