package com.dyl.base_lib.view

import android.content.Context
import android.graphics.drawable.Drawable
import android.os.Build
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes

fun Context.getAColor(@ColorRes color:Int): Int {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getColor(color,null)
    }else{
        resources.getColor(color)
    }
}

fun Context.getADrawable(@DrawableRes color:Int): Drawable? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
        resources.getDrawable(color,null)
    }else{
        resources.getDrawable(color)
    }
}