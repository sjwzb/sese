package com.joyme.recordlib.media;

import android.content.Context;
import android.util.Log;


/**
 * Created by jianweishao on 2016/5/9.
 */
public class MediaConfig {

    public boolean isAudioRecord = true;
    public static MediaConfig instance;
    public boolean isOpenCamera = true;
    public static int screenWidth;
    public static int screenHeight;
    public float scale = 1;
    private static final int HIGH_DEF_W = 960;
    private static final int HIGH_DEF_H = 480;
    private static final int SUPER_HIGH_DEF_W = 1280;
    private static final int SUPER_HIGH_DEF_H = 544;
    public  int cameraW = 320;
    public  int cameraH = 240;
    public int rendCamera_w;
    public int rendCamera_h;
    public int quality_code;
    private String TAG="MediaConfig";
    public static MediaConfig getInstance() {
        if (instance == null) {
            instance = new MediaConfig();
        }
        return instance;
    }

    private MediaConfig() {

    }

    public void initMediaInfo(Context context,int quality_code,boolean isAudioRecord,boolean isOpenCamera) {
        this.isAudioRecord=isAudioRecord;
        this.isOpenCamera=isOpenCamera;
        this.quality_code=quality_code;
        Log.d("meidaConfig","---------------"+quality_code);
        switch (quality_code) {
            case 1:  //超清
                if (screenWidth<screenHeight){ //竖版
//                    if (screenHeight<SUPER_HIGH_DEF_W){
//                        mediaW = screenWidth;
//                        mediaH = screenHeight;
//                        scale=1;
//                    }else {
                        mediaH=SUPER_HIGH_DEF_W;
                        scale=(float)mediaH/screenHeight;
                        mediaW= (int) (screenWidth*scale);
                        mediaW=mediaW/2*2;
//                    }

                }else { //橫版
//                    if (screenWidth<SUPER_HIGH_DEF_W){
//                        mediaW = screenWidth;
//                        mediaH = screenHeight;
//                        scale=1;
//                    }else {
                        mediaW=SUPER_HIGH_DEF_W;
                        scale=(float) mediaW/screenWidth;
                        mediaH= (int) (screenHeight*scale);
                        mediaH=mediaH/2*2;
//                    }

                }
                break;
            case 2: // 高清
                if (screenWidth<screenHeight){ //竖版
//                    if (screenHeight<HIGH_DEF_W){
//                        mediaW = screenWidth;
//                        mediaH = screenHeight;
//                        scale=1;
//                    }else {
                        mediaH=HIGH_DEF_W;
                        scale=(float)mediaH/screenHeight;
                        mediaW= (int) (screenWidth*scale);
                        mediaW=mediaW/2*2;
//                    }

                }else { //橫版
//                    if (screenWidth<HIGH_DEF_W){
//                        mediaW = screenWidth;
//                        mediaH = screenHeight;
//                        scale=1;
//                    }else {
                        mediaW=HIGH_DEF_W;
                        scale=(float) mediaW/screenWidth;
                        mediaH= (int) (screenHeight*scale);
                        mediaH=mediaH/2*2;
//                    }

                }
                break;

        }

        Log.d(TAG,"screenW "+ screenWidth+"  screenH "+screenHeight  +" mediaW: "+mediaW+" meidaH:  "+mediaH+" scale :"+scale+" quality_code: "+quality_code);

        rendCamera_w = cameraW;
        rendCamera_h = cameraH;
    }


    public int mediaW;
    public int mediaH;
    public int rendCamerX;
    public int rendCamerY;
    public String video_path;

    public void restData() {
        rendCamerX = 0;
        rendCamerY = 0;
    }

    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
