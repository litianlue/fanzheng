package fanzhen.first.com.chujiayoupin.module.my.ui

import android.content.Context
import com.dyl.base_lib.base.BaseView
import com.dyl.base_lib.img.loadAutoImage
import com.first.chujiayoupin.model.UserData
import fanzhen.first.com.chujiayoupin.R
import kotlinx.android.synthetic.main.layout_my.view.*
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.sdk25.coroutines.onClick

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
        v!!.iv_head_pic.loadAutoImage("", error = R.mipmap.ic_default_head, radius = -1)
        v!!.iv_head_pic.onClick { startActivity<PersonalIinfoActivity>() }
        v!!.rl_myorder.onClick { startActivity<MyOrderActivity>() }
        v!!.rl_head.onClick { startActivity<HeadRecruitActivity>() }
        v!!.rl_head_info.onClick { startActivity<HeadInfoActivity>() }
    }

}