package com.dyl.base_lib.model

open class NetEventModel2(val code:String, val data:BModel, val call:(code:Int,msg:String)->Unit={_,_->}){
    companion object {
        val NET_RECONNETCTION=1
        val NET_FAIL=-1
        val NET_SUCCESS=0
    }
}