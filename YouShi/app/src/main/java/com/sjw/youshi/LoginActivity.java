package com.sjw.youshi;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.sjw.youshi.Bean.UserBean;

import cn.bmob.v3.Bmob;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * Created by jianweishao on 2016/8/1.
 */
public class LoginActivity extends BaseActivity {
    private EditText et_name, et_pwd;
    private Button btn_login;
    private String name, pwd;
    private UserBean userBean;
    private String key = "479c1e1389befe61d37e26124ab15927";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bmob.initialize(this, key);
        setContentView(R.layout.layout_login);
        initView();
        initData();

    }

    @Override
    public void initView() {
        et_name = findView(R.id.et_name);
        et_pwd = findView(R.id.et_pwd);
        btn_login = findView(R.id.btn_login);
        findView(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        findView(R.id.btn_regist).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, RegistActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    public void initData() {

    }

    private void login() {
        name = et_name.getText().toString().trim().replaceAll(" ", "");
        pwd = et_pwd.getText().toString().trim().replaceAll(" ", "");
        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(pwd)) {
            Toast("用户名和密码不能为空");
        }
        if (userBean == null) {
            userBean = new UserBean();
        }
        userBean.setUsername(name);
        userBean.setPassword(pwd);
        userBean.login(new SaveListener<UserBean>() {
            @Override
            public void done(UserBean userBean, BmobException e) {
                if (e == null) {
                    Toast("登陆成功");
                    finish();
                } else {
                    Toast(e.toString());
                }
            }
        });
    }
}
