package com.sjw.youshi.Frament;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import com.qbw.customview.RefreshLoadMoreLayout;
import com.sjw.youshi.Bean.VideoBean;
import com.sjw.youshi.R;
import com.sjw.youshi.VideoDisActivity;
import com.sjw.youshi.adapter.MainVideoAdapter;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by jianweishao on 2016/8/2.
 */
public class FirstFrament extends BaseFrament implements RefreshLoadMoreLayout.CallBack {
    private GridView gv;
    //  private SwipeRefreshLayout srl;
    private MainVideoAdapter mainGVAdapter;
    private RefreshLoadMoreLayout loadMoreLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.frament_first, null);
        initView();
        initData();
        return contentView;
    }


    @Override
    public void initView() {
        gv = findView(R.id.gridview);
        loadMoreLayout = findView(R.id.refreshloadmore);
        loadMoreLayout.setCanRefresh(true);
        loadMoreLayout.setCanLoadMore(true);
        loadMoreLayout.startAutoRefresh();
        loadMoreLayout.init(new RefreshLoadMoreLayout.Config(this));
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(context, VideoDisActivity.class);
                intent.putExtra("videoBean", mainGVAdapter.getItem(position));
                startActivity(intent);
            }
        });

    }

    @Override
    public void initData() {

        mainGVAdapter = new MainVideoAdapter(context);
        gv.setAdapter(mainGVAdapter);

        // srl.setRefreshing(true);
       // getNetData(true);
        // mainGVAdapter.addList(list);
    }


    private void getNetData(final boolean isRe) {
        BmobQuery<VideoBean> bmobQuery = new BmobQuery<VideoBean>();
        if (isRe) {
            bmobQuery.setSkip(0);
        } else {
            bmobQuery.setSkip(mainGVAdapter.getList().size());
        }
        bmobQuery.setLimit(20);
        bmobQuery.findObjects(new FindListener<VideoBean>() {
            @Override
            public void done(List<VideoBean> list, BmobException e) {
                loadMoreLayout.stopLoadMore();
                loadMoreLayout.stopRefresh();
                //   srl.setRefreshing(false);

                if (list != null && list.size() > 0) {

                    //   for (int i = 0; i < 20; i++) {
//                         if (isRe) {
//                            mainGVAdapter.clearData();
//                        }
                    mainGVAdapter.addList(list);
                    //  }


                } else {
                    Toast.makeText(context, "亲，没有更多数据了", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onRefresh() {
        mainGVAdapter.clearData();
        getNetData(true);
    }

    @Override
    public void onLoadMore() {
        getNetData(false);
    }
}
