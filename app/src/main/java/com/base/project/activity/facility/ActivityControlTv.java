package com.base.project.activity.facility;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.adapter.WriteTimeAdapter;
import com.base.project.base.BaseApplication;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.Constants;
import com.base.project.utils.SpTools;

import org.feezu.liuli.timeselector.TimeSelector;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.base.project.activity.voice.OperatingDevice.operatingDeviceHand;

/**
 * 电视详情页
 */
public class ActivityControlTv extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_backto)
    ImageView ivBaseBackto;
    @BindView(R.id.iv_base_add)
    ImageView ivAddBack;
    @BindView(R.id.tv_base_title)
    TextView tvBaseTitle;
    @BindView(R.id.iv_base_edit)
    ImageView ivBaseEdit;
    @BindView(R.id.img_control_switch)
    CheckBox imgControlSwitch;
    @BindView(R.id.button_tv_up)
    Button buttonTvUp;
    @BindView(R.id.button_tv_down)
    Button buttonTvDown;
    @BindView(R.id.button_tv_right)
    Button buttonTvRight;
    @BindView(R.id.button_tv_left)
    Button buttonTvLeft;
    @BindView(R.id.relative_tv_contorl)
    RelativeLayout relativeTvContorl;
    @BindView(R.id.linear_volume_plus)
    LinearLayout linearVolumePlus;
    @BindView(R.id.liner_volume_minus)
    LinearLayout linerVolumeMinus;
    @BindView(R.id.liner_volume_layout)
    LinearLayout linerVolumeLayout;
    @BindView(R.id.img_facility_incident)
    ImageView imgFacilityIncident;
    private WriteTimeAdapter writetimeAdapter;
    private TimeSelector timeSelector;
    private String macaddr;
    private int db_sbID;
    private int di_clusterID;
    private String db_sw;
    private String di_status;
    private String clustername;
    private String ui_id;
    private int dd_ui_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_tv);
        ButterKnife.bind(this);
        transferData();
        ivAddBack.setVisibility(View.GONE);
        ivBaseBackto.setVisibility(View.VISIBLE);
        tvBaseTitle.setVisibility(View.VISIBLE);
        ivBaseEdit.setVisibility(View.GONE);
        tvBaseTitle.setText(clustername);
        initListener();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_tv_up:  // 上
                CommonUtils.toastMessage("上");
                break;
            case R.id.button_tv_down:  // 下
                CommonUtils.toastMessage("下");

                break;
            case R.id.button_tv_left:  // 左
                CommonUtils.toastMessage("左");
                break;
            case R.id.button_tv_right:  // 右
                CommonUtils.toastMessage("右");
                break;
            case R.id.linear_volume_plus:  // 音量+
                CommonUtils.toastMessage("音量+");
                break;
            case R.id.liner_volume_minus:  // 音量-
                CommonUtils.toastMessage("音量-");
                break;
            case R.id.img_facility_incident:  // 事件记录
                Intent intent = new Intent(this, ActivityIncidentRecord.class);
                intent.putExtra("clustername", clustername);
                intent.putExtra("macaddr", macaddr);
                intent.putExtra("db_sbID", db_sbID);
                intent.putExtra("di_clusterID", di_clusterID);
                intent.putExtra("db_sw", db_sw);
                intent.putExtra("di_status", di_status);
                intent.putExtra("dd_ui_id", dd_ui_id);
                startActivity(intent);
                break;
        }
    }


    /**
     * 点击事件
     */
    private void initListener() {
        imgFacilityIncident.setOnClickListener(this);
        buttonTvUp.setOnClickListener(this);
        buttonTvDown.setOnClickListener(this);
        buttonTvRight.setOnClickListener(this);
        buttonTvLeft.setOnClickListener(this);
        linearVolumePlus.setOnClickListener(this);
        linerVolumeMinus.setOnClickListener(this);
        ivBaseBackto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        imgControlSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CommonUtils.toastMessage("开启");
                    operatingDeviceHand(0, 0, Integer.parseInt(SpTools.getString(BaseApplication.getContext(), Constants.sbid, "")), macaddr ,di_clusterID,0 ,dd_ui_id);
                } else {
                    CommonUtils.toastMessage("关闭");
                    operatingDeviceHand(0 ,1, Integer.parseInt(SpTools.getString(BaseApplication.getContext(), Constants.sbid, "")), macaddr,di_clusterID,0 ,dd_ui_id);
                }
            }
        });
    }

    /**
     * fragmenthome 传过来的参数
     */
    private void transferData() {
        ui_id = SpTools.getString(this, Constants.userId, "0");

        clustername = getIntent().getStringExtra("clustername");

        macaddr = getIntent().getStringExtra("macaddr");

        db_sbID = getIntent().getIntExtra("db_sbID", 0);

        di_clusterID = getIntent().getIntExtra("di_clusterID", 0);

        di_status = getIntent().getStringExtra("di_status");

        dd_ui_id  = getIntent().getIntExtra("dd_ui_id" , 0);
        if (di_status.equals("0")) {
              imgControlSwitch.setEnabled(false);
        } else {
             imgControlSwitch.setEnabled(true);
        }


        db_sw = getIntent().getStringExtra("db_sw");
        if ("0".equals(db_sw)) {
            imgControlSwitch.setChecked(false);
        } else if ("255".equals(db_sw)) {
            imgControlSwitch.setChecked(true);
        }
       // Log.i("qaz", "接收到的参数 " + "clustername" + clustername + "macaddr" + macaddr + "db_sbID" + db_sbID + "di_clusterID" + di_clusterID + "db_sw" + db_sw + "di_status" + di_status);

    }
}
