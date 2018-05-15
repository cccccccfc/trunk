package com.base.utilslibrary.bean;

import java.util.List;

/**
 * @author Admin
 * @time 2017/4/7 9:44
 * @des ${TODO}
 */

public class DataInfo {
    
    public String ui_id;//用户的ID
    public String sum;//用户下的家庭组数量，包括家庭和楼层、房间。
    public String code;//验证码
    public String role;//权限
    public UserInfoBean user_info;//个人信息
    public GroupInfoBean group_info;//家庭管理信息
    public GroupInfoBean groups_list;//添加成功后的信息
    public List<GroupInfoBean> list;//家庭管理下的房间管理信息
    public List<UserPowerInfo> user_list;//成员与权限信息
    public List<HistoryListBean> history_list;  //设备历史操作记录
    public List<List<CtrlListBean>> device_lists;; //用户下的设备列表
    public int ctrl;// 0只能看到设备 不能控制  1 能控制设备不能修改 2能修改设备
    public GroupInfoBean invite;//邀请成员

    public List<GroupInfoBean> home_list;//主页下的家庭组信息
    public List<CtrlListIndexBean> device_list;//主页下的默认设备列表
    public List<SceneListBean> scene_list; //用户情景模式列表
    public List<List<CtrlListBean>> execute_list; //查询出某个情景内可使用的操作
    public List<RecordListBean> record_list; //报警图片

    public List<CtrlListIndexBean> ctrl_list;//用户下的设备列表 1

    public String token;//摄像头 token
    public String url;//摄像头 url
    public String appkey;//摄像头 appkey

    public List<BluerecordForuserBean> bluerecord_list; //位置信息
   public String cardid ;
}
