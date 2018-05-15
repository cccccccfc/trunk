package com.base.project.activity.facility;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.adapter.CameraPagerAdapter;
import com.base.project.fragment.AlarmimagesFragment;
import com.base.project.fragment.CameraInfoFragment;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityCameraInfo extends AppCompatActivity implements View.OnClickListener {
    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_add)
    ImageView add;
    @BindView(R.id.tv_base_title)
    TextView title;
    @BindView(R.id.camera)
    TextView camera;
    @BindView(R.id.image)
    TextView image;
    @BindView(R.id.viewpager_camera)
    ViewPager mViewPager;

    private ArrayList fragments;
    private int db_sbID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera_info);
        ButterKnife.bind(this);

        /*
        * 去屏保
        * */
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON, WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        title.setText("摄像头信息");
        add.setVisibility(View.INVISIBLE);
        db_sbID = getIntent().getIntExtra("db_sbID", 0);
        initdata();
        initListener();
    }

    private void initListener() {
        camera.setOnClickListener(this);
        image.setOnClickListener(this);
        back.setOnClickListener(this);
    }

    private void initdata() {

        fragments = new ArrayList<Fragment>();
        Fragment fragmentNews = new CameraInfoFragment();
        Fragment fragmentOther = new AlarmimagesFragment();
        fragments.add(fragmentNews);
        fragments.add(fragmentOther);
        mViewPager.setAdapter(new CameraPagerAdapter(getSupportFragmentManager(), fragments));
        mViewPager.setCurrentItem(0);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_base_back://退出
                finish();
                break;
            case R.id.camera://摄像头
                mViewPager.setCurrentItem(0);
                break;
            case R.id.image://图片
                mViewPager.setCurrentItem(1);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
    public int getDb_sbID(){
        return db_sbID;
    }
}
