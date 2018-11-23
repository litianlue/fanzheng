package com.dyl.base_lib.data.cache

import android.util.LruCache
import com.dyl.base_lib.model.TimeTagCache

/**
 * Created by dengyulin on 2018/4/20.
 */
class Cache {
    companion object {
        val COOKIE = "Cookie"
        val cache: LruCache<String, Any> by lazy {
            val maxSize = Runtime.getRuntime().maxMemory().toInt() / 8
            LruCache<String, Any>(maxSize)
        }

        fun <T> getCache(str: String): T {
            return cache.get(str) as T
        }

        fun putCache(str: String, any: Any) {
            cache.put(str, any)
        }

        fun hasGoDieCache(str: String): Boolean {
            if (!has(str)) {
                return false
            }
            if (getCache<TimeTagCache<*>>(str).GoDie()) {
                rmCache(str)
                return false
            } else {
                return true
            }
        }

        fun has(str: String) =
                cache.get(str) != null

        fun <T> popCache(key: String): T = cache.remove(key) as T

        fun <T> popContainsCache(key: String): T? {
            val lmap = cache.snapshot()
            val str = lmap.keys.firstOrNull { it.contains(key) }
            lmap.clear()
            if (str == null) {
                return null
            }
            return cache.remove(str) as T
        }

        fun rmCache(key: String) {
            cache.remove(key)
        }


    }
}