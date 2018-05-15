package com.base.project.internet;

import android.content.Context;
import android.text.TextUtils;
import android.util.Log;

import com.base.project.R;

import com.base.project.base.BaseApplication;
import com.base.project.utils.CommonUtils;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import okhttp3.Call;
import okhttp3.MediaType;


/**
 * Created by Administrator on 2017/8/8.
 */

public class GetDataInterntet {
    private static int index = 0;
    private static Context context;
    private static Gson mGson;
    private static ForResultListener mListener;


    public interface ForResultListener {
        void onResponseMessage(String response);
    }

    static {
        mGson = new Gson();
        context = BaseApplication.getContext();

    }

    /**
     *
     */
        public static void pushCmd(String cmd ,ForResultListener listener) {
        mListener = listener;

        if(!CommonUtils.isNetworkAvailable(context)){
            mListener.onResponseMessage(null);
            return;
        }

        String url = BaseApplication.getContext().getResources().getString(R.string.pushs)
                .concat(BaseApplication.getContext().getResources().getString(R.string.push_cmds));
            Log.i("qaz", "pushCmd: " + url);
        OkHttpUtils.postString().url(url)
                .content(cmd)
                .mediaType(MediaType.parse("application/json; charset=utf-8"))
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                Log.i("qaz", "onResponse: 错误"+e);
                if (e .equals("UnknownHostException")) {
                  //  speekText("域名解析不成功");
                } else if (e.equals("SocketTimeoutException")){
                    //speekText("链接服务器超时");
                }
                mListener.onResponseMessage("");

              //  CommonUtils.toastMessage("您网络信号不稳定，请稍后再试");
            }

            @Override
            public void onResponse(String response, int id) {

                Log.i("qaz", "onResponse: "+response.toString());
                if (TextUtils.isEmpty(response)) {
                   // CommonUtils.toastMessage("上传数据失败");
                    mListener.onResponseMessage("");
                    return;
                }

                if (!TextUtils.isEmpty(response)) {
                    mListener.onResponseMessage(response);

                }

            }
        });

    }


}
