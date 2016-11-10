package com.seu.magiccamera.activity;

import android.content.Context;
import android.os.Environment;

import java.io.File;

public  class FileManager {
    private static final String APP_NAME = "youshi";
    private static final String BASE_PATH = APP_NAME;
    private static final String DEBUG = "debug";
    private static final String VIDEO = "video";

    public static String getAppCachePath(Context context) {
        String appCacheDir = context.getFilesDir().getAbsolutePath() + File.separator + APP_NAME;
        return appCacheDir;
    }

    public static File getDebugPath(Context context) {
        return getPath(DEBUG, context);
    }

    public static File getVideoPath(Context context) {
        return getPath(VIDEO, context);
    }

    private static File getPath(String fileName, Context context) {
        File file = new File(getBasepath(context), fileName);
        if (!file.exists())
            file.mkdirs();

        return file;
    }

    private static File getBasepath(Context context) {
        File appWorkDir = null;

        boolean isSDCardAvailable = Environment.getExternalStorageState()
                .equals(Environment.MEDIA_MOUNTED);
        if (isSDCardAvailable) {//根据包名区分不同的应用，创建各自的目录
            appWorkDir = new File(Environment.getExternalStorageDirectory().getPath() + File.separator + BASE_PATH
                    + File.separator + context.getPackageName().replace('.', '_'));
        }

        if (appWorkDir == null) {
            appWorkDir = new File(getAppCachePath(context));
        }

        if (!appWorkDir.exists()) {
            appWorkDir.mkdirs();
        }

        return appWorkDir;
    }

}
