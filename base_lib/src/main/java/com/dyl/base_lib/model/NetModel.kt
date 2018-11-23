package com.dyl.base_lib.model

import com.dyl.base_lib.data.cache.Cache
import java.io.Serializable

/**
 * Created by dengyulin on 2018/3/31.
 */
open class BModel() : Serializable

open class ReqModel() : BModel() {
    fun sava() {
        if (!Cache.has(this::class.java.name)) {
            Cache.putCache(this::class.java.name, this)
        }
    }
}

open class RepModel(
        val ErrorCode: String = "",
        val Message: String = ""
) :Serializable
