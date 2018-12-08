package fanzhen.first.com.chujiayoupin.module.home.ui

import com.dyl.base_lib.view.showDialog
import fanzhen.first.com.chujiayoupin.R
import fanzhen.first.com.chujiayoupin.external.BaseActivity
import fanzhen.first.com.chujiayoupin.external.OrderDialog
import kotlinx.android.synthetic.main.orderacivity.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.sdk25.coroutines.onClick

class OrderAcivity:BaseActivity() {
    override fun initData() {
    }
    override fun initView() {
       setContentView(R.layout.orderacivity)
        commit_order.onClick {
            OrderDialog(this@OrderAcivity,""){

            }.apply { dialog = showDialog(dip(630), dip(600)) { } }
        }
    }
}