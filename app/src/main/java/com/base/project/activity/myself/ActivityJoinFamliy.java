package com.base.project.activity.myself;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.Constants;
import com.base.project.utils.SpTools;
import com.base.project.view.adapter.JoinFamliyAdapter;
import com.base.utilslibrary.bean.GroupInfoBean;
import com.base.utilslibrary.internet.PersonalHomeManagerRequestUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityJoinFamliy extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_add)
    ImageView add;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.et_join_famliy)
    EditText et_join;
    @BindView(R.id.iv_join_famliy)
    ImageView iv_delete;
    @BindView(R.id.tv_join_famliy)
    TextView tv_research;
    @BindView(R.id.rv_join_famliy)
    RecyclerView mRecyclerView;
    LinearLayoutManager layoutManager;
    JoinFamliyAdapter famliyAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_join_famliy);
        ButterKnife.bind(this);

        title.setText("加入已有家庭");
        add.setVisibility(View.INVISIBLE);

        initdata();
        initListener();
    }

    private void initListener() {
        back.setOnClickListener(this);
        tv_research.setOnClickListener(this);
        iv_delete.setOnClickListener(this);
    }

    private void initdata() {
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back://退出
                finish();
                break;
            case R.id.iv_join_famliy://
                et_join.setText("");
                break;
            case R.id.tv_join_famliy://搜索
                CommonUtils.logMes("---22--"+SpTools.getString(this, Constants.userId, "0"));
                PersonalHomeManagerRequestUtils.getJoinFamliyInfo(this, SpTools.getString(this, Constants.userId, "0")
                        , et_join.getText().toString(), new PersonalHomeManagerRequestUtils.ForResultJoinFamliyListener() {
                            @Override
                            public void onResponseMessage(List<GroupInfoBean> lists) {
                                if(lists != null){
                                    layoutManager = new LinearLayoutManager(ActivityJoinFamliy.this);
                                    mRecyclerView.setLayoutManager(layoutManager);
                                    famliyAdapter = new JoinFamliyAdapter(ActivityJoinFamliy.this,lists);
                                    mRecyclerView.setAdapter(famliyAdapter);
                                    famliyAdapter.setOnItemClickListener(new JoinFamliyAdapter.MyItemClickListener() {
                                        @Override
                                        public void onItemClick(View v, int position, GroupInfoBean info) {
                                            PersonalHomeManagerRequestUtils.applyToNewHomeInfo(ActivityJoinFamliy.this
                                                    , SpTools.getString(ActivityJoinFamliy.this, Constants.userId, "0"), info.mg_id, info.ui_phone
                                                    , new PersonalHomeManagerRequestUtils.ForResultListener() {
                                                        @Override
                                                        public void onResponseMessage(String code) {
                                                            if(!TextUtils.isEmpty(code)){
                                                                CommonUtils.toastMessage(code);
                                                            }else {
                                                                CommonUtils.toastMessage("申请失败，请重试");
                                                            }
                                                        }
                                                    });
                                        }
                                    });
                                    et_join.setText("");
                                }
                            }
                        });
                break;
        }
    }
}
