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

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityCreateHouse extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_add)
    ImageView add;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.tv_create_house_confirm)
    TextView tv_house_name;
    @BindView(R.id.et_create_house_name)
    EditText et_name;
    @BindView(R.id.rl_create_house_confirm)
    RelativeLayout rl_confirm;
    private int resultCode = 11;
    private int resultCodeFlour = 12;
    private int requsetCode = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_house);
        ButterKnife.bind(this);
        title.setText("创建房间");
        add.setVisibility(View.INVISIBLE);
        requsetCode = getIntent().getIntExtra("code",0);
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
                    tv_house_name.setTextColor(getResources().getColor(R.color.colorText));
                }else {
                    tv_house_name.setTextColor(getResources().getColor(R.color.colorWhite));
                }
            }
        });
    }

    private void initdata() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back://退出
                Intent intent = new Intent();
                setResult(15,intent);
                finish();
                break;
            case R.id.rl_create_house_confirm://退出
                if(TextUtils.isEmpty(et_name.getText().toString())){
                    return;
                }else {
                    finishAll();
                }
                break;
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        setResult(15,intent);
        finish();
    }

    private void finishAll() {
        Intent intent = new Intent();
        intent.putExtra("houseName",et_name.getText().toString());
        if(requsetCode == 10){//只有一层的时候
            setResult(resultCode,intent);
        }else if(requsetCode == 9){//多层的时候
            setResult(resultCodeFlour,intent);
        }
        finish();
    }
}
