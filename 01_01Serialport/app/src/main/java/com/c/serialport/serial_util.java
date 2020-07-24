package com.c.serialport;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewDebug;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * 串口操作类
 *
 * @author leichen6666
 *
 */
public class serial_util {
    private String TAG = serial_util.class.getSimpleName();
    private serialport_JNI mSerialPort;
    private OutputStream mOutputStream;
    private InputStream mInputStream;
    private ReadThread mReadThread;
    private OnDataReceiveListener onDataReceiveListener = null;
    private boolean isStop = false;
    public  Handler myHandler;
    public static String buffer_show="";

    public serial_util(Handler myHandler)
    {
        this.myHandler=myHandler;
    }
    public interface OnDataReceiveListener {
        public void onDataReceive(byte[] buffer, int size);
    }


    private void getSerialPort() {
        mReadThread = new ReadThread();
        mReadThread.start();
        isStop=false;
    }
    /**
     * 初始化串口信息
     */
    public void OpenSerialport(String path,int baudrate) {
        try {
            mSerialPort = new serialport_JNI(new File(path), baudrate);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSerialPort();  //open receive
    }

    /**
     * 发送指令到串口
     *
     * @param Data
     * @return
     */
    public boolean SendDate(byte[] Data) {
        boolean result = true;

        try {
            if (mOutputStream != null) {
                mOutputStream.write(Data);
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    public boolean sendBuffer(byte[] mBuffer) {
        boolean result = true;
        String tail = "\r\n";
        byte[] tailBuffer = tail.getBytes();
        byte[] mBufferTemp = new byte[mBuffer.length+tailBuffer.length];
        System.arraycopy(mBuffer, 0, mBufferTemp, 0, mBuffer.length);
        System.arraycopy(tailBuffer, 0, mBufferTemp, mBuffer.length, tailBuffer.length);
//注意：我得项目中需要在每次发送后面加\r\n，大家根据项目项目做修改，也可以去掉，直接发送mBuffer
        try {
            if (mOutputStream != null) {
                mOutputStream.write(mBufferTemp);
            } else {
                result = false;
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = false;
        }
        return result;
    }

    private class ReadThread extends Thread {
        @Override
        public void run() {
            super.run();
            while (true) {
                if (this.isInterrupted()) {
                    Log.e("cl", "停止了！");
                    return;
                }
                while (!isStop) {//&& !isInterrupted()
                    int size;
                    try {
                        if (mInputStream == null)
                            return;
                        byte[] buffer = new byte[1024];
                        size = mInputStream.read(buffer);
                        String s = String.valueOf(size);
                        if (size > 0) {
                        /*发送消息给主线程 告知它处理事件*/
                            {
                                buffer_show += new String(buffer, 0, size, "UTF-8");
                                Message msg = new Message();// 消息
                                msg.what = 0x01;// 消息类别
                                myHandler.sendMessage(msg);
                            }
                        }
                        //Thread.sleep(10);  //receive data reflesh time
                    } catch (Exception e) {
                        e.printStackTrace();
                        return;
                    }
                }
            }
        }
    }

    /**
     * 关闭串口
     */
    public void closeSerialPort() {
        isStop = true;

            if (mReadThread != null) {
                    mReadThread.interrupt();
            }
        if (mSerialPort != null) {
            mSerialPort.close();
        }

        try {
            if (mInputStream != null) {
                mInputStream.close();
            }
            if (mOutputStream != null) {
                mOutputStream.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
