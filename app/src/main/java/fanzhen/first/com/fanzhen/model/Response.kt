package com.first.chujiayoupin.model

import android.util.Log
import android.widget.TextView
import com.dyl.base_lib.coroutines.CoroutinesInject
import com.dyl.base_lib.event.sendEvent
import com.dyl.base_lib.model.BModel
import com.dyl.base_lib.model.RepModel
import com.dyl.base_lib.util.date.TimeDateUtil
import com.dyl.base_lib.util.isNotNull
import com.dyl.base_lib.util.to2Double
import com.google.gson.annotations.SerializedName
import fanzhen.first.com.fanzhen.external.SequenceUtil
import kotlinx.coroutines.experimental.CommonPool
import kotlinx.coroutines.experimental.Job
import kotlinx.coroutines.experimental.android.UI
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch

data class CategoriesRData(
        val Result: ProductCategoryListResponse
) : RepModel()

data class ProductCategoryListResponse(
        val Categories: List<TinyProductCategory> = listOf(),//分类
        val Brands: List<TinyProductBrand> = listOf()//品牌
) : BModel()

data class TinyProductCategory(
        val Id: Int = 0,
        val CategoryName: String = "",      //品类名
        val DisplayOrder: Int = 0,          //排序 倒序
        val ParentId: Int = 0,              //是一级品类
        val PicUrl: String = "",            //品类图片
        val Description: String = "",       //描述
        val IsHot: Boolean = false,         //是否为推荐
        val Childs: List<TinyProductCategory> = listOf(),//子品
        val PosterImgUrl: String = "",
        val PosterUrl: String = ""
) : BModel()

data class TinyProductBrand(
        val Id: Int = 0,
        val BrandName: String = "",// 品牌名,
        val DisplayOrder: Int = 0,//排序,
        val PicUrl: String = "", //图片,
        val Description: String = "" //描述
) : BModel()

data class RepMember(
        val HeadUrl: String = "",
        val ShopName: String = "",
        val FavCount: String = "",
        val Id: String = "",
        val ShopNo: String = ""
)

class DetailsJob {
    companion object {
        var job: Job? = null
    }
}

data class GoodsDetails(
        val Remarks: String = "",
        val ShareImgUrl: String = "",
        val ShareUrl: String = "",
        val ActivityHelpRules: String = "",
        val Timer: Long = 0,
        val State: Int = 0,
        val CustomId: Int = 0,
        val CustomHelpId: Int = 0,
        val Product: RepProduct = RepProduct(),
        val member: RepMember = RepMember(),
        val IsMember: Boolean = false,
        var CartNum: Int = 0,
        var Favorite: Boolean = false,
        val Activities: List<RepActivities> = listOf(),
        val PostAddress: String = "",
        val ServiceDescriptions: List<RepServiceDescriptions> = listOf(),
        val KfUrl: String = "",
        val RelationProducts: List<RepRelationProduct> = listOf(),
        val notice: RepNotice = RepNotice(),
        val ShareTitle: String = "",
        val ShareLink: String = "",
        val MallDesc: String = "",
        val PostMoney: Double = 0.0,
        val BarCode: String = "",
        val STName: String = "",
        val STDesc: String = "",
        val PMFree: Boolean = false,
        val ProductGifts: List<RepProductGift> = listOf(),
        val SpikeId: Int = 0,
        var SpikeEnds: Long = 0

) {
    data class RepServiceDescriptions(
            val ServiceItems: String = "",
            val Servicedescription: String = ""
    )

    data class RepProduct(
            val Id: String = "",
            val Name: String = "",
            val SaleType: Int = 0,
            val ImgUrl: String = "",
            val ImgUrls: List<String> = listOf(),
            val Desc: String = "",
            val Description: String = "",
            val PriceTags: List<String> = listOf(),
            val TotalStock: Int = 0,
            val IsDisplay: Boolean = false,
            val Specs: List<RepSpec> = listOf(),
            val FreightTemplateName: String = "",
            val CommentPercent: Double = 0.0,
            val SellRate: Double = 0.0,
            val GroupBuyingId: Int = 0,
            val GroupBuyingPrice: Double = 0.0,
            val Price: Double = 0.0,
            val SpikePrice: Double = 0.0,
            val LimitCount: Int = 0,
            val Detail: String = "",
            val SpecTitle: String = "",
            val SpecTitle2: String = ""
    ) {
        data class RepSpec(
                val Id: String = "",
                val ImgUrl: String = "",
                val SpecValue: String = "",
                val SpecValue2: String = "",
                val RealStock: Int = 0,
                val Price: Double = 0.0,
                val SpikePrice: Double = 0.0,
                val MarketPrice: Double = 0.0,
                val AreaPrice: Double = 0.0,
                val GroupBuyPrice: Double = 0.0,
                val Num: Int = 0
        )

    }


    data class RepNotice(
            val CustomerId: String = "",
            val ProductId: String = "",
            val NoticeTime: String = "",
            val CommonNoticeId: String = "",
            val ShopNo: String = "",
            val SellTime: String = "",
            val Id: String = "",
            val AddTime: String = "",
            val Delstatus: Boolean = false
    )

    data class RepProductGift(
            val GiftName: String = "",
            val ImgUrl: String = "",
            val Price: Double = 0.0,
            val Id: String = "",
            val AddTime: String = "",
            val Delstatus: Boolean = false
    )

    data class RepActivities(
            val Id: Int = 0,
            val Name: String = "",
            val ActivityType: Int = 0,
            val TypeName: String = "",
            val ActivityDetail: String = "",
            val SubTitle: String = "",
            val IsViewSubhead: Boolean = false,
            val FullMoney: Double = 0.0,
            val ReduceMoney: Double = 0.0,
            val ActivityState: String = "",
            val BeginTime: String = "",
            val EndTime: String = "",
            val Begins: Int = 0,
            val Ends: Long = 0,
            val Countdown: Long = 0
    )


    data class RepRelationProduct(
            val Id: String = "",
            val ImgUrl: String = "",
            val Name: String = "",
            val IsDisplay: Boolean = false,
            val Price: Double = 0.0,
            val MarketPrice: Double = 0.0,
            val TotalStock: String = "",
            val BrandId: String = "",
            val PreSell: Boolean = false,
            val Spec: String = "",
            val CategoryId: String = "",
            val ChildCategoryId: String = ""
    )

    fun getPrice(): String {
        if (SpikeId != 0 && SpikeEnds > 0) {
            return "¥${Product.SpikePrice.to2Double()}"
        } else if (IsMember && (Product.SaleType == 0 || Product.SaleType == 2)) {
            var min = 0.0
            var max = 0.0
            if (Product.Specs.isNotEmpty()) {
                min = Product.Specs[0].AreaPrice
            }
            Product.Specs.forEach {
                if (min > it.AreaPrice) {
                    min = it.AreaPrice
                }
                if (max < it.AreaPrice) {
                    max = it.AreaPrice
                }
            }
            return if (min == max) "¥ ${max.to2Double()}" else "¥ ${min.to2Double()} ~ ${max.to2Double()}"
        } else {
            var min = 0.0
            var max = 0.0
            if (Product.Specs.isNotEmpty()) {
                min = Product.Specs[0].Price
            }
            Product.Specs.forEach {
                if (min > it.Price) {
                    min = it.Price
                }
                if (max < it.Price) {
                    max = it.Price
                }
            }
            return if (min == max) "¥ ${max.to2Double()}" else "¥ ${min.to2Double()} ~ ${max.to2Double()}"
        }
        return "¥ 0.00"
    }

    fun getPrice(specId: String): String {
        if (SpikeId != 0 && SpikeEnds > 0) {
            return "¥${Product.SpikePrice.to2Double()}"
        } else if (IsMember && (Product.SaleType == 0 || Product.SaleType == 2)) {
            Product.Specs.forEach {
                if (it.Id == specId) {
                    return "¥ ${it.AreaPrice.to2Double()}"
                }
            }
            return ""
        } else {
            Product.Specs.forEach {
                if (it.Id == specId) {
                    return "¥ ${it.Price.to2Double()}"
                }
            }
            return ""
        }
        return ""
    }

    fun getGroupPrice(): String {
        return "¥ ${Product.GroupBuyingPrice.to2Double()}"
    }

    fun getGroupPrice(specId: String): String {
        Product.Specs.forEach {
            if (it.Id == specId) {
                return "¥ ${it.GroupBuyPrice.to2Double()}"
            }
        }
        return ""
    }

    fun getMaxPrice(): String {
        if (SpikeId != 0 && SpikeEnds > 0) {
            var list = mutableListOf<Double>()
            if (Product.Specs.isNotEmpty()) {
                Product.Specs.forEachIndexed { index, it ->
                    list.add(it.Price)
                }
                list = SequenceUtil.BubbleSort(list)
                return list[list.size - 1].to2Double()
            } else {
                return ""
            }
        } else if (IsMember && (Product.SaleType == 0 || Product.SaleType == 2)) {
            var max = 0.0
            Product.Specs.forEach {
                if (max < it.AreaPrice) {
                    max = it.AreaPrice
                }
            }
            return "¥${max.to2Double()}"
        } else {
            var max = 0.0
            Product.Specs.forEach {
                if (max < it.Price) {
                    max = it.Price
                }
            }
            return "¥ ${max.to2Double()}"
        }
        return "¥ 0.00"
    }

    fun getMinPrice(): String {
        if (SpikeId != 0 && SpikeEnds > 0) {
            return "¥${Product.SpikePrice.to2Double()}"
        } else if (IsMember && (Product.SaleType == 0 || Product.SaleType == 2)) {
            var min = 0.0
            if (Product.Specs.isNotEmpty()) {
                min = Product.Specs[0].AreaPrice
            }
            Product.Specs.forEach {
                if (min > it.AreaPrice) {
                    min = it.AreaPrice
                }
            }
            return "${min.to2Double()}"
        } else {
            var min = 0.0
            if (Product.Specs.isNotEmpty()) {
                min = Product.Specs[0].Price
            }
            Product.Specs.forEach {
                if (min > it.Price) {
                    min = it.Price
                }
            }
            return "${min.to2Double()}"
        }
        return "0.00"
    }

    fun getMinMarketPrice(): String {
        var min = 0.0
        Product.Specs.forEach {
            if (min < it.MarketPrice) {
                min = it.MarketPrice
            }
        }

        return min.to2Double()
    }

    fun getMarketPrice(): String {
        var min = 0.0
        var max = 0.0
        if (Product.Specs.isNotEmpty()) {
            min = Product.Specs[0].MarketPrice
        }
        Product.Specs.forEach {
            if (min > it.MarketPrice) {
                min = it.MarketPrice
            }
            if (max < it.MarketPrice) {
                max = it.MarketPrice
            }
        }
        return if (min == max) "¥ ${max.to2Double()}" else "¥ ${min.to2Double()} ~ ${max.to2Double()}"
    }

    fun isMarketPrice(): Boolean {


        return Product.Specs.firstOrNull {
            it.MarketPrice > 0
        } != null
    }

    fun setSellTime(v: TextView) {
        if (DetailsJob.job != null) {
            DetailsJob.job?.cancel()
        }
        DetailsJob.job = CoroutinesInject().whileTime(mCall = {
            v.text = "距离结束 ${TimeDateUtil.long2String(SpikeEnds, TimeDateUtil.hms)}"
            SpikeEnds -= 1000
            if (SpikeEnds < 0) {
                DetailsJob.job?.cancel()
            }
        }, afterCall = {
            delay(1000)
        })

    }

    fun getMemberPrice(): String {
        if (SpikeId != 0 && SpikeEnds > 0) {
            return "¥${getSellRate(Product.SpikePrice).to2Double()}"
        } else if (IsMember && (Product.SaleType == 0 || Product.SaleType == 2)) {
            var min = 0.0
            var max = 0.0
            if (Product.Specs.isNotEmpty()) {
                min = Product.Specs[0].AreaPrice
            }
            Product.Specs.forEach {
                if (min > it.AreaPrice) {
                    min = it.AreaPrice
                }
                if (max < it.AreaPrice) {
                    max = it.AreaPrice
                }
            }
            return if (min == max) "¥ ${getSellRate(max).to2Double()}" else "¥ ${getSellRate(min).to2Double()} ~ ${getSellRate(max).to2Double()}"
        } else {
            var min = 0.0
            var max = 0.0
            if (Product.Specs.isNotEmpty()) {
                min = Product.Specs[0].Price
            }
            Product.Specs.forEach {
                if (min > it.Price) {
                    min = it.Price
                }
                if (max < it.Price) {
                    max = it.Price
                }
            }
            return if (min == max) "¥ ${getSellRate(max).to2Double()}" else "¥ ${getSellRate(min).to2Double()} ~ ${getSellRate(max).to2Double()}"
        }
        return "¥ 0.00"
    }

    fun getMinMemberPrice(): String {
        if (SpikeId != 0 && SpikeEnds > 0) {
            return "${getSellRate(Product.SpikePrice).to2Double()}"
        } else if (IsMember && (Product.SaleType == 0 || Product.SaleType == 2)) {
            var min = 0.0
            if (Product.Specs.isNotEmpty()) {
                min = Product.Specs[0].AreaPrice
            }
            Product.Specs.forEach {
                if (min > it.AreaPrice) {
                    min = it.AreaPrice
                }
            }
            return "${getSellRate(min).to2Double()}"
        } else {
            var min = 0.0
            if (Product.Specs.isNotEmpty()) {
                min = Product.Specs[0].Price
            }
            Product.Specs.forEach {
                if (min > it.Price) {
                    min = it.Price
                }
            }
            return "${getSellRate(min).to2Double()}"
        }
        return "0.00"
    }

    private fun getSellRate(price: Double): Double {
        return Product.SellRate * price / 100.0
    }
}

