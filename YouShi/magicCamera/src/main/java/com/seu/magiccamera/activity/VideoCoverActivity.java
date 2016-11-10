package com.seu.magiccamera.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.seu.magiccamera.R;
import com.seu.magiccamera.common.adapter.CoverAdapter;
import com.seu.magiccamera.common.utils.DisplayUtils;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by jianweishao on 2016/8/5.
 */
public class VideoCoverActivity extends Activity {
    private VideoView vv;
    private String path;
    private ImageView ivCover;
    private Button btn_play;
    private RecyclerView rv;
    private CoverAdapter coverAdapter;
    private boolean isFirst = true;
    public static Bitmap coverBitmap;
    private TextView btn_next;
    private String TAG = "VideoCoverActivity";
    private ImageView iv_select;
    private LinearLayout ll_cover;
    private List<Bitmap> bitmaps = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cover);
        initView();
        initData();
    }

    private float startX;
    private float startY;
    private boolean isFirstDown = true;

    public void initView() {
        vv = (VideoView) findViewById(R.id.vv_cover);
        ivCover = (ImageView) findViewById(R.id.iv_cover);
        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(linearLayoutManager);
        btn_play = (Button) findViewById(R.id.btn_play);
        btn_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPlay();
            }
        });
        btn_next = (TextView) findViewById(R.id.btn_next);
        btn_next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity();
            }
        });
        ll_cover = (LinearLayout) findViewById(R.id.ll_cover);
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        iv_select = (ImageView) findViewById(R.id.iv_select);
        iv_select.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                switch (event.getAction()) {
                    case MotionEvent.ACTION_DOWN:


                        break;
                    case MotionEvent.ACTION_MOVE:
                        float x = event.getRawX() - iv_select.getWidth() / 2;
                        if (x <= 0) {
                            x = 0;
                        }
                        if (x >= DisplayUtils.getDisplayW(VideoCoverActivity.this) - iv_select.getWidth()) {
                            x = DisplayUtils.getDisplayW(VideoCoverActivity.this) - iv_select.getWidth();
                        }
                        iv_select.setX(x);
                        selectImg(x);
                        //iv_select.setY(pointY);
                        break;
                    case MotionEvent.ACTION_UP:
                        break;
                }
                return true;
            }
        });
    }

    private void selectImg(float endX) {
        int imgW = DisplayUtils.getDisplayW(this) / allImg;
        int index = (int) (endX / imgW);
        iv_select.setImageBitmap(bitmaps.get(index));
        ivCover.setImageBitmap(bitmaps.get(index));
        coverBitmap = bitmaps.get(index);

    }

    public void initData() {
        path = getIntent().getStringExtra("videoPath");
        coverAdapter = new CoverAdapter(this);
        rv.setAdapter(coverAdapter);
        getFrameOfVideo();
    }

    private void startActivity() {
//        Intent intent = new Intent(this, UploadFileActivity.class);
//        intent.putExtra("videoPath", path);
//        startActivity(intent);
    }

    private void startPlay() {
        vv.setVideoURI(Uri.parse(path));
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                ivCover.setVisibility(View.GONE);
                mp.start();
            }
        });
        vv.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                return false;
            }
        });
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                ivCover.setVisibility(View.VISIBLE);
            }
        });
        vv.setMediaController(new MediaController(this));
    }

    private void getFrameOfVideo() {
        RetriveFrameTask retriveFrameTask = new RetriveFrameTask();
        retriveFrameTask.execute();
    }

    private int allImg;

    private class RetriveFrameTask extends AsyncTask<Void, Object, Void> {

        @Override
        protected Void doInBackground(Void... params) {
            try {
                MediaMetadataRetriever retriever = new MediaMetadataRetriever();
                retriever.setDataSource(path);
                // 取得视频的长度(单位为毫秒)
                // 取得视频的长度(单位为毫秒)
                String time = retriever.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
                Log.d(TAG, "video time " + time);
                // 取得视频的长度(单位为秒)
                int seconds = Integer.valueOf(time) / 1000;
                allImg = seconds;
                // 得到每一秒时刻的bitmap比如第一秒,第二秒
                for (int i = 1; i <= seconds; i++) {
                    Bitmap bitmap = retriever.getFrameAtTime(i * 1000 * 1000, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
                    if (bitmap != null) {
                        publishProgress(i, bitmap);
                    }
                }
                retriever.release();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onProgressUpdate(Object... values) {
            if (values == null || values.length < 2) {
                return;
            }
            Bitmap bitmap = (Bitmap) values[1];
            if (bitmap != null) {
                if (isFirst) {
                    ivCover.setImageBitmap(bitmap);
                    coverBitmap = bitmap;
                    iv_select.setImageBitmap(bitmap);
                    isFirst = false;
                }
                // coverAdapter.addItem(bitmap);
                bitmaps.add(bitmap);
                addCoverImg(bitmap);
            }

        }

        @Override
        protected void onCancelled() {
        }

        @Override
        protected void onPostExecute(Void aVoid) {

        }
    }

    private void addCoverImg(Bitmap bitmap) {
        ImageView imageView = new ImageView(this);
        int imgW = DisplayUtils.getDisplayW(this) / allImg;
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(imgW, LinearLayout.LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(params);
        imageView.setImageBitmap(bitmap);
        imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
        ll_cover.addView(imageView);
    }
}
