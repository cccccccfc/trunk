package com.base.project.fragment;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.activity.MainActivityTabHost;
import com.base.project.activity.facility.ActivityTemperature;
import com.base.project.adapter.FragmentHomeAdapter;
import com.base.project.base.BaseApplication;
import com.base.project.base.BaseFragment;
import com.base.project.bean.ListenerDataBean;
import com.base.project.bean.PayloadBean;
import com.base.project.bean.WeatherBean;
import com.base.project.severs.Listener;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.Constants;
import com.base.project.utils.FileCacheUtil;
import com.base.project.utils.SpTools;
import com.base.project.view.DoubleListDialog;
import com.base.project.view.adapter.HomeHomeListAdapter;
import com.base.utilslibrary.bean.CtrlListIndexBean;
import com.base.utilslibrary.bean.DataInfo;
import com.base.utilslibrary.bean.FragmentHomeShowBean;
import com.base.utilslibrary.bean.GroupInfoBean;
import com.base.utilslibrary.bean.PushDataBean;
import com.base.utilslibrary.internet.HomeListtoGroupRequestUtils;
import com.base.utilslibrary.internet.PersonalHomeManagerRequestUtils;
import com.google.gson.Gson;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.base.project.utils.Constants.home_mgid;

/**
 * @author IMXU
 * @time 2017/5/3 13:21
 * @des 资讯首页
 * 邮箱：butterfly_xu@sina.com
 */
public class FragmentHome extends BaseFragment implements View.OnClickListener {

    private static String temp;
    private static String ch2o;
    private static String co2;
    private static String pm25;
    private static String light;
    private static String noise;
    private static String humi;

    @BindView(R.id.iv_base_backto)
    ImageView ivBaseBackto;
    @BindView(R.id.iv_base_back)
    ImageView ivBaseBack;
    @BindView(R.id.tv_base_title)
    TextView tvBaseTitle;
    @BindView(R.id.iv_base_edit)
    ImageView ivBaseEdit;
    @BindView(R.id.iv_base_more)
    ImageView iv_more;
    @BindView(R.id.iv_base_down)
    ImageView iv_down;
    @BindView(R.id.rl_home_title)
    RelativeLayout rl_title;
    @BindView(R.id.linear_home_roomtem)
    LinearLayout linearHomeRoomtem;
    @BindView(R.id.text_home_outsidetem)
    TextView textHomeOutsidetem;
    @BindView(R.id.linear_home_outsidetem)
    LinearLayout linearHomeOutsidetem;
    @BindView(R.id.linear_home_weather)
    LinearLayout linearHomeWeather;
    @BindView(R.id.linear_home_title)
    LinearLayout linearHomeTitle;
    @BindView(R.id.recycler_home_surface)
    RecyclerView recyclerHomeSurface;
    private View view;
    private FragmentHomeAdapter fragmenthomeAdapter;
    @BindView(R.id.srl_home)
    SmartRefreshLayout smartRefreshLayout;
    private static Context context;
    private static TextView textHomeRoomtem;
    private ArrayList<String> floorInfo;
    private ArrayList<ArrayList<String>> houseInfo;
    private ArrayList<String> arrayList;
    private String houseEquipmentInfo;
    private String houseWeatherInfo;
    private static List<FragmentHomeShowBean.ListBean> changehome;
    private static String Tv_db_sbID;
    private static String Air_db_sbID;
    private static String Lock_db_sbID;
    private static String Trend_db_sbID;
    private static String Curtain_db_sbID;
    private static String Volve_db_sbID;
    private static String Lingt_db_sbID;
    private List<FragmentHomeShowBean> listhome;
    private int pm2_5;
    private int aqi;
    private double co;
    private int pm10;
    private int so2;
    private int no2;
    private String wendu;
    private static String[] namelist;
    private static String Shexiang_db_sbID;
    private static PushDataBean pushDataBean;
    private static PushDataBean.PayloadBean payloadBean;
    private static PushDataBean.PayloadBean.DataBean dataBean;
    private static List<PushDataBean.PayloadBean> pushDatalist;
    private static String mac;
    private DoubleListDialog dialog;
    private String mgid;
    private static String Wendu_db_sbID;
    private HomeHomeListAdapter adapter;
    private List<FragmentHomeShowBean.ListBean> listsbean;
    private List<FragmentHomeShowBean.ListBean> listvoice;
    private static TimerTask task;


