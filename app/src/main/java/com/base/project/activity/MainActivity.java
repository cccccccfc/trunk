package com.base.project.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.activity.login.ActivityLogin;
import com.base.project.activity.login.ActivityLoginCreateHome;
import com.base.project.utils.Constants;
import com.base.project.utils.SpTools;
import com.base.utilslibrary.bean.FragmentHomeShowBean;
import com.base.utilslibrary.internet.PersonalHomeManagerRequestUtils;

import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {


    @BindView(R.id.tv_welcome_page)
    TextView tv_welcom_page;
    int i = 0;
    private Timer mTimer;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        tv_welcom_page.setText("");

        mTimer = new Timer();//计时器 3秒后关闭
        mTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(i >= 3){
                    toFinish();
                }
                i++;
            }
        },0,1000);
        FragmentHomeShowBean fragmenthomeShowBean = new FragmentHomeShowBean();
        fragmenthomeShowBean.setChangestate(true);
        //Log.i("qaz", "判断依据:111111 " + fragmenthomeShowBean.changestate);
    }

    private void toFinish() {
        if(mTimer !=null){
            mTimer.cancel();
            mTimer = null;
        }
        if("0".equals(SpTools.getString(this, Constants.userId,"0"))){
            Intent intent = new Intent(this, ActivityLogin.class);
            startActivity(intent);
            overridePendingTransition(0,0);
            finish();
        }else {
            if(SpTools.getBoolean(this,Constants.createhome,false)){
                Intent intent = new Intent(this, MainActivityTabHost.class);
                startActivity(intent);
            }else {
                PersonalHomeManagerRequestUtils.getHomeNumberInfo(SpTools.getString(this, Constants.userId,"0"), new PersonalHomeManagerRequestUtils.ForResultListener() {
                    @Override
                    public void onResponseMessage(String code) {
                        if(!TextUtils.isEmpty(code)){
                            if(Integer.parseInt(code) > 0){
                                Intent intent = new Intent(MainActivity.this, MainActivityTabHost.class);
                                startActivity(intent);
                            }else {
                                Intent intent = new Intent(MainActivity.this, ActivityLoginCreateHome.class);
                                startActivity(intent);
                            }
                        }else {
                            Intent intent = new Intent(MainActivity.this, ActivityLoginCreateHome.class);
                            startActivity(intent);
                        }
                    }
                });
            }
            overridePendingTransition(0,0);

            finish();
        }
    }

}
