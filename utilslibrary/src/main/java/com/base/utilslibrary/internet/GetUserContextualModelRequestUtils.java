package com.base.utilslibrary.internet;

import android.content.Context;
import android.util.Log;

import com.base.utilslibrary.ToolUtils;
import com.base.utilslibrary.bean.CtrlListBean;
import com.base.utilslibrary.bean.HomeManagerResult;
import com.base.utilslibrary.bean.ProjectUrlInfo;
import com.base.utilslibrary.bean.RecordListBean;
import com.base.utilslibrary.bean.SceneListBean;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;

/**
 * Created by Administrator on 2017/11/20.
 */

public class GetUserContextualModelRequestUtils {

    private static Gson mGson;
    private static String message;
    public static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");

    static {  //静态方法 初始化之前调用
        mGson = new Gson();
    }

    /**
     * 1.1	创建情景模式（包含情景删除功能）
     *
     * @param ui_id
     * @param mg_id
     * @param ms_name
     * @param date
     * @param type
     * @param listener
     */
    public static void setaddscene(String ui_id, String mg_id, String ms_name, String date, String type, String ms_type, String ms_id, final AddSceneListener listener) {
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.addscene;
        if ("2".equals(type) ) {
            ms_name = "";
            ms_type = "";
            date = "";
            mg_id = "";
        }
        if ("1".equals(type)) {
            ms_id = "";
        }
        Log.i("qaz", "setaddscene: "  +"ms_name---"+ms_name+"ms_type---"+ms_type+"date--"
                +date+"mg_id--"+mg_id+"ms_id----"+ms_id + "type" +type);
        OkHttpUtils.post().url(url)
                .addParams("ui_id", ui_id)
                .addParams("ms_name", ms_name)
                .addParams("type", type)
                .addParams("ms_type", ms_type)
                .addParams("data", date)
                .addParams("mg_id", mg_id)
                .addParams("ms_id", ms_id)

               /* .content(date)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))*/
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToolUtils.logMes("gethandlehistory  e=" + e);
                message = "0";
                listener.onResponseMessage("1");
            }

            @Override
            public void onResponse(String response, int id) {

                ToolUtils.logMes("gethandlehistory=" + response);
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                if ("0".equals(result.errno)) {

                    listener.onResponseMessage(result.errno);

                } else if ("2".equals(result.errno)) {

                    listener.onResponseMessage(result.errno);

                } else {

                    listener.onResponseMessage(result.errno);
                }
            }
        });

    }

    public interface AddSceneListener {
        void onResponseMessage(String message);
    }

    /**
     * 1.2	查询房间拥有的所有情景（情景模式已添加）
     *
     * @param ui_id
     * @param mg_id
     * @param listener
     */
    public static void getlisttoscene(String ui_id, String mg_id, final ListToSceneListener listener) {
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.listtoscene;
        OkHttpUtils.post().url(url)
                .addParams("ui_id", ui_id)
                .addParams("mg_id", mg_id)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToolUtils.logMes("gethandlehistory  e=" + e);
                message = "0";
                listener.onResponseMessage(null, "1");
            }

            @Override
            public void onResponse(String response, int id) {

                ToolUtils.logMes("gethandlehistory=" + response);
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                if ("0".equals(result.errno)) {

                    listener.onResponseMessage(result.data.scene_list, result.errno);

                } else if ("2".equals(result.errno)) {

                    listener.onResponseMessage(result.data.scene_list, result.errno);

                } else {

                    listener.onResponseMessage(null, result.errno);
                }
            }
        });


    }

    public interface ListToSceneListener {
        void onResponseMessage(List<SceneListBean> info, String message);
    }

    /**
     * 1.3	查询出某个情景内可使用的操作
     *
     * @param ui_id
     * @param mg_id
     * @param listener
     */
    public static void getlisttoexecute(String ui_id, String mg_id, final ListToExecuteListener listener) {
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.listtoexecute;
        OkHttpUtils.post().url(url)
                .addParams("ui_id", ui_id)
                .addParams("ms_id", mg_id)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToolUtils.logMes("gethandlehistory  e=" + e);
                message = "0";
                listener.onResponseMessage(null, "1");
            }

            @Override
            public void onResponse(String response, int id) {

                ToolUtils.logMes("gethandlehistory=" + response);
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                if ("0".equals(result.errno)) {

                    listener.onResponseMessage(result.data.execute_list, result.errno);

                } else if ("2".equals(result.errno)) {

                    listener.onResponseMessage(result.data.execute_list, result.errno);

                } else {

                    listener.onResponseMessage(null, result.errno);
                }
            }
        });


    }

    public interface ListToExecuteListener {
        void onResponseMessage(List<List<CtrlListBean>> info, String message);
    }

    /**
     * 1.4	获取摄像头参数
     *
     * @param listener
     */
    public static void getaccesstoken( final ModifySceneListener listener) {
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.getaccesstoken;
        OkHttpUtils.post().url(url)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToolUtils.logMes("gethandlehistory  e=" + e);
                message = "0";
                listener.onResponseMessage(null, null,null);
            }

            @Override
            public void onResponse(String response, int id) {

                ToolUtils.logMes("gethandlehistory=" + response);
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                if ("0".equals(result.errno)) {
                    listener.onResponseMessage(result.data.token ,result.data.url ,result.data.appkey);
                   // Log.i("qaz", "onResponseMessage:3 " +"token--"+result.data.token+"--url--"+result.data.url+"--key---"+result.data.appkey);
                } else {
                    listener.onResponseMessage(null, null, null);
                }
            }
        });


    }

    public interface ModifySceneListener {
        void onResponseMessage(String message, String url, String appkey);
    }

    /**
     * 1.5	删除情景内某个操作
     *
     * @param db_sbID
     * @param action
     * @param listener
     */
    public static void getcamerarecord(String db_sbID, String action, final DelexCuteListener listener) {
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.getcamerarecord;
        OkHttpUtils.post().url(url)
                .addParams("db_sbID", db_sbID)
                .addParams("action", action)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToolUtils.logMes("gethandlehistory  e=" + e);
                message = "0";
                listener.onResponseMessage(null);
            }

            @Override
            public void onResponse(String response, int id) {

                ToolUtils.logMes("gethandlehistory=" + response);
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                if ("0".equals(result.errno)) {
                    listener.onResponseMessage(result.data.record_list);
                } else {
                    listener.onResponseMessage(null);
                }
            }
        });


    }

    public interface DelexCuteListener {
        void onResponseMessage(List<RecordListBean> message);
    }

    /**
     * 获取设备列表
     */
    public static void getctrllists(final Context context, String ui_id, String mg_id, String type, final CtrllistListener listener) {

        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.listtotopgroup;
        OkHttpUtils.post().url(url)
                .addParams("ui_id", ui_id)
                .addParams("mg_id", mg_id)
                .addParams("type", type)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {


                ToolUtils.toastMessage("您的网络不稳定，请刷新", context);
            }

            @Override
            public void onResponse(String response, int id) {

                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                Log.i("wsx", "onResponse: " + response);
                if ("0".equals(result.errno)) {
                    listener.onResponseMessage(result.data.device_lists);
                } else {
                    listener.onResponseMessage(null);
                }
            }
        });
    }

    public interface CtrllistListener {
        void onResponseMessage(List<List<CtrlListBean>> info);
    }
}