    @Override
    public View initView(LayoutInflater inflater) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_home, null);
            getData();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }

        textHomeRoomtem = (TextView) view.findViewById(R.id.text_home_roomtem);
        ButterKnife.bind(this, view);

        ivBaseEdit.setVisibility(View.INVISIBLE);
        ivBaseBackto.setVisibility(View.INVISIBLE);
        iv_more.setVisibility(View.VISIBLE);
        iv_down.setVisibility(View.VISIBLE);

        MainActivityTabHost.changeBackgroud(R.drawable.bg_home);
        ClassicsHeader header = new ClassicsHeader(getActivity());
        header.setPrimaryColors(this.getResources().getColor(R.color.colorNUll), Color.WHITE);
        smartRefreshLayout.setRefreshHeader(header);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                if (SpTools.getInt(getActivity(), Constants.home_position, 0) > 0) {
                    changeHomeDataList(SpTools.getString(getActivity(), Constants.information, ""));
                } else {
                    getDataFromList();
                }
                refresh(SpTools.getString(getActivity(), Constants.floor_name, ""), SpTools.getString(getActivity(), Constants.home_mgid, "")
                        , SpTools.getString(getActivity(), Constants.house_name, ""));
                //getData();
                smartRefreshLayout.finishRefresh(1000);
            }
        });
        context = getActivity();
        return view;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void initdata() {
        super.initdata();
    }

    /**
     * 请求首页数据
     */
    public void getData() {
        if (SpTools.getInt(getActivity(), Constants.home_position, 0) > 0) {
            //  Log.i("qaz", "标题3 ");
            changeHomeData(SpTools.getString(getActivity(), Constants.information, ""));
        } else {
            getDataFromID();
        }
        super.initdata();
    }


    /**
     * 获取设备最新状态
     */

    public static void PushDataBean(int db_sbID) {
        if (pushDataBean == null) {
            pushDataBean = new PushDataBean();

        }
        if (payloadBean == null) {
            payloadBean = new PushDataBean.PayloadBean();
        }

        if (dataBean == null) {
            dataBean = new PushDataBean.PayloadBean.DataBean();
        }

        pushDataBean.setDb_sbID(0);
        dataBean.setPacketID(132);
        dataBean.setCmd(19);
        dataBean.setClusterID(0);
        dataBean.setDb_sbID(db_sbID);
        payloadBean.setData(dataBean);
        payloadBean.setClusterID(1);
        if (pushDatalist == null) {
            pushDatalist = new ArrayList<PushDataBean.PayloadBean>();
        }
        pushDatalist.clear();
        pushDatalist.add(payloadBean);
        pushDataBean.setPayload(pushDatalist);
        final Gson gson = new Gson();
        String jsonString = gson.toJson(pushDataBean);
        // Log.i("-----","-----jsonString---"+jsonString);
        Listener.mqttPublisher(jsonString, mac, new Listener.ForResultListener() {
            @Override
            public void onResponseMessage(String response) {

                if ("OK".equals(response)) {
                    Log.i("qaz", "onResponseMessage: 获取成功");
                    // CommonUtils.toastMessage();
                } else {
                    //   setTime();
                    Log.i("qaz", "onResponseMessage: 获取失败");
                }

            }
        });
        pushDatalist.clear();
    }

    @Override
    public void initListener() {
        //点击天气图标 携带参数跳转页面
        linearHomeWeather.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ActivityTemperature.class);
                intent.putExtra("temp", temp);
                intent.putExtra("ch2o", ch2o);
                intent.putExtra("co2", co2);
                intent.putExtra("pm25", pm25);
                intent.putExtra("light", light);
                intent.putExtra("noise", noise);
                intent.putExtra("humi", humi);
                intent.putExtra("pm2_5", pm2_5);
                intent.putExtra("aqi", aqi);
                intent.putExtra("co", co);
                intent.putExtra("pm10", pm10);
                intent.putExtra("so2", so2);
                intent.putExtra("no2", no2);
                intent.putExtra("wendu", wendu);
                startActivity(intent);
            }
        });
        iv_more.setOnClickListener(this);
        iv_down.setOnClickListener(this);
        tvBaseTitle.setOnClickListener(this);
        /**
         * 获取mqtt反馈的数据
         */
        Listener.setJsonDataInfoListener(new Listener.JsonDataInformationListener() {
            @Override
            public void onResult(final String json) {
                //  Log.i("wsx", "室内温度 " + json);
                if (json.equals("onFailure")) {
                    if (getActivity() != null) {
                        if (json.equals("onFailure")) {

                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    CommonUtils.toastMessage("连接服务器失败，请重试");
                                }
                            });
                        }
                    }
                    processChange(json);
                    return;
                }
                if (json.equals("onConnected")) {
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CommonUtils.toastMessage("服务器连接成功");
                        }
                    });
                    return;
                }

                ListenerDataBean bean = new Gson().fromJson(json, ListenerDataBean.class);


                Log.i("qaz", "1查询返回 "+refresh);

                if (refresh) {
                        // 判断有无查询设备状态的返回数据
                    if (bean.messpacketid.equals("132")) {
                        refresh = false;
                    }
                }

                //  Log.i("qaz", "onResult:设备id "+Tv_db_sbID +"-------"+bean.db_sbID);
                if (json.contains("quality") && json.contains("level")) {/** 室外 */
                    processTempoutdoor(json);
                } else if (json.contains("wendu") && json.contains("shidu")) {/** 室外温度*/
                    WeatherBean infoBean = new Gson().fromJson(json, WeatherBean.class);
                    wendu = infoBean.wendu;
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textHomeOutsidetem.setText(wendu + "℃");
                        }
                    });
                } else if (json.contains("invite_ui_phone")) {/** 接受邀请 */
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final GroupInfoBean infoBean = new Gson().fromJson(json, GroupInfoBean.class);
                            AlertDialog.Builder builder = new AlertDialog.Builder(context); //先得到构造器
                            mDialog = builder.create();
                            mDialog.show();
                            View view = View.inflate(CommonUtils.getContext(), R.layout.activity_dialog_invite, null);
                            TextView textView = (TextView) view.findViewById(R.id.tv_invite_text);
                            textView.setText(infoBean.ui_nc + "(" + infoBean.ui_phone + ")" + "邀请你加入" + infoBean.mg_name);
                            mDialog.getWindow().setContentView(view);
                            mDialog.setCanceledOnTouchOutside(false);
                            view.findViewById(R.id.tv_invite_refuse).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    isagree("2", infoBean);
                                }
                            });
                            view.findViewById(R.id.tv_invite_agree).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    isagree("1", infoBean);

                                }
                            });
                        }
                    });
                } else if (json.contains("apply_ui_phone")) {/** 申请确认 */
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            final GroupInfoBean infoBean = new Gson().fromJson(json, GroupInfoBean.class);
                            AlertDialog.Builder builder = new AlertDialog.Builder(context); //先得到构造器
                            mDialog = builder.create();
                            mDialog.show();
                            View view = View.inflate(CommonUtils.getContext(), R.layout.activity_dialog_invite, null);
                            TextView textView = (TextView) view.findViewById(R.id.tv_invite_text);
                            textView.setText(infoBean.ui_nc + "(" + infoBean.ui_phone + ")" + "申请加入" + infoBean.mg_name);
                            mDialog.getWindow().setContentView(view);
                            mDialog.setCanceledOnTouchOutside(false);
                            view.findViewById(R.id.tv_invite_refuse).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    isagreeApply("2", infoBean);
                                }
                            });
                            view.findViewById(R.id.tv_invite_agree).setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {

                                    isagreeApply("1", infoBean);

                                }
                            });
                        }
                    });
                } else if (Wendu_db_sbID.equals(bean.db_sbID)) {/** 室内温度 */
                    processTemperature(bean.payload);
                } else if (Lingt_db_sbID.equals(bean.db_sbID)) {/** 灯 */
                    Log.i("qaz", "2查询返回 "+facilityrefresh);
                    if (facilityrefresh) {
                        facilityrefresh = false;
                    }
                    FileCacheUtil.setCache(json, getActivity(), "LingtMessage", Context.MODE_PRIVATE);
                    //    SetUihandler(bean);
                    processLight(bean);
                } else if (Shexiang_db_sbID.equals(bean.db_sbID)) {/** 摄像头*/
                    processShexiangtou(bean);
                } else if (Tv_db_sbID.equals(bean.db_sbID)) {/** 红外设备 */
                    if (facilityrefresh) {
                        facilityrefresh = false;
                    }
                    FileCacheUtil.setCache(json, getActivity(), "TvMessage", Context.MODE_PRIVATE);
                    processTv(bean);
                } else if (Lock_db_sbID.equals(bean.db_sbID)) {/** 门锁 */
                    if (facilityrefresh) {
                        facilityrefresh = false;
                    }
                    FileCacheUtil.setCache(json, getActivity(), "LockMessage", Context.MODE_PRIVATE);
                    processLock(bean);
                } else if (Curtain_db_sbID.equals(bean.db_sbID)) {/** 窗帘 */
                    FileCacheUtil.setCache(json, getActivity(), "CurtainMessage", Context.MODE_PRIVATE);
                    processCurtain(bean);
                } else if (Volve_db_sbID.equals(bean.db_sbID)) {/** 阀门 */
                    if (facilityrefresh) {
                        facilityrefresh = false;
                    }
                    FileCacheUtil.setCache(json, getActivity(), "VolveMessage", Context.MODE_PRIVATE);
                    processValve(bean);
                } else if (Trend_db_sbID.equals(bean.db_sbID)) {/** 空气净化器 */
                    // FileCacheUtil.setCache(json,getActivity(),"TrendMessage", Context.MODE_PRIVATE);
                    // processLight(bean);
                }
            }
        });
        super.initListener();
    }

    public static boolean isFacilityrefresh() {
        return facilityrefresh;
    }

    public static void setFacilityrefresh(boolean facilityrefresh) {
        FragmentHome.facilityrefresh = facilityrefresh;
    }

    private static boolean facilityrefresh = false;

    public static void setRefresh(boolean refresh) {
        FragmentHome.refresh = refresh;
    }

    public static boolean isRefresh() {
        return refresh;
    }

    private static boolean refresh = false;
