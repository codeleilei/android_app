package com.c.screenshot_ser;

import android.app.Notification;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.FileObserver;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

public class screenshot extends Service {
    private String  TAG =" test";
    private boolean flag_true=false;
    private static final String[] KEYWORDS = {
            "screenshot", "screen_shot", "screen-shot", "screen shot",
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
            if(flag_true)
                handleMediaRowData(data, dateTaken);
            flag_true=!flag_true;
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
        if (checkScreenShot(data, dateTaken)) {
            Log.e(TAG, data + " " + dateTaken);
            Toast.makeText(getApplicationContext(), "截图成功图片路径 " + data, 0).show();
        } else {
            Log.d(TAG, "Not screenshot event");
        }
    }

    /**
     * 判断是否是截屏
     */
    private boolean checkScreenShot(String data, long dateTaken) {

        data = data.toLowerCase();
        // 判断图片路径是否含有指定的关键字之一, 如果有, 则认为当前截屏了
        for (String keyWork : KEYWORDS) {
            if (data.contains(keyWork)) {
                return true;
            }
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
}
