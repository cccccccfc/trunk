package com.base.project.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/8.
 */

public class BasicBean {
    public String db_sbID;
    public String deviceType;
    public String clusterNUM;
    public List<PayloadBean> payload;
    public InfoBean info;
    public String location;




    public void setDb_sbID(String db_sbID) {
        this.db_sbID = db_sbID;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public void setClusterNUM(String clusterNUM) {
        this.clusterNUM = clusterNUM;
    }

    public void setPayload(List<PayloadBean> payload) {
        this.payload = payload;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDb_sbID() {
        return db_sbID;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public String getClusterNUM() {
        return clusterNUM;
    }

    public List<PayloadBean> getPayload() {
        return payload;
    }

    public InfoBean getInfo() {
        return info;
    }

    public String getLocation() {
        return location;
    }
}