//    public interface OnSetUIListener {
//        void onSetUI(String s);
//    }
//    private static OnSetUIListener mOnSetUIListener;
//    public static void setOnSetUIListener(OnSetUIListener listener) {
//        mOnSetUIListener = listener;
//    }


    /**
     * 红外设备开关状态
     *
     * @param bean
     */
    private void processTv(ListenerDataBean bean) {
        //Log.i("qaz", "onProcessTv: " +bean.payload.get(0).data.sw);
        if (mOnProcessTvListener != null) {
            mOnProcessTvListener.onProcessTv(bean);
        }

    }

    public interface OnProcessTvListener {
        void onProcessTv(ListenerDataBean bean);
    }

    private static OnProcessTvListener mOnProcessTvListener;

    public static void setOnProcessTvListener(OnProcessTvListener listener) {
        mOnProcessTvListener = listener;
    }

    /**
     * 阀门状态
     *
     * @param bean
     */
    private void processValve(ListenerDataBean bean) {
        if (mOnProcessValveListener != null) {
            mOnProcessValveListener.onProcessValve(bean);
        }
    }

    /**
     * 设备状态
     */

    public interface OnProcessValveListener {
        void onProcessValve(ListenerDataBean bean);
    }

    private static OnProcessValveListener mOnProcessValveListener;

    public static void setOnProcessValveListener(OnProcessValveListener listener) {
        mOnProcessValveListener = listener;
    }

    /**
     * 判断窗帘的开关信息
     *
     * @param bean
     */
    private void processCurtain(ListenerDataBean bean) {

    }

    /**
     * 判断门锁开关状态
     *
     * @param bean
     */
    private void processLock(ListenerDataBean bean) {
        if (mOnProcessLockListener != null) {
            mOnProcessLockListener.onProcessLock(bean);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_base_more://主页家庭切换
                if (!CommonUtils.isNetworkAvailable(getContext())) {
                    CommonUtils.toastMessage("当前无可用网络，在有网时刷新再试");
                    return;
                }
                if (mPopWindow != null && mPopWindow.isShowing()) {
                    mPopWindow.dismiss();
                } else {
                    showPopupWindow();
                }
                break;
            case R.id.tv_base_title://楼层
            case R.id.iv_base_down://楼层切换
                if (!CommonUtils.isNetworkAvailable(getContext())) {
                    CommonUtils.toastMessage("当前无可用网络，在有网时刷新再试");
                    return;
                }
                if (floorInfo == null || houseInfo == null) {
                    return;
                }
                dialog = new DoubleListDialog(getActivity(), floorInfo, houseInfo);
                dialog.setCallBackListener(new DoubleListDialog.CallBack() {
                    @Override
                    public void clickResult(final String data1, String data2, final String house) {
                        textHomeRoomtem.setText("0℃");
                        // temp = "";
                        SpTools.setString(getActivity(), Constants.home_mgid, data2);
                        SpTools.setString(getActivity(), Constants.floor_name, data1);
                        SpTools.setString(getActivity(), Constants.house_name, house);
                        refresh(data1, data2, house);
                        dialog.dismiss();
                    }
                });
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
                break;
        }
    }

    /**
     * 获取选择房间的设备列表
     */

    private void refresh(final String data1, String data2, final String house) {

        HomeListtoGroupRequestUtils.getctrllist(getActivity(), SpTools.getString(getActivity(), Constants.userId, "0")
                , data2, "2", "1", new HomeListtoGroupRequestUtils.CtrllistListener() {
                    @Override
                    public void onResponseMessage(List<CtrlListIndexBean> info) {
                        if (info == null) {

                        } else {
                            presentation(info);
                        }
                        tvBaseTitle.setText(data1 + "的" + house);

                    }
                });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SpTools.getBoolean(getActivity(), Constants.housechange, false)) {
            SpTools.setBoolean(CommonUtils.getContext(), Constants.housechange, false);
            getData();
        }
        CommonUtils.logMes("-----delete----" + SpTools.getBoolean(getActivity(), Constants.deletehome, false));
        /**
         * 删除房间的时候要重新用id刷新
         */
        if (SpTools.getBoolean(getActivity(), Constants.deletehome, false)) {
            SpTools.setBoolean(CommonUtils.getContext(), Constants.deletehome, false);
            SpTools.setString(getActivity(), Constants.home_name, "");
            SpTools.setString(getActivity(), Constants.information, "");
            SpTools.setString(getActivity(), Constants.home_role, "");
            SpTools.setInt(getActivity(), Constants.home_position, 0);
            getDataFromID();
            //CommonUtils.logMes("-----name------" + SpTools.getString(getActivity(), Constants.home_name, ""));
            //CommonUtils.logMes("-----info------" + SpTools.getString(getActivity(), Constants.information, ""));
            // CommonUtils.logMes("-----posi------" + SpTools.getInt(getActivity(), Constants.home_position, 0));
        }
    }

    /**
     * 设置首页数据
     *
     * @param info
     */
    private void presentation(final List<CtrlListIndexBean> info) {
        listhome = new ArrayList<>();
        listvoice = new ArrayList<>();
        for (int i = 0; i < info.size(); i++) {
            for (int j = 0; j < info.get(i).publicList.size(); j++) {
                FragmentHomeShowBean fragmenthomeShowBean = new FragmentHomeShowBean();
                if ("100".equals(info.get(i).publicList.get(j).db_dt_id)) {
                } else if (61 == info.get(i).publicList.get(j).db_dt_id) {  //电视 空调
                    Tv_db_sbID = info.get(i).publicList.get(j).dd_db_sbID;
                    SpTools.setString(BaseApplication.getContext(), Constants.sbid, String.valueOf(info.get(i).publicList.get(j).dd_db_sbID));
                } else if (73 == info.get(i).publicList.get(j).db_dt_id) { //门锁
                    Lock_db_sbID = info.get(i).publicList.get(j).dd_db_sbID;
                } else if (46 == info.get(i).publicList.get(j).db_dt_id) {   //空气净化器
                    Trend_db_sbID = info.get(i).publicList.get(j).dd_db_sbID;
                } else if (9 == info.get(i).publicList.get(j).db_dt_id) {  //窗帘
                    Curtain_db_sbID = info.get(i).publicList.get(j).dd_db_sbID;
                } else if (75 == info.get(i).publicList.get(j).db_dt_id) {  //煤气阀门
                    Volve_db_sbID = info.get(i).publicList.get(j).dd_db_sbID;
                } else if (5 == info.get(i).publicList.get(j).db_dt_id) { //灯
                    Lingt_db_sbID = info.get(i).publicList.get(j).dd_db_sbID;
                } else if (76 == info.get(i).publicList.get(j).db_dt_id) { //温度
                    Wendu_db_sbID = info.get(i).publicList.get(j).dd_db_sbID;
                } else if (78 == info.get(i).publicList.get(j).db_dt_id) { //奇耐ZIGBEE开关面板
                    Lingt_db_sbID = info.get(i).publicList.get(j).dd_db_sbID;
                    //Log.i("wsx", "1presentation: " + info.get(i).get(j).di_db_sbID);
                } else if (7 == info.get(i).publicList.get(j).db_dt_id) { //摄像头
                    Shexiang_db_sbID = info.get(i).publicList.get(j).dd_db_sbID;
                }
                SpTools.setString(CommonUtils.getContext(), Constants.dataid, String.valueOf(info.get(i).publicList.get(j).dd_ui_id));
                fragmenthomeShowBean.setDb_dt_id(info.get(i).publicList.get(j).db_dt_id);
                fragmenthomeShowBean.setDi_clustername(info.get(i).publicList.get(j).db_name);
                fragmenthomeShowBean.setDi_db_sbID(Integer.parseInt(info.get(i).publicList.get(j).dd_db_sbID));
                fragmenthomeShowBean.setDd_macaddr(info.get(i).publicList.get(j).dd_macaddr);
                fragmenthomeShowBean.setDi_status(info.get(i).publicList.get(j).di_status);
                fragmenthomeShowBean.setDd_ui_id(info.get(i).publicList.get(j).dd_ui_id);
                // fragmenthomeShowBean.setList(info.get(i).publicList.get(j).list);
                listsbean = new ArrayList<>();
                for (int h = 0; h < info.get(i).publicList.get(j).list.size(); h++) {
                    FragmentHomeShowBean.ListBean listBean = new FragmentHomeShowBean.ListBean();
                    listBean.setDb_dt_id(info.get(i).publicList.get(j).list.get(h).db_dt_id);
                    listBean.setDi_clustername(info.get(i).publicList.get(j).list.get(h).di_clustername);
                    listBean.setDd_db_sbID(info.get(i).publicList.get(j).list.get(h).dd_db_sbID);
                    listBean.setDd_macaddr(info.get(i).publicList.get(j).list.get(h).dd_macaddr);
                    listBean.setDi_status(info.get(i).publicList.get(j).list.get(h).di_status);
                    listBean.setDd_ui_id(info.get(i).publicList.get(j).list.get(h).dd_ui_id);
                    listBean.setDi_clusterID(info.get(i).publicList.get(j).list.get(h).di_clusterID);
                    listsbean.add(listBean);
                }
                fragmenthomeShowBean.setList(listsbean);
                listvoice.addAll(listsbean);//语音页面需要的数据
                //  Log.i("wsx", "presentation:打印一下订阅id " + info.get(i).publicList.get(j).dd_db_sbID);
                listhome.add(fragmenthomeShowBean); //收益也需要的数据
            }
        }

        if (listvoice != null) {
            //Log.i("qaz", "2presentation: " +listsbean.size());
            changehome = listvoice;//语音页面需要的数据
            namelist = new String[listvoice.size()];
            for (int i = 0; i < listvoice.size(); i++) {
                namelist[i] = listvoice.get(i).di_clustername;
            }
        }

        fragmenthomeAdapter = new FragmentHomeAdapter(listhome, MainActivityTabHost.getInstance());
        recyclerHomeSurface.setLayoutManager(new GridLayoutManager(getActivity(), 2));
        recyclerHomeSurface.setAdapter(fragmenthomeAdapter);
        fragmenthomeAdapter.notifyDataSetChanged();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (!TextUtils.isEmpty(houseEquipmentInfo)) {
                    Listener.mqttUnSubscribe(houseEquipmentInfo);
                }
                if (info.size() > 0) {
                    if (!TextUtils.isEmpty(listhome.get(0).dd_macaddr)) {
                        houseEquipmentInfo = "data/" + SpTools.getString(getActivity(), Constants.dataid, "0")
                                + "/" + listhome.get(0).dd_macaddr + "/+";  //38FFD2053058  30FFD4053143
                        mac = listhome.get(0).dd_macaddr;
                        Listener.mqttSubscribe(houseEquipmentInfo);
                    }
                }
                if (!TextUtils.isEmpty(mac)) {
                    // PushDataBean();
                }
                if (TextUtils.isEmpty(houseWeatherInfo)) {
                    //接收邀请
                    houseWeatherInfo = "common/" + SpTools.getString(getActivity(), Constants.userId, "0") + "/#";
                    Listener.mqttSubscribe(houseWeatherInfo);
                    Listener.mqttSubscribe("common/air");
                    Listener.mqttSubscribe("common/weather");
                }
            }
        }, 1000);

    }

    static AlertDialog mDialog;

    /**
     * 根据mqtt返回设备状态更改显示数据
     *
     * @param bean
     */
    private void processLight(ListenerDataBean bean) {
        if (mOnFacilityInfoListener != null) {
            mOnFacilityInfoListener.onFacilityInfo(bean);
        }
    }

    /**
     * 判断mqtt连接状态
     *
     * @param json
     */
    private void processChange(String json) {
        if (getActivity() == null) {
            return;
        }

        if (listhome != null) {
            if (!"onConnected".equals(json)) {
                listhome.get(0).setChangestate(false);
                if (mOnProcessChangeListener != null) {
                    mOnProcessChangeListener.onProcessChange(json);
                }
            }
        }
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                fragmenthomeAdapter.notifyDataSetChanged();
            }
        });
    }

    /**
     * 邀请加入家庭组 刷新数据
     *
     * @param type
     * @param infoBean
     */
    private void isagree(String type, GroupInfoBean infoBean) {
        PersonalHomeManagerRequestUtils.isAgreeInvite(infoBean.ui_id, infoBean.mg_id, infoBean.invite_ui_phone
                , infoBean.mg_m_type, infoBean.room_id, infoBean.invite_ui_id, type, new PersonalHomeManagerRequestUtils.ForResultListener() {
                    @Override
                    public void onResponseMessage(String code) {
                        if ("成功".equals(code)) {
                            if (SpTools.getInt(getActivity(), Constants.home_position, 0) > 0) {
                                changeHomeDataList(SpTools.getString(getActivity(), Constants.information, ""));
                            } else {
                                getDataFromList();
                            }
                        } else {
                            CommonUtils.toastMessage(code);
                        }
                        mDialog.dismiss();
                    }
                });
    }

    /**
     * 申请加入家庭组 确认数据
     *
     * @param type
     * @param infoBean
     */
    private void isagreeApply(String type, GroupInfoBean infoBean) {
        PersonalHomeManagerRequestUtils.isAgreeApply(infoBean.ui_id, infoBean.mg_id, infoBean.apply_ui_phone
                , infoBean.mg_m_type, infoBean.room_id, infoBean.apply_ui_id, type, new PersonalHomeManagerRequestUtils.ForResultListener() {
                    @Override
                    public void onResponseMessage(String code) {
                        if ("成功".equals(code)) {
                            if (SpTools.getInt(getActivity(), Constants.home_position, 0) > 0) {
                                changeHomeDataList(SpTools.getString(getActivity(), Constants.information, ""));
                            } else {
                                getDataFromList();
                            }
                        } else {
                            CommonUtils.toastMessage(code);
                        }
                        mDialog.dismiss();
                    }
                });
    }

    /**
     * 摄像头报警信息
     *
     * @param bean
     */
    private void processShexiangtou(ListenerDataBean bean) {

        if (mOnSecurityAlarmListener != null) {
            mOnSecurityAlarmListener.onSecurityAlarm(bean);
        }
    }

    /**
     * 显示室内天气数据
     *
     * @param
     */
    private void processTemperature(final List<PayloadBean> list) {
        if (list != null) {
            if (list.get(0).info != null) {
                // Log.i("wsx", "室内温度: 1" + list.get(0).info.getStatus());
                if ("online".equals(list.get(0).info.getStatus())) {
                    //Log.i("wsx", "室内温度: 2" + list.get(0).info.getStatus());
                    temp = list.get(0).data.temp;
                    ch2o = list.get(0).data.ch2o;
                    co2 = list.get(0).data.co2;
                    pm25 = list.get(0).data.pm25;
                    light = list.get(2).data.light;
                    noise = list.get(2).data.noise;
                    humi = list.get(0).data.humi;
                    if (mOnTempChangeListener != null) {
                        mOnTempChangeListener.onTempChange(temp, ch2o, co2, pm25, light, noise, humi);
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            textHomeRoomtem.setText(list.get(0).data.temp + "℃");
                        }
                    });
                    //
                } else {
                    temp = "0";
                    ch2o = "0";
                    co2 = "0";
                    pm25 = "0";
                    light = "0";
                    noise = "0";
                    humi = "0";
                    if (mOnTempChangeListener != null) {
                        mOnTempChangeListener.onTempChange("0", "0", "0", "0", "0", "0", "0");
                    }
                }
            }
        }
    }

    /**
     * 显示室外天气数据
     *
     * @param json
     */
    private void processTempoutdoor(String json) {
        WeatherBean infoBean = new Gson().fromJson(json, WeatherBean.class);
        pm2_5 = infoBean.pm2_5;
        aqi = infoBean.aqi; //空气质量
        co = infoBean.co;  //一氧化碳
        pm10 = infoBean.pm10;
        so2 = infoBean.so2;  //二氧化硫
        no2 = infoBean.no2;
        if (mOnTempOutdoorListener != null) {
            mOnTempOutdoorListener.onTempOutdoor(infoBean, wendu);
        }
    }

    /**
     * MQTT连接状态状态监听   ProcessLock
     */
    public interface OnProcessChangeListener {
        void onProcessChange(String bean);
    }

    private static OnProcessChangeListener mOnProcessChangeListener;

    public static void setOnProcessChangeListener(OnProcessChangeListener listener) {
        mOnProcessChangeListener = listener;
    }

    /**
     * 设备状态
     */

    public interface OnProcessLockListener {
        void onProcessLock(ListenerDataBean bean);
    }

    private static OnProcessLockListener mOnProcessLockListener;

    public static void setOnProcessLockListener(OnProcessLockListener listener) {
        mOnProcessLockListener = listener;
    }

    /**
     * 设备状态监听
     */
    public interface OnFacilityInfoListener {
        void onFacilityInfo(ListenerDataBean bean);
    }

    private static OnFacilityInfoListener mOnFacilityInfoListener;

    public static void setOnFacilityInfoListener(OnFacilityInfoListener listener) {
        mOnFacilityInfoListener = listener;
    }

    /**
     * 安防报警监听
     */
    public interface OnSecurityAlarmListener {
        void onSecurityAlarm(ListenerDataBean bean);
    }

    private static OnSecurityAlarmListener mOnSecurityAlarmListener;

    public static void setOnSecurityAlarmListener(OnSecurityAlarmListener listener) {
        mOnSecurityAlarmListener = listener;
    }

    /**
     * 室外温度 传感器监听
     */
    public interface OnTempOutdoorListener {
        void onTempOutdoor(WeatherBean infoBean, String wendu);
    }

    private static OnTempOutdoorListener mOnTempOutdoorListener;

    public static void setOnTempOutdoorListener(OnTempOutdoorListener listener) {
        mOnTempOutdoorListener = listener;
    }

    /**
     * 室内温度 传感器监听
     */
    public interface OnTempChangeListener {
        void onTempChange(String temp, String ch2o, String co2, String pm25, String light, String noise, String humi);
    }

    private static OnTempChangeListener mOnTempChangeListener;

    public static void setOnTempChangeListener(OnTempChangeListener listener) {
        mOnTempChangeListener = listener;
    }

    private static PopupWindow mPopWindow;

    public static PopupWindow getPopWindow() {
        return mPopWindow;
    }

    ListView listView;

    /**
     * 显示家庭组列表
     */
    private void showPopupWindow() {
        View contentView = LayoutInflater.from(getActivity()).inflate(R.layout.activity_home_page_pop, null);
        mPopWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopWindow.setContentView(contentView);
        listView = (ListView) contentView.findViewById(R.id.lv_home_home);
        if (arrayList == null) {
            CommonUtils.toastMessage("暂无数据");
            return;
        }
        adapter = new HomeHomeListAdapter(getContext(), arrayList);
        listView.setAdapter(adapter);
        /** 获取默认的选择家庭信息 */
        adapter.setCurrentItem(SpTools.getInt(getActivity(), Constants.home_position, 0));
        adapter.setClick(true);
        adapter.notifyDataSetChanged();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                changeHomeData(arrayList.get(position).split(",")[1]);
                SpTools.setString(getActivity(), Constants.information, arrayList.get(position).split(",")[1]);
                SpTools.setString(getActivity(), Constants.home_name, arrayList.get(position).split(",")[0]);
                SpTools.setString(getActivity(), Constants.home_role, arrayList.get(position).split(",")[2]);
                SpTools.setInt(getActivity(), Constants.home_position, position);
                // Log.i("qaz", "onItemClick: 楼层信息" + SpTools.getString(getActivity(), Constants.home_name, ""));
            }
        });
