package com.sjw.youshi;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.seu.magiccamera.activity.*;
import com.sjw.youshi.Bean.ADBean;
import com.sjw.youshi.Frament.FirstFrament;
import com.sjw.youshi.Frament.HuiyuanFrament;
import com.sjw.youshi.Frament.SecondFrament;
import com.sjw.youshi.utils.SPUtils;

import java.util.ArrayList;
import java.util.List;

import c.b.BP;
import cn.bmob.v3.Bmob;
import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.update.BmobUpdateAgent;

/**
 * Created by jianweishao on 2016/7/29.
 */
public class MainActivity extends BaseActivity {

    private String key = "479c1e1389befe61d37e26124ab15927";
    private ViewPager vp;
    private List<Fragment> fragments = new ArrayList<>();
    private FirstFrament firstFrament;
    private SecondFrament secondFrament;
    private HuiyuanFrament huiyuanFrament;
    private RadioGroup radioGroup, radioGroup1;
    private ImageView btn_zhibo;
    private RadioButton rb1, rb2, rb4, rb5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_main);
        Bmob.initialize(this, key);
        BP.init(this, key);
        BmobUpdateAgent.update(this);
        initView();
        initData();
        // initQUPAI();
    }

    @Override
    public void initView() {
//        findView(R.id.btn_ad).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // 显示推荐列表（软件）
//                AppConnect.getInstance(context).showAppOffers(context);
//
//            }
//        });
        vp = findView(R.id.vp);
        rb1 = findView(R.id.rb1);
        rb2 = findView(R.id.rb2);
        rb4 = findView(R.id.rb4);
        rb5 = findView(R.id.rb5);

    }

    @Override
    public void initData() {

        //第二：自v3.4.7版本开始,设置BmobConfig,允许设置请求超时时间、文件分片上传时每片的大小、文件的过期时间(单位为秒)，
//        BmobConfig config = new BmobConfig.Builder(this)
//                ////设置appkey
//                .setApplicationId(key)
//                ////请求超时时间（单位为秒）：默认15s
//                .setConnectTimeout(30)
//                ////文件分片上传时每片的大小（单位字节），默认512*1024
//                .setUploadBlockSize(1024 * 1024)
//                ////文件的过期时间(单位为秒)：默认1800s
//                .setFileExpiration(2500)
//                .build();
//        Bmob.initialize(config);
        if (firstFrament == null) {
            firstFrament = new FirstFrament();
        }
        if (secondFrament == null) {
            secondFrament = new SecondFrament();
        }
//        if (huiyuanFrament == null) {
//            huiyuanFrament = new HuiyuanFrament();
//        }
        fragments.clear();
        fragments.add(firstFrament);
        fragments.add(secondFrament);
        //fragments.add(huiyuanFrament);
        // fragments.add(secondFrament);
        VpFramentAdapter vpFramentAdapter = new VpFramentAdapter(getSupportFragmentManager());
        vp.setAdapter(vpFramentAdapter);
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        rb4.setChecked(true);

                        break;
                    case 1:
                        rb5.setChecked(true);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
        radioGroup = findView(R.id.rg);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb1:
                        vp.setCurrentItem(0, true);
                        break;
                    case R.id.rb2:
                        if (!isLogin()) {
                            Intent intent = new Intent(context, LoginActivity.class);
                            startActivity(intent);
                            return;
                        }
                        Intent intent = new Intent(context, CameraActivity.class);
                        startActivity(intent);
                        vp.setCurrentItem(1, true);

                        break;
                    case R.id.rb3:
                        //vp.setCurrentItem(1);
                        if (!isLogin()) {
                            Intent intent2 = new Intent(context, LoginActivity.class);
                            startActivity(intent2);
                            return;
                        }
//                        Intent intent1 = new Intent(context, UserActivity.class);
//                        intent1.putExtra("userID", BmobUser.getCurrentUser().getObjectId());
//                        startActivity(intent1);
                        vp.setCurrentItem(1);
                        break;
                }
            }
        });
        radioGroup1 = findView(R.id.rg1);
        radioGroup1.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                switch (checkedId) {
                    case R.id.rb4:
                        vp.setCurrentItem(0, true);
                        break;
                    case R.id.rb5:
//                        if (!isLogin()) {
//                            Intent intent = new Intent(context, LoginActivity.class);
//                            startActivity(intent);
//                            return;
//                        }
//                        Intent intent = new Intent(context, CameraActivity.class);
//                        startActivity(intent);
                        vp.setCurrentItem(1, true);

                        break;
                    case R.id.rb3:
                        //vp.setCurrentItem(1);
                        if (!isLogin()) {
                            Intent intent2 = new Intent(context, LoginActivity.class);
                            startActivity(intent2);
                            return;
                        }
//                        Intent intent1 = new Intent(context, UserActivity.class);
//                        intent1.putExtra("userID", BmobUser.getCurrentUser().getObjectId());
//                        startActivity(intent1);
                        vp.setCurrentItem(1);
                        break;
                }
            }
        });
        btn_zhibo = findView(R.id.btn_zhibo);
        btn_zhibo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  ZBUtils.openZBActivity(context);
                //  startRecord();
                // Log.d(TAG, "-------------");
                if (!isLogin()) {
                    Intent intent2 = new Intent(context, LoginActivity.class);
                    startActivity(intent2);
                    return;
                }
                Intent intent = new Intent(MainActivity.this, CameraActivity.class);
                startActivity(intent);
            }
        });
        // getAD();

    }


    private void getAD() {
        BmobQuery<ADBean> query = new BmobQuery<>();
        query.getObject("SP8sWWWD", new QueryListener<ADBean>() {
            @Override
            public void done(ADBean adBean, BmobException e) {
                if (e == null) {
                    if (adBean != null) {
                        SPUtils.saveBooler(context, "isOpen", adBean.isOpen());
                        Log.d(TAG, "isOpen" + adBean.isOpen());
                    }
                }
            }
        });
    }

    private class VpFramentAdapter extends FragmentPagerAdapter {

        public VpFramentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }


}
