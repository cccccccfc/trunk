package com.base.project.activity.voice;

import android.text.TextUtils;
import android.util.Log;

import com.base.project.base.BaseApplication;
import com.base.project.bean.PushcmdBean;
import com.base.project.fragment.FragmentHome;
import com.base.project.severs.Listener;
import com.base.project.utils.Constants;
import com.base.project.utils.SpTools;
import com.base.utilslibrary.bean.FragmentHomeShowBean;
import com.google.gson.Gson;

import org.fusesource.mqtt.client.QoS;

import java.util.ArrayList;
import java.util.List;

import static com.base.project.activity.voice.VoicePlaying.startSpeaking;
import static com.base.project.utils.KeywordMatching.isHave;

/**
 * Created by Administrator on 2017/11/2.
 * 控制设备
 */

public class OperatingDevice {
    private static String TAG = "qaz";
    private static String id;  //设备id
    private static PushcmdBean pushcmdBean;
    private static List<PushcmdBean.PayloadBean> plist;
    private static PushcmdBean.PayloadBean payloadBean;
    private static PushcmdBean.PayloadBean.CtrlBean ctrlBean;
    private static List<FragmentHomeShowBean.ListBean> list;
    /**
     * 根据传入的字符串判断是哪种设备
     *
     * @param Command
     */
    static String[] strOne = new String[]{};//定义字符串数组
    static String[] strTwo = {"开", "关"};
    static String[] number = {"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13"};
    static String[] chinese = {"一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二", "十三"};
    private static int clusterID;
    private static String macaddr;
    private static int db_sbID;
    private static int dd_ui_id;
    private static int db_dt_id;
//    private static ActivityPattern.MyQueue mMyQueue;

