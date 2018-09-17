package com.example.project3s1;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.project3s1.util.CameraUtil;

import java.io.IOException;

import static android.hardware.Camera.PreviewCallback;

public class Preview extends SurfaceView implements SurfaceHolder.Callback
{
    static {
        System.loadLibrary("native-lib");
    }

    private native int decodeYUV420sp(byte[] yuv420sp);

    private SurfaceHolder mHolder;
    private Camera mCamera;
    private DrawOnTop mDrawOnTop;
    boolean mFinished;

    public Preview(Context context, DrawOnTop drawOnTop, Camera camera)
    {
        super(context);

        mCamera = camera;
        mDrawOnTop = drawOnTop;
        mFinished = false;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        synchronized (holder) {
            try {
                mCamera.setPreviewDisplay(holder);
                mCamera.setPreviewCallback(new PreviewCallback() {
                    @Override
                    public void onPreviewFrame(byte[] data, Camera camera)
                    {
                        if ((mDrawOnTop == null) || mFinished)
                            return;

                        mDrawOnTop.mSum = decodeYUV420sp(data);

                        mDrawOnTop.invalidate();
                    }
                });
            } catch (IOException e) {
                mCamera.release();
                mCamera = null;
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        synchronized (holder) {
            CameraUtil.setOrientation(mCamera);
            Camera.Parameters parameters = mCamera.getParameters();
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
            mCamera.setParameters(parameters);
            mCamera.startPreview();
        }
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        synchronized (holder) {
            mFinished = true;
        }
    }

}
