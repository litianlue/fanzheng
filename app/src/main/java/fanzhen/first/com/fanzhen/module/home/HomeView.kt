package fanzhen.first.com.fanzhen.module.home
import android.content.Context
import com.dyl.base_lib.base.BaseView
import com.dyl.base_lib.event.sendEvent

import fanzhen.first.com.fanzhen.R
/**
 * Created by dengyulin on 2018/5/3.
 */
class HomeView(context: Context) : BaseView(context) {

    override fun initData() {
    }
    override fun initView(){
        initContentView(R.layout.layout_home){
            sendEvent("刷新")
        }
    }
    companion object {
        var shopNo:String=""
    }

}