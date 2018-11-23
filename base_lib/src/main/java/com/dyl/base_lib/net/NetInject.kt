package com.dyl.base_lib.net

import com.dyl.base_lib.BuildConfig
import com.dyl.base_lib.base.BApplication
import com.dyl.base_lib.base.NET
import com.dyl.base_lib.event.Next
import com.dyl.base_lib.event.register
import com.dyl.base_lib.event.sendEvent
import com.dyl.base_lib.event.unRegister
import com.dyl.base_lib.model.RepModel
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.greenrobot.eventbus.Subscribe
import retrofit2.Call
import retrofit2.Callback
import java.util.*

/**
 * Created by dengyulin on 2018/3/31.
 */

fun <T : RepModel> Call<T>.to(s: CallBack<T>.() -> Unit) {
    launch(UI){BApplication.currentActivity?.showDialog()}
    val call = CallBack<T>()
    call.s()
    enqueue(call)
}
fun <T : RepModel> Call<T>.backTo(s: CallBack<T>.() -> Unit) {
    val call = CallBack<T>()
    call.s()
    enqueue(call)
}
fun <T : RepModel> Call<T>.toNext(s: CallBack2<T>.() -> Unit) {
    val call = CallBack2<T>()
    call.s()
    enqueue(call)
}

fun <T : RepModel> Call<T>.call(s: T.() -> Unit) {
    BApplication.currentActivity?.showDialog()
    back(s)
}
fun <T : RepModel> Call<T>.back(s: T.() -> Unit) {
    enqueue(object : XJCallBack<T>() {
        override fun onSuccess(t: T) {
            s.invoke(t)
            BApplication.currentActivity?.dissmissDialog()
        }

        override fun onFail(errorCode: String?, errorInfo: String?) {
            BApplication.currentActivity?.dissmissDialog()
        }

    })
}


fun <T> Call<T>.call(call:Callback<T>) {
    enqueue(call)
}



fun <T : RepModel> Call<T>.callNext(s: T.() -> Unit) {

    enqueue(object : XJCallBack<T>() {
        override fun onSuccess(t: T) {
            s.invoke(t)
            sendEvent(Next(t))
        }

        override fun onFail(errorCode: String?, errorInfo: String?) {
        }

    })
}


class CallBack2<T : RepModel> : XJCallBack<T>() {

    override fun onSuccess(result: T) {
        val handler = _onSuccess ?: return
        launch(UI) {
            handler(result)
            sendEvent(Next(result))
        }
    }

    var _onSuccess: (suspend CoroutineScope.(T) -> Unit)? = null
    fun onSuccess(result: suspend CoroutineScope.(T) -> Unit) {
        _onSuccess = result

    }

    override fun onFail(errorCode: String?, errorInfo: String?) {
        val handler = _onFail ?: return
        launch(UI) {
            handler(errorCode, errorInfo)
        }

    }

    var _onFail: (suspend CoroutineScope.(String?, String?) -> Unit)? = null
    fun onFail(result: suspend CoroutineScope.(errorCode: String?, errorInfo: String?) -> Unit) {
        _onFail = result
    }
}

class CallBack<T : RepModel> : XJCallBack<T>() {

    override fun onSuccess(result: T) {
        val handler = _onSuccess ?: return
        launch(UI) {
            handler(result)
            BApplication.currentActivity?.dissmissDialog()
        }
    }

    var _onSuccess: (suspend CoroutineScope.(T) -> Unit)? = null
    fun onSuccess(result: suspend CoroutineScope.(T) -> Unit) {
        _onSuccess = result
    }

    override fun onFail(errorCode: String?, errorInfo: String?) {
        val handler = _onFail ?: return
        launch(UI) {
            handler(errorCode, errorInfo)
            BApplication.currentActivity?.dissmissDialog()
        }

    }

    var _onFail: (suspend CoroutineScope.(String?, String?) -> Unit)? = null
    fun onFail(result: suspend CoroutineScope.(errorCode: String?, errorInfo: String?) -> Unit) {
        _onFail = result
    }
}

class NetQueue<T>(val baseClass: Class<T>) {
    init {
        register()
    }

    val service by lazy {
        NET.create(baseClass)!!
    }

    val list: LinkedList<T.(next: Next) -> Unit> = LinkedList()
    @Subscribe
    fun onEvent(next: Next) {
        if (list.isNotEmpty()) {
            list.pop().invoke(service, next)
        } else {
            unRegister()
            BApplication.currentActivity?.dissmissDialog()
        }
    }
}

fun <P, T> P.load(t: Class<T>): T {
    return NET.newBuilder().baseUrl(t.getAnnotation(ApiPath::class.java)?.value
            ?: BuildConfig.HOSTPATH).build().create(t)!!
}

var lastNetQueue:NetQueue<*>?=null
fun <P, T> P.netQueue(t: Class<T>, vararg call: T.(next: Next) -> Unit) {
    BApplication.currentActivity?.showDialog()
    lastNetQueue?.apply { unRegister() }
    lastNetQueue= NetQueue(t).apply {
        list.addAll(call)
        onEvent(Next(RepModel()))
    }

}