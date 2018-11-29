package fanzhen.first.com.chujiayoupin.external

import android.content.Context
import android.support.v7.app.AppCompatDialog
import com.dyl.base_lib.base.BaseView
import fanzhen.first.com.chujiayoupin.R
import kotlinx.android.synthetic.main.orderdialog.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class OrderDialog(context: Context,string: String,val call:AppCompatDialog.()->Unit):BaseView(context) {
    var dialog: AppCompatDialog?=null
    override fun initData() {

    }

    override fun initView() {
        initContentView(R.layout.orderdialog).apply {
            v!!.cancle_tv.onClick {
                dialog?.dismiss()
            }
            v!!.affirm_tv.onClick {
                dialog?.dismiss()
            }
        }
    }
}