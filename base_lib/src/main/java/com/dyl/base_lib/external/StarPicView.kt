package com.dyl.base_lib.external

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.PointF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.dyl.base_lib.R
import com.dyl.base_lib.img.getBitmap
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.dip

class StarPicView : View {
    var starCount = 5
    var starSelect = 5
    var size = dip(45)
    var space = dip(10)
    private val starArray = mutableListOf<PointF>()
    var bitmap: Bitmap? = null
        set(value) {
            field=value
            postInvalidate()
        }
    var bitmapBG: Bitmap? = null
        set(value) {
            field=value
            postInvalidate()
        }
    var call:(index:Int)->Unit={}
    constructor(context: Context?) : super(context,null,0)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs,0)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    override fun onFinishInflate() {
        super.onFinishInflate()
        launch(CommonPool){
            bitmap=Bitmap.createScaledBitmap(this@StarPicView.context.getBitmap(R.drawable.wjx),size,size,true)
            bitmapBG=Bitmap.createScaledBitmap(this@StarPicView.context.getBitmap(R.drawable.wjx_n),size,size,true)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (!isEnabled) return super.onTouchEvent(event)
        when (event.action) {
            MotionEvent.ACTION_MOVE or MotionEvent.ACTION_DOWN -> {
                starSelect= Math.ceil(event.x/(size+space).toDouble()).toInt()
                call.invoke(starSelect)
                invalidate()
            }
        }
        parent.requestDisallowInterceptTouchEvent(true)
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
            starArray.add(PointF((paddingLeft+(size+space)*i).toFloat(),0f))
        }

    }


    override fun dispatchDraw(canvas: Canvas) {
        if(bitmap==null||bitmapBG==null)return
        starArray.forEachIndexed { index, pointF ->
            if(index<starSelect){
                canvas.drawBitmap(bitmap,pointF.x,pointF.y,null)
            }else{
                canvas.drawBitmap(bitmapBG,pointF.x,pointF.y,null)
            }

        }
    }
}