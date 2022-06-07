package com.lib.view.testview;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.lib.code.R;


public class CircleTestView extends View {
//    public CircleTestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
//        super(context, attrs, defStyleAttr, defStyleRes);
//    }
    private Context mContext;
    private int mColor= Color.RED;
    private Paint mPaint=new Paint(Paint.ANTI_ALIAS_FLAG);

    public CircleTestView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context,attrs,0);
        mContext=context;
        TypedArray array=mContext.obtainStyledAttributes(attrs, R.styleable.CircleTestView);
        mColor=array.getColor(R.styleable.CircleTestView_circle_color,Color.RED);
        init();
    }

    public CircleTestView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public CircleTestView(Context context) {
        super(context);
        mContext=context;
        init();
    }
    private void init(){
        mPaint.setColor(mColor);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec,heightMeasureSpec);
        int widthSpecMode=MeasureSpec.getMode(widthMeasureSpec);
        int widthSpecSize=MeasureSpec.getSize(widthMeasureSpec);
        int heightSpecMode=MeasureSpec.getMode(heightMeasureSpec);
        int heightSpecSize=MeasureSpec.getSize(heightMeasureSpec);
        if (widthSpecMode == MeasureSpec.AT_MOST
                && heightMeasureSpec ==MeasureSpec.AT_MOST){
            setMeasuredDimension(200,200);
        }else  if (widthSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(200,heightSpecSize);
        }else if (heightSpecMode == MeasureSpec.AT_MOST){
            setMeasuredDimension(widthSpecSize,200);
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        final int paddingLeft=getPaddingLeft();
        final int paddingRight=getPaddingLeft();
        final int paddingTop=getPaddingLeft();
        final int paddingBottom=getPaddingLeft();
        int width=getWidth()-paddingLeft-paddingRight;
        int height=getHeight()-paddingTop-paddingBottom;
        int radiu=Math.min(width,height)/2;
        canvas.drawCircle(paddingLeft+width/2,paddingTop+height/2,radiu,mPaint);
    }

}
