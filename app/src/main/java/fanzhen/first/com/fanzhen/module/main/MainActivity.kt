package fanzhen.first.com.fanzhen.module.main


import android.content.Intent
import com.dyl.base_lib.show.toast.toast
import com.ppx.kotlin.utils.inject.notifyAny
import fanzhen.first.com.fanzhen.R
import fanzhen.first.com.fanzhen.external.BaseActivity
import fanzhen.first.com.fanzhen.module.home.HomeView
import fanzhen.first.com.fanzhen.module.my.MyView
import kotlinx.android.synthetic.main.activity_main.*
import org.greenrobot.eventbus.Subscribe
import android.util.Log

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
        isSwipeEnabled = false
        initBottom()

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
