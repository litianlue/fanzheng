package com.dyl.base_lib.base

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.View
import android.view.ViewGroup

/**
 * Created by dengyulin on 2018/4/12.
 */
abstract class BVAdapter<T> : RecyclerView.Adapter<BVAdapter.Item>() {
    var list: MutableList<T> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Item = Item(getView(parent!!.context, viewType))

    override fun onBindViewHolder(holder: Item, position: Int) {
        onNotify(holder.itemView,position,currentPosition(position))
    }
    open fun onNotify(v:View,index:Int,data:T){

    }
    override fun getItemCount(): Int = list.size

    fun currentPosition(position: Int): T = list[position]

    abstract fun getView(context: Context, type: Int): BaseView

    class Item(itemView: BaseView) : RecyclerView.ViewHolder(itemView.getView())

    fun notifyDataSetChanged(list: Collection<T>): BVAdapter<T> {
        this.list = list.toMutableList()
        notifyDataSetChanged()
        return this
    }

    fun addListAndNotifyData(list: Collection<T>): BVAdapter<T> {
        this.list.addAll(list)
        notifyDataSetChanged()
        return this
    }

    fun addAndNotifyData(t: T, index: Int = 0): BVAdapter<T> {
        list.add(index, t)
        notifyDataSetChanged()
        return this
    }

    fun rmAndNotifyData(index: Int = 0): BVAdapter<T> {
        list.removeAt(index)
        notifyDataSetChanged()
        return this
    }

    fun moveAndNotifyData(from: Int, to: Int): BVAdapter<T> {
        val t1 = list.removeAt(from)
        list.add(from, list.removeAt(to))
        list.add(to, t1)
        notifyItemMoved(from, to)
        return this
    }

    fun changeAndNotifyData(t: T, index: Int = 0): BVAdapter<T> {
        list.set(index, t)
        notifyItemChanged(index)
        return this
    }

}
