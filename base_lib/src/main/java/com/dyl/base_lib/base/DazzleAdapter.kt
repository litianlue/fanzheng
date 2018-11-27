package com.dyl.base_lib.base

import android.content.Context
import android.graphics.Color
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import com.dyl.base_lib.R
import com.dyl.base_lib.img.loadImage
import com.dyl.base_lib.view.getADrawable
import com.ppx.kotlin.utils.inject.inflate
import kotlinx.android.synthetic.main.item_no_find_data.view.*
import org.jetbrains.anko.*
import org.w3c.dom.Text

/**
 * Created by dengyulin on 2018/6/22.
 */
fun RecyclerView.dazzle(call: DazzleAdapter.() -> Unit) {
    val dazzle = DazzleAdapter(this)
    call.invoke(dazzle)
    val adapter = object : BpAdapter<Any>() {
        override fun getView(context: Context, type: Int): View {
            dazzle._createViews.keys.forEachIndexed { index, clazz ->
                if (index == type) {
                    return dazzle.performItemView(context, clazz)
                }
            }
            return View(context)
        }

        override fun onNotify(v: View, index: Int, data: Any) {
            dazzle.performNotifyView(v, index, data)
        }

        override fun getItemViewType(position: Int): Int {
            dazzle._createViews.keys.forEachIndexed { t, x ->
                if (x == currentPosition(position)::class.java) {
                    return t
                }
            }
            return 0
        }
    }
    this@dazzle.adapter = adapter
    if (layoutManager is GridLayoutManager) {
        (layoutManager as GridLayoutManager).spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return dazzle.line[adapter.currentPosition(position)::class.java] ?: 1
            }
        }
    }
}

class DazzleAdapter(val rcy: RecyclerView) {
    data class NullDataItem(
            val hint: String = "暂无数据",
            val src: Int = -1
    )

    val _createViews: LinkedHashMap<Class<*>, Pair<Any?, Any?>> by lazy {
        linkedMapOf<Class<*>, Pair<Any?, Any?>>()
    }
    val line: HashMap<Class<*>, Int> by lazy {
        hashMapOf<Class<*>, Int>()
    }

    fun nullTextView() {
        createItemView<NullDataItem> {
            TextView(it).apply {
                width = rcy.measuredWidth
                height = rcy.measuredHeight
                textColor = Color.parseColor("#333333")
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, 30f)
                gravity = Gravity.CENTER
            }
        }
        notifyItemView<NullDataItem> { item, index, data -> (item as TextView).text = data.hint }
    }

    fun nullView() {
        createItemView<NullDataItem> {
            it.inflate { R.layout.item_no_find_data }.apply {
                layoutParams=RecyclerView.LayoutParams(rcy.measuredWidth,rcy.measuredHeight)
            }

        }
        notifyItemView<NullDataItem> { item, index, data ->
            item.apply {
                no_find_data_message.text = data.hint
                no_find_data_pic.imageResource=data.src
            }
        }
    }

    fun vertical() {
        rcy.layoutManager = LinearLayoutManager(rcy.context, LinearLayoutManager.VERTICAL, false)
    }

    fun horizontal() {
        rcy.layoutManager = LinearLayoutManager(rcy.context, LinearLayoutManager.HORIZONTAL, false)
    }

    fun invalid_offset() {
        rcy.layoutManager = LinearLayoutManager(rcy.context, LinearLayoutManager.INVALID_OFFSET, false)
    }

    fun vertical(spanCount: Int) {
        rcy.layoutManager = GridLayoutManager(rcy.context, spanCount, GridLayoutManager.VERTICAL, false)
    }

    fun horizontal(spanCount: Int) {
        rcy.layoutManager = GridLayoutManager(rcy.context, spanCount, GridLayoutManager.HORIZONTAL, false)
    }

    fun invalid_offset(spanCount: Int) {
        rcy.layoutManager = GridLayoutManager(rcy.context, spanCount, GridLayoutManager.INVALID_OFFSET, false)
    }

    fun isPager() {
        PagerSnapHelper().attachToRecyclerView(rcy)
    }

    fun <P> performItemView(context: Context, data: Class<P>): View {
        if (_createViews.containsKey(data)) {
            return (_createViews[data]?.first as DazzleAdapter.(Context) -> View).invoke(this@DazzleAdapter, context)
        }
        return TextView(context).apply {
            width = rcy.measuredWidth
            height = rcy.measuredHeight
            textColor = Color.BLACK
            setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20f)
            gravity = Gravity.CENTER
            text = "数据格式有误，请重新设置"
        }
    }

    fun <P : Any> performNotifyView(item: View, index: Int, data: P) {
        if (_createViews.containsKey(data::class.java)) {
            if (_createViews[data::class.java]?.second != null) {
                (_createViews[data::class.java]?.second as DazzleAdapter.(View, Int, P) -> Unit).invoke(this@DazzleAdapter, item, index, data)
            }
        }
    }

    fun <P : Any> spanLine(data: Class<P>): Int {
        return line[data] ?: 1
    }

    inline fun <reified P> createItemView(spanLine: Int = 1, noinline result: DazzleAdapter.(context: Context) -> View) {
        line.put(P::class.java, spanLine)
        if (_createViews.containsKey(P::class.java)) {
            _createViews.put(P::class.java, Pair(result, _createViews[P::class.java]?.second))
            return
        }
        _createViews.put(P::class.java, Pair(result, null))
    }


    inline fun <reified P> notifyItemView(noinline result: DazzleAdapter.(item: View, index: Int, data: P) -> Unit) {
        if (_createViews.containsKey(P::class.java)) {
            _createViews.put(P::class.java, Pair(_createViews[P::class.java]?.first, result))
            return
        }
        _createViews.put(P::class.java, Pair(null, result))
    }

    fun notifyDataSetChanged() {
        rcy.toBpAdapter<Any>().notifyDataSetChanged()
    }

    fun notifyDataSetChanged(list: Collection<Any>) {
        rcy.toBpAdapter<Any>().notifyDataSetChanged(list)
    }

    fun addListAndNotifyData(list: Collection<Any>) {
        rcy.toBpAdapter<Any>().addListAndNotifyData(list)
    }

    fun addAndNotifyData(t: Any, index: Int = 0) {
        rcy.toBpAdapter<Any>().addAndNotifyData(t, index)
    }

    fun rmAndNotifyData(index: Int = 0) {
        rcy.toBpAdapter<Any>().rmAndNotifyData(index)
    }

    fun moveAndNotifyData(from: Int, to: Int) {
        rcy.toBpAdapter<Any>().moveAndNotifyData(from, to)
    }

    fun changeAndNotifyData(t: Any, index: Int = 0) {
        rcy.toBpAdapter<Any>().changeAndNotifyData(t, index)
    }

    fun changeAndNotifyData(index: Int = 0) {
        rcy.toBpAdapter<Any>().notifyItemChanged(index)
    }
}

fun RecyclerView.nullView(str: String, picId: Int = -1) {
    toBpAdapter<Any>().notifyDataSetChanged(listOf(DazzleAdapter.NullDataItem(str, picId)))
}