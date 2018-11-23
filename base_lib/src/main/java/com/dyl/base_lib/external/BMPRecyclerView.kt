package com.dyl.base_lib.external

import android.content.Context
import android.graphics.*
import android.support.annotation.DrawableRes
import android.support.v4.view.MotionEventCompat
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.ViewConfiguration
import android.widget.Scroller
import com.dyl.base_lib.R
import com.dyl.base_lib.util.measureTextCenterY
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.dip


/**
 * Created by dengyulin
 */

open class BMPRecyclerView : RecyclerView {
    private val INVALID_POINTER = -1
    private var overScroller: Scroller? = null
    private var mActivePointerId: Int = 0
    private var mLastY = 0f
    private var rotate = 0f
    var bitmap: Bitmap? = null
    private var isLoad = 0
    private var isNext = 0
    private val loadHeight = dip(120)
    private var totalMove = 0
    private var mTouchSlope = 0
    var fontSize = dip(24).toFloat()
        set(value) {
            field = value
            paint.textSize = value
        }
    var refreshStrs = listOf(
            "下拉刷新....",
            "松开刷新....",
            "正在刷新...."
    )
    var moreStrs = listOf(
            "上拉加载....",
            "松开加载....",
            "正在加载...."
    )

    fun loadBitmap(@DrawableRes id: Int, r: Int = dip(45)) {
        bitmap = Bitmap.createScaledBitmap(BitmapFactory.decodeResource(resources, id), r, r, false)
    }

    private var onLoad: (BMPRecyclerView.() -> Unit)? = null
    private var onNext: (BMPRecyclerView.() -> Unit)? = null
    private val paint by lazy {
        Paint().apply {
            color = Color.parseColor("#8a8a8a")
            isAntiAlias = true
            style = Paint.Style.STROKE
            textSize = fontSize
        }
    }
    private var jobs = getJob().apply { cancel() }
    private fun getJob() = launch(CommonPool) {
        while (true) {
            delay(8)
            rotate += 1
            postInvalidate()
        }
    }.apply { start() }

