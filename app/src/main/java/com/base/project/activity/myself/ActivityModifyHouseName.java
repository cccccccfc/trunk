package com.base.project.activity.myself;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.utils.CommonUtils;
import com.base.utilslibrary.internet.PersonalHomeManagerRequestUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityModifyHouseName extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_add)
    ImageView add;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.et_modify_house_name)
    EditText et_name;
    @BindView(R.id.rl_modify_house_confirm)
    RelativeLayout rl_confirm;

    private String mg_id;
    private String ub_id;
    private int pos;
    private int list_id;
    private String houseName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_house_name);
        ButterKnife.bind(this);
        title.setText("修改房间名称");
        add.setVisibility(View.INVISIBLE);
        houseName = getIntent().getStringExtra("name");
        et_name.setText(houseName);
        initdata();
        initListener();
    }

    private void initListener() {
        back.setOnClickListener(this);
        rl_confirm.setOnClickListener(this);
    }

    private void initdata() {
        mg_id = getIntent().getStringExtra("mg_id");
        ub_id = getIntent().getStringExtra("ub_id");
        pos = getIntent().getIntExtra("pos",0);
        list_id = getIntent().getIntExtra("list_id",0);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back://退出
                finish();
                break;
            case R.id.rl_modify_house_confirm://退出
                if(TextUtils.isEmpty(et_name.getText().toString())){
                    CommonUtils.toastMessage("房间名称不能为空");
                    return;
                }
                if(houseName.equals(et_name.getText().toString())){
                    CommonUtils.toastMessage("房间名称没有修改");
                    return;
                }
                PersonalHomeManagerRequestUtils.modifygroupName(this,ub_id, mg_id, et_name.getText().toString(), new PersonalHomeManagerRequestUtils.ForResultListener() {
                    @Override
                    public void onResponseMessage(String code) {
                        if("成功".equals(code)){
                            nameListener.modifyName(et_name.getText().toString(),pos,list_id);
                            finish();
                        }else {
                            CommonUtils.toastMessage("修改房间名称失败，请重试");
                        }
                    }
                });
                break;
        }
    }

    public interface ModifyNameListener {
        void modifyName(String name,int position,int list_id);
    }
    public static ModifyNameListener nameListener;
    public static void setModifyNameListener(ModifyNameListener listener){
        nameListener = listener;
    }
}
