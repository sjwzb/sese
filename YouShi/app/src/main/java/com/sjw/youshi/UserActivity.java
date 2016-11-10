package com.sjw.youshi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.sjw.youshi.Bean.UserBean;
import com.sjw.youshi.Bean.VideoBean;
import com.sjw.youshi.adapter.UserVideoAdapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;

/**
 * Created by jianweishao on 2016/8/17.
 */
public class UserActivity extends BaseActivity {
    private TextView tv_name, tv_dis;
    private ImageView iv_title;
    private String userID;
    private UserBean currUser;
    private GridView rv;
    private UserVideoAdapter adapter;
    private TextView tv_qianming;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initView();
        initData();
        initImageLoader();
    }

    @Override
    public void initView() {
        tv_dis = findView(R.id.tv_dis);
        tv_name = findView(R.id.tv_name);
        iv_title = findView(R.id.iv_head);
        tv_qianming=findView(R.id.tv_qianming);
        //GridLayoutManager manager=new GridLayoutManager(this,2);
        rv = findView(R.id.rv_user);
        findView(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        findView(R.id.tv_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, EditUserInfoActivity.class);
                startActivity(intent);
            }
        });
        // rv.setLayoutManager(manager);

    }

    @Override
    public void initData() {
        userID = getIntent().getStringExtra("userID");
        adapter = new UserVideoAdapter(this);
       // getUserDis();
        rv.setAdapter(adapter);
    }

    private void getUserDis() {
        pd.setMessage("加载中");
        BmobQuery<UserBean> query = new BmobQuery<>();
        query.getObject(userID, new QueryListener<UserBean>() {

            @Override
            public void done(UserBean userBean, BmobException e) {
                tv_name.setText(userBean.getNickName());
                tv_dis.setText(userBean.getSex());
                tv_qianming.setText(userBean.getQinming());
                ImageLoader.getInstance().displayImage(userBean.getHeaderUrl(),iv_title,options);
                currUser = userBean;
                getAllMyVideos();
            }
        });
    }

    private void getAllMyVideos() {
        BmobQuery<VideoBean> query = new BmobQuery<>();
        //query.addWhereEqualTo("videoUser",currUser);
        query.findObjects(new FindListener<VideoBean>() {
            @Override
            public void done(List<VideoBean> list, BmobException e) {
                pd.dismiss();
                if (list != null && list.size() > 0) {
                    adapter.addList(list);
                } else {
                    Toast("没有视频");
                }

            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserDis();
    }

    private DisplayImageOptions options;
    private void initImageLoader(){
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
