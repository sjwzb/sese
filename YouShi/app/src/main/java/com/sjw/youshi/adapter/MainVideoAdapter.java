package com.sjw.youshi.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.facebook.drawee.drawable.ScalingUtils;
import com.facebook.drawee.generic.GenericDraweeHierarchy;
import com.facebook.drawee.generic.GenericDraweeHierarchyBuilder;
import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.sjw.youshi.Bean.VideoBean;
import com.sjw.youshi.R;
import com.sjw.youshi.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianweishao on 2016/8/5.
 */
public class MainVideoAdapter extends YouShiBaseAdapter<VideoBean> {
    private Context context;
    private ImageLoader imageLoader;
    private DisplayImageOptions options;

    public MainVideoAdapter(Context context) {
        super(context);
        this.context = context;
        imageLoader = ImageLoader.getInstance();
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

    @Override
    public View getView(int position, View convertView, ViewGroup parent, ViewHolder holder) {
        LinearLayout ll = holder.obtainView(convertView, R.id.ll);
        TextView tv_name = holder.obtainView(convertView, R.id.tv_video_name);
        TextView tv_play = holder.obtainView(convertView, R.id.tv_count);
        ImageView iv_cover = holder.obtainView(convertView, R.id.iv);
        VideoBean videoBean = adapterList.get(position);
        tv_name.setText(videoBean.getVideoName());
        tv_play.setText(videoBean.getPlayCount() + "");
        //holder.iv_cover.setHierarchy(hierarchy);
        iv_cover.getLayoutParams().width = DisplayUtils.getDisplayW((Activity) context) / 2 - 2;
        iv_cover.getLayoutParams().height = DisplayUtils.getDisplayW((Activity) context) / 2 - 2;
        iv_cover.setImageResource(0);
        //  Uri uri = Uri.parse(videoBean.getVideoCover().getFileUrl());
        imageLoader.displayImage(videoBean.getVideoCover().getFileUrl(), iv_cover, options);
        //holder.iv_cover.setImageURI(uri);

        return convertView;
    }

    @Override
    public int itemLayoutRes() {
        return R.layout.adapter_gv;
    }

    public List<VideoBean> getList() {
        return adapterList;
    }


}