data class SpcesModel(
        val Specs: List<Spec> = listOf()
) {
    data class Spec(
            val Id: Int = 0,
            val ImgUrl: String = "",
            val SpecValue: String = "",
            val SpecValue2: String = "",
            val RealStock: Int = 0,
            val Price: Double = 0.0,
            val MarketPrice: Double = 0.0
    )
}

data class OrderConfirm(
        val OrderDetails: List<RepOrderDetail> = listOf(),
        val PostageMoney: Double = 0.0,
        val OrderMoney: Double = 0.0,
        val ProductMoney: Double = 0.0,
        val TogetherMoney: Double = 0.0,
        val Addresses: List<RepAddresse> = listOf(),
        val member: RepMember = RepMember(),
        val SelectAddress: RepSelectAddress = RepSelectAddress(),
        val Coupons: List<RepCoupon> = listOf(),
        val MyBalance: Double = 0.0,
        val IsMember: Boolean = false,
        val FTs: List<RepFT> = listOf()
) {
    data class RepAddresse(
            val CustomerId: String = "",
            val Name: String = "",
            val Phone: String = "",
            val Province: String = "",
            val City: String = "",
            val Area: String = "",
            val Address: String = "",
            val IsDefault: Boolean = false,
            val FullAddress: String = "",
            val Id: String = "",
            val AddTime: String = "",
            val Delstatus: Boolean = false
    ) {
        fun getFullAdress(): String {
            return if (FullAddress.isNotNull()) FullAddress else "$Province $City $Area $Address"
        }
    }

    data class RepSelectAddress(
            val CustomerId: String = "",
            val Name: String = "",
            val Phone: String = "",
            val Province: String = "",
            val City: String = "",
            val Area: String = "",
            val Address: String = "",
            val IsDefault: Boolean = false,
            val FullAddress: String = "",
            val Id: String = "",
            val AddTime: String = "",
            val Delstatus: Boolean = false
    )

    data class RepOrderDetail(
            val Id: String = "",
            val OrderNo: String = "",
            val ProductPrice: Double = 0.0,
            val MarketPrice: Double = 0.0,
            val ProductCount: Int = 0,
            val ProductName: String = "",
            val DealType: String = "",
            val Spec: String = "",
            val ImgUrl: String = "",
            val SupportNo: String = "",
            val Freight: Int = 0,
            val Limit: Int = 0,
            val STName: String = ""
    )


    data class RepFT(
            val FTId: String = "",
            val Free: Boolean = false,
            val STs: List<RepST> = listOf()
    ) {
        data class RepST(
                val Name: String = "",
                val Id: String = "",
                val Money: Int = 0,
                val Desc: String = ""
        )
    }

    data class RepCoupon(
            val Id: String = "",
            val CouponMoney: Double = 0.0,
            val FullMoney: Double = 0.0,
            val BeginTime: String = "",
            val EndTime: String = "",
            val CouponType: String = "",
            val Limit: String = "",
            val Usable: Boolean = false
    ) : BModel()
}

data class SearchModel(
        val Products: SearchModel.SProducts = SearchModel.SProducts(),
        val Categories: List<SearchModel.SCategories> = listOf(),
        val Title: String = "",
        val Member: SearchModel.SMember = SearchModel.SMember()
) {
    data class SProducts(
            val Offset: Int = 0,
            val Total: Int = 0,
            val Results: List<SPResults> = listOf()
    ) {
        data class SPResults(
                val ImgUrl: String = "",
                val Name: String = "",
                val IsDisplay: Boolean = false,
                val Price: Double = 0.0,
                val MarketPrice: Double = 0.0,
                val TotalStock: Int = 0,
                val BrandId: Int = 0,
                val CommentPercent: Double = 0.0,
                val PreSell: Boolean = false,
                val CategoryId: Int = 0,
                val ChildCategoryId: Int = 0,
                val SaleType: Int = 0,
                val Id: Int = 0,
                val PriceTags: List<String> = listOf()
        )
    }

    data class SCategories(
            val Id: Int = 12,
            val CategoryName: String = "",
            val DisplayOrder: Int = 0,
            val ParentId: Int = 0,
            val PicUrl: String = "",
            val IsHot: Boolean = false
    )

    data class SMember(
            val HeadUrl: String = "",
            val ShopName: String = "",
            val FavCount: Int = 0,
            val Id: Int = 0,
            val ShopNo: String = ""
    )
}

data class MyOrderModel(
        val Offset: Int = 0,
        val Total: Int = 0,
        val Results: List<SMyOrderResults> = listOf()
) {
    data class SMyOrderResults(
            val Id: Int = 0,
            val ReceiveName: String = "",
            val OrderMoney: String = "",
            val AddTime: String = "",
            val OrderState: Int = 0,
            val CommentState: Int = 0,
            val OrderType: Int = 0,
            val GroupBuyState: Int = 0,
            val Details: List<SMyOrderDetails> = listOf(),
            val OrderNo: String = "",
            val PayState: Int = 0,
            val IsAuthOrder: Boolean = false,
            val SendWay: Int = 0,
            val GroupBuyId: Int = 0,
            val ShopName: String = "",
            val ShopNo: String = "",
            val PostageMoney: Double = 0.0,
            val GiftId: Int = 0,
            val GiftPackageProduct: SGiftPackageProduct = SGiftPackageProduct(),
            val GroupBuy : SGroupBuy =SGroupBuy()
    ) {
        open class SMyOrderDetails(
                val Id: Int = 0,
                val ProductId: String = "",
                val OrderNo: String = "",
                val ProductPrice: Double = 0.0,
                val MarketPrice: Int = 0,
                val ProductCount: Int = 0,
                val ProductName: String = "",
                val DealType: Int = 0,
                val Spec: String = "",
                val ImgUrl: String = "",
                val Freight: Int = 0,
                val SupportNo: String = "",
                val STName: String = "",
                val SupportState: Int = 0
        ) : BModel()

        data class SGiftPackageProduct(
                val BrandId: Int = 0,
                val CategoryId: Int = 0,
                val ChildCategoryId: Int = 0,
                val Id: Int = 0,
                val Num: Int = 0,
                val Price: Double = 0.0,
                val Name: String = "",
                val ImgUrl: String = "",
                val Stock: Int = 0,
                val IsDisplay: Int = 0,
                val Preference: Int = 0,
                val ProductType: Int = 0,
                val Weight: Int = 0,
                val CompanyId: Int = 0
        ) : BModel()

        data class SGroupBuy(
                val Id: Int = 0,
                val Remain: Int = 0,
                val Ends: Long = 0,
                val CanJoin: Boolean = false,
                val ActivityId: Int = 0
        )
    }
}

data class SOrerDetails(
        val pId: String = "",
        val type: Int = 0,
        val commentState: Int = 0,
        val productSum: Int = 0,
        val state: Int = 0,
        val total: String = "",
        val orderType : Int = 0,
        val model: MyOrderModel.SMyOrderResults.SMyOrderDetails? = null,
        val modelGift: MyOrderModel.SMyOrderResults.SGiftPackageProduct? = null
)

data class SOrerDetails2(
        val pId: String = "",
        val type: Int = 0,
        val commentState: Int = 0,
        val productSum: Int = 0,
        val state: Int = 0,
        val total: String = "",
        val groupNum : Int =0,
        val model: MyOrderModel.SMyOrderResults.SMyOrderDetails? = null,
        val modelGift: MyOrderModel.SMyOrderResults.SGiftPackageProduct? = null
)

data class SOrerDetails1(
        val pId: String = "",
        val type: Int = 0,
        val addtime: String = "",
        val model: MyOrderModel.SMyOrderResults.SMyOrderDetails? = null,
        val modelGift: MyOrderModel.SMyOrderResults.SGiftPackageProduct? = null
)

data class RepCancel(
        val OrderNo: String = "",
        val state: String = ""
)

data class RepAddresses(
        val Addresses: List<OrderConfirm.RepAddresse> = listOf()
)

data class FavoriteOrTrackList(
        val Offset: String = "",
        val Total: String = "",
        val Results: List<Result> = listOf()
) {
    data class Result(
            val CustomerId: String = "",
            val MemberId: String = "",
            val ProductId: String = "",
            val ProductName: String = "",
            val ProductUrl: String = "",
            val Price: Double = 0.0,
            val VipPrice: Double = 0.0,
            val ImgUrl: String = "",
            val Name: String = "",
            val FavCount: String = "",
            val ShortTime: String = "",
            // val Act: RepAct = RepAct(),
            val Stock: String = "",
            val CustomerName: String = "",
            val GroupId: String = "",
            val Ip: String = "",
            val IsDisplay: Boolean = false,
            val PreSell: Boolean = false,
            val ShopNo: String = "",
            val FavoriteType: String = "",
            val Id: String = "",
            val AddTime: String = "",
            val Delstatus: Boolean = false
    )

}

