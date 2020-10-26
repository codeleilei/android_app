package com.watch.a26_mutlcamera;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.graphics.ImageFormat;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.graphics.SurfaceTexture;
import android.hardware.camera2.CameraAccessException;
import android.hardware.camera2.CameraCaptureSession;
import android.hardware.camera2.CameraCharacteristics;
import android.hardware.camera2.CameraDevice;
import android.hardware.camera2.CameraManager;
import android.hardware.camera2.CaptureRequest;
import android.hardware.camera2.CaptureResult;
import android.hardware.camera2.TotalCaptureResult;
import android.hardware.camera2.params.MeteringRectangle;
import android.hardware.camera2.params.StreamConfigurationMap;
import android.media.Image;
import android.media.ImageReader;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.os.Environment;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.SurfaceView;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * to handle interaction events.
 * Use the {@link CameraFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CameraFragment extends Fragment {
    private final String TAG="cl---camera";
    private static final String TAG_PREVIEW = "预览";

    private TextureView mTextureView1,mTextureView2,mTextureView3;
    private Activity activity=null;

    private List<Camerainfo> Cameralist =new ArrayList<>( );;

    private CameraDevice mCameraDevice;
    private Surface mPreviewSurface;
    private Size mPreviewSize;
    private ImageReader mImageReader;
    private CameraCaptureSession mCaptureSession;
    private CaptureRequest.Builder mPreviewRequestBuilder;


    private CameraCaptureSession.CaptureCallback mPreviewCaptureCallback = new CameraCaptureSession.CaptureCallback() {
        @Override
        public void onCaptureCompleted(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull TotalCaptureResult result) {

        }

        @Override
        public void onCaptureProgressed(@NonNull CameraCaptureSession session, @NonNull CaptureRequest request, @NonNull CaptureResult partialResult) {

        }
    };

    public CameraFragment() {
        // Required empty public constructor
    }

    public static CameraFragment newInstance() {
        Log.e("111","333");
        return new CameraFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        Log.e(TAG,"1111");
        return inflater.inflate( R.layout.fragment_camera, container, false );
    }
    @Override
    public void onViewCreated(final View view, Bundle savedInstanceState) {
        mTextureView1 = (TextureView) view.findViewById(R.id.textureView);
        mTextureView2 = (TextureView) view.findViewById(R.id.textureView2);
        mTextureView3 = (TextureView) view.findViewById(R.id.textureView3);

        activity=getActivity();

        initData();
        Log.e(TAG,"123123");
    }

    @Override
    public void onResume() {
        super.onResume();
        mTextureView1.setSurfaceTextureListener(new  TxListener("0",mTextureView1));
        //mTextureView2.setSurfaceTextureListener(new  TxListener("1",mTextureView2));
        //mTextureView3.setSurfaceTextureListener(new  TxListener("2",mTextureView3));

    }
    @Override
    public void onDetach() {
        super.onDetach();
    }

    public void initData(){
        //获取摄像头的管理者CameraManager
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
            //遍历所有摄像头
            for (String cameraId: manager.getCameraIdList()) {

                Camerainfo mCamerainfo =new Camerainfo();
                mCamerainfo.setCameraID( cameraId );
                Cameralist.add(mCamerainfo);
            }
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }


    }

    /*TextureView listeners*/
    private class TxListener implements TextureView.SurfaceTextureListener {
        private int CameraId;
        private Camerainfo mCamerainfo;
        private TextureView mTextureView;

        public TxListener(String id,TextureView View){
            CameraId=Integer.parseInt(id);
            mTextureView=View;
            mCamerainfo=Cameralist.get(CameraId);
        }
        @Override
        public void onSurfaceTextureAvailable(SurfaceTexture surface, int width, int height) {

            //当SurefaceTexture可用的时候，设置相机参数并打开相机
            setupConfig(width, height,mTextureView,mCamerainfo); //获取cameraid  并设置预览大小

            openCamera(mCamerainfo);  //检查相机权限 然后调用opencamera
        }

        @Override
        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i1) {

        }

        @Override
        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            return false;
        }

        @Override
        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {

        }

    }

    private void configureTransform(int viewWidth, int viewHeight,Size mViewsize,TextureView textureView) {
        if (null == textureView || null == mViewsize) {
            return;
        }
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        Matrix matrix = new Matrix();
        RectF viewRect = new RectF(0, 0, viewWidth, viewHeight);
        RectF bufferRect = new RectF(0, 0, mViewsize.getHeight(), mViewsize.getWidth());
        float centerX = viewRect.centerX();
        float centerY = viewRect.centerY();
        if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
            bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY());
            matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL);
            float scale = Math.max(
                    (float) viewHeight / mViewsize.getHeight(),
                    (float) viewWidth / mViewsize.getWidth());
            matrix.postScale(scale, scale, centerX, centerY);
            matrix.postRotate(90 * (rotation - 2), centerX, centerY);
        } else if (Surface.ROTATION_180 == rotation) {
            matrix.postRotate(180, centerX, centerY);
        }
        textureView.setTransform(matrix);
    }
    /*2、设置相机参数*/
    private void setupConfig(int width, int height,TextureView View,Camerainfo mCamerainfo) {

        //获取摄像头的管理者CameraManager
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        try {
                CameraCharacteristics characteristics = manager.getCameraCharacteristics(mCamerainfo.getCameraID());
                //获取StreamConfigurationMap，它是管理摄像头支持的所有输出格式和尺寸
                StreamConfigurationMap map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP);

                //根据TextureView的尺寸设置预览尺寸
                Size mPreviewSize = getOptimalSize(map.getOutputSizes(SurfaceTexture.class), width, height);

                configureTransform(width,height,mPreviewSize,View); //

                mCamerainfo.setmPreviewSize( mPreviewSize );
                mCamerainfo.setTextureView( View );

        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
    // 选择sizeMap中大于并且最接近width和height的size
    private Size getOptimalSize(Size[] sizeMap, int width, int height) {
        List<Size> sizeList = new ArrayList<>();
        for (Size option : sizeMap) {
            if (width > height) {
                if (option.getWidth() > width && option.getHeight() > height) {
                    sizeList.add(option);
                }
            } else {
                if (option.getWidth() > height && option.getHeight() > width) {
                    sizeList.add(option);
                }
            }
        }
        if (sizeList.size() > 0) {
            return Collections.min(sizeList, new Comparator<Size>() {
                @Override
                public int compare(Size lhs, Size rhs) {
                    return Long.signum(lhs.getWidth() * lhs.getHeight() - rhs.getWidth() * rhs.getHeight());
                }
            });
        }
        return sizeMap[0];
    }
    /*3、打开摄像头*/
    private void openCamera(Camerainfo mCamerainfo) {
        //获取相机的管理者CameraManager
        CameraManager manager = (CameraManager) activity.getSystemService(Context.CAMERA_SERVICE);
        //检查权限
        try {
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            //打开相机，第一个参数指示打开哪个摄像头，第二个参数stateCallback为相机的状态回调接口，第三个参数用来确定Callback在哪个线程执行，为null的话就在当前线程执行
            manager.openCamera(mCamerainfo.getCameraID(), stateCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }
/*3-1 opencamera的回调函数*/
    private final CameraDevice.StateCallback stateCallback = new CameraDevice.StateCallback() {
        @Override
        public void onOpened(CameraDevice camera) {
            String cameraID=camera.getId();
            Camerainfo mCamerainfo = Cameralist.get(Integer.parseInt(cameraID));
            mCamerainfo.setCameraDevice(camera);
            //开启预览
            startPreview(mCamerainfo);
        }

        @Override
        public void onDisconnected(CameraDevice cameraDevice) {

        }

        @Override
        public void onError(CameraDevice cameraDevice, int i) {

        }
    };

/*4、创建预览*/
    private void startPreview(Camerainfo mCamerainfo) {
        TextureView textureView= mCamerainfo.getTextureView();
        Size mPreviewSize=mCamerainfo.getmPreviewSize();
        CameraDevice mCameraDevice=mCamerainfo.getCameraDevice();
        Surface mPreviewSurface;

        setupImageReader(mCamerainfo);

        SurfaceTexture mSurfaceTexture = textureView.getSurfaceTexture();
        //设置TextureView的缓冲区大小
        mSurfaceTexture.setDefaultBufferSize(mPreviewSize.getWidth(), mPreviewSize.getHeight());
        //获取Surface显示预览数据
        mPreviewSurface= new Surface(mSurfaceTexture);
        mCamerainfo.setPreviewSurface(mPreviewSurface);
        try {
            getPreviewRequestBuilder(mCamerainfo);
            //创建相机捕获会话，
            // 第一个参数是捕获数据的输出Surface列表，
            // 第二个参数是CameraCaptureSession的状态回调接口，当它创建好后会回调onConfigured方法，
            // 第三个参数用来确定Callback在哪个线程执行，为null的话就在当前线程执行
            mCameraDevice.createCaptureSession( Arrays.asList(mPreviewSurface, mCamerainfo.getImageReader().getSurface()), new CameraCaptureSession.StateCallback() {
                @Override
                public void onConfigured(CameraCaptureSession session) {
                    String cameid = session.getDevice().getId();
                    Camerainfo mCamerainfo = Cameralist.get(Integer.parseInt(cameid));
                    mCamerainfo.setCaptureSession( session);
                    repeatPreview(mCamerainfo);
                }

                @Override
                public void onConfigureFailed(CameraCaptureSession session) {

                }
            }, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
    }

    private void setupImageReader(Camerainfo mCamerainfo) {
        ImageReader mImageReader;
        //前三个参数分别是需要的尺寸和格式，最后一个参数代表每次最多获取几帧数据
        mImageReader = ImageReader.newInstance(mCamerainfo.getmPreviewSize().getWidth(), mCamerainfo.getmPreviewSize().getHeight(), ImageFormat.JPEG, 1);
        //监听ImageReader的事件，当有图像流数据可用时会回调onImageAvailable方法，它的参数就是预览帧数据，可以对这帧数据进行处理
        mImageReader.setOnImageAvailableListener(new ImageReader.OnImageAvailableListener() {
            @Override
            public void onImageAvailable(ImageReader reader) {
                Log.i(TAG, "Image Available!");
                Image image = reader.acquireLatestImage();
                // 开启线程异步保存图片
                new Thread(new ImageSaver(image)).start();
            }
        }, null);
        mCamerainfo.setImageReader(mImageReader);
    }

    private void repeatPreview(Camerainfo mCamerainfo) {
        CaptureRequest mPreviewRequest;
        CaptureRequest.Builder mPreviewRequestBuilder=mCamerainfo.getRequestBuilder();
        mPreviewRequestBuilder.setTag(TAG_PREVIEW);
        mPreviewRequest = mPreviewRequestBuilder.build();
        //设置反复捕获数据的请求，这样预览界面就会一直有数据显示
        try {
            mCamerainfo.getCaptureSession().setRepeatingRequest(mPreviewRequest, mPreviewCaptureCallback, null);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        mCamerainfo.setPreviewRequest(  mPreviewRequest);
    }


    // 创建预览请求的Builder（TEMPLATE_PREVIEW表示预览请求）
    private void getPreviewRequestBuilder(Camerainfo mCamerainfo) {
        CaptureRequest.Builder mPreviewRequestBuilder = null;
        try {
            mPreviewRequestBuilder = mCamerainfo.getCameraDevice().createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW);
        } catch (CameraAccessException e) {
            e.printStackTrace();
        }
        //设置预览的显示界面
        mPreviewRequestBuilder.addTarget(mCamerainfo.getPreviewSurface());
        MeteringRectangle[] meteringRectangles = mPreviewRequestBuilder.get(CaptureRequest.CONTROL_AF_REGIONS);
        if (meteringRectangles != null && meteringRectangles.length > 0) {
            Log.d(TAG, "PreviewRequestBuilder: AF_REGIONS=" + meteringRectangles[0].getRect().toString());
        }
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_MODE, CaptureRequest.CONTROL_MODE_AUTO);
        mPreviewRequestBuilder.set(CaptureRequest.CONTROL_AF_TRIGGER, CaptureRequest.CONTROL_AF_TRIGGER_IDLE);

        mCamerainfo.setRequestBuilder( mPreviewRequestBuilder );
    }

    public static class ImageSaver implements Runnable {
        private Image mImage;

        public ImageSaver(Image image) {
            mImage = image;
        }

        @Override
        public void run() {
            ByteBuffer buffer = mImage.getPlanes()[0].getBuffer();
            byte[] data = new byte[buffer.remaining()];
            buffer.get(data);
            File imageFile = new File( Environment.getExternalStorageDirectory() + "/DCIM/myPicture.jpg");
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(imageFile);
                fos.write(data, 0, data.length);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (fos != null) {
                    try {
                        fos.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

}
