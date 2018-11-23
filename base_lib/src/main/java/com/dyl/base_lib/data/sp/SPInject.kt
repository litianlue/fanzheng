package com.dyl.base_lib.data.sp

import android.content.Context
import android.content.Context.MODE_PRIVATE
import com.dyl.base_lib.BuildConfig

/**
 * Created by dengyulin on 2018/4/8.
 */
private val SP_FILENAME = BuildConfig.SP_NAME

fun Context.putSpData(tag: String, obj: Any? = "",spName:String=SP_FILENAME) {
    getSharedPreferences(spName, MODE_PRIVATE).apply {
        with(edit()) {
            when (obj) {
                is String -> putString(tag, obj)
                is Long -> putLong(tag, obj)
                is Float -> putFloat(tag, obj)
                is Int -> putInt(tag, obj)
                is Boolean -> putBoolean(tag, obj)
                else -> throw NotImplementedError()
            }
            commit()
        }
    }
}
fun Context.putSpData(vararg obj: Pair<String,Any> ,spName:String=SP_FILENAME) {
    getSharedPreferences(spName, MODE_PRIVATE).apply {
        with(edit()) {
            obj.forEach {
                val data= it.second
                when (data) {
                    is String -> putString(it.first, data)
                    is Long -> putLong(it.first, data)
                    is Float -> putFloat(it.first, data)
                    is Int -> putInt(it.first, data)
                    is Boolean -> putBoolean(it.first, data)
                    else -> throw NotImplementedError()
                }

            }
            commit()
        }
    }

}

fun Context.hasSpData(tag: String,spName:String=SP_FILENAME) = getSharedPreferences(spName, MODE_PRIVATE).contains(tag)

fun Context.rmSpTag(tag: String,spName:String=SP_FILENAME) {
    getSharedPreferences(spName, MODE_PRIVATE).apply {
        edit().remove(tag).commit()
    }
}

fun Context.rmSp(spName:String=SP_FILENAME) {
    getSharedPreferences(spName, MODE_PRIVATE).apply {
        edit().clear().commit()
    }
}

fun <T> Context.getSpData(tag: String, t: T,spName:String=SP_FILENAME): T {
    return with(getSharedPreferences(spName, MODE_PRIVATE)) {
        when (t) {
            is String -> getString(tag, t)
            is Long -> getLong(tag, t)
            is Float -> getFloat(tag, t)
            is Int -> getInt(tag, t)
            is Boolean -> getBoolean(tag, t)
            else -> throw NotImplementedError()
        }
    } as T
}


