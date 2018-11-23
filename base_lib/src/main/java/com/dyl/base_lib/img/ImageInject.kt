package com.dyl.base_lib.img

import android.content.Context
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.dyl.base_lib.BuildConfig
import com.dyl.base_lib.R

/**
 * Created by dengyulin on 2018/3/31.
 */
fun <T : View, P> T.loadImage(path: P, placeholders: Int = -1, error: Int = -1) {
    Glide.with(this.context.applicationContext).load(path).transition(GenericTransitionOptions.with(R.anim.fade_in)).apply(RequestOptions().placeholder(placeholders).error(error)).into(this as ImageView)
}

fun <T : View, P> T.loadImageRes(path: P, placeholders: Int = -1, error: Int = -1) {
    Glide.with(this.context.applicationContext).load(BuildConfig.RESPATH + path).transition(GenericTransitionOptions.with(R.anim.fade_in)).apply(RequestOptions().placeholder(placeholders).error(error)).into(this as ImageView)
}

fun <T : Context, P> T.getBitmap(path: P) =
        Glide.with(this.applicationContext).asBitmap().load(path).submit().get()
fun <T : Context, P> T.getBitmap(path: P,w:Int,h:Int) =
        Glide.with(this.applicationContext).asBitmap().load( path).apply(RequestOptions().override(w,h)).submit().get()

fun <T : Context, P> T.getBitmapRes(path: P,w:Int,h:Int) =
        Glide.with(this.applicationContext).asBitmap().load(BuildConfig.RESPATH + path).apply(RequestOptions().override(w,h)).submit().get()