data class GoodsCarNum(
        val CartNum: Int = 0,
        val Total: Double = 0.0
)

class HomeJob {
    companion object {
        var job: Job? = null
    }
}

data class HomeData(
        val MallName: String = "",
        val MallDescription: String = "",
        val MallLogo: String = "",
        val Shop: RepShop = RepShop(),
        val PlatformTrusts: List<RepPlatformTrust> = listOf(),
        val Categories: List<RepCategory> = listOf(),
        val NewproductsPosters: String = "",
        val CarouselInfo: List<RepCarouselInfo> = listOf(),
        val HotSearchKeyWord: String = "",
        val NewuserZoneImgUrls: List<String> = listOf(),
        val IsNewuser: Boolean = false,
        val CouDanModule: ImagesAndHrefInfo = ImagesAndHrefInfo(),
        val IsMember: Boolean = false,
        val JoinPosters: String = "",
        val HomePageCard: String = "",
        val Spike: RepSpike = RepSpike(),
        val ProductModules: List<RepProductModule> = listOf(),
        val GrassArticles: List<GrassList> = listOf()
) {
    data class RepProductModule(
            val Id: String = "",
            val Title: String = "",
            val DisplayOrder: String = "",
            val Products: List<Product> = listOf()
    ) {
        data class Product(
                val ImgUrl: String = "",
                val Name: String = "",
                val IsDisplay: Boolean = false,
                val Price: Double = 0.0,
                val MarketPrice: Double = 0.0,
                val TotalStock: Int = 0,
                val BrandId: String = "",
                val PreSell: Boolean = false,
                val Spec: String = "",
                val CategoryId: String = "",
                val ChildCategoryId: String = "",
                val Id: String = "",
                val CommentPercent: Double = 0.0,
                val PriceTags: List<String> = listOf(),
                val SpecTitle: String = "",
                val SpecTitle2: String = ""
        )
    }

    data class RepShop(
            val HeadUrl: String = "",
            val ShopName: String = "",
            val FavCount: String = "",
            val Id: String = "",
            val ShopNo: String = ""
    )

    data class RepCategory(
            val Id: String = "",
            val CategoryName: String = "",
            val DisplayOrder: String = "",
            val ParentId: String = "",
            val PicUrl: String = "",
            val Description: String = "",
            val PosterImgUrl: String = "",
            val PosterUrl: String = "",
            val IsHot: Boolean = false,
            val Childs: List<String> = listOf()
    )

    data class RepSpike(
            val Id: String = "",
            val Name: String = "",
            val ActivityType: String = "",
            val TypeName: String = "",
            val ActivityDetail: String = "",
            val SubTitle: String = "",
            val HeadPicUrl: String = "",
            val IsViewSubhead: Boolean = false,
            val FullMoney: Double = 0.0,
            val ReduceMoney: Double = 0.0,
            val ActivityState: String = "",
            val BeginTime: String = "",
            val EndTime: String = "",
            val Spikes: List<Spike> = listOf(),
            val TogetherProducts: List<TogetherProduct> = listOf(),
            val Begins: String = "",
            var Ends: Long = 0L,
            var Countdown: Long = 0L,
            val ShareContent: String = ""
    ) {
        data class Spike(
                val ProductId: String = "",
                val Price: Double = 0.0,
                val RealStock: String = "",
                val ProductName: String = "",
                val ProductImgUrl: String = "",
                val ProductPrice: Double = 0.0,
                val MarketPrice: Double = 0.0
        )

        data class TogetherProduct(
                val ProductId: String = "",
                val Price: Double = 0.0,
                val TotalStock: String = "",
                val ProductName: String = "",
                val ProductImgUrl: String = "",
                val MarketPrice: Double = 0.0
        )

        fun setSellTime(v1: TextView, v2: TextView, v3: TextView) {
            if (HomeJob.job != null) {
                HomeJob.job?.cancel()
            }
            HomeJob.job = CoroutinesInject().whileTime(mCall = {
                v1.text = TimeDateUtil.milliTohours(Countdown).toBigDecimal().toString()
                v2.text = TimeDateUtil.milliTominutes(Countdown).toBigDecimal().toString()
                v3.text = TimeDateUtil.milliToseconds(Countdown).toBigDecimal().toString()
                Countdown -= 1000
                if (Countdown < 0) {
                    HomeJob.job?.cancel()
                }
            }, afterCall = {
                delay(1000)
            })
        }
    }

    data class RepCarouselInfo(
            val ImgUrl: String = "",
            val ImgHref: String = ""
    )

    data class RepPlatformTrust(
            val Title: String = "",
            val Content: String = ""
    )
    data class GrassList(
            val Id: Int = 0,
            val GrassTitle: String = "",
            val ShortTitle: String = "",
            val GrassContent: String = "",
            val ImgUrls: List<String> = listOf(),
            val ProductIds: List<Int> = listOf(),
            var Liked: Int = 0,
            val IsRecommend: Boolean = false,
            val GrassMasterId: Int = 0,
            val ReleaseTime: String = "",
            val NickName: String = "",
            val HeadPortrait: String = "",
            val IsAttention: Int = 0,
            val IsCollection: Int = 0,
            val ShareUrl: String = "",
            val CommentsCount: Int = 0,
            val CollectionCount: Int = 0,
            val GrassMasterType: Int,
            var IsLike: Int = 0,
            val IsSelf: Boolean = false,
            val ProductList: List<GrassProducts> = listOf()

    )

    data class GrassProducts(
            val BrandId: Int,
            val BrandName: String,
            val Name: String,
            val Id: Int,
            val CanSaleStock: Int,
            val SalePrice: String,
            val Stock: Int,
            val LockStock: Int,
            val SaleMinPrice: Double,
            val SaleMaxPrice: Double,
            val ImgUrl: String,
            val ProductId: Int,
            val RecordNum: Int
    )
}

data class MyOrderDetails(
        val Order: RepOrder = RepOrder(),
        val OrderDetails: List<ROrderDetails> = listOf(),
        val NoSupport: Boolean = false,
        val ExressDetailList: List<ExressDetails> = listOf()
) {
    data class RepOrder(
            val Id: Int = 0,
            var Timer: Long = 0L,
            val AddTime: String = "",
            val PayTime: String = "",
            val SendTime: String = "",
            val ApplyCancleTime: String = "",
            val ReceivePhone: String = "",
            val ReceiveProvince: String = "",
            val ReceiveCity: String = "",
            val ReceiveArea: String = "",
            val ReceiveAddress: String = "",
            val ExpressCompanyName: String = "",
            val ExpressNo: String = "",
            val CancelTime: String = "",
            val OrderMoney: String = "",
            val ReceiveName: String = "",
            val OrderState: Int = 0,
            val CommentState: Int = 0,
            val OrderType: Int = 0,
            val GroupBuyState: Int = 0,
            val OrderNo: String = "",
            val PayByEnum: String = "",
            val ActivityCouponMoney: Double = 0.0,
            val TogetherMoney: Double = 0.0,
            val PayState: Int = 0,
            val IsAuthOrder: Boolean = false,
            val SendWay: Int = 0,
            val GroupBuyId: Int = 0,
            val PostageMoney: Double = 0.0,
            val GiftId: Int = 0,
            val ActivityGroupBuyingId : Int = 0,
            val GiftPackageProduct: SGiftPackageProduct = SGiftPackageProduct()
    ) {
        var job: Job? = null
        fun setSellTime(v1: TextView, str: String) {
            job = launch(CommonPool) {
                while (true) {
                    launch(UI) {
                        v1.text = TimeDateUtil.longToString(Timer * 1000, str)
                        Timer -= 1
                        if (Timer < 0) {
                            job?.cancel()
                            return@launch
                        }
                    }
                    delay(1000)
                }
            }.apply { start() }
        }
    }

    data class SGiftPackageProduct(
            val BrandId: Int = 0,
            val CategoryId: Int = 0,
            val ChildCategoryId: Int = 0,
            val Id: Int = 0,
            val Num: Int = 0,
            val Price: Double = 0.0,
            val Name: String = "",
            val ImgUrl: String = "",
            val Stock: Int = 0,
            val IsDisplay: Int = 0,
            val Preference: Int = 0,
            val ProductType: Int = 0,
            val Weight: Int = 0,
            val CompanyId: Int = 0

    ) : BModel()

    data class ROrderDetails(
            val Id: Int = 0,
            val ProductId: Int = 0,
            val OrderNo: String = "",
            val ProductPrice: Double = 0.0,
            val MarketPrice: Double = 0.0,
            val ProductCount: Int = 0,
            val ProductName: String = "",
            val DealType: Int = 0,
            val Spec: String = "",
            val ImgUrl: String = "",
            val SupportNo: String = "",
            val Freight: Int = 0,
            val STName: String = "",
            val SupportState: Int = 0

    )

    data class ExressDetails(
            val Products: List<ExressDetailsProduct> = listOf(),
            val Freight: Double = 0.0,
            var STName: String = "",
            val IsFree: Boolean = false
    ) {
        data class ExressDetailsProduct(
                val ImgUrl: String = "",
                val Name: String = "",
                val IsDisplay: Boolean = false,
                val Price: Double = 0.0,
                val SpikePrice: Double = 0.0,
                val TotalStock: Int = 0,
                val BrandId: Int = 0,
                val PreSell: Boolean = false,
                val CategoryId: Int = 0,
                val ChildCategoryId: Int = 0,
                val SaleType: Int = 0,
                val LimitCount: Int = 0,
                val Id: Int = 0
        )
    }
}

data class RecommentData(
        val Offset: String = "",
        val Total: String = "",
        val Results: List<Result> = listOf()
) {
    data class Result(
            val Name: String = "",
            val ImgUrl: String = "",
            val ImgUrls: List<String> = listOf(),
            val Desc: String = "",
            val Description: String = "",
            val TotalStock: Int = 0,
            val IsDisplay: Boolean = false,
            val Specs: List<Spec> = listOf(),
            val FreightTemplateName: String = "",
            val SpikePrice: Double = 0.0,
            val Price: Double = 0.0,
            val LimitCount: String = "",
            val Detail: String = "",
            val Id: String = "",
            val CommentPercent: Double = 0.0,
            val PriceTags: List<String> = listOf(),
            val SpecTitle: String = "",
            val SpecTitle2: String = ""
    ) {
        data class Spec(
                val Id: String = "",
                val ImgUrl: String = "",
                val SpecValue: String = "",
                val SpecValue2: String = "",
                val RealStock: String = "",
                val Price: Double = 0.0,
                val SpikePrice: Double = 0.0,
                val MarketPrice: Double = 0.0
        )
    }
}


data class CouponList(
        val Results: List<Result> = listOf(),
        val Total: Int = 0
) {

    data class Result(
            val ActivityCouponId: Int = 0,
            val MemberId: Int = 0,
            val MemberCouponState: Int = 0,
            val CustomerId: Int = 0,
            val OperationId: Int = 0,
            val IsPresented: Boolean = false,
            val InitCustomerId: Int = 0,
            val CouponMoney: Double = 0.0,
            val RelationId: Int = 0,
            val FullMoney: Double = 0.0,
            val Type: Int = 0,
            val CouponType: Int = 0,
            val BeginTime: String = "",
            val EndTime: String = "",
            val Remark: String = "",
            val TypeName: String = "",
            val MaxMoney: Int = 0,
            val ApplyIds: List<Int> = listOf(),
            val CanSend: Boolean = false,
            val UseState: String = "",
            val S: String = "",
            val E: String = "",
            val Color: String = "",
            val Style: Int = 0,
            val CouponName: String = "",
            val Id: Int = 0,
            val AddTime: String = "",
            val Delstatus: Boolean = false
    )
}

