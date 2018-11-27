package com.dyl.base_lib.model

import android.content.Context
import android.graphics.Bitmap
import com.dyl.base_lib.data.cache.Cache
import com.dyl.base_lib.data.sp.getSpData
import com.dyl.base_lib.data.sp.putSpData
import com.dyl.base_lib.data.sp.rmSpTag
import com.google.gson.Gson
import java.io.File

/**
 * Created by dengyulin on 2018/5/26.
 */
data class TimeTagCache<out T>(
        val time: Long = System.currentTimeMillis(),
        val data: T
) {
    open fun GoDie(): Boolean {
        return Math.abs(time - System.currentTimeMillis()) > 5 * 60000
    }
}

class CoockieData {
    companion object {
        var cookies: MutableMap<String, String> = mutableMapOf()
        fun save(context: Context) {
            context.putSpData(Cache.COOKIE, Gson().toJson(cookies))
        }

        fun reset(context: Context) {
            cookies = Gson().fromJson(context.getSpData(Cache.COOKIE, ""), cookies::class.java)
        }
        fun rm(context: Context) {
            cookies= mutableMapOf()
            context.rmSpTag(Cache.COOKIE)
        }
    }
}

class WeiXin {
    data class WXCODE(
            val code: String = ""
    )

    data class WXPayModel(
            var appId: String? = "",
            var partnerId: String? = "",
            var prepayId: String? = "",
            var nonceStr: String? = "",
            var timeStamp: String? = "",
            var packageValue: String? = "",
            var sign: String? = ""
    )

    data class WXPayResult(
            val code: String = ""
    )

    data class WXShareResult(
            val msg: String = ""
    )
}

data class ShareData(
        val shareLink: String = "",
        val shareTitle: String = "",
        val shareContent: String = "",
        val shareImg: Int = -1,
        val bitmap: Bitmap? = null,
        val Type:Int=0,
        val Files:List<File> = listOf()

)