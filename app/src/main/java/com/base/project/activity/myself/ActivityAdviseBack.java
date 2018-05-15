package com.base.project.activity.myself;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.project.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityAdviseBack extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_add)
    ImageView add;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.et_advise_back_content)
    EditText et_content;
    @BindView(R.id.et_advise_back_phone)
    EditText et_phone;
    @BindView(R.id.rl_advise_back_confirm)
    RelativeLayout rl_confirm;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advise_back);
        ButterKnife.bind(this);
        title.setText("建议反馈");
        add.setVisibility(View.INVISIBLE);
        initdata();
        initListener();
    }

    private void initListener() {
        back.setOnClickListener(this);
        rl_confirm.setOnClickListener(this);
    }

    private void initdata() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back://退出
                finish();
                break;
            case R.id.rl_advise_back_confirm://提交
                break;
        }
    }
}