data class SCart(
        val type: Int = 0,
        val productSum: Int = 0,
        var isCheckEdit: Boolean = false,
        var stock: Int = 0,
        var isDisplay: Boolean = false,
        val AactivityModel: ShoppingCart.ActivityFull? = null,
        val proModel: ShoppingCart.RepCart.CARProduct? = null,
        val RecommProduct: List<RecommentData.Result> = listOf()
)

data class ShoppingCart(
        val PlatformTrusts: List<HomeData.RepPlatformTrust> = listOf(),
        val Cart: RepCart = RepCart(),
        val Total: Double = 0.0,
        val Member: RMember = RMember(),
        val ActivityFulls: List<ActivityFull> = listOf(),
        val Products: List<RecommentData.Result> = listOf()

) {
    data class PlatformTrust(
            val Title: String = "",
            val Content: String = ""
    )

    data class RepCart(
            val CustomerId: Int = 0,
            val MemberId: Int = 0,
            var Products: MutableList<CARProduct> = mutableListOf(),
            val Id: Int = 0,
            val AddTime: String = "",
            val Delstatus: Boolean = false
    ) {
        data class CARProduct(
                val CategoryId: Int = 0,
                var IsChecked: Boolean = false,
                val ProductId: Int = 0,
                var Num: Int = 0,
                val Price: Double = 0.0,
                val RealPrice: Double = 0.0,
                val Name: String = "",
                val ImgUrl: String = "",
                val Spec: String = "",
                val SpecId: Int = 0,
                val Stock: Int = 0,
                val PreSell: Boolean = false,
                val IsDisplay: Boolean = false,
                val Can: Boolean = false,
                val Limit: Int = 0,
                val MinOrder: Int = 0,
                val DealType: Int = 0,
                val SellRate: Double = 0.0,
                val FTId: Int = 0,
                val UF: Double = 0.0,
                val SendTypeId: Int = 0,
                val Delstatus: Boolean = false,
                val SpikeLimit: Int = 0,
                val IsOnlyRewardSelf: Boolean = false,
                val BuyLimit: Int = 0,
                val Rate: Int = 0,
                val RecommendPercent: Int = 0,
                val CostPrice: Int = 0,
                val Tax: Double = 0.0,
                var SellMoney: List<Int> = listOf(),
                val Points: Int = 0,
                val IsAllowUsePoints: Boolean = false,
                val Id: String = "",
                val NeedMoney: Int = 0,
                val CartId: Int = 0,
                val ChildCategoryId: Int = 0
        ) : BModel()
    }

    data class RMember(
            val FavCount: Int = 0,
            val Id: Int = 0
    )

    data class ActivityFull(
            val Integer: Int = 0,
            val Name: String = "",
            val ActivityType: Int = 0,
            val TypeName: String = "",
            val ActivityDetail: String = "",
            val SubTitle: String = "",
            val IsViewSubhead: Boolean = false,
            val FullMoney: Double = 0.0,
            val ReduceMoney: Double = 0.0,
            val ActivityState: String = "",
            val BeginTime: String = "",
            val EndTime: String = "",
            val Begins: Int = 0,
            val Ends: Long = 0,
            val Countdown: Long = 0,
            val IsAll: Boolean = false,
            var ProductIds: List<Int> = listOf()
    ) : BModel()

    data class Product(
            val Name: String = "",
            val IsDisplay: Boolean = false,
            val Price: Double = 0.0,
            val MarketPrice: Double = 0.0,
            val TotalStock: Int = 0,
            val BrandId: Int = 0,
            val PreSell: Boolean = false,
            val CategoryId: Int = 0,
            val ChildCategoryId: Int = 0,
            val Id: Int = 0
    )
}


data class UserData(
        val Id: String = "",
        val HeadUrl: String = "",
        val WxNickname: String = "",
        val IsAuthed: Boolean = false,
        val VipLevel: String = "",
        val VipLevelName: String = "",
        val NextVipLevelName: String = "",
        val IsTop: Boolean = false,
        val IsForward: Boolean = false,
        val ShopNo: String = "",
        val Name: String = "",
        val AuthNo: String = "",
        val UpgradeTips: Boolean = false,
        val Phone: String = "",
        val Exp: String = "",
        val Orders: List<Int> = listOf(0, 0, 0, 0, 0),
        val AvailableMoney: Double = 0.0,
        val Balance: Double = 0.0,
        val WithdrawMoney: Double = 0.0,
        val TogetMoney: Double = 0.0,
        val TotalWithdrawMoney: Double = 0.0,
        val HoldMoney: Double = 0.0,
        val UnavailableMoney: Double = 0.0,
        val GrassMasterId: Int = 0
)

data class SLookExpress(
        val Tpye: Int = 0,
        var lookExpress: LookExpress? = null
)

data class RselectCar(
        val CartNum: Int = 0,
        val Total: Double = 0.0
)

data class SBatchDelect(
        val Id: String = ""
)


data class TogetherList(
        val Activities: List<TogetherActivity> = listOf(),
        val CouDanModule: ImagesAndHrefInfo = ImagesAndHrefInfo(),
        val ShareLink: String = "",
        val MallDescription: String = ""
)

data class ImagesAndHrefInfo(
        val ImgUrl: String = "",
        val ImgHref: String = ""
)

data class TogetherActivity(
        val Id: String = "",
        val Name: String = "",
        val ActivityType: String = "",
        val TypeName: String = "",
        val ActivityDetail: String = "",
        val SubTitle: String = "",
        val HeadPicUrl: String = "",
        val IsViewSubhead: Boolean = false,
        val FullMoney: Double = 0.0,
        val ReduceMoney: Double = 0.0,
        val ActivityState: String = "",
        val BeginTime: String = "",
        val EndTime: String = "",
        val Spikes: List<TogetherSpike> = listOf(),
        val TogetherProducts: List<TogetherProduct> = listOf(),
        val Begins: String = "",
        val Ends: String = "",
        val Countdown: String = "",
        val ShareContent: String = "",
        val IsAll: Boolean = false,
        val ProductIds: List<String> = listOf()
)

data class TogetherSpike(
        val ProductId: String = "",
        val Price: Double = 0.0,
        val RealStock: String = "",
        val ProductName: String = "",
        val Description: String = "",
        val ProductImgUrl: String = "",
        val MarketPrice: Double = 0.0,
        val Notice: Boolean = false
)

data class TogetherProduct(
        val ProductId: String = "",
        val Price: Double = 0.0,
        val TotalStock: Int = 0,
        val ProductName: String = "",
        val ProductImgUrl: String = "",
        val MarketPrice: Double = 0.0
) : BModel()

data class SpikeList(
        val Activities: List<SpikeActivity> = listOf(),
        val ShareLink: String = "",
        val MallDescription: String = ""
)

data class SpikeActivity(
        val Id: String = "",
        val Name: String = "",
        val ActivityType: String = "",
        val TypeName: String = "",
        val ActivityDetail: String = "",
        val SubTitle: String = "",
        val HeadPicUrl: String = "",
        val IsViewSubhead: Boolean = false,
        val FullMoney: Double = 0.0,
        val ReduceMoney: Double = 0.0,
        val ActivityState: String = "",
        val BeginTime: String = "",
        val EndTime: String = "",
        val Spikes: List<SpikeSpike> = listOf(),
        val TogetherProducts: List<SpikeTogetherProduct> = listOf(),
        val Begins: Long = 0L,
        var Ends: Long = 0L,
        var State: Int = 0,
        val Countdown: String = "",
        val ShareContent: String = "",
        val IsAll: Boolean = false,
        val ProductIds: List<String> = listOf()
) {
    data class SpikeSpike(
            val ProductId: String = "",
            val Price: Double = 0.0,
            val RealStock: String = "",
            val ProductName: String = "",
            val Description: String = "",
            val ProductImgUrl: String = "",
            val ProductPrice: Double = 0.0,
            val MarketPrice: Double = 0.0,
            val Notice: Boolean = false
    )

    data class SpikeTogetherProduct(
            val ProductId: String = "",
            val Price: Double = 0.0,
            val AreaPrice: Double = 0.0,
            val TotalStock: String = "",
            val ProductName: String = "",
            val ProductImgUrl: String = "",
            val MarketPrice: Double = 0.0,
            val SpecTitle: String = "",
            val SpecTitle2: String = "",
            val SaleType: String = "",
            val Description: String = "",
            val LimitCount: String = ""
    )

    var job: Job? = null
    fun setSellTime(v: TextView) {
        job = CoroutinesInject().whileTime(mCall = {
            v.text = "本场还剩 ${TimeDateUtil.milliTohours(Ends).toBigDecimal()}" + ":${TimeDateUtil.milliTominutes(Ends).toBigDecimal()}" + ":${TimeDateUtil.milliToseconds(Ends).toBigDecimal()}"
            Ends -= 1000
        }, afterCall = {
            delay(1000)
        })
    }
}

data class OrderSubmit(
        val OrderNo: String = "",
        val Free: Boolean = false
)

data class ShareMenber(
        val IsMember: Boolean = false,
        val FromId: String = "",
        val ImgUrls: List<String> = listOf(),
        val InviteShareTitle: String = "",
        val InviteShareContent: String = "",
        val CommonQuestions: List<CommonQuestion> = listOf(),
        val MallLogo: String = "",
        val ShareLink: String = ""
) {
    data class CommonQuestion(
            val Title: String = "",
            val Content: String = ""
    )
}

data class ConfirmReceipt(
        val ErrorCode: String = ""
)

data class LookExpress(
        val ExpressCompanyName: String = "",
        val ExpressNo: String = "",
        val Imgs: List<String> = listOf(),
        val TraceList: List<STraceList> = listOf(),
        val Nid: String = ""
) {
    data class STraceList(
            val AcceptTime: String = "",
            val AcceptStation: String = ""
    )
}

data class Logisittics(
        val orderId: String = "",
        val orderState: String = "",
        val productSum: Int = 0
)

data class WXData(
        @SerializedName("appid") val appid: String = "",
        @SerializedName("partnerid") val partnerid: String = "",
        @SerializedName("prepayid") val prepayid: String = "",
        @SerializedName("package") val packageX: String = "",
        @SerializedName("noncestr") val noncestr: String = "",
        @SerializedName("timestamp") val timestamp: String = "",
        @SerializedName("sign") val sign: String = ""
)

data class AlpayInfo(
        var Html: String = ""
)

data class CommentProduct(
        val Offset: String = "",
        val Total: String = "",
        val Results: List<CommentResult> = listOf()
) {
    data class CommentResult(
            val DPContent: String = "",
            val AddTime: String = "",
            val DPStar: String = "",
            val NikName: String = "",
            val ImgUrls: List<String> = listOf(),
            val Spec: String = "",
            val HeadUrl: String = ""
    )
}

