package fanzhen.first.com.chujiayoupin.module.shopping.ui

import android.content.Context
import android.view.View
import android.widget.Toast
import com.dyl.base_lib.base.BaseView
import com.dyl.base_lib.base.toBpAdapter
import com.dyl.base_lib.net.call
import com.dyl.base_lib.net.load
import com.dyl.base_lib.view.OnItemClickListener
import com.first.chujiayoupin.model.RLogin
import com.first.chujiayoupin.model.RShoppingCart
import com.first.chujiayoupin.model.SCart
import com.tencent.mm.opensdk.utils.Log
import fanzhen.first.com.chujiayoupin.R
import fanzhen.first.com.chujiayoupin.module.shopping.include.initShoppingRcy
import fanzhen.first.com.chujiayoupin.service.ConnectApi
import kotlinx.android.synthetic.main.layout_shopping.view.*
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.sdk25.coroutines.onClick


/**
 * Created by dengyulin on 2018/5/3.
 */
class ShoppingView(context: Context):BaseView(context) {


    var goodList= mutableListOf<SCart?>()

    override fun initView() {
        goodList.add(SCart())
        goodList.add(SCart())
        goodList.add(SCart())
        goodList.add(SCart())
        goodList.add(SCart())
        goodList.add(SCart())
        load(ConnectApi::class.java).login(RLogin("1234")).call {
             Log.w("test","model="+Result)
        }
    }
    @Subscribe
    fun onEvent(model: RShoppingCart){

    }
    override fun initData() {
        initContentView(R.layout.layout_shopping)
        initShoppingRcy()
        v!!.cb_allCheck.onClick {
            if( v!!.cb_allCheck.isChecked) v!!.tv_select_all.text ="取消全选"
            else v!!.tv_select_all.text ="全选"
        }
        v!!.tv_delete_all.onClick {

        }
    }


}