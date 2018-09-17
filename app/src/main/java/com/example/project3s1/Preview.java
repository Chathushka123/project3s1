package com.example.project3s1;

import android.content.Context;
import android.hardware.Camera;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.project3s1.util.CameraUtil;

import java.io.IOException;

import static android.hardware.Camera.*;

public class Preview extends SurfaceView implements SurfaceHolder.Callback
{
    private SurfaceHolder mHolder;
    private Camera mCamera;
    private DrawOnTop mDrawOnTop;
    boolean mFinished;

    public Preview(Context context, DrawOnTop drawOnTop)
    {
        super(context);

        mDrawOnTop = drawOnTop;
        mFinished = false;
        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        mCamera = CameraUtil.getCameraInstance();
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.setPreviewCallback(new PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera)
                {
                    if ((mDrawOnTop == null) || mFinished)
                        return;

                    mDrawOnTop.invalidate();
                }
            });
        } catch (IOException e) {
            mCamera.release();
            mCamera = null;
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        CameraUtil.setOrientation(mCamera);
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
        mFinished = true;
        mCamera.setPreviewCallback(null);
        mCamera.stopPreview();
        mCamera.release();
        mCamera = null;
    }

}
