package com.dyl.base_lib.view

import android.content.Context
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.support.v4.view.PagerAdapter
import android.support.v4.view.ViewPager
import android.support.v7.app.AppCompatDialog
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.PopupWindow
import android.widget.TextView
import com.dyl.base_lib.R
import com.dyl.base_lib.base.BaseActivity
import com.dyl.base_lib.base.BaseView
import com.dyl.base_lib.external.address.CityPicker
import com.dyl.base_lib.show.anim.animation
import com.ppx.kotlin.utils.inject.inflate
import kotlinx.android.synthetic.main.select_address.view.*
import org.jetbrains.anko.dip
import org.jetbrains.anko.layoutInflater
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.wrapContent
import java.lang.Exception
import java.lang.reflect.Field
import java.util.*

/**
 * Created by dengyulin on 2018/3/31.
 */
var EditText.maxLength: Int
    get() = length()
    set(value) {
        filters = mutableListOf<InputFilter>().apply {
            addAll(filters)
            add(InputFilter.LengthFilter(value))
        }.toTypedArray()
    }

var View.show: Boolean
    set(value) {
        if (value) visibility = View.VISIBLE else visibility = View.GONE
    }
    get() = visibility == View.VISIBLE

var Context.width: Int
    set(value) {}
    get() {
        return resources.displayMetrics.widthPixels
    }

var Context.height: Int
    set(value) {}
    get() {
        return resources.displayMetrics.heightPixels
    }
val onTextWatcher by lazy {
    var field: Field? = null
    try {
        field = TextView::class.java.getDeclaredField("mListeners")
        field?.isAccessible = true
    } catch (e: Exception) {
    }
    return@lazy field
}

fun Context.inflateView(id: Int, rootView: ViewGroup? = null, attach: Boolean = rootView != null) =
        layoutInflater.inflate(id, rootView, attach)

fun EditText.onInputChange(call: (text: String) -> Unit) {
    if (onTextWatcher?.get(this) is ArrayList<*>) {
        (onTextWatcher?.get(this) as ArrayList<*>).clear()
    }
    addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(s: Editable?) {
        }

        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
        }

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            call(text.toString())

        }
    })
}

//中划线
fun TextView.strike() {
    paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
}

//添加金额符号
fun TextView.rmb() {
    text = "¥$text"
}

fun TextView.blod() {
    typeface = Typeface.defaultFromStyle(Typeface.BOLD)
}

fun BaseView.showDialog(w: Int = wrapContent, h: Int = wrapContent, call: (v: View) -> Unit): AppCompatDialog {
    val dialog = AppCompatDialog(context, R.style.dialog_theme)
    dialog.setContentView(getView())
    dialog.show()
    val lp = dialog.window.attributes as android.view.WindowManager.LayoutParams
    lp.width = w
    lp.height = h
    dialog.window.attributes = lp
    return dialog
}
fun BaseView.showDialogLucency(alpha:Float,w: Int = wrapContent, h: Int = wrapContent, call: (v: View) -> Unit): AppCompatDialog {
    val dialog = AppCompatDialog(context, R.style.dialog_theme)
    dialog.setContentView(getView())
    dialog.show()
    val lp = dialog.window.attributes as android.view.WindowManager.LayoutParams
    lp.width = w
    lp.height = h
    lp.alpha =alpha
    dialog.window.attributes = lp
    return dialog
}

fun BaseView.showNoCancelDialog(w: Int = wrapContent, h: Int = wrapContent, call: (v: View) -> Unit): AppCompatDialog {
    val dialog = showDialog(w, h, call)
    dialog.setCanceledOnTouchOutside(false)
    dialog.setCancelable(false)
    return dialog
}

fun View.showDialog(w: Int = wrapContent, h: Int = wrapContent, call: (v: View) -> Unit): AppCompatDialog {
    val dialog = AppCompatDialog(context, R.style.dialog_theme)
    dialog.setContentView(this)
    dialog.show()
    val lp = dialog.window.attributes as android.view.WindowManager.LayoutParams
    lp.width = w
    lp.height = h
    dialog.window.attributes = lp
    return dialog
}

fun View.showLoadDialog(w: Int = wrapContent, h: Int = wrapContent, call: (v: View) -> Unit): AppCompatDialog {
    val dialog = AppCompatDialog(context, R.style.dialog_theme)
    dialog.setContentView(this)
    dialog.setCanceledOnTouchOutside(false)
    dialog.show()
    val lp = dialog.window.attributes as android.view.WindowManager.LayoutParams
    lp.width = w
    lp.height = h
    lp.dimAmount = 0f
    dialog.window.attributes = lp
    return dialog
}

