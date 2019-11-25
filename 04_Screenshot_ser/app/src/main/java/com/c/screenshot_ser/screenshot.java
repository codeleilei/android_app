package com.c.screenshot_ser;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.os.FileObserver;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

public class screenshot extends Service {
    private String  TAG =" test";
    private static String  pre_data="test";//增加pre_data 用来记录前一次的信息   防止误触导致弹屏
    private boolean flag=true;     //pre_data和flag用于屏蔽第一次 莫名的触发
    private static final String[] KEYWORDS = {
            "hdshot", "screen_shot", "screen-shot", "screen shot",
            "screencapture", "screen_capture", "screen-capture", "screen capture",
            "screencap", "screen_cap", "screen-cap", "screen cap",".png"
    };

    /** 读取媒体数据库时需要读取的列 */
    private static final String[] MEDIA_PROJECTIONS =  {
            MediaStore.Images.ImageColumns.DATA,
            MediaStore.Images.ImageColumns.DATE_TAKEN,
    };

    /** 内部存储器内容观察者 */
    private ContentObserver mInternalObserver;

    /** 外部存储器内容观察者 */
    private ContentObserver mExternalObserver;
    /**数据保存*/
    SharedPreferences config;

    private HandlerThread mHandlerThread;
    private Handler mHandler;
    @Override
    public void onCreate() {
        super.onCreate();
        mHandlerThread = new HandlerThread("Screenshot_Observer");
        mHandlerThread.start();
        mHandler = new Handler(mHandlerThread.getLooper());

        // 初始化
        mInternalObserver = new MediaContentObserver(MediaStore.Images.Media.INTERNAL_CONTENT_URI, mHandler);
       mExternalObserver = new MediaContentObserver(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, mHandler);

        /**数据保存*/
        config = getSharedPreferences("config", MODE_PRIVATE);
        pre_data=config.getString("data","");

        // 添加监听
        this.getContentResolver().registerContentObserver(
                MediaStore.Images.Media.INTERNAL_CONTENT_URI,
                false,
                mInternalObserver
        );

        this.getContentResolver().registerContentObserver(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                false,
                mExternalObserver
        );
/*
       if(pre_data.equals("test")) {
            Log.e(TAG, " read  pre_data     "+pre_data);
            pre_data = Read_data();    //开机的时候读取数据
        }
*/
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        return super.onStartCommand(intent, flags, startId);
    }



    @Override
    public void onDestroy() {
        super.onDestroy();
        // 注销监听
        this.getContentResolver().unregisterContentObserver(mInternalObserver);
        this.getContentResolver().unregisterContentObserver(mExternalObserver);
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }
    private void handleMediaContentChange(Uri contentUri) {
        Cursor cursor = null;
        try {
            // 数据改变时查询数据库中最后加入的一条数据
            cursor = this.getContentResolver().query(
                    contentUri,
                    MEDIA_PROJECTIONS,
                    null,
                    null,
                    MediaStore.Images.ImageColumns.DATE_ADDED + " desc limit 1"
            );

            if (cursor == null) {
                return;
            }
            if (!cursor.moveToFirst()) {
                return;
            }

            // 获取各列的索引
            int dataIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            int dateTakenIndex = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATE_TAKEN);

            // 获取行数据
            String data = cursor.getString(dataIndex);
            long dateTaken = cursor.getLong(dateTakenIndex);

            // 处理获取到的第一行数据
                handleMediaRowData(data, dateTaken);
        } catch (Exception e) {
            e.printStackTrace();

        } finally {
            if (cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
    }

    /**
     * 处理监听到的资源
     */
    private void handleMediaRowData(String data, long dateTaken) {

        Log.e(TAG, data + " " + dateTaken);
        if(pre_data.equals(data)|| (pre_data.equals("test")&& flag)){
            if(flag) flag=false;
            return;
        }
        if (checkScreenShot(data, dateTaken)) {
            Log.e(TAG, data + " " + dateTaken);
            Toast.makeText(getApplicationContext(), "截图成功图片路径 " + data, 0).show();
            saveData(data);
            //Write_data(data);   //实时更新数据 防止关机以后丢失pre_data的值
            pre_data=data;
        } else {
            Log.e(TAG, "Not screenshot event");
        }
    }

    /**
     * 判断是否是截屏
     */
    private boolean checkScreenShot(String data, long dateTaken) {

        data = data.toLowerCase();
        // 判断图片路径是否含有指定的关键字之一, 如果有, 则认为当前截屏了
        //截图格式的关键字 hdshot  格式关键字.png
        if (data.contains("hdshot")&&data.contains(".png") ) {
                return true;
        }
        return false;
    }

    /**
     * 媒体内容观察者(观察媒体数据库的改变)
     */
    private class MediaContentObserver extends ContentObserver {

        private Uri mContentUri;

        public MediaContentObserver(Uri contentUri, Handler handler) {
            super(handler);
            mContentUri = contentUri;
        }

        @Override
        public void onChange(boolean selfChange) {
            super.onChange(selfChange);
            Log.d(TAG, mContentUri.toString());
            handleMediaContentChange(mContentUri);
        }
    }

    /*
    /**
    **用于写入pre_data的值防止开关机以后数据丢失
     */
         public void Write_data(String data){
             try {

                 File file =new File("/sdcard/11.txt");
                 FileOutputStream fos = new FileOutputStream(file);//openFileOutput("/data/temp.txt", Context.MODE_PRIVATE);

                 OutputStreamWriter osw =new OutputStreamWriter(fos,"UTF-8");

                 osw.write("1111111");
                 osw.flush();
                 fos.flush();

                 osw.close();
                 fos.close();

                 Log.e(TAG, "Successful");
             } catch (FileNotFoundException e) {
                 e.printStackTrace();
             } catch (IOException e) {
                 e.printStackTrace();
             }
         }

    /**
     **用于读入pre_data的值防止开关机以后数据丢失
     */
    public String Read_data(){
        try {
            File file =new File("/sdcard/11.txt");
            if(!file.exists())
                file.createNewFile();
            FileInputStream fis = new FileInputStream(file);
            InputStreamReader isr =new InputStreamReader(fis,"UTF-8");
            char input[] =new char[fis.available()];

            isr.read(input);
            isr.close();
            fis.close();

            String text =new String(input);
            Log.d(TAG, "Read_data"+text);
            return text;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }


    public void saveData(String data) {
        // 2获取到了编辑器
        SharedPreferences.Editor edit = config.edit();
        // 3 保存数据  key -value
        edit.putString("data",data);
        //4 保存到文件中
        //edit.commit(); // 效率慢
        edit.apply();  // 效率快
    }


}
