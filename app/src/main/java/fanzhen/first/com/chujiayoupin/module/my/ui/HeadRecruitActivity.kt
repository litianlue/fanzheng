package fanzhen.first.com.chujiayoupin.module.my.ui

import com.dyl.base_lib.view.showDialog
import fanzhen.first.com.chujiayoupin.R
import fanzhen.first.com.chujiayoupin.external.BaseActivity
import fanzhen.first.com.chujiayoupin.external.CallDialogView
import kotlinx.android.synthetic.main.activity_head_recruit.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.sdk25.coroutines.onClick

class HeadRecruitActivity : BaseActivity() {
    override fun initData() {

    }

    override fun initView() {
        setContentView(R.layout.activity_head_recruit)
        initTitle("团长招募")
        btn_head.onClick {
            CallDialogView(this@HeadRecruitActivity,"13800138000"){

            }.apply { dialog = showDialog(dip(630), dip(600)) { } }
        }
    }
}