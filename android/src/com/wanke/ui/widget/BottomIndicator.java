package com.wanke.ui.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.wanke.tv.R;

public class BottomIndicator extends View {
    // 当前选择
    private int mCurrentSelection = 0;

    // 数量
    private int mCount = 0;

    // 半径
    private int mRadius = 0;

    private RectF oval = new RectF();

    // 圆点之剑间隔
    private int mPadding = (int) getResources().getDimension(
            R.dimen.bottom_indicator_gap);

    private Paint mPaint;
    private Paint mArcPaint;

    public BottomIndicator(Context context) {
        super(context);
    }

    public BottomIndicator(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setNumber(int number) {
        if (number <= 0) {
            throw new IllegalArgumentException("number should great than 0!");
        }

        setBackgroundColor(Color.TRANSPARENT);

        mCount = number;
        mRadius = (int) getResources().getDimension(
                R.dimen.bottom_indicator_raduis);
        mCurrentSelection = 0;

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setAlpha(255);
        mPaint.setColor(Color.WHITE);

        mArcPaint = new Paint();
        mArcPaint.setAntiAlias(true);
        mArcPaint.setColor(Color.WHITE);
        mArcPaint.setStrokeWidth((float) 3.0);
        mArcPaint.setStyle(Style.STROKE);
    }

    public void setRadius(int radius) {
        mRadius = radius;
    }

    public void setSelection(int index) {
        if (index >= 0 && index < mCount && index != mCurrentSelection) {
            mCurrentSelection = index;

            invalidate();
        }
    }

    private int measureHeight(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        Log.d("indicator", "measureHeight:" + specSize + "," + specMode);
        // Default size if no limits are specified.
        int result = 10 + 2 * mRadius;

        return result;
    }

    private int measureWidth(int measureSpec) {
        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);
        Log.d("indicator", "measureWidth:" + specSize + "," + specMode);
        // Default size if no limits are specified.
        int result = mCount * (2 * mRadius + mPadding);

        return result;
    }

    @Override
    protected void onMeasure(int arg0, int arg1) {
        super.onMeasure(arg0, arg1);

        int measuredHeight = measureHeight(arg1);
        int measuredWidth = measureWidth(arg0);
        setMeasuredDimension(measuredWidth, measuredHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int width = getMeasuredWidth();
        int height = getMeasuredHeight();

        int contentWidth = mCount * (2 * mRadius + mPadding) - mPadding;

        int beginX = (width - contentWidth) / 2;
        int beginY = height / 2;

        canvas.save();

        int radius;
        for (int i = 0; i < mCount; i++) {
            if (i == mCurrentSelection) {
                radius = mRadius;
                canvas.drawCircle(beginX, beginY, radius, mPaint);
            } else {
                radius = mRadius;
                oval.set(beginX - radius, beginY - radius, beginX + radius,
                        beginY + radius);
                canvas.drawArc(oval, 0, 360, false, mArcPaint);
            }

            beginX = beginX + radius + radius + mPadding;
        }
        canvas.restore();
    }
}
