package com.dyl.base_lib.util.pic

import android.app.Activity.RESULT_OK
import android.content.Context
import android.net.Uri
import android.widget.PopupWindow
import com.dyl.base_lib.R
import com.dyl.base_lib.base.BaseActivity
import com.dyl.base_lib.base.BaseView
import com.dyl.base_lib.view.getAColor
import com.vondear.rxtool.RxPhotoTool
import com.yalantis.ucrop.UCrop
import com.yalantis.ucrop.UCropActivity
import kotlinx.android.synthetic.main.select_pic.view.*
import org.jetbrains.anko.sdk25.coroutines.onClick
import java.io.File
import java.text.SimpleDateFormat
import java.util.*


class SelectHeadPicDialog(context: Context) : BaseView(context) {
    var dialog: PopupWindow? = null
    val act: BaseActivity = context as BaseActivity
    override fun initData() {
    }

    override fun initView() {

        initContentView(R.layout.select_pic) {
            select_cancel.onClick {
                dialog?.dismiss()
            }
            select_pick.onClick {
                RxPhotoTool.openLocalImage(act)
//                dialog?.dismiss()
            }
            select_take.onClick {
                RxPhotoTool.openCameraImage(act)
//                dialog?.dismiss()
            }

        }

    }


}