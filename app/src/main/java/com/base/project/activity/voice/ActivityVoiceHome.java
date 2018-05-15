package com.base.project.activity.voice;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.adapter.TextHintAdapter;
import com.base.project.utils.CommonUtils;
import com.carlos.voiceline.mylibrary.VoiceLineView;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.base.project.activity.voice.VoicePlaying.speekText;
import static com.base.project.activity.voice.VoicePlaying.stopSpeak;
import static com.base.project.activity.voice.VoiceRecognition.engineInit;
import static com.base.project.activity.voice.VoiceRecognition.startSpeech;
import static com.base.project.activity.voice.VoiceRecognition.stopSpeech;


public class ActivityVoiceHome extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_base_backto)
    ImageView ivBaseBackto;
    @BindView(R.id.iv_base_back)
    ImageView ivBaseBack;
    @BindView(R.id.tv_base_title)
    TextView tvBaseTitle;
    @BindView(R.id.iv_base_edit)
    ImageView ivBaseEdit;
    @BindView(R.id.isr_text)
    TextView isrText;   //你可以这样说
    @BindView(R.id.img_voice_lineview)
     // 音浪控件
    VoiceLineView imgVoiceLineview;
    @BindView(R.id.img_voice_wildcard) //问号
            ImageView imgVoiceWildcard;
    @BindView(R.id.img_voice_mic)  // 麦克风
            ImageView imgVoiceMic;
    @BindView(R.id.img_voice_setting)  //设置
            ImageView imgVoiceSetting;
    @BindView(R.id.isr_lin)
    RelativeLayout isrLin;    //按钮父布局
    @BindView(R.id.rel_layout_light)
    RelativeLayout relLayoutLight;
    @BindView(R.id.rel_layout_air)
    RelativeLayout relLayoutAir;
    @BindView(R.id.rel_layout_tv)
    RelativeLayout relLayoutTv;
    @BindView(R.id.rel_layout_curtain)
    RelativeLayout relLayoutCurtain;
    @BindView(R.id.rel_layout_sound)
    RelativeLayout relLayoutSound;
    @BindView(R.id.rel_layout_socket)
    RelativeLayout relLayoutSocket;
    @BindView(R.id.rel_layout_stb)
    RelativeLayout relLayoutStb;
    @BindView(R.id.rel_layout_sensor)
    RelativeLayout relLayoutSensor;
    @BindView(R.id.text_layout_facility)
    LinearLayout textLayoutFacility; // 设备展示页面
    @BindView(R.id.tec_text_hint)
    RecyclerView tecTextHint;   //声音页面 动态提示展示
    @BindView(R.id.text_hint_speek)
    TextView textHintSpeek;   //提示请说话
    @BindView(R.id.text_setting_rouse)
    TextView textSettingRouse;  //设置的文字
    @BindView(R.id.img_setting_switch)
    CheckBox imgSettingSwitch;   // 设置的按钮
    @BindView(R.id.rel_setting_layout)
    RelativeLayout relSettingLayout;  // 设置的布局
    @BindView(R.id.text_setting_hint)
    TextView textSettingHint;  //
    private TextHintAdapter textHintAdapter;
    private String textHint;
    //建立一个静态私有变量用于存储上下文实例
    private static ActivityVoiceHome instance;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voice_home);
        ButterKnife.bind(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        engineInit(); //语音识别初始化
        speekText(); //语音合成初始化
        ivBaseBackto.setVisibility(View.GONE);
        ivBaseBack.setVisibility(View.VISIBLE);
        tvBaseTitle.setVisibility(View.GONE);
        ivBaseEdit.setVisibility(View.GONE);
        tecTextHint.setVisibility(View.VISIBLE);
        textHintSpeek.setVisibility(View.GONE);
        relSettingLayout.setVisibility(View.GONE);
        // init();
        this.instance=ActivityVoiceHome.this;
        textHint(9); // 主页面提示信息
        initListener();

    }

    /**
     * 根据select获取对应的资源传入adapter
     *
     * @param select
     */
    private void textHint(int select) {
      //  Log.i("qaz", "textHint: " + getTexthint(select));
        textHintAdapter = new TextHintAdapter(getTexthint(select), this);
        tecTextHint.setLayoutManager(new LinearLayoutManager(this));
        tecTextHint.setAdapter(textHintAdapter);
        textHintAdapter.notifyDataSetChanged();
    }

    /**
     * 获取对应的展示数据
     *
     * @param select
     * @return
     */
    private String[] getTexthint(int select) {
        if (select == 1) {
            textHint = getResources().getString(R.string.text_hint_light);
        } else if (select == 2) {
            textHint = getResources().getString(R.string.text_hint_air);
        } else if (select == 3) {
            textHint = getResources().getString(R.string.text_hint_tv);
        } else if (select == 4) {
            textHint = getResources().getString(R.string.text_hint_curtain);
        } else if (select == 5) {
            textHint = getResources().getString(R.string.text_hint_sound);
        } else if (select == 6) {
            textHint = getResources().getString(R.string.text_hint_socket);
        } else if (select == 7) {
            textHint = getResources().getString(R.string.text_hint_stb);
        } else if (select == 8) {
            textHint = getResources().getString(R.string.text_hint_sensor);
        } else if (select == 9) {
            textHint = getResources().getString(R.string.text_hint_home);
        }
        String[] string = textHint.split(",");

        return string;
    }

    public void initListener() {
        imgVoiceWildcard.setOnClickListener(this);
        imgVoiceMic.setOnClickListener(this);
        imgVoiceSetting.setOnClickListener(this);
        //
        ivBaseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopSpeech(); // 停止语音识别
                stopSpeak(); //停止语音合成
                finish();
            }
        });
        relLayoutLight.setOnClickListener(this);
        relLayoutAir.setOnClickListener(this);
        relLayoutTv.setOnClickListener(this);
        relLayoutCurtain.setOnClickListener(this);
        relLayoutSound.setOnClickListener(this);
        relLayoutSocket.setOnClickListener(this);
        relLayoutStb.setOnClickListener(this);
        relLayoutSensor.setOnClickListener(this);
        imgSettingSwitch.setOnClickListener(this);
        imgSettingSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    CommonUtils.toastMessage("开启摇一摇");
                } else {
                    CommonUtils.toastMessage("关闭摇一摇");
                }
            }
        });
    }

    /**
     * 根据不同页面设置相应的返回键
     *
     * @param setonclick
     */
    private void setOnclick(int setonclick) {
        if (setonclick == 1) {
            ivBaseBackto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textLayoutFacility.setVisibility(View.VISIBLE);
                    tecTextHint.setVisibility(View.GONE);
                    ivBaseBack.setVisibility(View.GONE);
                    setOnclick(0);
                }
            });
        } else {
            ivBaseBackto.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    textHint(9);
                    setWidget(4);
                }
            });
        }

    }

    /**
     * 设置控件显示/不显示
     */
    public void setWidget(int wigdetShow) {
        if (wigdetShow == 1) {  // 点击具体设备
            textSettingHint.setVisibility(View.GONE);
            isrText.setVisibility(View.VISIBLE);
            relSettingLayout.setVisibility(View.GONE);
            tvBaseTitle.setVisibility(View.GONE);
            textHintSpeek.setVisibility(View.GONE);
            ivBaseBackto.setVisibility(View.VISIBLE);
            tecTextHint.setVisibility(View.VISIBLE);
            ivBaseBack.setVisibility(View.GONE);
            textLayoutFacility.setVisibility(View.GONE);
        } else if (wigdetShow == 2) {  // 点击问号
            textSettingHint.setVisibility(View.GONE);
            relSettingLayout.setVisibility(View.GONE);
            tvBaseTitle.setVisibility(View.GONE);
            textHintSpeek.setVisibility(View.GONE);
            ivBaseBackto.setVisibility(View.VISIBLE);
            tecTextHint.setVisibility(View.GONE);
            ivBaseBack.setVisibility(View.GONE);
            textLayoutFacility.setVisibility(View.VISIBLE);
        } else if (wigdetShow == 3) {  //点击设置
            textSettingHint.setVisibility(View.VISIBLE);
            tvBaseTitle.setVisibility(View.VISIBLE);
            isrText.setVisibility(View.GONE);
            tvBaseTitle.setText("设置");
            tecTextHint.setVisibility(View.GONE);
            textLayoutFacility.setVisibility(View.GONE);
            isrLin.setVisibility(View.GONE);
            relSettingLayout.setVisibility(View.VISIBLE);
            ivBaseBack.setVisibility(View.GONE);
            ivBaseBackto.setVisibility(View.VISIBLE);
            textHintSpeek.setVisibility(View.GONE);
        } else if (wigdetShow == 0) {  //点击麦克风

            textSettingHint.setVisibility(View.GONE);
           // isrText.setVisibility(View.VISIBLE);
            relSettingLayout.setVisibility(View.GONE);
            tvBaseTitle.setVisibility(View.GONE);
            //tecTextHint.setVisibility(View.VISIBLE);
            //textLayoutFacility.setVisibility(View.GONE);
            isrLin.setVisibility(View.GONE);
            imgVoiceLineview.setVisibility(View.VISIBLE);
            // ivBaseBack.setVisibility(View.VISIBLE);
            // ivBaseBackto.setVisibility(View.GONE);
            textHintSpeek.setVisibility(View.VISIBLE);
            // 向右边移出
            isrLin.setAnimation(AnimationUtils.makeOutAnimation(this, true));
            // 向右边移入
            imgVoiceLineview.setAnimation(AnimationUtils.makeInAnimation(this, true));
        } else if (wigdetShow == 4) {  //点击返回键
            textHintSpeek.setVisibility(View.GONE);
            isrText.setVisibility(View.VISIBLE);
            tvBaseTitle.setVisibility(View.GONE);
            textLayoutFacility.setVisibility(View.GONE);
            tecTextHint.setVisibility(View.VISIBLE);
            ivBaseBack.setVisibility(View.VISIBLE);
            ivBaseBackto.setVisibility(View.GONE);
            relSettingLayout.setVisibility(View.GONE);
            isrLin.setVisibility(View.VISIBLE);
            textSettingHint.setVisibility(View.GONE);
        }else if(wigdetShow == 6){ //语音识别结束
            goback = true;
            textHintSpeek.setVisibility(View.GONE);
            imgVoiceLineview.setVisibility(View.GONE);
            isrLin.setVisibility(View.VISIBLE);
            // 向左边移入
            isrLin.setAnimation(AnimationUtils.makeInAnimation(this, false));
            // 向左边移出
            imgVoiceLineview.setAnimation(AnimationUtils.makeOutAnimation(this, false));
        }

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_voice_mic:  // 麦克风
                goback = false;
                //textHint(9);
                imgVoiceLineview.run();
                startSpeech();  // 开始语音识别
                setWidget(0);
                break;
            case R.id.img_voice_setting: //设置
                goback = true;
                setOnclick(0);
                setWidget(3);
                break;
            case R.id.img_voice_wildcard: //问号
                goback = true;
                setWidget(2);
                setOnclick(0);
                break;
            case R.id.iv_base_back:
                finish();
                break;
            case R.id.rel_layout_light:  //灯
                goback = true;
                textHint(1);
                setOnclick(1);
                setWidget(1);
                break;
            case R.id.rel_layout_air:  //空调
                goback = true;
                textHint(2);
                setOnclick(1);
                setWidget(1);
                break;
            case R.id.rel_layout_tv:  //电视
                goback = true;
                textHint(3);
                setOnclick(1);
                setWidget(1);
                break;
            case R.id.rel_layout_curtain:  //窗帘
                goback = true;
                textHint(4);
                setOnclick(1);
                setWidget(1);
                break;
            case R.id.rel_layout_sound:  //音响
                goback = true;
                textHint(5);
                setOnclick(1);
                setWidget(1);
                break;
            case R.id.rel_layout_socket:  //插座
                goback = true;
                textHint(6);
                setOnclick(1);
                setWidget(1);
                break;
            case R.id.rel_layout_stb:  //机顶盒
                goback = true;
                textHint(7);
                setOnclick(1);
                setWidget(1);
                break;
            case R.id.rel_layout_sensor:  //传感器
                goback = true;
                textHint(8);
                setOnclick(1);
                setWidget(1);
                break;
        }
    }


    private boolean goback = true; // 判断是否  关闭音浪控件 默认true不关闭

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if (!goback) {
                stopSpeech(); // 停止语音识别
                stopSpeak(); //停止语音合成
                setWidget(6);
            } else {
                stopSpeech(); // 停止语音识别
                stopSpeak(); //停止语音合成
                finish();
            }

            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    //建立一个静态方法，用于返回所需要的上下文实例
    public static ActivityVoiceHome getInstance() {
        return instance;
    }

    private static String TAG = "qaz";

}
