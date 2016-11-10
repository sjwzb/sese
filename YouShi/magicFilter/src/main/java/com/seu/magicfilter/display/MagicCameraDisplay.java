package com.seu.magicfilter.display;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.IntBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.SurfaceTexture;
import android.graphics.SurfaceTexture.OnFrameAvailableListener;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.hardware.Camera.Size;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;

import com.joyme.unitydemo.JMRecorder;
import com.seu.magicfilter.camera.CameraEngine;
import com.seu.magicfilter.filter.base.MagicCameraInputFilter;
import com.seu.magicfilter.filter.helper.MagicFilterParam;
import com.seu.magicfilter.filter.helper.MagicFilterType;
import com.seu.magicfilter.utils.OpenGLUtils;
import com.seu.magicfilter.utils.Rotation;
import com.seu.magicfilter.utils.SaveTask;
import com.seu.magicfilter.utils.TextureRotationUtil;
import com.seu.magicfilter.utils.SaveTask.onPictureSaveListener;

/**
 * MagicCameraDisplay is used for camera preview
 */
public class MagicCameraDisplay extends MagicDisplay {
    /**
     * 用于绘制相机预览数据，当无滤镜及mFilters为Null或者大小为0时，绘制到屏幕中，
     * 否则，绘制到FrameBuffer中纹理
     */
    private final MagicCameraInputFilter mCameraInputFilter;

    /**
     * Camera预览数据接收层，必须和OpenGL绑定
     * 过程见{@link OpenGLUtils.getExternalOESTextureID()};
     */
    private SurfaceTexture mSurfaceTexture;

    public MagicCameraDisplay(Context context, GLSurfaceView glSurfaceView) {
        super(context, glSurfaceView);
        mCameraInputFilter = new MagicCameraInputFilter();
    }

    @Override
    public void onSurfaceCreated(GL10 gl, EGLConfig config) {
        GLES20.glDisable(GL10.GL_DITHER);
        GLES20.glClearColor(0, 0, 0, 0);
        GLES20.glEnable(GL10.GL_CULL_FACE);
        GLES20.glEnable(GL10.GL_DEPTH_TEST);
        MagicFilterParam.initMagicFilterParam(gl);
        //setFilter(MagicFilterType.BEAUTY);
        mCameraInputFilter.init();

    }

    @Override
    public void onSurfaceChanged(GL10 gl, int width, int height) {
        GLES20.glViewport(0, 0, width, height);
        mSurfaceWidth = width;
        mSurfaceHeight = height;
        onFilterChanged();
    }

    @Override
    public void onDrawFrame(GL10 gl) {
        GLES20.glClearColor(0.0f, 0.0f, 0.0f, 0.0f);
        GLES20.glClear(GLES20.GL_COLOR_BUFFER_BIT | GLES20.GL_DEPTH_BUFFER_BIT);
        mSurfaceTexture.updateTexImage();
        float[] mtx = new float[16];
        mSurfaceTexture.getTransformMatrix(mtx);
        mCameraInputFilter.setTextureTransformMatrix(mtx);
        int textureID = 0;
        if (mFilters == null) {
            mCameraInputFilter.onDrawFrame(mTextureId, mGLCubeBuffer, mGLTextureBuffer);
        } else {
            textureID = mCameraInputFilter.onDrawToTexture(mTextureId);
            mFilters.onDrawFrame(textureID, mGLCubeBuffer, mGLTextureBuffer);
            Log.d("camera", "w" + mSurfaceWidth + " h " + mSurfaceHeight);

        }
        //saveBitmap(mSurfaceWidth, mSurfaceHeight);
        JMRecorder.getInstance().onDrawFrame(0, mSurfaceWidth, mSurfaceHeight);


    }

    private ByteBuffer pixelBuffer;

