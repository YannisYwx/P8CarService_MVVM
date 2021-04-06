package com.p8.common.net;

import com.p8.common.bean.Agency;
import com.p8.common.bean.Inspection;
import com.p8.common.bean.Landlords;
import com.p8.common.bean.LoginInfo;
import com.p8.common.bean.Machines;
import com.p8.common.bean.Orders;

import io.reactivex.Observable;

/**
 * @author : WX.Y
 * date : 2020/9/8 16:05
 * description : 网络请求
 */
public interface HttpHelper {

    /**
     * 账号登录
     *
     * @param username
     * @param password
     * @return
     */
    Observable<HttpResponse<LoginInfo>> doLoginByLandlord(String username, String password);

    /**
     * 大主登录
     *
     * @param loginName
     * @param password
     * @return
     */
    Observable<HttpResponse<LoginInfo>> doLoginByLargeMaster(String loginName, String password);

    /**
     * 获取大主(代理商)信息
     *
     * @return 大主(代理商)信息
     */
    Observable<HttpResponse<Agency>> getAgencyInfo();

    /**
     * 获取地主信息
     *
     * @return 地主(巡检员)信息
     */
    Observable<HttpResponse<Inspection>> getInspectInfo();

    /**
     * 获取地主信息列表
     *
     * @param currentPage 当前页
     * @param pageSize    一页几条
     * @return 地主信息列表
     */
    Observable<HttpResponse<Landlords>> getLandlords(int currentPage, int pageSize);

    /**
     * 添加地主
     *
     * @param phone    手机号码
     * @param realName 真实姓名
     * @return
     */
    Observable<HttpResponse<String>> addLandlord(String phone, String realName);


    /**
     * 获取订单列表
     *
     * @param currentPage 当前页
     * @param pageSize    一页几条
     * @return
     */
    Observable<HttpResponse<Orders>> getOrders(int currentPage, int pageSize);

//    /**
//     * 获取验证码
//     *
//     * @param phoneNum 手机号
//     * @return
//     */
//    Observable<HttpResponse<VCode>> getVCode(String phoneNum);

    /**
     * 重置密码
     *
     * @param url         地址
     * @param newPassword 新密码
     * @param oldPassword 旧密码
     * @return
     */
    Observable<HttpResponse<String>> resetPassword(String url, String newPassword, String oldPassword);

    /**
     * 获取设备列表
     *
     * @return
     */
    Observable<HttpResponse<Machines>> getMachines(String address, String parkingStatus, int currentPage);

    /**
     * 绑定设备
     *
     * @param address
     * @param parkingNumber
     * @param lat
     * @param lng
     * @return 绑定设备
     */
    Observable<HttpResponse<Object>> bindDevice(String address, String parkingNumber, String lat, String lng);

}
