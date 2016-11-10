package com.sjw.youshi;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.pili.pldroid.player.PLMediaPlayer;
import com.pili.pldroid.player.widget.PLVideoView;
import com.sjw.youshi.Bean.CommentBean;
import com.sjw.youshi.Bean.UserBean;
import com.sjw.youshi.Bean.VideoBean;
import com.sjw.youshi.adapter.CommentAdapter;
import com.sjw.youshi.view.GiftDialog;
import com.sjw.youshi.view.SSMediaController;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;


public class VideoDisActivity extends BaseActivity implements PLMediaPlayer.OnPreparedListener,
        PLMediaPlayer.OnInfoListener,
        PLMediaPlayer.OnCompletionListener,
        PLMediaPlayer.OnVideoSizeChangedListener,
        PLMediaPlayer.OnErrorListener {

    private PLVideoView videoView;
    //    private String path = "http://bmob-cdn-5356.b0.upaiyun.com/2016/08/08/f31b89f111dd4570986c0f2f54726509.mp4";
    private String path = "http://flv2.bn.netease.com/tvmrepo/2016/4/G/O/EBKQOA8GO/SD/EBKQOA8GO-mobile.mp4";
    private GiftDialog giftDialog;
    private TextView tv_video_user_name, tv_play_count, tv_all_gold;
    private VideoBean videoBean;
    private EditText et_comment;
    private TextView btn_send;
    private RecyclerView rv_comment;
    private CommentAdapter adapter;
    private ImageView iv_user;
    private SimpleDraweeView iv_cover;
    private ImageView iv_play;
    private ProgressBar pb;
    private ImageView iv_close;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
//                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // path = "http://bmob-cdn-5356.b0.upaiyun.com/2016/08/09/38b7fb8e3070414fb5038d2479d055b9.mp4";
        initView();
        initData();
        initPlayer();
        getAllComment();
        initRepeat();

    }

    @Override
    protected void onResume() {
        super.onResume();
        getUserInfo(videoBean.getVideoUser().getObjectId());
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
        //iv_play.setVisibility(View.VISIBLE);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
        repHandler.removeCallbacks(repRunnable);
    }

    @Override
    public void initView() {
        pb = findView(R.id.pb);
        iv_close = findView(R.id.iv_close);
        iv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        pb.setVisibility(View.VISIBLE);
        iv_user = findView(R.id.iv_user_icon);
        iv_play = findView(R.id.iv_player_start);
        iv_play.setVisibility(View.GONE);
        iv_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //initPlayer();
                iv_play.setVisibility(View.GONE);
                iv_cover.setVisibility(View.GONE);
                videoView.start();
//                ijkVideoView.seekTo(0);
//                ijkVideoView.start();
            }
        });
        iv_cover = findView(R.id.iv_cover);
        //获取GenericDraweeHierarchy对象

        videoView = findView(R.id.vv);
        //View view = LayoutInflater.from(this).inflate(R.layout.view_video_item, null);
        // CustomMediaContoller mediaContoller = new CustomMediaContoller(this, view,ijkVideoView);
        //ijkVideoView.setMediaController(mediaContoller);
        findView(R.id.iv_gift).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isLogin()) {
                    Intent intent = new Intent(context, LoginActivity.class);
                    startActivity(intent);
                    return;
                }
                if (giftDialog == null) {
                    giftDialog = new GiftDialog(context, videoBean);
                }
                if (giftDialog.isShowing()) {
                    return;
                }
                giftDialog.show();
            }
        });
        tv_all_gold = findView(R.id.tv_all_glod);
        tv_play_count = findView(R.id.tv_play_count);
        tv_video_user_name = findView(R.id.tv_video_user_name);
        et_comment = findView(R.id.et_comment);
        btn_send = findView(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendComment();
            }
        });
        rv_comment = findView(R.id.rv_comment);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        rv_comment.setLayoutManager(layoutManager);


    }

    private void sendComment() {
        String comment = et_comment.getText().toString();
        if (TextUtils.isEmpty(comment)) {
            Toast("内容不能为空");
            return;
        }
        final CommentBean commentBean = new CommentBean();
        UserBean userBean = BmobUser.getCurrentUser(UserBean.class);
        commentBean.setCommentUser(userBean);
        commentBean.setComment(comment);
        commentBean.setCommentVideo(videoBean);
        commentBean.setVideoId(videoBean.getObjectId());
        commentBean.save(new SaveListener<String>() {
            @Override
            public void done(String s, BmobException e) {
                if (e == null) {
                    Toast("发表成功");
                    hideSoftInput();
                    et_comment.setText("");
                    if (adapter != null) {
                        adapter.addItem(commentBean);
                    }
                } else {
                    Toast(s);
                }
            }
        });
    }

    @Override
    public void initData() {
        videoBean = (VideoBean) getIntent().getSerializableExtra("videoBean");
        path = videoBean.getBmobFile().getFileUrl();
        addPlayCount(videoBean);
        tv_video_user_name.setText(videoBean.getVideoName());
        tv_play_count.setText(String.valueOf(videoBean.getPlayCount()) + "人看过");
        //  tv_all_gold.setText(String.valueOf(videoBean.getVideoUser().getAllGold()));
        adapter = new CommentAdapter(this);
        rv_comment.setAdapter(adapter);
        iv_cover.setImageURI(videoBean.getVideoCover().getFileUrl());

    }

    private void getUserInfo(final String userId) {
        BmobQuery<UserBean> query = new BmobQuery<>();
        query.getObject(userId, new QueryListener<UserBean>() {
            @Override
            public void done(UserBean userBean, BmobException e) {
                if (userBean != null) {
                    Log.d(TAG, "nick name  " + userBean.getNickName() + " head url " + userBean.getHeaderUrl());
                    tv_all_gold.setText(String.valueOf(userBean.getAllGold()));
                    if (TextUtils.isEmpty(userBean.getHeaderUrl())) {
                        ImageLoader.getInstance().displayImage(videoBean.getVideoCover().getFileUrl(), iv_user, roundOptions);
                    } else {
                        ImageLoader.getInstance().displayImage(userBean.getHeaderUrl(), iv_user, roundOptions);
                    }

                }
            }
        });

    }

    private void bindUserInfoToView(UserBean userBean) {

    }

    private void initPlayer() {
        videoView.setOnPreparedListener(this);
        videoView.setOnCompletionListener(this);
        videoView.setOnInfoListener(this);
        videoView.setOnVideoSizeChangedListener(this);
        videoView.setOnErrorListener(this);
       // videoView.setVideoURI(Uri.parse(videoBean.getBmobFile().getFileUrl()));
        videoView.setVideoURI(Uri.parse("rtmp://live.joyme.com/live/test4?auth_key=1480497750-0-0-54a8991f898e471c465e96fb9bfaccbc"));
        SSMediaController mediaController = new SSMediaController(this, false, false);
        mediaController.setAnchorView(videoView);
        videoView.setMediaController(mediaController);
        videoView.start();
        iv_cover.setVisibility(View.GONE);
        iv_play.setVisibility(View.GONE);
    }

    private void addPlayCount(VideoBean videoBean) {
        int count = videoBean.getPlayCount();
        videoBean.setPlayCount(count + 1);
        videoBean.update(videoBean.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Toast("修改成功 " + e.toString());
                } else {
                    Log.d("UPDATE ", e.toString());
                }


            }
        });
    }

    private void getAllComment() {
        BmobQuery<CommentBean> query = new BmobQuery<>();
        query.addWhereEqualTo("videoId", videoBean.getObjectId());
        query.findObjects(new FindListener<CommentBean>() {
            @Override
            public void done(List<CommentBean> list, BmobException e) {
                if (e == null && list != null && list.size() > 0) {
                    adapter.addAll(list);
                }
            }
        });
    }

    @Override
    public void onCompletion(PLMediaPlayer plMediaPlayer) {
        iv_play.setVisibility(View.VISIBLE);

    }

    @Override
    public boolean onError(PLMediaPlayer plMediaPlayer, int i) {
        return false;
    }

    @Override
    public boolean onInfo(PLMediaPlayer plMediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(PLMediaPlayer plMediaPlayer) {
        if (plMediaPlayer != null) {
            pb.setVisibility(View.GONE);
            plMediaPlayer.start();
        }
    }

    @Override
    public void onVideoSizeChanged(PLMediaPlayer plMediaPlayer, int i, int i1) {

    }

    private void initRepeat() {
        repHandler.postDelayed(repRunnable, 0);
    }

    private Handler repHandler = new Handler();
    private Runnable repRunnable = new Runnable() {
        @Override
        public void run() {
            rv_comment.scrollBy(0, 20);
            repHandler.postDelayed(this, 100);
        }
    };
}
