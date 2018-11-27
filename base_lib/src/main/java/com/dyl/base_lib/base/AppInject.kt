package com.dyl.base_lib.base

import android.app.Application
import android.content.Context
import android.content.res.Resources
import android.util.DisplayMetrics
import android.util.Log
import com.dyl.base_lib.BuildConfig
import com.dyl.base_lib.data.sp.getSpData
import com.dyl.base_lib.model.CoockieData
import com.dyl.base_lib.net.HttpLoggingInterceptor
import com.orhanobut.logger.AndroidLogAdapter
import com.orhanobut.logger.Logger
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


/**
 * Created by dengyulin on 2018/3/31.
 */
fun Application.init() {

    initLog()
    NET
    initDatabase()
//    applicationContext.initDisplaySize(resources::class.java)
}


val <T> T.NET: Retrofit by lazy {
    initNet()
}

tailrec fun Context.initDisplaySize(res:Resources,c: Class<*>):Resources {
    c.declaredFields.forEach {
        it.isAccessible = true
        it.get(res)?.let { a ->
            if (a == res.displayMetrics) {
                val dis = DisplayMetrics()
                dis.setTo(res.displayMetrics)
                dis.density = dis.widthPixels / (BuildConfig.DisplaySize.toFloatOrNull()
                        ?: dis.density)
                it.set(res, dis)
                return res
            } else if (a::class.java.name.toLowerCase().contains("ResourcesImpl".toLowerCase())) {
                a::class.java.declaredFields.forEach { b ->
                    b.isAccessible = true
                    if (b.get(a) == res.displayMetrics) {
                        val dis = DisplayMetrics()
                        dis.setTo(res.displayMetrics)
                        dis.density = dis.widthPixels / (BuildConfig.DisplaySize.toFloatOrNull()
                                ?: dis.density)
                        b.set(a, dis)
                        return res
                    }

                }
                a::class.java.superclass?.declaredFields?.forEach { b ->
                    b.isAccessible = true
                    if (b.get(a) == res.displayMetrics) {
                        val dis = DisplayMetrics()
                        dis.setTo(res.displayMetrics)
                        dis.density = dis.widthPixels / (BuildConfig.DisplaySize.toFloatOrNull()
                                ?: dis.density)
                        b.set(a, dis)
                        return res
                    }
                }
            }
        }
    }
    val clz = c.superclass ?: return res
    initDisplaySize(res,clz)
    return res
}
tailrec fun Context.initDisplaySize(c: Class<*>) {
    c.declaredFields.forEach {
        it.isAccessible = true
        it.get(resources)?.let { a ->
            if (a == resources.displayMetrics) {
                val dis = DisplayMetrics()
                dis.setTo(resources.displayMetrics)
                dis.density = dis.widthPixels / (BuildConfig.DisplaySize.toFloatOrNull()
                        ?: dis.density)
                it.set(resources, dis)
                return
            } else if (a::class.java.name.toLowerCase().contains("ResourcesImpl".toLowerCase())) {
                a::class.java.declaredFields.forEach { b ->
                    b.isAccessible = true
                    if (b.get(a) == resources.displayMetrics) {
                        val dis = DisplayMetrics()
                        dis.setTo(resources.displayMetrics)
                        dis.density = dis.widthPixels / (BuildConfig.DisplaySize.toFloatOrNull()
                                ?: dis.density)
                        b.set(a, dis)
                        return
                    }

                }
                a::class.java.superclass?.declaredFields?.forEach { b ->
                    b.isAccessible = true
                    if (b.get(a) == resources.displayMetrics) {
                        val dis = DisplayMetrics()
                        dis.setTo(resources.displayMetrics)
                        dis.density = dis.widthPixels / (BuildConfig.DisplaySize.toFloatOrNull()
                                ?: dis.density)
                        b.set(a, dis)
                        return
                    }
                }
            }
        }
    }
    val clz = c.superclass ?: return
    initDisplaySize(clz)
}
 fun initNet() =
        Retrofit.Builder()
                .baseUrl(BuildConfig.HOSTPATH)
                .addConverterFactory(GsonConverterFactory.create())
                .client(initOkHttp())
                .build()!!

 fun getCookiemmp(): String {
    if (CoockieData.cookies.isNotEmpty()) {
        val sb = StringBuffer()
        val list = CoockieData.cookies
        list.forEach {
            sb.append("${it.key}=${it.value};")
        }
        return sb.toString()
    }
    return ""
}

 fun savaCookie(cookie: List<String>) {
    if (cookie.isNotEmpty()) {
        val map = CoockieData.cookies
        cookie.forEach {
            val a = it.split(";")[0].split("=")
            map[a[0]] = a[1]
        }
        CoockieData.cookies = map
    }
}

 fun initOkHttp(): OkHttpClient = OkHttpClient.Builder()
        .addInterceptor {
            val proceed = it.proceed(it.request().newBuilder()
                    .header("Accept-Language", "en,zh-CN,zh")
                    .header("Accept-Charset", "utf-8")
                    .header("Content-Type", "application/json")
                    .header("Cookie", getCookiemmp())
                    .build())
            val headers = proceed.headers("Set-Cookie")
            savaCookie(headers)
            proceed
        }
        .addInterceptor(initInterceptor())
        .connectTimeout(30000L, TimeUnit.MILLISECONDS)
        .readTimeout(30000L, TimeUnit.MILLISECONDS)
        .build()!!


 fun initInterceptor(): HttpLoggingInterceptor = HttpLoggingInterceptor(object : HttpLoggingInterceptor.Logger {
    override fun log(chain: Interceptor.Chain?, message: String?) {
        if(BuildConfig.DEBUG){
            Log.d("okhttp-${chain?.request()?.url()?.uri()?.path}", message)
        }
    }

    override fun body(chain: Interceptor.Chain?, json: String?) {
        Logger.t("okhttp-${chain?.request()?.url()?.uri()?.path}").json(json)
    }
}).apply { level = if (BuildConfig.DEBUG) HttpLoggingInterceptor.Level.BODY else HttpLoggingInterceptor.Level.HEADERS }!!


 fun initDatabase() {

}

 fun initLog() {
    Logger.addLogAdapter(AndroidLogAdapter())
}