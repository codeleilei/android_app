package com.watch.a26_mutlcamera;

import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CaptureRequest;
import android.media.ImageReader;
import android.util.Size;
import android.view.Surface;
import android.view.TextureView;

public class Camerainfo {
    private String CameraID;
    private CameraDevice mCameraDevice;
    private Surface mPreviewSurface;
    private Size mPreviewSize;
    private ImageReader mImageReader;
    private CameraCaptureSession mCaptureSession;
    private CaptureRequest.Builder mPreviewRequestBuilder;
    private CaptureRequest mPreviewRequest;

    private TextureView mTextureView;


    public String getCameraID(){
        return CameraID;
    }

    public void setCameraID(String ID){
        CameraID=ID;
    }

    public CameraDevice getCameraDevice(){
        return mCameraDevice;
    }

    public void setCameraDevice(CameraDevice mCameraDevice){
        this.mCameraDevice=mCameraDevice;
    }
    public Surface getPreviewSurface(){
        return mPreviewSurface;
    }

    public void setPreviewSurface(Surface mPreviewSurface){
        this.mPreviewSurface=mPreviewSurface;
    }


    public Size getmPreviewSize(){
        return mPreviewSize;
    }
    public void setmPreviewSize(Size mPreviewSize){
        this.mPreviewSize=mPreviewSize;
    }

    public TextureView getTextureView(){
        return mTextureView;
    }

    public void setTextureView(TextureView mTextureView){
        this.mTextureView=mTextureView;
    }

    public ImageReader getImageReader(){
        return mImageReader;
    }

    public void setImageReader(ImageReader mImageReader){
        this.mImageReader=mImageReader;
    }

    public CaptureRequest.Builder getRequestBuilder(){
        return mPreviewRequestBuilder;
    }

    public void setRequestBuilder(CaptureRequest.Builder mPreviewRequestBuilder){
        this.mPreviewRequestBuilder=mPreviewRequestBuilder;
    }

    public CameraCaptureSession getCaptureSession(){
        return mCaptureSession;
    }

    public void setCaptureSession(CameraCaptureSession mCaptureSession){
        this.mCaptureSession=mCaptureSession;
    }

    public CaptureRequest getPreviewRequest(){
        return mPreviewRequest;
    }

    public void setPreviewRequest(CaptureRequest mPreviewRequest){
        this.mPreviewRequest=mPreviewRequest;
    }

}
