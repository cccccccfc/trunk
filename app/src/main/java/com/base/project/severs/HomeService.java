package com.base.project.severs;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import com.google.gson.Gson;

/**
 * @author IMXU
 * @time 2017/9/20 10:47
 * @des ${TODO}
 * 邮箱：butterfly_xu@sina.com
 */

public class HomeService extends Service {


    private static Gson mGson;


    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                publishWhile();
            }
        }).start();
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public void onDestroy() {

        super.onDestroy();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    /**
     * 订阅主题
     */
    private void publishWhile() {
        String[] arg = {};
        try {
            Listener.main(arg);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
