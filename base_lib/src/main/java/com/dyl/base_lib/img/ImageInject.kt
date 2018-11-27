package com.dyl.base_lib.img

import android.content.Context
import android.graphics.Bitmap
import android.view.View
import android.widget.ImageView
import com.bumptech.glide.GenericTransitionOptions
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.target.Target
import com.bumptech.glide.request.transition.Transition
import com.dyl.base_lib.BuildConfig
import com.dyl.base_lib.R

/**
 * Created by dengyulin on 2018/3/31.
 */
fun <T : View, P> T.loadImage(path: P, placeholders: Int = -1, error: Int = -1, radius: Int = 0) {
    Glide.with(this.context.applicationContext).load(path).transition(GenericTransitionOptions.with(R.anim.fade_in)).apply(RequestOptions().placeholder(placeholders).error(error).apply {
        if (radius < 0) {
            transform(GlideCircleTransform())
        } else if (radius > 0) {
            transform(GlideRoundTransform(radius))
        }
    }).into(this as ImageView)
}
fun <T : View> T.loadAutoImage(path: String, placeholders: Int = -1, error: Int = -1, radius: Int = 0) {
    Glide.with(this.context.applicationContext).load(if(path.contains("http")) path else BuildConfig.RESPATH+path).transition(GenericTransitionOptions.with(R.anim.fade_in)).apply(RequestOptions().placeholder(placeholders).error(error).apply {
        if (radius < 0) {
            transform(GlideCircleTransform())
        } else if (radius > 0) {
            transform(GlideRoundTransform(radius))
        }
    }).into(this as ImageView)
}
fun <T : View, P> T.loadImageRes(path: P, placeholders: Int = -1, error: Int = -1, radius: Int = 0) {
    Glide.with(this.context.applicationContext).load(BuildConfig.RESPATH + path).transition(GenericTransitionOptions.with(R.anim.fade_in)).apply(RequestOptions().placeholder(placeholders).error(error).apply {
        if (radius < 0) {
            transform(GlideCircleTransform())
        } else if (radius > 0) {
            transform(GlideRoundTransform(radius))
        }
    }).into(this as ImageView)
}
fun <T : Context, P> T.getBitmap(path: P) =
        Glide.with(this.applicationContext).asBitmap().load(path).submit().get()
fun <T :Context,P> T.getBitmap(path:P,w:Int,h:Int,call:(Bitmap)->Unit){
    Glide.with(this.applicationContext).asBitmap().load(path).apply(RequestOptions().override(w,h)).into(object :SimpleTarget<Bitmap>(){
        override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
            call.invoke(resource)
        }

    })
}
fun <T : Context> T.getAutoBitmap(path: String,w:Int,h:Int) =
        Glide.with(this.applicationContext).asBitmap().load(if(path.contains("http")) path else BuildConfig.RESPATH+path).apply(RequestOptions().override(w,h)).submit().get()

fun <T : Context, P> T.getBitmap(path: P,w:Int,h:Int) =
        Glide.with(this.applicationContext).asBitmap().load( path).apply(RequestOptions().override(w,h)).submit().get()

fun <T : Context, P> T.getBitmapRes(path: P,w:Int,h:Int) =
        Glide.with(this.applicationContext).asBitmap().load(BuildConfig.RESPATH + path).apply(RequestOptions().override(w,h)).submit().get()
fun <T : Context, P> T.getBitmapRes(path: P) =
        Glide.with(this.applicationContext).asBitmap().load(BuildConfig.RESPATH + path).submit().get()
