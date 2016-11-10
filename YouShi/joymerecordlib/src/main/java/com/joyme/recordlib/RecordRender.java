package com.joyme.recordlib;

import android.opengl.EGLContext;
import android.opengl.Matrix;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.view.Surface;

import com.joyme.recordlib.gles.EglCore;
import com.joyme.recordlib.gles.FullFrameRect;
import com.joyme.recordlib.media.MediaConfig;

import java.lang.ref.WeakReference;
import java.nio.ByteBuffer;

/**
 * Created by jianweishao on 2016/7/20.
 */
public class RecordRender implements Runnable {

    public static RecordRender instance;
    private InputSurface mInputSurface;
    private EglCore eglCore;
    private EGLContext shareContext;
    private Surface surface;
    private FullFrameRect frameRect;
    private static float[] mIdentityMatrix;
    private static final int HANDLER_DRAW = 1;
    private static final int HANDLER_CREATE_SURFACE = 0;
    private static final int HANDLER_MSG_QUIT = 3;
    private boolean mReady;
    private boolean mRunning;
    private String TAG = "RecordRender";
    private int w;
    private int h;

    public RecordRender(Surface surface, EGLContext shareContext, int w, int h) {
        this.surface = surface;
        this.shareContext = shareContext;
        mIdentityMatrix = new float[16];
        Matrix.setIdentityM(mIdentityMatrix, 0);
        this.w = w;
        this.h = h;

    }


    public void createRecordSurface() {
        if (shareContext != null) {
            Log.d(TAG, "shareContext is  not null");
        }
//        if (eglCore == null) {
//            eglCore = new EglCore(shareContext, EglCore.FLAG_RECORDABLE | EglCore.FLAG_TRY_GLES3);
//        }
        if (mInputSurface == null) {
            mInputSurface = new InputSurface(surface, shareContext);
            mInputSurface.makeCurrent();
        }

//        if (frameRect == null) {
//            frameRect = new FullFrameRect(new Texture2dProgram(Texture2dProgram.ProgramType.TEXTURE_2D));
//        }
        surfaceIsCreate = true;
    }

    private ByteBuffer lastBuffer;

    public void drawFrame(int texId) throws Exception{

//        GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,texId);
//        GLES20.glTexSubImage2D(GLES20.GL_TEXTURE_2D,0,);
//        frameRect.drawFrame(texId, mIdentityMatrix);

        if (mInputSurface != null) {
            drawTexId(texId);
            mInputSurface.setPresentationTime(System.nanoTime());
            mInputSurface.swapBuffers();
        }
    }

    private RenderSrfTex renderSrfTex;

    private void drawTexId(int texId) {
        if (renderSrfTex == null) {
            renderSrfTex = new RenderSrfTex(w, h);
        }

        renderSrfTex.draw(texId);
    }

    public void handleRelease() {

        handerQuitThread();
    }

    public void release() {
        Log.d(TAG, "release------------");
        if (eglCore != null) {
            eglCore.release();
            eglCore = null;
        }
        if (mInputSurface != null) {
            mInputSurface.release();
            mInputSurface = null;
        }
        if (frameRect != null) {
            frameRect.release(true);
        }
        if (surface != null) {
            surface.release();
        }

        surfaceIsCreate = false;
    }

    public void handerCreateRecordSurface() {
        synchronized (mReadyFence) {
            if (!mReady) {
                return;
            }
        }
        if (renderHandler != null) {
            renderHandler.sendEmptyMessage(HANDLER_CREATE_SURFACE);
        }

    }

    public void handerQuitThread() {
        if (renderHandler != null) {
            renderHandler.sendEmptyMessage(HANDLER_MSG_QUIT);
        }
    }

    public void handerDrawFrame(int texId) {
        synchronized (mReadyFence) {
            if (!mReady) {
                return;
            }
        }
        if (renderHandler != null && surfaceIsCreate) {
            renderHandler.sendMessage(renderHandler.obtainMessage(HANDLER_DRAW, texId));
        }

    }

    private RecordRenderHandler renderHandler;

    @Override
    public void run() {
        Log.d(TAG, "run---------");
        Looper.prepare();
        synchronized (mReadyFence) {
            if (renderHandler == null) {
                renderHandler = new RecordRenderHandler(this);
            }
            mReady = true;
            mReadyFence.notify();
        }
        Looper.loop();
        synchronized (mReadyFence) {
            mReady = mRunning = false;
            renderHandler = null;
            Log.d(TAG, "--------" + mReady);
        }

    }

    private Object mReadyFence = new Object();
    private boolean surfaceIsCreate;

    public void start() {
        Log.d(TAG, "Encoder: startRecording()");
        synchronized (mReadyFence) {
            if (mRunning) {
                Log.w(TAG, "Encoder thread already running");
                return;
            }
            mRunning = true;
            new Thread(this, getClass().getName()).start();
            while (!mReady) {
                try {
                    Log.d(TAG, "while---------");
                    mReadyFence.wait();
                } catch (InterruptedException ie) {
                    // ignore
                }
            }
        }
        Log.d(TAG, "run ---start----------");
        if (renderHandler == null) {
        } else {
        }
        handerCreateRecordSurface();
    }

    public class RecordRenderHandler extends Handler {
        private WeakReference<RecordRender> mWeakEncoder;

        public RecordRenderHandler(RecordRender encoder) {
            mWeakEncoder = new WeakReference<RecordRender>(encoder);
        }

        @Override
        public void handleMessage(Message msg) {
            RecordRender recordRender = mWeakEncoder.get();
            switch (msg.what) {
                case HANDLER_CREATE_SURFACE:
                    recordRender.createRecordSurface();
                    break;
                case HANDLER_DRAW:
                    try {
                        recordRender.drawFrame((int) msg.obj);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case HANDLER_MSG_QUIT:
                    Looper.myLooper().quit();
                    break;
            }
            super.handleMessage(msg);
        }
    }
}
