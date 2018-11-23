package fanzhen.first.com.fanzhen.module.main

import android.content.Context
import android.graphics.Color
import android.view.View
import com.dyl.base_lib.base.BpAdapter
import com.dyl.base_lib.base.vertical
import com.ppx.kotlin.utils.inject.inflate
import fanzhen.first.com.fanzhen.R
import fanzhen.first.com.fanzhen.external.BaseActivity
import kotlinx.android.synthetic.main.mytest.*
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.MotionEvent
import android.widget.LinearLayout
import com.dyl.base_lib.show.toast.toast
import com.dyl.base_lib.view.SwipeItemLayout




class MyTestActivity : BaseActivity(){
    override fun initData() {

    }

    override fun initView() {
       setContentView(R.layout.mytest)

        isSwipeAnyWhere = true
        recyclerview.vertical()
        recyclerview.onPullLoad { reset() }
        recyclerview.onPullNext { reset() }
      //  load(ConnectApi.VerifyPhone::class.java).sendSmsCode(RSmsCode("18476298376","10001","200007")).call {  }
        recyclerview.adapter  = object: BpAdapter<String>() {
            override fun getView(context: Context, type: Int): View {
                return inflate { R.layout.item_layout }
            }

            override fun onNotify(v: View, index: Int, data: String) {
                super.onNotify(v, index, data)
                v.setOnClickListener {
                    toast("slfsl")
                }
            }
        }.notifyDataSetChanged(listOf("","","","",""))
        recyclerview.layoutManager = LinearLayoutManager(this)
        recyclerview.addOnItemTouchListener(object : SwipeItemLayout.OnSwipeItemTouchListener(this) {})
        recyclerview.addItemDecoration(object :DividerItemDecoration(this,LinearLayoutManager.VERTICAL){  })

    }


}