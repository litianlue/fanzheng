package fanzhen.first.com.fanzhen.module.main

import com.dyl.base_lib.base.addView
import com.ppx.kotlin.utils.inject.notifyAny
import com.ppx.kotlin.utils.inject.radio
import fanzhen.first.com.fanzhen.R
import kotlinx.android.synthetic.main.activity_main.*
import org.jetbrains.anko.imageResource
/**
 * Created by li on 2018/8/13.
 */
inline fun MainActivity.initBottom(){
    main.apply {
        home.radio({
            iv_home.imageResource= R.mipmap.main_home
        }, {
            iv_home.imageResource= R.mipmap.main_home_select
            this@initBottom.fl_content.removeAllViews()
            this@initBottom.fl_content.addView(homeView)
        })
        live.radio ({
            iv_live.imageResource= R.mipmap.live
        }, {
            iv_live.imageResource= R.mipmap.live_select
            this@initBottom.fl_content.removeAllViews()
            this@initBottom.fl_content.addView(liveView)
        })
        info.radio ({
            iv_info.imageResource= R.mipmap.info
        }, {
            iv_info.imageResource= R.mipmap.info_select
            this@initBottom.fl_content.removeAllViews()
            this@initBottom.fl_content.addView(infoView)
        })
        mshopping.radio ({
            miv_shopping.imageResource= R.mipmap.main_shopping
        }, {
            miv_shopping.imageResource= R.mipmap.main_shopping_select
            this@initBottom.fl_content.removeAllViews()
            this@initBottom.fl_content.addView(shoppingView)
        })

       /* shopping.radio ({
            iv_shopping.imageResource= R.mipmap.main_shopping
        }, {
           // this@initBottom.checkLoginStartActivity<MainActivity> {
                iv_shopping.imageResource= R.mipmap.main_shopping_select
                this@initBottom.fl_content.removeAllViews()
                this@initBottom.fl_content.addView(shoppingView)
           // }
        })*/
        my.radio ({
            iv_my.imageResource= R.mipmap.main_my
        }, {
           // this@initBottom.checkLoginStartActivity<MainActivity> {
                iv_my.imageResource= R.mipmap.main_my_select
                this@initBottom.fl_content.removeAllViews()
                this@initBottom.fl_content.addView(myView)
           // }
        })
    }.notifyAny(0)
}