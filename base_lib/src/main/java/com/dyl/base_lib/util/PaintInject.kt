package com.dyl.base_lib.util

import android.graphics.Paint

fun Paint.measureTextCenterY(y: Float): Float {
    val fm = fontMetrics
    return y + (fm.bottom - fm.top) / 2 - fm.bottom
}

fun Paint.measureTextTopY(y: Float): Float {
    val fm = fontMetrics
    return y - fm.top
}

fun Paint.measureTextHeight(): Float {
    val fm = fontMetrics
    return -fm.ascent + fm.descent
}

fun Paint.measureTextCenterX(size: Int, str: String) = (size - measureText(str)) / 2f

