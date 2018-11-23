package com.ppx.kotlin.utils.inject

import android.content.Context
import android.support.v4.widget.NestedScrollView
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import android.view.ViewParent
import android.widget.ListView
import android.widget.ScrollView
import com.dyl.base_lib.model.NTFModel
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.channels.NULL_VALUE
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.forEachChild
import org.jetbrains.anko.sdk25.coroutines.onClick
import top.zibin.luban.Luban
import top.zibin.luban.OnCompressListener
import java.io.File
import java.io.Serializable

/**
 * Created by dengyulin on 2017/6/19.
 */


fun ViewGroup.replace(view: View) {
    if (indexOfChild(view) == -1) {
        addView(view)
    }
}

fun ViewGroup.removeOthers(view: View) {
    (0 until childCount)
            .map { getChildAt(it) }
            .filter { it != view }
            .forEach { removeView(it) }
}

fun ViewGroup.removeOthers(index: Int) {
    (0 until childCount)
            .filter { it != index }
            .forEach { removeViewAt(it) }
}

fun View.notify(call: View.() -> Unit) {
    call.invoke(this)
    setTag(notifyTag, NTFModel(0, call, if (getTag(notifyTag) is NTFModel) (getTag(notifyTag) as NTFModel).parameter else NULL_VALUE))
}

fun View.parameter(call: View.() -> Unit) {
    setTag(notifyTag, NTFModel(0, if (getTag(notifyTag) is NTFModel) (getTag(notifyTag) as NTFModel).notify else NULL_VALUE, call))
}

fun <U : Serializable> View.notifyData(call: View.(U) -> Unit) {
    setTag(notifyTag, NTFModel(1, call, if (getTag(notifyTag) is NTFModel) (getTag(notifyTag) as NTFModel).parameter else NULL_VALUE))
}

fun <U : Any> View.notifyAny(call: View.(U) -> Unit) {
    setTag(notifyTag, NTFModel(2, call, if (getTag(notifyTag) is NTFModel) (getTag(notifyTag) as NTFModel).parameter else NULL_VALUE))
}

tailrec fun View.notify() {
    val tag = getTag(notifyTag)
    if (tag != null && tag is NTFModel) {
        if (tag.key == 0) {
            try {
                (tag.notify as View.() -> Unit).invoke(this)
            } catch (e: Exception) {

            }
        }
    }
    (this as? ViewGroup)?.forEachChild {
        it.notify()
    }
}

val View.notifyTag
    get() = com.dyl.base_lib.R.id.tag_notify


tailrec fun View.parameter() {
    val tag = getTag(notifyTag)
    (this as? ViewGroup)?.forEachChild {
        it.parameter()
    }
    if (tag != null && tag is NTFModel) {
        try {
            (tag.parameter as View.() -> Unit).invoke(this)
        } catch (e: Exception) {

        }
    }
}


tailrec fun <T : Serializable> View.notifyData(t: T) {
    val tag = getTag(notifyTag)
    if (tag != null && tag is NTFModel) {
        if (tag.key == 1) {
            try {
                (tag.notify as View.(T) -> Unit).invoke(this, t)
            } catch (e: Exception) {

            }
        }
    }
    (this as? ViewGroup)?.forEachChild {
        it.notifyData(t)
    }
}

tailrec fun <T : Serializable> View.notifyData(index:Int,t: T) {
    val tag = getTag(notifyTag)
    if (tag != null && tag is NTFModel) {
        if (tag.key == 3) {
            try {
                var handle=(tag.notify as suspend CoroutineScope.(index:Int, data:T)->Unit)?:return
                launch(UI){
                    handle(index,t)
                }
            } catch (e: Exception) {

            }
        }
    }
//    (this as? ViewGroup)?.forEachChild {
//        it.notifyData(index,t)
//    }
}

tailrec fun <T : Any> View.notifyAny(t: T) {
    val tag = getTag(notifyTag)
    if (tag != null && tag is NTFModel) {
        if (tag.key == 2) {
            try {
                (tag.notify as View.(T) -> Unit).invoke(this, t)
            } catch (e: Exception) {

            }
        }
    }
    (this as? ViewGroup)?.forEachChild {
        it.notifyAny(t)
    }
}

tailrec fun ViewParent.intercept(_intercept: Boolean) {
    if (this is ScrollView || this is RecyclerView || this is ListView || this is NestedScrollView) {
        requestDisallowInterceptTouchEvent(_intercept)
    } else {
        if (parent != null) {
            parent.intercept(_intercept)
        }
    }
}

fun Context.inflate(call: () -> Int): View {
    return View.inflate(this, call.invoke(), null)
}

fun Context.luban(file: File, call: (File?) -> Unit) {
    Luban.with(this).load(file).setCompressListener(object : OnCompressListener {
        override fun onSuccess(file: File?) {
            call.invoke(file)
        }

        override fun onError(e: Throwable?) {
            call.invoke(null)
        }

        override fun onStart() {
        }

    }).launch()
}

fun View.radio(normal: View.() -> Unit = {},selected: View.() -> Unit={}): View {
    notifyAny<Int> {
        if (parent is ViewGroup) {
            val i = (parent as ViewGroup).indexOfChild(this@radio)
            if (it == i) {
                selected.invoke(this@radio)
            } else {
                normal.invoke(this@radio)
            }
            onClick {
                (parent as ViewGroup).notifyAny(i)
            }
        } else {
            kotlin.error("")
        }
    }
    return this
}
