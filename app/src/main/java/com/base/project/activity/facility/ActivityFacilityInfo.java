package com.base.project.activity.facility;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.activity.MainActivityTabHost;
import com.base.project.adapter.FacilityInfoAdapter;
import com.base.project.bean.ListenerDataBean;
import com.base.project.fragment.FragmentHome;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.FileCacheUtil;
import com.base.utilslibrary.bean.FragmentHomeShowBean;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.File;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.base.project.fragment.FragmentHome.setFacilityrefresh;
import static com.base.project.fragment.FragmentHome.setRefresh;

public class ActivityFacilityInfo extends AppCompatActivity {

    private static TimerTask tasks;
    @BindView(R.id.iv_base_backto)
    ImageView ivBaseBackto;
    @BindView(R.id.iv_base_back)
    ImageView ivBaseBack;
    @BindView(R.id.tv_base_title)
    TextView tvBaseTitle;
    @BindView(R.id.iv_base_down)
    ImageView ivBaseDown;
    @BindView(R.id.iv_base_add)
    ImageView ivBaseEdit;
    @BindView(R.id.rl_home_title)
    RelativeLayout rlHomeTitle;
    @BindView(R.id.recycler_home_surface)
    RecyclerView recyclerHomeSurface;
    private static FacilityInfoAdapter facilityInfoAdapter;
    private Gson gson;
    private String jsonString;
    private List<FragmentHomeShowBean.ListBean> mFacilityList;
    private String clustername;
    private int db_sbID;
    private String db_dt_id;
    private TimerTask task;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_facility_info);
        ButterKnife.bind(this);
        gson = new Gson();
        ivBaseEdit.setVisibility(View.GONE);
        ivBaseBackto.setVisibility(View.VISIBLE);
        ivBaseBack.setVisibility(View.VISIBLE);
        ivBaseDown.setVisibility(View.GONE);
        jsonString = getIntent().getStringExtra("jsonString");
        clustername = getIntent().getStringExtra("clustername");
        db_dt_id = getIntent().getStringExtra("db_dt_id");
        db_sbID = getIntent().getIntExtra("db_sbID", 0);
        tvBaseTitle.setText(clustername);
        Log.i("wsx", "传过来的json " + jsonString);
        initListener();
        SetDate(jsonString);

    }

    private void initListener() {
        ivBaseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ControlState();
    }

    private void SetDate(String jsonString) {
        mFacilityList = gson.fromJson(jsonString, new TypeToken<List<FragmentHomeShowBean.ListBean>>() {
        }.getType());
        facilityInfoAdapter = new FacilityInfoAdapter(mFacilityList, MainActivityTabHost.getInstance());
        recyclerHomeSurface.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerHomeSurface.setAdapter(facilityInfoAdapter);
        facilityInfoAdapter.notifyDataSetChanged();
        RefreshPage();
        FragmentHome.PushDataBean(db_sbID);
        FacilityInfoListener();
        SecurityAlarmListener();


    }

    private void FacilityInfoListener() {

        FragmentHome.setOnFacilityInfoListener(new FragmentHome.OnFacilityInfoListener() {
            @Override
            public void onFacilityInfo(ListenerDataBean bean) {
                for (int i = 0; i < mFacilityList.size(); i++) {
                    if (String.valueOf(mFacilityList.get(i).getDd_db_sbID()).equals(bean.db_sbID)) {
                        for (int j = 0; j < bean.payload.size(); j++) {
                            //Log.i("qaz", "00onItemChange: ---"  + studentList.get(i).di_clusterID+ "----" + bean.payload.get(j).clusterID);
                            if (mFacilityList.get(i).di_clusterID == bean.payload.get(j).clusterID) {
                                mFacilityList.get(i).setDi_sw(bean.payload.get(j).data.sw);
                                if ("online".equals(bean.info.getStatus())) {
                                    mFacilityList.get(i).setDi_status(1);
                                }
                            }
                        }
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        facilityInfoAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        FragmentHome.setOnProcessValveListener(new FragmentHome.OnProcessValveListener() {
            @Override
            public void onProcessValve(ListenerDataBean bean) {
                for (int i = 0; i < mFacilityList.size(); i++) {
                    if (String.valueOf(mFacilityList.get(i).getDd_db_sbID()).equals(bean.db_sbID)) {
                        for (int j = 0; j < bean.payload.size(); j++) {
                            // Log.i("qaz", "00onItemChange: ---"  + mFacilityList.get(i).di_clusterID+ "----" + bean.payload.get(j).clusterID);
                            if (mFacilityList.get(i).di_clusterID == bean.payload.get(j).clusterID) {
                                mFacilityList.get(i).setDi_sw(bean.payload.get(j).data.sw);
                                if ("online".equals(bean.info.getStatus())) {
                                    mFacilityList.get(i).setDi_status(1);
                                }
                            }
                        }
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        facilityInfoAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        FragmentHome.setOnProcessChangeListener(new FragmentHome.OnProcessChangeListener() {
            @Override
            public void onProcessChange(String bean) {
                if (mFacilityList != null) {
                    if (!"onConnected".equals(bean)) {
                        mFacilityList.get(0).setChangestate(false);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        facilityInfoAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
        FragmentHome.setOnProcessTvListener(new FragmentHome.OnProcessTvListener() {
            @Override
            public void onProcessTv(ListenerDataBean bean) {
                for (int j = 0; j < bean.payload.size(); j++) {
                    for (int i = 0; i < mFacilityList.size(); i++) {
                        // Log.i("qaz", "onProcessTv:1111全部" + mFacilityList.get(i).dd_db_sbID);
                        if ("online".equals(bean.info.getStatus())) {
                            if (mFacilityList.get(i).dd_db_sbID == 10005) {
                                if (String.valueOf(bean.payload.get(j).data.ircmd).equals("4")) {
                                    mFacilityList.get(i).setDi_sw(bean.payload.get(j).data.sw);
                                    mFacilityList.get(i).setDi_status(1);
                                }
                            }
                            if (mFacilityList.get(i).dd_db_sbID == 10006) {
                                if (String.valueOf(bean.payload.get(j).data.ircmd).equals("134")) {
                                    mFacilityList.get(1).setDi_sw(bean.payload.get(j).data.sw);
                                    mFacilityList.get(i).setDi_status(1);
                                }
                            }
                        }
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        facilityInfoAdapter.notifyDataSetChanged();
                    }
                });
            }
        });

        FragmentHome.setOnProcessLockListener(new FragmentHome.OnProcessLockListener() {
            @Override
            public void onProcessLock(ListenerDataBean bean) {
                for (int i = 0; i < mFacilityList.size(); i++) {
                    if (String.valueOf(mFacilityList.get(i).dd_db_sbID).equals(bean.db_sbID)) {
                        for (int j = 0; j < bean.payload.size(); j++) {
                            if ("online".equals(bean.info.getStatus())) {
                                mFacilityList.get(i).setDi_status(2);
                            }
                        }
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        facilityInfoAdapter.notifyDataSetChanged();
                    }
                });
            }
        });
//        FragmentHome.setOnSetUIListener(new FragmentHome.OnSetUIListener() {
//            @Override
//            public void onSetUI(String s) {
//                if (s.equals("null")) {
//                    //setRefresh(false);
//                    runOnUiThread(new Runnable() {
//                        @Override
//                        public void run() {
//                            CommonUtils.toastMessage("控制失败");
//                            facilityInfoAdapter.notifyDataSetChanged();
//                        }
//                    });
//                }
//            }
//        });
    }

    private void SecurityAlarmListener() {
        FragmentHome.setOnSecurityAlarmListener(new FragmentHome.OnSecurityAlarmListener() {
            @Override
            public void onSecurityAlarm(ListenerDataBean bean) {
                //  Log.i("qaz", "安防报警信息: " + bean.messagetype);
                if ("notice".equals(bean.messagetype)) {
                    for (int i = 0; i < mFacilityList.size(); i++) {
                        if (String.valueOf(mFacilityList.get(i).dd_db_sbID).equals(bean.db_sbID)) {
                            // Log.i("qaz", "对比设备id"  + lists.get(i).di_db_sbID + "----" + bean.db_sbID);
                            mFacilityList.get(i).setDi_sw("1");
                            mFacilityList.get(i).setDi_status(3);
                        }
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        facilityInfoAdapter.notifyDataSetChanged();
                    }
                });

            }
        });
    }

    private void RefreshPage() {

        if ("100".equals(db_dt_id)) {

        } else if ("61".equals(db_dt_id)) { //红外转发器
            File file = new File(FileCacheUtil.getCachePath(this) + "/" + "TvMessage");
            if (file.exists()) {
                String cache = FileCacheUtil.getCache(this, "TvMessage");
                ListenerDataBean bean = new Gson().fromJson(cache, ListenerDataBean.class);
                for (int j = 0; j < bean.payload.size(); j++) {
                    for (int i = 0; i < mFacilityList.size(); i++) {
                     //   Log.i("qaz", "onProcessTv:1111全部" + mFacilityList.get(i).dd_db_sbID);
                        if ("online".equals(bean.info.getStatus())) {
                            if (mFacilityList.get(i).dd_db_sbID == 10005) {
                                if (String.valueOf(bean.payload.get(j).data.ircmd).equals("4")) {
                                    mFacilityList.get(i).setDi_sw(bean.payload.get(j).data.sw);
                                    mFacilityList.get(i).setDi_status(1);
                                }
                            }
                            if (mFacilityList.get(i).dd_db_sbID == 10006) {
                                if (String.valueOf(bean.payload.get(j).data.ircmd).equals("134")) {
                                    mFacilityList.get(1).setDi_sw(bean.payload.get(j).data.sw);
                                    mFacilityList.get(i).setDi_status(1);
                                }
                            }
                        }
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        facilityInfoAdapter.notifyDataSetChanged();
                    }
                });
            }
        } else if ("76".equals(db_dt_id)) { //传感器

        } else if ("73".equals(db_dt_id)) { //门锁
            File file = new File(FileCacheUtil.getCachePath(this) + "/" + "LockMessage");
            if (file.exists()) {
                String cache = FileCacheUtil.getCache(this, "LockMessage");
                RefreshData(cache);
            }
        } else if ("46".equals(db_dt_id)) {   //空气净化器

        } else if ("9".equals(db_dt_id)) {  //窗帘
            File file = new File(FileCacheUtil.getCachePath(this) + "/" + "CurtainMessage");
            if (file.exists()) {
                String cache = FileCacheUtil.getCache(this, "CurtainMessage");
                //RefreshData(cache);
            }
        } else if ("75".equals(db_dt_id)) {  //煤气阀门
            File file = new File(FileCacheUtil.getCachePath(this) + "/" + "VolveMessage");
            if (file.exists()) {
                String cache = FileCacheUtil.getCache(this, "VolveMessage");
                RefreshData(cache);
            }
        } else if ("78".equals(db_dt_id)) {//灯
            File file = new File(FileCacheUtil.getCachePath(this) + "/" + "LingtMessage");
            if (file.exists()) {
                String cache = FileCacheUtil.getCache(this, "LingtMessage");
                RefreshData(cache);
            }
        } else if ("77".equals(db_dt_id) || "79".equals(db_dt_id)) {//定位

        } else if ("7".equals(db_dt_id)) {//摄像头

        } else { //灯

        }

    }

    private void RefreshData(String cache) {
        ListenerDataBean bean = new Gson().fromJson(cache, ListenerDataBean.class);
        for (int i = 0; i < mFacilityList.size(); i++) {
            if (String.valueOf(mFacilityList.get(i).getDd_db_sbID()).equals(bean.db_sbID)) {
                for (int j = 0; j < bean.payload.size(); j++) {
                    // Log.i("qaz", "onProcessLight:1111全部" + mFacilityList.get(i).dd_db_sbID);
                    //Log.i("qaz", "00onItemChange: ---"  + studentList.get(i).di_clusterID+ "----" + bean.payload.get(j).clusterID);
                    if (mFacilityList.get(i).di_clusterID == bean.payload.get(j).clusterID) {
                        mFacilityList.get(i).setDi_sw(bean.payload.get(j).data.sw);
                        if ("online".equals(bean.info.getStatus())) {
                            mFacilityList.get(i).setDi_status(1);
                        }
                    }
                }
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                facilityInfoAdapter.notifyDataSetChanged();
            }
        });
    }

    private void ControlState() {
        FacilityInfoAdapter.setOnControlStateListener(new FacilityInfoAdapter.OnControlStateListener() {
            @Override
            public void onControlState(String string) {
                setFacilityrefresh(true);
                setTime();
            }
        });
    }

    private void setTime() {
        Timer timer = new Timer();
        task = new TimerTask() {
            @Override
            public void run() {
               // Log.i("qaz", "1查询 "+FragmentHome.isRefresh());
                //Log.i("qaz", "2查询 "+FragmentHome.isFacilityrefresh());
                if (FragmentHome.isFacilityrefresh()) {
                    FragmentHome.setFacilityrefresh(false);
                    setRefresh(true);
                    FragmentHome.PushDataBean(db_sbID);
                    setRefreshTime();
                }

            }
        };
        timer.schedule(task, 3000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null) {
            task.cancel();
            task = null;
        }
    }
    private void setRefreshTime() {
        Timer timer = new Timer();
        tasks = new TimerTask() {

            @Override
            public void run() {
                Log.i("qaz", "run: 确认时间");
                if (FragmentHome.isRefresh()) {
                    setRefresh(false);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            CommonUtils.toastMessage("控制失败");
                            facilityInfoAdapter.notifyDataSetChanged();
                        }
                    });
                }
            }
        };
        timer.schedule(tasks, 3000);
    }
}
