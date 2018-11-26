package com.first.chujiayoupin.model

import com.dyl.base_lib.model.ReqModel
import fanzhen.first.com.chujiayoupin.module.home.HomeView.Companion.shopNo


data class HFRequstModel(
        val ProductId: String,
        val SpecId: String,
        val AddressId:String,
        val IsApp:String
) : ReqModel()
data class RGoodsRequstModel(
        val Id: String = "",
        val ShopNo: String = shopNo
) : ReqModel()

data class RHome(
        val ShopNo: String = shopNo
)

data class RGoodsSpecModel(
        val Specs: List<Spec> = listOf()
) {
    data class Spec(
            val Id: String = "",
            val ImgUrl: String = "",
            val SpecValue: String = "",
            val SpecValue2: String = "",
            val RealStock: String = "",
            val Price: Int = 0,
            val SpikePrice: Int = 0,
            val MarketPrice: Int = 0
    )
}


data class ROrderConfirm(
        val SpecId: String = "",
        var Num: String = "",
        val ShopNo: String = "",
        val SpikeId: String = "",
        var CouponId: String = "",
        var AddressId: String = "",
        var GiftId: String = "",
        var GActivityId: Int = 0,
        var GroupBuyId: Int = 0
)

data class RLogin(
        val UserName: String,
        val provider: String = "weixinapp"

) : ReqModel()

data class RAuth(
        val PreserveSession: Boolean = false
) : ReqModel()

data class RSearchRequstModel(
        val ShopNo: String = "",
        var CategoryId: String = "",
        var ChildCategoryId: String = "",
        var BrandId: String = "",
        var BrandIdIn: List<Int> = listOf(),
        var Keywords: String = "",
        val IdIn: String = "",
        var Cates: String = "",
        var CateIds: String = "",
        val Brands: String = "",
        var OrderString: String = "",
        val Newproducts: String = "",
        var ProductModuleId: String = ""
) : ReqModel()

data class RMyOrderModel(
        val OrderStateParam: String = "",
        val Keywords: String = ""
) : ReqModel()

data class RAddressDeleteOrSetDef(
        val Id: String = ""
) : ReqModel()


data class RAddressNew(
        val Id: String = "",
        val Name: String = "",
        val Phone: String = "",
        val Province: String = "",
        val City: String = "",
        val Area: String = "",
        val Address: String = ""
)

data class RFavorite(
        val ProductId: String = "",
        val ShopNo: String = "",
        val ProductName: String = "",
        val FavoriteType: String = ""
)
data class RAddToCar(
        val SpecId: String = "",
        val Num: String = "",
        val DealType: String = "",
        val Del: Boolean = false,
        val ShopNo: String = shopNo,
        val Cancel: Boolean = false,
        val Add: Boolean = false,
        val ProductId: String = "",
        val Activity: RAct = RAct()
) {
    data class RAct(
            val Id: String = "",
            val DetailId: String = "",
            val Type: String = "",
            val Used: Boolean = false,
            val Sign: String = ""
    )
}


data class RDelFavorite(
        val Ids: List<String> = listOf(),
        val DelAll: Boolean = false,
        val FavoriteType: String = ""
)

// 商品收藏=2,商品浏览记录=3,
data class RFavoriteOrTrack(val FavoriteType: Int = 2)

data class RMyOrderDetails(
        val OrderNo: String = "",
        val Pay: String = ""
) : ReqModel()

data class RMyOrderCancel(
        val OrderNo: String = "",
        val Reason: String = ""
)

data class RConfirmReceipt(
        val OrderNo: String = ""
)

data class RShoppingCart(
        val ShopNo: String = "",
        val CheckSellOut: Boolean = false
)

data class RCouponList(
        val State: String = ""
)

data class RSelectShoppingCar(
        val MemberId : Int = 0,
        val Selected : Boolean = false,
        val Ids :  List<Int> = listOf()
)

data class RBatchDelect(
        val Products : Set<String> = setOf(),
        val MemberId : Int = 0
)

data class RSmsCode(
        val Phone: String = "",
        val Type: String = "",
        val Flag: String = ""
)

data class RCheckCode(
        val Phone: String = "",
        val Type: String = "",
        val Code: String = ""
)

data class RCheckPhoneExist(
        val Phone: String = ""
)

data class RCheckRealName(
        val Phone: String = "",
        val Name: String = "",
        val IdNumber: String = "",
        val IdCardFacePic: String = "",
        val IdCardBackPic: String = ""
)
data class GoodsComments(
        val OrderNo:String = "",
        val Comments : MutableList<RepComments> = mutableListOf()
){
    data class RepComments(
            var ProductId: String ="",
            var DPStar: Int =0,
            var DPContent: String ="",
            var ImgUrls: MutableList<String> = mutableListOf(),
            var Spec: String =""
    )
}
data class BalanceParam(
        var OrderNo : String=""
)

data class WechatParam(
        var OrderNo : String="",
        var App : Boolean=true
)

