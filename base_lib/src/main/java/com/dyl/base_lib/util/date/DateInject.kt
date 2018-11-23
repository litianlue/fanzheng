package com.dyl.base_lib.util.date

import java.util.*

/**
 * Created by dengyulin on 2018/6/9.
 */
fun <T> T.nowDay(): Int {
    val calendar = Calendar.getInstance()
    return calendar.get(Calendar.DAY_OF_MONTH)
}
fun <T> T.nowMonth(): Int {
    val calendar = Calendar.getInstance()
    return calendar.get(Calendar.MONTH)+1
}
fun <T> T.nowYear(): Int {
    val calendar = Calendar.getInstance()
    return calendar.get(Calendar.YEAR)
}

fun <T> T.nowHour(): Int {
    val calendar = Calendar.getInstance()
    return calendar.get(Calendar.HOUR_OF_DAY)
}

fun <T> T.nowMinute(): Int {
    val calendar = Calendar.getInstance()
    return calendar.get(Calendar.MINUTE)
}

fun <T> T.nowSecond(): Int {
    val calendar = Calendar.getInstance()
    return calendar.get(Calendar.SECOND)
}
