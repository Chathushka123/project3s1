package com.example.project3s1.util;

import android.hardware.Camera;
import android.util.Log;

import static com.example.project3s1.util.Debug.tag;

public class CameraUtil
{
    private static final int ORIENTATION = 90;

    public static void setOrientation(Camera camera)
    {
        camera.setDisplayOrientation(ORIENTATION);
    }

    public static Camera getCameraInstance()
    {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            Log.w(tag(new CameraUtil()), "getCameraInstance", e);
        }
        return camera;
    }
}