//        mPopWindow.setBackgroundDrawable(new BitmapDrawable(getResources()));
//        mPopWindow.setOutsideTouchable(true);
        mPopWindow.showAsDropDown(rl_title);
    }

    /**
     * 语音控制页面获取数据
     *
     * @return
     */
    public static List<FragmentHomeShowBean.ListBean> getChangeHome() {

        return changehome;
    }

    /**
     * 语音控制页面获取数据
     *
     * @return
     */
    public static String[] getChangeName() {

        return namelist;
    }

    /**
     * 获取首页默认数据
     *
     * @param mg_id
     */
    public void changeHomeData(String mg_id) {
        HomeListtoGroupRequestUtils.getHomeChangeInfo(getActivity(), SpTools.getString(getActivity(), Constants.userId, "0")
                , mg_id, new HomeListtoGroupRequestUtils.HomeInfolistListener() {
                    @Override
                    public void onResponseMessage(DataInfo info) {
                        if (mPopWindow != null && mPopWindow.isShowing()) {
                            mPopWindow.dismiss();
                        }
                        if (info == null) {

                        } else {
                            floorInfo = new ArrayList<String>();
                            houseInfo = new ArrayList<ArrayList<String>>();

                            tvBaseTitle.setText(info.list.get(0).child.get(0).mg_name + "的" +
                                    info.list.get(0).child.get(0).child.get(0).mg_name);
                            //  Log.i("qaz", "标题4 " + info.list.get(0).child.get(0).mg_name + "的" +
                            // info.list.get(0).child.get(0).child.get(0).mg_name);
                            SpTools.setString(getActivity(), Constants.home_mgid, info.list.get(0).child.get(0).child.get(0).mg_id);
                            SpTools.setString(getActivity(), Constants.floor_name, info.list.get(0).child.get(0).mg_name);
                            SpTools.setString(getActivity(), Constants.house_name, info.list.get(0).child.get(0).child.get(0).mg_name);
                            presentation(info.device_list);
                            //存房间号
//                            SpTools.setString(getActivity(), Constants.information, info.list.get(0).child.get(0).child.get(0).mg_id);
                            for (int i = 0; i < info.list.get(0).child.size(); i++) {
                                floorInfo.add(info.list.get(0).child.get(i).mg_name);//默认家庭的楼层信息
                                ArrayList<String> listData2 = new ArrayList<String>();
                                for (int j = 0; j < info.list.get(0).child.get(i).child.size(); j++) {
                                    listData2.add(info.list.get(0).child.get(i).child.get(j).mg_name + ","
                                            + info.list.get(0).child.get(i).child.get(j).mg_id);
                                }
                                houseInfo.add(listData2);
                            }
                        }
                        arrayList = new ArrayList<String>();//家庭组列表
                        for (int i = 0; i < info.home_list.size(); i++) {
                            arrayList.add(info.home_list.get(i).mg_name + "," + info.home_list.get(i).mg_id + ","
                                    + info.home_list.get(i).role);

                            // Log.i("wsx", "家庭组列表: =3=" + info.home_list.get(i).mg_name);
                        }

                    }
                });
    }

    /**
     * 刷新家庭组
     *
     * @param mg_id
     */
    public void changeHomeDataList(String mg_id) {
        HomeListtoGroupRequestUtils.getHomeChangeInfo(getActivity(), SpTools.getString(getActivity(), Constants.userId, "0")
                , mg_id, new HomeListtoGroupRequestUtils.HomeInfolistListener() {
                    @Override
                    public void onResponseMessage(DataInfo info) {
                        if (mPopWindow != null && mPopWindow.isShowing()) {
                            mPopWindow.dismiss();
                        }
                        if (info == null) {

                        } else {
                    /*        tvBaseTitle.setText(info.list.get(0).child.get(0).mg_name + "的" +
                                    info.list.get(0).child.get(0).child.get(0).mg_name);
                            SpTools.setString(getActivity(), Constants.home_mgid, info.list.get(0).child.get(0).child.get(0).mg_id);
                            SpTools.setString(getActivity(), Constants.floor_name, info.list.get(0).child.get(0).mg_name);
                            SpTools.setString(getActivity(), Constants.house_name, info.list.get(0).child.get(0).child.get(0).mg_name);
*/
                            arrayList = new ArrayList<String>();//家庭组列表
                            for (int i = 0; i < info.home_list.size(); i++) {
                                arrayList.add(info.home_list.get(i).mg_name + "," + info.home_list.get(i).mg_id + ","
                                        + info.home_list.get(i).role);

                                // Log.i("wsx", "家庭组列表: =4=" + info.home_list.get(i).mg_name);
                            }

                        }
                    }
                });

    }

    /**
     * 获取首页 家庭组 数据
     */
    private void getDataFromID() {
        HomeListtoGroupRequestUtils.getHomePageInfo(getActivity(), SpTools.getString(getActivity(), Constants.userId, "0")
                , new HomeListtoGroupRequestUtils.HomeInfolistListener() {
                    @Override
                    public void onResponseMessage(DataInfo info) {
                        if (info == null) {

                        } else {
                            floorInfo = new ArrayList<String>();
                            houseInfo = new ArrayList<ArrayList<String>>();
                            presentation(info.device_list);
                            tvBaseTitle.setText(info.list.get(0).child.get(0).mg_name + "的" +
                                    info.list.get(0).child.get(0).child.get(0).mg_name);
                            // Log.i("qaz", "标题1 " + info.list.get(0).child.get(0).mg_name + "的" +
                            // info.list.get(0).child.get(0).child.get(0).mg_name);
                            //存房间号
                            SpTools.setString(getActivity(), home_mgid, info.list.get(0).child.get(0).child.get(0).mg_id);
                            SpTools.setString(getActivity(), Constants.home_mgid, info.list.get(0).child.get(0).child.get(0).mg_id);
                            SpTools.setString(getActivity(), Constants.floor_name, info.list.get(0).child.get(0).mg_name);
                            SpTools.setString(getActivity(), Constants.house_name, info.list.get(0).child.get(0).child.get(0).mg_name);
                            for (int i = 0; i < info.list.get(0).child.size(); i++) {
                                floorInfo.add(info.list.get(0).child.get(i).mg_name);//默认家庭的楼层信息
                                ArrayList<String> listData2 = new ArrayList<String>();
                                for (int j = 0; j < info.list.get(0).child.get(i).child.size(); j++) {
                                    listData2.add(info.list.get(0).child.get(i).child.get(j).mg_name + ","
                                            + info.list.get(0).child.get(i).child.get(j).mg_id);
                                }
                                houseInfo.add(listData2);
                            }
                            arrayList = new ArrayList<String>();//家庭组列表
                            for (int i = 0; i < info.home_list.size(); i++) {
                                arrayList.add(info.home_list.get(i).mg_name + "," + info.home_list.get(i).mg_id + ","
                                        + info.home_list.get(i).role);
                                // Log.i("wsx", "家庭组列表: =2=" + info.home_list.get(i).mg_name);
                            }

                        }
                    }
                });
    }

    /**
     * 刷新家庭组
     */
    private void getDataFromList() {
        HomeListtoGroupRequestUtils.getHomePageInfo(getActivity(), SpTools.getString(getActivity(), Constants.userId, "0")
                , new HomeListtoGroupRequestUtils.HomeInfolistListener() {
                    @Override
                    public void onResponseMessage(DataInfo info) {
                        if (info == null) {

                        } else {
                            arrayList = new ArrayList<String>();//家庭组列表
                            for (int i = 0; i < info.home_list.size(); i++) {
                                arrayList.add(info.home_list.get(i).mg_name + "," + info.home_list.get(i).mg_id + ","
                                        + info.home_list.get(i).role);
                                //Log.i("wsx", "家庭组列表: =1=" + info.home_list.get(i).mg_name);
                            }
                        }
                    }
                });
    }
}