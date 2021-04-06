package com.p8.common.net.download;

import java.io.File;
import java.util.List;

import io.reactivex.observers.DisposableObserver;

/**
 * @author : WX.Y
 * date : 2020/9/24 17:53
 * description :
 */
public final class DownloadObserver extends DisposableObserver<DownloadInfo> {
    List<DownloadListener> listeners;
    private String tag;

    public DownloadObserver(List<DownloadListener> listeners, String tag) {
        this.listeners = listeners;
        this.tag = tag;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (listeners != null) {
            for (DownloadListener listener : listeners) {
                listener.onStartDownload(tag);
            }
        }
    }

    @Override
    public void onNext(DownloadInfo downloadInfo) {
        File file = new File(downloadInfo.getDefaultLocalPath());
        if (!file.exists()) {
            if (listeners != null) {
                for (DownloadListener listener : listeners) {
                    listener.onFail(tag, "File is not exist");
                }
            }
        } else {
            if (listeners != null) {
                for (DownloadListener listener : listeners) {
                    listener.onFinishDownload(tag, downloadInfo);
                }
            }
        }
    }

    @Override
    public void onError(Throwable e) {
        if (listeners != null) {
            for (DownloadListener listener : listeners) {
                listener.onFail("333", e.getMessage());
            }
        }
    }

    @Override
    public void onComplete() {

    }

}

