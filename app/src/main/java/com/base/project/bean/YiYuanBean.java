package com.base.project.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/8.
 */

public class YiYuanBean {
    public List<YiYuanDeviceBean> deviceDetail;
    public InfoBean info;

    public List<YiYuanDeviceBean> getDeviceDetail() {
        return deviceDetail;
    }

    public void setDeviceDetail(List<YiYuanDeviceBean> deviceDetail) {
        this.deviceDetail = deviceDetail;
    }

    public InfoBean getInfo() {
        return info;
    }

    public void setInfo(InfoBean info) {
        this.info = info;
    }
}
