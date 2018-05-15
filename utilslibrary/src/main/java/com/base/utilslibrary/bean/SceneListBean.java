package com.base.utilslibrary.bean;

/**
 * Created by Administrator on 2017/11/20.
 */

public class SceneListBean {


    /**
     * ms_id : 8
     * ms_name : 情景2
     */

    public String ms_id;
    public String ms_name;


    public String getMs_type() {
        return ms_type;
    }

    public void setMs_type(String ms_type) {
        this.ms_type = ms_type;
    }

    public String ms_type;
    public boolean isshow ;

    public String getMs_id() {
        return ms_id;
    }

    public void setMs_id(String ms_id) {
        this.ms_id = ms_id;
    }

    public String getMs_name() {
        return ms_name;
    }

    public void setMs_name(String ms_name) {
        this.ms_name = ms_name;
    }

    public boolean isshow() {
        return isshow;
    }

    public void setIsshow(boolean isshow) {
        this.isshow = isshow;
    }
}
