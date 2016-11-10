package com.seu.magiccamera.common.utils;

import android.app.Activity;
import android.util.DisplayMetrics;

/**
 * Created by jianweishao on 2016/8/2.
 */
public class DisplayUtils {
    public static int getDisplayW(Activity context){
        DisplayMetrics dm = new DisplayMetrics();
        context.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int width = dm.widthPixels;    //得到宽度
        return width;
    }

    public static int getDisplayH(Activity activity){
        DisplayMetrics dm = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(dm);
        int height = dm.heightPixels;  //得到高度
        return height;
    }
}
