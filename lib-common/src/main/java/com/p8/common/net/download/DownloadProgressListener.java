package com.p8.common.net.download;

/**
 * @author : WX.Y
 * date : 2020/9/24 16:19
 * description :
 */
public interface DownloadProgressListener {
    /**
     * 下载进度
     *
     * @param totalSize 总长度
     * @param downSize  下载长度
     * @param done      是否下载完毕
     */
    void onProgress(long totalSize, long downSize, boolean done);
}
