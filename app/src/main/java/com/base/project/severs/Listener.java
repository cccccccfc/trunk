/**
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.base.project.severs;

import android.text.TextUtils;
import android.util.Log;

import com.base.project.bean.YiYuanBean;
import com.base.project.bean.YiYuanDeviceBean;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.Constants;
import com.base.project.utils.SpTools;
import com.google.gson.Gson;

import org.fusesource.hawtbuf.AsciiBuffer;
import org.fusesource.hawtbuf.Buffer;
import org.fusesource.hawtbuf.UTF8Buffer;
import org.fusesource.mqtt.client.Callback;
import org.fusesource.mqtt.client.CallbackConnection;
import org.fusesource.mqtt.client.MQTT;
import org.fusesource.mqtt.client.QoS;
import org.fusesource.mqtt.client.Topic;

import java.util.Random;

/**
 * Uses an callback based interface to MQTT.  Callback based interfaces
 * are harder to use but are slightly more efficient.
 */
public class Listener {

    private static MQTT mqtt;
    static CallbackConnection connection = null;
    private static YiYuanBean yiYuanBean;
    private static YiYuanDeviceBean deviceBeanAirCleaner;
    private static YiYuanDeviceBean deviceBeanLight;
    private static YiYuanDeviceBean deviceBeanCurtain;
    private static ForResultListener mListener;
    private static String destination0;

    public static void main(String[] args) throws Exception {


        Long startDate = System.currentTimeMillis();
        String user = env("APOLLO_USER", "root");
        String password = env("APOLLO_PASSWORD", "52399399");
        String host = env("APOLLO_HOST", "mqtt.lchtime.cn");
        int port = Integer.parseInt(env("APOLLO_PORT", "1883"));


        mqtt = new MQTT();
        mqtt.setHost(host, port);
        mqtt.setUserName(user);
        mqtt.setPassword(password);
        mqtt.setClientId("ID" + new Random().nextInt(5000));//加上ub_id
        mqtt.setCleanSession(true);
        mqtt.setKeepAlive((short) 10);
        mqtt.setConnectAttemptsMax(20);
//       String mac = CommonUtils.getMaxString().toLowerCase();
        String ubid = SpTools.getString(CommonUtils.getContext(), Constants.userId, "0");
        final String destination0 = arg(args, 0, "data/" + ubid + "/#");

        mqtt.setWillMessage(new Gson().toJson(yiYuanBean));
        Log.i("--------", "---json=" + new Gson().toJson(yiYuanBean));

        connection = mqtt.callbackConnection();
        //MQTT网络连接
        connection.listener(new org.fusesource.mqtt.client.Listener() {
            long count = 0;
            long start = System.currentTimeMillis();

            public void onConnected() {
                Log.i("-----MQTTListener", "onConnected");
                if(informationListener !=null){
                    informationListener.onResult("onConnected");
                }
            }

            public void onDisconnected() {
                if(informationListener !=null){

                }
            //    informationListener.onResult("onFailure");
                Log.i("-----MQTTListener", "onDisconnected");
            }

            public void onFailure(Throwable value) {
                Log.i("-----MQTTListener", "onFailure");
                value.printStackTrace();
                if(informationListener !=null){
                    informationListener.onResult("onFailure");
                }
//                System.exit(-2);
            }

            //监听收到的主题
            public void onPublish(UTF8Buffer topic, Buffer msg, Runnable ack) {
                String body = msg.utf8().toString();
                if ("SHUTDOWN".equals(body)) {
                    long diff = System.currentTimeMillis() - start;
                    Log.i("-----MQTTListener", String.format("Received %d in %.2f seconds", count, (1.0 * diff / 1000.0)));
                    //断开连接
                    connection.disconnect(new Callback<Void>() {
                        @Override
                        public void onSuccess(Void value) {
                            Log.i("MQTTListener", "disconnect");
                            System.exit(0);
                        }

                        @Override
                        public void onFailure(Throwable value) {
                            value.printStackTrace();
//                            System.exit(-2);
                        }
                    });
                } else {
                    Log.i("--------MQTTListener", String.format("'%s' Received: %s.", topic, body));
                    if (!TextUtils.isEmpty(body)) {
                        informationListener.onResult(body);
                        Log.i("--------MQTTListener", body);
                    }
                }
            }
        });
        //连接成功后 执行相关的订阅内容
        connection.connect(new Callback<Void>() {
            @Override
            public void onSuccess(Void value) {

            }

            @Override
            public void onFailure(Throwable value) {
                value.printStackTrace();
//                System.exit(-2);
            }
        });


        // Wait forever..
        synchronized (Listener.class) {
            while (true)
                Listener.class.wait();
        }

    }

