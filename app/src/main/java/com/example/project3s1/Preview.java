package com.example.project3s1;

import android.content.Context;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.project3s1.util.CameraUtil;

import java.io.IOException;

import static android.hardware.Camera.PreviewCallback;
import static com.example.project3s1.util.DebugUtil.tag;

public class Preview extends SurfaceView implements SurfaceHolder.Callback
{
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
        try {
            mCamera.setPreviewDisplay(holder);
            mCamera.setPreviewCallback(new PreviewCallback() {
                @Override
                public void onPreviewFrame(byte[] data, Camera camera)
                {
                    if ((mDrawOnTop == null) || mFinished)
                        return;

                    if (mDrawOnTop.mRgb == null) {
                        Camera.Size size = mCamera.getParameters().getPreviewSize();
                        mDrawOnTop.mWidth = size.width;
                        mDrawOnTop.mHeight = size.height;
                        mDrawOnTop.mDataLength = data.length;
                        mDrawOnTop.mYuv = new byte[data.length];
                        mDrawOnTop.mRgb = new int[size.width * size.height];
                    }
                    System.arraycopy(data, 0, mDrawOnTop.mYuv, 0, data.length);

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
    }

}
