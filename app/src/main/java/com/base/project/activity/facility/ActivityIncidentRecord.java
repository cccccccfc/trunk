package com.base.project.activity.facility;

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
import com.base.project.adapter.WriteTimeAdapter;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.Constants;
import com.base.project.utils.DateUtil;
import com.base.project.utils.SpTools;
import com.base.utilslibrary.bean.HistoryListBean;
import com.base.utilslibrary.internet.GetControlHistoryRequestUtils;

import org.feezu.liuli.timeselector.TimeSelector;

import java.text.ParseException;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.base.project.utils.DateUtil.formatToformat;
import static com.base.project.utils.DateUtil.isSameDay;

public class ActivityIncidentRecord extends AppCompatActivity implements View.OnClickListener {
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
        internet(String.valueOf(dd_ui_id), db_sbID, "1", DateUtil.getCurrentTime_Assignformat("yyyy-MM-dd"));
        Log.i("qaz", "接收到的参数 " + "clustername" + clustername + "macaddr" + macaddr + "db_sbID" + db_sbID + "di_clusterID" + di_clusterID + "db_sw" + db_sw + "di_status" + di_status);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {

            case R.id.liner_control_after:  // 后一天

                writetimeAdapter = null ;
                try {
                    if (isSameDay(DateUtil.getCurrentData_String(), textControlTime.getText().toString(), "yyyy-MM-dd")) {
                        CommonUtils.toastMessage("还不到明天");
                    } else {
                        String s1 = DateUtil.getTomorrows(String.valueOf(textControlTime.getText()), "yyyy-MM-dd");
                        internet(String.valueOf(dd_ui_id), db_sbID, "1", s1);
                        textControlTime.setText(DateUtil.getTomorrows(String.valueOf(textControlTime.getText()), "yyyy-MM-dd"));

                    }
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                break;
            case R.id.linear_control_before:  // 前一天
                writetimeAdapter = null ;
                try {  //
                    if (isSameDay(DateUtil.getMonth("yyyy-MM-dd"), textControlTime.getText().toString(), "yyyy-MM-dd")) {
                        CommonUtils.toastMessage("只能查询30天的记录");
                    } else {
                        String s = DateUtil.getYesterdays(String.valueOf(textControlTime.getText()), "yyyy-MM-dd");
                        internet(String.valueOf(dd_ui_id), db_sbID, "1", s);
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

    /**
     * 设置时间记录的适配器
     *
     * @param info
     */
    private void writeTime(List<HistoryListBean> info) {

        if (writetimeAdapter == null) {
            writetimeAdapter = new WriteTimeAdapter(info, this);
        }
        recyclerControlRecord.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerControlRecord.setAdapter(writetimeAdapter);
        writetimeAdapter.notifyDataSetChanged();

    }

    /**
     * 网络请求操作事件记录
     *
     * @param ui_id   用户ID
     * @param db_sbID 设备ID
     * @param type    开关记录
     * @param date    日期
     */
    private void internet(String ui_id, int db_sbID, String type, String date) {
        Log.i("qaz", "internet: " + "ui_id" + ui_id + "db_sbID" + db_sbID + "type" + type + "date" + date);
        GetControlHistoryRequestUtils.gethandlehistory(ui_id, db_sbID, type, date, new GetControlHistoryRequestUtils.HandleHistoryListener() {
            @Override
            public void onResponseMessage(List<HistoryListBean> info, String message) {

                if (info == null) {
                    writeTime(info);
                    CommonUtils.toastMessage("暂无数据");
                } else {
                    writeTime(info);
                }
            }
        });

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
                //  Log.i("qaz", "handle: " + selectdate + selectYear);
                internet(String.valueOf(dd_ui_id), db_sbID, "1", selectYear);
            }
        }, DateUtil.getMonth("yyyy-MM-dd HH:mm"), DateUtil.getCurrentTime_String());
        timeSelector.setMode(TimeSelector.MODE.YMD);
        timeSelector.setTitle("选择日期");
        timeSelector.show();

        //显示的时间
        // textControlTime.setText(DateUtil.getCurrentData_String());
    }
}