    public static void controlCommand(String Command) {

        list = FragmentHome.getChangeHome();

        strOne = FragmentHome.getChangeName();
        //Log.i(TAG, "传过来的数据: " + strOne.length);

        for (int j = 0; j < number.length; j++) {
            Command = Command.replace(chinese[j], number[j]);
        }


        String order = isHave(strOne, strTwo, Command);
        if (TextUtils.isEmpty(order)) {
          //  Log.i("qaz", "不包含控制命令 ");
            startSpeaking("不包含控制命令");
            return;
        }
        //Log.i("ddddd", "转换"+ Command +"---"+strOne);
       // Log.i(TAG, "111生成控制命令" +order.toString() );
        for (int i = 0; i < list.size(); i++) {
            if (order.contains(list.get(i).di_clustername)) {
                for(int j = 0; j < list.size(); j++){
                    clusterID = list.get(i).di_clusterID;
                    macaddr = list.get(i).dd_macaddr;
                    db_sbID = list.get(i).dd_db_sbID;
                    dd_ui_id =list.get(i).dd_ui_id;
                    db_dt_id = list.get(i).db_dt_id;
                    if (61 == db_dt_id) {
                        SpTools.setString(BaseApplication.getContext(), Constants.sbid, String.valueOf(list.get(i).dd_db_sbID));
                    }
                }
            }
            // Log.i(TAG, "打印设备名称: " + list.get(i).di_clustername);
        }
        //Log.i(TAG, "获取到的一些参数: " + clusterID + " ---------" + macaddr + "---------" + db_sbID);
        if (clusterID == 0 &&
                TextUtils.isEmpty(macaddr) &&
                db_sbID == 0) {
            startSpeaking("控制命令错误");
            return;
        }
        // Log.i(TAG, "包含控制命令 " + order);
        if (order.toString().contains("电视")) {
            if (order.toString().contains("开")) {
                operatingDeviceVoice(0, 0, Integer.parseInt(SpTools.getString(BaseApplication.getContext(), Constants.sbid, "")), macaddr, clusterID ,dd_ui_id);
            } else if (order.toString().contains("关")) {
                operatingDeviceVoice(0, 1, Integer.parseInt(SpTools.getString(BaseApplication.getContext(), Constants.sbid, "")), macaddr, clusterID,dd_ui_id);
            }

        } else if (order.toString().contains("空调")) {

            if (order.toString().contains("开")) {
                operatingDeviceVoice(1, 0, Integer.parseInt(SpTools.getString(BaseApplication.getContext(), Constants.sbid, "")), macaddr, clusterID,dd_ui_id);
            } else if (order.toString().contains("关")) {
                operatingDeviceVoice(1, 1, Integer.parseInt(SpTools.getString(BaseApplication.getContext(), Constants.sbid, "")), macaddr, clusterID,dd_ui_id);
            }
        } else if (order.toString().contains("窗帘")) {

            if (order.toString().contains("开")) {
                operatingDeviceVoice(2, 0, db_sbID, macaddr, clusterID,dd_ui_id);
            } else if (order.toString().contains("关")) {
                operatingDeviceVoice(2, 1, db_sbID, macaddr, clusterID,dd_ui_id);
            }

        } else if (order.toString().contains("门锁1")) {

            if (order.toString().contains("开")) {
                operatingDeviceVoice(3, 0, db_sbID, macaddr, clusterID,dd_ui_id);

            }
        } else if (order.toString().contains("客厅灯") || order.toString().contains("灯1")) {

            if (order.toString().contains("开")) {
                operatingDeviceVoice(4, 0, db_sbID, macaddr, clusterID,dd_ui_id);
            } else if (order.toString().contains("关")) {
                operatingDeviceVoice(4, 1, db_sbID, macaddr, clusterID,dd_ui_id);

            }

        } else if (order.toString().contains("卧室灯") || order.toString().contains("灯2")) {

            if (order.toString().contains("开")) {
                operatingDeviceVoice(4, 0, db_sbID, macaddr, clusterID,dd_ui_id);
            } else if (order.toString().contains("关")) {
                operatingDeviceVoice(4, 1, db_sbID, macaddr, clusterID,dd_ui_id);
            }

        } else if (order.toString().contains("厨房灯") || order.toString().contains("灯3")) {

            if (order.toString().contains("开")) {
                operatingDeviceVoice(4, 0, db_sbID, macaddr, clusterID,dd_ui_id);

            } else if (order.toString().contains("关")) {
                operatingDeviceVoice(4, 1, db_sbID, macaddr, clusterID,dd_ui_id);

            }

        } else if (order.toString().contains("灯4") || order.toString().contains("浴室灯")) {

            if (order.toString().contains("开")) {
                operatingDeviceVoice(4, 0, db_sbID, macaddr, clusterID,dd_ui_id);
            } else if (order.toString().contains("关")) {
                operatingDeviceVoice(4, 1, db_sbID, macaddr, clusterID,dd_ui_id);
            }

        } else if (order.toString().contains("灯5") || order.toString().contains("阳台灯")) {

            if (order.toString().contains("开")) {
                operatingDeviceVoice(4, 0, db_sbID, macaddr, clusterID,dd_ui_id);
            } else if (order.toString().contains("关")) {
                operatingDeviceVoice(4, 1, db_sbID, macaddr, clusterID,dd_ui_id);
            }

        } else if (order.toString().contains("灯6") || order.toString().contains("走廊灯")) {

            if (order.toString().contains("开")) {
                operatingDeviceVoice(4, 0, db_sbID, macaddr, clusterID,dd_ui_id);
            } else if (order.toString().contains("关")) {
                operatingDeviceVoice(4, 1, db_sbID, macaddr, clusterID,dd_ui_id);
            }

        } else if (order.toString().contains("空气净化器")) {

            if (order.toString().contains("开")) {
                operatingDeviceVoice(10, 0, db_sbID, macaddr, clusterID,dd_ui_id);
            } else if (order.toString().contains("关")) {
                operatingDeviceVoice(10, 1, db_sbID, macaddr, clusterID,dd_ui_id);
            }

        } else if (order.toString().contains("煤气阀门")) {

            if (order.toString().contains("开")) {
                operatingDeviceVoice(11, 0, db_sbID, macaddr, clusterID,dd_ui_id);
            } else if (order.toString().contains("关")) {
                operatingDeviceVoice(11, 1, db_sbID, macaddr, clusterID,dd_ui_id);
            }

        }
    }

