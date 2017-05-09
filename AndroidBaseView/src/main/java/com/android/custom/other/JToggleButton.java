package com.android.custom.other;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * author cipherGG
 * Created by Administrator on 2015/12/29.
 * describe
 */
public class JToggleButton extends View {

    private ToggleState toggleState;
    private Bitmap slideBg;
    private Bitmap switchBg;
    private int currentX;
    private boolean isSliding;

    public OnToggleStateChangeListener listener;

    public JToggleButton(Context context) {
        super(context);
    }

    public JToggleButton(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public JToggleButton(Context context, AttributeSet attrs, int currentX) {
        super(context, attrs);
        this.currentX = currentX;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(switchBg.getWidth(), switchBg.getHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 绘制背景
        canvas.drawBitmap(switchBg, 0, 0, null);
        // 绘制滑动块
        if (isSliding) {
            int left = currentX - slideBg.getWidth() / 2;
            if (left < 0) left = 0;
            if (left > switchBg.getWidth() - slideBg.getWidth()) {
                left = switchBg.getWidth() - slideBg.getWidth();
            }
            canvas.drawBitmap(slideBg, left, 0, null);
        } else {
            if (toggleState == ToggleState.Open) {
                canvas.drawBitmap(slideBg, switchBg.getWidth() - slideBg.getWidth(), 0, null);
            } else {
                canvas.drawBitmap(slideBg, 0, 0, null);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        currentX = (int) event.getX();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isSliding = true;
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
                isSliding = false;
                int centerX = switchBg.getWidth() / 2;
                if (currentX > centerX) {//open
                    if (toggleState != ToggleState.Open) {
                        toggleState = ToggleState.Open;
                        if (listener != null) {//当状态发生改变的稍后才会调用借口
                            listener.onToggleStateChange(toggleState);
                        }
                    }
                } else {//close
                    if (toggleState != ToggleState.Close) {
                        toggleState = ToggleState.Close;
                        if (listener != null) {//当状态发生改变的稍后才会调用借口
                            listener.onToggleStateChange(toggleState);
                        }
                    }
                }
                break;
        }
        invalidate();// 相当于notifyAdapter()
        return true;
        // return super.onTouchEvent(event);
    }

    /**
     * 设置背景
     */
    public void setSwitchBackground(int resource) {
        switchBg = BitmapFactory.decodeResource(getResources(), resource);
    }

    /**
     * 设置划块
     */
    public void setSlideBackground(int resource) {
        slideBg = BitmapFactory.decodeResource(getResources(), resource);
    }

    /**
     * 设置状态
     */
    public void setToggleState(ToggleState state) {
        toggleState = state;
    }

    public interface OnToggleStateChangeListener {
        void onToggleStateChange(ToggleState state);
    }

    public void setOnToggleStateChangeListener(OnToggleStateChangeListener listener) {
        this.listener = listener;
    }

    public enum ToggleState {
        Open, Close
    }

}
