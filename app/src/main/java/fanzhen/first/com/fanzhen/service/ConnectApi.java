package fanzhen.first.com.fanzhen.service;

import com.dyl.base_lib.model.RepModel;
import com.dyl.base_lib.model.ReqModel;
import com.dyl.base_lib.net.ApiPath;
import com.first.chujiayoupin.model.RAuth;
import com.first.chujiayoupin.model.RCheckCode;
import com.first.chujiayoupin.model.RLogin;
import com.first.chujiayoupin.model.RSmsCode;

import java.util.List;

import fanzhen.first.com.fanzhen.model.CRepModel;
import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by dengyulin on 2018/4/17.
 */
public interface ConnectApi {

    @POST("/Storehome/test")
    Call<RepModel> test1(@Body ReqModel reqModel);
    @GET("/Storehome/test")
    Call<RepModel> test2(@Path("xxx") String xxx);
    @PUT("/Storehome/test")
    Call<RepModel> test3(@Body ReqModel reqModel);
    @GET("/Storehome/test")
    Call<RepModel> test4(@Query("xxx") String xxx);

    @ApiPath("http://api.xsmore.com/api/")
    interface VerifyPhone{
        //
        @POST("/sms/send")
        Call<CRepModel<Object>> sendSmsCode(@Body RSmsCode reqModel);
        //
        @POST("/sms/check")
        Call<CRepModel<Object>> checkSmsCode(@Body RCheckCode reqModel);
    }
    //
    @POST("/activity/HelpList")
    Call<CRepModel<String>> logins(RLogin rLogin);
    @ApiPath("http://file.xsmore.com/api/")
    interface UpImage{
        //
        @Multipart
        @POST("/image/upload")
        Call<List<String>> UpLoadImage(@Query("PlatForm") String PlatForm, @Query("Path") String Path, @Part MultipartBody.Part file);
    }

    @POST("/activity/HelpList")
    Call<CRepModel<String>> auth(RAuth rAuth);

    //登录
    @POST("/auth")
    Call<CRepModel<Object>> login(@Body RLogin reqModel);

}
