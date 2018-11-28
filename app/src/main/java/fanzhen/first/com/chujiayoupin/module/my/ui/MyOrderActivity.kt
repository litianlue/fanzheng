package fanzhen.first.com.chujiayoupin.module.my.ui

import com.dyl.base_lib.base.BaseView
import fanzhen.first.com.chujiayoupin.R
import fanzhen.first.com.chujiayoupin.external.BaseActivity
import fanzhen.first.com.chujiayoupin.module.my.include.initViewPager
import kotlinx.android.synthetic.main.activity_my_order.*

class MyOrderActivity : BaseActivity() {

    var tabs: List<String> = listOf()
    var itemView: List<BaseView> = listOf()
    var lastPix=-1

    override fun initData() {
        tabs = listOf("全部","待付款","已付款","已完成")
        itemView = listOf(MyOrderView(this,""),MyOrderView(this,"1"),
                MyOrderView(this,"2"),MyOrderView(this,"4"))

        tablayout.setupWithViewPager(viewpager!!)
        initViewPager()
    }

    override fun initView() {
        setContentView(R.layout.activity_my_order)
        initTitle("我的订单")
    }
}