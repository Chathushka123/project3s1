package com.example.project3s1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;

import com.example.project3s1.util.DebugUtil;

public class DrawOnTop extends FrameLayout
{
    static {
        System.loadLibrary("native-lib");
    }

    private native int[] decodeYUV420sp(byte[] yuv420sp, int dataLength, int width, int height);
    private native Bitmap fillBitmap(byte[] yuv420sp, int dataLength, int width, int height, Bitmap bitmap);

    public int[] mRgb;
    public byte[] mYuv;
    public int mDataLength;
    public int mWidth;
    public int mHeight;

    private Paint mPaintBlack;

    public DrawOnTop(Context context)
    {
        super(context);
        init();
    }

    public DrawOnTop(Context context, AttributeSet attrs)
    {
        super(context, attrs);
        init();
    }

    public DrawOnTop(Context context, AttributeSet attrs, int defStyleAttr)
    {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init()
    {
        mPaintBlack = new Paint();
        mPaintBlack.setStyle(Paint.Style.FILL);
        mPaintBlack.setColor(Color.WHITE);
        mPaintBlack.setTextSize(30);

        View view = inflate(getContext(), R.layout.draw_on_top, null);
        addView(view);
        mRgb = null;
        mYuv = null;
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas)
    {
        if (mYuv == null || mRgb == null)
            return;
        /*
        mRgb = decodeYUV420sp(mYuv, mDataLength, mWidth, mHeight);
        if (mRgb == null)
            return;
        */

        /*
        int[] rgb = new int[3];
        if(mDataLength % 2 == 0) {
             rgb = IMUtil.extractRgb(mRgb[(mDataLength / 2)-(mWidth/2)]);
        }
        else{
             rgb = IMUtil.extractRgb(mRgb[(mDataLength / 2)+1]);
        }
        String colour=IMUtil.findColour(rgb);
        canvas.drawText(colour, 50,50, mPaintBlack);
        */
        Bitmap.Config conf = Bitmap.Config.ARGB_8888;
        Bitmap bmp = Bitmap.createBitmap(canvas.getWidth(), canvas.getHeight(), conf);
        bmp = fillBitmap(mYuv, mDataLength, mWidth, mHeight, bmp);
        if (bmp == null)
            return;
        canvas.drawBitmap(bmp, 0, 0, null);
        super.onDraw(canvas);
    }
}
