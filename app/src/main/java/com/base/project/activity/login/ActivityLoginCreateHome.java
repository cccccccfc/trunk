package com.base.project.activity.login;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.activity.MainActivity;
import com.base.project.activity.myself.ActivityCreateHome;
import com.base.project.activity.myself.ActivityJoinFamliy;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.Constants;
import com.base.project.utils.SpTools;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityLoginCreateHome extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.rl_create_home_create)
    RelativeLayout rl_create;
    @BindView(R.id.rl_create_home_join)
    RelativeLayout rl_join;
    @BindView(R.id.tv_create_home_quit)
    TextView tv_quit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_create_home);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        initdata();
        initListener();
    }

    private void initListener() {
        rl_create.setOnClickListener(this);
        rl_join.setOnClickListener(this);
        tv_quit.setOnClickListener(this);
    }

    private void initdata() {

    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.rl_create_home_create://创建新家庭
                 intent = new Intent(this, ActivityCreateHome.class);
                intent.putExtra("ub_id",SpTools.getString(this,Constants.userId,"0"));
                intent.putExtra("type",5);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
                break;
            case R.id.rl_create_home_join://加入家庭
                intent = new Intent(this, ActivityJoinFamliy.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                break;
            case R.id.tv_create_home_quit://退出登录
                SpTools.setString(this, Constants.userId,"0");
                CommonUtils.deleteBitmap("myicon.jpg");//删除本地图片
                intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
                finish();
                break;
        }
    }
}
