package com.base.project.activity.myself;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.project.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityAddImage extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_base_back)
    ImageView ivBaseBack;
    @BindView(R.id.tv_base_title)
    TextView tvBaseTitle;
    @BindView(R.id.iv_base_add)
    ImageView ivBaseAdd;
    @BindView(R.id.lin_pattern_goout)
    LinearLayout linPatternGoout;
    @BindView(R.id.lin_pattern_home)
    LinearLayout linPatternHome;
    @BindView(R.id.lin_pattern_getup)
    LinearLayout linPatternGetup;
    @BindView(R.id.lin_pattern_sleep)
    LinearLayout linPatternSleep;
    @BindView(R.id.lin_pattern_relx)
    LinearLayout linPatternRelx;
    @BindView(R.id.lin_pattern_recrr)
    LinearLayout linPatternRecrr;
    @BindView(R.id.lin_pattern_repast)
    LinearLayout linPatternRepast;
    @BindView(R.id.lin_pattern_openlight)
    LinearLayout linPatternOpenlight;
    @BindView(R.id.lin_pattern_closelight)
    LinearLayout linPatternCloselight;
    @BindView(R.id.lin_pattern_video)
    LinearLayout linPatternVideo;
    private Intent intent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_image);
        ButterKnife.bind(this);

        tvBaseTitle.setText("选择情景图标");
        ivBaseBack.setVisibility(View.VISIBLE);
        ivBaseAdd.setVisibility(View.GONE);

        initListener();
    }

    private void initListener() {
        ivBaseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        intent = new Intent();
        linPatternVideo.setOnClickListener(this);
        linPatternCloselight.setOnClickListener(this);
        linPatternOpenlight.setOnClickListener(this);
        linPatternRepast.setOnClickListener(this);
        linPatternRecrr.setOnClickListener(this);
        linPatternRelx.setOnClickListener(this);
        linPatternSleep.setOnClickListener(this);
        linPatternGetup.setOnClickListener(this);
        linPatternHome.setOnClickListener(this);
        linPatternGoout.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lin_pattern_video:  // 影音
                intent.putExtra("type","1");
                Log.i("wsx", "点击选择图标");
                setResult(2,intent);
                finish();
                break;
            case R.id.lin_pattern_openlight: // 开灯
                intent.putExtra("type","2");
                setResult(2,intent);
                finish();
                break;
            case R.id.lin_pattern_repast:  //就餐
                intent.putExtra("type","3");
                setResult(2,intent);
                finish();
                break;
            case R.id.lin_pattern_recrr:  // 娱乐
                intent.putExtra("type","4");
                setResult(2,intent);
                finish();
                break;
            case R.id.lin_pattern_relx:  // 休闲
                intent.putExtra("type","5");
                setResult(2,intent);
                finish();
                break;
            case R.id.lin_pattern_sleep:  // 睡眠
                intent.putExtra("type","6");
                setResult(2,intent);
                finish();
                break;
            case R.id.lin_pattern_getup:  // 起床
                intent.putExtra("type","7");
                setResult(2,intent);
                finish();
                break;
            case R.id.lin_pattern_home:  // 在家
                intent.putExtra("type","8");
                setResult(2,intent);
                finish();
                break;
            case R.id.lin_pattern_goout:  // 外出
                intent.putExtra("type","9");
                setResult(2,intent);
                finish();
                break;
            case R.id.lin_pattern_closelight:  // 关灯
                intent.putExtra("type","10");
                setResult(2,intent);
                finish();
                break;
        }
    }
}
