package com.base.project.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/8.
 */

public class YiYuanDeviceBean {
    public String db_sbID;
    public String deviceType;
    public int clusterNUM;
    public List<PayloadBean> payload;

    public String getDb_sbID() {
        return db_sbID;
    }

    public void setDb_sbID(String db_sbID) {
        this.db_sbID = db_sbID;
    }

    public String getDeviceType() {
        return deviceType;
    }

    public void setDeviceType(String deviceType) {
        this.deviceType = deviceType;
    }

    public int getClusterNUM() {
        return clusterNUM;
    }

    public void setClusterNUM(int clusterNUM) {
        this.clusterNUM = clusterNUM;
    }

    public List<PayloadBean> getPayload() {
        return payload;
    }

    public void setPayload(List<PayloadBean> payload) {
        this.payload = payload;
    }
}
