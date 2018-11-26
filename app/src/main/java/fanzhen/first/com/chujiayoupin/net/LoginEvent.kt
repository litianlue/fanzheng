package com.first.chujiayoupin.event.net

import com.dyl.base_lib.data.cache.Cache
import com.dyl.base_lib.data.sp.getSpData
import com.dyl.base_lib.data.sp.hasSpData
import com.dyl.base_lib.event.sendEvent
import com.dyl.base_lib.model.CoockieData
import com.dyl.base_lib.model.NetEventModel
import com.dyl.base_lib.net.callNext
import com.dyl.base_lib.net.netQueue
import com.first.chujiayoupin.model.HomeData
import com.first.chujiayoupin.model.RAuth
import com.first.chujiayoupin.model.RLogin
import fanzhen.first.com.chujiayoupin.BApplication
import fanzhen.first.com.chujiayoupin.service.ConnectApi


const val IS_LOGIN = "isLogin"
const val Cache_event = "nextEvent"
fun BApplication.loginEvent(e: NetEventModel) {
    Cache.putCache(Cache_event, e)
    if (hasSpData(IS_LOGIN)&&getSpData(IS_LOGIN,false)) {
        CoockieData.reset(this)
    } else {
        //currentActivity?.startActivity<LoginActivity>()
    }
}
inline fun login(code: String, crossinline next:()->Unit={}) {
    var e: NetEventModel? = null
    if (Cache.has(Cache_event)) {
        e = Cache.popCache<NetEventModel>(Cache_event)
    }
    code.netQueue(ConnectApi::class.java, {
        logins(RLogin(code)).callNext {}
    }, {
        auth(RAuth()).callNext {
            next.invoke()
            e?.call?.invoke(0)
            sendEvent(HomeData())
        }
    })
}
