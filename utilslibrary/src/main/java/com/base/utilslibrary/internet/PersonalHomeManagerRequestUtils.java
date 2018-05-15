package com.base.utilslibrary.internet;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.base.utilslibrary.MyDialog;
import com.base.utilslibrary.ToolUtils;
import com.base.utilslibrary.bean.GroupInfoBean;
import com.base.utilslibrary.bean.HomeManagerResult;
import com.base.utilslibrary.bean.ProjectUrlInfo;
import com.base.utilslibrary.bean.UserPowerInfo;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;
import java.util.Map;

import okhttp3.Call;

/**
 * @author Admin
 * @time 2017/4/7 9:56
 * @des ${TODO}
 */

public class PersonalHomeManagerRequestUtils {

    private static Gson mGson;
    private static ForResultListener mListener;
    private static ForResultDataListener dataListener;
    private static ForResultInfoListener mInfoListener;
    private static ForUserPowerResultInfoListener powerResultInfoListener;
    private static MyDialog dialog;
    static {
        mGson = new Gson();
    }
    /**
     * 查询房间管理信息
     * @param id 用户id
     * @param mg_id 家庭组id
     * @param listener listener
     */
    public static void houseManagerInfo(final Context context, String id, String mg_id, ForResultDataListener listener){
        ToolUtils.logMes("----------6666---------" + id +"------"+ mg_id);
        dialog = MyDialog.showDialog(context);
        dialog.show();
        dataListener = listener;

        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.myidentity;
        OkHttpUtils.post().url(url)
                .addParams("ui_id",id)
                .addParams("mg_id",mg_id)
                .addParams("type","3")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dialog.dismiss();
                dialog = null;
                ToolUtils.logMes("houseManagerInfo  e="+e );
                dataListener.onResponseMessage(null);
            }
            @Override
            public void onResponse(String response, int id) {
                dialog.dismiss();
                dialog = null;
                ToolUtils.logMes("houseManagerInfo="+response );
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                if ("0".equals(result.errno)) {
                    dataListener.onResponseMessage(result.data.list.get(0));
                } else {
                    dataListener.onResponseMessage(null);
                }
            }
        });
    }

    /**
     * 添加房间或创建家庭
     * @param ui_id 管理员id
     * @param mg_parent_id 创建家庭传0，添加房间 mg_parent_id_dir','mg_id，家庭组路径和家庭组ID，以逗号隔开
     * @param mg_name 组名称
     */
    public static void addHouse(Context context,String ui_id, String mg_parent_id, String mg_name, final ForResultDataListener listener){
        dialog = MyDialog.showDialog(context);
        dialog.show();
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.addgroup;
        OkHttpUtils.post().url(url)
                .addParams("ui_id",ui_id)
                .addParams("mg_parent_id",mg_parent_id)
                .addParams("mg_name",mg_name)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dialog.dismiss();
                dialog = null;
                ToolUtils.logMes("addHouse  e="+e);
                listener.onResponseMessage(null);
            }
            @Override
            public void onResponse(String response, int id) {
                dialog.dismiss();
                dialog = null;
                ToolUtils.logMes("addHouse="+response);
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                if ("0".equals(result.errno)) {
                    listener.onResponseMessage(result.data.groups_list);
                } else {
                    listener.onResponseMessage(null);
                }
            }
        });

    }

    /**
     * 删除房间
     * @param ui_id 用户id
     * @param mg_id 删除的id
     * @param listener
     */
    public static void deleteGroup(Context context,String ui_id, String mg_id, final ForResultListener listener){
        dialog = MyDialog.showDialog(context);
        dialog.show();
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.delgroup;
        OkHttpUtils.post().url(url)
                .addParams("ui_id",ui_id)
                .addParams("mg_id",mg_id)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dialog.dismiss();
                dialog = null;
                ToolUtils.logMes("deleteGroup  e="+e);
                listener.onResponseMessage("");
            }
            @Override
            public void onResponse(String response, int id) {
                dialog.dismiss();
                dialog = null;
                ToolUtils.logMes("deleteGroup="+response);
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                if ("0".equals(result.errno)) {
                    listener.onResponseMessage("成功");
                } else {
                    listener.onResponseMessage("");
                }
            }
        });
    }
    /**
     * 删除家庭
     * @param ui_id 用户id
     * @param mg_id 删除的id
     * @param listener
     */
    public static void deleteHomeGroup(Context context,String ui_id, String mg_id, final ForResultListener listener){
        dialog = MyDialog.showDialog(context);
        dialog.show();
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.delgroup;
        OkHttpUtils.post().url(url)
                .addParams("ui_id",ui_id)
                .addParams("mg_id",mg_id)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dialog.dismiss();
                dialog = null;
                ToolUtils.logMes("deleteHomeGroup  e="+e);
                listener.onResponseMessage("");
            }
            @Override
            public void onResponse(String response, int id) {
                dialog.dismiss();
                dialog = null;
                ToolUtils.logMes("deleteHomeGroup="+response);
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                if ("0".equals(result.errno)) {
                    listener.onResponseMessage(result.data.sum);
                } else {
                    listener.onResponseMessage("");
                }
            }
        });
    }

    /**
     * 退出家庭组
     * @param ui_id 用户id
     * @param mg_id 用户组id
     * @param type 1管理员 2普通成员
     * @param listener
     */
    public static void quitHomeGroup(Context context,String ui_id, String mg_id,String type, final ForResultListener listener){
        dialog = MyDialog.showDialog(context);
        dialog.show();
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.quitgroup;
        OkHttpUtils.post().url(url)
                .addParams("ui_id",ui_id)
                .addParams("mg_id",mg_id)
                .addParams("type",type)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dialog.dismiss();
                dialog = null;
                ToolUtils.logMes("quitHomeGroup  e="+e);
                listener.onResponseMessage("");
            }
            @Override
            public void onResponse(String response, int id) {
                dialog.dismiss();
                dialog = null;
                ToolUtils.logMes("quitHomeGroup="+response);
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                if ("0".equals(result.errno)) {
                    listener.onResponseMessage(result.data.sum);
                } else {
                    listener.onResponseMessage("");
                }
            }
        });
    }

    /**
     * 修改家庭名称
     * @param ui_id 用户id
     * @param mg_id 要修改的用户组id
     * @param name 修改的名称
     * @param listener
     */
    public static void modifygroupName(Context context,String ui_id, String mg_id,String name,final ForResultListener listener){
        dialog = MyDialog.showDialog(context);
        dialog.show();
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.modifygroup;
        OkHttpUtils.post().url(url)
                .addParams("ui_id",ui_id)
                .addParams("mg_id",mg_id)
                .addParams("mg_name",name)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dialog.dismiss();
                dialog = null;
                ToolUtils.logMes("modifygroupName  e="+e);
                listener.onResponseMessage("");
            }
            @Override
            public void onResponse(String response, int id) {
                dialog.dismiss();
                dialog = null;
                ToolUtils.logMes("modifygroupName="+response);
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                if ("0".equals(result.errno)) {
                    listener.onResponseMessage("成功");
                } else {
                    listener.onResponseMessage("");
                }
            }
        });
    }

    /**
     * 获取成员与权限信息
     * @param ui_id ub_id
     * @param mg_id 家庭组id
     * @param listener
     */
    public static void getPowerListInfo(Context context,String ui_id, String mg_id, final ForUserPowerResultInfoListener listener){
        dialog = MyDialog.showDialog(context);
        dialog.show();
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.listtouser;
        OkHttpUtils.post().url(url)
                .addParams("ui_id",ui_id)
                .addParams("mg_id",mg_id)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dialog.dismiss();
                dialog = null;
                ToolUtils.logMes("getPowerListInfo  e="+e);
                listener.onResponseMessage(null);
            }
            @Override
            public void onResponse(String response, int id) {
                dialog.dismiss();
                dialog = null;
                ToolUtils.logMes("getPowerListInfo="+response);
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                if ("0".equals(result.errno)) {
                    listener.onResponseMessage(result.data.user_list);
                } else {
                    listener.onResponseMessage(null);
                }
            }
        });
    }

    /**
     * 添加家庭成员(申请加入家庭成员)
     * @param context
     * @param ui_id 用户 id
     * @param mg_id 家庭 id
     * @param invite_ui_phone 被邀请人电话
     * @param mg_m_type 被邀请类型
     * @param type 1邀请 2申请
     * @param listener
     */
    public static void inviteMember(Context context, String ui_id, String mg_id, String invite_ui_phone
            , String mg_m_type, String type, final ForResultListener listener){
        if(TextUtils.isEmpty(invite_ui_phone)){
            ToolUtils.toastMessage("您输入的手机号为空",context);
            return;
        }
        if(!TextUtils.isEmpty(invite_ui_phone)){
            if(!ToolUtils.isMobilePhone(invite_ui_phone)){
                ToolUtils.toastMessage("您输入的手机号有误",context);
                return ;
            }
        }
        dialog = MyDialog.showDialog(context);
        dialog.show();
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.inviteuser;
        OkHttpUtils.post().url(url)
                .addParams("ui_id",ui_id)
                .addParams("mg_id",mg_id)
                .addParams("invite_ui_phone",invite_ui_phone)
                .addParams("mg_m_type",mg_m_type)
                .addParams("type",type)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dialog.dismiss();
                dialog = null;
                ToolUtils.logMes("inviteMember  e="+e);
            }
            @Override
            public void onResponse(String response, int id) {
                dialog.dismiss();
                dialog = null;
                ToolUtils.logMes("inviteMember="+response);
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                if ("0".equals(result.errno)) {
                    listener.onResponseMessage("成功");
                } else {
                    listener.onResponseMessage(result.error);
                }
            }
        });
    }

    /**
     * 是否接受邀请
     * @param ui_id
     * @param mg_id
     * @param invite_ui_phone
     * @param mg_m_type
     * @param room_id
     * @param invite_ui_id
     * @param type
     * @param listener
     */
    public static void isAgreeInvite(String ui_id,String mg_id, String invite_ui_phone
            , String mg_m_type, String room_id,String invite_ui_id,String type,final ForResultListener listener){
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.agreeinvite;
        OkHttpUtils.post().url(url)
                .addParams("ui_id",ui_id)
                .addParams("mg_id",mg_id)
                .addParams("invite_ui_phone",invite_ui_phone)
                .addParams("mg_m_type",mg_m_type)
                .addParams("type",type)
                .addParams("room_id",room_id)
                .addParams("invite_ui_id",invite_ui_id)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToolUtils.logMes("isAgreeInvite  e="+e);
            }
            @Override
            public void onResponse(String response, int id) {
                ToolUtils.logMes("isAgreeInvite="+response);
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                if ("0".equals(result.errno)) {
                    listener.onResponseMessage("成功");
                } else {
                    listener.onResponseMessage(result.error);
                }
            }
        });
    }

    /**
     * 设为管理员和解除管理员
     * @param ui_id
     * @param mg_id
     * @param type 1解除 2新增管理员
     */
    public static void addManagerStatus(String ui_id,String mg_id,String remove_ui_id,String type,final ForResultListener listener){
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.addmanager;
        OkHttpUtils.post().url(url)
                .addParams("ui_id",ui_id)
                .addParams("mg_id",mg_id)
                .addParams("remove_ui_id",remove_ui_id)
                .addParams("type",type)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToolUtils.logMes("addManagerStatus  e="+e);
            }
            @Override
            public void onResponse(String response, int id) {
                ToolUtils.logMes("addManagerStatus="+response);
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                if ("0".equals(result.errno)) {
                    listener.onResponseMessage("成功");
                } else {
                    listener.onResponseMessage(result.error);
                }
            }
        });
    }

    /**
     * 删除家庭成员
     * @param ui_id
     * @param mg_id
     * @param remove_ui_id
     * @param role
     * @param listener
     */
    public static void deleteMember(String ui_id,String mg_id,String remove_ui_id,String role,final ForResultListener listener){
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.deleteManager;
        OkHttpUtils.post().url(url)
                .addParams("ui_id",ui_id)
                .addParams("mg_id",mg_id)
                .addParams("ui_phone",remove_ui_id)
                .addParams("type","1")
                .addParams("role",role)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToolUtils.logMes("deleteMember  e="+e);
            }
            @Override
            public void onResponse(String response, int id) {
                ToolUtils.logMes("deleteMember="+response);
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                if ("0".equals(result.errno)) {
                    listener.onResponseMessage("成功");
                } else {
                    listener.onResponseMessage(result.error);
                }
            }
        });
    }

    /**
     * 获取用户权限信息
     * @param ui_id 用户id
     * @param mg_id 用户组id
     * @param listener
     */
    public static void getRoleInfo(String ui_id,String mg_id,final ForResultListener listener){
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.userrole;
        OkHttpUtils.post().url(url)
                .addParams("ui_id",ui_id)
                .addParams("mg_id",mg_id)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToolUtils.logMes("getRoleInfo  e="+e);
            }
            @Override
            public void onResponse(String response, int id) {
                ToolUtils.logMes("getRoleInfo="+response);
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                if ("0".equals(result.errno)) {
                    listener.onResponseMessage(result.data.role);
                } else {
                    listener.onResponseMessage(result.error);
                }
            }
        });
    }

    /**
     * 根据手机号搜索用户组信息
     * @param id 用户id
     * @param phone 管理员手机号
     * @param listener
     */
    public static void getJoinFamliyInfo(Context context,String id, String phone, final ForResultJoinFamliyListener listener){
        if(!ToolUtils.isNetworkAvailable(context)){
            ToolUtils.toastMessage("您当前无网络，请联网再试",context);
            listener.onResponseMessage(null);
            return;
        }
        if(TextUtils.isEmpty(phone)){
            ToolUtils.toastMessage("您输入的手机号为空",context);
            listener.onResponseMessage(null);
            return ;
        }
        if(!TextUtils.isEmpty(phone)){
            if(!ToolUtils.isMobilePhone(phone)){
                ToolUtils.toastMessage("您输入的手机号有误",context);
                listener.onResponseMessage(null);
                return ;
            }
        }

        dialog = MyDialog.showDialog(context);
        dialog.show();
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.myidentity;
        OkHttpUtils.post().url(url)
                .addParams("ui_id",id)
                .addParams("ui_phone",phone)
                .addParams("type","4")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                dialog.dismiss();
                dialog = null;
                ToolUtils.logMes("getJoinFamliyInfo  e="+e );
                listener.onResponseMessage(null);
            }
            @Override
            public void onResponse(String response, int id) {
                dialog.dismiss();
                dialog = null;
                ToolUtils.logMes("getJoinFamliyInfo="+response );
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                if ("0".equals(result.errno)) {
                    listener.onResponseMessage(result.data.list);
                } else {
                    listener.onResponseMessage(null);
                }
            }
        });
    }

    /**
     * 获取用户的家庭组数量信息
     * @param listener
     */
    public static void getHomeNumberInfo(final String id, final ForResultListener listener){
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.numbergroup;
        OkHttpUtils.post().url(url)
                .addParams("ui_id",id)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToolUtils.logMes("getHomeNumberInfo  e="+e);
                listener.onResponseMessage("");
            }
            @Override
            public void onResponse(String response, int id) {
                ToolUtils.logMes("getHomeNumberInfo="+response);
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                if ("0".equals(result.errno)) {
                    listener.onResponseMessage(result.data.sum);
                } else {
                    listener.onResponseMessage("");
                }
            }
        });
    }

    /**
     * 申请加入家庭组
     * @param id
     * @param mg_id
     * @param phone
     */
    public static void applyToNewHomeInfo(Context context, String id, String mg_id, String phone, final ForResultListener listener){
        dialog = MyDialog.showDialog(context);
        dialog.show();
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.applyuser;
        OkHttpUtils.post().url(url)
                .addParams("ui_id",id)
                .addParams("mg_id",mg_id)
                .addParams("apply_ui_phone",phone)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToolUtils.logMes("applyToNewHomeInfo  e="+e);
                listener.onResponseMessage("");
                dialog.dismiss();
                dialog = null;
            }
            @Override
            public void onResponse(String response, int id) {
                ToolUtils.logMes("applyToNewHomeInfo="+response);
                dialog.dismiss();
                dialog = null;
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                if ("0".equals(result.errno)) {
                    listener.onResponseMessage(result.error);
                } else {
                    listener.onResponseMessage("");
                }
            }
        });
    }

    public static void isAgreeApply(String ui_id,String mg_id, String apply_ui_phone
            , String mg_m_type, String room_id,String apply_ui_id,String type,final ForResultListener listener){
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.agreeapply;
        Log.i("qaz", "ui_id-" +ui_id+"-mg_id-"+mg_id+"-apply_ui_phone-"+apply_ui_phone +"-mg_m_type-"+mg_m_type+"-type-"+type+"-room_id-"+room_id+"-apply_ui_id-"+apply_ui_id);
        OkHttpUtils.post().url(url)
                .addParams("ui_id",ui_id)
                .addParams("mg_id",mg_id)
                .addParams("apply_ui_phone",apply_ui_phone)
                .addParams("mg_m_type",mg_m_type)
                .addParams("type",type)
                .addParams("room_id",room_id)
                .addParams("apply_ui_id",apply_ui_id)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToolUtils.logMes("isAgreeInvite  e="+e);
            }
            @Override
            public void onResponse(String response, int id) {
                ToolUtils.logMes("isAgreeInvite="+response);
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                if ("0".equals(result.errno)) {
                    listener.onResponseMessage("成功");
                } else {
                    listener.onResponseMessage(result.error);
                }
            }
        });
    }
    public interface ForResultListener{
        void onResponseMessage(String code);
    }
    public interface ForResultDataListener{
        void onResponseMessage(GroupInfoBean info);
    }
    public interface ForResultInfoListener{
        void onResponseMessage(Map<String, String> map);
    }
    public interface ForUserPowerResultInfoListener{
        void onResponseMessage(List<UserPowerInfo> lists);
    }
    public interface ForResultJoinFamliyListener{
        void onResponseMessage(List<GroupInfoBean> lists);
    }
}
