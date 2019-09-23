package com.c.light;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import static android.webkit.ConsoleMessage.MessageLevel.LOG;

public class MainActivity extends AppCompatActivity {

    private SensorManager mSensorManager;
    private float mLux;
    private TextView mTextView;
    private Sensor mSensor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mTextView = (TextView) findViewById(R.id.textView);
        // 获取服务
        mSensorManager =  (SensorManager)getSystemService(Context.SENSOR_SERVICE);
        // 传感器种类-光照传感器
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);//TYPE_LIGHT
        mSensorManager.registerListener(listener, mSensor,
        SensorManager.SENSOR_DELAY_NORMAL);

    }
    protected void onDestroy() {
            if (mSensorManager != null) {
                mSensorManager.unregisterListener(listener);
            }
                super.onDestroy();
    }

    private SensorEventListener listener =
            new SensorEventListener() {

            public void onSensorChanged(SensorEvent event) {
                if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                // 获取光线强度
                    mLux = event.values[0];
                    mTextView.setText("当前光照强度为：" + mLux);

                }
            }

            public void onAccuracyChanged(Sensor sensor, int accuracy) {

            }
            };


        public void changeWindowBrightness(int brightness) {
            Window window = this.getWindow();
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            if (brightness == -1) {
                layoutParams.screenBrightness =
                    WindowManager.LayoutParams.BRIGHTNESS_OVERRIDE_NONE;
            } else {
                    layoutParams.screenBrightness =
                    (brightness <= 0 ? 1 : brightness) / 255f;
            }
                window.setAttributes(layoutParams);
             }

    public void onSensorChanged(SensorEvent event) {
            if (event.sensor.getType() == Sensor.TYPE_LIGHT) {
                // 获取光线强度
                mLux = event.values[0];
                mTextView.setText("当前光照强度为：" + mLux);
                changeWindowBrightness((int) mLux);
            }
    }

}
