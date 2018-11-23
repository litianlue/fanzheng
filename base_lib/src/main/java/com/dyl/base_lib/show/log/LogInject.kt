package com.dyl.base_lib.show.log

import com.dyl.base_lib.data.file.createNewFile
import com.orhanobut.logger.Logger
import java.io.FileOutputStream
import java.io.IOException
import java.io.OutputStreamWriter


/**
 * Created by dengyulin on 2018/3/31.
 */
fun getArgsStr(vararg strs: Any?): String {
    val sb = StringBuffer()
    for (it in strs) {
        sb.append(it.toString())
        sb.append("\r\n")
    }
    return sb.toString()
}

fun <T> T.logi(vararg str: Any?) {
    Logger.i(getArgsStr(*str))
}

fun <T> T.loge(vararg str: Any?) {
    Logger.e(getArgsStr(*str))
}

fun <T> T.logw(vararg str: Any?) {
    Logger.w(getArgsStr(*str))
}

fun <T> T.logv(vararg str: Any?) {
    Logger.v(getArgsStr(*str))
}

fun <T> T.logd(vararg str: Any?) {
    Logger.d(getArgsStr(*str))
}

fun <T> T.logtag(tag: String?) {
    Logger.t(tag)
}

fun logFile(tag: String, str: String) {
    val sb = StringBuffer()
    sb.append("$tag=$str\n")
    val file=createNewFile("/sdcard/log/log.txt")
    val fos=FileOutputStream(file,true)
    val writer=OutputStreamWriter(fos)
    try {
        writer.append(sb.toString())
        writer.flush()
        fos.flush()
    } catch (e: IOException) {
        e.printStackTrace()
    }
    writer.close()
    fos.close()
}