data class AlpayParam(
        var OrderNo : String="",
        var App : Boolean=true
)

data class CommentList(
        var ProductId :String=""
)
data class CommentListInt(
        var ProdutId :Int
)
data class RNoobPrefectureModel(
        val isApp:Boolean=true
)
data class BankCardNo(val CardNo:String)

data class BalanceWithdraw(
        val Money:Double=0.0,
        val IdCardNo:String="",
        val BankName:String=""
)
data class RGetSaleList(
        val StateParam: Int=0,
        val Skip: Int=0,
        val Take: Int=0
)
data class MHelpInfoRecord(
        val HelpId:Int,
        val CustId:Int
)
data class HelpGoodsDetailRecord(
        val Id:Int,
        val HelpId:Int
)
data class RBalanceRecord(
        val Skip: Int=0,
        val Take: Int=0
)
data class RBalanceRecordDetail(
        val id: Int=0
)
data class RGiftProduct(
        val Id:String=""
)
data class RGetPostMoney(
        val Province:String="",
        val MemberId:String=""

)
data class RSetHint(
        val ProductId:String="",
        val Add:Boolean=true,
        val ShopNo:String=shopNo,
        val ActivityId:String=""
)
data class RReturnSale(
        val Support: RAfterSalet = RAfterSalet()
){
    data class RAfterSalet(
            var ImgUrls: MutableList<String> = mutableListOf(),
            var OrderNo:String="",
            var SupportType:Int=0,
            var ProductCount:Int= 0,
            var OrderDetailId:Int= 0,
            var RefundMoney:Double=0.0,
            var SupportDetail:String="",
            var SupportWhy:String=""
    )
}
data class RAfterSaleDetail(
        var SupportNo: String = ""
)
data class RAfterSaleExpress(
        var SupportNo: String = "",
        var ReturnExpressNo: String = "",
        var ReceivePhone: String = "",
        var Remarks: String = "",
        var ReturnExpressCompanyId: String = "",
        var ReturnExpressCompany: String = ""
)
data class RMyGroup(
        val title:String="",
        val type:Int=0
)
data class GrassMaster(
        val Id: Int = 0
)
data class Collects(
        var Id: Int = 0,
        var CollectionFlag: Int = 0
)
data class Attention(
        var Id: Int = 0,
        var AttentionFlag: Int = 0
)

data class GrassLikesRecord(
        var RecommendFlag:Int= 0,
        var TagId:Int= 0,
        var Keywords:String = ""
)

data class BargainCommitParam(
        val ProductId:Int=0,
        val ActivityBargainId:Int=0,
        val SpecId:Int=0,
        val AddressId:Int=0,
        val Price:Double=0.0
)

data class GroupBuyParam(
        var OrderNo:String=""
)

data class GroupBuyListParam (
        var CategoryId:Int=0,
        var Skip:Int=0,
        var Take:Int=0
)

open class GroupBuyListParam2(
        var Skip:Int=0,
        var Take:Int=10
)

data class GroupDetailParam(
        val ActivityId:Int=0,
        var ProductId:Int=0,
        val ShopNo:String=""
)

data class GroupBuyInfoParam(
        val GroupBuyId:Int=0
)

data class MyBragainListParam(
        val State:Int=0
)

data class MyBragainListParam2(
        val State:Int=0,
        var Skip:Int=0,
        var Take:Int=10
)

data class MineBaraginDetailParam(
        val CustomerBargainId:Int=0,
        val MemberId:Int=0
)
data class ArticleEdit(
        var Id:Int =0,
        var GrassTitle:String= "",
        var ShortTitle:String = "",
        var GrassContent:String = "",
        var ImgUrls:MutableList<String> = mutableListOf(),
        var ProductIds:MutableList<Int> = mutableListOf()
)
data class ProductSelect(
        val Keywords:String = ""
)
data class GrassLiKeRecord(
        var Id:Int = 0,
        var LikeFlag:Int = 0
)

data class BaraginGoodsDetailParam(
        val ProductId:Int=0,
        val CustomerBargainId:Int=0
)

data class BaraginGoodsDetailParam2(
        val ProductId:Int=0,
        val CustomerBargainId:Int=0,
        val Type:Int=0
)

data class MyBragainShareParam(
        val CustomerBargainId:Int=0
)
data class Article(
        val ArticleId:Int = 0
)
data class ReplyComm(
        var Id:Int = 0,
        var Comment:String = "",
        var ReplyCommentId:Int = 0
)
data class ArticleComment(
        var Id:Int = 0,
        var Comment:String = ""
)

data class GroupContinueListParam(
        val ProductId:Int=0,
        val ActivityId:Int=0
)

data class GroupOrderListParam(
        val OrderStateIn: List<Int> = listOf(),
        val OrderTypeIn: List<Int> = listOf()

)
data class ArticlePrai(
        val GrassMasterId:Int=0
)
data class Comment(
        val CommentId:Int=0
)
data class ReFreshData(
        val type:String="",
        val position:Int
)