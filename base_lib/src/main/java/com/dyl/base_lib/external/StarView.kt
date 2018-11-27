package com.dyl.base_lib.external

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.dyl.base_lib.util.to2Double
import org.jetbrains.anko.dip

class StarView : View {
    var starCount = 5
    var size = dip(40)
    var space = dip(10)
    var iszs = true
    private val paint by lazy {
        Paint().apply {
            isAntiAlias = true
            isDither = true
            style= Paint.Style.FILL
            color = Color.parseColor("#858585")
        }
    }
    private var moveX = 0f
    private val starArray = mutableListOf<Array<PointF>>()
    var selectStarCount = 0.0
        set(value) {
            field = value
            moveX = (value.toInt() * (size + space) + (value % value.toInt().toDouble() * size) + paddingLeft).toFloat()
        }
        get() {
            val siz = ((moveX - paddingLeft) / (size + space)).toInt()
            return (siz + (moveX - paddingLeft - siz * (size + space)) / size).toDouble().to2Double().toDouble()
        }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)


    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return super.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_MOVE or MotionEvent.ACTION_DOWN -> {
                if (iszs) {
                    val sp = event.x - paddingLeft
                    moveX = ((sp / (size + space)).toInt() * (size + space) + if (sp % (size + space) > 0) size else 0).toFloat()
                } else {
                    moveX = event.x
                }

                invalidate()
            }
        }
        return true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val model = MeasureSpec.getMode(widthMeasureSpec)
        val widths = when (model) {
            MeasureSpec.AT_MOST -> (starCount * (space + size) - space) + paddingLeft + paddingRight
            MeasureSpec.EXACTLY -> getDefaultSize(suggestedMinimumHeight, widthMeasureSpec)
            MeasureSpec.UNSPECIFIED -> (starCount * space * size - space) + paddingLeft + paddingRight
            else -> 0
        }
        setMeasuredDimension(widths, size + paddingTop + paddingBottom)
        starArray.clear()
        for (i in 0 until starCount) {
            // 左半颗 + （整星 + 边距） * 颗数
            starArray.add(fivePoints(((paddingLeft + size / 2) + (size + space) * i).toFloat(), paddingTop.toFloat(), size))

        }

    }

     fun fivePoints(xA: Float, yA: Float, rFive: Int): Array<PointF> {
        val pb = PointF(0F, 0F)
        val pc = PointF(0F, 0F)
        var pd = PointF(0F, 0F)
        var pe = PointF(0F, 0F)
        pd.x = (xA - rFive * Math.sin(Math.toRadians(18.0))).toFloat()
        pc.x = (xA + rFive * Math.sin(Math.toRadians(18.0))).toFloat()
        pc.y = (yA + Math.cos(Math.toRadians(18.0)) * rFive).toFloat()
        pd.y = pc.y
        pe.y = (yA + Math.sqrt(Math.pow((pc.x - pd.x).toDouble(), 2.0) - Math.pow((rFive / 2).toDouble(), 2.0))).toFloat()
        pb.y = pe.y
        pb.x = xA + rFive / 2
        pe.x = xA - rFive / 2
        return arrayOf(PointF(xA, yA), pd, pb, pe, pc, PointF(xA, yA))
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val path = Path()
        starArray.forEach {
            it.forEach {
                path.lineTo(it.x, it.y)
            }
        }
        val sc = canvas.saveLayerAlpha(0F, 0F, width.toFloat(), height.toFloat(), 255,
                Canvas.ALL_SAVE_FLAG)
        paint.color = Color.parseColor("#858585")
        canvas.drawPath(path, paint)
        canvas.saveLayerAlpha(0F, 0F, width.toFloat(), height.toFloat(), 255,
                Canvas.ALL_SAVE_FLAG)
        paint.color = Color.WHITE
        paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        paint.color = Color.parseColor("#ea7b7b")
        canvas.drawRect(left.toFloat(), top.toFloat(), moveX, bottom.toFloat(), paint)
        paint.xfermode = null
        canvas.restoreToCount(sc)
    }
}