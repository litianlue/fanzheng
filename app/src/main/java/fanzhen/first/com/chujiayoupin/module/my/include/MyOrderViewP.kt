package fanzhen.first.com.chujiayoupin.module.my.include

import android.content.Context
import android.view.View
import com.dyl.base_lib.base.BpAdapter
import com.dyl.base_lib.base.vertical
import com.first.chujiayoupin.model.SOrerDetails
import com.ppx.kotlin.utils.inject.inflate
import fanzhen.first.com.chujiayoupin.R
import fanzhen.first.com.chujiayoupin.module.my.ui.MyOrderView
import kotlinx.android.synthetic.main.myview_order.view.*

fun MyOrderView.initOrderRcy() {
    v!!.rv_order.vertical()
//    onPullLoad { downLoad() }
//    onPullNext {
//        rv_allorder.adapter.notifyDataSetChanged()
//        reset()
//    }
    v!!.rv_order.adapter = object : BpAdapter<String>() {
        override fun getView(context: Context, type: Int): View {
            return context.inflate { R.layout.item_order_info }
//            return when (type) {
//                0 -> context.inflate { R.layout.item_order_info }
//                else -> context.inflate { R.layout.item_order_info }
//            }
        }

        override fun onNotify(v: View, index: Int, data: String) {

        }

//        override fun getItemViewType(position: Int): Int {
//            return currentPosition(position).type
//        }
    }.notifyDataSetChanged(listOf("","","","",""))
}
