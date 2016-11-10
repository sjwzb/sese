package com.sjw.youshi;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sjw.youshi.Bean.UserBean;
import com.sjw.youshi.view.CameraOrPicDialog;
import com.sjw.youshi.view.SexDialog;

import java.io.File;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import cn.bmob.v3.listener.UploadFileListener;

/**
 * Created by jianweishao on 2016/10/13.
 */
public class EditUserInfoActivity extends BaseActivity {
    private ImageView iv_head;
    private EditText et_nick;
    private EditText et_qianming;
    private TextView tv_sex;
    private CameraOrPicDialog dialog;
    private File sdcardTempFile;
    private SexDialog sexDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_userinfo);
        initView();
        initData();
    }

    @Override
    public void initView() {
        iv_head = findView(R.id.iv_user_head);
        et_nick = findView(R.id.et_nick);
        tv_sex = findView(R.id.tv_sex);
        tv_sex.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                if (sexDialog == null) {
                    sexDialog = new SexDialog(context, new SexDialog.DialogClickListener() {
                        @Override
                        public void onClickListener(int viewId) {
                            switch (viewId) {
                                case R.id.tv_woman:
                                    tv_sex.setText("女");
                                    break;
                                case R.id.tv_man:
                                    tv_sex.setText("男");
                                    break;
                            }
                        }
                    });
                }
                if (!sexDialog.isShowing()) {
                    sexDialog.show();
                }
            }
        });
        et_qianming = findView(R.id.tv_qianming);
        findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadPic();
            }
        });
        sdcardTempFile = new File("/mnt/sdcard/", "tmp_pic_" + ".jpg");
        findView(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (dialog == null) {
                    dialog = new CameraOrPicDialog(context, new CameraOrPicDialog.DialogClickListener() {
                        @Override
                        public void onClickListener(int viewId) {
                            switch (viewId) {
                                case R.id.tv_camera:
                                    Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
                                    // intent.putExtra("output", Uri.fromFile(sdcardTempFile));
                                    intent.putExtra("crop", "true");
                                    intent.putExtra("aspectX", 1);// 裁剪框比例
                                    intent.putExtra("aspectY", 1);
                                    intent.putExtra("outputX", 80);// 输出图片大小
                                    intent.putExtra("outputY", 80);
                                    startActivityForResult(intent, 101);
                                    break;
                                case R.id.tv_album:
                                    Intent intent2 = new Intent("android.intent.action.PICK");
                                    intent2.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
                                    intent2.putExtra("output", Uri.fromFile(sdcardTempFile));
                                    intent2.putExtra("crop", "true");
                                    intent2.putExtra("aspectX", 1);// 裁剪框比例
                                    intent2.putExtra("aspectY", 1);
                                    intent2.putExtra("outputX", 80);// 输出图片大小
                                    intent2.putExtra("outputY", 80);
                                    startActivityForResult(intent2, 100);
                                    break;
                            }
                        }
                    });
                }
                if (!dialog.isShowing()) {
                    dialog.show();
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            Bitmap bmp = BitmapFactory.decodeFile(sdcardTempFile.getAbsolutePath());
            iv_head.setImageBitmap(bmp);

        }
    }

    @Override
    public void initData() {

    }

    private void uploadPic() {
        pd.show();
        pd.setMessage("头像上传中...");
        final BmobFile file = new BmobFile(sdcardTempFile);
        file.upload(new UploadFileListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast("头像上传成功");
                    saveUserInfo(file.getUrl());
                } else {
                    Toast("头像上传失败" + e.getMessage());
                }
            }
        });
    }

    private void saveUserInfo(String headUrl) {
        pd.setMessage("资料修改中...");
        String nick = et_nick.getText().toString();
        String sex = tv_sex.getText().toString();
        String qiaming = et_qianming.getText().toString();
        UserBean userBean = UserBean.getCurrentUser(UserBean.class);
        userBean.setNickName(nick);
        userBean.setSex(sex);
        userBean.setQinming(qiaming);
        userBean.setHeaderUrl(headUrl);
        userBean.update(userBean.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                pd.dismiss();
                if (e == null) {
                    Toast("个人资料更改成功");
                    finish();
                } else {
                    Toast("个人资料更改失败" + e.getMessage().toString());
                }
            }
        });
    }
}
