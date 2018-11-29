package fanzhen.first.com.chujiayoupin.external

import android.content.Context
import android.support.v7.app.AppCompatDialog
import com.dyl.base_lib.base.BaseView
import fanzhen.first.com.chujiayoupin.R
import kotlinx.android.synthetic.main.view_call.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class CallDialogView (context : Context, val message:String, val call:AppCompatDialog.()->Unit) : BaseView(context)  {
    var dialog:AppCompatDialog?=null
    override fun initData() {

    }

    override fun initView() {
        initContentView(R.layout.view_call)
        v?.tv_phone?.text = message
        v?.iv_close?.onClick {   dialog?.cancel() }
        v?.btn_call?.onClick {
            call.invoke(dialog!!)
        }
    }
}