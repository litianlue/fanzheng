package com.dyl.base_lib.external

import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.util.AttributeSet
import android.view.View

/**
 * Created by dengyulin on 2018/5/26.
 */  
class BigScrollView:NestedScrollView {
    constructor(context: Context) : super(context)
    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)

    override fun onStartNestedScroll(child: View, target: View, nestedScrollAxes: Int): Boolean {
        return false
    }
}