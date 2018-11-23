package com.dyl.base_lib.external

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.Gravity
import android.widget.HorizontalScrollView
import android.widget.TextView
import com.ppx.kotlin.utils.inject.notifyAny
import com.ppx.kotlin.utils.inject.radio
import org.jetbrains.anko.*

class PointView : HorizontalScrollView {
    private var strs = mutableListOf("推荐", "新品", "热卖", "蛤蛤")
    private var position=0
    constructor(context: Context?) : super(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initView()
    }
    val paint by lazy{
        Paint().apply {
            style=Paint.Style.FILL
        }
    }
    fun initView() {
        strs.forEach {
            textView(it).apply {
                gravity = Gravity.CENTER
                textColor = Color.BLACK
                textSize = dip(24).toFloat()
                layoutParams=MarginLayoutParams(wrapContent, wrapContent).apply {
                    leftMargin=dip(30)
                    rightMargin=dip(30)
                    gravity=Gravity.CENTER_VERTICAL
                }
            }.radio({
                (this as TextView).apply {
                    paint.isFakeBoldText = false
                }
            }, {
                (this as TextView).apply {
                    paint.isFakeBoldText = true

                }
            })
        }
        notifyAny<Int>{
            position=it
        }
        notifyAny(0)
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        forEachChild {
//            canvas?.drawLine()
        }
    }
}