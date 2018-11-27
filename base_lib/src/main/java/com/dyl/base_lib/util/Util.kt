package com.dyl.base_lib.util

import android.Manifest
import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.graphics.Bitmap
import android.location.Address
import android.location.Geocoder
import android.text.TextUtils
import com.dyl.base_lib.show.toast.toast
import com.google.zxing.BarcodeFormat
import com.google.zxing.EncodeHintType
import com.google.zxing.WriterException
import com.google.zxing.qrcode.QRCodeWriter
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import java.io.IOException
import java.text.DecimalFormat
import java.util.*



/**
 * Created by dengyulin on 2018/4/18.
 */
public inline fun <reified T> List<out T>.forEachToList(action: (T) -> T): List<T> {
    val list = ArrayList<T>()
    for (element in this) {
        list.add(action(element))
    }
    return list
}

public inline fun <reified T> List<out T>.filtration(action: (T) -> Boolean): List<T> {
    val list = ArrayList<T>()
    for (element in this) {
        if (action(element)) {
            list.add(element)
        }
    }
    return list
}

public fun Double.to2Double(): String {
    val df = DecimalFormat("######0.00")
    return df.format(this)
}
public fun Double.keep2Double(): Double {
    return to2Double().toDouble()
}

public fun Double.toAutoDouble(): String {
    val sd = keep2Double()
    return if (sd.toString().split(".")[1].toInt() > 0) {
        sd.toString()
    } else {
        sd.toLong().toString()
    }
}

public fun String.isNull(): Boolean {
    return this == null || this == "" || this.trim().isEmpty()
}

public fun String.isNotNull(): Boolean {
    return !isNull()
}

fun Context.copyTo(str: String) {
    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    // 创建普通字符型ClipData
    val mClipData = ClipData.newPlainText("Label", str)
    // 将ClipData内容放到系统剪贴板里。
    cm.primaryClip = mClipData
    toast("复制成功")
}
fun Context.copyToNotoast(str: String) {
    val cm = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
    // 创建普通字符型ClipData
    val mClipData = ClipData.newPlainText("Label", str)
    // 将ClipData内容放到系统剪贴板里。
    cm.primaryClip = mClipData

}

fun Context.location(call: (x: Double, y: Double) -> Unit) {
    reqPermission(Manifest.permission.ACCESS_FINE_LOCATION) {
        val doubles = LocationUtil.getLngAndLat(this@location)
        call.invoke(doubles[0], doubles[1])
    }
}

fun Context.locationAddress(call: (p: String, c: String, a: String, address: String, full: String) -> Unit) {
    location { x, y ->
        address(x, y, call)
    }
}

fun Context.address(x: Double, y: Double, call: (p: String, c: String, a: String, address: String, full: String) -> Unit) {
    var addList: List<Address>? = null
    val ge = Geocoder(this)
    try {
        addList = ge.getFromLocation(x, y, 1)
    } catch (e: IOException) {
        e.printStackTrace()
    }

    if (addList != null && addList.isNotEmpty()) {
        for (i in addList!!.indices) {
            val ad = addList!![i]
            launch(UI) {
                if (TextUtils.isEmpty(ad.featureName)) {
                    call.invoke(ad.adminArea, ad.locality, ad.subLocality, "", ad.adminArea + ad.locality + ad.subLocality)
                } else {
                    call.invoke(ad.adminArea, ad.locality, ad.subLocality, ad.featureName, ad.adminArea + ad.locality + ad.subLocality)
                }
            }

        }
    }
}

fun <T>  T.createQRImage(url: String="", width: Int=400, height: Int=400): Bitmap? {
    try {
        // 判断URL合法性
        if (url == null || "" == url || url.length < 1) {
            return null
        }
        val hints = Hashtable<EncodeHintType,String>()
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8")
        // 图像数据转换，使用了矩阵转换
        val bitMatrix = QRCodeWriter().encode(url,
                BarcodeFormat.QR_CODE, width, height, hints)
        val pixels = IntArray(width * height)
        // 下面这里按照二维码的算法，逐个生成二维码的图片，
        // 两个for循环是图片横列扫描的结果
        for (y in 0 until height) {
            for (x in 0 until width) {
                if (bitMatrix.get(x, y)) {
                    pixels[y * width + x] = -0x1000000
                } else {
                    pixels[y * width + x] = -0x1
                }
            }
        }
        // 生成二维码图片的格式，使用ARGB_8888
        val bitmap = Bitmap.createBitmap(width, height,
                Bitmap.Config.ARGB_8888)
        bitmap.setPixels(pixels, 0, width, 0, 0, width, height)
        return bitmap
    } catch (e: WriterException) {
        e.printStackTrace()
    }

    return null
}