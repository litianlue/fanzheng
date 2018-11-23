package com.dyl.base_lib.show.toast

import android.content.Context
import com.dyl.base_lib.util.ToastUtil

/**
 * Created by dengyulin on 2018/4/8.
 */
//var mToast:Toast

fun Context.toast(vararg str:String?){
    val sb=StringBuffer()
    if(str.isNotEmpty()){
        str.forEach {
            sb.append(it)
        }
        ToastUtil.showMessage(applicationContext,sb.toString())
    }
}