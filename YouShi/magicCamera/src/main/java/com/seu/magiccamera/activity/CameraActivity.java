package com.seu.magiccamera.activity;

import android.animation.Animator;
import android.animation.Animator.AnimatorListener;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Intent;
import android.opengl.GLSurfaceView;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.joyme.unitydemo.JMRecorder;
import com.seu.magiccamera.R;
import com.seu.magiccamera.common.view.FilterLayoutUtils;
import com.seu.magicfilter.camera.CameraEngine;
import com.seu.magicfilter.display.MagicCameraDisplay;

import java.io.File;

public class CameraActivity extends Activity {
    private MagicCameraDisplay mMagicCameraDisplay;
    private LinearLayout mFilterLayout;
    private TextView tv_time;
    private Handler timerHandler;
    private String filePath = "";
    private ImageView iv_start;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generat  ed method stub
        super.onCreate(savedInstanceState);

        setContentView(R.layout.meiyan_camera);
        timerHandler = new Handler();
        initMagicPreview();
        initFilterLayout();
        filePath = FileManager.getVideoPath(CameraActivity.this) + File.separator + "123.mp4";


    }

    private void initFilterLayout() {
        tv_time = (TextView) findViewById(R.id.tv_time);
        findViewById(R.id.btn_camera_filter).setOnClickListener(btn_camera_filter_listener);
        findViewById(R.id.btn_camera_closefilter).setOnClickListener(btn_camera_filter_close_listener);
        iv_start = (ImageView) findViewById(R.id.btn_camera_shutter);
        iv_start.setOnClickListener(btn_camera_shutter_listener);
        findViewById(R.id.btn_camera_album).setOnClickListener(btn_camera_album_listener);
        mFilterLayout = (LinearLayout) findViewById(R.id.layout_filter);
        FilterLayoutUtils mFilterLayoutUtils = new FilterLayoutUtils(this, mMagicCameraDisplay);
        mFilterLayoutUtils.init();
    }

    private void initMagicPreview() {
        GLSurfaceView glSurfaceView = (GLSurfaceView) findViewById(R.id.glsurfaceview_camera);
        //  FrameLayout.LayoutParams params = new LayoutParams(Constants.mScreenWidth, Constants.mScreenHeight);
        // glSurfaceView.setLayoutParams(params);
        mMagicCameraDisplay = new MagicCameraDisplay(this, glSurfaceView);
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mMagicCameraDisplay != null)
            mMagicCameraDisplay.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mMagicCameraDisplay != null)
            mMagicCameraDisplay.onResume();
    }

    @Override
    protected void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        if (mMagicCameraDisplay != null)
            mMagicCameraDisplay.onDestroy();
    }

    private OnClickListener btn_camera_shutter_listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            //mMagicCameraDisplay.onTakePicture(Constants.getOutputMediaFile(), null, null);
            iv_start.setEnabled(false);
            startRecord();

        }
    };

    private OnClickListener btn_camera_album_listener = new OnClickListener() {

        @Override
        public void onClick(View v) {

        }
    };

    private OnClickListener btn_camera_filter_listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mFilterLayout, "translationY", mFilterLayout.getHeight(), 0);
            animator.setDuration(200);
            animator.addListener(new AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {
                    findViewById(R.id.btn_camera_shutter).setClickable(false);
                    mFilterLayout.setVisibility(View.VISIBLE);
                }

                @Override
                public void onAnimationRepeat(Animator animation) {

                }

                @Override
                public void onAnimationEnd(Animator animation) {

                }

                @Override
                public void onAnimationCancel(Animator animation) {

                }
            });
            animator.start();
        }
    };

    private OnClickListener btn_camera_filter_close_listener = new OnClickListener() {

        @Override
        public void onClick(View v) {
            ObjectAnimator animator = ObjectAnimator.ofFloat(mFilterLayout, "translationY", 0, mFilterLayout.getHeight());
            animator.setDuration(200);
            animator.addListener(new AnimatorListener() {

                @Override
                public void onAnimationStart(Animator animation) {
                    // TODO Auto-generated method stub
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                    // TODO Auto-generated method stub

                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    // TODO Auto-generated method stub
                    mFilterLayout.setVisibility(View.INVISIBLE);
                    findViewById(R.id.btn_camera_shutter).setClickable(true);
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                    // TODO Auto-generated method stub
                    mFilterLayout.setVisibility(View.INVISIBLE);
                    findViewById(R.id.btn_camera_shutter).setClickable(true);
                }
            });
            animator.start();
        }
    };

    private void startTime() {
        timerHandler.postDelayed(timeRunnable, 0);
    }

    private int timeCount = 15;
    private Runnable timeRunnable = new Runnable() {
        @Override
        public void run() {
            timerHandler.postDelayed(this, 1000);
            tv_time.setText(String.valueOf(timeCount--) + "s");
            if (timeCount < 0) {
                stopRecord();
            }
        }
    };

    private void stopRecord() {
        iv_start.setEnabled(true);
        JMRecorder.getInstance().stopRecord(CameraActivity.this);
        CameraEngine.releaseCamera();
        timerHandler.removeCallbacks(timeRunnable);
        Intent intent = new Intent(this, VideoCoverActivity.class);
        intent.putExtra("videoPath", filePath);
        startActivity(intent);
        finish();
    }

    private void startRecord() {
        JMRecorder.getInstance().startVideo(CameraActivity.this, 240, 360, FileManager.getVideoPath(CameraActivity.this) + File.separator + "123.mp4", true);
        Log.d("youshi", filePath);
        startTime();
    }
}
