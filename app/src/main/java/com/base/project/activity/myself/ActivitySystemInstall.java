package com.base.project.activity.myself;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.activity.MainActivity;
import com.base.project.activity.MainActivityTabHost;
import com.base.project.activity.login.ActivityChangePas;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.Constants;
import com.base.project.utils.SpTools;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivitySystemInstall extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_add)
    ImageView add;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.ll_system_quit)
    LinearLayout ll_quit;
    @BindView(R.id.ll_system_change_password)
    LinearLayout ll_pas;
    @BindView(R.id.tv_system_phone)
    TextView tv_phone;
    @BindView(R.id.tv_system_version)
    TextView tv_version;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_system_install);
        ButterKnife.bind(this);
        title.setText("系统设置");
        add.setVisibility(View.INVISIBLE);
        initdata();
        initListener();
    }

    private void initListener() {
        back.setOnClickListener(this);
        ll_quit.setOnClickListener(this);
        ll_pas.setOnClickListener(this);
    }

    private void initdata() {

        tv_phone.setText(omitPhone(getIntent().getStringExtra("phone")));
        tv_version.setText("v"+CommonUtils.getVersionName(this));
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.iv_base_back://返回
                finish();
                break;
            case R.id.ll_system_quit://退出登录，需要把主页也退出，重新出现登录界面
                SpTools.setString(this, Constants.userId,"0");
                SpTools.setString(this, Constants.information, "");
                SpTools.setString(this, Constants.home_name, "");
                SpTools.setString(this, Constants.home_role, "");
                SpTools.setInt(this,Constants.home_position,0);
                SpTools.setBoolean(this, Constants.createhome,false);
                CommonUtils.deleteBitmap("myicon.jpg");//删除本地图片
                MainActivityTabHost.getInstance().finishActivity();
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
                break;
            case R.id.ll_system_change_password://修改密码
                intent = new Intent(this, ActivityChangePas.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
        }
    }

    private String omitPhone(String pNumber){
        if(!TextUtils.isEmpty(pNumber) && pNumber.length() > 6 ){
            StringBuilder sb  =new StringBuilder();
            for (int i = 0; i < pNumber.length(); i++) {
                char c = pNumber.charAt(i);
                if (i >= 3 && i <= 6) {
                    sb.append('*');
                } else {
                    sb.append(c);
                }
            }

            return sb.toString();
        }
        return null;
    }

}
