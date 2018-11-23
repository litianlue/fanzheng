package com.dyl.base_lib.external

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.util.AttributeSet
import org.jetbrains.anko.dip

class MenuRecyclerView : RecyclerView {
    var scrollColor = Color.BLACK
    var scrollColorBg = Color.GRAY
    var isMenu = false
    val paint by lazy {
        Paint().apply {
            color=scrollColor
            isAntiAlias=true
            isDither=true
            strokeWidth=dip(5).toFloat()
            style= Paint.Style.FILL
        }
    }

    constructor(context: Context?) : super(context, null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs, 0)
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(context, attrs, defStyle) {
        isHorizontalScrollBarEnabled = false
        isVerticalScrollBarEnabled = false
    }

    override fun onDraw(c: Canvas?) {
        super.onDraw(c)
        if (isMenu) {
            var w=computeHorizontalScrollExtent().toFloat()/computeHorizontalScrollRange().toFloat()*measuredWidth
            if(w>measuredWidth){
                w=measuredWidth.toFloat()
            }
            val x=computeHorizontalScrollOffset()/(computeHorizontalScrollRange().toFloat()-computeHorizontalScrollExtent().toFloat())*(measuredWidth-w)
            paint.color=scrollColorBg
            c?.drawLine(0f, (bottom-dip(5)).toFloat(), right.toFloat(), (bottom-dip(5)).toFloat(),paint)
            paint.color=scrollColor
            c?.drawLine(x, (bottom-dip(5)).toFloat(), x+w, (bottom-dip(5)).toFloat(),paint)

        }else{
            var w=computeHorizontalScrollExtent().toFloat()/computeHorizontalScrollRange().toFloat()*measuredWidth
            if(w>measuredWidth){
                w=measuredWidth.toFloat()
            }
            val x=computeHorizontalScrollOffset()/(computeHorizontalScrollRange().toFloat()-computeHorizontalScrollExtent().toFloat())*(measuredWidth-w)
            paint.color=scrollColorBg
            c?.drawLine(0f, (bottom-dip(5)).toFloat(), right.toFloat(), (bottom-dip(5)).toFloat(),paint)
            paint.color=scrollColor
            c?.drawLine(x, (bottom-dip(5)).toFloat(), x+w, (bottom-dip(5)).toFloat(),paint)


        }
    }
}