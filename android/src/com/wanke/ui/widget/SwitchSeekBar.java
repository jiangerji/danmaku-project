package com.wanke.ui.widget;

import android.content.Context;
import android.graphics.drawable.ClipDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.widget.Checkable;
import android.widget.SeekBar;

public class SwitchSeekBar extends SeekBar implements
        SeekBar.OnSeekBarChangeListener, Checkable {
    public final static String TAG = "util";

    private int scaledTouchSlop;
    private float touchDownX;
    private boolean mChecked;
    LayerDrawable progressDrawable;
    Drawable[] outDrawables;
    ClipDrawable proDrawable;
    private OnCheckedChangeListener mOnCheckedChangeListener;

    public SwitchSeekBar(Context context) {
        super(context);
        init(context);
    }

    public SwitchSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public SwitchSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setOnSeekBarChangeListener(this);
        scaledTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();
        mChecked = false;
        progressDrawable = (LayerDrawable) getProgressDrawable();
        outDrawables = new Drawable[progressDrawable.getNumberOfLayers()];
        for (int i = 0; i < progressDrawable.getNumberOfLayers(); i++) {
            switch (progressDrawable.getId(i)) {
            case android.R.id.progress:// ���ý����
                proDrawable = (ClipDrawable) progressDrawable.getDrawable(i);
                break;
            default:
                break;
            }
        }
    }

    public static interface OnCheckedChangeListener {
        void onCheckedChanged(boolean isChecked);
    }

    /**
     * Register a callback to be invoked when there is an attempt to change the
     * state of the switch when its in fixated
     * 
     * @param listener
     *            the callback to call on checked state change
     */
    public void setOnCheckedChangeListener(OnCheckedChangeListener listener) {
        mOnCheckedChangeListener = listener;
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        switch (event.getAction()) {
        case MotionEvent.ACTION_DOWN:
            touchDownX = event.getX();
            break;

        case MotionEvent.ACTION_MOVE:
            break;

        case MotionEvent.ACTION_UP:
            final float x = event.getX();
            if (Math.abs(x - touchDownX) < scaledTouchSlop) {
                performOnClick();
                return true;
            }
            break;

        case MotionEvent.ACTION_CANCEL:
            break;
        }
        return super.onTouchEvent(event);

    }

    private boolean performOnClick() {
        toggle();
        return super.performClick();
    }

    /*
     * SeekBarֹͣ�����Ļص�����
     */
    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {
        if (seekBar.getProgress() > 50) {
            setChecked(true);
            setProgress(100);
        } else {
            setChecked(false);
            setProgress(0);
        }
        proDrawable.setAlpha(255);
    }

    /*
     * SeekBar��ʼ�����Ļص�����
     */
    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    /*
     * SeekBar����ʱ�Ļص�����
     */
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress,
            boolean fromUser) {
        proDrawable.setAlpha(seekBar.getProgress() * 255 / 100);

    }

    public boolean isChecked() {
        return mChecked;
    }

    public void setChecked(boolean checked) {
        if (mChecked != checked) {
            mChecked = checked;
            if (mChecked) {
                setProgress(100);
            } else {
                setProgress(0);
            }
            getProgressDrawable().setAlpha(255);
            if (mOnCheckedChangeListener != null) {
                mOnCheckedChangeListener.onCheckedChanged(mChecked);
            }
        }
    }

    @Override
    public void toggle() {
        setChecked(!mChecked);
    }
}
