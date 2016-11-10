package com.joyme.recordlib;

/**
 * Created by jianweishao on 2016/6/27.
 */
public class RecordFboNative {

    static {
        try {
            System.loadLibrary("jmrecorder");
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    private native void NBONE();

    private native void NBTWO();

    private native int NBTHREE();

    private native void NBFOUR();
}
