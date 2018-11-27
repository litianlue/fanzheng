package com.dyl.base_lib.base.slide


import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import com.dyl.base_lib.R
import com.dyl.base_lib.util.NetworkUtil
import com.dyl.base_lib.view.show
import kotlinx.android.synthetic.main.action_bar_activity_layout.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick


/**
 * @author lihong
 * @since 2016/10/28
 */
open class ActionBarActivity : SlideBackActivity() {

    var base_group: ViewGroup? = null
    override fun setContentView(layoutResID: Int) {
        setContentView(LayoutInflater.from(this).inflate(layoutResID, null))
    }

    open fun initTitle(str: String) {
        base_group?.title_back?.show = true
        base_group?.base_title_back?.onClick {
            this@ActionBarActivity.finish()
        }
        base_group?.base_title_name?.text = str
    }

    override fun setContentView(view: View) {
        base_group = LayoutInflater.from(this).inflate(R.layout.action_bar_activity_layout, null) as ViewGroup

         /* if(!NetworkUtil.isNetworkConnected(this@ActionBarActivity)){
            base_group?.error?.show  = true
            base_group?.error?.onClick {
                this@ActionBarActivity.finish()
                var intent  = Intent(this@ActionBarActivity,this@ActionBarActivity::class.java)
                startActivity(intent)
            }
        }*/
        val params = RelativeLayout.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
        params.addRule(RelativeLayout.BELOW, R.id.title_back)
        base_group?.addView(view, 1, params)
        super.setContentView(base_group)
    }

    fun showBackButton(show: Boolean) {
        findViewById<View>(R.id.left_back_image_view).visibility = if (show) View.VISIBLE else View.INVISIBLE
    }

}
