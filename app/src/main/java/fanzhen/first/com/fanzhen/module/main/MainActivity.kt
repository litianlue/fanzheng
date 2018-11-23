package fanzhen.first.com.fanzhen.module.main


import android.content.Intent
import com.dyl.base_lib.show.toast.toast
import com.ppx.kotlin.utils.inject.notifyAny
import fanzhen.first.com.fanzhen.R
import fanzhen.first.com.fanzhen.external.BaseActivity
import fanzhen.first.com.fanzhen.external.util.UWhiteListSetting
import fanzhen.first.com.fanzhen.jpush.NotificationUtil
import fanzhen.first.com.fanzhen.module.home.HomeView
import fanzhen.first.com.fanzhen.module.info.InfoView
import fanzhen.first.com.fanzhen.module.live.LiveView
import fanzhen.first.com.fanzhen.module.my.MyView
import fanzhen.first.com.fanzhen.module.shopping.ui.ShoppingView
import fanzhen.first.com.fanzhen.service.LocalService
import fanzhen.first.com.fanzhen.service.RemoteService
import kotlinx.android.synthetic.main.activity_main.*

import org.greenrobot.eventbus.Subscribe
import cn.jpush.android.api.JPushInterface
import android.R.attr.action
import android.support.v4.app.FragmentActivity
import android.util.Log
import com.dyl.base_lib.data.sp.putSpData
import fanzhen.first.com.fanzhen.jpush.TagAliasOperatorHelper.*


class MainActivity : BaseActivity() {
    val SETTINGS_ACTION = "android.settings.APPLICATION_DETAILS_SETTINGS"
    val homeView by lazy {
        HomeView(this)
    }
    val liveView by lazy {
        LiveView(this)
    }
    val infoView by lazy {
        InfoView(this)
    }
    val shoppingView by lazy {
        ShoppingView(this)
    }
    val myView by lazy {
        MyView(this)
    }

    override fun initData() {

    }

    override fun onNewIntent(intent: Intent?) {
        super.onNewIntent(intent)
        val id = popAllData<Any>() ?: null
        if (id is Int) {
            main.notifyAny(id)
        }
    }

    override fun initView() {
        setContentView(R.layout.activity_main)
        //isSwipeAnyWhere = false
        isSwipeEnabled = false
        initBottom()
        this.startService(Intent(this, LocalService::class.java))
        this.startService(Intent(this, RemoteService::class.java))
       // UWhiteListSetting.enterWhiteListSetting(this)
     //   NotificationUtil.setNotification(this)
        //var intent = Intent(this,MyTestActivity::class.java)
        // startActivity(intent)

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("test","onDestroy")
    }

    @Subscribe
    fun onEvent(code: String) {
        toast(code)
    }


}
