package com.base.project.activity.facility;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.Constants;
import com.base.project.utils.SpTools;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.base.project.activity.voice.OperatingDevice.operatingDeviceHand;

/**
 * 煤气阀门详情页
 */
public class ActivityControlValve extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_base_backto)
    ImageView ivBaseBackto;
    @BindView(R.id.iv_base_back)
    ImageView ivBaseBack;
    @BindView(R.id.tv_base_title)
    TextView tvBaseTitle;
    @BindView(R.id.iv_base_edit)
    ImageView ivBaseEdit;
    @BindView(R.id.iv_base_add)
    ImageView ivBaseAdd;
    @BindView(R.id.iv_base_member_add)
    ImageView ivBaseMemberAdd;
    @BindView(R.id.tv_base_flour)
    TextView tvBaseFlour;
    @BindView(R.id.tv_base_confirm)
    TextView tvBaseConfirm;
    @BindView(R.id.img_control_switch)
    CheckBox imgControlSwitch;
    @BindView(R.id.img_facility_incident)
    ImageView imgFacilityIncident;
    private String ui_id;
    private String clustername;
    private int db_sbID;
    private String macaddr;
    private String di_status;
    private String db_sw;
    private int di_clusterID;
    private int dd_ui_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_valve);
        ButterKnife.bind(this);

        transferData();
        ivBaseAdd.setVisibility(View.GONE);
        ivBaseBackto.setVisibility(View.VISIBLE);
        tvBaseTitle.setVisibility(View.VISIBLE);
        ivBaseEdit.setVisibility(View.GONE);
        tvBaseTitle.setText(clustername);
        initListener();
        ui_id = SpTools.getString(this, Constants.userId, "0");
    }

    /**
     * 点击事件
     */
    private void initListener() {
        imgFacilityIncident.setOnClickListener(this);
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
                    operatingDeviceHand(11, 0, db_sbID, macaddr, di_clusterID,0 ,dd_ui_id);
                } else {
                    CommonUtils.toastMessage("关闭");
                    operatingDeviceHand(11, 1, db_sbID, macaddr, di_clusterID,0  ,dd_ui_id);
                }
            }
        });

    }




    @Override
    public void onClick(View v) {
        switch (v.getId()) {
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

    @Override
    protected void onResume() {
        super.onResume();
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