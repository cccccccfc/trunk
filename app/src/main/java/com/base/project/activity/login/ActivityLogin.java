package com.base.project.activity.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.activity.MainActivityTabHost;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.Constants;
import com.base.project.utils.SpTools;
import com.base.utilslibrary.bean.DataInfo;
import com.base.utilslibrary.internet.PersonalInternetRequestUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityLogin extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.et_login_phone)//登录手机号
    EditText phone;
    @BindView(R.id.et_login_passport)//登录密码
    EditText passport;
    @BindView(R.id.rl_login_eye)//密码明文
    RelativeLayout eye;
    @BindView(R.id.rl_login_login)//登录
    RelativeLayout login;
    @BindView(R.id.tv_login_register)//新用户注册
    TextView register;
    @BindView(R.id.tv_login_forget)//忘记密码
    TextView forget;
    @BindView(R.id.iv_login_qq)//qq登陆
    ImageView qq;
    @BindView(R.id.iv_login_wx)//微信登录
    ImageView wx;
    @BindView(R.id.iv_login_sina)//sina登录
    ImageView sina;
    @BindView(R.id.tv_login_logo)//sina登录
    TextView logo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        title.setText("登录");
        edit.setVisibility(View.INVISIBLE);
        logo.getPaint().setFakeBoldText(true);//加粗
        initdata();
        initListener();
    }

    private void initdata() {
    }
    private void initListener() {
        back.setOnClickListener(this);
        eye.setOnClickListener(this);
        login.setOnClickListener(this);
        register.setOnClickListener(this);
        forget.setOnClickListener(this);
        qq.setOnClickListener(this);
        wx.setOnClickListener(this);
        sina.setOnClickListener(this);
    }
    private boolean isEye = false;
    Intent intent;
    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.iv_base_back:
                finish();
                break;
            case R.id.rl_login_eye:
                if(!isEye){
                    isEye = !isEye;
                    passport.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                } else {
                    isEye = !isEye;
                    passport.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
                passport.setSelection(passport.getText().length());
                break;
            case R.id.rl_login_login://登录
                logIn();
                break;
            case R.id.tv_login_register://跳转注册
                intent = new Intent(this,ActivityRegister.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            case R.id.tv_login_forget://跳转忘记密码
                intent = new Intent(this,ActivityForget.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
        }
        intent = null;
    }

    private void logIn() {
        PersonalInternetRequestUtils.login(this, phone.getText().toString(), passport.getText().toString(), new PersonalInternetRequestUtils.ForResultDataListener() {
            @Override
            public void onResponseMessage(DataInfo info) {
                if(info != null){
                    SpTools.setString(CommonUtils.getContext(), Constants.userId,info.ui_id);
                    if(Integer.parseInt(info.sum) > 0){
                        //跳转到个人主页
                        SpTools.setBoolean(ActivityLogin.this, Constants.createhome,true);
                        Intent intent = new Intent(ActivityLogin.this, MainActivityTabHost.class);
                        startActivity(intent);
                    }else {
                        //数量为0，需要创建家庭或者加入家庭
                        Intent intent = new Intent(ActivityLogin.this, ActivityLoginCreateHome.class);
                        startActivity(intent);
                    }
                    overridePendingTransition(0,0);
                    finish();
                }else {
                    CommonUtils.toastMessage("数据加载失败，请重新登录");
                }
            }
        });
    }

    //activity回调  第三方登录的回调
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }
}
