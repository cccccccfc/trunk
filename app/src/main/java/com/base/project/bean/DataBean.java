package com.base.project.bean;

/**
 * Created by Administrator on 2017/9/8.
 */

public class DataBean {

    public String location;
    public String cmd;
    public String sw;

    public String temp;
    public String humi; //湿度
    public String co2;
    public String pm25;
    public String tvoc;
    public String ch2o; //可燃气体

    public String dew_point;
    public String light; //光照
    public String noise; // 噪音
    public String smoke; //烟雾
    public String uv;  //紫外线

    public int ircmd;

    public void setLocation(String location) {
        this.location = location;
    }

    public void setCmd(String cmd) {
        this.cmd = cmd;
    }

    public String getLocation() {
        return location;
    }

    public String getCmd() {
        return cmd;
    }

    /**
     * 空气进化器
     */
    public String pm;
    public String battery;
    public String state;

    public String getPm() {
        return pm;
    }

    public void setPm(String pm) {
        this.pm = pm;
    }

    public String getBattery() {
        return battery;
    }

    public void setBattery(String battery) {
        this.battery = battery;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    @Override
    public String toString() {
        return "DataBean{" +
                "location='" + location + '\'' +
                ", cmd='" + cmd + '\'' +
                ", sw='" + sw + '\'' +
                ", temp='" + temp + '\'' +
                ", humi='" + humi + '\'' +
                ", co2='" + co2 + '\'' +
                ", pm25='" + pm25 + '\'' +
                ", tvoc='" + tvoc + '\'' +
                ", ch2o='" + ch2o + '\'' +
                ", dew_point='" + dew_point + '\'' +
                ", light='" + light + '\'' +
                ", noise='" + noise + '\'' +
                ", smoke='" + smoke + '\'' +
                ", uv='" + uv + '\'' +
                ", pm='" + pm + '\'' +
                ", battery='" + battery + '\'' +
                ", state='" + state + '\'' +
                '}';
    }
}
