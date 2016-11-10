package com.seu.magiccamera.activity;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.MediaController;
import android.widget.VideoView;

import com.seu.magiccamera.R;
import com.seu.magiccamera.common.utils.VideoDecoder;

/**
 * Created by jianweishao on 2016/9/23.
 */
public class EditVideoActivity extends Activity {
    private Button btn_edit;
    private String videoPath;
    private VideoView videoView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_video);
        initView();
        initData();
        initVideoView();
    }

    private void initView() {
        videoView = (VideoView) findViewById(R.id.vv_edit);
        btn_edit = (Button) findViewById(R.id.btn_edit);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoPath != null) {
                    VideoDecoder decoder = new VideoDecoder();
                    decoder .decodeVideo(videoPath, 1*1000000, 6*1000000);
                }
            }
        });
    }

    private void initData() {
        videoPath = getIntent().getStringExtra("videoPath");
    }

    private void initVideoView() {
        videoView.setVideoPath(videoPath);
        videoView.setMediaController(new MediaController(this));
        videoView.start();
    }
}
