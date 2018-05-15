package com.base.utilslibrary.internet;

import android.content.Context;
import android.util.Log;

import com.base.utilslibrary.MyDialog;
import com.base.utilslibrary.ToolUtils;
import com.base.utilslibrary.bean.CtrlListIndexBean;
import com.base.utilslibrary.bean.DataInfo;
import com.base.utilslibrary.bean.HomeManagerResult;
import com.base.utilslibrary.bean.ProjectUrlInfo;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/11/3.
 */

public class HomeListtoGroupRequestUtils {

    private static Gson mGson;
    private static MyDialog dialog;
    static {  //静态方法 初始化之前调用
        mGson = new Gson();
    }

    public static void getctrllist(final Context context, String ui_id, String mg_id, String type, String mobile, final CtrllistListener listener){
        final MyDialog[] dialog = {MyDialog.showDialog(context)};
        dialog[0].show();
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.ctrllist;
        OkHttpUtils.post().url(url)  // .addParams("mobile","")
                .addParams("ui_id",ui_id)
                .addParams("mg_id",mg_id)
                .addParams("type",type)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if(dialog[0] !=null){
                    dialog[0].dismiss();
                    dialog[0] = null;
                }
                ToolUtils.logMes("getctrllist  e="+e);
                ToolUtils.toastMessage("您的网络不稳定，请刷新",context);
            }
            @Override
            public void onResponse(String response, int id) {
                if(dialog[0] !=null){
                    dialog[0].dismiss();
                    dialog[0] = null;
                }
                ToolUtils.logMes("getctrllist="+response);
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                if ("0".equals(result.errno)) {
                    listener.onResponseMessage(result.data.ctrl_list);
                } else {
                    listener.onResponseMessage(null);
                }
            }
        });
    }

    /**
     * 获取主页信息
     * @param ui_id
     * @param listener
     */
    public static void getHomePageInfo(final Context context, String ui_id, final HomeInfolistListener listener){
        final MyDialog[] dialog = {MyDialog.showDialog(context)};
        dialog[0].show();

        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.indexIdentity;

        OkHttpUtils.post().url(url)
                .addParams("ui_id",ui_id)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if(dialog[0] !=null){
                    dialog[0].dismiss();
                    dialog[0] = null;
                }
                ToolUtils.logMes("getHomePageInfo  e="+e);

                ToolUtils.toastMessage("您的网络不稳定，请刷新",context);
            }
            @Override
            public void onResponse(String response, int id) {
                if(dialog[0] !=null){
                    dialog[0].dismiss();
                    dialog[0] = null;
                }
                ToolUtils.logMes("getHomePageInfo="+response);
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                if ("0".equals(result.errno)) {
                    listener.onResponseMessage(result.data);
                } else {
                    listener.onResponseMessage(null);
                }
            }
        });

    }
    /**
     * 获取更换家庭后的设备信息
     * @param ui_id
     * @param listener
     */
    public static void getHomeChangeInfo(final Context context, String ui_id, String mg_id, final HomeInfolistListener listener){
        final MyDialog[] dialog = {MyDialog.showDialog(context)};
        dialog[0].show();
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.indexIdentity;
        Log.i("qaz", "getHomePageInfo: " + url+ "ui_id=" +ui_id + "mg_id="+ mg_id);
        OkHttpUtils.post().url(url)
                .addParams("ui_id",ui_id)
                .addParams("mg_id",mg_id)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if(dialog[0] !=null){
                    dialog[0].dismiss();
                    dialog[0] = null;
                }
                ToolUtils.logMes("getHomeChangeInfo  e="+e);
                ToolUtils.toastMessage("您的网络不稳定，请刷新",context);
            }
            @Override
            public void onResponse(String response, int id) {
                if(dialog[0] !=null){
                    dialog[0].dismiss();
                    dialog[0] = null;
                }
                ToolUtils.logMes("getHomeChangeInfo="+response);
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                if ("0".equals(result.errno)) {
                    listener.onResponseMessage(result.data);
                } else {
                    listener.onResponseMessage(null);
                }
            }
        });

    }

    public interface CtrllistListener{
        void onResponseMessage(List<CtrlListIndexBean> info);
    }
    public interface HomeInfolistListener{
        void onResponseMessage(DataInfo info);
    }



}
