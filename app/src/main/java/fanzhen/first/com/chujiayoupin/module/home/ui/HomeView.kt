package fanzhen.first.com.chujiayoupin.module.home.ui
import android.content.Context
import com.dyl.base_lib.base.BaseView
import com.dyl.base_lib.base.toBpAdapter
import fanzhen.first.com.chujiayoupin.R
import fanzhen.first.com.chujiayoupin.module.home.include.initRv
import kotlinx.android.synthetic.main.layout_home.view.*


/**
 * Created by dengyulin on 2018/5/3.
 */
class HomeView(context: Context) : BaseView(context) {
    var list = mutableListOf<Any>()

    override fun initData() {
        v!!.brv.toBpAdapter<Any>().notifyDataSetChanged(list)
    }
    override fun initView(){
        initContentView(R.layout.layout_home).apply {
            initRv()
            list.add("ssd")
            list.add("ds")
            list.add("dsf")
            list.add(1)

        }


    }



    companion object {
        var shopNo:String=""
    }

}