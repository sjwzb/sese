/*
 * Copyright (C) 2013 MorihiroSoft
 * Copyright 2013 Google Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.joyme.recordlib;

import android.graphics.ImageFormat;
import android.graphics.SurfaceTexture;
import android.hardware.Camera;
import android.opengl.EGL14;
import android.opengl.GLES20;
import android.util.Log;

import com.joyme.recordlib.media.MediaConfig;

public class CameraController implements  Camera.PreviewCallback{
	// ---------------------------------------------------------------------
	// MEMBERS
	// ---------------------------------------------------------------------
	private final static String TAG = "CameraController";
	private static final int    FPS     = 30;

	private Camera mCamera = null;
	private int previewWidth = MediaConfig.getInstance().cameraW;
	private int previewHeight = MediaConfig.getInstance().cameraH;
	// ---------------------------------------------------------------------
	// PUBLIC METHODS
	// ---------------------------------------------------------------------
	public CameraController(SurfaceTexture surfaceTexture) {
		initCamera(surfaceTexture);
	}

	public void startCamera() {
		mCamera.startPreview();
	}

	public void stopCamera() {
		quitCamera();
	}

	// ---------------------------------------------------------------------
	// PRIVATE...
	// ---------------------------------------------------------------------

	private void initCamera(SurfaceTexture surfaceTexture) {
		if (mCamera == null) {
			try {
				mCamera = Camera.open(Camera.CameraInfo.CAMERA_FACING_FRONT);
				parameters = mCamera.getParameters();
				// Frame rate
				int[] fps = null;
				for (int[] f : parameters.getSupportedPreviewFpsRange()) {
					 Log.d(TAG, "FpsRange = {"+
							f[Camera.Parameters.PREVIEW_FPS_MIN_INDEX]+","+
							f[Camera.Parameters.PREVIEW_FPS_MAX_INDEX]+"}");
					if (f[Camera.Parameters.PREVIEW_FPS_MAX_INDEX] >= FPS*1000) {
						fps = f;
					}
				}
				if (fps == null) {
					throw new UnsupportedOperationException(
							String.format("Not support fps: %d",FPS));
				}
				parameters.setPreviewSize(previewWidth,previewHeight);
				parameters.setPreviewFpsRange(
						fps[Camera.Parameters.PREVIEW_FPS_MIN_INDEX],
						fps[Camera.Parameters.PREVIEW_FPS_MAX_INDEX]);
				mCamera.setParameters(parameters);

//				previewWidth = mVideoParam.mSize.width;
//				previewHeight = mVideoParam.mSize.height;
				bufferSize = previewWidth * previewHeight;
				textureBuffer=new int[bufferSize];
				bufferSize  = 2*bufferSize * ImageFormat.getBitsPerPixel(parameters.getPreviewFormat()) / 8;
				gBuffer = new byte[bufferSize];
				mCamera.addCallbackBuffer(gBuffer);
				mCamera.setPreviewCallbackWithBuffer(this);
				mCamera.setPreviewTexture(surfaceTexture);
				Camera.Size size = null;
				for (Camera.Size s : parameters.getSupportedPreviewSizes()) {
					 Log.d(TAG, "Size = "+s.width+"x"+s.height);
//					if (s.width == VIDEO_W && s.height == VIDEO_H) {
//						size = s;
//					}
				}

			} catch (Exception e) {
                MediaConfig.getInstance().isOpenCamera=false;
				e.printStackTrace();

				throw new RuntimeException("setup camera");
			}
		}
	}

	private void quitCamera() {
		if (mCamera != null) {
			mCamera.stopPreview();
			mCamera.release();
			mCamera = null;
		}
	}

	public byte gBuffer[];
	public int textureBuffer[];
	private Camera.Parameters parameters;
	private int bufferSize;
	private boolean isUpdate;
	@Override
	public void onPreviewFrame(byte[] data, Camera camera) {
		if (isUpdate){
			isUpdate=!isUpdate;
			return;
		}
//		Log.e("---------->", "---------------onPreviewFrame ");
		camera.addCallbackBuffer(gBuffer);
		if(CameraCallbackManager.getInstance().getCurrentUpdateBitmapCallback() != null){
			CameraCallbackManager.getInstance().getCurrentUpdateBitmapCallback().updateBitmap(data, previewWidth, previewHeight);
		}
	}

	public int getCameraWidth(){
		return previewWidth;
	}
	public int getCameraHeight(){
		return previewHeight;
	}


}
