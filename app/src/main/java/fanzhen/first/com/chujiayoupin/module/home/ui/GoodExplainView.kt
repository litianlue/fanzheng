package fanzhen.first.com.chujiayoupin.module.home.ui

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.support.annotation.RequiresApi
import android.util.Log
import android.util.TypedValue
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import bolts.Bolts
import com.dyl.base_lib.base.BaseView
import com.dyl.base_lib.view.getAColor
import com.first.chujiayoupin.model.GoodsDetails
import com.ppx.kotlin.utils.inject.inflate
import com.ppx.kotlin.utils.inject.notifyAny
import com.ppx.kotlin.utils.inject.radio
import fanzhen.first.com.chujiayoupin.R
import kotlinx.android.synthetic.main.good_explain.view.*
import kotlinx.android.synthetic.main.item_goods_spec.view.*
import kotlinx.coroutines.experimental.CoroutineScope
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick

class GoodExplainView(context: Context) : BaseView(context) {
    var specString: String = ""
    var list = mutableSetOf<String>()
    var listCanCheck = mutableListOf<Boolean>()
    override fun initData() {
    }
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun initView() {
        initContentView(R.layout.good_explain)
        for (i in 1..3) {
            list.add("颜色" + i)
            if (i == 1 || i == 2)
                listCanCheck.add(true)
            else
                listCanCheck.add(false)
        }
        v?.affirm?.onClick {
            startActivity<OrderAcivity>()
        }
        v?.fll_spec_1?.removeAllViews()
        list.forEachIndexed { index, s ->
            v?.fll_spec_1?.addView(getSpecItemView1(s, listCanCheck[index]))
        }
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun GoodExplainView.getSpecItemView1(string: String, realStock: Boolean): View {
        val specItem = context.inflate { R.layout.item_goods_spec }
        specItem.tv_spec_name.text = string
        specItem.tv_spec_name.isEnabled = realStock
        specItem.isEnabled = realStock
        specItem.tv_spec_name.onClick {
            specString = specItem.tv_spec_name.text.toString()
            initSpecs()
        }
        return specItem
    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    fun GoodExplainView.initSpecs() {
        var positon = 0
        v?.fll_spec_1?.forEachChild {
            var b = listCanCheck[positon]
            it.isEnabled = b
            if (b)
                it.tv_spec_name.backgroundDrawable = context.getDrawable(R.drawable.ellipse_c)
            else
                it.tv_spec_name.backgroundDrawable = context.getDrawable(R.drawable.ellipse_c_stroke)
            it.tv_spec_name.isEnabled = b
            it.tv_spec_name.textColor = R.color.c999
            positon++
        }
        v?.fll_spec_1?.forEachChild { b ->
            if (b.tv_spec_name.text == specString) {
                b.tv_spec_name.isEnabled = true
                b.tv_spec_name.textColor = android.graphics.Color.WHITE
                b.tv_spec_name.backgroundDrawable = context.getDrawable(R.drawable.ellipse_c_select)
                b.isEnabled = true
            }
        }
    }

    fun close(call: suspend CoroutineScope.(v: android.view.View?) -> Unit) {
        // v?.close?.onClick(handler = call)
        v!!.close.onClick(handler = call)
    }
}