package com.sjw.youshi;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sjw.youshi.Bean.UserBean;

import cn.bmob.v3.BmobSMS;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

/**
 * Created by jianweishao on 2016/8/1.
 */
public class RegistActivity extends BaseActivity {
    private EditText et_name, et_pwd;
    private Button btn_login;
    private String name, pwd;
    private UserBean userBean;
    private Button btn_coed;
    private EditText et_code;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_regist);
        initView();
        initData();
    }

    @Override
    public void initView() {
        et_name = findView(R.id.et_name);
        et_pwd = findView(R.id.et_pwd);
        btn_login = findView(R.id.btn_login);
        et_code = findView(R.id.et_code);
        findView(R.id.btn_regist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });
        findView(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                verCode(et_code.getText().toString(), et_name.getText().toString());
            }
        });
        btn_coed = findView(R.id.btn_code);
        btn_coed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String phoneNum = et_name.getText().toString();
                getVerCode(phoneNum);
            }
        });
    }

    private void getVerCode(String phoneNum) {
        handler.postDelayed(timeRunnable, 1000);
        btn_coed.setEnabled(false);
        BmobSMS.requestSMSCode(phoneNum, "色色", new QueryListener<Integer>() {
            @Override
            public void done(Integer integer, BmobException e) {
                if (e == null) {
                    Toast("验证码发送成功");
                } else {
                    Toast(e.getMessage().toString());
                }
            }
        });
    }

    private void verCode(String code, String phoneNum) {
        if (TextUtils.isEmpty(code)) {
            Toast("验证码不能为空");
            return;
        }
        BmobSMS.verifySmsCode(phoneNum, code, new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    regist();
                } else {
                    Toast(e.getMessage().toString());
                }
            }
        });
    }

    @Override
    public void initData() {

    }

    private void regist() {
        name = et_name.getText().toString().trim().replaceAll(" ", "");
        pwd = et_pwd.getText().toString().trim().replaceAll(" ", "");
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
            Toast("用户名和密码不能为空");
        }
        userBean = new UserBean();
        userBean.setUsername(name);
        userBean.setPassword(pwd);
        userBean.setNickName("sjw");
        userBean.signUp(new SaveListener<UserBean>() {
            @Override
            public void done(UserBean userBean, BmobException e) {
                if (e == null) {
                    Toast("注册成功:");
                    finish();
                } else {
                    Toast(e.toString());
                    Log.d(TAG, "注册失败 " + e.toString());
                }
            }
        });
    }

    private Handler handler = new Handler();
    private int tmieCount = 60;
    private Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            btn_coed.setText(String.valueOf(tmieCount--) + "s");
            if (tmieCount <= 0) {
                btn_coed.setEnabled(true);
                btn_coed.setText("获取验证码");
                handler.removeCallbacks(this);
            }
            handler.postDelayed(this, 1000);
        }
    };

}
