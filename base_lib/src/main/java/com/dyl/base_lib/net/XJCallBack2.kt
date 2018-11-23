package com.dyl.base_lib.net

import com.dyl.base_lib.base.BApplication
import com.dyl.base_lib.event.sendEvent
import com.dyl.base_lib.model.BModel
import com.dyl.base_lib.model.NetEventModel2
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

/**
 * Created by dengyulin on 2018/3/31.
 */
abstract class XJCallBack2<T : BModel> : Callback<T> {
    override fun onFailure(call: Call<T>?, t: Throwable?) {
        onFail(t?.message, t?.toString())
        BApplication.currentActivity?.dissmissDialog()
    }

    override fun onResponse(call: Call<T>?, response: Response<T>) {
        if (response.code() != 200) {
            sendEvent(NetEventModel2(response.code().toString(), BModel(), { code, msg->
                when (code) {
                    NetEventModel2.NET_RECONNETCTION -> {
                        call?.clone()?.enqueue(this)
                    }
                    NetEventModel2.NET_FAIL -> onFail(response.code().toString(), response.errorBody()?.string())
                }
            }))
            return
        }
        val body = response?.body()!!
        sendEvent(NetEventModel2(response.code().toString(), body, { code, msg ->
            when (code) {
                NetEventModel2.NET_RECONNETCTION -> call!!.enqueue(this)
                NetEventModel2.NET_FAIL -> onFail(response.code().toString(), msg)
                NetEventModel2.NET_SUCCESS -> onSuccess(body)
                else -> onFail(response.code().toString(), msg)
            }
        }))

    }
    abstract fun onSuccess(t: T)

    abstract fun onFail(errorCode: String?, errorInfo: String?)

}