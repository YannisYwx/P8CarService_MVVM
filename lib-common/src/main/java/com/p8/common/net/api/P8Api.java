package com.p8.common.net.api;


import com.p8.common.bean.Agency;
import com.p8.common.bean.Inspection;
import com.p8.common.bean.Landlords;
import com.p8.common.bean.LoginInfo;
import com.p8.common.bean.LoginResponse;
import com.p8.common.bean.Machines;
import com.p8.common.bean.Orders;
import com.p8.common.net.HttpResponse;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * @author : WX.Y
 * date : 2020/9/8 16:09
 * description :
 * * ---------------------------------------------------------------------------------------------------
 * * @GET 表明这是get请求
 * * @POST 表明这是post请求
 * * @PUT 表明这是put请求
 * * @DELETE 表明这是delete请求
 * * @PATCH 表明这是一个patch请求，该请求是对put请求的补充，用于更新局部资源
 * * @HEAD 表明这是一个head请求
 * * @OPTIONS 表明这是一个option请求
 * * @HTTP 通用注解, 可以替换以上所有的注解，其拥有三个属性：method，path，hasBody
 * * @Headers 用于添加固定请求头，可以同时添加多个。通过该注解添加的请求头不会相互覆盖，而是共同存在
 * * @Header 作为方法的参数传入，用于添加不固定值的Header，该注解会更新已有的请求头
 * * @Body 多用于post请求发送非表单数据, 比如想要以post方式传递json格式数据
 * * @Filed 多用于post请求中表单字段, Filed和FieldMap需要FormUrlEncoded结合使用
 * * @FiledMap 和@Filed作用一致，用于不确定表单参数
 * * @Part 用于表单字段, Part和PartMap与Multipart注解结合使用, 适合文件上传的情况
 * * @PartMap 用于表单字段, 默认接受的类型是Map<String,REquestBody>，可用于实现多文件上传
 * * <p>
 * * Part标志上文的内容可以是富媒体形势，比如上传一张图片，上传一段音乐，即它多用于字节流传输。
 * * 而Filed则相对简单些，通常是字符串键值对。
 * * </p>
 * * Part标志上文的内容可以是富媒体形势，比如上传一张图片，上传一段音乐，即它多用于字节流传输。
 * * 而Filed则相对简单些，通常是字符串键值对。
 * * @Path 用于url中的占位符,{占位符}和PATH只用在URL的path部分，url中的参数使用Query和QueryMap代替，保证接口定义的简洁
 * * @Query 用于Get中指定参数
 * * @QueryMap 和Query使用类似
 * * @Url 指定请求路径
 * * ----------------------------------------------------------------------------------------------------------
 */
public interface P8Api {

    String HOST = "http://service.p8.world";

    /*=========================== 大主 =========================== Start*/

    /**
     * 大主登录
     *
     * @param loginName
     * @param password
     * @return
     */
    @POST("/app_agency/login.html")
    Observable<HttpResponse<LoginInfo>> doLoginByLargeMaster(@Query("loginName") String loginName, @Query("password") String password);

    /**
     * 重置密码
     *
     * @param url         地址  大主：/app_agency/change_pwd.html
     * @param newPassword 新密码
     * @param oldPassword 旧密码
     * @return
     */
    @POST
    Observable<HttpResponse<String>> resetPassword(@Url String url, @Query("newPassword") String newPassword, @Query("oldPassword") String oldPassword);

    /**
     * 获取大主信息
     *
     * @return
     */
    @GET("/app_agency/info.html")
    Observable<HttpResponse<Agency>> getAgencyInfo();

    /**
     * 获取地主信息列表
     *
     * @param currentPage 当前页
     * @param pageSize    一页几条
     * @return
     */
    @GET("/app_agency/inspect/list.html")
    Observable<HttpResponse<Landlords>> getLandlords(@Query("currentPage") int currentPage, @Query("pageSize") int pageSize);