data class MyHelpGoodInfo(
        val AddTime: String,
        val PayExpired: Int,
        val Id: Int,
        val Timer: Long,
        val CustomerHelpAddTime: String,
        val CustomerHelpEndTime: String,
        val State: Int,
        val ProductId: Int,
        val Name: String,
        val ImgUrl: String,
        val Price: Double,
        val HelpAmount: Int,
        val HelpCount: Int,
        val GiveAmount: Int,
        val GiveCount: Int,
        val SpecTitle2: String,
        val SpecTitle: String,
        val ShareImgUrl: String,
        val ActivityHelpDetailProductId: Int,
        val SpecId: Int,
        val Specs: List<ISpecs> = listOf(),
        val CanSaleStock: Int,
        val Headimgurl: String,
        val CustFriList: List<MCustFriList> = listOf(),
        val ProductCount: Int

) {
    data class ISpecs(
            val Id: Int,
            val ImgUrl: String,
            val SpecValue: String,
            val SpecValue2: String,
            val RealStock: Int,
            val Price: Double,
            val SpikePrice: Double,
            val MarketPrice: Double,
            val AreaPrice: Double
    )

    data class MCustFriList(
            val MemberId: Int,
            val IsHelp: Int,
            val AddTime: String,
            val Remarks: String,
            val WxName: String,
            val Headimgurl: String
    )
}

data class MyHelpGoods(
        val Total: Int,
        val Results: List<MHGoods> = listOf()
) {
    data class MHGoods(
            val Price: Double,
            val AddTime: String,
            val PayExpired: Int,
            val Id: Int,
            var Timer: Long = 0L,
            val CustomerHelpAddTime: String,
            val CustomerHelpEndTime: String,
            val State: Int,
            val ProductId: Int,
            val Name: String,
            val ImgUrl: String,
            val HelpAmount: Int,
            val HelpCount: Int,
            val GiveAmount: Int,
            val GiveCount: Int,
            val SpecTitle2: String,
            val SpecTitle: String,
            val ShareImgUrl: String,
            val ActivityHelpDetailProductId: Int,
            val SpecId: Int,
            val Specs: List<MSpecs> = listOf(),
            val CanSaleStock: Int,
            val CustomerId: Int,
            val WxName: String,
            val Headimgurl: String

    ) {
        data class MSpecs(
                val Id: Int,
                val SpecValue: String,
                val SpecValue2: String,
                val RealStock: Int,
                val Price: Double,
                val MarketPrice: Double,
                val AreaPrice: Double
        )
    }

}

data class HelpFreeGoods(
        val ActivityHelpRules: String,
        val ActivityHelpList: List<HActivityHelpList> = listOf()
) {
    data class HActivityHelpList(
            val Id: Int,
            val ProductId: Int,
            val ImgUrl: String,
            val Name: String,
            val CanSaleStock: Int,
            val SpecTitle: String,
            val SpecTitle2: String,
            val TotalStock: Int,
            val HelpAmount: Int,
            val GiveAmount: Int,
            val SaleMinPrice: Double,
            val SaleMaxPrice: Double,
            val Price: Double,
            val SepList: List<HSepList> = listOf(),
            val Timer: Long,
            val CustomId: Int,
            val CustomHelpId: Int,
            val CustomBargainId: Int = 0,
            val Count: Int = 0,
            val State: Int = 0,
            val ShareImgUrl: String = ""
    ) {
        data class HSepList(
                var Id: Int = 0,
                var SpecValue: String = "",
                var SpecValue2: String = "",
                var RealStock: Int = 0,
                var Price: Double = 0.0,
                var MarketPrice: Double = 0.0
        ) : BModel()
    }


}

data class NoobPrefectureModel(
        val IsNewuser: Boolean = false,
        val ShareLink: String = "",
        val MallDescription: String = "",
        val NewuserZone: NoobNewuserZone = NoobNewuserZone(),
        val CouponMoney: Double = 0.0,
        val Products: List<NoobProduct> = listOf(),
        val Activities: List<NoobActivity> = listOf(),
        val Categories: List<NoobCategory> = listOf(),
        val IsReceived: Boolean = false
) {
    data class NoobNewuserZone(
            val Title: String = "",
            val Desc: String = "",
            val ImgUrl: String = "",
            val Title1: String = "",
            val CouponPic: String = "",
            val Title2: String = "",
            val Title3: String = ""
    )

    data class NoobActivity(
            val ActivityName: String = "",
            val ActivityId: String = "",
            val Products: List<NoobProduct> = listOf()
    )

    data class NoobProduct(
            val ImgUrl: String = "",
            val Name: String = "",
            val IsDisplay: Boolean = false,
            val Price: Double = 0.0,
            val MarketPrice: Double = 0.0,
            val TotalStock: Int = 0,
            val BrandId: String = "",
            val PreSell: Boolean = false,
            val Spec: String = "",
            val CategoryId: String = "",
            val ChildCategoryId: String = "",
            val SaleType: String = "",
            val LimitCount: String = "",
            val Id: String = "",
            val CommentPercent: Double = 0.0,
            val PriceTags: List<String> = listOf(),
            val SpecTitle: String = "",
            val SpecTitle2: String = "",
            val Description: String = ""
    )

    data class NoobCategory(
            val Id: String = "",
            val CategoryName: String = "",
            val DisplayOrder: String = "",
            val ParentId: String = "",
            val PicUrl: String = "",
            val Description: String = "",
            val IsHot: Boolean = false,
            val Childs: List<String> = listOf(),
            val PosterImgUrl: String = "",
            val PosterUrl: String = "",
            val Products: List<NoobProduct> = listOf()
    )
}

data class CustomerVipInit(
        val HeadUrl: String = "",
        val WxNickname: String = "",
        val VipLevelName: String = "",
        val Exp: Long = 0,
        val IsTop: Boolean = false,
        val NextVipLevelName: String = "",
        val NextLevelExp: Long = 0,
        val Products: List<VipProduct> = listOf(),
        val PackageProducts: List<VipProduct> = listOf(),
        val IsHaveGotVipCoupons: Boolean = false,
        val VipCouponsCount: String = "",
        val VipCouponsMoney: Double = 0.0,
        val OpenSendCouponMoney: Double = 0.0
) {
    data class VipProduct(
            val ImgUrl: String = "",
            val Name: String = "",
            val IsDisplay: Boolean = false,
            val Price: Double = 0.0,
            val MarketPrice: Double = 0.0,
            val TotalStock: String = "",
            val BrandId: String = "",
            val PreSell: Boolean = false,
            val Spec: String = "",
            val CategoryId: String = "",
            val ChildCategoryId: String = "",
            val SaleType: String = "",
            val LimitCount: String = "",
            val Id: String = "",
            val CommentPercent: Double = 0.0,
            val PriceTags: List<String> = listOf(),
            val SpecTitle: String = "",
            val SpecTitle2: String = "",
            val Description: String = ""
    )
}

data class FreeProduct(
        val ImgUrl: String = "",
        val Name: String = "",
        val IsDisplay: Boolean = false,
        val Price: Double = 0.0,
        val MarketPrice: Double = 0.0,
        val TotalStock: String = "",
        val BrandId: String = "",
        val PreSell: Boolean = false,
        val Spec: String = "",
        val CategoryId: String = "",
        val ChildCategoryId: String = "",
        val SaleType: String = "",
        val LimitCount: String = "",
        val Id: String = "",
        val Description: String = ""
)

data class GetRealNameStatus(
        var State: Int = 0,
        val RefuseReason: String = ""
)

data class BankInfo(
        val BankName: String = "",
        val Id: Int = 0,
        val ShortBankName: String = "",
        val IconUrl: String = ""
)


data class GetSaleList(
        val SaleList: List<SaleLists> = listOf(),
        val Total: Int = 0
) {
    data class SaleLists(
            val StateStr: String = "",
            val Reward: Double = 0.0,
            val Id: Int = 0,
            val Timer: Int = 0,
            val AddTime: String = "",
            val PayTime: String = "",
            val SendTime: String = "",
            val ReceiveName: String = "",
            val ReceivePhone: String = "",
            val ReceiveProvince: String = "",
            val ReceiveCity: String = "",
            val ReceiveArea: String = "",
            val ReceiveAddress: String = "",
            val OrderMoney: String = "",
            val OrderState: Int = 0,
            val CommentState: Int = 0,
            val OrderType: Int = 0,
            val GroupBuyState: Int = 0,
            val Details: List<Detail> = listOf(),
            val OrderNo: String = "",
            val PayState: Int = 0,
            val IsAuthOrder: Boolean = false,
            val SendWay: Int = 0,
            val GroupBuyId: Int = 0,
            val PostageMoney: Double = 0.0,
            val ProductMoney: Double = 0.0,
            val ActivityCouponMoney: Double = 0.0,
            val GiftId: Int = 0,
            val GiftPackageProduct: GiftPackageProducts = GiftPackageProducts(),
            val PayBy: Int = 0,
            val PayByEnum: String = "",
            val PayMoney: Double = 0.0
    ) {
        data class Detail(
                val Id: Int = 0,
                val ProductId: Int = 0,
                val OrderNo: String = "",
                val ProductPrice: Double = 0.0,
                val MarketPrice: Int = 0,
                val ProductCount: Int = 0,
                val ProductName: String = "",
                val DealType: Int = 0,
                val Spec: String = "",
                val ImgUrl: String = "",
                val Freight: Int = 0,
                val Extend: Extends = Extends()
        ) : BModel() {
            data class Extends(
                    val Freight: Double = 0.0,
                    val IsFree: Boolean = false,
                    val TemplateId: Int = 0
            )
        }

        data class GiftPackageProducts(
                val BrandId: Int = 0,
                val CategoryId: Int = 0,
                val ChildCategoryId: Int = 0,
                val Id: Int = 0,
                val Num: Int = 0,
                val Price: Double = 0.0,
                val Name: String = "",
                val ImgUrl: String = "",
                val Stock: Int = 0,
                val IsDisplay: Int = 0,
                val Preference: Int = 0,
                val ProductType: Int = 0,
                val Weight: Int = 0,
                val CompanyId: Int = 0
        ) : BModel()
    }
}

data class SSaleLists(
        val type: Int = 0,
        val orderTime: String = "",
        val orderNo: String = "",
        val orderMoney: String = "",
        val StateStr: String = "",
        val Reward: Double = 0.0,
        val model: GetSaleList.SaleLists.Detail? = null,
        val giftmodel: GetSaleList.SaleLists.GiftPackageProducts? = null
)

data class BalanceRecord(
        val Id: Int = 0,
        val BalanceWithdrawId: Int = 0,
        val AddTime: String = "",
        val Balance: Double = 0.0,
        val OrderNo: String = "",
        val Turn: Int = 0,
        val InType: Int = 0,
        val OutType: Int = 0,
        val TypeNameStr: String = "",
        val TypeName: String = ""
)

data class BlackVip(
        val Title: String = "",
        val Title2: String = "",
        val Products: List<BlackProducts> = listOf()
)

data class BlackProducts(
        val Name: String = "",
        val ImgUrl: String = "",
        val LongImgUrl: String = "",
        val TotalStock: Int = -1,
        val IsDisplay: Boolean = false,
        val Detail: String = "",
        val Price: Double = 0.0,
        val MarketPrice: Double = 0.0,
        val Id: String = ""
)

