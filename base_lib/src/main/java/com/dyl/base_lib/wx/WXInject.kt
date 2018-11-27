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
import com.dyl.base_lib.util.isNotNull
import com.dyl.base_lib.util.isNull
import com.tencent.mm.opensdk.modelmsg.*
import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import java.io.ByteArrayOutputStream


fun Application.initWx() {
    BApplication.wxApi = WXAPIFactory.createWXAPI(this, BuildConfig.WXCODE, true)
    BApplication.wxApi?.registerApp(BuildConfig.WXCODE)
}

fun Context.shareWXSceneSession(url: String="", title: String="", desc: String="", id: Int = 0, bitmap: Bitmap? = null) {
    if(url.isNull()&&title.isNull()&&desc.isNotNull()){
        share(desc)
        return
    }
    if (id != 0) {
        val bitmap=BitmapFactory.decodeResource(resources, id)
        if(url.isNull()&&title.isNull()&&desc.isNull()){
            share(bitmap)
            return
        }
        share(url, title, desc,bitmap, SendMessageToWX.Req.WXSceneSession)
    } else if (bitmap != null) {
        if(url.isNull()&&title.isNull()&&desc.isNull()){
            share(bitmap)
            return
        }
        share(url, title, desc, bitmap!!, SendMessageToWX.Req.WXSceneSession)
    }

}

private fun share(url: String = "", title: String = "", desc: String = "", bmp: Bitmap, scenes: Int = SendMessageToWX.Req.WXSceneSession) {
    BApplication.wxApi?.sendReq(SendMessageToWX.Req().apply {
        transaction = "webpage${System.currentTimeMillis()}"
        message = WXMediaMessage(WXWebpageObject().apply { webpageUrl = url }).apply {
            this.title = title
            description = desc
            val bitmap=Bitmap.createScaledBitmap(bmp,80,80,true)
            bmp.recycle()
            thumbData = bmpToByteArray(bitmap, true)
        }
        scene = scenes
    })
}
private fun share(bmp: Bitmap, scenes: Int = SendMessageToWX.Req.WXSceneSession) {
    BApplication.wxApi?.sendReq(SendMessageToWX.Req().apply {
        transaction = "img${System.currentTimeMillis()}"
        message = WXMediaMessage().apply {
            mediaObject= WXImageObject(bmp)
            val bitmap=Bitmap.createScaledBitmap(bmp,80,80,true)
            bmp.recycle()
            thumbData = bmpToByteArray(bitmap, true)
        }
        scene = scenes
    })
}
private fun share(text:String, scenes: Int = SendMessageToWX.Req.WXSceneSession) {
    BApplication.wxApi?.sendReq(SendMessageToWX.Req().apply {
        transaction = "text${System.currentTimeMillis()}"
        message = WXMediaMessage().apply {
            mediaObject=WXTextObject().apply { this.text=text }
            description=text
        }
        scene = scenes
    })
}
fun Context.shareWXSceneTimeline(url: String="", title: String="", desc: String="", id: Int = 0, bitmap: Bitmap? = null) {
    if(url.isNull()&&title.isNull()&&desc.isNotNull()){
        share(desc,SendMessageToWX.Req.WXSceneTimeline)
        return
    }
    if (id != 0) {
        val bmp=BitmapFactory.decodeResource(resources, id)
        if(url.isNull()&&title.isNull()&&desc.isNull()){
            share(bmp,SendMessageToWX.Req.WXSceneTimeline)
            return
        }
        share(url, title, desc, bmp, SendMessageToWX.Req.WXSceneTimeline)
    } else if (bitmap != null) {
        if(url.isNull()&&title.isNull()&&desc.isNull()){
            share(bitmap,SendMessageToWX.Req.WXSceneTimeline)
            return
        }
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