package com.dyl.base_lib.base

import android.content.Context
import android.content.Intent
import android.content.res.Resources
import android.graphics.Color
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.app.AppCompatDialog
import android.util.TypedValue
import android.view.*
import android.widget.FrameLayout
import android.widget.ImageView
import com.bugtags.library.Bugtags
import com.dyl.base_lib.BuildConfig
import com.dyl.base_lib.R
import com.dyl.base_lib.data.cache.Cache
import com.dyl.base_lib.data.sp.getSpData
import com.dyl.base_lib.data.sp.hasSpData
import com.dyl.base_lib.event.register
import com.dyl.base_lib.event.unRegister
import com.dyl.base_lib.img.loadImage
import com.dyl.base_lib.util.isNotNull
import com.dyl.base_lib.view.showLoadDialog
import com.dyl.base_lib.view.width
import com.ppx.kotlin.utils.inject.inflate
import kotlinx.android.synthetic.main.base_dialog.view.*
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.jetbrains.anko.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import android.view.MotionEvent
import android.view.inputmethod.InputMethodManager
import com.dyl.base_lib.base.slide.ActionBarActivity

/**
 * Created by dengyulin on 2018/4/2.
 */
abstract class BaseActivity : ActionBarActivity() {
    val ISLOGIN="isLogin"
    val onKeyListener = mutableListOf<(keyCode: Int, event: KeyEvent) -> Boolean>()
    var dialog: AppCompatDialog? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initWindow()
        this::class.java.declaredMethods.forEach {
            if (it.getAnnotation(Subscribe::class.java) != null) {
                if (!EventBus.getDefault().isRegistered(this)) {
                    register()
                }
                return@forEach
            }
        }

        initView()
        initData()
    }
    fun hideKeyBord() {
        (getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager).hideSoftInputFromWindow(this?.currentFocus?.windowToken, InputMethodManager.HIDE_NOT_ALWAYS);
    }
    override fun getResources(): Resources {
        val dis = super.getResources().displayMetrics
        if (dis.density != dis.widthPixels / (BuildConfig.DisplaySize.toFloatOrNull() ?: dis.density)) {
           return initDisplaySize(super.getResources(),super.getResources()::class.java)
        }
        return super.getResources()
    }

    open fun initWindow() {

        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN)
    }

    abstract fun initData()

    abstract fun initView()

    override fun onDestroy() {
        super.onDestroy()
        unRegister()

    }
    fun initTitle(str: String, right: String = "", call: ((View) -> Unit)? = null) {
        val v = findViewById<ViewGroup>(android.R.id.content)
        v.addView(FrameLayout(this).apply {
            layoutParams = ViewGroup.LayoutParams(context.width, dip(80))
            backgroundColorResource = R.color.main_color
            imageView {
                layoutParams = FrameLayout.LayoutParams(dip(80), dip(80)).apply {
                    leftMargin = dip(20)
                }
                padding = dip(20)
                loadImage(R.drawable.back_w)
                scaleType = ImageView.ScaleType.CENTER
                onClick { finish() }

            }
            textView(str) {
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, 28f)
                textColor = Color.parseColor("#ffffff")
                gravity = Gravity.CENTER
                layoutParams = FrameLayout.LayoutParams(dip(300), dip(80)).apply {
                    gravity = Gravity.CENTER
                }
            }
            textView(right) {
                setTextSize(TypedValue.COMPLEX_UNIT_DIP, 24f)
                textColor = Color.parseColor("#ffffff")
                setOnClickListener(call)
                gravity = Gravity.CENTER_VERTICAL
                layoutParams = FrameLayout.LayoutParams(wrapContent, dip(80)).apply {
                    rightMargin = dip(30)
                    gravity = Gravity.RIGHT
                }
            }
        }, 0)
        v.getChildAt(1).apply {
            (layoutParams as FrameLayout.LayoutParams).topMargin = dip(80)
        }
    }
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        var boolean = false
        onKeyListener.forEach {
            if (it.invoke(keyCode, event!!)) return true
        }
        return super.onKeyDown(keyCode, event) || boolean
    }

    fun onKeyDown(call: (keyCode: Int, event: KeyEvent) -> Boolean) {
        onKeyListener.add(call)
    }

    fun removeOnKeyDown(call: (keyCode: Int, event: KeyEvent) -> Boolean) {
        onKeyListener.remove(call)
    }

    inline fun <reified T : BaseActivity> startActivity() {
        startActivity<T>(null)
    }

    inline fun <reified T : BaseActivity> startActivity(p: Any?) {
        if (p != null) {
            Cache.putCache("${this::class.java.name}to${T::class.java.name}-data", p)
        }
        startActivity(Intent(this, T::class.java))
    }

    inline fun <reified T, P> getData(): P =
            Cache.getCache("${T::class.java.name}to${this::class.java.name}-data")

    inline fun <reified T, P> popData():P =
            Cache.popCache("${T::class.java.name}to${this::class.java.name}-data")


    inline fun <P> popAllData(): P? =
            Cache.popContainsCache("to${this::class.java.name}-data")


    fun showDialog() {
        if(!isFinishing){
            if (dialog == null || dialog?.isShowing == false) {
                inflate { R.layout.base_dialog }.apply {
                    dialog = showLoadDialog { }
                    load_view.anim?.start()
                    dialog?.setOnDismissListener { load_view.anim?.cancel() }
                }
            }
        }
    }

    fun dissmissDialog() {
        dialog?.dismiss()
    }
    fun isLogin():Boolean{
        return hasSpData(ISLOGIN)&&getSpData(ISLOGIN,false)&&hasSpData(Cache.COOKIE)&&getSpData(Cache.COOKIE,"").isNotNull()
    }

    override fun onResume() {
        super.onResume()
        //注：回调 1
        Bugtags.onResume(this)
    }

    override fun onPause() {
        super.onPause()
        //注：回调 2
        Bugtags.onPause(this)
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        //注：回调 3
        Bugtags.onDispatchTouchEvent(this, event)
        return super.dispatchTouchEvent(event)
    }
}
