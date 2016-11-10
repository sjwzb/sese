package com.sjw.youshi.adapter;

import android.content.Context;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by jianweishao on 2016/7/29.
 */
public abstract class YouShiBaseAdapter<T> extends BaseAdapter {
    public List<T> adapterList = new ArrayList<T>();
    public Context context;

    public YouShiBaseAdapter(Context context) {
        this.context = context;
    }

    @Override
    public int getCount() {
        return adapterList.size();
    }

    @Override
    public T getItem(int position) {
        return adapterList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (null == convertView) {
            holder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(itemLayoutRes(), null);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        return getView(position, convertView, parent, holder);
    }

    public void addList(List<T> list) {
        adapterList.addAll(list);
        notifyDataSetChanged();
    }

    public void clearData() {
        if (adapterList != null) {
            adapterList.clear();
            notifyDataSetChanged();
        }
    }

    public void addItem(T t) {
        if (t != null) {
            adapterList.add(t);
            notifyDataSetChanged();
        }
    }

    ;

    /**
     * 使用该getView方法替换原来的getView方法，需要子类实现
     *
     * @param position
     * @param convertView
     * @param parent
     * @param holder
     * @return
     */
    public abstract View getView(int position, View convertView, ViewGroup parent, ViewHolder holder);

    /**
     * 改方法需要子类实现，需要返回item布局的resource id
     *
     * @return
     */
    public abstract int itemLayoutRes();

    public class ViewHolder {
        public SparseArray<View> views = new SparseArray<View>();

        /**
         * 指定resId和类型即可获取到相应的view
         *
         * @param convertView
         * @param resId
         * @param <T>
         * @return
         */
        public <T extends View> T obtainView(View convertView, int resId) {
            View v = views.get(resId);
            if (null == v) {
                v = convertView.findViewById(resId);
                views.put(resId, v);
            }
            return (T) v;
        }

    }
}
