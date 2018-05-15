package com.base.utilslibrary.internet;

import android.text.TextUtils;
import android.util.Log;

import com.base.utilslibrary.ToolUtils;
import com.base.utilslibrary.bean.BluerecordForuserBean;
import com.base.utilslibrary.bean.HistoryListBean;
import com.base.utilslibrary.bean.HomeManagerResult;
import com.base.utilslibrary.bean.ProjectUrlInfo;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.util.List;

import okhttp3.Call;

/**
 * Created by Administrator on 2017/11/2.
 */

public class GetControlHistoryRequestUtils {


    private static Gson mGson;
    private static String message;

    static {  //静态方法 初始化之前调用
        mGson = new Gson();
    }

    public static void gethandlehistory(String ui_id, int db_sbID, String type,String date , final HandleHistoryListener listener){
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.handlehistory;
        OkHttpUtils.post().url(url)
                .addParams("ui_id",ui_id)
                .addParams("db_sbID", String.valueOf(db_sbID))
                .addParams("type",type)
                .addParams("date",date)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToolUtils.logMes("gethandlehistory  e="+e);
                message = "0";
                listener.onResponseMessage(null, "1");
            }
            @Override
            public void onResponse(String response, int id) {

                ToolUtils.logMes("gethandlehistory="+response);
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                if ("0".equals(result.errno)) {

                    listener.onResponseMessage(result.data.history_list , result.errno);

                }else if("2".equals(result.errno)){

                    listener.onResponseMessage(result.data.history_list , result.errno);

                } else {

                    listener.onResponseMessage(null ,result.errno);
                }
            }
        });


    }

    public interface HandleHistoryListener{

        void onResponseMessage(List<HistoryListBean> info ,String message);
    }

    public static void getbindbluecard(String ui_id, String cardID, String type, final BindbluecardListener listener){
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.bindbluecard;
        Log.i("qaz", "getbindbluecard: " +ui_id+"----"+cardID+"----"+type);
        OkHttpUtils.post().url(url)
                .addParams("ui_id",ui_id)
                .addParams("cardID", cardID)
                .addParams("type",type)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToolUtils.logMes("gethandlehistory  e="+e);
                message = "0";
                listener.onResponseMessage(null ,null ,null);
            }
            @Override
            public void onResponse(String response, int id) {
                ToolUtils.logMes("gethandlehistory="+response);
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);
                Log.i("qaz", "=返回的数据:= "+result.error +"====="+result.errno+"=====");
                if (!TextUtils.isEmpty(result.errno)) {
                    listener.onResponseMessage(result.error ,result.errno ,result.data.cardid);
                } else {
                    listener.onResponseMessage(null ,null ,null);
                }
            }
        });
    }
    public interface BindbluecardListener{
        void onResponseMessage(String error, String errno, String data);
    }
    public static void getbluerecordforuser(String ui_id, String date ,final BluerecordforuserListener listener){
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.bluerecordforuser;

        OkHttpUtils.post().url(url)
                .addParams("ui_id",ui_id)
                .addParams("date",date)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {

                ToolUtils.logMes("gethandlehistory1111="+e);
                listener.onResponseMessage(null);
            }

            @Override
            public void onResponse(String response, int id) {
                ToolUtils.logMes("gethandlehistory1111="+response);
                HomeManagerResult result = mGson.fromJson(response, HomeManagerResult.class);

                if ("0".equals(result.errno)) {
                    Log.i("qaz", "onResponse: 1");
                    listener.onResponseMessage(result.data.bluerecord_list );
                }else if("2".equals(result.errno)){
                    listener.onResponseMessage(result.data.bluerecord_list  );
                } else {
                    listener.onResponseMessage(null );
                }
            }
        });


    }

    public interface BluerecordforuserListener{

        void onResponseMessage(List<BluerecordForuserBean> message);
    }
}
