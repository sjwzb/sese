package com.sjw.youshi.Frament;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.sjw.youshi.Bean.UserBean;
import com.sjw.youshi.LoginActivity;
import com.sjw.youshi.R;
import com.sjw.youshi.UserActivity;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by jianweishao on 2016/8/2.
 */
public class SecondFrament extends BaseFrament {
    private RelativeLayout rl_user;
    private TextView tv_name, tv_qianming;
    private ImageView iv_head;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initImageLoader();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.frament_second, null);
        initView();
        initData();
        return contentView;
    }

    @Override
    public void initView() {
        iv_head = findView(R.id.iv_head);
        tv_name = findView(R.id.tv_name);
        tv_qianming = findView(R.id.tv_qianming);
        rl_user = findView(R.id.rl_user);
        rl_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, UserActivity.class);
                intent.putExtra("userID", BmobUser.getCurrentUser().getObjectId());
                startActivity(intent);
            }
        });
    }

    @Override
    public void initData() {
        initImageLoader();

    }

    @Override
    public void onResume() {
        super.onResume();
        getUserDis();
    }

    private void getUserDis() {
        if (!isLogin()) {
            return;
        }
        BmobQuery<UserBean> query = new BmobQuery<>();
        query.getObject(BmobUser.getCurrentUser().getObjectId(), new QueryListener<UserBean>() {

            @Override
            public void done(UserBean userBean, BmobException e) {
                tv_name.setText(userBean.getNickName());
                tv_qianming.setText(userBean.getSex());
                tv_qianming.setText(userBean.getQinming());
                ImageLoader.getInstance().displayImage(userBean.getHeaderUrl(), iv_head, options);
            }
        });
    }

    private DisplayImageOptions options;

    private void initImageLoader() {
        options = new DisplayImageOptions.Builder()
                // .showImageOnLoading(R.drawable.test)
                .showImageForEmptyUri(R.drawable.test_header)
                .showImageOnFail(R.drawable.test_header)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .displayer(new RoundedBitmapDisplayer(90))//是否设置为圆角，弧度为多少
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }
}
