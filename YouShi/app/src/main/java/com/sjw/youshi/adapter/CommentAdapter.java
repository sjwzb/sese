package com.sjw.youshi.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sjw.youshi.Bean.CommentBean;
import com.sjw.youshi.Bean.GiftBean;
import com.sjw.youshi.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianweishao on 2016/8/5.
 */
public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyHolder> {
    private Context context;
    private List<CommentBean> list = new ArrayList<>();

    public CommentAdapter(Context context) {
        this.context = context;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_comment, null);
        MyHolder coverHolder = new MyHolder(view);
        return coverHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        CommentBean commentBean=list.get(position%(list.size()==0?1:list.size()));
       // holder.tv_name.setText(commentBean.getCommentUser().getNickName());
        holder.tv_comment.setText(commentBean.getComment());

    }

    public void addItem(CommentBean giftBean) {
        if (list != null) {
            list.add(giftBean);
            notifyDataSetChanged();
        }
    }

    public void addAll(List<CommentBean> list) {
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size()==0?0:Integer.MAX_VALUE;
    }

    public class MyHolder extends RecyclerView.ViewHolder {
        public ImageView iv_comment;
        public TextView tv_name, tv_comment;

        public MyHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View view) {
        //    iv_comment = (ImageView) view.findViewById(R.id.iv_comment);
            tv_comment = (TextView) view.findViewById(R.id.tv_comment);
          //  tv_name = (TextView) view.findViewById(R.id.tv_comment_name);
        }
    }

}
