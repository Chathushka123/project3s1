package com.example.project3s1;

import android.content.Context;
import android.graphics.Canvas;
import android.hardware.Camera;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static com.example.project3s1.util.Debug.tag;

public class CameraPreview
        extends
            SurfaceView
        implements
            SurfaceHolder.Callback,
            Camera.PreviewCallback
{
    private final SurfaceHolder mHolder;
    private Camera mCamera;

    public CameraPreview(Context context, Camera camera)
    {
        super(context);
        mCamera = camera;
        mHolder = getHolder();
        mHolder.addCallback(this);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder)
    {
        synchronized (this) {
            this.setWillNotDraw(false);
            mCamera.startPreview();
            mCamera.setPreviewCallback(this);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height)
    {
        /*
        if (mHolder.getSurface() == null) {
            return;
        }

        try {
            mCamera.setPreviewDisplay(mHolder);
            mCamera.setPreviewCallback(this);
            mCamera.startPreview();
        } catch (Exception e) {
            Log.d(tag(this), "Error starting camera preview: " + e.getMessage());
        }
        */
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder)
    {
    }

    @Override
    public void onPreviewFrame(byte[] data, Camera camera)
    {
        Canvas c;

        synchronized (mHolder) {
            c = mHolder.lockCanvas();
            if (c == null) {
                Log.d(tag(this), "canvas is null");
            }
        }
    }
}
