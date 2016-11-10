package com.sjw.youshi;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.sjw.youshi.Bean.UserBean;

import cn.bmob.v3.BmobUser;

/**
 * Created by jianweishao on 2016/7/29.
 */
public abstract class BaseActivity extends FragmentActivity {
    public Context context;
    public String TAG="youshi";
    public ProgressDialog pd;
    public DisplayImageOptions roundOptions,options;

    public abstract void initView();

    public abstract void initData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        this.context = this;
        super.onCreate(savedInstanceState);
        pd=new ProgressDialog(this);
        initImageLoader();
    }

    public <T extends View> T findView(int resId) {

        View v = findViewById(resId);

        return (T) v;
    }

    public void Toast(String msg) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
    public boolean isLogin() {
        UserBean userBean = BmobUser.getCurrentUser(UserBean.class);
        if (userBean == null) {
            return false;
        }
        return true;
    }

    public void hideSoftInput(){
        InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    private void initImageLoader() {
        roundOptions = new DisplayImageOptions.Builder()
                // .showImageOnLoading(R.drawable.test)
                .showImageForEmptyUri(R.drawable.test_header)
                .showImageOnFail(R.drawable.test_header)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(90))//是否设置为圆角，弧度为多少
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        options = new DisplayImageOptions.Builder()
                // .showImageOnLoading(R.drawable.test)
                .showImageForEmptyUri(R.drawable.test)
                .showImageOnFail(R.drawable.test)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }
}