    private val verticalSpace: Int
        get() = height - paddingBottom - paddingTop

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }


    //脚部下拉
    private fun getFootPy(deltaY: Int): Int {
        //整体高度 - 滑动X - 页面高度 +滑动距离
        if (childCount > 0) {
            if (verticalSpace > computeVerticalScrollRange()) {
                return (deltaY * Math.pow(((verticalSpace.toFloat() - computeVerticalScrollOffset() - deltaY) / verticalSpace.toFloat()).toDouble(), 5.0)).toInt()
            } else {
                return (deltaY * Math.pow(((computeVerticalScrollRange() - computeVerticalScrollOffset() - deltaY) / verticalSpace.toFloat()).toDouble(), 5.0)).toInt()
            }
            return (deltaY * Math.pow(((verticalSpace.toFloat() - computeVerticalScrollOffset() - deltaY) / verticalSpace.toFloat()).toDouble(), 5.0)).toInt()
        }
        return (deltaY * Math.pow(((verticalSpace.toFloat() - totalMove - deltaY) / verticalSpace.toFloat()).toDouble(), 5.0)).toInt()
    }

    //头部下拉
    private fun getHeadPy(deltaY: Int): Int {
        if (childCount > 0) {
            return (deltaY * Math.pow(((verticalSpace + computeVerticalScrollOffset() + deltaY) / verticalSpace.toFloat()).toDouble(), 5.0)).toInt()
        }
        return (deltaY * Math.pow(((verticalSpace + (totalMove + deltaY)) / verticalSpace.toFloat()).toDouble(), 5.0)).toInt()
    }

    override fun dispatchTouchEvent(ev: MotionEvent): Boolean {
        val totalMove = if (childCount > 0) computeVerticalScrollOffset() else totalMove
        when (MotionEventCompat.getActionMasked(ev)) {
            MotionEvent.ACTION_MOVE -> {
                val activePointerId = mActivePointerId
                if (activePointerId != INVALID_POINTER) {
                    val pointerIndex = ev.findPointerIndex(activePointerId)
                    val y = ev.getY(pointerIndex)
                    val deltaY = mLastY - y
                    val hs = totalMove + deltaY
                    mLastY = y
                    if (hs <= 0) {
                        val h = getHeadPy(-deltaY.toInt())
                        moveTo(h)
                        if (!jobs.isActive) {
                            jobs = getJob()
                        }
                        isLoad = if (hs <= -loadHeight - dip(30) && onLoad != null) 1 else 0
                    } else if (hs >= (computeVerticalScrollRange() - verticalSpace)) {
                        val h = getFootPy(deltaY.toInt())
                        moveTo(-h)
                        if (!jobs.isActive) {
                            jobs = getJob()
                        }
                        isNext = if (computeVerticalScrollRange() > verticalSpace) {
                            if (hs >= (computeVerticalScrollRange() - verticalSpace) + loadHeight + dip(30)) 1 else 0
                        } else {
                            if (hs + verticalSpace >= verticalSpace + loadHeight + dip(30)) 1 else 0
                        }
                        if (onNext == null) {
                            isNext = 0
                        }
                    } else {
                        if (jobs.isActive) {
                            jobs.cancel()
                        }
                    }
                }
            }
            MotionEvent.ACTION_DOWN -> {
                mLastY = ev.y
                mActivePointerId = ev.getPointerId(0)
            }
            MotionEvent.ACTION_CANCEL -> {
                mActivePointerId = INVALID_POINTER
                if (isLoad == 1) {
                    resetToLoad()
                } else if (isNext == 1) {
                    resetToNext()
                } else if (isNext == 0 && isLoad == 0) {
                    reset()
                }
            }
            MotionEvent.ACTION_UP -> {
                /* Release the drag */
                mActivePointerId = INVALID_POINTER
                if (isLoad == 1) {
                    resetToLoad()
                } else if (isNext == 1) {
                    resetToNext()
                } else if (isNext == 0 && isLoad == 0) {
                    reset()
                }

            }
            MotionEvent.ACTION_POINTER_UP -> onSecondaryPointerUp(ev)
        }
        return super.dispatchTouchEvent(ev)
    }

    private var lastPointX = 0f
    private var lastPointY = 0f
    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        super.onInterceptTouchEvent(ev)
        var isTouch = false
        when (ev!!.action) {
            MotionEvent.ACTION_DOWN -> {
                lastPointX = ev.x
                lastPointY = ev.y
            }
            MotionEvent.ACTION_MOVE -> {
                val xDiff = Math.abs(lastPointX - ev.x)
                val yDiff = Math.abs(ev.y - lastPointY)
                if (yDiff > mTouchSlope && yDiff * 0.5f > xDiff) {
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


    private fun moveTo(move: Int) {
        if (childCount > 0) {
            offsetChildrenVertical(move)
        } else {
            totalMove -= move
        }

    }

    private fun onSecondaryPointerUp(ev: MotionEvent) {
        val pointerIndex = ev.action and MotionEvent.ACTION_POINTER_INDEX_MASK shr MotionEvent.ACTION_POINTER_INDEX_SHIFT
        val pointerId = ev.getPointerId(pointerIndex)
        if (pointerId == mActivePointerId) {
            // TODO: Make this decision more intelligent.
            val newPointerIndex = if (pointerIndex == 0) 1 else 0
            mLastY = ev.getY(newPointerIndex)
            mActivePointerId = ev.getPointerId(newPointerIndex)
        }
    }

    //    fun MoveToPosition(manager: LinearLayoutManager, mRecyclerView: RecyclerView, n: Int) {
//
//
//        val firstItem = manager.findFirstVisibleItemPosition()
//        val lastItem = manager.findLastVisibleItemPosition()
//        if (n <= firstItem) {
//            mRecyclerView.scrollToPosition(n)
//        } else if (n <= lastItem) {
//            val top = mRecyclerView.getChildAt(n - firstItem).top
//            mRecyclerView.scrollBy(0, top)
//        } else {
//            mRecyclerView.scrollToPosition(n)
//        }
//
    private fun resetScroller(move: Int) {
        if (move < 0) {
            overScroller!!.startScroll(0, move, 0, -move, 250)
            scrollyY = 0
            invalidate()

        } else if (move > computeVerticalScrollRange() - verticalSpace) {
            if (computeVerticalScrollRange() > verticalSpace) {
                overScroller!!.startScroll(0, move, 0, -(verticalSpace - getChildAt(childCount - 1).bottom), 250)
                scrollyY = move
                invalidate()
            } else {
                overScroller!!.startScroll(0, move, 0, -computeVerticalScrollOffset(), 500)
                scrollyY = move
                invalidate()

            }

        }
    }

    //重置
    fun reset() {
        if (!overScroller!!.isFinished) {
            overScroller!!.abortAnimation()
        }
        if (childCount > 0) {
            resetScroller(computeVerticalScrollOffset())
        } else {
            resetScroller(totalMove)
        }
        if (jobs.isActive) {
            jobs.cancel()
        }
        if (isLoad != 0) {
            isLoad = 0
        }
        if (isNext != 0) {
            isNext = 0
        }
        totalMove = 0
    }

    private fun resetToLoad() {
        if (!overScroller!!.isFinished) {
            overScroller!!.abortAnimation()
        }
        if (childCount > 0) {
            if (computeVerticalScrollOffset() < 0) {
                overScroller!!.startScroll(0, computeVerticalScrollOffset(), 0, (-computeVerticalScrollOffset()) - loadHeight, 250)
                scrollyY = 0
            }
        } else {
            if (totalMove < 0) {
                overScroller!!.startScroll(0, totalMove, 0, -totalMove - loadHeight, 250)
                scrollyY = 0
            }
        }
        invalidate()
        if (onLoad != null) {
            if (isLoad != 2) {
                onLoad!!.invoke(this)
            }
            isLoad = 2
        }
        totalMove = 0
    }

    private fun nextScroll(move: Int) {
        if (computeVerticalScrollRange() > verticalSpace) {
            overScroller!!.startScroll(0, move, 0, (computeVerticalScrollRange() - verticalSpace) - move + loadHeight, 250)
            scrollyY = 0
        } else {
            overScroller!!.startScroll(0, move, 0, -move + loadHeight, 250)
            scrollyY = move
        }
    }

    private fun resetToNext() {
        if (!overScroller!!.isFinished) {
            overScroller!!.abortAnimation()
        }
        if (childCount > 0) {
            nextScroll(computeVerticalScrollOffset())
        } else {
            nextScroll(totalMove)
        }
        invalidate()
        if (onNext != null) {
            if (isNext != 2) {
                onNext!!.invoke(this)
            }
            isNext = 2
        }
        totalMove = 0

    }

    override fun onMeasure(widthSpec: Int, heightSpec: Int) {
        super.onMeasure(widthSpec, heightSpec)

    }

    //baseline = center + (FontMetrics.bottom - FontMetrics.top)/2 - FontMetrics.bottom
    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        val scrollToMove = if (childCount > 0) {
            computeVerticalScrollOffset()
        } else {
            totalMove
        }
        if (bitmap != null) {
            if (scrollToMove <= 0) {
                if (onLoad == null) return
                val str = refreshStrs[isLoad]
                val wstr = (measuredWidth - (bitmap!!.width + dip(20) + paint.measureText(str))) / 2
                val texth = paint.measureTextCenterY(loadHeight / 2f)
                canvas.drawText(str, wstr + bitmap!!.width + dip(20), -(scrollToMove + (loadHeight - texth)), paint)
                val matrix = Matrix()
                matrix.postRotate(rotate, bitmap!!.width.toFloat() / 2f, bitmap!!.height.toFloat() / 2f)
                matrix.postTranslate(wstr, (-scrollToMove.toFloat() - (loadHeight + bitmap!!.height.toFloat()) / 2f))
                canvas.drawBitmap(bitmap, matrix, paint)
            } else if (scrollToMove >= computeVerticalScrollRange() - verticalSpace) {
                if (onNext == null) return
                val str = moreStrs[isNext]
                val wstr = (measuredWidth - (bitmap!!.width + dip(20) + paint.measureText(str))) / 2
                var size = if (computeVerticalScrollRange() > verticalSpace) {
                    (measuredHeight - (scrollToMove + verticalSpace - computeVerticalScrollRange())).toFloat()
                } else {
                    (measuredHeight - (scrollToMove + verticalSpace - computeVerticalScrollRange())).toFloat()
                }
                val texth = paint.measureTextCenterY(loadHeight / 2f)
                canvas.drawText(str, wstr + bitmap!!.width + dip(20), size + texth, paint)
                val matrix = Matrix()
                matrix.postRotate(rotate, bitmap!!.width.toFloat() / 2f, bitmap!!.height.toFloat() / 2f)
                matrix.postTranslate(wstr, size + (loadHeight / 2f - bitmap!!.height.toFloat() / 2f))
                canvas.drawBitmap(bitmap, matrix, paint)
            } else {

            }
        }
    }

    var scrollyY = 0
    override fun computeScroll() {
        if (overScroller!!.computeScrollOffset()) {
            if (scrollyY != 0) {
                var dy = (if (childCount > 0) scrollyY else totalMove) - overScroller!!.currY
                if (childCount > 0) {
                    if (computeVerticalScrollRange() < verticalSpace) {
                        if (computeVerticalScrollOffset() - dy <= 0) {
                            offsetChildrenVertical(-computeVerticalScrollOffset())
                            overScroller?.abortAnimation()
                            requestLayout()
                            return
                        }

                        if (isNext == 2) {
                            if (computeVerticalScrollOffset() - dy <= context.dip(120)) {
                                overScroller?.abortAnimation()
                                offsetChildrenVertical(scrollyY-overScroller!!.finalY)
                                scrollyY -= dy
                                invalidate()
                                return
                            }
                        }

                    }
                    offsetChildrenVertical(dy)

                } else {
                    totalMove -= dy
                }

                scrollyY -= dy
                invalidate()
            } else {
                var dy = (if (childCount > 0) computeVerticalScrollOffset() else totalMove) - overScroller!!.currY
                moveTo(dy)
                invalidate()
            }


        }
    }

    private fun init(context: Context) {
        overScroller = Scroller(context)
        post { scrollTo(0, 0) }
        bitmap ?: loadBitmap(R.drawable.load)
        overScrollMode = View.OVER_SCROLL_NEVER
        mTouchSlope = ViewConfiguration.get(context).scaledPagingTouchSlop
    }

    fun onPullLoad(call: BMPRecyclerView.() -> Unit) {
        onLoad = call
    }

    fun onPullNext(call: BMPRecyclerView.() -> Unit) {
        onNext = call
    }

}
