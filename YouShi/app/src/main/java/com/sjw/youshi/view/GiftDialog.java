package com.sjw.youshi.view;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.sjw.youshi.Bean.GiftBean;
import com.sjw.youshi.Bean.UserBean;
import com.sjw.youshi.Bean.UserGiftHouse;
import com.sjw.youshi.Bean.VideoBean;
import com.sjw.youshi.R;
import com.sjw.youshi.adapter.GiftAdapter;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by jianweishao on 2016/8/9.
 */
public class GiftDialog extends Dialog {
    private GridView rl_gift;
    private Context context;
    private GiftAdapter giftAdapter;
    private VideoBean videoBean;
    private String[] giftNames = new String[]{"爱心", "玫瑰", "口红", "套套", "香水", "包包", "钻戒", "跑车"};
    private int[] giftIcons = {R.drawable.gift1, R.drawable.gift2, R.drawable.gift3, R.drawable.gift4, R.drawable.gift5, R.drawable.gift6, R.drawable.gift7, R.drawable.gift8};
    private int[] giftPrices = {8, 38, 68, 98, 188, 288, 588, 988};
    private int selectIndex;
    private TextView tv_glod;

    public GiftDialog(Context context, VideoBean videoBean) {
        super(context, R.style.Dialog);
        this.context = context;
        this.videoBean = videoBean;
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialg_gift);
        Window dialogWindow = getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        dialogWindow.setAttributes(layoutParams);
        initView();
        initData();
        getUserInfo();
    }

    private void initView() {
        tv_glod = (TextView) findViewById(R.id.tv_gold);
        rl_gift = (GridView) findViewById(R.id.rl_gift);
        rl_gift.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                giftAdapter.setSelect(position);
                selectIndex = position;

            }
        });
        findViewById(R.id.btn_send_gift).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int gold = Integer.valueOf(giftAdapter.getItem(selectIndex).getGiftPrice());
                int myGold = myUser.getAllGold();
                if (gold > myGold) {
                    Toast.makeText(context, "金币不足", Toast.LENGTH_SHORT).show();
                    return;
                }
                sendGift(giftAdapter.getItem(selectIndex));
            }
        });

    }

    private void initData() {
        giftAdapter = new GiftAdapter(context);
        for (int i = 0; i < giftNames.length; i++) {
            GiftBean giftBean = new GiftBean();
            giftBean.setGiftName(giftNames[i]);
            giftBean.setSelect(false);
            giftBean.setGiftIcon(giftIcons[i]);
            giftBean.setGiftPrice(String.valueOf(giftPrices[i]));
            giftBean.setVideoId(videoBean.getObjectId());
            giftBean.setUserBean(BmobUser.getCurrentUser(UserBean.class));
            giftAdapter.addItem(giftBean);
        }
        rl_gift.setAdapter(giftAdapter);

    }

    private void sendGift(GiftBean giftBean) {
        UserGiftHouse userGiftHouse = new UserGiftHouse();
        UserBean userBean = BmobUser.getCurrentUser(UserBean.class);
        userGiftHouse.setFromUser(userBean);
        userGiftHouse.setGlod(giftBean.getGiftPrice());
        userGiftHouse.setGiftBean(giftBean);
        userGiftHouse.setUserID(giftBean.getUserBean().getObjectId());
        userGiftHouse.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast.makeText(context, "送礼成功", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(context, s + e.toString(), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private UserBean myUser;

    private void getUserInfo() {
        BmobQuery<UserBean> query = new BmobQuery<>();
        query.getObject(BmobUser.getCurrentUser().getObjectId(), new QueryListener<UserBean>() {
            @Override
            public void done(UserBean userBean, BmobException e) {
                if (userBean != null) {
                    myUser = userBean;
                    tv_glod.setText(String.valueOf(userBean.getAllGold()) + "金币");
                }
            }
        });
    }

    @Override
    public void show() {
        super.show();
        getUserInfo();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
