package br.com.cursosdocng.cursosdocng.util;


import android.content.Context;
import android.content.SharedPreferences;


public class Pref {

    private static final String PREF_ID = Pref.class.getSimpleName();

    public static void setBoolean(Context context, String flag, boolean on) {
        SharedPreferences pref = context.getSharedPreferences(PREF_ID, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putBoolean(flag, on);
        editor.commit();
    }

    public static boolean getBoolean(Context context, String flag) {
        SharedPreferences pref = context.getSharedPreferences(PREF_ID, 0);
        boolean b = pref.getBoolean(flag, false);
        return b;
    }

    public static void setInteger(Context context, String flag, int valor) {
        SharedPreferences pref = context.getSharedPreferences(PREF_ID, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putInt(flag, valor);
        editor.commit();
    }

    public static int getInteger(Context context, String flag) {
        SharedPreferences pref = context.getSharedPreferences(PREF_ID, 0);
        int b = pref.getInt(flag, 0);
        return b;
    }

    private static void setString(Context context, String flag, String valor) {
        SharedPreferences pref = context.getSharedPreferences(PREF_ID, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(flag, valor);
        editor.commit();
    }

    public static String getString(Context context, String flag) {
        SharedPreferences pref = context.getSharedPreferences(PREF_ID, 0);
        String b = pref.getString(flag, "");
        return b;
    }

}