    /**
     * 根据传入的变量生成对应的json  onoff  0开 1关
     *
     * @param device
     */
    public static void operatingDeviceVoice(int device, int onoff, int db_sbID, String mac, int clusterID, int dd_ui_id ) {
        if (pushcmdBean == null) {
            pushcmdBean = new PushcmdBean();

        }
        if (payloadBean == null) {
            payloadBean = new PushcmdBean.PayloadBean();
        }

        if (ctrlBean == null) {
            ctrlBean = new PushcmdBean.PayloadBean.CtrlBean();
        }

        if (device == 0) { //0电视 1空调 2窗帘 3锁 4卧室灯 5客厅灯 6台灯 7灯四 8灯五 9灯六 10空气净化器 11煤气阀门
            pushcmdBean.setDb_sbID(db_sbID);
            if (onoff == 0) {
                ctrlBean.setHandle(5);
                ctrlBean.setSw(255);
            } else if (onoff == 1) {
                ctrlBean.setHandle(6);
                ctrlBean.setSw(0);

            }
            ctrlBean.setFunctions(0);
            ctrlBean.setTimes(5);

            ctrlBean.setLearnmode(1);
            ctrlBean.setPosition(0);
            ctrlBean.setCmd(10);
            payloadBean.setClusterID(clusterID);
        } else if (device == 1) {
            pushcmdBean.setDb_sbID(db_sbID);

            if (onoff == 0) {
                ctrlBean.setHandle(5);
                ctrlBean.setSw(255);

            } else if (onoff == 1) {
                ctrlBean.setHandle(6);
                ctrlBean.setSw(0);

            }
            ctrlBean.setLearnmode(0);
            ctrlBean.setCmd(10);
            payloadBean.setClusterID(clusterID);
        } else if (device == 2) {
            pushcmdBean.setDb_sbID(db_sbID);

            if (onoff == 0) {
                ctrlBean.setHandle(5);
                ctrlBean.setSw(255);
                ctrlBean.setLocation(255);

            } else if (onoff == 1) {
                ctrlBean.setHandle(6);
                ctrlBean.setSw(0);
                ctrlBean.setLocation(0);

            }
            ctrlBean.setCmd(10);
            payloadBean.setClusterID(clusterID);
        } else if (device == 3) {
            pushcmdBean.setDb_sbID(db_sbID);

            if (onoff == 0) {
                ctrlBean.setHandle(5);
                ctrlBean.setSw(255);
                ctrlBean.setCmd(10);

            }
            payloadBean.setClusterID(clusterID);
        } else if (device == 4) {
            pushcmdBean.setDb_sbID(db_sbID);

            if (onoff == 0) {
                ctrlBean.setHandle(5);
                ctrlBean.setSw(255);

            } else if (onoff == 1) {
                ctrlBean.setHandle(6);
                ctrlBean.setSw(0);

            }
            ctrlBean.setCmd(10);
            payloadBean.setClusterID(clusterID);
        } else if (device == 10) {

            pushcmdBean.setDb_sbID(db_sbID);
            if (onoff == 0) {
                ctrlBean.setHandle(5);
                ctrlBean.setSw(255);

            } else if (onoff == 1) {
                ctrlBean.setHandle(6);
                ctrlBean.setSw(0);

            }
            ctrlBean.setCmd(10);
            payloadBean.setClusterID(clusterID);
        } else if (device == 11) {

            pushcmdBean.setDb_sbID(db_sbID);
            if (onoff == 0) {
                ctrlBean.setHandle(5);
                ctrlBean.setSw(255);
            } else if (onoff == 1) {
                ctrlBean.setHandle(6);
                ctrlBean.setSw(0);
            }
            ctrlBean.setCmd(10);
            payloadBean.setClusterID(clusterID);
        }
        payloadBean.setCtrl(ctrlBean);
        if (plist == null) {
            plist = new ArrayList<PushcmdBean.PayloadBean>();
        }
        plist.clear();
        plist.add(payloadBean);
        pushcmdBean.setPayload(plist);

        final Gson gson = new Gson();
        String jsonString = gson.toJson(pushcmdBean);
        Pushcmd(jsonString, db_sbID, mac,dd_ui_id);
        plist.clear();
    }