data class BalanceRecordDetail(
        val CompleteTime: String = "",
        val StateStr: String = "",
        val RefundReason: String = "",
        val FailReason: String = "",
        val ApplyTime: String = "",
        val Id: Int = 0,
        val BalanceWithdrawId: Int = 0,
        val AddTime: String = "",
        val Balance: Double = 0.0,
        val OrderNo: String = "",
        val SupportNo: String = "",
        val Turn: Int = 0,
        val InType: Int = 0,
        val OutType: Int = 0,
        val Remark: String = "",
        val BankName: String = "",
        val BankcardNo: String = "",
        val TypeNameStr: String = "",
        val TypeName: String = ""
)

data class PostMoney(
        val Money: Double = 0.0
)

data class GiftData(
        val Product: GiftProduct = GiftProduct(),
        val ShareLink: String = "",
        val Exp: Int = 0,
        val Text: String = "",
        val MallDescription: String = ""

) {
    data class GiftProduct(
            val Name: String = "",
            val ImgUrl: String = "",
            val LongImgUrl: String = "",
            val ImgUrls: List<String> = listOf(),
            val TotalStock: Int = 0,
            val IsDisplay: Boolean = false,
            val FreightTemplateName: String = "",
            val Detail: String = "",
            val Price: Double = 0.0,
            val MarketPrice: Double = 0.0,
            val Id: String = "",
            val CommentPercent: Double = 0.0,
            val PriceTags: List<String> = listOf(),
            val SpecTitle: String = "",
            val SpecTitle2: String = "",
            val Description: String = ""
    )
}

data class VipModel(
        val Offset: Int = 0,
        val Total: Int = 0,
        val Results: List<SResults> = listOf()
) {
    data class SResults(
            val Id: Int = 0,
            val WxNickname: String = "",
            val HeadUrl: String = "",
            val AuthTime: String = ""
    )
}

data class AfterSaleModel(
        val list: List<SaleList> = listOf()
) {
    data class SaleList(
            val Support: Supports = Supports(),
            val OrderDetail: OrderDetails = OrderDetails()
    ) {
        data class Supports(
                val OrderDetailId: String = "",
                val OrderNo: String = "",
                val SupportNo: String = "",
                val SupportState: Int = 0,
                val SupportType: Int = 0,
                val ProductPrice: Double = 0.0,
                val ProductCount: Int = 0,
                val SupportDetail: String = "",
                val ReturnExpressCompanyId: Int = 0,
                val ImgUrls: List<String> = listOf(),
                val NewExpressCompanyId: Int = 0,
                val CustomerId: Int = 0,
                val NewSpecId: Int = 0,
                val RefundMoney: Double = 0.0,
                val Address: String = "",
                val RealRefundMoney: Double = 0.0,
                val RealRefundVirtualMoney: Double = 0.0,
                val NewProductCount: Int = 0,
                val ReturnExpired: Int = 0,
                val Timer: Int = 0,
                val RealRefundPoint: Int = 0,
                val RealRefundRedPacket: Double = 0.0,
                val IsReceiveProduct: Boolean = false,
                val IsReturnStock: Boolean = false,
                val Commission: Double = 0.0,
                val CommissionEmployeeId: Double = 0.0,
                val RefundBy: Int = 0,
                val Id: Int = 0,
                val AddTime: String = "",
                val Delstatus: Boolean = false
        ) : BModel()

        data class OrderDetails(
                val OrderNo: String = "",
                val ProductId: Int = 0,
                val ProductDesc: String = "",
                val ProductPrice: Double = 0.0,
                val RealPrice: Double = 0.0,
                val MarketPrice: Double = 0.0,
                val ProductCount: Int = 0,
                val ProductName: String = "",
                val SpecId: Int = 0,
                val DealType: Int = 0,
                val MemberId: Int = 0,
                val Spec: String = "",
                val SpikeId: Int = 0,
                val ImgUrl: String = "",
                val SupportNo: String = "",
                val SellRate: Double = 0.0,
                val SellDiscountMoney: Double = 0.0,
                val Commission: Double = 0.0,
                val Tax: Double = 0.0,
                val ExpressId: Int = 0,
                val SendTypeId: Int = 0,
                val Extend: Extends = Extends(),
                val SaleType: Int = 0,
                val SaleArea: Boolean = false,
                val DiscountMoney: Double = 0.0,
                val CostPrice: Double = 0.0,
                val PreSell: Boolean = false,
                val IsDisplay: Boolean = false,
                val Stock: Int = 0,
                val IsOnlyRewardSelf: Boolean = false,
                val Points: Int = 0,
                val PointPrice: Int = 0,
                val Limit: Int = 0,
                val Id: Int = 0,
                val AddTime: String = "",
                val Delstatus: Boolean = false
        ) : BModel() {
            data class Extends(
                    val Freight: Double = 0.0,
                    val IsFree: Boolean = false,
                    val TemplateId: Int = 0
            )
        }
    }
}

data class SAfterSaleModels(
        val type: Int = 0,
        val Support: AfterSaleModel.SaleList.Supports? = null,
        val OrderDetail: AfterSaleModel.SaleList.OrderDetails? = null
)

data class SAfterSaleTypeModels(
        var title: String = "",
        var type: String = "",
        var OrderNo: String = "",
        var Id: Int = 0,
        var ImgUrl: String = "",
        var ProductName: String = "",
        var ProductPrice: Double = 0.0,
        var Spec: String = "",
        var ProductCount: Int = 0
)

data class SAfterSaleDetailModels(
        val Support: SSupport = SSupport(),
        val OrderDetail: SOrderDetail = SOrderDetail(),
        val Expresses: List<SExpresses> = listOf()
) {
    data class SSupport(
            val OrderDetailId: String = "",
            val OrderNo: String = "",
            val SupportNo: String = "",
            val SupportState: Int = 0,
            val SupportType: Int = 0,
            val ProductPrice: Double = 0.0,
            val ProductCount: Int = 0,
            val SupportDetail: String = "",
            val ReturnExpressCompanyId: Int = 0,
            val ImgUrls: List<String> = listOf(),
            val FailedPassReason: String = "",
            val NewExpressCompanyId: Int = 0,
            val CustomerId: Int = 0,
            val NewSpecId: Int = 0,
            val SupportWhy: String = "",
            val RefundMoney: Double = 0.0,
            val Address: String = "",
            val Records: List<SRecords> = listOf(),
            val RealRefundMoney: Double = 0.0,
            val RealRefundVirtualMoney: Double = 0.0,
            val NewProductCount: Int = 0,
            val ReturnExpired: Int = 0,
            val CheckApplyTime: String = "",
            var Timer: Long = 0L,
            val RealRefundPoint: Int = 0,
            val RealRefundRedPacket: Double = 0.0,
            val IsReceiveProduct: Boolean = false,
            val IsReturnStock: Boolean = false,
            val Commission: Double = 0.0,
            val CommissionEmployeeId: Double = 0.0,
            val RefundBy: Int = 0,
            val Id: Int = 0,
            val AddTime: String = "",
            val Delstatus: Boolean = false
    ) {
        data class SRecords(
                val SupportNo: String = "",
                val BeforeValue: String = "",
                val AfterValue: String = "",
                val OperationType: Int = 0,
                val OperationUserId: Int = 0,
                val OperationUserIp: String = "",
                val Remark: String = "",
                val IsClient: Boolean = false,
                val Id: Int = 0,
                val AddTime: String = "",
                val Delstatus: Boolean = false
        )

        var job: Job? = null
        var isFrist: Boolean? = false
        fun setSellTime(v1: TextView, start: String, end: String) {
            job = launch(CommonPool) {

                while (true) {
                    launch(UI) {
                        v1.text = start + "${TimeDateUtil.milliTodays(Timer * 1000)}天" + "${TimeDateUtil.milliTohours1(Timer * 1000)}时" +
                                "${TimeDateUtil.milliTominutes(Timer * 1000)}分" + "${TimeDateUtil.milliToseconds(Timer * 1000)}秒" + end
                        Timer -= 1
                        if (Timer < 0) {
                            if (isFrist!!) {
                                isFrist = false
                                sendEvent(SAfterSaleDetailModels())
                            }
                            job?.cancel()
                            return@launch
                        } else {
                            isFrist = true
                        }
                    }
                    delay(1000)
                }
            }.apply { start() }
        }
    }

    data class SOrderDetail(
            val OrderNo: String = "",
            val ProductId: Int = 0,
            val ProductDesc: String = "",
            val ProductPrice: Double = 0.0,
            val RealPrice: Double = 0.0,
            val MarketPrice: Double = 0.0,
            val ProductCount: Int = 0,
            val ProductName: String = "",
            val SpecId: Int = 0,
            val BrandId: Int = 0,
            val CategroyId: Int = 0,
            val DealType: Int = 0,
            val MemberId: Int = 0,
            val Spec: String = "",
            val SpikeId: Int = 0,
            val ImgUrl: String = "",
            val SupportNo: String = "",
            val SellRate: Double = 0.0,
            val SellDiscountMoney: Double = 0.0,
            val Commission: Double = 0.0,
            val Tax: Double = 0.0,
            val ExpressId: Int = 0,
            val SendTypeId: Int = 0,
            val Extend: SExtends = SExtends(),
            val SaleType: Int = 0,
            val SaleArea: Boolean = false,
            val DiscountMoney: Double = 0.0,
            val CostPrice: Double = 0.0,
            val Activity: SActivity = SActivity(),
            val PreSell: Boolean = false,
            val IsDisplay: Boolean = false,
            val Stock: Int = 0,
            val IsOnlyRewardSelf: Boolean = false,
            val Points: Int = 0,
            val PointPrice: Int = 0,
            val Limit: Int = 0,
            val Id: Int = 0,
            val AddTime: String = "",
            val Delstatus: Boolean = false
    ) {
        data class SExtends(
                val Freight: Double = 0.0,
                val IsFree: Boolean = false,
                val TemplateId: Int = 0
        )

        data class SActivity(
                val Id: Int = 0,
                val DetailId: Int = 0,
                val Type: String = "",
                val Used: Boolean = false,
                val Sign: String = ""
        )
    }

    data class SExpresses(
            val ExpressName: String = "",
            val Id: Int = 0,
            val AddTime: String = "",
            val Delstatus: Boolean = false

    )
}

data class Allexpress(
        val ExpressName: String = "",
        val Id: Int = 0,
        val AddTime: String = "",
        val Delstatus: Boolean = false

)

data class AfterSalesAdd(
        val Support: Supports = Supports()
) {
    data class Supports(
            val SupportNo: String = "",
            val OrderNo: String = "",
            val Id: Int = 0
    )
}

data class Material(

        val Results: List<MaterialLists> = listOf()
) {
    data class MaterialLists(
            val MaterialType: Int = 0,
            val Title: String = "",
            val Info: String = "",
            val ImgUrls: List<String> = listOf(),
            val ProductId: Int = 0,
            val Name: String = "",
            val Price: Double = 0.0,
            val SellRate: Double = 0.0,
            val SharePrice: Double = 0.0,
            val MarketPrice: Double = 0.0,
            val Description: String = "",
            val Count: Int = 0,
            val AddTime: String = "",
            val ShareUrl: String = "",
            val ImgUrl: String = "",
            val DisplayTime:String = ""
    ) : BModel()
}

