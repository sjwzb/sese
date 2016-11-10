package com.joyme.recordlib;

import java.util.ArrayList;

/**
 * Created by xianfengli on 2016/5/5.
 */
public class CameraCallbackManager {

    private static CameraCallbackManager mCameraCallbackManager;
    private UpdateBitmapCallback mCallback;
    private CameraCallbackManager(){
    }

    public static CameraCallbackManager getInstance(){
        if(mCameraCallbackManager == null){
            mCameraCallbackManager = new CameraCallbackManager();
        }
        return mCameraCallbackManager;
    }

    public void addCameraUpdateBitmapCallback(UpdateBitmapCallback callback){
        mCallback = callback;
    }

    public UpdateBitmapCallback getCurrentUpdateBitmapCallback(){
        return mCallback;
    }

}
