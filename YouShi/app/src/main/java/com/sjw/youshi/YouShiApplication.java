package com.sjw.youshi;

import android.app.Application;
import android.content.Context;

import com.example.yzblib.ZBLoadResultCallback;
import com.example.yzblib.ZBUtils;
import com.facebook.drawee.backends.pipeline.Fresco;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.sjw.youshi.utils.ImagePipelineConfigFactory;

/**
 * Created by jianweishao on 2016/8/3.
 */
public class YouShiApplication extends Application {

    private Context context;
    @Override
    public void onCreate() {
        super.onCreate();
        context=this;
        // sdk初始
        initImageLoader(context);
        Fresco.initialize(this, ImagePipelineConfigFactory.getImagePipelineConfig(this));
        ZBUtils.init(this, new ZBLoadResultCallback() {
            @Override
            public void success() {
                ZBUtils.setAppKey(context,"f2ecffb21f999350Ji2fleOppFohTmZTC8E0aWoYvO7u4VdmK4MJj2qtMRXzfaEhmg");
            }

            @Override
            public void failed(String s) {

            }
        });
        //initAD();
    }

    @Override
    protected void attachBaseContext(Context base) {
        ZBUtils.initAttachBaseContext(base);
        super.attachBaseContext(base);
    }

    public static void initImageLoader(Context context) {
        // This configuration tuning is custom. You can tune every option, you may tune some of them,
        // or you can create default configuration by
        //  ImageLoaderConfiguration.createDefault(this);
        // method.
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY - 2);
        config.denyCacheImageMultipleSizesInMemory();
        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        config.diskCacheSize(50 * 1024 * 1024); // 50 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        config.writeDebugLogs(); // Remove for release app

        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

//    private void initAD(){
//        // 初始化统计器，并通过代码设置APP_ID, APP_PID
//        AppConnect.getInstance("1c3eb3052bb8c2005ac9936e0e10f596", "waps", this);
//        // 预加载插屏广告内容（仅在使用到插屏广告的情况，才需要添加）
//        AppConnect.getInstance(this).initPopAd(this);
//    }
}
