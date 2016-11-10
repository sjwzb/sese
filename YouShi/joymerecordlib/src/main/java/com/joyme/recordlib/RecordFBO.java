package com.joyme.recordlib;

import android.annotation.TargetApi;
import android.opengl.GLES20;
import android.opengl.GLES30;
import android.opengl.GLES30;
import android.os.Build;
import android.util.Log;

import com.joyme.recordlib.media.MediaConfig;

import java.nio.ByteBuffer;

/**
 * Created by jianweishao on 2016/6/25.
 */
public class RecordFBO {

    public RecordFBO(int width, int height) {
        this.width = width;
        this.height = height;
        createFbo();
    }

    private int width;
    private int height;
    private final int[] mFboId = new int[]{0};
    private final int[] mRboId = new int[]{0};
    private final int[] mTexId = new int[]{0};

    private void createFbo() {

        GlUtil.checkGlError("initFBO_S");

        GLES30.glGenFramebuffers(1, mFboId, 0);
        GLES30.glGenRenderbuffers(1, mRboId, 0);
        GLES30.glGenTextures(1, mTexId, 0);

        GLES30.glBindRenderbuffer(GLES30.GL_RENDERBUFFER, mRboId[0]);
        GLES30.glRenderbufferStorage(GLES30.GL_RENDERBUFFER,
                GLES30.GL_DEPTH_COMPONENT16, width, height);

        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, mFboId[0]);
        GLES30.glFramebufferRenderbuffer(GLES30.GL_FRAMEBUFFER,
                GLES30.GL_DEPTH_ATTACHMENT, GLES30.GL_RENDERBUFFER, mRboId[0]);

        GLES30.glActiveTexture(GLES30.GL_TEXTURE0);
        GLES30.glBindTexture(GLES30.GL_TEXTURE_2D, mTexId[0]);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,
                GLES30.GL_TEXTURE_MIN_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,
                GLES30.GL_TEXTURE_MAG_FILTER, GLES30.GL_LINEAR);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,
                GLES30.GL_TEXTURE_WRAP_S, GLES30.GL_CLAMP_TO_EDGE);
        GLES30.glTexParameteri(GLES30.GL_TEXTURE_2D,
                GLES30.GL_TEXTURE_WRAP_T, GLES30.GL_CLAMP_TO_EDGE);


//		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR);
//		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR);
//		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_CLAMP_TO_EDGE);
//		glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_CLAMP_TO_EDGE);
//        GLES30.glDisable(GLES30.GL_DEPTH_TEST);
//        GLES30.glDisable(GLES30.GL_STENCIL_TEST);
        GLES30.glTexImage2D(GLES30.GL_TEXTURE_2D, 0, GLES30.GL_RGBA,
                width, height, 0, GLES30.GL_RGBA, GLES30.GL_UNSIGNED_BYTE, null);
        GLES30.glFramebufferTexture2D(GLES30.GL_FRAMEBUFFER,
                GLES30.GL_COLOR_ATTACHMENT0, GLES30.GL_TEXTURE_2D, mTexId[0], 0);

        if (GLES30.glCheckFramebufferStatus(GLES30.GL_FRAMEBUFFER) !=
                GLES30.GL_FRAMEBUFFER_COMPLETE) {
            throw new RuntimeException("glCheckFramebufferStatus()");
        }
        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER,0);
        GlUtil.checkGlError("initFBO_E");
    }


    public void blitToFbo() {


//        GLES30.glGetIntegerv(GLES30.GL_FRAMEBUFFER_BINDING, oldFBO, 0);
//
//        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, oldFBO[0]);
//        GLES30.glBindFramebuffer(GLES30.GL_READ_FRAMEBUFFER, 0);
//        GLES30.glBindFramebuffer(GLES30.GL_DRAW_FRAMEBUFFER, mFboId[0]);
//        GLES30.glViewport(0,0,width,height);
//        GLES30.glBlitFramebuffer(0, 0, width, height, 0, 0,
//                width, height, GLES30.GL_COLOR_BUFFER_BIT, GLES30.GL_NEAREST);
//        // copyPix();
//
//        GLES30.glBindFramebuffer(GLES30.GL_FRAMEBUFFER, oldFBO[0]);
//
//        GlUtil.checkGlError("blitFbo");
        try {
            GLES30.glBindFramebuffer(GLES20.GL_FRAMEBUFFER, 0);
            GLES20.glBindTexture(GLES20.GL_TEXTURE_2D,mTexId[0]);
           // GLES30.glReadBuffer(GLES30.GL_BACK);
            GLES20.glCopyTexImage2D(GLES20.GL_TEXTURE_2D,0,GLES20.GL_RGB,0,0, width, height,0);
            GlUtil.checkGlError("copy");
        }catch (Exception e){
            Log.e("RecordFBO", "-----blitToFbo----->" + e.toString());
        }

    }




    public int getTexId() {
        return mTexId[0];
    }

    public void release() {
        if (mFboId[0] != 0) {
            GLES30.glDeleteFramebuffers(1, mFboId, 0);
            GLES30.glDeleteTextures(1, mTexId, 0);
            GLES30.glDeleteRenderbuffers(1, mRboId, 0);
        }
    }
}
