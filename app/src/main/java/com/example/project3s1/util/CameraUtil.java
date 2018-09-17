package com.example.project3s1.util;

import android.hardware.Camera;
import android.os.Build;
import android.util.Log;

import static com.example.project3s1.util.DebugUtil.tag;

public class CameraUtil
{
    public static void setOrientation(Camera camera)
    {
        int orientation = 90;
        if (Build.MODEL.equals("Nexus 5X"))
            orientation = 270;
        camera.setDisplayOrientation(orientation);
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
