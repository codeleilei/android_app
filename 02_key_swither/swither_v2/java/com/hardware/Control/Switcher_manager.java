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
            "UART_MODE_S1","UART_MODE_S2","UART_MODE_S3","UART_MODE_S4","UART_MODE_S5","BT_MODE_S6","VCC_MAIN_PWR","VCC_EX_12V_AD","LUM_STATUS"
    };
    Activity ac=null;
    public Switcher_manager(Activity activity){
        ac=activity;
        mHardwareControl_Jni = new HardwareControl_Jni();
    }


    public int get_Mode_value(int i){
        try {
            mHardwareControl_Jni.hwOperationByJNI(pinName[5],GetValue,false);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
    /*flag :ture set 1  false set 0*/
    public int set_Mode_value(int i,boolean flag){
        if(i<=7 && i >=0) {
            try {
                if (flag)
                    mHardwareControl_Jni.hwOperationByJNI(pinName[i], SetValue, true);
                else
                    mHardwareControl_Jni.hwOperationByJNI(pinName[i], SetValue, false);
            } catch (Exception e) {
                e.printStackTrace();
            }
            return 0;
        }else {
            Toast.makeText(ac.getApplicationContext(), "输入错误 请输入1-6", Toast.LENGTH_SHORT).show();
            return 1;
        }
    }

    public int getMain_Pwr() {
        return mHardwareControl_Jni.hwOperationByJNI(pinName[6],GetValue, false);
    }

    public int getEx12v_AD() {
        return mHardwareControl_Jni.hwOperationByJNI(pinName[7],GetValue, false);
    }

    public int getLum() {
        return mHardwareControl_Jni.hwOperationByJNI(pinName[8],GetValue, false);
    }
}
