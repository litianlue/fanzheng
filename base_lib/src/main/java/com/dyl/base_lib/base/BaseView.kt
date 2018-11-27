package com.dyl.base_lib.base

import android.content.Context
import android.content.Intent
import android.os.Looper
import android.support.annotation.LayoutRes
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dyl.base_lib.R
import com.dyl.base_lib.data.cache.Cache
import com.dyl.base_lib.util.NetworkUtil
import com.dyl.base_lib.view.inflateView
import com.dyl.base_lib.view.show
import com.ppx.kotlin.utils.inject.inflate
import org.jetbrains.anko.sdk25.coroutines.onAttachStateChangeListener

/**
 * Created by dengyulin on 2018/5/3.
 */
abstract class BaseView {
    var v: View? = null
    var context: Context
    private var keydown: ((Int, KeyEvent) -> Boolean)? = null

    constructor(context: Context) {
        this.context = context
        Looper.myQueue().addIdleHandler {
            if (v == null) {
                onCreate()
            }
            return@addIdleHandler false
        }
    }

    fun onCreate() {
        initView()
        initData()
    }

    open fun initContentView(@LayoutRes id: Int, call: View.() -> Unit = {}) {

        v = context.inflateView(id)

        v!!.onAttachStateChangeListener {
            onViewAttachedToWindow {
                onShow()
            }
            onViewDetachedFromWindow {
                keydown?.apply { (context as BaseActivity).removeOnKeyDown(this) }
                onDismiss()
            }
        }
        call.invoke(v!!)

        (context as BaseActivity)
    }

    open fun initContentView(cv: View, call: View.() -> Unit = {}) {
        v = cv
        v!!.onAttachStateChangeListener {
            onViewAttachedToWindow {
                onShow()
            }
            onViewDetachedFromWindow {
                keydown?.apply { (context as BaseActivity).removeOnKeyDown(this) }
                onDismiss()
            }
        }
        call.invoke(v!!)
        (context as BaseActivity)
    }

    abstract fun initData()

    abstract fun initView()

    open fun onShow() {}

    open fun onDestroy() {}

    open fun onDismiss() {}

    open fun onKeyDown(event: (keyCode: Int, keyEvent: KeyEvent) -> Boolean) {
        keydown = event
        (context as BaseActivity).onKeyDown(keydown!!)
    }

    open fun getView(): View {
        v ?: onCreate()
        return v ?: throw NotImplementedError("没有调用initContentView")
    }

    fun clear() {
        onDestroy()
        v = null
    }

    inline fun <reified T : BaseActivity> startActivity() {
        startActivity<T>(null)
    }

    inline fun <reified T : BaseActivity> startActivity(p: Any?) {
        if (p != null) {
            Cache.putCache("${this::class.java.name}to${T::class.java.name}-data", p)
        }
        context.startActivity(Intent(context, T::class.java))
    }

    inline fun <reified T, P> getData(): P =
            Cache.getCache("${T::class.java.name}to${(context as BaseActivity)::class.java.name}-data")

    inline fun <reified T, P> popData(): P? =
            Cache.popCache("${T::class.java.name}to${(context as BaseActivity)::class.java.name}-data")

    inline fun <reified T> putData(p: Any?) {
        if (p != null) {
            Cache.putCache("${this::class.java.name}to${T::class.java.name}-data", p)
        }
    }

}

fun ViewGroup.addView(baseView: BaseView) {
    addView(baseView.getView())
}

fun ViewGroup.removeView(baseView: BaseView) {
    removeView(baseView.getView())
}
