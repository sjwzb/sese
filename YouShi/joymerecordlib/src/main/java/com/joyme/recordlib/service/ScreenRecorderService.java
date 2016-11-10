package com.joyme.recordlib.service;
/*
 * ScreenRecordingSample
 * Sample project to cature and save audio from internal and video from screen as MPEG4 file.
 *
 * Copyright (c) 2015 saki t_saki@serenegiant.com
 *
 * File name: ScreenRecorderService.java
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

import android.app.IntentService;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Surface;
import android.widget.Toast;


import com.joyme.recordlib.media.AudioManager;
import com.joyme.recordlib.media.MediaAudioEncoder;
import com.joyme.recordlib.media.MediaConfig;
import com.joyme.recordlib.media.MediaEncoder;
import com.joyme.recordlib.media.MediaMuxerWrapper;
import com.joyme.recordlib.media.MediaScreenEncoder;

import java.io.IOException;

public class ScreenRecorderService extends IntentService {
    private static final boolean DEBUG = true;
    private static final String TAG = "ScreenRecorderService";
    private static final String BASE = "com.serenegiant.service.ScreenRecorderService.";
    public static final String ACTION_START = BASE + "ACTION_START";
    public static final String ACTION_STOP = BASE + "ACTION_STOP";
    public static final String ACTION_PAUSE = BASE + "ACTION_PAUSE";
    public static final String ACTION_RESUME = BASE + "ACTION_RESUME";
    public static final String ACTION_QUERY_STATUS = BASE + "ACTION_QUERY_STATUS";
    public static final String ACTION_QUERY_STATUS_RESULT = BASE + "ACTION_QUERY_STATUS_RESULT";
    public static final String EXTRA_RESULT_CODE = BASE + "EXTRA_RESULT_CODE";
    public static final String EXTRA_QUERY_RESULT_RECORDING = BASE + "EXTRA_QUERY_RESULT_RECORDING";
    public static final String EXTRA_QUERY_RESULT_PAUSING = BASE + "EXTRA_QUERY_RESULT_PAUSING";
    public static final String FILE_PATH = "FILE_PATH";
    public static final String VIDEO_WIDTH = "VIDEO_WIDTH";
    public static final String VIDEO_HEIGHT = "VIDEO_HEIGHT";
    public static final String VIDEO_AUDIO_RECORD = "AUDIO_RECORD";

    private static Object sSync = new Object();
    private static MediaMuxerWrapper sMuxer;


    public ScreenRecorderService() {
        super(TAG);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if (DEBUG) Log.v(TAG, "onCreate:");
    }

    @Override
    protected void onHandleIntent(final Intent intent) {
        if (DEBUG) Log.v(TAG, "onHandleIntent:intent=" + intent);
        final String action = intent.getAction();
        if (ACTION_START.equals(action)) {
            startScreenRecord(intent);
            updateStatus();
        } else if (ACTION_STOP.equals(action)) {
            stopScreenRecord();
            updateStatus();
        } else if (ACTION_QUERY_STATUS.equals(action)) {
            updateStatus();
        } else if (ACTION_PAUSE.equals(action)) {
            pauseScreenRecord();
        } else if (ACTION_RESUME.equals(action)) {
            resumeScreenRecord();
        }
    }

    private void updateStatus() {
        final boolean isRecording, isPausing;
        synchronized (sSync) {
            isRecording = (sMuxer != null);
            isPausing = isRecording ? sMuxer.isPaused() : false;
        }
        final Intent result = new Intent();
        result.setAction(ACTION_QUERY_STATUS_RESULT);
        result.putExtra(EXTRA_QUERY_RESULT_RECORDING, isRecording);
        result.putExtra(EXTRA_QUERY_RESULT_PAUSING, isPausing);
        if (DEBUG)
            Log.v(TAG, "sendBroadcast:isRecording=" + isRecording + ",isPausing=" + isPausing);
        sendBroadcast(result);
    }

    private boolean isRecording() {
        boolean isRecording = (sMuxer != null);
        return isRecording;

    }

    private boolean isPause() {
        boolean isPausing = isRecording() ? sMuxer.isPaused() : false;
        return isPausing;
    }

    /**
     * start screen recording as .mp4 file
     *
     * @param intent
     */
    private void startScreenRecord(final Intent intent) {
        int w = intent.getIntExtra(VIDEO_WIDTH, 720);
        int h = intent.getIntExtra(VIDEO_HEIGHT, 480);
        w = MediaConfig.getInstance().mediaW;
        h = MediaConfig.getInstance().mediaH;
        String filePath = intent.getStringExtra(FILE_PATH);
        boolean isAudioRecord = intent.getBooleanExtra(VIDEO_AUDIO_RECORD, true);
        if (DEBUG) Log.v(TAG, "startScreenRecord:sMuxer=" + sMuxer);
        synchronized (sSync) {
            if (sMuxer == null) {

                if (DEBUG) Log.v(TAG, "startRecording:");
                try {
                    sMuxer = new MediaMuxerWrapper(filePath);    // if you record audio only, ".m4a" is also OK.
                    if (true) {
                        // for screen capturing
                        new MediaScreenEncoder(this,sMuxer, mMediaEncoderListener,
                                w, h);
                    }
                    if (MediaConfig.getInstance().isAudioRecord) {
                        AudioManager manager=new AudioManager();
                        boolean b=manager.initAudio();

                        if (b){
                            // for audio capturing
                            new MediaAudioEncoder(sMuxer, mMediaEncoderListener,manager.getAudioRecord());
                        }

                    }
                    sMuxer.prepare();
                    sMuxer.startRecording();
                } catch (final IOException e) {
                    Log.e(TAG, "startScreenRecord:", e);
                }
            } else {
                Toast.makeText(this, "已经开始录制了", Toast.LENGTH_SHORT);
            }
        }
    }

    /**
     * stop screen recording
     */
    private void stopScreenRecord() {
        if (DEBUG) Log.v(TAG, "stopScreenRecord:sMuxer=" + sMuxer);
        Log.d("===========", "stopScreenRecord  service ");
        synchronized (sSync) {
            if (sMuxer != null) {
                sMuxer.stopRecording();
                sMuxer = null;
                // you should not wait here
            }
        }
    }

    private void pauseScreenRecord() {
        synchronized (sSync) {
            if (sMuxer != null) {
                sMuxer.pauseRecording();
            }
        }
    }

    private void resumeScreenRecord() {
        synchronized (sSync) {
            if (sMuxer != null && isPause()) {
                sMuxer.resumeRecording();
            }
        }
    }

    /**
     * callback methods from encoder
     */
    private static final MediaEncoder.MediaEncoderListener mMediaEncoderListener = new MediaEncoder.MediaEncoderListener() {
        @Override
        public void onPrepared(final MediaEncoder encoder) {
            if (DEBUG) Log.v(TAG, "onPrepared:encoder=" + encoder);
        }

        @Override
        public void onStopped(final MediaEncoder encoder) {
            if (DEBUG) Log.v(TAG, "onStopped:encoder=" + encoder);
        }
    };

}
