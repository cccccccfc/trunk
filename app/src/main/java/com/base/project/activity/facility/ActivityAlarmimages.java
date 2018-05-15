package com.base.project.activity.facility;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.adapter.RecordListAdapter;
import com.base.project.utils.CommonUtils;
import com.base.utilslibrary.bean.RecordListBean;
import com.base.utilslibrary.internet.GetUserContextualModelRequestUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityAlarmimages extends AppCompatActivity {

    @BindView(R.id.iv_base_back)
    ImageView ivBaseBack;
    @BindView(R.id.tv_base_title)
    TextView tvBaseTitle;
    @BindView(R.id.rv_house_manager_flour)
    RecyclerView rvHouseManagerFlour;
    @BindView(R.id.iv_base_add)
    ImageView ivBaseAdd;
    private int db_sbID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarmimages);
        ButterKnife.bind(this);

        ivBaseBack.setVisibility(View.VISIBLE);
        tvBaseTitle.setText("报警图片");
        ivBaseAdd.setVisibility(View.GONE);
        db_sbID = getIntent().getIntExtra("db_sbID", 0);
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
    }

    private void initdata() {
       // Log.i("qaz", "initdata: 11111111111" + db_sbID);
        GetUserContextualModelRequestUtils.getcamerarecord(String.valueOf(db_sbID), "1", new GetUserContextualModelRequestUtils.DelexCuteListener() {
            @Override
            public void onResponseMessage(List<RecordListBean> message) {
                if (message == null) {
                    CommonUtils.toastMessage("暂无数据");
                } else {
                    presentation(message);

                }
            }
        });
    }

    private void presentation(List<RecordListBean> message) {

        RecordListAdapter record = new RecordListAdapter(message, this);
        rvHouseManagerFlour.setLayoutManager(new GridLayoutManager(this, 1));
        rvHouseManagerFlour.setAdapter(record);

    }
}
