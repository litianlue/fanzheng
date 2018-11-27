package com.dyl.base_lib.base

import android.content.Context
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.PagerSnapHelper
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup
import com.dyl.base_lib.model.NTFModel
import com.ppx.kotlin.utils.inject.notifyTag
import kotlinx.coroutines.experimental.CoroutineScope
import kotlinx.coroutines.experimental.channels.NULL_VALUE

/**
 * Created by dengyulin on 2018/4/12.
 */
abstract class BpAdapter<T> : RecyclerView.Adapter<BpAdapter.Item>() {
    var list: MutableList<T> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item = Item(getView(parent!!.context, viewType))

    override fun onBindViewHolder(holder: Item, position: Int) {
//        holder.itemView.notifyData(position,currentPosition(position))
        onNotify(holder.itemView, position, currentPosition(position))
    }

    open fun onNotify(v: View, index: Int, data: T) {

    }

    override fun getItemCount(): Int = list.size

    fun currentPosition(position: Int): T = list[position]

    abstract fun getView(context: Context, type: Int): View

    class Item(itemView: View) : RecyclerView.ViewHolder(itemView)
    private inner class FootViewHolder(private val view: View) : RecyclerView.ViewHolder(view)

    fun notifyDataSetChanged(list: Collection<T>): BpAdapter<T> {
        this.list = list.toMutableList()
        notifyDataSetChanged()
        return this
    }

    fun addListAndNotifyData(list: Collection<T>): BpAdapter<T> {
        this.list.addAll(list)
        notifyDataSetChanged()
        return this
    }

    fun addAndNotifyData(t: T, index: Int = 0): BpAdapter<T> {
        list.add(index, t)
        notifyDataSetChanged()
        return this
    }

    fun rmAndNotifyData(index: Int = 0): BpAdapter<T> {
        list.removeAt(index)
        notifyDataSetChanged()
        return this
    }

    fun moveAndNotifyData(from: Int, to: Int): BpAdapter<T> {
        val t1 = list.removeAt(from)
        list.add(from, list.removeAt(to))
        list.add(to, t1)
        notifyItemMoved(from, to)
        return this
    }

    fun changeAndNotifyData(t: T, index: Int = 0): BpAdapter<T> {
        list.set(index, t)
        notifyItemChanged(index)
        return this
    }

}

fun <T> View.onNotifyData(call: suspend CoroutineScope.(index: Int, data: T) -> Unit) {
    setTag(notifyTag, NTFModel(3, call, if (getTag(notifyTag) is NTFModel) (getTag(notifyTag) as NTFModel).parameter else NULL_VALUE))
}

fun <T> RecyclerView.toBpAdapter() = adapter as BpAdapter<T>

fun RecyclerView.vertical() {
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
}

fun RecyclerView.horizontal() {
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
}

fun RecyclerView.invalid_offset() {
    layoutManager = LinearLayoutManager(context, LinearLayoutManager.INVALID_OFFSET, false)
}

fun RecyclerView.vertical(spanCount: Int) {
    layoutManager = GridLayoutManager(context, spanCount, GridLayoutManager.VERTICAL, false)
}

fun RecyclerView.horizontal(spanCount: Int) {
    layoutManager = GridLayoutManager(context, spanCount, GridLayoutManager.HORIZONTAL, false)
}

fun RecyclerView.invalid_offset(spanCount: Int) {
    layoutManager = GridLayoutManager(context, spanCount, GridLayoutManager.INVALID_OFFSET, false)
}

fun RecyclerView.isPager() {
    PagerSnapHelper().attachToRecyclerView(this)
}

fun RecyclerView.verticalVariableMananger(spanCount: Int = 1, call: (position: Int) -> Int) {
    layoutManager = GridLayoutManager(context, spanCount, GridLayoutManager.VERTICAL, false).apply {
        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return call.invoke(position)
            }
        }
    }
}

fun RecyclerView.horizontalVariableMananger(spanCount: Int = 1, call: (position: Int) -> Int) {
    layoutManager = GridLayoutManager(context, spanCount, GridLayoutManager.HORIZONTAL, false).apply {
        spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return call.invoke(position)
            }
        }
    }
}

