package fanzhen.first.com.fanzhen

import android.app.Activity
import android.os.Bundle
import android.support.multidex.MultiDexApplication
import cn.jpush.android.api.JPushInterface
import com.dyl.base_lib.base.BApplication
import com.dyl.base_lib.base.BApplication.Companion.currentActivity
import com.dyl.base_lib.base.init
import com.dyl.base_lib.event.register
import com.dyl.base_lib.model.NetEventModel
import com.dyl.base_lib.model.RepModel
import com.dyl.base_lib.show.toast.toast
import com.dyl.base_lib.wx.initWx
import com.facebook.drawee.backends.pipeline.Fresco
import org.greenrobot.eventbus.Subscribe
/**
 * Created by dengyulin on 2018/4/17.
 */
class BApplication : MultiDexApplication(), BApplication {
    override fun onCreate() {
        super.onCreate()
        //MobSDK.init(this)
        JPushInterface.setDebugMode(true)
        JPushInterface.init(this)
        init()
        initWx()
        register()
        Fresco.initialize(applicationContext)
        registerActivityLifecycleCallbacks(object : ActivityLifecycleCallbacks {
            override fun onActivityPaused(activity: Activity?) {

            }

            override fun onActivityResumed(activity: Activity?) {

            }

            override fun onActivityStarted(activity: Activity?) {

            }

            override fun onActivityDestroyed(activity: Activity?) {

            }

            override fun onActivitySaveInstanceState(activity: Activity?, outState: Bundle?) {
            }

            override fun onActivityStopped(activity: Activity?) {

            }

            override fun onActivityCreated(activity: Activity?, savedInstanceState: Bundle?) {

            }

        })

    }

    @Subscribe
    fun initNetEvent(e: NetEventModel) {
        when (e.code) {
            "401" -> {}
            "1000000" -> {
                e.call.invoke(1)
            }
            "1000001" -> {
                toast("验证码错误")
                currentActivity?.dissmissDialog()
            }
            "1000002" -> {
                toast("请在微信中打开页面")
                currentActivity?.dissmissDialog()
            }
            "1000003" -> {
                toast((e.data as RepModel).Message)
                currentActivity?.dissmissDialog()
            }
            "1100001" -> toast("红包已领完")
            "1100002" ->{
                toast("优惠券已领取")
                currentActivity?.dissmissDialog()
            }
            "1100003" -> {
                toast("优惠券已失效")
                currentActivity?.dissmissDialog()
            }
            "1200001" ->{
                toast("活动已结束")
                currentActivity?.dissmissDialog()
            }
            "1200002" -> {
                toast("活动未开始")
                currentActivity?.dissmissDialog()
            }
            "1200003" -> {


            }
            "1300001" -> {
                toast("暂无物流信息")
                currentActivity?.dissmissDialog()
            }
            "1400001" -> {

            }
            "1400002" -> {

            }
            "1400004" -> {

            }
            "1500001" -> {
                toast("订单不存在");currentActivity?.finish()
            }
            "1500002" -> {
                toast("订单金额变化")
                currentActivity?.dissmissDialog()
            }
            "1500003" -> {
                toast("订单不能支付")
                currentActivity?.dissmissDialog()
            }
            "1500004" ->{
                toast("未开通支付")
                currentActivity?.dissmissDialog()
            }
            else -> {
                e.call.invoke(1)
                currentActivity?.dissmissDialog()
            }
        }
    }
}