package com.base.utilslibrary.bean;

/**
 * Created by Administrator on 2017/11/30.
 */

public class RecordListBean {

    /**
     * ci_id : 2771
     * ci_db_sbID : 10017
     * ci_time : 2017-11-28 18:00:07
     * pic : /upload/device/camera/0bbd16f2b6d0d814f562b00f91bfbeb0.jpg
     * type : 1
     */

    public String ci_id;
    public String ci_db_sbID;
    public String ci_time;

    public String getCi_id() {
        return ci_id;
    }

    public void setCi_id(String ci_id) {
        this.ci_id = ci_id;
    }

    public String getCi_db_sbID() {
        return ci_db_sbID;
    }

    public void setCi_db_sbID(String ci_db_sbID) {
        this.ci_db_sbID = ci_db_sbID;
    }

    public String getCi_time() {
        return ci_time;
    }

    public void setCi_time(String ci_time) {
        this.ci_time = ci_time;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String pic;
    public int type;
}
