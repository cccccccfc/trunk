package com.base.utilslibrary.bean;

/**
 * @author IMXU
 * @time 2017/10/25 17:47
 * @des ${TODO}
 * 邮箱：butterfly_xu@sina.com
 */

public interface ProjectUrlInfo {
     String service_host_address = "http://icadmin.lchtime.cn:8001/index.php/";
     String phtot_address = "http://icadmin.lchtime.cn:8001";


     String reg = "user/user/register";
     String getLogin = "user/user/login";
     String sendCode = "user/user/sendsms";
     String resetpwd = "user/user/resetpwd";
     String userinfo = "user/user/userinfo";
     String modifydetail = "user/user/modifydetail";//修改头像
     String modifyuser = "user/user/modifyuser";//编辑个人信息
     String userrole = "user/user/userrole";//获取个人权限信息
     String numbergroup = "user/user/numbergroup";//获取个人的拥有家庭组数量


     String myidentity = "user/group/myidentity";//查看用户组信息
     String addgroup = "user/group/addgroup";//创建家庭或者房间
     String delgroup = "user/group/delgroup";//删除家庭或者房间
     String quitgroup = "user/group/quitgroup";//退出家庭或者房间
     String modifygroup = "user/group/modifygroup";//修改家庭名称
     String listtouser = "user/group/listtouser";//查询成员与权限
     String inviteuser = "user/group/inviteuser";//查询成员与权限
     String agreeinvite = "user/group/agreeinvite";//编辑个人信息
     String indexIdentity = "user/group/indexIdentity";//获取主页信息
     String addmanager = "user/group/addmanager";//增加和删除管理员身份
     String deleteManager = "user/group/adduser";//增加和删除管理员身份
     String applyuser = "user/group/applyuser";//申请加入家庭
     String agreeapply = "/user/group/agreeapply";//1.2	是否同意申请加入家庭
     String handlehistory = "device/control/gethandlehistory" ;// 获取用户操作设备的历史记录
     String ctrllist = "user/group/listtogroup" ;// 获取用户的设备
     String addscene = "user/scene/addscene" ;// 创建情景模式（包含情景删除功能）
     String listtoscene = "user/scene/listtoscene" ;// 	查询房间拥有的所有情景（情景模式已添加）
     String listtoexecute = "user/scene/listtoexecute" ;// 查询出某个情景内可使用的操作
     String getaccesstoken = "data/notice/getaccesstoken" ;// 	获取摄像头信息
     String delexcute = "user/scene/delexcute" ;// 删除情景内某个操作
     String listtotopgroup = "user/group/listtotopgroup" ;// 获取用户场景需要的设备
     String getcamerarecord = "data/data/getcamerarecord" ;// 读取摄像头记录数据
     String bindbluecard = "user/user/bindbluecard" ;// 绑定定位卡号（含解除绑定）操作类型:1为绑定 2为解除绑定
     String bluerecordforuser = "user/user/bluerecordforuser" ;// 查询定位信息
}
