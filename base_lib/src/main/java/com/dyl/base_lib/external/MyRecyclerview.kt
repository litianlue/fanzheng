package com.dyl.base_lib.external

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent

class MyRecyclerview : RecyclerView {
    private var position = 0

    constructor(context: Context) : super(context) {}

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {}

    constructor(context: Context, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {}

    fun setPosition(position: Int) {
        this.position = position
    }

    override fun dispatchTouchEvent(e: MotionEvent): Boolean {//派遣
        val x = e.x.toInt()
        val y = e.y.toInt()
        val view = findChildViewUnder(x.toFloat(), y.toFloat())

        val childLayoutPosition = getChildLayoutPosition(view)
        if (childLayoutPosition != 0)
            parent.requestDisallowInterceptTouchEvent(true)
        return super.dispatchTouchEvent(e)
    }


}
