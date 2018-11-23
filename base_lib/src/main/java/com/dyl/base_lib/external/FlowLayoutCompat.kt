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
class FlowLayoutCompat : ViewGroup {
    var lineCount = -1
    var itemWidth = 0
    var itemHeight = 0
    val pointList by lazy {
        mutableListOf<Rect>()
    }

    override fun checkLayoutParams(p: LayoutParams?): Boolean {
        return p is MarginLayoutParams
    }

    override fun generateDefaultLayoutParams(): LayoutParams {
        return MarginLayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
    }

    override fun generateLayoutParams(p: LayoutParams?): LayoutParams {
        return MarginLayoutParams(p)
    }

    override fun generateLayoutParams(attrs: AttributeSet?): LayoutParams {
        return MarginLayoutParams(context, attrs)
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) = if (lineCount > 0) {
        forEachChildWithIndex { i, view ->
            var startTop = i / lineCount * itemHeight + paddingTop
            val lp = view.layoutParams as MarginLayoutParams
            view.layout((i % lineCount) * itemWidth + lp.leftMargin + paddingLeft, startTop, (i % lineCount + 1) * itemWidth - lp.rightMargin + paddingLeft, startTop + itemHeight)
        }
    } else {
        pointList.forEachIndexed { index, rect ->
            val view = getChildAt(index)
            view.layout(rect.left + paddingLeft, rect.top + paddingTop, rect.right + paddingLeft, rect.bottom + paddingTop)
        }
    }

    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val size = MeasureSpec.getSize(widthMeasureSpec) - paddingLeft - paddingRight
        val heightSpec=MeasureSpec.makeMeasureSpec(MeasureSpec.getSize(heightMeasureSpec),MeasureSpec.AT_MOST)
        if (lineCount > 0) {
            itemWidth = size / lineCount
            var totalHeight = 0
//            if (childCount > 0) {
//                val line=childCount/lineCount + if(childCount%lineCount==0) 0 else 1
//                itemHeight = getChildAt(0).measuredHeight

//            }
            forEachChild {
                with(it) {
                    var itemWidth = itemWidth
                    val lp = it.layoutParams as MarginLayoutParams
                    itemWidth = itemWidth - lp.leftMargin - lp.rightMargin
                    it.measure(MeasureSpec.makeMeasureSpec(itemWidth, MeasureSpec.EXACTLY), heightSpec)
                    val he = it.measuredHeight
                    if (itemHeight < he) {
                        itemHeight = he
                    }
                    pointList.add(Rect())
                }
            }
            val line=childCount/lineCount + if(childCount%lineCount==0) 0 else 1
            totalHeight = line * itemHeight
            setMeasuredDimension(size + paddingLeft + paddingRight, totalHeight + paddingTop + paddingBottom)
        } else {
            measureChildren(widthMeasureSpec, heightMeasureSpec)
            var startX = 0
            var startY = 0
            var maxHeight = 0
            pointList.clear()
            forEachChild {

                var w = it.measuredWidth
                val h = it.measuredHeight
                val lp = it.layoutParams as MarginLayoutParams
                w += lp.leftMargin + lp.rightMargin
                if (maxHeight < h) maxHeight = h
                if (startX + w > size) {
                    startY += maxHeight
                    maxHeight = 0
                    startX = 0
                }
                pointList.add(Rect(startX + lp.leftMargin, startY, startX + w - lp.rightMargin, startY + h))
                startX += w

            }
            setMeasuredDimension(size + paddingLeft + paddingRight, startY + maxHeight + paddingTop + paddingBottom)
        }


    }
}

@Suppress("NOTHING_TO_INLINE")
inline fun ViewManager.flowLayoutCompat(theme: Int = 0) = flowLayoutCompat(theme) {}

inline fun ViewManager.flowLayoutCompat(theme: Int = 0, init: FlowLayoutCompat.() -> Unit): FlowLayoutCompat {
    return ankoView({ FlowLayoutCompat(it) }, theme, init)
}
