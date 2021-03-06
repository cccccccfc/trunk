package com.base.project.activity.login;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.utils.CommonUtils;
import com.base.utilslibrary.internet.PersonalInternetRequestUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityChangePas extends FragmentActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.et_forget_phone)
    EditText phone;
    @BindView(R.id.et_forget_passport)
    EditText passport;
    @BindView(R.id.et_forget_next_passport)
    EditText nextPassport;
    @BindView(R.id.et_forget_yzm)
    EditText yzm;
    @BindView(R.id.rl_forget_get)
    RelativeLayout getYzm;
    @BindView(R.id.rl_forget_success)
    RelativeLayout success;
    @BindView(R.id.tv_forget_yzm)
    TextView mView;


    private String phoneNumber;
    private String mCode;
    private String mPassword;
    private String nextPassword;
    private String mVc_code ="";//验证码

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change);
        ButterKnife.bind(this);
        title.setText("修改密码");
        edit.setVisibility(View.INVISIBLE);
        initdata();
        initListener();
    }

    private void initdata() {

    }
    private void initListener() {
        back.setOnClickListener(this);
        getYzm.setOnClickListener(this);
        success.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back:
                finishAll();
                finish();
                break;
            case R.id.rl_forget_get://获取验证码
                getCode();
                break;
            case R.id.rl_forget_success://完成修改密码
                successForget();
                break;
        }
    }

    private void finishAll() {
    }

    public void getCode() {
        phoneNumber = phone.getText().toString();
        PersonalInternetRequestUtils.verificationCode(this,phoneNumber, mView,
                new PersonalInternetRequestUtils.ForResultListener() {
            @Override
            public void onResponseMessage(String code) {
                mVc_code = code;
            }
        });
    }
    private void successForget() {
        phoneNumber = phone.getText().toString();
        mPassword = passport.getText().toString();
        nextPassword = nextPassport.getText().toString();
        mCode = yzm.getText().toString();
        PersonalInternetRequestUtils.forgetPassword(this,phoneNumber, mVc_code, mCode, mPassword, nextPassword,
                mView, passport,nextPassport,new PersonalInternetRequestUtils.ForResultListener() {
            @Override
            public void onResponseMessage(String code) {
                if(code.equals("成功")){
                    CommonUtils.toastMessage("修改密码成功");
                    finish();
                }
            }
        });
    }

}
