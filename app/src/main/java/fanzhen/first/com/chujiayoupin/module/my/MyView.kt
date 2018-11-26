package fanzhen.first.com.chujiayoupin.module.my

import android.content.Context
import com.dyl.base_lib.base.BaseView
import com.first.chujiayoupin.model.UserData
import fanzhen.first.com.chujiayoupin.R
import org.greenrobot.eventbus.Subscribe

/**
 * Created by dengyulin on 2018/5/3.
 */
class MyView(context: Context) : BaseView(context) {
    var userData:UserData?=null
    override fun initData() {

    }
    @Subscribe
    fun onEvent(use:UserData){
        initData()
    }
    override fun initView() {
        initContentView(R.layout.layout_my)


    }

}