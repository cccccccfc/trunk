package com.base.project.activity.facility;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.adapter.BlueRecordAdapter;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.Constants;
import com.base.project.utils.DateUtil;
import com.base.project.utils.SpTools;
import com.base.utilslibrary.bean.BluerecordForuserBean;
import com.base.utilslibrary.internet.GetControlHistoryRequestUtils;

import org.feezu.liuli.timeselector.TimeSelector;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.base.project.utils.DateUtil.formatToformat;
import static com.base.project.utils.DateUtil.isSameDay;

public class ActivityIncidentbody extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.iv_base_backto)
    ImageView ivBaseBackto;
    @BindView(R.id.iv_base_back)
    ImageView ivBaseBack;
    @BindView(R.id.iv_base_add)
    ImageView ivAddBack;
    @BindView(R.id.tv_base_title)
    TextView tvBaseTitle;
    @BindView(R.id.iv_base_edit)
    ImageView ivBaseEdit;
    @BindView(R.id.linear_control_before)
    LinearLayout linearControlBefore;
    @BindView(R.id.text_control_time)
    TextView textControlTime;
    @BindView(R.id.liner_control_after)
    LinearLayout linerControlAfter;
    @BindView(R.id.liner_control_layout)
    LinearLayout linerControlLayout;
    @BindView(R.id.recycler_control_record)
    RecyclerView recyclerControlRecord;
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
        setContentView(R.layout.activity_incident_record);
        ButterKnife.bind(this);
        transferData();
        ivAddBack.setVisibility(View.GONE);
        ivBaseBackto.setVisibility(View.VISIBLE);
        tvBaseTitle.setVisibility(View.VISIBLE);
        ivBaseEdit.setVisibility(View.GONE);
        tvBaseTitle.setText("事件记录");
        initListener();

        //显示的时间
        textControlTime.setText(DateUtil.getCurrentData_String());
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

        dd_ui_id = getIntent().getIntExtra("dd_ui_id", 0);
        db_sw = getIntent().getStringExtra("db_sw");
       // Log.i("qaz", "接收到的参数 " + "clustername" + clustername + "macaddr" + macaddr + "db_sbID" + db_sbID + "di_clusterID" + di_clusterID + "db_sw" + db_sw + "di_status" + di_status);
        bluerecordforuser(String.valueOf(dd_ui_id), DateUtil.getCurrentTime_Assignformat("yyyy-MM-dd"));
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.liner_control_after:  // 后一天
                bluerecordAdapter = null;
                try {
                    if (isSameDay(DateUtil.getCurrentData_String() ,textControlTime.getText().toString(),"yyyy-MM-dd")) {
                        CommonUtils.toastMessage("还不到明天");
                    }else {
                        //  Log.i("qaz", "后一天 " +DateUtil.getTomorrows(String.valueOf(textControlTime.getText()), "yyyy-MM-dd"));
                        bluerecordforuser(String.valueOf(dd_ui_id), DateUtil.getTomorrows(String.valueOf(textControlTime.getText()), "yyyy-MM-dd"));
                        textControlTime.setText(DateUtil.getTomorrows(String.valueOf(textControlTime.getText()), "yyyy-MM-dd"));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.linear_control_before:  // 前一天
                bluerecordAdapter = null;
                try {  //
                    if (isSameDay(DateUtil.getMonth("yyyy-MM-dd") ,textControlTime.getText().toString() ,"yyyy-MM-dd")) {
                        CommonUtils.toastMessage("只能查询30天的记录");
                    }else {
                        // Log.i("qaz", "后一天 " +DateUtil.getYesterdays(String.valueOf(textControlTime.getText()), "yyyy-MM-dd"));
                        bluerecordforuser(String.valueOf(dd_ui_id), DateUtil.getYesterdays(String.valueOf(textControlTime.getText()), "yyyy-MM-dd"));
                        textControlTime.setText(DateUtil.getYesterdays(String.valueOf(textControlTime.getText()), "yyyy-MM-dd"));
                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
        }
    }
    /**
     * 点击事件
     */
    private void initListener() {
        linearControlBefore.setOnClickListener(this);
        linerControlAfter.setOnClickListener(this);
        ivBaseBackto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        textControlTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setDate();
            }
        });
    }

    private BlueRecordAdapter bluerecordAdapter;
    private List<BluerecordForuserBean> listsize;
    private void bluerecordforuser(String dd_ui_id, String time) {
        GetControlHistoryRequestUtils.getbluerecordforuser(dd_ui_id, time, new GetControlHistoryRequestUtils.BluerecordforuserListener() {
            @Override
            public void onResponseMessage(List<BluerecordForuserBean> message) {
                if (message == null) {
                    CommonUtils.toastMessage("暂无数据");
                    presentation(message);
                } else {
                    presentation(message);
                }
            }
        });

    }
    private void presentation(List<BluerecordForuserBean> message) {
        listsize = new ArrayList<BluerecordForuserBean>();
        listsize = message;
        if (bluerecordAdapter == null) {
            bluerecordAdapter = new BlueRecordAdapter(message, this);
        }
        recyclerControlRecord.setLayoutManager(new GridLayoutManager(this, 1));
        recyclerControlRecord.setAdapter(bluerecordAdapter);
        bluerecordAdapter.notifyDataSetChanged();
    }
    /**
     * 日期选择器
     */
    private void setDate() {
        timeSelector = new TimeSelector(this, new TimeSelector.ResultHandler() {
            @Override
            public void handle(String time) {
                String selectdate = formatToformat(time.toString(), "yyyy-MM-dd HH:mm", "MM月dd日");
                String selectYear = formatToformat(time.toString(), "yyyy-MM-dd HH:mm", "yyyy-MM-dd");
                textControlTime.setText(selectYear);
                bluerecordforuser(String.valueOf(dd_ui_id), selectYear);
            }
        }, DateUtil.getMonth("yyyy-MM-dd HH:mm"), DateUtil.getCurrentTime_String());
        timeSelector.setMode(TimeSelector.MODE.YMD);
        timeSelector.setTitle("选择日期");
        timeSelector.show();

    }
}
