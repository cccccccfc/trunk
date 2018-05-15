package com.base.project.bean;

/**
 * Created by Administrator on 2017/9/8.
 */

public class PayloadBean {

    public DataBean data;
    public InfoBean info;
    public int clusterID;

    public void setData(DataBean data) {
        this.data = data;
    }

    public DataBean getData() {
        return data;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public void setClusterID(int clusterID) {
        this.clusterID = clusterID;
    }

    public InfoBean getInfo() {
        return info;
    }

    public int getClusterID() {
        return clusterID;
    }
}
