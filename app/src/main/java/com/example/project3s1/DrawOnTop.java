package com.example.project3s1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;

public class DrawOnTop extends View
{
    private Paint mPaintBlack;

    public DrawOnTop(Context context)
    {
        super(context);
        init();
    }

    private void init()
    {
        mPaintBlack = new Paint();
        mPaintBlack.setStyle(Paint.Style.FILL);
        mPaintBlack.setColor(Color.BLACK);
        mPaintBlack.setTextSize(25);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawText("Hello, World", 10-1, 30-1, mPaintBlack);
        super.onDraw(canvas);
    }
}
