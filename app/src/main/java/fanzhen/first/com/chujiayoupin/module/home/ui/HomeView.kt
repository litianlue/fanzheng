package fanzhen.first.com.chujiayoupin.module.home.ui
import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.os.Build
import android.support.v4.content.ContextCompat.checkSelfPermission
import android.widget.Toast
import com.dyl.base_lib.base.BaseView
import com.dyl.base_lib.base.toBpAdapter
import com.dyl.base_lib.util.ToastUtil
import com.dyl.base_lib.util.reqPermission
import fanzhen.first.com.chujiayoupin.Manifest
import fanzhen.first.com.chujiayoupin.R
import fanzhen.first.com.chujiayoupin.module.home.include.initRv
import fanzhen.first.com.chujiayoupin.module.home.include.locaTion
import kotlinx.android.synthetic.main.layout_home.view.*
import org.jetbrains.anko.locationManager


/**
 * Created by dengyulin on 2018/5/3.
 */
class HomeView(context: Context) : BaseView(context) {
    var list = mutableListOf<Any>()
    var provider:String?=null
    var locationManager:LocationManager? = null
    var listlocation= mutableListOf<String>()
    override fun initData() {
        v!!.brv.toBpAdapter<Any>().notifyDataSetChanged(list)
    }
    @SuppressLint("MissingPermission")
    override fun initView(){
        initContentView(R.layout.layout_home).apply {
            initRv()
            list.add("ssd")
            list.add("ds")
            list.add("dsf")
            list.add(1)
        }
        locaTion()



    }



    companion object {
        var shopNo:String=""
    }

}