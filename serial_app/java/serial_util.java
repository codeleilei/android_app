package com.example.serialtool;

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

    public interface OnDataReceiveListener {
        public void onDataReceive(byte[] buffer, int size);
    }

    private void getSerialPort() {
        if (mReadThread == null) {

            mReadThread = new ReadThread();
        }
        mReadThread.start();
    }
    /**
     * 初始化串口信息
     */
    public void OpenSerialport(String path,int baudrate) {
        try {
            mSerialPort = new serialport_JNI(new File(path), baudrate);
            mOutputStream = mSerialPort.getOutputStream();
            mInputStream = mSerialPort.getInputStream();
            ;
        } catch (Exception e) {
            e.printStackTrace();
        }
        getSerialPort();  //open receive
    }

    /**
     * 发送指令到串口
     *
     * @param date
     * @return
     */
    public boolean SendDate(String date) {
        boolean result = true;
        byte[] sendData  = (date+"\r\n").getBytes();
        try {
            if (mOutputStream != null) {
                mOutputStream.write(sendData);
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
            while (!isStop && !isInterrupted()) {
                int size;
                try {
                    if (mInputStream == null)
                        return;
                    byte[] buffer = new byte[1024];
                    size = mInputStream.read(buffer);
                    if (size > 0) {
                        /*need add some message to show data on ui*/
                        /* wait to add*/
                        /*add_end*/
                        if (null != onDataReceiveListener) {
                            onDataReceiveListener.onDataReceive(buffer, size);
                        }
                    }
                    Thread.sleep(10);  //receive data reflesh time
                } catch (Exception e) {
                    e.printStackTrace();
                    return;
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
    }

}
