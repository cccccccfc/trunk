package com.base.project.activity.myself;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.project.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityBaseCopy extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_add)
    ImageView add;
    @BindView(R.id.tv_base_title)
    TextView title;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself_info);
        ButterKnife.bind(this);

        title.setText("XXXX");
        add.setVisibility(View.INVISIBLE);

        initdata();
        initListener();
    }

    private void initListener() {
        back.setOnClickListener(this);
    }

    private void initdata() {

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back://退出
                finish();
                break;
        }
    }
}
