package com.dyl.base_lib.wx

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Bitmap.CompressFormat
import android.graphics.BitmapFactory
import android.util.Log
import com.dyl.base_lib.BuildConfig
import com.dyl.base_lib.base.BApplication
import com.dyl.base_lib.model.WeiXin
import com.dyl.base_lib.net.call
import com.dyl.base_lib.net.load
import com.dyl.base_lib.show.toast.toast
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.modelmsg.SendMessageToWX
import com.tencent.mm.opensdk.modelmsg.WXMediaMessage
import com.tencent.mm.opensdk.modelmsg.WXWebpageObject
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import java.io.ByteArrayOutputStream


fun Application.initWx() {
    BApplication.wxApi = WXAPIFactory.createWXAPI(this, BuildConfig.WXCODE, true)
    BApplication.wxApi?.registerApp(BuildConfig.WXCODE)
}

fun Context.shareWXSceneSession(url: String, title: String, desc: String, id: Int = 0, bitmap: Bitmap? = null) {
    if (id != 0) {
        share(url, title, desc, BitmapFactory.decodeResource(resources, id), SendMessageToWX.Req.WXSceneSession)
    } else if (bitmap != null) {
        share(url, title, desc, bitmap!!, SendMessageToWX.Req.WXSceneSession)
    }

}

private fun share(url: String = "", title: String = "", desc: String = "", bmp: Bitmap, scenes: Int = SendMessageToWX.Req.WXSceneSession) {
    BApplication.wxApi?.sendReq(SendMessageToWX.Req().apply {
        transaction = "webpage${System.currentTimeMillis()}"
        message = WXMediaMessage(WXWebpageObject().apply { webpageUrl = url }).apply {
            this.title = title
            description = desc
            thumbData = bmpToByteArray(bmp, true)
        }
        scene = scenes
    })
}

fun Context.shareWXSceneTimeline(url: String, title: String, desc: String, id: Int = 0, bitmap: Bitmap? = null) {
    if (id != 0) {
        share(url, title, desc, BitmapFactory.decodeResource(resources, id), SendMessageToWX.Req.WXSceneTimeline)
    } else if (bitmap != null) {
        share(url, title, desc, bitmap!!, SendMessageToWX.Req.WXSceneTimeline)
    }
}

private fun bmpToByteArray(bmp: Bitmap, needRecycle: Boolean): ByteArray {
    val output = ByteArrayOutputStream()
    bmp.compress(CompressFormat.PNG, 100, output)
    val result = output.toByteArray()
    try {
        output.close()
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return result
}

fun Context.loginWx(scope: String = "snsapi_userinfo", state: String = "wechat_sdk_xb_live_state123") {
    if (BApplication.wxApi?.isWXAppInstalled == false) {
        toast("您手机尚未安装微信，请安装后再登录")
        return
    }
    BApplication.wxApi?.sendReq(SendAuth.Req().apply {
        this.scope = scope
        this.state = state

    })

}

fun Context.payWx(req: WeiXin.WXPayModel) {
    val request = PayReq()
    request.appId = req.appId
    request.partnerId = req.partnerId
    request.prepayId = req.prepayId
    request.packageValue = "Sign=WXPay"
    request.nonceStr = req.nonceStr
    request.timeStamp = req.timeStamp
    request.sign = req.sign
    BApplication.wxApi?.sendReq(request)
}