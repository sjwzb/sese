package com.sjw.youshi.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sjw.youshi.Bean.GiftBean;
import com.sjw.youshi.Bean.VideoBean;
import com.sjw.youshi.R;
import com.sjw.youshi.utils.DisplayUtils;

import java.util.List;

/**
 * Created by jianweishao on 2016/8/5.
 */
public class GiftAdapter extends YouShiBaseAdapter<GiftBean> {
    private Context context;

    public GiftAdapter(Context context) {
        super(context);
        this.context = context;

    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent, ViewHolder holder) {
        LinearLayout ll_bg = holder.obtainView(convertView, R.id.ll_bg);
        TextView tv_name = holder.obtainView(convertView, R.id.tv_gift_name);
        TextView tv_play = holder.obtainView(convertView, R.id.tv_gift_price);
        ImageView iv_cover = holder.obtainView(convertView, R.id.iv_gift);
        GiftBean giftBean = adapterList.get(position);
        if (giftBean.isSelect()) {
            ll_bg.setBackgroundResource(R.drawable.shapebg7);
        } else {
            ll_bg.setBackgroundResource(0);
        }
        tv_name.setText(giftBean.getGiftName());
        tv_play.setText(giftBean.getGiftPrice() + "金币");
        iv_cover.setImageResource(giftBean.getGiftIcon());
        return convertView;
    }

    @Override
    public int itemLayoutRes() {
        return R.layout.item_gift;
    }

    public List<GiftBean> getList() {
        return adapterList;
    }

    public void setSelect(int index) {
        for (int i = 0; i < adapterList.size(); i++) {
            if (index == i) {
                adapterList.get(i).setSelect(true);
            } else {
                adapterList.get(i).setSelect(false);
            }
        }
        notifyDataSetChanged();
    }

}
