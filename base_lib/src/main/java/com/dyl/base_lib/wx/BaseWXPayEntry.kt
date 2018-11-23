package com.dyl.base_lib.wx

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.dyl.base_lib.base.BApplication
import com.dyl.base_lib.event.sendEvent
import com.dyl.base_lib.model.WeiXin
import com.tencent.mm.opensdk.modelbase.BaseReq
import com.tencent.mm.opensdk.modelbase.BaseResp
import com.tencent.mm.opensdk.openapi.IWXAPI
import com.tencent.mm.opensdk.openapi.IWXAPIEventHandler

open class BaseWXPayEntry : Activity(), IWXAPIEventHandler {
    private var api: IWXAPI? = null

    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        api = BApplication.wxApi
        api!!.handleIntent(intent, this)
    }

    override fun onNewIntent(intent: Intent) {
        super.onNewIntent(intent)
        setIntent(intent)
        api!!.handleIntent(intent, this)
    }

    override fun onReq(req: BaseReq) {
        finish()
    }

    override fun onResp(resp: BaseResp) {
        when (resp.errCode) {
            0 -> {
                sendEvent(WeiXin.WXPayResult(resp!!.errCode.toString()))
                finish()
            }
            1 -> finish()
            2 -> finish()
            else->finish()
        }
    }
}