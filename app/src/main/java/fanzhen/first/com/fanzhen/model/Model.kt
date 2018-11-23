package fanzhen.first.com.fanzhen.model

import com.dyl.base_lib.model.RepModel

/**
 * Created by dengyulin on 2018/4/17.
 */
data class getmall(
        val Name: String = "",
        val MallId: Int = 0,
        val WxQrcodeUrl: String = "",
        val MallType: Int = 0,
        val StoreUserId: Int = 0,
        val StoreUserName: String = "",
        val ShowFrom: Boolean = false,
        val FromImg: String = ""
) : RepModel()


open class CRepModel<out T>(
    val Result:T
):RepModel()

