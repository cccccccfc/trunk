package com.base.project.activity.myself;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.Constants;
import com.base.project.utils.SpTools;
import com.base.utilslibrary.internet.PersonalHomeManagerRequestUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityMemberInfo extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_add)
    ImageView add;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.tv_member_phone)
    TextView tv_phone;
    @BindView(R.id.tv_member_nc)
    TextView tv_nc;
    @BindView(R.id.tv_member_power)
    TextView tv_power;
    @BindView(R.id.ll_member_info)
    LinearLayout ll_info;
    @BindView(R.id.rl_member_info)
    RelativeLayout rl_member;

    @BindView(R.id.rl_member_request)
    RelativeLayout rl_request;//设置管理员权限
    @BindView(R.id.rl_member_delete)
    RelativeLayout rl_delete;//删除成员

    private String mg_id;
    private String ub_id;
    private String remove_ui_id;
    private String role;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_member_info);
        ButterKnife.bind(this);

        title.setText("成员设置");
        add.setVisibility(View.INVISIBLE);

        initdata();
        initListener();
    }

    private void initListener() {
        back.setOnClickListener(this);
        rl_member.setOnClickListener(this);
        rl_request.setOnClickListener(this);
        rl_delete.setOnClickListener(this);
    }

    private void initdata() {
        mg_id = getIntent().getStringExtra("mg_id");
        ub_id = getIntent().getStringExtra("ub_id");
        remove_ui_id = getIntent().getStringExtra("remove_ui_id");
        String phone = getIntent().getStringExtra("phone");
        role = getIntent().getStringExtra("role");
        String nc = getIntent().getStringExtra("nc");
        tv_phone.setText(phone);
        tv_nc.setText(nc);
        if("1".equals(role)){
            tv_power.setText("删除管理员");
            ll_info.setVisibility(View.INVISIBLE);
        }else {
            tv_power.setText("设为管理员");
            ll_info.setVisibility(View.VISIBLE);
        }
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back://退出
                finish();
                break;
            case R.id.rl_member_request:
                if("删除管理员".equals(tv_power.getText().toString())){
                    managerStatus("1");
                }else {
                    managerStatus("2");
                }
                break;
            case R.id.rl_member_delete:
                PersonalHomeManagerRequestUtils.deleteMember(ub_id, mg_id, remove_ui_id, role, new PersonalHomeManagerRequestUtils.ForResultListener() {
                    @Override
                    public void onResponseMessage(String code) {
                        if("成功".equals(code)){
                            CommonUtils.toastMessage("修改成功");
                            SpTools.setBoolean(CommonUtils.getContext(), Constants.managerStatus,true);
                            finish();
                        }else {
                            CommonUtils.toastMessage("删除失败，请重试");
                        }
                    }
                });
                break;
        }
    }

    private void managerStatus(final String str) {
        PersonalHomeManagerRequestUtils.addManagerStatus(ub_id, mg_id, remove_ui_id, str, new PersonalHomeManagerRequestUtils.ForResultListener() {
            @Override
            public void onResponseMessage(String code) {
                if("成功".equals(code)){
                    CommonUtils.toastMessage("修改成功");
                    SpTools.setBoolean(CommonUtils.getContext(), Constants.managerStatus,true);
                    finish();
                }else {
                    CommonUtils.toastMessage("更改失败，请重试");
                }
            }
        });
    }
}
