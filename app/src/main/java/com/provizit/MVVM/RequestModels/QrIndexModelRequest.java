package com.provizit.MVVM.RequestModels;

public class QrIndexModelRequest {

    String cid;
    String key;
    String img;

    public QrIndexModelRequest(String cid, String key, String img) {
        this.cid = cid;
        this.key = key;
        this.img = img;
    }


    public String getCid() {
        return cid;
    }

    public void setCid(String cid) {
        this.cid = cid;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }
}
