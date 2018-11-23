package com.dyl.base_lib.alipay

import android.app.Activity
import android.content.Context
import android.text.TextUtils
import com.alipay.sdk.app.PayTask
import com.dyl.base_lib.model.PayResult
import com.dyl.base_lib.show.toast.toast
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch

fun Context.payAli(param:String,call:(code:String)->Unit){
    launch(CommonPool) {
        val alipay = PayTask(this@payAli as Activity)
        val result = alipay.payV2(param, true)
        launch(UI) {
            val payResult = PayResult(result)
            /**
             * 对于支付结果，请商户依赖服务端的异步通知结果。同步通知结果，仅作为支付结束的通知。
             */
            val resultInfo = payResult.getResult()// 同步返回需要验证的信息
            val resultStatus = payResult.getResultStatus()
            // 判断resultStatus 为9000则代表支付成功
            if (TextUtils.equals(resultStatus, "9000")) {
                // 该笔订单是否真实支付成功，需要依赖服务端的异步通知。
                this@payAli.toast("支付成功")
                call.invoke("0")
            } else {
                // 该笔订单真实的支付结果，需要依赖服务端的异步通知。
                this@payAli.toast("支付失败")
            }
        }
    }.start()
}