package fanzhen.first.com.chujiayoupin.module.home.include

import android.annotation.SuppressLint
import android.content.Context
import android.location.LocationManager
import android.util.Log
import android.view.View
import com.dyl.base_lib.base.BpAdapter
import com.dyl.base_lib.base.vertical
import com.dyl.base_lib.util.ToastUtil
import com.dyl.base_lib.util.reqPermission
import com.google.gson.JsonObject
import com.ppx.kotlin.utils.inject.inflate
import fanzhen.first.com.chujiayoupin.R
import fanzhen.first.com.chujiayoupin.module.home.ui.GoodDetailActivity
import fanzhen.first.com.chujiayoupin.module.home.ui.HomeView
import kotlinx.android.synthetic.main.home_item.view.*
import kotlinx.android.synthetic.main.layout_home.view.*
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.launch
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.MalformedURLException
import java.net.URL
import org.json.JSONObject




inline fun HomeView.initRv(){
    v!!.brv.vertical()
    v!!.brv.onPullNext {
        list.remove(list.size-1)
        v!!.brv.reset()
    }
    v!!.brv.onPullLoad {
        list.remove(list.size-1)
        v!!.brv.reset()
    }
     v!!.brv.adapter = object : BpAdapter<Any>() {
         override fun getView(context: Context, type: Int): View {
             return when (type) {
                 0 -> context.inflate { R.layout.home_item }
                 1-> context.inflate { R.layout.last_item }
                 else -> context.inflate { R.layout.empty_item }
             }
         }
         override fun onNotify(v: View, index: Int, data: Any) {
             super.onNotify(v, index, data)
             when (getItemViewType(index)) {
                 0 -> {
                    v.img.onClick {
                        startActivity<GoodDetailActivity>()
                    }
                 }
                 1->{ //商家加盟

                 }
                 2 -> {//没有更多显示

                 }

             }
         }
         override fun getItemViewType(position: Int): Int {
             return when (currentPosition(position)) {
                 is String -> 0
                 is Integer -> 1
                 else -> 2
             }
         }

     }
}
@SuppressLint("MissingPermission")
inline  fun HomeView.locaTion(){
    context.reqPermission(android.Manifest.permission.WRITE_EXTERNAL_STORAGE, android.Manifest.permission.READ_EXTERNAL_STORAGE) {
        locationManager = context.getSystemService(Context.LOCATION_SERVICE) as LocationManager?
        listlocation = locationManager!!.getProviders(true)
        if (listlocation.contains(LocationManager.GPS_PROVIDER)) {
            //是否为GPS位置控制器
            provider = LocationManager.GPS_PROVIDER
        }
        else if (listlocation.contains(LocationManager.NETWORK_PROVIDER)) {
            //是否为网络位置控制器
            provider = LocationManager.NETWORK_PROVIDER
        } else {
            ToastUtil.toast(context,"请检查网络或GPS是否打开")
            return@reqPermission
        }
        val location = locationManager!!.getLastKnownLocation(provider)
        if (location != null) {
            //获取当前位置，这里只用到了经纬度
            val latitude = location.latitude.toString() + ""
            val longitude = location.longitude.toString() + ""
            val url = "http://api.map.baidu.com/geocoder/v2/?ak=pPGNKs75nVZPloDFuppTLFO3WXebPgXg&callback=renderReverse&location=$latitude,$longitude&output=json&pois=0"
            launch(CommonPool) {
                var str = GetHttpConnectionData.getData(url)
             //   Log.w("test","str="+str)
                launch(UI) {
                    str = str?.replace("renderReverse&&renderReverse", "")
                    str = str?.replace("(", "")
                    str = str?.replace(")", "")
                    val jsonObject = JSONObject(str)
                    val address = jsonObject.getJSONObject("result")
                    val city = address.getString("formatted_address")
                    val district = address.getString("sematic_description")
                    var addressComponent = address.getString("addressComponent")
                    val json =  JSONObject(addressComponent)
                    val addressC = json.getString("district")

                     v!!.tab_layout.text= addressC

                }
            }

        }
    }
}

class GetHttpConnectionData {

    internal var str: String? = null//网路请求往回的数据

    companion object {
        fun getData(url: String): String? {//url网路请求的网址
            var u: URL? = null
            try {
                u = URL(url)
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            }

            var hc: HttpURLConnection? = null
            var inputStream: InputStream? = null
            var sb: StringBuffer? = null
            var br: BufferedReader? = null
            try {
                hc = u!!.openConnection() as HttpURLConnection
                hc!!.setRequestMethod("GET")
                inputStream = hc!!.getInputStream()
                sb = StringBuffer()
                br = BufferedReader(InputStreamReader(inputStream))
                var len: String? = null

//                while ((len == br!!.readLine())!=null) {
//                    sb.append(len)
//                }
                sb.append(br!!.readLine())
                return sb.toString()
            } catch (e: Exception) {
                e.printStackTrace()
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream!!.close()
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }

                }
            }
            return null
        }
    }
}