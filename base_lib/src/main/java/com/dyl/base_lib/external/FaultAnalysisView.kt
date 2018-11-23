package com.chexiang.external

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.MotionEvent
import android.widget.ImageView
import com.ppx.kotlin.utils.inject.intercept
import org.jetbrains.anko.dip


/**
 * Created by dengyulin on 2017/6/28.
 */
class FaultAnalysisView:ImageView{
    var paintColor=0xFF333333.toInt()
    var canPaint =false


    private var lastX=0f
    private var lastY=0f
    private val paths by lazy {
        mutableListOf<Pair<Int,Path>>()
    }
    private val paint by lazy {
        Paint().apply {
            style=Paint.Style.STROKE
            strokeWidth= dip(5).toFloat()
            strokeCap=Paint.Cap.ROUND
            strokeJoin=Paint.Join.ROUND
            color=paintColor
            isDither=true
            isAntiAlias=true
        }
    }

    constructor(context: Context?) : this(context,null)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs){

    }
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        if(canPaint) {
            parent.intercept(true)
            when (event!!.action) {
                MotionEvent.ACTION_DOWN -> {
                    paths.add(Pair(paintColor, Path().apply {
                        moveTo(event.x, event.y)
                    }))
                    invalidate()
                }

                MotionEvent.ACTION_MOVE -> {
                    paths.last().second.quadTo(lastX, lastY, event.x, event.y)
                    invalidate()
                }
                MotionEvent.ACTION_UP, MotionEvent.ACTION_CANCEL -> {
                    parent.intercept(false)
                }
            }
            lastX = event.x
            lastY = event.y
            return true
        }else{
            return false
        }
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        paths.forEach {
            paint.color=it.first
            canvas!!.drawPath(it.second,paint)
        }
    }
    fun removeLast(){
        if(paths.size>0) {
            paths.removeAt(paths.lastIndex)
            invalidate()
        }
    }
    fun reset(){
        paths.clear()
        invalidate()
    }
    fun createSnapshoot(): Bitmap? {
        var bitmap: Bitmap? = null
        try {
            val width = width
            val height = height
            if (width != 0 && height != 0) {
                bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
                val canvas = Canvas(bitmap)
                draw(canvas)
            }
        } catch (e: Exception) {
            bitmap = null
            e.stackTrace
        }

        return bitmap
    }
}