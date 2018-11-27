package com.dyl.base_lib.external

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.support.v4.view.ViewPager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import android.util.Log
import android.util.TypedValue
import android.view.Gravity
import android.view.MotionEvent
import android.view.ViewGroup
import android.widget.HorizontalScrollView
import android.widget.LinearLayout
import android.widget.TextView
import com.dyl.base_lib.R
import com.ppx.kotlin.utils.inject.notify
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class SlidingTabLayout : HorizontalScrollView {
    var rv: RecyclerView? = null
    var vp: ViewPager? = null
    var listTextView  = mutableListOf<TextView>()
    var list = mutableListOf<String>()
        set(value) {
            field = value
            removeAllViews()
            initView()
        }
    var map2height = mutableMapOf<String, Int>()
        set(value) {
            field = value
            list = value.keys.toMutableList()
        }
    val rect: RectF by lazy {
        RectF()
    }
    var w = dip(200).toFloat()
    val h = dip(100).toFloat()
    val paint by lazy {
        Paint().apply {
            style = Paint.Style.FILL
            color = Color.parseColor("#e9d8c4")
            isAntiAlias = true
        }
    }
    var Pindex: Int = -1

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle)

    override fun onFinishInflate() {
        super.onFinishInflate()
        initView()
    }

    fun initView() {
        backgroundColor = Color.parseColor("#f5e8d9")
        addView(LinearLayout(context).apply {
            orientation = LinearLayout.HORIZONTAL
            minimumHeight = h.toInt()
            gravity = Gravity.CENTER_VERTICAL
        })
        listTextView.clear()
        list.forEachIndexed { index, s ->
            var textView = TextView(context)
            listTextView.add(textView)
            (getChildAt(0) as ViewGroup).addView(textView.apply {
                minimumWidth = w.toInt()
                minimumHeight = h.toInt()
                text = s
                gravity = Gravity.CENTER
                setLineSpacing(0f, 1.2f)
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, 28f)

                setOnClickListener {
                   // Pindex  = index
                  //  it.backgroundResource  = R.color.e9d8

                    if (vp != null) {
                        vp?.currentItem = index
                    }
                    if (rv != null) {
                        moveToPosition(map2height?.get(s) ?: 0)
                    }
                }
             /*   onClick {

                    Log.w("test","s="+s+ " index="+index)
                    if (vp != null) {
                        vp?.currentItem = index
                    }
                    if (rv != null) {
                        moveToPosition(map2height?.get(s) ?: 0)
                    }
                }*/
            })
        }
    }

    private fun moveToPosition(n: Int) {
        val firstItem = (rv?.layoutManager as LinearLayoutManager).findFirstVisibleItemPosition()
        val lastItem = (rv?.layoutManager as LinearLayoutManager).findLastVisibleItemPosition()
        if (n <= firstItem) {
            rv?.scrollToPosition(n)

        } else if (n <= lastItem) {
            val top = rv?.getChildAt(n - firstItem)?.top ?: 0
            rv?.scrollBy(0, top)
        } else {
            rv?.scrollToPosition(n)
        }
    }

    fun attachView(recyclerView: RecyclerView) {
        rv = recyclerView
        recyclerView!!.addOnScrollListener(object :RecyclerView.OnScrollListener(){
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val size = computeHorizontalScrollOffset() % recyclerView!!.measuredWidth
                val index = computeHorizontalScrollOffset() / recyclerView!!.measuredWidth
                rect.left = index * w + size * w
                rect.top = 0f
                rect.right = index * w + size * w + w
                rect.bottom = h
                if (rect.left > measuredWidth / 2f - w / 2f) {
                    scrollTo(((index * w + size * w) - (measuredWidth / 2f - w / 2f)).toInt(), 0)
                }
                invalidate()
            }
        })
    }

    fun attachVerView(recyclerView: RecyclerView) {
        rv = recyclerView
//        recyclerView!!.onScrollChange { v, scrollX, scrollY, oldScrollX, oldScrollY ->
//            //  scrollTo(w*recyclerView.layoutManager.f)
//        }
    }

    override fun onDraw(canvas: Canvas?) {
        canvas!!.drawRect(rect, paint)
        super.onDraw(canvas)
    }

    override fun onTouchEvent(ev: MotionEvent?): Boolean {
        return super.onTouchEvent(ev)
    }

    fun attachView(viewpager: ViewPager) {
        vp = viewpager
        viewpager.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                val size = positionOffset % viewpager.measuredWidth
                //val index = positionOffset / viewpager.measuredWidth
                rect.left = position * w + size * w
                rect.top = 0f
                rect.right = position * w + size * w + w
                rect.bottom = h
                if (rect.left > measuredWidth / 2f - w / 2f) {
                    scrollTo(((position * w + size * w) - (measuredWidth / 2f - w / 2f)).toInt(), 0)
                }
                invalidate()
            }

            override fun onPageSelected(position: Int) {
            }

        })

    }


}