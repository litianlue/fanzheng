package com.dyl.base_lib.external

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.FrameLayout
import android.widget.TextView
import com.dyl.base_lib.show.anim.move
import org.jetbrains.anko.*

/**
 * Created by dengyulin on 2017/9/12.
 */
class SweepItem : FrameLayout {
    var otherView: View? = null
    var lastPointX: Float = 0f
    var lastPointY: Float = 0f
    var touchMove = 0f
    var mTouchSlope: Int = 0
    val delete by lazy {
        TextView(context).apply {
            text = "删除"
            textColor = Color.WHITE
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30f)
            gravity = Gravity.CENTER
            backgroundColor = Color.RED

        }
    }

    fun reset() {
        otherView!!.move(otherView!!.translationX, x1 = 0f, time = 250)
    }
    fun resetNoAnim() {
        otherView!!.translationX=0f
    }

    constructor(context: Context?) : super(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        mTouchSlope = ViewConfiguration.get(context).scaledPagingTouchSlop
    }

    override fun onFinishInflate() {
        super.onFinishInflate()
        otherView = getChildAt(0)
        delete.layoutParams=FrameLayout.LayoutParams(matchParent, matchParent).apply {
            gravity = Gravity.RIGHT
        }
        addView(delete, 0)

    }
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        delete.measure(MeasureSpec.makeMeasureSpec(dip(120),MeasureSpec.EXACTLY),MeasureSpec.makeMeasureSpec(measuredHeight-bottomPadding,MeasureSpec.EXACTLY))
    }
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        var isTouch = false
        when (ev!!.action) {
            MotionEvent.ACTION_DOWN -> {
                lastPointX = ev.x
                lastPointY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                val xDiff = Math.abs(lastPointX - ev.x)
                val yDiff = Math.abs(ev.y - lastPointY)
                if (xDiff > mTouchSlope && xDiff * 0.5f > yDiff) {
                    requestDisallowInterceptTouchEvent(true)
                    isTouch = true
                } else {
                    isTouch = false
                }
                lastPointX = ev.x
                lastPointY = ev.y
            }
            MotionEvent.ACTION_UP -> {
                lastPointX = 0f
                lastPointY = 0f
                isTouch = false

            }
        }
        return isTouch
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        when (ev!!.action) {
            MotionEvent.ACTION_DOWN -> {
                lastPointX = ev.x
                lastPointY = ev.y
                touchMove = 0f
            }
            MotionEvent.ACTION_MOVE -> {
                val xDiff = Math.abs(lastPointX - ev.x)
                val yDiff = Math.abs(lastPointY - ev.y)
                if (xDiff > mTouchSlope && xDiff > yDiff) {
                    requestDisallowInterceptTouchEvent(true)
//                    if (Math.abs(otherView!!.translationX - (xDiff)) > delete.measuredWidth) {
//                    } else {
                        if (otherView!!.translationX - (lastPointX - ev.x) < 0) {
                            otherView!!.translationX -= lastPointX - ev.x
                            touchMove += lastPointX - ev.x
                        } else {
                            otherView!!.translationX = 0f
                        }
//                    }
                    parent?.requestDisallowInterceptTouchEvent(true)
                }
                lastPointX = ev.x
                lastPointY = ev.y
            }
            MotionEvent.ACTION_UP -> {
                if (Math.abs(otherView!!.translationX) > delete.measuredWidth / 2f) {
                    otherView!!.move(otherView!!.translationX, x1 = -delete.measuredWidth.toFloat(), time = 250)
                } else {
                    reset()
                }
                lastPointX = 0f
                lastPointY = 0f
            }
        }
        return true
    }

}