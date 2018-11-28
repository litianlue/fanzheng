package fanzhen.first.com.chujiayoupin.module.my.ui

import com.dyl.base_lib.img.loadAutoImage
import fanzhen.first.com.chujiayoupin.R
import fanzhen.first.com.chujiayoupin.external.BaseActivity
import kotlinx.android.synthetic.main.activity_personal_info.*

class PersonalIinfoActivity : BaseActivity(){
    override fun initData() {

    }

    override fun initView() {
       setContentView(R.layout.activity_personal_info)
        initTitle("个人信息")
        iv_head_pic.loadAutoImage("", error = R.mipmap.ic_default_head, radius = -1)
    }

}