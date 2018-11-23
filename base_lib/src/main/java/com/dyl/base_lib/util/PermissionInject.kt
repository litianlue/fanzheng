package com.dyl.base_lib.util

import android.content.Context
import com.dyl.base_lib.base.BaseActivity
import com.yxp.permission.util.lib.PermissionUtil
import com.yxp.permission.util.lib.callback.PermissionResultCallBack


fun Context.reqPermission(vararg permissions:String,call:()->Unit){
    PermissionUtil.getInstance().request(this as BaseActivity, permissions,
            object : PermissionResultCallBack {
                override fun onPermissionGranted() {
                    // 当所有权限的申请被用户同意之后,该方法会被调用
                    call.invoke()
                }

                override fun onPermissionGranted(vararg strings: String) {
                    // 当所有权限的申请被用户同意之后,该方法会被调用
                }
                override fun onPermissionDenied(vararg permissions: String) {
                    // 当权限申请中的某一个或多个权限,被用户曾经否定了,并确认了不再提醒时,也就是权限的申请窗口不能再弹出时,该方法将会被调用
                }

                override fun onRationalShow(vararg permissions: String) {
                    // 当权限申请中的某一个或多个权限,被用户否定了,但没有确认不再提醒时,也就是权限窗口申请时,但被否定了之后,该方法将会被调用.
                }
            })
}