package com.base.project.activity.myself;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.adapter.ListToSceneAdapter;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.Constants;
import com.base.project.utils.SpTools;
import com.base.utilslibrary.bean.CtrlListBean;
import com.base.utilslibrary.bean.SceneListBean;
import com.base.utilslibrary.internet.GetUserContextualModelRequestUtils;

import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.base.project.activity.voice.OperatingDevice.operatingDeviceHand;

public class ActivityPattern extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView ivBaseBack;
    @BindView(R.id.tv_base_title)
    TextView tvBaseTitle;
    @BindView(R.id.iv_base_edit)
    ImageView ivBaseEdit;
    @BindView(R.id.iv_base_add)
    ImageView ivBaseAdd;
    @BindView(R.id.lineat_pattern_hint)
    LinearLayout lineatPatternHint;

    @BindView(R.id.rv_house_manager_flour)
    RecyclerView rvHouseManagerFlour;
    @BindView(R.id.ll_house_manager_flour)
    LinearLayout llHouseManagerFlour;

    private String mg_id;
    private String ub_id;
    private ListToSceneAdapter listtoSceneAdapter;
    private List<SceneListBean> mSceneList;

    private String transmit_name;
    private String transmit_type;
    private String transmit_id;
    private boolean isChecked = true;
    private String role;
    private TimerTask task;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pattern);
        ButterKnife.bind(this);

        tvBaseTitle.setText("情景模式");
        ivBaseAdd.setVisibility(View.VISIBLE);
        ivBaseEdit.setVisibility(View.GONE);
        initdata();
        initListener();

    }

    private void initListener() {
        ivBaseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ivBaseAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication() ,ActivityAddscene.class);
                intent.putExtra("ub_id", ub_id);
                intent.putExtra("mg_id", mg_id);
                intent.putExtra("type", "1");
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });
        /**
         * 进入情景编辑页面
         */
        ListToSceneAdapter.setOnPatternClickListener(new ListToSceneAdapter.OnPatternClickListener() {
            @Override
            public void onPatternClick(View view, int position, String type, String id) {
                for (int i = 0; i < mSceneList.size(); i++) {
                    mSceneList.get(i).setIsshow(false);
                }
                listtoSceneAdapter.notifyDataSetChanged();
                transmit_name = mSceneList.get(position).getMs_name();
                transmit_type = mSceneList.get(position).getMs_type();
                transmit_id = mSceneList.get(position).getMs_id();
                Intent intent = new Intent(getApplication() ,ActivityAddscene.class);
                intent.putExtra("ub_id", ub_id);
                intent.putExtra("mg_id", mg_id);
                intent.putExtra("statetype", "1");
                intent.putExtra("type", "3");
                intent.putExtra("transmit_id", transmit_id);
                intent.putExtra("transmit_name", transmit_name);
                intent.putExtra("transmit_type", transmit_type);
                startActivity(intent);
                overridePendingTransition(0, 0);

            }
        });
        /**
         * 控制编辑按钮
         */
        ivBaseEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isChecked) {
                    for (int i = 0; i < mSceneList.size(); i++) {
                        mSceneList.get(i).setIsshow(true);
                    }
                    isChecked  = !isChecked ;
                    listtoSceneAdapter.notifyDataSetChanged();
                }else{
                    isChecked  = !isChecked ;
                    for (int i = 0; i < mSceneList.size(); i++) {
                        mSceneList.get(i).setIsshow(false);
                    }
                    listtoSceneAdapter.notifyDataSetChanged();
                }
            }
        });
        /**
         * 点击 启动情景模式
         */
        ListToSceneAdapter.setOnCtrlClickListener(new ListToSceneAdapter.OnCtrlClickListener() {
            @Override
            public void onCtrlClick(View view, int position, String type, String id) {
                transmit_name = mSceneList.get(position).getMs_name();
                transmit_type = mSceneList.get(position).getMs_type();
                transmit_id = mSceneList.get(position).getMs_id();
                CommonUtils.toastMessage("已启动");
                setTime();
                getDate(ub_id, transmit_id);
            }
        });
    }
    /**
     * 情景模式定时器
     */
    private void setTime(){
        Timer timer = new Timer();
         task = new TimerTask() {

            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listtoSceneAdapter.notifyDataSetChanged();
                    }
                });
            }
        };
        timer.schedule(task, 3000);
    }
    /**
     * 情景模式列表 进入情景模式详情页 网络请求
     *
     * @param ui_id
     * @param ms_id
     */
    public void getDate(String ui_id, String ms_id) {
      //  Log.i("qaz", "打印数据" + ui_id + "ui_id" + ms_id + "ms_id");
        GetUserContextualModelRequestUtils.getlisttoexecute(ui_id, ms_id, new GetUserContextualModelRequestUtils.ListToExecuteListener() {
            @Override
            public void onResponseMessage(List<List<CtrlListBean>> info, String message) {

                if ("0".equals(message)) {
                    //  Log.i("qaz", "返回的数据 成功1111111");
                    if (info == null) {
                        // Log.i("qaz", "暂无数据11111");
                    } else {
                        presentation(info);
                    }
                } else if ("1".equals(message)) {
                    // Log.i("qaz", "返回的数据 参数错误1111111");
                } else if ("2".equals(message)) {
                    //  Log.i("qaz", "返回的数据 没有数据111111111");
                } else {
                    // Log.i("qaz", "返回的数据 操作失败111111111");
                }
            }
        });
    }
    /**
     * 解析数据
     * @param info
     */
    private void presentation(List<List<CtrlListBean>> info) {
        for (int i = 0; i < info.size(); i++) {
            for (int j = 0; j < info.get(i).size(); j++) {
                try {
                    Thread.currentThread().sleep(100);//阻断0.1秒
                    String  db_dt_id = String.valueOf(info.get(i).get(j).db_dt_id);
                    int db_sbID = info.get(i).get(j).me_di_db_sbID;
                    String  macaddr = info.get(i).get(j).dd_macaddr;
                    int  di_clusterID = info.get(i).get(j).me_di_clusterID;
                    int mh_type = info.get(i).get(j).me_mh_type;
                    int me_time = info.get(i).get(j).me_time; //,dd_ui_id
                    int dd_ui_id = info.get(i).get(j).dd_ui_id;
                    Log.i("wsx", "presentation: " +"--dd_ui_id----"+dd_ui_id);
                    //根据设备id  发送不同的控制命令
                    if ("100".equals(db_dt_id)) {
                    } else if ("61".equals(db_dt_id)) { //传感器
                        SpTools.setString(this, Constants.sbid, String.valueOf(info.get(i).get(j).di_db_sbID));
                    } else if ("42".equals(db_dt_id)) {  //电视
                        if (6 == mh_type) {
                            operatingDeviceHand(0, 0, Integer.parseInt(SpTools.getString(this, Constants.sbid, "")), macaddr, di_clusterID ,me_time ,dd_ui_id);
                        } else if (5== mh_type) {
                            operatingDeviceHand(0, 1, Integer.parseInt(SpTools.getString(this, Constants.sbid, "")), macaddr, di_clusterID,me_time,dd_ui_id);
                        }
                    } else if ("49".equals(db_dt_id)) { //空调
                        if (6 == mh_type) {
                            operatingDeviceHand(1, 1, Integer.parseInt(SpTools.getString(this, Constants.sbid, "")), macaddr, di_clusterID,me_time,dd_ui_id);
                        } else if (5 == mh_type) {
                            operatingDeviceHand(1, 0, Integer.parseInt(SpTools.getString(this, Constants.sbid, "")), macaddr, di_clusterID,me_time,dd_ui_id);
                        }
                    } else if ("73".equals(db_dt_id)) { //门锁
                        if (5== mh_type) {
                            operatingDeviceHand(3, 0, db_sbID, macaddr, di_clusterID,me_time,dd_ui_id);
                        }else if(6== mh_type){
                            operatingDeviceHand(3, 0, db_sbID, macaddr, di_clusterID,me_time,dd_ui_id);
                        }
                    } else if ("46".equals(db_dt_id)) {   //空气净化器
                        if (6 == mh_type) {
                            operatingDeviceHand(11, 1, db_sbID, macaddr, di_clusterID,me_time,dd_ui_id);
                        } else if (5 == mh_type) {
                            operatingDeviceHand(11, 0, db_sbID, macaddr, di_clusterID,me_time,dd_ui_id);
                        }
                    } else if ("9".equals(db_dt_id)) {  //窗帘
                        if (6 == mh_type) {
                            operatingDeviceHand(2, 1, db_sbID, macaddr, di_clusterID,me_time,dd_ui_id);
                        } else if (5== mh_type) {
                            operatingDeviceHand(2, 0, db_sbID, macaddr, di_clusterID,me_time,dd_ui_id);
                        }
                    } else if ("75".equals(db_dt_id)) {  //煤气阀门
                        if (6 == mh_type) {
                            operatingDeviceHand(11, 1, db_sbID, macaddr, di_clusterID,me_time,dd_ui_id);
                        } else if (5 == mh_type) {
                            operatingDeviceHand(11, 0, db_sbID, macaddr, di_clusterID,me_time,dd_ui_id);
                        }
                    } else if ("78".equals(db_dt_id)) {//灯
                        if (6 == mh_type) {
                            operatingDeviceHand(4, 1, db_sbID, macaddr, di_clusterID,me_time,dd_ui_id);
                        } else if (5 == mh_type) {
                            operatingDeviceHand(4, 0, db_sbID, macaddr, di_clusterID,me_time,dd_ui_id);
                        }
                    } else if ("5".equals(db_dt_id)) {//灯
                        if (6 == mh_type) {
                            operatingDeviceHand(4, 1, db_sbID, macaddr, di_clusterID,me_time,dd_ui_id);
                        } else if (5 == mh_type) {
                            operatingDeviceHand(4, 0, db_sbID, macaddr, di_clusterID,me_time,dd_ui_id);
                        }
                    } else {
                        if (6 == mh_type) {
                            Log.i("wsx", "presentation: 1"  +"--di_clusterID--"+di_clusterID);
                            // operatingDeviceHand(4, 1, db_sbID, macaddr, di_clusterID,me_time,dd_ui_id);
                        } else if (5 == mh_type) {
                            Log.i("wsx", "presentation: 1"  +"--di_clusterID--"+di_clusterID);
                            // operatingDeviceHand(4, 0, db_sbID, macaddr, di_clusterID,me_time,dd_ui_id);
                        }
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            }
        }
    }

    /**
     * 获取情景模式列表
     */
    private void initdata() {
        mg_id = getIntent().getStringExtra("mg_id");
        ub_id = getIntent().getStringExtra("ub_id"); //
        role = getIntent().getStringExtra("role");
        if ("0".equals(role)) {
            ivBaseAdd.setEnabled(false);
            ivBaseEdit.setEnabled(false);
        }else{
            ivBaseAdd.setEnabled(true);
            ivBaseEdit.setEnabled(true);
        }
        GetUserContextualModelRequestUtils.getlisttoscene(ub_id, mg_id, new GetUserContextualModelRequestUtils.ListToSceneListener() {
            @Override
            public void onResponseMessage(List<SceneListBean> info, String message) {
                if ("2".equals(message)) {
                    lineatPatternHint.setVisibility(View.VISIBLE);
                    llHouseManagerFlour.setVisibility(View.GONE);
                    ivBaseEdit.setVisibility(View.GONE);
                    CommonUtils.toastMessage("暂无数据");
                } else if ("0".equals(message)) {
                    if (info == null) {
                        CommonUtils.toastMessage("暂无数据");
                    } else {
                        setDate(info);
                    }
                } else {
                    CommonUtils.toastMessage("操作失败");
                }
            }
        });
    }
    /**
     * 设置数据
     *
     * @param info
     */
    private void setDate(List<SceneListBean> info) {
        ivBaseEdit.setVisibility(View.VISIBLE);
        lineatPatternHint.setVisibility(View.GONE);
        llHouseManagerFlour.setVisibility(View.VISIBLE);
        mSceneList = info;
        listtoSceneAdapter = new ListToSceneAdapter(info, this ,role);
        rvHouseManagerFlour.setLayoutManager(new GridLayoutManager(this, 1));
        rvHouseManagerFlour.setAdapter(listtoSceneAdapter);
        listtoSceneAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        if (SpTools.getBoolean(CommonUtils.getContext(), Constants.editpanttern, false)) {
            initdata();
            SpTools.setBoolean(CommonUtils.getContext(), Constants.editpanttern, false);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (task != null) {
            task.cancel();
            task = null;
        }
    }
}
