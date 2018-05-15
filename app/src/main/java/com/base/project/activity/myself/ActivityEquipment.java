package com.base.project.activity.myself;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.project.R;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityEquipment extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_add)
    ImageView add;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.ll_equipment_open)
    LinearLayout ll_open;
    @BindView(R.id.iv_equipment_open_door)
    ImageView iv_open_door;
    @BindView(R.id.iv_equipment_open_open)
    ImageView iv_open_open;

    @BindView(R.id.ll_equipment_close)
    LinearLayout ll_close;
    @BindView(R.id.iv_equipment_close_door)
    ImageView iv_close_door;
    @BindView(R.id.iv_equipment_close_open)
    ImageView iv_close_open;

    private boolean openIsopen = true;
    private boolean closeIsopen = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_equipment);
        ButterKnife.bind(this);

        title.setText("设备联动");
        add.setVisibility(View.INVISIBLE);

        initdata();
        initListener();
    }

    private void initListener() {
        back.setOnClickListener(this);
        ll_close.setOnClickListener(this);
        ll_open.setOnClickListener(this);
    }

    private void initdata() {
        openControl();
        closeControl();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back://退出
                finish();
                break;
            case R.id.ll_equipment_open:
                openControl();
                openIsopen = !openIsopen;
                break;
            case R.id.ll_equipment_close:
                closeControl();
                closeIsopen = !closeIsopen;
                break;
        }
    }

    private void closeControl() {
        if(closeIsopen){
            iv_close_door.setImageResource(R.drawable.img_light_door);
            iv_close_open.setImageResource(R.drawable.img_equipment_open);
            ll_close.setBackgroundResource(R.drawable.img_light_rectangle);
        }else {
            iv_close_door.setImageResource(R.drawable.img_nolight_door);
            iv_close_open.setImageResource(R.drawable.img_equipment_close);
            ll_close.setBackgroundResource(R.drawable.img_nolight_rectangle);
        }
    }

    private void openControl() {
        if (openIsopen) {
            iv_open_door.setImageResource(R.drawable.img_light_door);
            iv_open_open.setImageResource(R.drawable.img_equipment_open);
            ll_open.setBackgroundResource(R.drawable.img_light_rectangle);
        } else {
            iv_open_door.setImageResource(R.drawable.img_nolight_door);
            iv_open_open.setImageResource(R.drawable.img_equipment_close);
            ll_open.setBackgroundResource(R.drawable.img_nolight_rectangle);
        }
    }
}
