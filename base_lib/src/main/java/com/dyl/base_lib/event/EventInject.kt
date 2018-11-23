package com.dyl.base_lib.event

import org.greenrobot.eventbus.EventBus

/**
 * Created by dengyulin on 2018/3/31.
 */
fun<T> T.register(){
    EventBus.getDefault().register(this)

}
fun<T> T.unRegister(){
    EventBus.getDefault().unregister(this)
}
fun<T,P> T.sendEvent(obj:P?){
    EventBus.getDefault().post(obj)
}
