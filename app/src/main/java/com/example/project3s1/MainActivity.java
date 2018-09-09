package com.example.project3s1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    private Camera mCamera;
    private CameraPreview mPreview;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;

    public native String stringFromJNI();

    static {
        System.loadLibrary("native-lib");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        grantCameraPermissions();
        if (mCamera != null) {
            loadCameraView();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        releaseCamera();
    }

    public void releaseCamera() {
        if(mCamera != null) {
            mCamera.release();
            mCamera = null;
        }
    }

    private void loadCameraView() {
        mCamera.setDisplayOrientation(270);
        mPreview = new CameraPreview(this, mCamera);
        ConstraintLayout preview = (ConstraintLayout) findViewById(R.id.camera_preview);
        preview.addView(mPreview);

    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                  View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }

    public static Camera getCameraInstance() {
        Camera c = null;
        try {
            c = Camera.open();
        } catch (Exception e) {
            Log.w(TAG, "getCameraInstance", e);
        }
        return c;
    }

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
                    loadCameraView();
                } else {
                    /* permission is denied by user */
                }
        }
    }
}
