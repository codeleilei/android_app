package com.c.screenshot_ser;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends Activity {
    private  String TAG ="MainActivity";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // setContentView(R.layout.activity_main);
        final Intent intent = new Intent(this, screenshot.class);
        startService(intent);
    }
    protected void onResume() {
        super.onResume();
        this.finish();
    }

    public void Request(){
        // Here, thisActivity is the current activity
        if(ContextCompat.checkSelfPermission(this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED)
        {
            Log.e(TAG,"requestPermissions");
            // No explanation needed, we can request the permission.
            ActivityCompat.requestPermissions(this,
                    new String[]{
                            Manifest.permission.READ_EXTERNAL_STORAGE
                    },
                    1);
        }
    }
}
