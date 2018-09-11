package com.example.project3s1;

import android.Manifest;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    private static String TAG = "MainActivity";

    private Button capture;
    private Camera mCamera;
    private CameraPreview mPreview;
    private static final int MY_PERMISSIONS_REQUEST_CAMERA = 1;
    private Camera.PictureCallback pictureCallback;

    public native String stringFromJNI();

    static {
        System.loadLibrary("native-lib");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        capture=findViewById(R.id.btn1);

        grantCameraPermissions();
        if (mCamera != null) {
            loadCameraView();
        }

        capture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCamera.takePicture(null,null,pictureCallback);
            }

        });
        pictureCallback=new Camera.PictureCallback() {
            @Override
            public void onPictureTaken(byte[] data, Camera camera) {
                Bitmap bmp= BitmapFactory.decodeByteArray(data,0,data.length);
                Bitmap cbmp=Bitmap.createBitmap(bmp,0,0,bmp.getWidth(),
                        bmp.getHeight(),null,true);
                String pathFileName=currentDateFormat();
                if(isExternalStorageWritable()) {
                    storePhotoStorage(cbmp, pathFileName);
                    Toast.makeText(getApplicationContext(),"Done!!!!",Toast.LENGTH_LONG).show();
                }

                mCamera.startPreview();
            }
        };
    }
    private void storePhotoStorage(Bitmap cbmp,String pathFileName){
        File outputFile=new File(Environment.getExternalStorageDirectory(),
                "/DCIM/"+"photo_"+pathFileName+".jpg");
        try {
            FileOutputStream fileOutputStream=new FileOutputStream(outputFile);
            cbmp.compress(Bitmap.CompressFormat.JPEG,100,fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String currentDateFormat(){
        SimpleDateFormat dateFormat=new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String currentTime=dateFormat.format(new Date());
        return currentTime;
    }

    private boolean isExternalStorageWritable() {
        return Environment.getExternalStorageState() == Environment.MEDIA_MOUNTED;
    }3aw


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
        mCamera.setDisplayOrientation(90);
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