data class selectData(
        var model: Material.MaterialLists? = Material.MaterialLists(),
        var type: String = ""
)

data class AttentionList(
        val Offset: Int = 0,
        val Total: Int = 0,
        val Results: List<Attentions> = listOf()
) {
    data class Attentions(
            val RegistrationTime: String = "",
            val HeadPortrait: String = "",
            val NickName: String = "",
            val ArticleCollection: List<Int> = listOf(),
            val Attention: List<Int> = listOf(),
            val Fans: List<Int> = listOf(),
            val GrassMasterType: Int = 0,
            val UserId: Int = 0,
            val GrassMasterStatus: Int = 0,
            val FansCount: Int = 0,
            val AttentionCount: Int = 0,
            val IsAttention: Int = 0,
            val Id: Int = 0,
            val AddTime: String = "",
            val Delstatus: Boolean = false
    )
}

data class Masters(
        val Id: Int = 0,
        val UserId: Int = 0,
        val NickName: String = "",
        val HeadPortrait: String = "",
        val RegistrationTime: String = "",
        val GrassMasterType: Int = 0,
        val AttentionCount: Int = 0,
        val FansCount: Int = 0,
        val PublicArticleCount: Int = 0,
        val CollectionCount: Int = 0,
        val IsAttention: Int = 0,
        val PraiseCount: Int = 0,
        val FansTrends: Boolean = false,
        val PraisesTrends: Boolean = false,
        val IsSelf: Boolean = false
)

data class ArticlePraises(
        val Offset: Int = 0,
        val Total: Int = 0,
        val Results: List<Praises> = listOf()
) {
    data class Praises(
            val GrassMasterId: Int = 0,
            val ArticleId: Int = 0,
            val LookStatus: Int = 0,
            val PraiseTime: String = "",
            val GrassTitle: String = "",
            val GrassContent: String = "",
            val ImgUrls: List<String> = listOf(),
            val NickName: String = "",
            val HeadPortrait: String = ""
    ) : BModel()
}

data class ArticleList(
        val Offset: Int = 0,
        val Total: Int = 0,
        val Results: List<Articles> = listOf()
) {
    data class Articles(
            val Id: Int = 0,
            val UserId: Int = 0,
            val GrassTitle: String = "",
            val ShortTitle: String = "",
            val GrassContent: String = "",
            val ImgUrls: List<String> = listOf(),
            val ProductIds: List<Int> = listOf(),
            var Liked: Int = 0,
            var IsLike: Int = 0,
            val IsRecommend: Boolean = false,
            val GrassMasterId: Int = 0,
            val ReleaseTime: String = "",
            val NickName: String = "",
            val HeadPortrait: String = "",
            val IsAttention: Int = 0,
            val IsCollection: Int = 0,
            val IsSelf:Boolean= false,
            val ProductList: List<ProductLists> = listOf(),
            val ArticleComments: List<ArticleComment> = listOf(),
            var ArticlePraises: List<ArticlePraise> = listOf()
    ) {
        data class ProductLists(
                val ProductId: Int = 0,
                val ImgUrl: String = "",
                val Name: String = ""
        )

        data class ArticleComment(
                val Id: Int = 0,
                val HeadUrl: String = "",
                val UserName: String = "",
                val CommentTime: String = "",
                val Comment: String = "",
                val ArticleId: Int = 0,
                val GrassMasterId: Int = 0
        )

        data class ArticlePraise(
                val GrassMasterId: Int = 0,
                val NickName: String = ""
        )
    }


}

data class GrassTag(
        val Total: Int,
        val Results: List<TagResults> = listOf()
) {
    data class TagResults(
            val TagName: String = "",
            val IsShow: Int = 0,
            val Id: Int = 0,
            val AddTime: String = "",
            val Delstatus: Boolean = false
    )
}

data class GrassArticle(
        val Id: Int = 0,
        val GrassTitle: String = "",
        val ShortTitle: String = "",
        val GrassContent: String = "",
        val ImgUrls: List<String> = listOf(),
        val ProductIds: List<Int> = listOf(),
        var Liked: Int = 0,
        val IsRecommend: Boolean = false,
        val GrassMasterId: Int = 0,
        val ReleaseTime: String = "",
        val NickName: String = "",
        val HeadPortrait: String = "",
        val IsAttention: Int = 0,
        val IsCollection: Int = 0,
        val ShareUrl: String = "",
        val CommentsCount: Int = 0,
        var CollectionCount: Int = 0,
        val GrassMasterType: Int = 0,
        var IsLike: Int = 0,
        val IsSelf: Boolean = false,
        val ProductList: List<GrassProducts> = listOf(),
        val ArticleComments: List<GArticleComments> = listOf(),
        val ArticlePraises: List<GArticlePraises> = listOf()
) {
    data class GrassProducts(
            val BrandId: Int = 0,
            val BrandName: String ="",
            val Description: String ="",
            val Name: String ="",
            val Id: Int  = 0,
            val CanSaleStock: Int  = 0,
            val SalePrice: String ="",
            val Stock: Int  = 0,
            val LockStock: Int  = 0,
            val SaleMinPrice: Double = 0.0,
            val SaleMaxPrice: Double = 0.0,
            val MarketPrice : String = "",
            val ImgUrl: String ="",
            val RecordNum: Int  = 0
    )

    data class GArticleComments(
            val Id: Int,
            val HeadUrl: String,
            val UserName: String,
            val CommentTime: String,
            val Comment: String,
            val ArticleId: Int,
            val GrassMasterId: Int,
            val CommentReplyList: List<GCommentReplyList> = listOf()
    )

    data class GArticlePraises(
            val GrassMasterId: Int,
            val ArticleId: Int,
            val LookStatus: Int,
            val PraiseTime: String,
            val GrassTitle: String,
            val GrassMasterType: Int,
            val GrassContent: String,
            val ImgUrls: List<String> = listOf(),
            val NickName: String,
            val HeadPortrait: String

    )

    data class GCommentReplyList(
            val HeadUrl: String,
            val UserName: String,
            val CommentTime: String,
            val Comment: String,
            val ArticleId: Int,
            val GrassMasterId: Int,
            val CommentFlag: Int,
            val ReplyGrassMasterId: String,
            val ReplyUserName: String,
            val ReplyHeadUrl: String
    )
}
data  class GrassMasters(
        val Total: Int,
        val Results: List<GrassList> = listOf()
){
    data class GrassList(
            val Id: Int =0,
            val UserId: Int=0,
            val NickName: String="",
            val HeadPortrait: String="",
            val RegistrationTime:String="",
            val GrassMasterType: Int =0,
            val AttentionCount:Int=0,
            val FansCount: Int=0,
            val PublicArticleCount:Int=0,
            val CollectionCount:Int=0,
            val PraiseCount: Int=0,
            val IsAttention:Int=0,
            val PraisesTrends:Boolean =false,
            val FansTrends: Boolean = false

    )

}
data class GrassArticles(
        val Total: Int = 0,
        val Results: List<GrassList> = listOf()

) {
    data class GrassList(
            val Id: Int = 0,
            val GrassTitle: String = "",
            val ShortTitle: String = "",
            val GrassContent: String = "",
            val ImgUrls: List<String> = listOf(),
            val ProductIds: List<Int> = listOf(),
            var Liked: Int = 0,
            val IsRecommend: Boolean = false,
            val GrassMasterId: Int = 0,
            val ReleaseTime: String = "",
            val NickName: String = "",
            val HeadPortrait: String = "",
            var IsAttention: Int = 0,
            val IsCollection: Int = 0,
            val ShareUrl: String = "",
            val CommentsCount: Int = 0,
            val CollectionCount: Int = 0,
            val GrassMasterType: Int,
            var IsLike: Int = 0,
            val IsSelf: Boolean = false,
            val ProductList: List<GrassProducts> = listOf()

    )

    data class GrassProducts(
            val BrandId: Int,
            val BrandName: String,
            val Name: String,
            val Id: Int,
            val CanSaleStock: Int,
            val SalePrice: String,
            val Stock: Int,
            val LockStock: Int,
            val SaleMinPrice: Double,
            val SaleMaxPrice: Double,
            val ImgUrl: String,
            val ProductId: Int,
            val RecordNum: Int
    )

}


data class BargainInfo(
        val RuleDescription: String = "",
        val Results: List<BargainInfoResults> = listOf()

) {
    data class BargainInfoResults(

            val ProductId: Int = 0,
            val Name: String = "",
            val ImgUrl: String = "",
            val CanSaleStock: Int = 0,
            val SpecTitle: String = "",
            val SpecTitle2: String = "",
            val TotalStock: Int = 0,
            val SaleMinPrice: Double = 0.0,
            val SaleMaxPrice: Double = 0.0,
            val Price: Double = 0.0,
            val SepList: List<Sep> = listOf(),
            val Timer: Int = 0,
            val CustomId: Int = 0,
            val Id: Int = 0,
            val CustomBargainId: Int = 0,
            val Count: Int = 0,
            val State: Int = 0
    ) {
        data class Sep(
                val Id: Int = 0,
                val SpecValue: String = "",
                val SpecValue2: String = "",
                val RealStock: Int = 0,
                val Price: Double = 0.0,
                val MarketPrice: Double = 0.0,
                val AreaPrice: Double = 0.0
        )
    }
}

data class BargainCommitInfoResult(
        val Id: Int = 0,
        val CustomerId: Int = 0,
        val ProductId: Int = 0,
        val ShareUrl: String = "",
        val SpecId: Int = 0,
        val State: Double = 0.0,
        val Price: Double = 0.0,
        val ActivityBargainId: Int = 0,
        val PayExpired: Int = 0,
        val CutPrice: Double = 0.0
)


data class GroupBuyDetailResult(
        val PaySuccess: Boolean = false,
        val GroupBuyId: String = "",
        val MallDescription: String = "",
        val GroupBuyState: Int = 0,
        val ShareLink: String = "",
        val HeadUrls: List<String> = listOf(),
        val ProductName: String = "",
        val ProductImgUrl: String = "",
        val GroupBuyPrice: Double = 0.0,
        val Reduce: Double = 0.0,
        val ServiceDescriptions: List<GoodsDetails.RepServiceDescriptions> = listOf(),
        val Remain: String = "",
        val GroupCount: Int = 0,
        val Successed: Long = 0,
        val State: String = "",
        val StateTexts: List<String> = listOf(),
        val Ends: String = "",
        val Rule: String = "",
        val ReceiveName: String = "",
        val ReceivePhone: String = "",
        val ReceiveProvince: String = "",
        val ReceiveCity: String = "",
        val ReceiveArea: String = "",
        val ReceiveAddress: String = "",
        val SuccessTime: String = "",
        val IsHeaderFree : Boolean = false
)

