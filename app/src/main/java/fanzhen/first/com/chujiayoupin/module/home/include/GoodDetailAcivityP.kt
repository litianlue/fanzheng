package fanzhen.first.com.chujiayoupin.module.home.include

import android.content.Context
import android.support.v4.view.PagerAdapter
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.ImageView
import com.ppx.kotlin.utils.inject.inflate
import fanzhen.first.com.chujiayoupin.R
import fanzhen.first.com.chujiayoupin.module.home.ui.GoodDetailActivity
import kotlinx.android.synthetic.main.gooddetail_activity.*
import org.jetbrains.anko.dip
import android.support.v7.widget.LinearLayoutManager
import android.view.ViewGroup
import android.webkit.WebSettings
import android.widget.PopupWindow
import com.dyl.base_lib.base.*
import com.dyl.base_lib.show.anim.animation
import com.dyl.base_lib.show.anim.scale
import com.dyl.base_lib.view.getAColor
import com.dyl.base_lib.view.width


inline fun GoodDetailActivity.initVp(){
    rv.horizontal()
    rv.isPager()
    rv.adapter = object :BpAdapter<String>(){
        override fun getView(context: Context, type: Int): View {
            return context.inflate { R.layout.good_item }
        }
        override fun onNotify(v: View, index: Int, data: String) {
            super.onNotify(v, index, data)


        }
    }
    rv.setOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            val l = recyclerView!!.layoutManager as LinearLayoutManager
            adapterNowPos = l.findFirstVisibleItemPosition()
            allItems = l.itemCount
            img_position.text =""+(adapterNowPos + 1) + "/" + allItems
            if(adapterNowPos==0){
                isSwipeAnyWhere = true
                if(lastPix==0&&dx==0) {
                    isSwipeAnyWhere = true
                }
            }else{
                isSwipeAnyWhere = false
            }
            lastPix  = dx
        }
    })

    rv.toBpAdapter<String>().notifyDataSetChanged(imgList)
}
inline fun GoodDetailActivity.initWB(){
    val webSettings = webview.settings
    webSettings.javaScriptEnabled = true
    webSettings.javaScriptCanOpenWindowsAutomatically = true
    webSettings.useWideViewPort = true// 关键点
    webSettings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
    webSettings.displayZoomControls = false
    webSettings.allowFileAccess = true // 允许访问文件
    webSettings.builtInZoomControls = false // 设置显示缩放按钮
    webSettings.setSupportZoom(true) // 支持缩放
    webSettings.loadWithOverviewMode = true
    webSettings.cacheMode = WebSettings.LOAD_DEFAULT
    webSettings.setAppCacheEnabled(true)
    webSettings.domStorageEnabled = true
    webSettings.textZoom = 200
}
//inline fun GoodDetailActivity.showPop(pop: BaseView): PopupWindow {
//    val rootview = findViewById<ViewGroup>(android.R.id.content)
//    var heig = org.jetbrains.anko.wrapContent
//    heig = dip(890)
//    val popwindow = android.widget.PopupWindow(pop.getView(), width, heig).apply {
//        animationStyle = R.style.showPopupAnimation
//        isFocusable = true
//        setBackgroundDrawable(android.graphics.drawable.ColorDrawable(getAColor(R.color.bantouming)))
//        setOnDismissListener {
//            v?.scale(0.9f, 0.9f, 1f, 1f)
//            v?.animation(0.4f, 1f, end = {
//                v?.setBackgroundResource(R.color.transparent)
//                //  (v?.parent as ViewGroup).setBackgroundResource(R.color.transparent)
//            }) {
//                val lp = window .attributes
//                lp.alpha = it
//                (context as BaseActivity).window.attributes = lp
//            }
//        }
//    }.apply { showAtLocation(rootview, android.view.Gravity.BOTTOM, 0, 0) }
//
//    v?.setBackgroundResource(R.color.white)
//    //   (v?.parent as ViewGroup).setBackgroundResource(R.color.black)
//    v?.scale(1f, 1f, 0.9f, 0.9f)
//    v?.animation(1f, 0.4f) {
//        val lp = (context as BaseActivity).window
//                .attributes
//        lp.alpha = it
//        (context as BaseActivity).window.attributes = lp
//    }
//    return popwindow
//}
