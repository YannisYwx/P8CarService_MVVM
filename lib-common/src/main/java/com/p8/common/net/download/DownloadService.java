package com.p8.common.net.download;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Streaming;
import retrofit2.http.Url;

/**
 * author : WX.Y
 * date : 2020/9/24 14:29
 * description :
 */
public interface DownloadService {
    //https://p8bucket.oss-cn-shenzhen.aliyuncs.com/J1-0.pdf
    //https://p8bucket.oss-cn-shenzhen.aliyuncs.com/J1-1.pdf
    //https://p8bucket.oss-cn-shenzhen.aliyuncs.com/J1-2.pdf

    String HOST = "https://p8bucket.oss-cn-shenzhen.aliyuncs.com/";
//    String HOST = "https://p8bucket.oss-cn-shenzhen.aliyuncs.com/J1-0.pdf";

    /**
     * @param start 从某个字节开始下载数据
     * @param url   文件下载的url
     * @return Observable
     * @Streaming 这个注解必须添加，否则文件全部写入内存，文件过大会造成内存溢出
     */
    @Streaming
    @GET
    Observable<ResponseBody> download(@Url String url);
}

