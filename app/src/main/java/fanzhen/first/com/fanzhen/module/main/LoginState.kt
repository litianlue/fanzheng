package fanzhen.first.com.fanzhen.module.main

import android.content.Context
import com.dyl.base_lib.base.BaseActivity
import com.dyl.base_lib.data.cache.Cache
import com.dyl.base_lib.data.sp.getSpData
import com.dyl.base_lib.data.sp.hasSpData
import com.dyl.base_lib.model.NetEventModel
import com.first.chujiayoupin.event.net.Cache_event
import com.first.chujiayoupin.event.net.IS_LOGIN

import org.jetbrains.anko.startActivity

fun Context.isLogin():Boolean{
    return hasSpData(IS_LOGIN)&&getSpData(IS_LOGIN,false)&&hasSpData(Cache.COOKIE)
}
inline fun <reified T : BaseActivity> Context.checkLoginStartActivity(p:Any?=null, crossinline call:()->Unit={(this as BaseActivity).startActivity<T>(p)}){
    if(isLogin()){
        call.invoke()
    }else{
        Cache.putCache(Cache_event, NetEventModel("","",{code->if(code==0)call.invoke()}))
        startActivity<LoginActivity>()
    }
}