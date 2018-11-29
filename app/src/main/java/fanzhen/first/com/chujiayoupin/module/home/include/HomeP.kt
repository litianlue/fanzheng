package fanzhen.first.com.chujiayoupin.module.home.include

import android.content.Context
import android.view.View
import com.dyl.base_lib.base.BpAdapter
import com.dyl.base_lib.base.vertical
import com.ppx.kotlin.utils.inject.inflate
import fanzhen.first.com.chujiayoupin.R
import fanzhen.first.com.chujiayoupin.module.home.ui.GoodDetailActivity
import fanzhen.first.com.chujiayoupin.module.home.ui.HomeView
import kotlinx.android.synthetic.main.home_item.view.*
import kotlinx.android.synthetic.main.layout_home.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick

inline fun HomeView.initRv(){
    v!!.brv.vertical()
    v!!.brv.onPullNext {
        list.remove(list.size-1)
        v!!.brv.reset()
    }
    v!!.brv.onPullLoad {
        list.remove(list.size-1)
        v!!.brv.reset()
    }
     v!!.brv.adapter = object : BpAdapter<Any>() {
         override fun getView(context: Context, type: Int): View {
             return when (type) {
                 0 -> context.inflate { R.layout.home_item }
                 1-> context.inflate { R.layout.last_item }
                 else -> context.inflate { R.layout.empty_item }
             }
         }
         override fun onNotify(v: View, index: Int, data: Any) {
             super.onNotify(v, index, data)
             when (getItemViewType(index)) {
                 0 -> {
                    v.img.onClick {
                        startActivity<GoodDetailActivity>()
                    }
                 }
                 1->{ //商家加盟

                 }
                 2 -> {//没有更多显示

                 }

             }
         }
         override fun getItemViewType(position: Int): Int {
             return when (currentPosition(position)) {
                 is String -> 0
                 is Integer -> 1
                 else -> 2
             }
         }

     }
}