    /**
     * 获取订单列表
     *
     * @param currentPage 当前页
     * @param pageSize    一页几条
     * @return
     */
    @GET("/merchant/order/query_list.html")
    Observable<HttpResponse<Orders>> getOrders(@Query("currentPage") int currentPage, @Query("pageSize") int pageSize);

    /**
     * 添加地主
     *
     * @param phone    手机号码
     * @param realName 真实姓名
     * @return
     */
    @POST("/app_agency/inspect/add.html")
    Observable<HttpResponse<String>> addLandlord(@Query("phone") String phone, @Query("realName") String realName);

    /*=========================== 大主 =========================== End*/


    /*=========================== 地主 =========================== Start*/

    /**
     * 登录
     *
     * @param loginName
     * @param password
     * @return
     */
    @POST("/app_inspect/login.html")
    Observable<HttpResponse<LoginInfo>> doLoginByLandlord(@Query("loginName") String loginName, @Query("password") String password);


    /**
     * 获取地主信息
     *
     * @return 地主(巡检员)信息
     */
    @GET("/app_inspect/info.html")
    Observable<HttpResponse<Inspection>> getInspectInfo();

//    /**
//     * 获取验证码
//     *
//     * @param phone
//     * @return
//     */
//    @GET("/app/inspect/getCode.html")
//    Observable<HttpResponse<VCode>> getCode(@Query("mobile") String phone);
//
//    /**
//     * 获取省列表
//     *
//     * @return
//     */
//    @GET("/app_inspect/region/provinces.html")
//    Observable<HttpResponse<Provinces>> getProvinces();
//
//    /**
//     * 获取城市列表
//     *
//     * @return
//     */
//    @GET("/app_inspect/region/cities.html")
//    Observable<HttpResponse<Cities>> getCites(@Query("provinceId") String provinceId);
//
//    /**
//     * 获取区列表
//     *
//     * @return
//     */
//    @GET("/app_inspect/region/areas.html")
//    Observable<HttpResponse<Areas>> getAreas(@Query("cityId") String cityId);
//
//    /**
//     * 获取街道列表
//     *
//     * @param address
//     * @return
//     */
//    @GET("/app_inspect/region/street.html")
//    Observable<HttpResponse<Streets>> getStreets(@Query("address") String address);

    /**
     * 获取设备列表
     *
     * @return
     */
    @GET("/app_inspect/machine/list.html")
    Observable<HttpResponse<Machines>> getMachines(@Query("address") String address, @Query("parkingstatus") String parkingStatus, @Query("currentPage") int currentPage);

    /**
     * 绑定设备设备列表
     *
     * @param address       地址
     * @param parkingNumber 泊位号
     * @param lat           纬度
     * @param lng           经度
     * @return
     */
    @POST("/app_inspect/machine/bind.html")
    Observable<HttpResponse<Object>> bindDevice(@Query("address") String address, @Query("parkingNumber") String parkingNumber,
                                                @Query("lat") String lat, @Query("lng") String lng);


    @Streaming
    @GET
    Observable<ResponseBody> downloadFile(@Url String fileUrl);

    @GET("/parking/app/order/by_number.html")
    Observable<HttpResponse<LoginResponse>> getOrderNumber(@Query("token") String token, @Query("parkingNumber") String parkingNumber);

    //    @POST("/parking/alipay/pay.html")//ali支付
    @POST("/wx/alipay/pay_data.html")
    //ali支付
    Observable<HttpResponse<LoginResponse>> alipay(@Query("serialNumber") String serialNumber, @Query("money") String money, @Query("ref") String ref);

    // @POST("/parking/wechatpay/pay.html")//wechat支付
    @POST("/wx/wechatPay/pay_data.html")
    Observable<HttpResponse<LoginResponse>> wechatPay(@Query("serialNumber") String serialNumber, @Query("money") String money, @Query("ref") String ref);

}
