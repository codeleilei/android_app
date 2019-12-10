package com.c.downloader;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private Button bt;
    private EditText ed;
    private TextView tv;
    private Context mContext;
    private long mTaskId;
    DownloadManager downloadManager;
    private String TAG="CL_main";
    java.lang.Process por;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        View_init();
        Button_event();
        mContext=getApplicationContext();
        //注册广播接收者，监听下载状态
        mContext.registerReceiver(receiver,
                new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));
    }

    //广播接受者，接收下载状态
    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            checkDownloadStatus();//检查下载状态
        }
    };


    public void View_init(){
        bt=(Button)findViewById(R.id.button);
        ed=(EditText)findViewById(R.id.editText);
        tv=(TextView)findViewById(R.id.textView);
    }

    public void Button_event(){
        bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String Url=null;
                Url=ed.getText().toString();
                Do(Url,"update.zip");
                //tv.setText(down.DownTXT(Url));
            }
        });

    }
    public void Do(String downloadUrl,String fileName){
        /**使用的是系统内置的下载器*/
        try {
            //创建下载任务,downloadUrl就是下载链接
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(downloadUrl));
            //指定下载路径和下载文件名
            request.setDestinationInExternalPublicDir("/download/", fileName);
            //获取下载管理器
            downloadManager = (DownloadManager)mContext .getSystemService(Context.DOWNLOAD_SERVICE);
            //将下载任务加入下载队列，否则不会进行下载
            //加入下载队列后会给该任务返回一个long型的id，
            //通过该id可以取消任务，重启任务等等，看上面源码中框起来的方法
            mTaskId=downloadManager.enqueue(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //检查下载状态
    private void checkDownloadStatus() {
        DownloadManager.Query query = new DownloadManager.Query();
        query.setFilterById(mTaskId);//筛选下载任务，传入任务ID，可变参数
        Cursor c = downloadManager.query(query);
        if (c.moveToFirst()) {
            int status = c.getInt(c.getColumnIndex(DownloadManager.COLUMN_STATUS));
            switch (status) {
                case DownloadManager.STATUS_PAUSED:
                    Log.i(TAG,">>>下载暂停");
                case DownloadManager.STATUS_PENDING:
                    Log.i(TAG,">>>下载延迟");
                case DownloadManager.STATUS_RUNNING:
                    Log.i(TAG,">>>正在下载");
                    break;
                case DownloadManager.STATUS_SUCCESSFUL:
                    Log.i(TAG,">>>下载完成");
                    //下载完成安装APK
                    //downloadPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).getAbsolutePath() + File.separator + versionName;
                    Mvdate();
                    break;
                case DownloadManager.STATUS_FAILED:
                    Log.i(TAG,">>>下载失败");
                    break;
            }
        }
    }


    public void Mvdate(){
        try{
            String[] cmd0 = new String[]{"su","-c","mv /sdcard/Download/update.zip  /cache"};
            por = Runtime.getRuntime().exec(cmd0);
            por.waitFor();
            if (por.exitValue() != 0) {
                System.out.println("echo失败");
                Toast.makeText(getApplicationContext(), "升级失败cmd0", Toast.LENGTH_SHORT).show();
            }

            String[] cmd1 = new String[]{"su","-c","echo \"--update_package=/cache/update.zip\" > /cache/recovery/command"};
            por = Runtime.getRuntime().exec(cmd1);
            por.waitFor();
            if (por.exitValue() != 0) {
                System.out.println("echo失败");
                Toast.makeText(getApplicationContext(), "升级失败cmd1", Toast.LENGTH_SHORT).show();
             }
            String[] cmd2 = new String[]{"su","-c","reboot recovery"};
            por = Runtime.getRuntime().exec(cmd2);
            por.waitFor();
            if (por.exitValue() != 0) {
                System.out.println("重启失败");
                Toast.makeText(getApplicationContext(), "升级失败cmd2", Toast.LENGTH_SHORT).show();
            }

        } catch (IOException e) {
                e.printStackTrace();
        } catch (InterruptedException e) {
                e.printStackTrace();
        }
        Toast.makeText(getApplicationContext(), "升级设置成功 等待升级", Toast.LENGTH_SHORT).show();

    }

}

