package fanzhen.first.com.chujiayoupin.module.home.ui

import android.webkit.WebSettings
import fanzhen.first.com.chujiayoupin.R
import fanzhen.first.com.chujiayoupin.external.BaseActivity
import fanzhen.first.com.chujiayoupin.module.home.include.initVp
import fanzhen.first.com.chujiayoupin.module.home.include.initWB
import fanzhen.first.com.chujiayoupin.module.home.include.showPop
import fanzhen.first.com.chujiayoupin.module.main.MainActivity
import kotlinx.android.synthetic.main.gooddetail_activity.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class GoodDetailActivity : BaseActivity() {
    val specView by lazy { GoodExplainView(this@GoodDetailActivity) }
    var lastPix = -1
    var adapterNowPos: Int = 0
    var allItems:Int =0
    var id: Int? = 0
    var imgList = mutableListOf<String>()
    override fun initData() {
//        webview.loadDataWithBaseURL(null, getNewContent(model?.Product?.Detail
//                ?: ""), "text/html", "utf-8", null)
    }

    override fun initView() {
        id = popAllData<Int>()
        setContentView(R.layout.gooddetail_activity)
        initTitle("商品详情")
        imgList.add("")
        imgList.add("")
        imgList.add("")
        initVp()
        initWB()

        go_home.onClick {
            startActivity<MainActivity>()
        }
        buy_tv.onClick {
            showPop(specView).apply {
                specView.close { dismiss() }
            }
        }
    }
}
