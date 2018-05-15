package com.base.project.activity.myself;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.activity.MainActivityTabHost;
import com.base.project.activity.login.ActivityLoginCreateHome;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.Constants;
import com.base.project.utils.SpTools;
import com.base.utilslibrary.bean.GroupInfoBean;
import com.base.utilslibrary.internet.PersonalHomeManagerRequestUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityCreateHome extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_add)
    ImageView add;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.et_create_home_name)
    EditText et_name;
    @BindView(R.id.rl_create_home_confirm)
    RelativeLayout rl_confirm;
    @BindView(R.id.tv_create_home_confirm)
    TextView tv_confirm;

    private String mg_id;
    private String ub_id;
    private int type;
    private String houseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_home);
        ButterKnife.bind(this);
        type = getIntent().getIntExtra("type", 0);//type=10为修改名称，其他是创建新家庭
        if(type == 10){
            title.setText("修改家庭名称");
            tv_confirm.setText("确认");
            houseName = getIntent().getStringExtra("name");
            et_name.setText(houseName);
        }else {
            title.setText("创建家庭");
            tv_confirm.setText("创建");
        }

        add.setVisibility(View.INVISIBLE);
        initdata();
        initListener();
    }

    private void initListener() {
        back.setOnClickListener(this);
        rl_confirm.setOnClickListener(this);
        et_name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(s.toString())){
                    tv_confirm.setTextColor(getResources().getColor(R.color.colorText));
                }else {
                    tv_confirm.setTextColor(getResources().getColor(R.color.colorWhite));
                }
            }
        });
    }

    private void initdata() {
        mg_id = getIntent().getStringExtra("mg_id");
        ub_id = getIntent().getStringExtra("ub_id");
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back://退出
                finishAll();
                break;
            case R.id.rl_create_home_confirm://退出
                if(TextUtils.isEmpty(et_name.getText().toString())){
                    CommonUtils.toastMessage("家庭名称不能为空");
                    return;
                }
                if(type == 10){
                    if(houseName.equals(et_name.getText().toString())){
                        CommonUtils.toastMessage("房间名称没有修改");
                        return;
                    }
                    PersonalHomeManagerRequestUtils.modifygroupName(this,ub_id, mg_id, et_name.getText().toString(), new PersonalHomeManagerRequestUtils.ForResultListener() {
                        @Override
                        public void onResponseMessage(String code) {
                            if("成功".equals(code)){
                                //新建家庭或退出家庭,通知主页更新信息，暂时先用sp通知
                                SpTools.setBoolean(CommonUtils.getContext(), Constants.housechange,true);
                                SpTools.setString(CommonUtils.getContext(),Constants.home_name,et_name.getText().toString());
                                Intent intent = new Intent();
                                intent.putExtra("name",et_name.getText().toString());
                                setResult(21,intent);
                                finish();
                            }else {
                                CommonUtils.toastMessage("修改家庭名称失败，请重试");
                            }

                        }
                    });
                }else {
                    PersonalHomeManagerRequestUtils.addHouse(this,ub_id, "0", et_name.getText().toString(), new PersonalHomeManagerRequestUtils.ForResultDataListener() {
                        @Override
                        public void onResponseMessage(GroupInfoBean info) {
                            if(info != null){
                                //新建家庭或退出家庭,通知主页更新信息，暂时先用sp通知
                                SpTools.setBoolean(CommonUtils.getContext(), Constants.housechange,true);
                                createSuccess();
                            }else {
                                CommonUtils.toastMessage("创建家庭名称失败，请重试");
                            }
                        }
                    });
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        finishAll();
    }

    private void finishAll() {
        if(type == 5){//新用户创建了家庭，跳转到首页
            Intent intent = new Intent(this, ActivityLoginCreateHome.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }else {
            Intent intent = new Intent();
            intent.putExtra("name","");
            setResult(20,intent);
        }
        finish();
    }
    private void createSuccess() {
        if(type == 5){//新用户创建了家庭，跳转到首页
            SpTools.setBoolean(this, Constants.createhome,true);
            Intent intent = new Intent(this, MainActivityTabHost.class);
            startActivity(intent);
            overridePendingTransition(0,0);
        }else {
            Intent intent = new Intent();
            intent.putExtra("name","");
            setResult(20,intent);
        }
        finish();
    }
}
