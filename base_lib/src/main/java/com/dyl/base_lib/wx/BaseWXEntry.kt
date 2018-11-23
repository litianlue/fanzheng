package com.dyl.base_lib.wx

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.dyl.base_lib.base.BApplication
import com.dyl.base_lib.event.sendEvent
import com.dyl.base_lib.model.WeiXin
import com.dyl.base_lib.net.call
import com.dyl.base_lib.net.load
import com.dyl.base_lib.show.toast.toast
import com.dyl.base_lib.util.isNull
import com.dyl.base_lib.view.show
import com.tencent.mm.opensdk.constants.ConstantsAPI
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.modelmsg.SendAuth
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

open class BaseWXEntry : Activity(), IWXAPIEventHandler {
    var code: String = ""
    var resp: BaseResp? = null
    private var api: IWXAPI? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = BApplication.wxApi
        api!!.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        api!!.handleIntent(intent, this)
    }

    override fun onReq(baseReq: BaseReq) {
        finish()
    }

    override fun onResp(baseResp: BaseResp?) {
        if (baseResp != null) {
            resp = baseResp
        }
        if (resp!!.type == ConstantsAPI.COMMAND_SENDMESSAGE_TO_WX) {
            when (resp!!.errCode) {
                BaseResp.ErrCode.ERR_OK -> {
                    sendEvent(WeiXin.WXShareResult(resp!!.errCode.toString()))
                  //  sendEvent("已经分享成功")
                    toast("分享成功")
                }
                BaseResp.ErrCode.ERR_USER_CANCEL ->
                    toast("取消分享")
                BaseResp.ErrCode.ERR_SENT_FAILED ->
                    toast("分享失败")
            }
            finish()
        } else {
            //微信登录授权返回值
            code = (baseResp as SendAuth.Resp).code //即为所需的code
            when (baseResp.errCode) {
                BaseResp.ErrCode.ERR_OK -> {
                    if (code.isNull()) {
                        finish()
                        return
                    }
                    sendEvent<Any, Any>(WeiXin.WXCODE(code))
                    finish()
                }
                BaseResp.ErrCode.ERR_USER_CANCEL -> {
                    finish()
                }
                BaseResp.ErrCode.ERR_AUTH_DENIED -> {
                    finish()
                }
            }
        }

    }

}