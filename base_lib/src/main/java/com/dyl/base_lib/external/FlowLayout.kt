package com.chexiang.external

import android.content.Context
import android.graphics.Rect
import android.util.AttributeSet
import android.view.ViewGroup
import android.view.ViewManager
import org.jetbrains.anko.custom.ankoView
import org.jetbrains.anko.forEachChild
import org.jetbrains.anko.forEachChildWithIndex

/**
 * Created by dengyulin on 2017/7/3.
 */
class FlowLayout : ViewGroup {
    var lineCount = 3
    var itemWidth = 0
    var itemHeight = 0
    val pointList by lazy {
        mutableListOf<Rect>()
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) = if (lineCount > 0) {

        forEachChildWithIndex { i, view ->
            var startTop = i / lineCount * itemHeight+paddingTop
            view.layout(itemWidth * (i % lineCount)+paddingLeft, startTop, itemWidth * (i % lineCount + 1)+paddingLeft, startTop + itemHeight)
        }
    } else {
        pointList.forEachIndexed { index, rect ->
            getChildAt(index).layout(rect.left+paddingLeft, rect.top+paddingTop, rect.right+paddingLeft, rect.bottom+paddingTop)
        }
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = MeasureSpec.getSize(widthMeasureSpec)-paddingLeft-paddingRight
        if (lineCount > 0) {
            itemWidth = size / lineCount
            var totalHeight = 0
            if (childCount > 0) {
                measureChildren(MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY), heightMeasureSpec)
                itemHeight = getChildAt(0).measuredHeight
                totalHeight = ((childCount - 1) / lineCount + 1) * itemHeight
            }
            setMeasuredDimension(size+paddingLeft+paddingRight, totalHeight+paddingTop+paddingBottom)
        } else {
            measureChildren(widthMeasureSpec, heightMeasureSpec)
            var startX = 0
            var startY = 0
            var maxHeight = 0
            pointList.clear()
            forEachChild {
                val w = it.measuredWidth
                val h = it.measuredHeight
                if (maxHeight < h) maxHeight = h
                if (startX + w > size) {
                    startY += maxHeight
                    maxHeight = 0
                    startX = 0
                }
                pointList.add(Rect(startX, startY, startX + w, startY + h))
                startX += w

            }
            setMeasuredDimension(size+paddingLeft+paddingRight, startY + maxHeight+paddingTop+paddingBottom)
        }


    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.flowLayout(theme: Int = 0) = flowLayout(theme) {}

inline fun ViewManager.flowLayout(theme: Int = 0, init: FlowLayout.() -> Unit): FlowLayout {
    return ankoView({ FlowLayout(it) }, theme, init)
}
