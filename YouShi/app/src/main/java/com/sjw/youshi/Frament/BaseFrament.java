package com.sjw.youshi.Frament;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.sjw.youshi.Bean.UserBean;

import cn.bmob.v3.BmobUser;

/**
 * Created by jianweishao on 2016/8/2.
 */
public abstract class BaseFrament extends Fragment{
    public abstract void initView();
    public View contentView;
    public abstract void initData();
    public Context context;
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context=getActivity();
    }


    public <T extends View> T findView(int resId) {

        View v = contentView.findViewById(resId);

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
}
