package fanzhen.first.com.chujiayoupin.module.my.include

import android.support.v4.view.PagerAdapter
import android.view.View
import android.view.ViewGroup
import fanzhen.first.com.chujiayoupin.module.my.ui.MyOrderActivity
import kotlinx.android.synthetic.main.activity_my_order.*
import org.jetbrains.anko.support.v4.onPageChangeListener

/**
 * Created by li on 2018/8/13.
 */
inline fun MyOrderActivity.initViewPager(){
    viewpager.adapter = object : PagerAdapter(){
        override fun isViewFromObject(view: View, `object`: Any): Boolean {
            return view == `object`
        }

        override fun getCount(): Int {
                return itemView.size
        }

        override fun getPageTitle(position: Int): CharSequence? {
            return tabs[position]
        }
        override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
            container.removeView(itemView[position].getView())
        }

        override fun instantiateItem(container: ViewGroup, position: Int): Any {
            container.addView(itemView[position].getView())
            return itemView[position].getView()
        }
    }
//    viewpager.currentItem = mygroup!!.type
    viewpager.onPageChangeListener {
        onPageScrolled { pos, fl, pix ->
            if(pos==0){
                isSwipeAnyWhere =true
                if(lastPix==0&&pix==0) {
                    isSwipeAnyWhere = true
                }
            }else{
                isSwipeAnyWhere = false
            }
            lastPix  = pix
        }
    }
}