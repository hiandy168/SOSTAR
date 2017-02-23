package com.renyu.sostar.bean;

/**
 * Created by renyu on 2017/2/24.
 */

public class UploadResponse {
    /**
     * fid : 2,067f3025a4
     * fileName :
     * fileUrl : 127.0.0.1:8081/2,067f3025a4
     * size : 50
     */

    private String fid;
    private String fileName;
    private String fileUrl;
    private int size;

    public String getFid() {
        return fid;
    }

    public void setFid(String fid) {
        this.fid = fid;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileUrl() {
        return fileUrl;
    }

    public void setFileUrl(String fileUrl) {
        this.fileUrl = fileUrl;
    }

    public int getSize() {
        return size;
    }

    public void setSize(int size) {
        this.size = size;
    }
}
