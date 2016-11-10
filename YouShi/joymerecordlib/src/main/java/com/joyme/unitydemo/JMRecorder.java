package com.joyme.unitydemo;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import com.joyme.recordlib.GlUtil;
import com.joyme.recordlib.RecordFBO;
import com.joyme.recordlib.RenderFbo;
import com.joyme.recordlib.RenderScreen;
import com.joyme.recordlib.media.AudioManager;
import com.joyme.recordlib.media.MediaAudioEncoder;
import com.joyme.recordlib.media.MediaConfig;
import com.joyme.recordlib.media.MediaEncoder;
import com.joyme.recordlib.media.MediaMuxerWrapper;
import com.joyme.recordlib.media.MediaScreenEncoder;
import com.joyme.recordlib.service.ScreenRecorderService;

import java.io.IOException;


/**
 * Created by xianfengli on 2016/4/18.
 */
public class JMRecorder {

    private static JMRecorder instance;
    private RenderListener mRenderListener;
    private String TAG = "JMRecorder";
    private RenderFbo renderFbo;
    private RenderScreen renderScreen;
    private boolean isStop;
    private OnStrartRecordListener listener;

    public OnStrartRecordListener getListener() {
        return listener;
    }

    public void setListener(OnStrartRecordListener listener) {
        this.listener = listener;
    }

    public static JMRecorder getInstance() {
        if (instance == null) {
            instance = new JMRecorder();
        }
        return instance;
    }

    private JMRecorder() {
    }


    /**
     * `
     *
     * @param context
     * @param w
     * @param h
     * @param filePath      视频路径
     * @param isAudioRecord 是否录制声音
     */
    private static MediaMuxerWrapper sMuxer;

    public void startVideo(Context context, int w, int h, String filePath, boolean isAudioRecord) {
        Log.d(TAG, "W " + w + "  h " + h);
        if (sMuxer == null) {
            try {
                sMuxer = new MediaMuxerWrapper(filePath);    // if you record audio only, ".m4a" is also OK.
                if (true) {
                    // for screen capturing
                    new MediaScreenEncoder(context, sMuxer, mMediaEncoderListener,
                            w, h);
                }
                if (MediaConfig.getInstance().isAudioRecord) {
                    AudioManager manager = new AudioManager();
                    boolean b = manager.initAudio();
                    Log.d(TAG, " is audio " + b);
                    if (b) {
                        new MediaAudioEncoder(sMuxer, mMediaEncoderListener, manager.getAudioRecord());
                    }

                }
                sMuxer.prepare();
                sMuxer.startRecording();
            } catch (final IOException e) {
                Log.e(TAG, "startScreenRecord:", e);
            }
        } else {
            Toast.makeText(context, "已经开始录制了", Toast.LENGTH_SHORT);
        }
    }


    public void setRenderListener(RenderListener listener) {
        this.mRenderListener = listener;
    }

    public void Update() {
    }

    public void OnPreRender() {
        // JMRecorderNative.bineFbo(1);

    }

    public void OnPostRender() {
        if (mRenderListener != null) {
            mRenderListener.onPostRender();
        }
    }

    private boolean isRecorder = true;

    boolean first = true;
    private int[] rendWidth = new int[1];
    private RecordFBO recordFBO;


    public void OnRenderImage(int id) {//最新接口
//        isRecorder = !isRecorder;
//        if (!isRecorder) {
//            return;
//        }

        //  GLES30.glGetRenderbufferParameteriv(GLES30.GL_RENDERBUFFER,GLES30.GL_RENDERBUFFER_WIDTH,rendWidth,0);
        GlUtil.checkEglError("getRendWidth---");
        // Log.d(TAG,"OnRenderImage rendWidth "+rendWidth[0]);
        //Log.d(TAG, "unityPlayer  W " +unityPlayer.getWidth() + " unityPlayer H " + unityPlayer.getHeight());
        // Log.d(TAG, "screenW " +unityPlayer.getWidth() + " screenH " + unityPlayer.getHeight());

        if (mRenderListener != null) {
            if (id == 0) {
                if (recordFBO == null) {
                    recordFBO = new RecordFBO(MediaConfig.screenWidth, MediaConfig.screenHeight);
                }
                recordFBO.blitToFbo();
                id = recordFBO.getTexId();
            }
            mRenderListener.onRenderImage(id);
        }

        //saveTexture(texturdId, MediaConfig.screenWidth, MediaConfig.screenHeight);
    }


    public void stopRecord(Context context) {
        mRenderListener = null;
        if (sMuxer != null) {
            sMuxer.stopRecording();
            sMuxer = null;
        }

    }

    public void resumeRecord(Context context) {
        final Intent intent = new Intent(context, ScreenRecorderService.class);
        intent.setAction(ScreenRecorderService.ACTION_RESUME);
        context.startService(intent);
    }

    public void pauseRecord(Context context) {
        final Intent intent = new Intent(context, ScreenRecorderService.class);
        intent.setAction(ScreenRecorderService.ACTION_PAUSE);
        context.startService(intent);
    }

    public interface RenderListener {
        void onRenderImage(int textId);

        void onPostRender();
    }

    /**
     * 录制coco2d-x
     */
    private int farmeCount;

    public void onDrawFrame(int texID, int w, int h) {
        farmeCount++;
        if (mRenderListener != null) {
            if (texID == 0) {
                if (recordFBO == null) {
                    recordFBO = new RecordFBO(w, h);
                }
                recordFBO.blitToFbo();
                mRenderListener.onRenderImage(recordFBO.getTexId());
            } else {
                mRenderListener.onRenderImage(recordFBO.getTexId());
            }


        }
    }


    public void onSurfaceCreate() {

//        if (renderFbo == null) {
//            Log.d(TAG, " " + MediaConfig.screenWidth + " " + MediaConfig.screenHeight);
//            renderFbo = new RenderFbo(MediaConfig.screenWidth, MediaConfig.screenHeight);
//        }
//        if (renderScreen == null) {
//            renderScreen = new RenderScreen(MediaConfig.screenWidth, MediaConfig.screenHeight, renderFbo.getFboTexId());
//            renderScreen.setSize(MediaConfig.screenWidth, MediaConfig.screenHeight);
//        }
    }

    /**
     * callback methods from encoder
     */
    private static final MediaEncoder.MediaEncoderListener mMediaEncoderListener = new MediaEncoder.MediaEncoderListener() {
        @Override
        public void onPrepared(final MediaEncoder encoder) {
            Log.d("JMRecorder", "onPrepared ");
        }

        @Override
        public void onStopped(final MediaEncoder encoder) {
            Log.d("JMRecorder", "stop luzhi ");
        }
    };
}
