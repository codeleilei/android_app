package com.hardware.Control;

import android.app.Activity;
import android.util.Log;
import android.widget.Toast;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class Switcher_manager {
    private HardwareControl_Jni mHardwareControl_Jni=null;
    final int GetValue=0;
    final int SetValue=1;
    final String pinName[] = {
            "UART_MODE_S1","UART_MODE_S2","UART_MODE_S3","UART_MODE_S4","UART_MODE_S5","BT_MODE_S6"
    };
    Activity ac=null;
    public Switcher_manager(Activity activity){
        ac=activity;
        mHardwareControl_Jni = new HardwareControl_Jni();
    }


    public int get_Mode_S1(int i){
        mHardwareControl_Jni.hwOperationByJNI(pinName[5],GetValue,false);
        return 1;
    }
    /*flag :ture set 1  false set 0*/
    public int set_Mode_value(int i,boolean flag){
        if(i<=6 && i >=0) {
            if (flag)
                mHardwareControl_Jni.hwOperationByJNI(pinName[i - 1], GetValue, true);
            else
                mHardwareControl_Jni.hwOperationByJNI(pinName[i - 1], GetValue, false);
            return 0;
        }else {
            Toast.makeText(ac.getApplicationContext(), "输入错误 请输入1-6", Toast.LENGTH_SHORT).show();
            return 1;
        }
    }


}
