package com.p8.common.util;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.ClientException;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.ServiceException;
import com.alibaba.sdk.android.oss.callback.OSSCompletedCallback;
import com.alibaba.sdk.android.oss.common.OSSLog;
import com.alibaba.sdk.android.oss.common.auth.OSSCredentialProvider;
import com.alibaba.sdk.android.oss.common.auth.OSSFederationToken;
import com.alibaba.sdk.android.oss.common.auth.OSSStsTokenCredentialProvider;
import com.alibaba.sdk.android.oss.internal.OSSAsyncTask;
import com.alibaba.sdk.android.oss.model.PutObjectRequest;
import com.alibaba.sdk.android.oss.model.PutObjectResult;
import com.blankj.utilcode.util.CacheDoubleUtils;
import com.blankj.utilcode.util.ConvertUtils;
import com.blankj.utilcode.util.LogUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luck.picture.lib.entity.LocalMedia;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.p8.common.Constants.KEY_TOKEN;

/**
 * @author : WX.Y
 * date : 2020/10/14 11:25
 * description :
 */
public class AliOssManager {

    public static final String BUCKET_NAME = "p8bucket";

    private OSSClient oss;
    private List<byte[]> files = new ArrayList<>();
    private List<String> urls = new ArrayList<>();
    private HashMap<Integer, Object> hashMap = new HashMap();
    private int maxNumber = 4;

    private OnUpdateListener mListener;

    private static final class Holder {
        private static AliOssManager INSTANCE = new AliOssManager();
    }

    public static AliOssManager getInstance() {
        return Holder.INSTANCE;
    }

    private AliOssManager() {

    }

    public void init(Context context) {
        OSSCredentialProvider credentialProvider = new OSSStsTokenCredentialProvider("LTAIvHXvK7gMkHgW", "97rQSVFAuDsDOFfEqlMe3hZYQv6ltA", "") {
            @Override
            public OSSFederationToken getFederationToken() {
                //可实现自动获取token
                return super.getFederationToken();
            }
        };
        ClientConfiguration conf = new ClientConfiguration();
        /*连接超时，默认15秒*/
        conf.setConnectionTimeout(15 * 1000);
        /*socket超时，默认15秒*/
        conf.setSocketTimeout(15 * 1000);
        /*最大并发请求数，默认5个*/
        conf.setMaxConcurrentRequest(5);
        /*失败后最大重试次数，默认2次*/
        conf.setMaxErrorRetry(2);
        OSSLog.enableLog();
        oss = new OSSClient(context, "http://oss-cn-shenzhen.aliyuncs.com", credentialProvider, conf);
    }


    /**
     * 上传多文件
     *
     * @param files
     */
    public void uploadFiles(List<byte[]> files) {
        if (null == files || files.size() == 0) {
            return;
        } // 上传文件
        ossUpload(files);
    }

    public void uploadFiles() {
        for (int i = 0; i < maxNumber; i++) {
            byte[] bytes = (byte[]) hashMap.get(i);
            if (bytes == null) {
                continue;
            }
            files.add(bytes);
        }
        urls.clear();
        ossUpload(AliOssManager.this.files);
    }


    public void uploadFiles(Context context, String[] paths) {
        for (String path : paths) {
            Glide.with(context).asBitmap().load(path).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    Bitmap bitmap = GlideUtils.compressScale(resource);
                    byte[] bytes = ConvertUtils.bitmap2Bytes(bitmap, Bitmap.CompressFormat.JPEG, 80);
                    files.add(bytes);
                    if (files.size() == paths.length) {
                        urls.clear();
                        ossUpload(AliOssManager.this.files);
                    }
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
        }
    }

    public void uploadFiles(Context context, List<LocalMedia> selectList) {
        for (LocalMedia localMedia : selectList) {
            LogUtils.eTag("wzh", "localMedia.getPath(): " + localMedia.getPath());

            Glide.with(context).asBitmap().load(localMedia.getPath()).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    Bitmap bitmap = GlideUtils.compressScale(resource);
                    byte[] bytes = ConvertUtils.bitmap2Bytes(bitmap, Bitmap.CompressFormat.JPEG, 80);
                    files.add(bytes);
                    if (files.size() == selectList.size()) {
                        LogUtils.eTag("wzh", "localMedia.getPath(): " + localMedia.getPath());
                        urls.clear();
                        ossUpload(AliOssManager.this.files);
                    }
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
        }
    }

    /**
     * 阿里云OSS上传（默认是异步多文件上传）
     *
     * @param files
     */
    private OSSAsyncTask ossUpload(final List<byte[]> files) {
        if (files.size() <= 0) {
            // 文件全部上传完毕，这里编写上传结束的逻辑，如果要在主线程操作，最好用Handler或runOnUiThead做对应逻辑
            return null;
        }
        final byte[] file = files.get(0);
        if (file.length == 0) {
            files.remove(0);
            // url为空就没必要上传了，这里做的是跳过它继续上传的逻辑。
            ossUpload(files);
            return null;
        }
        String fileSuffix = ".jpg";
        // 文件标识符objectKey
        final String objectKey = "img_" + CacheDoubleUtils.getInstance().getString(KEY_TOKEN) + "_" + System.currentTimeMillis() + fileSuffix;
        // 下面3个参数依次为bucket名，ObjectKey名，上传文件路径
        PutObjectRequest put = new PutObjectRequest(BUCKET_NAME, objectKey, file);
        // 异步上传
        OSSAsyncTask task = oss.asyncPutObject(put,
                new OSSCompletedCallback<PutObjectRequest, PutObjectResult>() {
                    @Override
                    public void onSuccess(PutObjectRequest request, PutObjectResult result) {
                        urls.add(objectKey);
                        files.remove(0);
                        LogUtils.dTag("wzh", "urls: " + urls.toString());
                        if (files.size() <= 0) {
                            mListener.onSuccess(urls);
                            return;
                        }
                        ossUpload(files);// 递归同步效果
                    }

                    @Override
                    public void onFailure(PutObjectRequest request, ClientException clientException,
                                          ServiceException serviceException) {
                        mListener.onFailure();
                        // 请求异常
                        if (clientException != null) {
                            // 本地异常如网络异常等
                            clientException.printStackTrace();
                        }
                        if (serviceException != null) {
                            // 服务异常
                            LogUtils.eTag("ErrorCode", serviceException.getErrorCode());
                            LogUtils.eTag("RequestId", serviceException.getRequestId());
                            LogUtils.eTag("HostId", serviceException.getHostId());
                            LogUtils.eTag("RawMessage", serviceException.getRawMessage());
                        }
                    }
                });
        return task;
    }

    public List<byte[]> getUploadFiles() {
        return files;
    }

    public List<byte[]> getFiles() {
        return files;
    }

    public int getMaxNumber() {
        return maxNumber;
    }

    public void setMaxNumber(int maxNumber) {
        this.maxNumber = maxNumber;
    }

    public HashMap<Integer, Object> getHashMap() {
        return hashMap;
    }

    public void setHashMap(HashMap<Integer, Object> hashMap) {
        this.hashMap = hashMap;
    }

    public interface OnUpdateListener {
        /**
         * 文件上传成功回调
         *
         * @param urls 需要上传的文件urls
         */
        void onSuccess(List<String> urls);

        /**
         * 文件上传失败回调
         */
        void onFailure();

    }

    public void setOnUpdateListener(OnUpdateListener listener) {
        mListener = listener;
    }
}

