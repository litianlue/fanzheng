package fanzhen.first.com.chujiayoupin.module.my.ui

import android.content.Context
import com.dyl.base_lib.base.BaseView
import fanzhen.first.com.chujiayoupin.R
import fanzhen.first.com.chujiayoupin.module.my.include.initOrderRcy

class MyOrderView(context: Context,OrderState:  String) : BaseView(context) {

    var orderState = OrderState

    override fun initData() {
        initOrderRcy()
    }

    override fun initView() {
        initContentView(R.layout.myview_order)
    }
}