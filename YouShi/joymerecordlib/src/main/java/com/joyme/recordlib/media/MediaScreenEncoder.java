package com.joyme.recordlib.media;
/*
 * ScreenRecordingSample
 * Sample project to cature and save audio from internal and video from screen as MPEG4 file.
 *
 * Copyright (c) 2015 saki t_saki@serenegiant.com
 *
 * File name: MediaScreenEncoder.java
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 * All files in the folder are under this Apache License, Version 2.0.
*/

import android.content.Context;
import android.opengl.EGL14;
import android.util.Log;
import android.view.Surface;

import com.joyme.recordlib.RecordRender;
import com.joyme.unitydemo.JMRecorder;

import java.io.IOException;

public class MediaScreenEncoder extends MediaVideoEncoderBase implements JMRecorder.RenderListener {
    private static final boolean DEBUG = true;    // TODO set false on release
    private static final String TAG = "MediaScreenEncoder";

    private static final String MIME_TYPE = "video/avc";
    // parameters for recording
    private static final int FRAME_RATE = 25;
    private Context context;
    private Surface mSurface;

    public MediaScreenEncoder(Context context, final MediaMuxerWrapper muxer, final MediaEncoderListener listener,
                              final int width, final int height) {

        super(muxer, listener, width, height);
        this.context = context;


    }


    @Override
    protected void release() {
        super.release();
    }

    @Override
    public void prepare() throws IOException {
        if (DEBUG) Log.i(TAG, "prepare: ");
        mSurface = prepare_surface_encoder(MIME_TYPE, FRAME_RATE);
        mIsCapturing = true;
        JMRecorder.getInstance().setRenderListener(this);
        if (DEBUG) Log.i(TAG, "prepare finishing");
        if (mListener != null) {
            try {
                mListener.onPrepared(this);
            } catch (final Exception e) {
                Log.e(TAG, "prepare:", e);
            }
        }
    }

    @Override
    void stopRecording() {
        Log.d("=========", "mediaS creenEncoder stop ...");
        synchronized (mSync) {

            if (recordRender != null) {
                recordRender.release();
                recordRender = null;
            }
            mIsCapturing = false;
            mSync.notifyAll();
        }
        super.stopRecording();
    }

    private boolean isFirst = true;
    private int cameraCounter = 0;
    private static RecordRender recordRender;

    @Override
    public void onRenderImage(int textId) {
        if (mIsCapturing) {
            synchronized (mSync) {
                if (recordRender == null) {
                    Log.d(TAG, "recordRender  is  null");
                    recordRender = new RecordRender(mSurface, EGL14.eglGetCurrentContext(), mWidth, mHeight);
                    recordRender.start();
                }
                if (recordRender != null) {

                    if (JMRecorder.getInstance().getListener() != null) {
                        JMRecorder.getInstance().getListener().onStartRecord();
                        JMRecorder.getInstance().setListener(null);
                    }
                    recordRender.handerDrawFrame(textId);
                    frameAvailableSoon();

                }
            }

        }

    }


    @Override
    public void onPostRender() {

    }


}
