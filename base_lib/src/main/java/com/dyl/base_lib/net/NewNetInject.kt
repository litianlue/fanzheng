package com.dyl.base_lib.net

import com.dyl.base_lib.base.BApplication
import com.dyl.base_lib.event.Next
import com.dyl.base_lib.event.sendEvent
import com.dyl.base_lib.model.BModel
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import retrofit2.Call

/**
 * Created by dengyulin on 2018/3/31.
 */

fun <T : BModel> Call<T>.to(s: CallBack4<T>.() -> Unit) {
    launch(UI) { BApplication.currentActivity?.showDialog() }
    val call = CallBack4<T>()
    call.s()
    enqueue(call)
}

fun <T : BModel> Call<T>.backTo(s: CallBack4<T>.() -> Unit) {
    val call = CallBack4<T>()
    call.s()
    enqueue(call)
}

fun <T : BModel> Call<T>.toNext(s: CallBack3<T>.() -> Unit) {
    val call = CallBack3<T>()
    call.s()
    enqueue(call)
}

fun <T : BModel> Call<T>.call(s: T.() -> Unit) {
    BApplication.currentActivity?.showDialog()
    back(s)
}

fun <T : BModel> Call<T>.back(s: T.() -> Unit) {
    enqueue(object : XJCallBack2<T>() {
        override fun onSuccess(t: T) {
            s.invoke(t)
            BApplication.currentActivity?.dissmissDialog()
        }

        override fun onFail(errorCode: String?, errorInfo: String?) {
            BApplication.currentActivity?.dissmissDialog()
        }

    })
}

fun <T:BModel> Call<T>.call(call: CallBack4<T>) {
    enqueue(call)
}

fun <T : BModel> Call<T>.callNext(s: T.() -> Unit) {

    enqueue(object : XJCallBack2<T>() {
        override fun onSuccess(t: T) {
            s.invoke(t)
            sendEvent(Next(t))
        }

        override fun onFail(errorCode: String?, errorInfo: String?) {
        }

    })
}

class CallBack3<T : BModel> : XJCallBack2<T>() {

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

class CallBack4<T:BModel> : XJCallBack2<T>() {

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

