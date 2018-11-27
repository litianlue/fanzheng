package fanzhen.first.com.chujiayoupin.module.shopping.include

import android.content.Context
import android.view.View
import com.dyl.base_lib.base.BpAdapter
import com.dyl.base_lib.base.vertical

import com.dyl.base_lib.util.ToastUtil
import com.first.chujiayoupin.model.SCart
import com.ppx.kotlin.utils.inject.inflate
import fanzhen.first.com.chujiayoupin.R
import fanzhen.first.com.chujiayoupin.module.shopping.ui.ShoppingView
import kotlinx.android.synthetic.main.item_shopping_product_info.view.*
import kotlinx.android.synthetic.main.layout_shopping.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick

fun ShoppingView.initShoppingRcy(){
    v!!.rv_goods.vertical()
    v!!.rv_goods.adapter  =object : BpAdapter<SCart>(){

        override fun getView(context: Context, type: Int): View {
             return  context.inflate { R.layout.item_shopping_product_info }
        }
        override fun onNotify(v: View, index: Int, data: SCart) {
            super.onNotify(v, index, data)

            v.add.onClick {
                var count = v.count.text.toString().toInt()
                v.count.text = (count+1).toString()
            }
            v.remove.onClick {
                var count = v.count.text.toString().toInt()
                if(count<1) v.count.text = "0"
                else
                v.count.text = (count-1).toString()
            }
           /* v.onClick {
                ToastUtil.toast(this@initShoppingRcy.context,"position="+index)
            }*/
            v.item_delete.onClick {
                ToastUtil.toast(this@initShoppingRcy.context,"删除="+index)
            }


        }

    }.notifyDataSetChanged(listOf(SCart(),SCart(),SCart(),SCart(),SCart()))


}