package com.dyl.base_lib.coroutines

import android.content.Context
import android.widget.TextView
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

/**
 * Created by dengyulin on 2018/6/8.
 */
class CoroutinesInject {
    fun startDelay(time: Long, tCall: () -> Unit, mCall: () -> Unit) {
        launch(CommonPool) {
            delay(time)
            tCall.invoke()
            launch(UI) {
                mCall.invoke()
            }
        }.start()
    }

    fun whileTime(tCall:suspend () -> Unit={},mCall:suspend () -> Unit={},afterCall:suspend () -> Unit={}): Job {
        return launch(CommonPool) {
            while (true) {
                tCall.invoke()
                launch(UI) {
                    mCall.invoke()
                }
                afterCall.invoke()
            }
        }.apply { start() }
    }
}
fun Context.startDelay(time: Long, tCall: () -> Unit, mCall: () -> Unit) {
    launch(CommonPool) {
        delay(time)
        tCall.invoke()
        launch(UI) {
            mCall.invoke()
        }
    }.start()
}

fun Context.whileTime(tCall:suspend () -> Unit={},mCall:suspend () -> Unit={},afterCall:suspend () -> Unit={}): Job {
    return launch(CommonPool) {
        while (true) {
            tCall.invoke()
            launch(UI) {
                mCall.invoke()
            }
            afterCall.invoke()
        }
    }.apply { start() }
}
fun Context.timer60s(tv:TextView){
    var time=59
    tv.isEnabled=false
    var job:Job?=null
    job =whileTime (mCall = {
        tv.text="${time}s"
        if(time==0){
            tv.isEnabled=true
            tv.text="重新获取"
            job?.cancel()
        }
    },afterCall = {
        delay(1000)
        time--
    })
}