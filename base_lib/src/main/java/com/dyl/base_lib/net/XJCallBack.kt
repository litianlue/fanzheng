package com.dyl.base_lib.net

import com.dyl.base_lib.base.BApplication
import com.dyl.base_lib.event.sendEvent
import com.dyl.base_lib.model.NetEventModel
import com.dyl.base_lib.model.RepModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


/**
 * Created by dengyulin on 2018/3/31.
 */
abstract class XJCallBack<T : RepModel> : Callback<T> {
    override fun onFailure(call: Call<T>?, t: Throwable?) {
        onFail(t?.message, t.toString())
        BApplication.currentActivity?.dissmissDialog()
    }

    override fun onResponse(call: Call<T>?, response: Response<T>) {
        if (response.code() != 200) {
            sendEvent(NetEventModel(response.code().toString(), response, {
                when (it) {
                    0 -> {
                        call?.clone()?.enqueue(this)
                    }
                    1 -> onFail(response.code().toString(), response.errorBody()?.string())
                }
            }))
            return
        }
        val body = response?.body()!!


        if (body.ErrorCode == null || body.ErrorCode.isBlank() || body.ErrorCode == "0") {
            onSuccess(body)
        } else {
            sendEvent(NetEventModel(body.ErrorCode, body, {
                when (it) {
                    0 -> call!!.enqueue(this)
                    1 -> onFail(body.ErrorCode, body.Message)
                }
            }))

        }
    }

    abstract fun onSuccess(t: T)

    abstract fun onFail(errorCode: String?, errorInfo: String?)

}