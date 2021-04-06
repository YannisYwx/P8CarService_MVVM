package com.p8.common.net.download;

/**
 * @author : WX.Y
 * date : 2020/9/24 15:39
 * description : 需要下载的文件
 */
public class DownloadFile {
    /**
     * 下载路径
     */
    private String url;
    /**
     * 下载标识
     */
    private String tag;
    /**
     * 本地路径
     */
    private String desPath;
    /**
     * 文件名
     */
    private String fileName;
    /**
     * 下载状态
     */
    private int state;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getDesPath() {
        return desPath;
    }

    public void setDesPath(String desPath) {
        this.desPath = desPath;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "DownloadFile{" +
                "url='" + url + '\'' +
                ", tag='" + tag + '\'' +
                ", desPath='" + desPath + '\'' +
                ", fileName='" + fileName + '\'' +
                ", state=" + state +
                '}';
    }
}

