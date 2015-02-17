package net.ogify.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Storage {
    private static String PREFS_NAME = "qwerty";

    public static String getPreference(String name) {
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(name, null);
    }

    public static String getPreference(String name, Context context) {
        MyApplication.setAppContext(context);
        SharedPreferences settings = MyApplication.getAppContext().getSharedPreferences(PREFS_NAME, 0);
        return settings.getString(name, null);
    }

    public static void setPreference(String name, String value) {
        SharedPreferences prefs = MyApplication.getAppContext().getSharedPreferences(PREFS_NAME, 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(name, value);
        editor.commit();
    }
}
