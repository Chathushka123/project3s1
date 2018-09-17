package com.example.project3s1;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class DrawOnTop extends FrameLayout
{
    public int mSum = 0;
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
        mPaintBlack.setColor(Color.BLACK);
        mPaintBlack.setTextSize(25);
        View view = inflate(getContext(), R.layout.draw_on_top, null);
        addView(view);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        canvas.drawText("" + mSum, 10-1, 30-1, mPaintBlack);
        super.onDraw(canvas);
    }
}
