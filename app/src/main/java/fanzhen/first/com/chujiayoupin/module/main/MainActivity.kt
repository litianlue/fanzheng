package fanzhen.first.com.chujiayoupin.module.main

import android.content.Intent
import android.util.Log
import com.ppx.kotlin.utils.inject.notifyAny
import fanzhen.first.com.chujiayoupin.R
import fanzhen.first.com.chujiayoupin.external.BaseActivity
import fanzhen.first.com.chujiayoupin.module.home.HomeView
import fanzhen.first.com.chujiayoupin.module.my.MyView
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.Subscribe


class MainActivity : BaseActivity() {
    val homeView by lazy {
        HomeView(this)
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
        initBottom()

    }

    override fun onDestroy() {
        super.onDestroy()
        Log.w("test","onDestroy")
    }

    @Subscribe
    fun onEvent(code: String) {

    }


}