fun BaseView.showPopup(h:Int= wrapContent, call: (v: View) -> Unit): PopupWindow {
    val rootview = (context as BaseActivity).findViewById<ViewGroup>(android.R.id.content)
    var heig = wrapContent
    val popwindow = PopupWindow(getView(), context.width, h).apply {
        animationStyle = R.style.showPopupAnimation
        isFocusable = true
        setBackgroundDrawable(ColorDrawable(context.getAColor(R.color.shadow)))
        setOnDismissListener {
            v!!.animation(0.6f, 1f, end = {
                v!!.setBackgroundResource(R.color.trans)
                if (v?.parent != null) {
                    (v!!.parent as ViewGroup).setBackgroundResource(R.color.trans)
                }
            }) {
                val lp = (context as BaseActivity).window
                        .attributes
                lp.alpha = it
                (context as BaseActivity).window.attributes = lp
            }

        }
    }.apply { showAtLocation(rootview, Gravity.BOTTOM, 0, 0) }
    v!!.animation(1f, 0.6f) {
        val lp = (context as BaseActivity).window
                .attributes
        lp.alpha = it
        (context as BaseActivity).window.attributes = lp
    }
    return popwindow
}
fun View.showAnimDialog(w: Int = wrapContent, h: Int = wrapContent, anim: Int = R.style.showPopupAnimation, call: (v: View) -> Unit): AppCompatDialog {
    val dialog = AppCompatDialog(context, R.style.dialog_theme)
    dialog.setContentView(this)
    dialog.window.setWindowAnimations(anim)
    dialog.show()
    val lp = dialog.window.attributes as android.view.WindowManager.LayoutParams
    lp.width = w
    lp.height = h
    dialog.window.attributes = lp
    return dialog
}
fun View.showPopup(h:Int= wrapContent, call: (v: View) -> Unit): PopupWindow {
    val rootview = (context as BaseActivity).window.decorView
    var heig = h
    val popwindow = PopupWindow(this, context.width, heig).apply {
        animationStyle = R.style.showPopupAnimation
        isFocusable = true
        setBackgroundDrawable(ColorDrawable(context.getAColor(R.color.shadow)))
        setOnDismissListener {
            this@showPopup!!.animation(0.6f, 1f, end = {
                this@showPopup!!.setBackgroundResource(R.color.trans)
                if (this@showPopup!!.parent != null) {
                    (this@showPopup!!.parent as ViewGroup).setBackgroundResource(R.color.trans)
                }
            }) {
                val lp = (context as BaseActivity).window
                        .attributes
                lp.alpha = it
                (context as BaseActivity).window.attributes = lp
            }

        }
    }.apply {
        showAtLocation(rootview, Gravity.BOTTOM, 0, 0) }
    this@showPopup!!.animation(1f, 0.6f) {
        val lp = (context as BaseActivity).window
                .attributes
        lp.alpha = it
        (context as BaseActivity).window.attributes = lp
    }
    return popwindow
}

fun View.showAsPopup(rootview: View, w: Int = wrapContent, h: Int = wrapContent, dissmiss: () -> Unit): PopupWindow {
    val popwindow = PopupWindow(this, w, h).apply {
        animationStyle = R.style.showAsPopupAnimation
        isFocusable = true
        isOutsideTouchable=true
        setBackgroundDrawable(BitmapDrawable())
        setOnDismissListener { dissmiss.invoke() }
    }.apply {
        showAsDropDown(rootview)
    }
    return popwindow
}
fun Context.showAddressSelect(call: CityPicker.OnSelectingListener) {
    inflate { R.layout.select_address }.apply {
        val dialog = showPopup { }
        confirm.onClick {
            call.selected(true, city_select_dialog.getprovince_name(), city_select_dialog.getcity_name(), city_select_dialog.getcouny_name(), city_select_dialog.getcity_code())
            dialog.dismiss()
        }
        cancel.onClick { dialog.dismiss() }
    }

}

fun <T:View> ViewPager.initAdapter(vararg t: T,call:(position:Int)->String={_->""}) {
    adapter = object : PagerAdapter() {
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
            return t.size
        }

        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(t[position])
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(t[position])
            return t[position]!!
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return call(position)
        }
    }
}
