package com.example.project3s1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.example.project3s1.util.IMUtil;

public class DrawOnTop extends FrameLayout
{
    static {
        System.loadLibrary("native-lib");
    }

    private native int[] decodeYUV420sp(byte[] yuv420sp, int dataLength, int width, int height);

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

    @Override
    protected void onDraw(Canvas canvas)
    {

        if (mYuv == null || mRgb == null)
            return;
        mRgb = decodeYUV420sp(mYuv, mDataLength, mWidth, mHeight);
        if (mRgb == null)
            return;
        int[] rgb = IMUtil.extractRgb(mRgb[mDataLength/2]);
        /*canvas.drawText("r:" + rgb[0] + " g:" + rgb[1] + " b:" + rgb[2], 50, 50, mPaintBlack);
        super.onDraw(canvas);*/

        String colour=IMUtil.findColour(rgb);

        canvas.drawText(colour, mWidth/2,50, mPaintBlack);
        super.onDraw(canvas);
    }
}
