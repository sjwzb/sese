package com.seu.magiccamera.common.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.seu.magiccamera.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianweishao on 2016/8/5.
 */
public class CoverAdapter extends RecyclerView.Adapter<CoverAdapter.CoverHolder> {
    private Context context;
    private List<Bitmap> bitmaps = new ArrayList<>();

    public CoverAdapter(Context context) {
        this.context = context;
    }

    @Override
    public CoverHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adapter_cover, null);
        CoverHolder coverHolder = new CoverHolder(view);
        return coverHolder;
    }

    @Override
    public void onBindViewHolder(CoverHolder holder, int position) {
        holder.iv_cover.setImageBitmap(bitmaps.get(position));
    }

    public void addItem(Bitmap bitmap) {
        if (bitmaps != null) {
            bitmaps.add(bitmap);
            notifyDataSetChanged();
        }
    }

    @Override
    public int getItemCount() {
        return bitmaps.size();
    }

    public class CoverHolder extends RecyclerView.ViewHolder {
        public ImageView iv_cover;

        public CoverHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View view) {
            iv_cover = (ImageView) view.findViewById(R.id.iv_cover);
        }
    }
}
