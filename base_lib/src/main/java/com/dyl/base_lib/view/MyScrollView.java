package com.dyl.base_lib.view;

import android.content.Context;
import android.support.v4.widget.NestedScrollView;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class MyScrollView extends NestedScrollView {
    private int downX;
    private int downY;
    private int mTouchSlop=10;
    private int srollModle =1; //拦截上下滑动事件，2 滑动左右滑动事件
    private OnScrollListener mOnScrollListener;

    public MyScrollView(Context context) {
        super(context);
    }

    public MyScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public MyScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    /**
     * 监听ScroView的滑动情况
     *
     * @param l    变化后的X轴位置
     * @param t    变化后的Y轴的位置
     * @param oldl 原先的X轴的位置
     * @param oldt 原先的Y轴的位置
     */
    @Override
    protected void onScrollChanged(int l, int t, int oldl, int oldt) {
        super.onScrollChanged(l, t, oldl, oldt);
        if (null != mOnScrollListener) {
            mOnScrollListener.onScroll(t);
        }
    }

    /**
     * 设置滚动接口
     *
     * @param listener
     */
    public void setOnScrollListener(OnScrollListener listener) {
        this.mOnScrollListener = listener;
    }
    /**
     * 滚动的回调接口
     */
    public interface OnScrollListener {

        /**
         * MyScrollView滑动的Y方向距离变化时的回调方法
         *
         * @param scrollY
         */
        void onScroll(int scrollY);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        return super.onTouchEvent(ev);
    }
    public void setModel(int srollModle){
        this.srollModle = srollModle;
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent e) {
        int action = e.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                downX = (int) e.getRawX();
                downY = (int) e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                int moveY = (int) e.getRawY();
                int moveX = (int) e.getRawX();
                if(srollModle==1){
                    if(Math.abs(moveY - downY)>Math.abs(moveX-downX)){
                        return true;
                    }else{
                        return  false;
                    }
                }else {
                    if(Math.abs(moveY - downY)>Math.abs(moveX-downX)){
                        return false;
                    }else{
                        return  true;
                    }
                }
        }
        return super.onInterceptTouchEvent(e);
    }

    @Override
    public boolean onStartNestedScroll(View child, View target, int nestedScrollAxes) {
        return false;
    }

}
