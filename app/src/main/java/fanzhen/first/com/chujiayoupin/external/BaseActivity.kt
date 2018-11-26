package fanzhen.first.com.chujiayoupin.external

import android.content.Context
import android.content.pm.ActivityInfo
import android.net.Uri
import android.view.ViewGroup
import com.dyl.base_lib.base.BaseActivity
import com.dyl.base_lib.net.call
import com.dyl.base_lib.net.load
import com.dyl.base_lib.util.isNotNull
import com.ppx.kotlin.utils.inject.inflate
import com.ppx.kotlin.utils.inject.luban
import fanzhen.first.com.chujiayoupin.R
import fanzhen.first.com.chujiayoupin.service.ConnectApi
import kotlinx.android.synthetic.main.title_action_bar.view.*
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import org.jetbrains.anko.dip
import org.jetbrains.anko.sdk25.coroutines.onClick
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.net.URI


/**
 * Created by dengyulin on 2018/4/28.
 */
abstract class BaseActivity : BaseActivity() {
    override fun initWindow() {
        //super.initWindow()
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
      // StatusBarUtil.setStatusBarColor(this, R.color.white)
    }
    fun initTitle(str: String) {
        val v = findViewById<ViewGroup>(android.R.id.content)
        v.addView(inflate { R.layout.title_action_bar }.apply {
            title_back.onClick { this@BaseActivity.finish() }
            title_name.text = str
        }, 0)
        v.getChildAt(1).apply {
            (layoutParams as ViewGroup.MarginLayoutParams).topMargin = dip(80)
        }
    }
}
fun Context.uploadImage(path: String = "", url: Uri? = null, infile: File? = null, next: (List<String>?) -> Unit) {
    var file: File?
    if (path.isNotNull()) {
        file = File(path)
    } else if (infile != null) {
        file = infile
    } else if (url != null) {
        file = File(URI(url.toString()))
    } else {
        throw NotImplementedError("忘记写了")
    }
    luban(file) {
        if (it == null) return@luban
        val rb = RequestBody.create(MediaType.parse("multipart/form-data"), it)
        val mb = MultipartBody.Part.createFormData("image", it.name, rb)
        load(ConnectApi.UpImage::class.java).UpLoadImage("cjyp", "IdCard", mb).call(object : Callback<MutableList<String>> {
            override fun onResponse(call: Call<MutableList<String>>?, response: Response<MutableList<String>>?) {
                next.invoke(response?.body())
            }

            override fun onFailure(call: Call<MutableList<String>>?, t: Throwable?) {
                call?.clone()?.enqueue(this)
            }

        })
    }



}