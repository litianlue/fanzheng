package com.dyl.base_lib.external

import android.content.Context
import android.support.v7.app.AppCompatDialog
import com.dyl.base_lib.R
import com.dyl.base_lib.base.BaseView
import com.dyl.base_lib.util.isNotNull
import com.dyl.base_lib.view.show
import kotlinx.android.synthetic.main.view_dialog.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick

/**
 * Created by dengyulin on 2018/6/12.
 */


class HintDialogView(context: Context, val title: String, val message: String, val leftStr: String = "取消", val rightStr: String = "确定", val call: AppCompatDialog.() -> Unit) : BaseView(context) {
    var dialog: AppCompatDialog? = null
    override fun initData() {

    }

    override fun initView() {
        initContentView(R.layout.view_dialog) {
            tv_dialog_title?.text = title
            tv_dialog_msg?.text = message
            btn_dialog_cancel?.text = leftStr
            btn_dialog_sure?.text = rightStr
            btn_dialog_cancel?.show = leftStr.isNotNull()
            btn_dialog_cancel?.onClick { dialog?.cancel() }
            btn_dialog_sure?.onClick {
                call.invoke(dialog!!)
            }
        }
    }
}
