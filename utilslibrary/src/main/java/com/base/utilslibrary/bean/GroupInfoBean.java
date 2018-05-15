package com.base.utilslibrary.bean;

import java.util.List;

/**
 * Created by Administrator on 2017/9/8.
 */

public class GroupInfoBean {

    public String mg_id;//id
    public String mg_name;//家庭名称
    public String mg_ui_id;//
    public String mg_m_ui_id;//
    public String mg_create_ui_id;//
    public String role;//身份 0是普通成员 1是管理员 2是组长

    public String mg_memo;
    public String mg_parent_id_dir;
    public String mg_parent_id;
    public String mg_status;
    public String mg_mgt_id;
    public String mg_pic;

    public String room_id;
    public String invite_ui_phone;
    public String ui_id;
    public String invite_ui_id;
    public String mg_m_type;
    public String ui_phone;
    public String ui_nc;
    public String ctrl;

    public boolean idHide = true;//保证刷新的时候不会串位
    public boolean isIdHide() {
        return idHide;
    }
    public void setIdHide(boolean idHide) {
        this.idHide = idHide;
    }

    public List<GroupInfoBean> child;
    public String getMg_id() {
        return mg_id;
    }

    public String getMg_name() {
        return mg_name;
    }

    public String getRole() {
        return role;
    }

    public void setMg_id(String mg_id) {
        this.mg_id = mg_id;
    }

    public void setMg_name(String mg_name) {
        this.mg_name = mg_name;
    }

    public String apply_ui_phone;
    public String apply_ui_id;
}
