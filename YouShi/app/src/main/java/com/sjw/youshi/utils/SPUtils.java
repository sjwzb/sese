package com.sjw.youshi.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by jianweishao on 2016/9/1.
 */
public class SPUtils {
    private static SharedPreferences getSP(Context context) {
        return context.getSharedPreferences("sese", Context.MODE_PRIVATE);
    }

    public static void saveStr(Context context, String key, String value) {
        SharedPreferences.Editor editor = getSP(context).edit();
        editor.putString(key, value);
        editor.commit();
    }

    public static void saveBooler(Context context, String key, boolean b) {
        SharedPreferences.Editor editor = getSP(context).edit();
        editor.putBoolean(key, b);
        editor.commit();
    }

    public static boolean getSpBoolean(Context context, String key) {
        return getSP(context).getBoolean(key, false);
    }
}
