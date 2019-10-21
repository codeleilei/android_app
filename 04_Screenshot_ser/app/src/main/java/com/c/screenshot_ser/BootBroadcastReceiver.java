package com.c.screenshot_ser;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;



public class BootBroadcastReceiver extends BroadcastReceiver {
    static final String ACTION = "android.intent.action.BOOT_COMPLETED";
    @Override
    public void onReceive(Context context, Intent intent){
        if(intent.getAction().equals(ACTION)){
            Intent myIntent = new Intent(context, MainActivity.class);
            myIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(myIntent);
        }
    }
}
