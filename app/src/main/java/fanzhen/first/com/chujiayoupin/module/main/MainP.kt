package fanzhen.first.com.chujiayoupin.module.main
import com.dyl.base_lib.base.addView
import com.ppx.kotlin.utils.inject.notifyAny
import com.ppx.kotlin.utils.inject.radio
import fanzhen.first.com.chujiayoupin.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.imageResource
import org.jetbrains.anko.textColorResource
/**
 * Created by li on 2018/8/13.
 */
inline fun MainActivity.initBottom(){
    main.apply {
        home.radio({
            iv_home.imageResource= R.mipmap.tab_icon_group_off
            tv_home.textColorResource= R.color.c9a9b
        }, {
            tv_home.textColorResource= R.color.g05ca
            iv_home.imageResource= R.mipmap.tab_icon_group_on
            this@initBottom.fl_content.removeAllViews()
            this@initBottom.fl_content.addView(homeView)
        })

        my.radio ({
            iv_my.imageResource= R.mipmap.tab_icon_my_off
            tv_my.textColorResource= R.color.c9a9b
        }, {
            this@initBottom.checkLoginStartActivity<MainActivity> {
                tv_my.textColorResource= R.color.g05ca
                iv_my.imageResource= R.mipmap.tab_icon_my_on
                this@initBottom.fl_content.removeAllViews()
                this@initBottom.fl_content.addView(myView)
            }
        })
    }.notifyAny(0)
}