data class GroupBuyListResult(
        val GroupBuyCoverUrl: String = "",
        val Products: GroupBuyProducts = GroupBuyProducts(),
        val Categories: List<Category> = listOf()
) {
    data class GroupBuyProducts(
            val Offset: Int = 0,
            val Total: Int = 0,
            val Results: List<Result> = listOf()
    )

    data class Result(
            val Name: String = "",
            val ImgUrl: String = "",
            val GroupBuyingId: Int = 0,
            val GroupCount: Int = 0,
            val GroupBuyingPrice: Double = 0.0,
            val IsLeaderFree: Boolean = false,
            val CategoryId: Int = 0,
            val Permission: Double = 0.0,
            val ActivityBeginTime: String = "",
            val ActivityEndTime: String = "",
            val ActivityState: Int = 0,
            val Id: Int = 0,
            val Price: Double = 0.0,
            val SaleType: Int = 0,
            val AreaPrice: Double = 0.0,
            val NewCustomerZonePrice: Double = 0.0
    )

    data class Category(
            val Id: Int = 0,
            val CategoryName: String = "",
            val DisplayOrder: Int = 0,
            val ParentId: Int = 0,
            val PicUrl: String = "",
            val IsHot: Boolean = false,
            val PosterImgUrl: String = "",
            val PosterUrl: String = ""
    )
}

data class GroupDetailInfo(
        val Product: GroupDetailProduct = GroupDetailProduct(),
        val ActivityState: Int = 0,
        val Ends: Long = 0,
        val Tips: String = "",
        val GroupBuys: List<GroupUserInfo> = listOf(),
        val Favorite: Boolean = false,
        val ShareTitle: String = "",
        val PostMoney: Double = 0.0,
        val STName: String = "",
        val STDesc: String = "",
        val PMFree: Boolean = false,
        val PostAddress: String = "",
        val KfUrl: String = "",
        val ShareLink: String = "",
        val Rule: String = "",
        val ServiceDescriptions: List<GoodsDetails.RepServiceDescriptions> = listOf(),
        val CommentCount: Int = 0,
        val Comment: GroupDetailComment = GroupDetailComment(),
        val GroupCount: Int = 0
) {

    fun getMaxPrice(): String {
        var max = 0.0
        Product.Specs.forEach {
            if (max < it.Price) {
                max = it.Price
            }
        }
        return "¥ ${max.to2Double()}"
    }

    data class GroupUserInfo(
            val Id: Int = 0,
            val HeadUrl: String = "",
            val WxNickname: String = "",
            val Remain: Int = 0,
            val Ends: Long = 0,
            val CanJoin: Boolean = false
    )

    data class GroupDetailProduct(
            val GroupBuyingId: Int = 0,
            val GroupBuyingPrice: Double = 0.0,
            val Permission: Int = 0,
            val Name: String = "",
            val ImgUrl: String = "",
            val ImgUrls: List<String> = listOf(),
            val TotalStock: Int = 0,
            val IsDisplay: Boolean = false,
            val Specs: List<Spec> = listOf(),
            val FreightTemplateName: String = "",
            val Detail: String = "",
            val FavoriteCount: Int = 0,
            val SellCount: Int = 0,
            val Id: Int = 0,
            val CommentPercent: Double = 0.0,
            val PriceTags: List<String> = listOf(),
            val SpecTitle: String = "",
            val SpecTitle2: String = "",
            val Description: String = "",
            val Price: Double = 0.0,
            val MarketPrice: Double = 0.0,
            val SaleType: Int = 0,
            val AreaPrice: Double = 0.0,
            val NewCustomerZonePrice: Double = 0.0
    )

    data class Spec(
            val Id: Int = 0,
            val ImgUrl: String = "",
            val SpecValue: String = "",
            val SpecValue2: String = "",
            val RealStock: Int = 0,
            val Price: Double = 0.0,
            val MarketPrice: Double = 0.0,
            val AreaPrice: Double = 0.0,
            val GroupBuyPrice: Double = 0.0
    )

    data class GroupDetailComment(
            val DPContent: String = "",
            val AddTime: String = "",
            val DPStar: Int = 0,
            val ImgUrls: List<Any> = listOf(),
            val Spec: String = "",
            val HeadUrl: String = ""
    )
}

data class GroupBuyInfo(
        val HeadUrls: List<String> = listOf(),
        val ProductName: String = "",
        val ProductImgUrl: String = "",
        val GroupCount: Int = 0,
        val Successed: Int = 0,
        val GroupBuyPrice: Double = 0.0,
        val Reduce: Double = 0.0,
        val ServiceDescriptions: List<GoodsDetails.RepServiceDescriptions> = listOf(),
        val Remain: Int = 0,
        val Ends: Int = 0,
        val Rule: String = "",
        val Specs: List<Spec> = listOf()
) {
    data class Spec(
            val Id: Int = 0,
            val SpecValue: String = "",
            val SpecValue2: String = "",
            val RealStock: Int = 0,
            val Price: Double = 0.0,
            val MarketPrice: Double = 0.0,
            val AreaPrice: Double = 0.0,
            val GroupBuyPrice: Double = 0.0
    )
}

data class MineBargainListModel(
        val Results: List<Result> = listOf()
) {
    data class Result(
            val ActivityBargainId: Int = 0,
            val ProductId: Int = 0,
            val Name: String = "",
            val ImgUrl: String = "",
            val Price: Double = 0.0,
            val SpecTitle: String = "",
            val SpecTitle2: String = "",
            val CanSaleStock: Int = 0,
            val Timer: Int = 0,
            val CustomerId: Int = 0,
            val Id: Int = 0,
            val State: Int = 0,
            val CutPirce: Double = 0.0,
            val OrderNo: String = "",
            val SepList: List<Sep> = listOf(),
            val PayExpired: Int = 0,
            val AddTime: String = "",
            val ShareUrl: String = ""
    ) {
        data class Sep(
                val Id: Int = 0,
                val SpecValue: String = "",
                val SpecValue2: String = "",
                val RealStock: Int = 0,
                val Price: Double = 0.0,
                val MarketPrice: Double = 0.0,
                val AreaPrice: Double = 0.0
        )
    }
}


data class MineBargainDetail(
        val ActivityFriendsHelpDetails: MineActivityFriendsHelpDetails = MineActivityFriendsHelpDetails()
) {
    data class MineActivityFriendsHelpDetails(
            val MemberDetails: MemberDetails = MemberDetails(),
            val ProducDetails: ProducDetails = ProducDetails(),
            val BargainFriendsList: List<BargainFriends> = listOf(),
            val UserType: Int = 0,
            val State: Int = 0,
            val Timer: Int = 0,
            val FriendSumPrice: Double = 0.0,
            val Surplusprice: Double = 0.0,
            val CustomerBargainId: Int = 0,
            val ShareUrl: String = ""
    )

    data class ProducDetails(
            val ProductName: String = "",
            val ProductImgUrl: String = "",
            val ProductId: Int = 0,
            val ProductPrice: Double = 0.0,
            val SpecId: Int = 0,
            val State: Int = 0,
            val Price: Double = 0.0,
            val ActivityBargainId: Int = 0,
            val PayExpired: Int = 0,
            val CutPrice: Double = 0.0,
            val BargainNum: Int = 0,
            val SepList: List<SepList> = listOf()
            )

    data class SepList(
            var Id: Int = 0,
            var SpecValue: String = "",
            var SpecValue2: String = "",
            var RealStock: Int = 0,
            var Price: Double = 0.0,
            var MarketPrice: Double = 0.0,
            var AreaPrice: Double = 0.0
    )

    data class BargainFriends(
            val BargainPrice: Double = 0.0,
            val Headimgurl: String = "",
            val WxNiceName: String = "",
            val Type: Int = 0
    )

    data class MemberDetails(
            val Headimgurl: String = "",
            val WxNiceName: String = ""
    )
}

data class Products(
        val Offset: Int = 0,
        val Total: Int = 0,
        val Results: List<ProductSelects> = listOf()
) {
    data class ProductSelects(
            var BrandId: Int = 0,
            var Name: String = "",
            var Id: Int = 0,
            var CanSaleStock: Int= 0,
            var SalePrice: String = "",
            var Description: String = "",
            var MarketPrice: String = "",
            var Stock: Int = 0,
            var LockStock: Int = 0,
            var SaleMinPrice: Double = 0.0,
            var SaleMaxPrice: Double = 0.0,
            var ImgUrl: String = "",
            var ProductId: Int = 0,
            var RecordNum: Int = 0
    )
}

data class BargainGoodsDetailInfo(
        val activityFriendsHelpDetails: ActivityFriendsHelpDetails = ActivityFriendsHelpDetails()
) {
    data class ActivityFriendsHelpDetails(
            val ProducDetails: ProducDetails = ProducDetails(),
            val CustomerBargainId: Int = 0,
            val State: Int = 0,
            val ShareUrl: String = "",
            val Timer: Int = 0
    )

    data class ProducDetails(
            val ProductName: String = "",
            val ShareUrl: String = "",
            val ProductImgUrl: String = "",
            val ProductId: Int = 0,
            val ProductPrice: Double = 0.0,
            val SpecId: Int = 0,
            val State: Int = 0,
            val Price: Double = 0.0,
            val ActivityBargainId: Int = 0,
            val PayExpired: Int = 0,
            val CutPrice: Double = 0.0,
            val BargainNum: Int = 0,
            val ImgUrls: List<String> = listOf(),
            val SepList:List<HSepList> = listOf()
    )

    data class HSepList(
            var Id: Int = 0,
            var SpecValue: String = "",
            var SpecValue2: String = "",
            var RealStock: Int = 0,
            var Price: Double = 0.0,
            var MarketPrice: Double = 0.0
    )

}

data class BargainShareData(
        val Price: Double = 0.0
)

data class CommentLists(
        val Offset: Int = 0,
        val Total: Int = 0,
        val Results: List<Commentions> = listOf()
) {
    data class Commentions(
            val Id: Int = 0,
            val HeadUrl: String = "",
            val UserName: String = "",
            val CommentTime: String = "",
            val Comment: String = "",
            val ArticleId: Int = 0,
            val GrassMasterId: Int = 0,
            val GrassMasterType: Int = 0,
            val IsCollection: Int = 0,
            val CommentReplyList: List<CommentReplys> = listOf()
    ) {
        data class CommentReplys(
                val Id: Int = 0,
                val HeadUrl: String = "",
                val UserName: String = "",
                val CommentTime: String = "",
                val Comment: String = "",
                val ArticleId: Int = 0,
                val GrassMasterId: Int = 0,
                val GrassMasterType: Int = 0,
                val CommentFlag: Int = 0,
                val ReplyGrassMasterId: Int = 0,
                val ReplyUserName: String = "",
                val ReplyHeadUrl: String = "",
                val ReplyGrassMasterType: Int = 0,
                val IsSelfReplyComment: Boolean = false
        )
    }
}


data class GroupContinueList(
        val Offset: Int = 0,
        val Total: Int = 0,
        val Results: List<Result> = listOf()
) {
    data class Result(
            val Id: Int = 0,
            val HeadUrl: String = "",
            val WxNickname: String = "",
            val Remain: Int = 0,
            val Ends: Long = 0,
            val CanJoin: Boolean = false
    )
}


data class GroupSuccessResultData(
        val PaySuccess: Boolean = false,
        val Success: Boolean = false,
        val Message: String = "",
        val GroupBuyId: String = "",
        val HeadUrls: List<String> = listOf(),
        val State: String = "",
        val Remain: String = "",
        val Ends: String = "",
        val MallDescription: String = "",
        val ShareLink: String = ""
)

data class LikeRecord(
        val LikeCount: Int = 0,
        val GrassMasterName: String = ""
)

