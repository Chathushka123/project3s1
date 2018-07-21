package com.example.project3s1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    private Camera mCamera;
    private CameraPreview mPreview;

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            Log.w(TAG, "getCameraInstance", e);
        }
        return c;
    }

    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

    private void grantCameraPermissions() {
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            /* try for camera permissions */
            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                    Manifest.permission.CAMERA))
            {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA},
                        MY_PERMISSIONS_REQUEST_CAMERA);
            }
        } else {
            /* permission is already granted for the camera */
            mCamera = getCameraInstance();
        }
    }

    /*
     * This callback will run when permission is granted
     */
    @Override
    public void onRequestPermissionsResult(
            int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_CAMERA:
                /* result array is empty => request is cancelled */
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    mCamera = getCameraInstance();
                } else {
                    /* permission is denied by user */
                }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /* !!! this shouldn't be the way fullscreen mode is handled !!! */
        /* remove title */
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        /* go fullscreen */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        this.setTitle(stringFromJNI());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grantCameraPermissions();
        if (mCamera != null) {
            mCamera.setDisplayOrientation(90);
            mPreview = new CameraPreview(this, mCamera);
            FrameLayout preview = (FrameLayout) findViewById(R.id.camera_preview);
            preview.addView(mPreview);
        }
    }

    public native String stringFromJNI();

    static {
        System.loadLibrary("native-lib");
    }
}
