package com.sjw.youshi.Frament;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.qbw.customview.RefreshLoadMoreLayout;
import com.sjw.youshi.Bean.VideoBean;
import com.sjw.youshi.R;
import com.sjw.youshi.VideoDisActivity;
import com.sjw.youshi.adapter.MainVideoAdapter;
import com.sjw.youshi.utils.BmobPayUtils;
import com.sjw.youshi.utils.SPUtils;

import java.util.List;

import c.b.BP;
import c.b.PListener;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by jianweishao on 2016/8/2.
 */
public class HuiyuanFrament extends BaseFrament implements RefreshLoadMoreLayout.CallBack {
    private GridView gv;
    //  private SwipeRefreshLayout srl;
    private MainVideoAdapter mainGVAdapter;
    private RefreshLoadMoreLayout loadMoreLayout;
    private TextView tv_no_huiyuan;
    private String key = "isPay";

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.frament_huiyuan_first, null);
        initView();
        initData();
        return contentView;
    }


    @Override
    public void initView() {
        gv = findView(R.id.gridview);
        tv_no_huiyuan = findView(R.id.tv_no_huiyuan);
        tv_no_huiyuan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (BmobPayUtils.initPay(context)) {
                    BP.pay("色色会员", "终生使用", 9.9, false, new PListener() {
                        @Override
                        public void orderId(String s) {

                        }

                        @Override
                        public void succeed() {
                            Toast("会员购买成功");
                            SPUtils.saveBooler(context, key, true);
                        }

                        @Override
                        public void fail(int i, String s) {
                            Toast("会员购买失败" + s);
                        }

                        @Override
                        public void unknow() {
                            Toast("会员购买失败");
                        }
                    });
                }
            }
        });
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
        if (SPUtils.getSpBoolean(context, key)) {
            tv_no_huiyuan.setVisibility(View.GONE);
            loadMoreLayout.setVisibility(View.VISIBLE);
        } else {
            tv_no_huiyuan.setVisibility(View.VISIBLE);
            loadMoreLayout.setVisibility(View.GONE);
        }
        // srl.setRefreshing(true);
        // getNetData(true);
        // mainGVAdapter.addList(list);
    }


    private void getNetData(final boolean isRe) {
        BmobQuery<VideoBean> bmobQuery = new BmobQuery<VideoBean>();
        bmobQuery.addWhereEqualTo("isPay",true);
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
    public void onResume() {
        super.onResume();
        if (SPUtils.getSpBoolean(context, key)) {
            tv_no_huiyuan.setVisibility(View.GONE);
            loadMoreLayout.setVisibility(View.VISIBLE);
        } else {
            tv_no_huiyuan.setVisibility(View.VISIBLE);
            loadMoreLayout.setVisibility(View.GONE);
        }
    }

    @Override
    public void onLoadMore() {
        getNetData(false);
    }
}
