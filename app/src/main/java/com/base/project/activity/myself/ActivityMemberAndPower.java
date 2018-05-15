package com.base.project.activity.myself;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.Constants;
import com.base.project.utils.SpTools;
import com.base.project.view.adapter.MemberPowerAdapter;
import com.base.utilslibrary.bean.UserPowerInfo;
import com.base.utilslibrary.internet.PersonalHomeManagerRequestUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityMemberAndPower extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_add)
    ImageView add;
    @BindView(R.id.iv_base_member_add)
    ImageView member_add;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.rv_member_power)
    RecyclerView mRecyclerView;
    LinearLayoutManager layoutManager;
    MemberPowerAdapter memberPowerAdapter;

    private String mg_id;
    private String ub_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_power);
        ButterKnife.bind(this);
        title.setText("成员与权限");
        add.setVisibility(View.INVISIBLE);
        member_add.setVisibility(View.VISIBLE);

        initdata();
        initListener();
    }

    private void initListener() {
        back.setOnClickListener(this);
        member_add.setOnClickListener(this);
    }

    private void initdata() {

        mg_id = getIntent().getStringExtra("mg_id");
        ub_id = getIntent().getStringExtra("ub_id");
        internet();

    }

    private void internet() {
        PersonalHomeManagerRequestUtils.getPowerListInfo(this,ub_id, mg_id, new PersonalHomeManagerRequestUtils.ForUserPowerResultInfoListener() {
            @Override
            public void onResponseMessage(List<UserPowerInfo> lists) {
                if(lists != null){
                    layoutManager = new LinearLayoutManager(ActivityMemberAndPower.this);
                    mRecyclerView.setLayoutManager(layoutManager);
                    memberPowerAdapter = new MemberPowerAdapter(ActivityMemberAndPower.this,lists);
                    mRecyclerView.setAdapter(memberPowerAdapter);
                    memberPowerAdapter.setOnItemClickListener(new MemberPowerAdapter.MyItemClickListener() {
                        @Override
                        public void onItemClick(View v, int position, UserPowerInfo info) {
                            Intent intent = new Intent(CommonUtils.getContext(),ActivityMemberInfo.class);
                            intent.putExtra("phone",info.list.getUi_phone());
                            intent.putExtra("nc",info.list.getUi_nc());
                            intent.putExtra("role",info.role);
                            intent.putExtra("remove_ui_id",info.list.getUi_id());
                            intent.putExtra("mg_id",mg_id);
                            intent.putExtra("ub_id",ub_id);
                            startActivity(intent);
                            overridePendingTransition(0,0);
                        }
                    });
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back://退出
                finish();
                break;
            case R.id.iv_base_member_add://添加成员
                Intent intent = new Intent(this,ActivityAddMember.class);
                intent.putExtra("mg_id",mg_id);
                intent.putExtra("ub_id",ub_id);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
                break;
        }
    }

    @Override
    protected void onResume() {
        if(SpTools.getBoolean(CommonUtils.getContext(),Constants.managerStatus,false)){
            internet();
            SpTools.setBoolean(CommonUtils.getContext(), Constants.managerStatus,false);
        }
        super.onResume();
    }
}
