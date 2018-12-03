package fanzhen.first.com.chujiayoupin.module.my.ui

import com.dyl.base_lib.external.TextStorage
import fanzhen.first.com.chujiayoupin.R
import fanzhen.first.com.chujiayoupin.external.BaseActivity
import kotlinx.android.synthetic.main.activity_order_details.*

class OrderDetailsActivity : BaseActivity() {
    override fun initData() {

    }

    override fun initView() {
       setContentView(R.layout.activity_order_details)
        initTitle("订单详情")
        tv_order_num.text = TextStorage().addText("订单号：").addText("02184815151165",26,"#333333").spb
        tv_phone.text = TextStorage().addText("联系方式：").addText("李生  134282182033",26,"#333333").spb
        tv_address.text = TextStorage().addText("取货地址：").addText("广州绿地中心广州绿地中心",26,"#333333").spb

    }
}