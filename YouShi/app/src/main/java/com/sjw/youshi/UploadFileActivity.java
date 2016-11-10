package com.sjw.youshi;

import android.graphics.Bitmap;
import android.graphics.pdf.PdfRenderer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.sjw.youshi.Bean.UserBean;
import com.sjw.youshi.Bean.VideoBean;

import java.io.File;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.ProgressCallback;
import cn.bmob.v3.listener.UploadFileListener;
import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;

/**
 * Created by jianweishao on 2016/8/5.
 */
public class UploadFileActivity extends BaseActivity {
    private ImageView iv_cover;
    private String videoPath;
    private Button btnUpload;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);
        initView();
        initData();
    }

    @Override
    public void initView() {
        iv_cover = findView(R.id.iv_cover);
        btnUpload = findView(R.id.btn_upload);
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadFile();
            }
        });

    }

    @Override
    public void initData() {
     //   Bitmap bitmap = VideoCoverActivity.coverBitmap;
        videoPath = getIntent().getStringExtra("videoPath");
//        if (bitmap != null) {
//            iv_cover.setImageBitmap(bitmap);
//        }
    }

    private void uploadFile() {
        File file = new File(videoPath);
        final BmobFile bmobFile = new BmobFile(file);
        bmobFile.uploadObservable(new ProgressCallback() {//上传文件操作
            @Override
            public void onProgress(Integer value, long total) {
                Log.d(TAG, "----progeress " + value + "  total " + total);
            }
        }).doOnNext(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {

            }
        }).concatMap(new Func1<Void, Observable<String>>() {//将bmobFile保存到movie表中
            @Override
            public Observable<String> call(Void aVoid) {
                VideoBean vb = new VideoBean();
                vb.setVideoName("sjw youshi ");
                vb.setBmobFile(bmobFile);
                vb.setVideoUser(BmobUser.getCurrentUser(UserBean.class));
                return saveObservable(vb);
            }
        }).concatMap(new Func1<String, Observable<String>>() {//下载文件
            @Override
            public Observable<String> call(String s) {
                return bmobFile.downloadObservable(new ProgressCallback() {
                    @Override
                    public void onProgress(Integer value, long total) {
                    }
                });
            }
        }).subscribe(new Subscriber<String>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable e) {
            }

            @Override
            public void onNext(String s) {
            }
        });
    }

    /**
     * save的Observable
     *
     * @param obj
     * @return
     */
    private Observable<String> saveObservable(BmobObject obj) {
        return obj.saveObservable();
    }
}
