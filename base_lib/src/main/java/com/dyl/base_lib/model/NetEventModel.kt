package com.dyl.base_lib.model

open class NetEventModel(val code:String, val data:Any,val call:(code:Int)->Unit={})