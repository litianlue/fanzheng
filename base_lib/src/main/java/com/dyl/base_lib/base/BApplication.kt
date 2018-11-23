package com.dyl.base_lib.base

import com.tencent.mm.opensdk.openapi.IWXAPI

/**
 * Created by dengyulin on 2018/4/17.
 */
interface BApplication {
    companion object {
        var currentActivity:BaseActivity?=null
        var wxApi: IWXAPI?=null
    }
}