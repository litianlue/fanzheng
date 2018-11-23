package com.dyl.base_lib.external

import android.animation.ValueAnimator
import android.content.Context
import android.util.AttributeSet
import android.view.animation.LinearInterpolator
import android.widget.ImageView

/**
 * Created by dengyulin on 2018/6/11.
 */
class LoadView : ImageView {
    var anim:ValueAnimator?=null
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onFinishInflate() {
        super.onFinishInflate()
        anim=ValueAnimator.ofFloat(0f,360f).apply {
            duration = 1500
            repeatCount = ValueAnimator.INFINITE
            repeatMode = ValueAnimator.RESTART
            interpolator =  LinearInterpolator()
            addUpdateListener { value->
               rotation=value.animatedValue as Float
            }
        }
    }

}