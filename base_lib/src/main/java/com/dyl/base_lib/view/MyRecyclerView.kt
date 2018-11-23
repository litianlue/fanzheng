package com.dyl.base_lib.view

import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent

import com.dyl.base_lib.external.BMPRecyclerView

class MyRecyclerView : BMPRecyclerView {
    var moveX = 0
    private var lastPointX = 0f
    private var lastPointY = 0f

    private var forbid:Boolean = false //1 禁止向左边滑动
    fun forbidMoveLeft(mode:Boolean){
        forbid  = mode
    }
    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {}

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {}
    fun setMove(x:Int){
        moveX = x
    }
    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {//派遣
        parent.requestDisallowInterceptTouchEvent(true)
        return super.dispatchTouchEvent(ev)
    }


}
