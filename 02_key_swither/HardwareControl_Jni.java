package com.hardware.Control;

public class HardwareControl_Jni {
    private HardwareControl_Jni mHardwareControl_Jni=null;
    final int GetValue=0;
    final int SetValue=1;

    public native int hwOperationByJNI(String module, int opt, boolean setValue);
    static {
        System.loadLibrary("hardware_control");
    }
}
