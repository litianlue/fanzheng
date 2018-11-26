package fanzhen.first.com.chujiayoupin.module.main

import com.dyl.base_lib.data.sp.putSpData
import com.dyl.base_lib.model.CoockieData
import com.dyl.base_lib.model.WeiXin
import com.first.chujiayoupin.event.net.IS_LOGIN
import fanzhen.first.com.chujiayoupin.R
import fanzhen.first.com.chujiayoupin.external.BaseActivity
import org.greenrobot.eventbus.Subscribe


class LoginActivity : BaseActivity() {
    var count=0
    override fun initData() {
    }

    override fun initView() {


        setContentView(R.layout.activity_login)
      /*  login.onClick {
            loginWx()

        }
        tv_hint.movementMethod = LinkMovementMethod.getInstance()
        tv_hint.text = TextStorage().addText("点击登录按钮即表示同意", 23, "#999999").addText("《用户服务协议》", 23, click = {
            startActivity<UserRegistActivity>()
        }).spb
        if(BApplication.wxApi?.isWXAppInstalled==false){
            tv_login.text="手机登录"
            login.onClick {
                startActivity<LoginPhoneActivity>()
                finish()
            }
        }*/

    }
    @Subscribe
    fun onEvent(code: WeiXin.WXCODE){
        com.first.chujiayoupin.event.net.login(code.code) {
            putSpData(IS_LOGIN,true)
            CoockieData.save(this@LoginActivity)
            finish()
        }
    }


}
