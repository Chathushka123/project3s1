package com.example.project3s1;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

public class DrawOnTop extends FrameLayout
{
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
        View view = inflate(getContext(), R.layout.draw_on_top, null);
        addView(view);
    }

    @Override
    protected void onDraw(Canvas canvas)
    {
        super.onDraw(canvas);
    }
}
