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
                //?????????????????????token
                return super.getFederationToken();
            }
        };
        ClientConfiguration conf = new ClientConfiguration();
        /*?????????????????????15???*/
        conf.setConnectionTimeout(15 * 1000);
        /*socket???????????????15???*/
        conf.setSocketTimeout(15 * 1000);
        /*??????????????????????????????5???*/
        conf.setMaxConcurrentRequest(5);
        /*????????????????????????????????????2???*/
        conf.setMaxErrorRetry(2);
        OSSLog.enableLog();
        oss = new OSSClient(context, "http://oss-cn-shenzhen.aliyuncs.com", credentialProvider, conf);
    }


    /**
     * ???????????????
     *
     * @param files
     */
    public void uploadFiles(List<byte[]> files) {
        if (null == files || files.size() == 0) {
            return;
        } // ????????????
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
     * ?????????OSS??????????????????????????????????????????
     *
     * @param files
     */
    private OSSAsyncTask ossUpload(final List<byte[]> files) {
        if (files.size() <= 0) {
            // ??????????????????????????????????????????????????????????????????????????????????????????????????????Handler???runOnUiThead???????????????
            return null;
        }
        final byte[] file = files.get(0);
        if (file.length == 0) {
            files.remove(0);
            // url??????????????????????????????????????????????????????????????????????????????
            ossUpload(files);
            return null;
        }
        String fileSuffix = ".jpg";
        // ???????????????objectKey
        final String objectKey = "img_" + CacheDoubleUtils.getInstance().getString(KEY_TOKEN) + "_" + System.currentTimeMillis() + fileSuffix;
        // ??????3??????????????????bucket??????ObjectKey????????????????????????
        PutObjectRequest put = new PutObjectRequest(BUCKET_NAME, objectKey, file);
        // ????????????
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
                        ossUpload(files);// ??????????????????
                    }

                    @Override
                    public void onFailure(PutObjectRequest request, ClientException clientException,
                                          ServiceException serviceException) {
                        mListener.onFailure();
                        // ????????????
                        if (clientException != null) {
                            // ??????????????????????????????
                            clientException.printStackTrace();
                        }
                        if (serviceException != null) {
                            // ????????????
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
         * ????????????????????????
         *
         * @param urls ?????????????????????urls
         */
        void onSuccess(List<String> urls);

        /**
         * ????????????????????????
         */
        void onFailure();

    }

    public void setOnUpdateListener(OnUpdateListener listener) {
        mListener = listener;
    }
}