    private void saveBitmap(int width, int height) {

        if (pixelBuffer == null) {
            pixelBuffer = ByteBuffer.allocateDirect(width * height * 4).order(ByteOrder.nativeOrder());
            return;
        }
        pixelBuffer.position(0);
        IntBuffer ib = IntBuffer.allocate(width * height);
        GLES20.glReadPixels(0, 0, width, height, GLES20.GL_RGBA, GLES20.GL_UNSIGNED_BYTE, pixelBuffer);
        // GLES20.glTexImage2D();
        Bitmap mBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        mBitmap.copyPixelsFromBuffer(pixelBuffer);
        // BitmapPool.getInstance().addBitmap(pixelBuffer);
        mSaveTask = new SaveTask(mContext, getOutputMediaFile(), null);
        mSaveTask.execute(mBitmap);
    }

    public File getOutputMediaFile() {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), "MagicCamera");
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.


        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.CHINESE).format(new Date());
        File mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                "IMG_" + timeStamp + ".jpg");

        return mediaFile;
    }

    private OnFrameAvailableListener mOnFrameAvailableListener = new OnFrameAvailableListener() {

        @Override
        public void onFrameAvailable(SurfaceTexture surfaceTexture) {
            // TODO Auto-generated method stub
            mGLSurfaceView.requestRender();
        }
    };

    private void setUpCamera() {
        mGLSurfaceView.queueEvent(new Runnable() {

            @Override
            public void run() {
                if (mTextureId == OpenGLUtils.NO_TEXTURE) {
                    mTextureId = OpenGLUtils.getExternalOESTextureID();
                    mSurfaceTexture = new SurfaceTexture(mTextureId);
                    mSurfaceTexture.setOnFrameAvailableListener(mOnFrameAvailableListener);
                }
                Size size = CameraEngine.getPreviewSize();
                int orientation = CameraEngine.getOrientation();
                if (orientation == 90 || orientation == 270) {
                    mImageWidth = size.height;
                    mImageHeight = size.width;
                } else {
                    mImageWidth = size.width;
                    mImageHeight = size.height;
                }
                mCameraInputFilter.onOutputSizeChanged(mImageWidth, mImageHeight);
                CameraEngine.startPreview(mSurfaceTexture);
            }
        });
    }

    protected void onFilterChanged() {
        super.onFilterChanged();
        mCameraInputFilter.onDisplaySizeChanged(mSurfaceWidth, mSurfaceHeight);
        if (mFilters != null)
            mCameraInputFilter.initCameraFrameBuffer(mImageWidth, mImageHeight);
        else
            mCameraInputFilter.destroyFramebuffers();
    }

    public void onResume() {
        super.onResume();
        if (CameraEngine.getCamera() == null)
            CameraEngine.openCamera();
        if (CameraEngine.getCamera() != null) {
            boolean flipHorizontal = CameraEngine.isFlipHorizontal();
            adjustPosition(CameraEngine.getOrientation(), flipHorizontal, !flipHorizontal);
        }
        setUpCamera();
    }

    public void onPause() {
        super.onPause();
        CameraEngine.releaseCamera();
    }

    public void onDestroy() {
        super.onDestroy();
    }

    public void onTakePicture(File file, onPictureSaveListener listener, ShutterCallback shutterCallback) {
        CameraEngine.setRotation(90);
        mSaveTask = new SaveTask(mContext, file, listener);
        CameraEngine.takePicture(shutterCallback, null, mPictureCallback);
    }

    private PictureCallback mPictureCallback = new PictureCallback() {

        @Override
        public void onPictureTaken(final byte[] data, Camera camera) {
            Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
            if (mFilters != null) {
                getBitmapFromGL(bitmap, true);
            } else {
                mSaveTask.execute(bitmap);
            }
        }
    };

    protected void onGetBitmapFromGL(Bitmap bitmap) {
        mSaveTask.execute(bitmap);
    }

    private void adjustPosition(int orientation, boolean flipHorizontal, boolean flipVertical) {
        Rotation mRotation = Rotation.fromInt(orientation);
        float[] textureCords = TextureRotationUtil.getRotation(mRotation, flipHorizontal, flipVertical);
        mGLTextureBuffer.clear();
        mGLTextureBuffer.put(textureCords).position(0);
    }
}