    public static void mqttSubscribe(String destination0) {
        final Topic[] topics = {
                new Topic(destination0, QoS.EXACTLY_ONCE),
//                        new Topic(destination1, QoS.EXACTLY_ONCE),
//                        new Topic(destination2, QoS.EXACTLY_ONCE),
        };
        connection.subscribe(topics, new Callback<byte[]>() {
            public void onSuccess(byte[] qoses) {
                for (int i = 0; i < topics.length; i++) {
                    Log.i("---------MQTTListener", "subscribe:" + topics[i] + " onSuccess " + qoses[i]);
                    //do it
                }
            }

            public void onFailure(Throwable value) {
                value.printStackTrace();
//                System.exit(-2);
            }
        });
    }
    public static void mqttUnSubscribe(String destination0){
        final UTF8Buffer[] topics = {new UTF8Buffer(destination0)};
        connection.unsubscribe(topics, new Callback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("---------MQTTListener","unsubscribe:"+topics[0]);
            }

            @Override
            public void onFailure(Throwable throwable) {

            }
        });
    }

    /**
     * @param
     * @param data json数据
     */
    //传递数据
    public static void mqttPublisher( String data ,String mac ,ForResultListener listener) {
        Log.i("qaz", "接收到需要publisher的数据: " + data);
        mListener = listener;
        destination0 = "common/"+ mac+"/get";
        Log.i("qaz", "接收到需要publisher的地址: " + destination0);
        Buffer msg = new AsciiBuffer(data);
        //发布主题内容
        UTF8Buffer topics = new UTF8Buffer(destination0);

        connection.publish(topics, msg, QoS.EXACTLY_ONCE, false, new Callback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                Log.i("qaz", "subscribe:" + " onSuccess发布成功 " + "-----");
                if (mListener != null) {
                    mListener.onResponseMessage("OK");
                } else {
                    Log.i("qaz", "判断mListener 是不是空1");
                }
            }
            @Override
            public void onFailure(Throwable throwable) {
                Log.i("qaz", "subscribe:" + " onSuccess发布失败" + "-----");

                if (mListener != null) {
                    mListener.onResponseMessage("");
                } else {
                    Log.i("qaz", "判断mListener 是不是空3");
                }
            }
        });

        // mqtt.callbackConnection().publish(topics, msg, QoS.AT_MOST_ONCE, false);

    }

    private static String env(String key, String defaultValue) {
        String rc = System.getenv(key);
        if (rc == null)
            return defaultValue;
        return rc;
    }

    private static String arg(String[] args, int index, String defaultValue) {
        if (index < args.length)
            return args[index];
        else
            return defaultValue;
    }


    //传递数据
    public static void mqttPublishers(int id, String data, String mac, int dd_ui_id ,QoS i, boolean b, ForResultListener listener) {
        mListener = listener;

        //  SpTools.getString(CommonUtils.getContext(), Constants.userId, "0")

        destination0 = "ctrl/"+ dd_ui_id+"/" + mac + "/" + id;

         Log.i("qaz", "mac地址: " + destination0);
        Buffer msg = new AsciiBuffer(data);
        //发布主题内容
        UTF8Buffer topics = new UTF8Buffer(destination0);

        connection.publish(topics, msg, i, b, new Callback<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                  Log.i("qaz", "subscribe:" + " onSuccess发布成功 " + "-----");
                if (mListener != null) {
                    mListener.onResponseMessage("OK");
                } else {
                    Log.i("qaz", "判断mListener 是不是空1");
                }
            }
            @Override
            public void onFailure(Throwable throwable) {
                 Log.i("qaz", "subscribe:" + " onSuccess发布失败" + "-----");

                if (mListener != null) {
                    mListener.onResponseMessage("");
                } else {
                    Log.i("qaz", "判断mListener 是不是空3");
                }
            }
        });
    }

    public interface ForResultListener {
        void onResponseMessage(String string);
    }

    public interface JsonDataInformationListener {
        void onResult(String json);
    }

    private static JsonDataInformationListener informationListener;

    public static void setJsonDataInfoListener(JsonDataInformationListener listener) {
        informationListener = listener;
    }

}