    public static void operatingDeviceHand(int device, int onoff, int db_sbID, String mac, int clusterID,int time ,int dd_ui_id) {
        // Log.i(TAG, "控制的参数 " +"device"+device + "onoff"+onoff+"db_sbID"+db_sbID+"mac"+mac+"id"+id+"clusterID"+clusterID);

        if (pushcmdBean == null) {
            pushcmdBean = new PushcmdBean();

        }
        if (payloadBean == null) {
            payloadBean = new PushcmdBean.PayloadBean();
        }

        if (ctrlBean == null) {
            ctrlBean = new PushcmdBean.PayloadBean.CtrlBean();
        }

        if (device == 0) { //0电视 1空调 2窗帘 3锁 4灯 10空气净化器 11煤气阀门
            pushcmdBean.setDb_sbID(db_sbID);
            if (onoff == 0) {
                ctrlBean.setHandle(5);
                ctrlBean.setSw(255);
            } else if (onoff == 1) {
                ctrlBean.setHandle(6);
                ctrlBean.setSw(0);

            }
            ctrlBean.setFunctions(0);
            ctrlBean.setTimes(5);
            ctrlBean.setDelayS(time);
            ctrlBean.setLearnmode(1);
            ctrlBean.setPosition(0);
            ctrlBean.setCmd(10);
            payloadBean.setClusterID(clusterID);
        } else if (device == 1) {
            pushcmdBean.setDb_sbID(db_sbID);

            if (onoff == 0) {
                ctrlBean.setHandle(5);
                ctrlBean.setSw(255);

            } else if (onoff == 1) {
                ctrlBean.setHandle(6);
                ctrlBean.setSw(0);

            }
            ctrlBean.setLearnmode(0);
            ctrlBean.setCmd(10);
            ctrlBean.setDelayS(time);
            payloadBean.setClusterID(clusterID);
        } else if (device == 2) {
            pushcmdBean.setDb_sbID(db_sbID);

            if (onoff == 0) {
                ctrlBean.setHandle(5);
                ctrlBean.setSw(255);
                ctrlBean.setLocation(255);

            } else if (onoff == 1) {
                ctrlBean.setHandle(6);
                ctrlBean.setSw(0);
                ctrlBean.setLocation(0);

            }
            ctrlBean.setCmd(10);
            ctrlBean.setDelayS(time);
            payloadBean.setClusterID(clusterID);
        } else if (device == 3) {
            pushcmdBean.setDb_sbID(db_sbID);

            if (onoff == 0) {
                ctrlBean.setHandle(5);
                ctrlBean.setSw(255);
                ctrlBean.setCmd(10);

            }
            ctrlBean.setDelayS(time);
            payloadBean.setClusterID(clusterID);
        } else if (device == 4) {
            pushcmdBean.setDb_sbID(db_sbID);

            if (onoff == 0) {
                ctrlBean.setHandle(5);
                ctrlBean.setSw(255);

            } else if (onoff == 1) {
                ctrlBean.setHandle(6);
                ctrlBean.setSw(0);

            }
            ctrlBean.setDelayS(time);
            ctrlBean.setCmd(10);
            payloadBean.setClusterID(clusterID);

        } else if (device == 10) {

            pushcmdBean.setDb_sbID(db_sbID);
            if (onoff == 0) {
                ctrlBean.setHandle(5);
                ctrlBean.setSw(255);

            } else if (onoff == 1) {
                ctrlBean.setHandle(6);
                ctrlBean.setSw(0);

            }
            ctrlBean.setDelayS(time);
            ctrlBean.setCmd(10);
            payloadBean.setClusterID(clusterID);
        } else if (device == 11) {

            pushcmdBean.setDb_sbID(db_sbID);
            if (onoff == 0) {
                ctrlBean.setHandle(5);
                ctrlBean.setSw(255);
            } else if (onoff == 1) {
                ctrlBean.setHandle(6);
                ctrlBean.setSw(0);
            }
            ctrlBean.setDelayS(time);
            ctrlBean.setCmd(10);
            payloadBean.setClusterID(clusterID);
        }
        payloadBean.setCtrl(ctrlBean);
        if (plist == null) {
            plist = new ArrayList<PushcmdBean.PayloadBean>();
        }
        plist.clear();
        plist.add(payloadBean);
        pushcmdBean.setPayload(plist);

        final Gson gson = new Gson();
        String jsonString = gson.toJson(pushcmdBean);
       /* synchronized (BaseApplication.getContext()){
            mMyQueue.enQueue(jsonString);
        }*/
        PushcmdHand(jsonString, db_sbID, mac, dd_ui_id);
        plist.clear();
         // Log.i("qaz", "生成的控制命令 " + jsonString.toString());
    }

    private static void Pushcmd(String data, int id, String mac ,int dd_ui_id) {


        Log.i("qaz", "run: 生成的json" + data.toString());
        Listener.mqttPublishers(id, data, mac, dd_ui_id,QoS.EXACTLY_ONCE, false, new Listener.ForResultListener() {
            @Override
            public void onResponseMessage(String response) {

                if ("OK".equals(response)) {
                    Log.i("qaz", "run: 控制成功" );
                    startSpeaking("控制成功");
                } else {

                    startSpeaking("控制失败");
                }

            }
        });
    }

    private static void PushcmdHand(String data, int id, String mac,int dd_ui_id) {

        Log.i("qaz", "run: 生成的json" + data.toString());
        // Log.i("qaz", "run: 生成的clusterID" + m);
        Listener.mqttPublishers(id, data, mac,dd_ui_id, QoS.EXACTLY_ONCE, false, new Listener.ForResultListener() {
            @Override
            public void onResponseMessage(String response) {

                if ("OK".equals(response)) {
                    Log.i("qaz", "onResponseMessage: 返回成功");

                    // startSpeaking("控制成功");
                } else {

                    // startSpeaking("控制失败");
                }

            }
        });
    }

}
