package com.base.project.activity.myself;

import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.utils.CommonUtils;
import com.base.utilslibrary.internet.PersonalHomeManagerRequestUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityAddMember extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_add)
    ImageView add;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.et_add_member_phone)
    EditText et_phone;
    @BindView(R.id.tv_add_member_type)
    TextView tv_type;
    @BindView(R.id.ll_add_member_type)
    LinearLayout ll_type;

    @BindView(R.id.ll_add_member_power)
    LinearLayout ll_power;
    @BindView(R.id.rl_add_member_power)
    RelativeLayout rl_power;
    @BindView(R.id.rl_add_member_request)
    RelativeLayout rl_request;

    private String mg_id;
    private String ub_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_member);
        ButterKnife.bind(this);

        title.setText("添加成员");
        add.setVisibility(View.INVISIBLE);

        initdata();
        initListener();
    }

    private void initListener() {
        back.setOnClickListener(this);
        ll_type.setOnClickListener(this);
        rl_request.setOnClickListener(this);
    }

    private void initdata() {
        mg_id = getIntent().getStringExtra("mg_id");
        ub_id = getIntent().getStringExtra("ub_id");
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back://退出
                finish();
                break;
            case R.id.rl_add_member_request://邀请
                int type = 1;
                if("管理员".equals(tv_type.getText().toString())){
                    type = 1;
                }else {
                    type = 2;
                }
                PersonalHomeManagerRequestUtils.inviteMember(this, ub_id, mg_id, et_phone.getText().toString()
                        , type + "", "1", new PersonalHomeManagerRequestUtils.ForResultListener() {
                            @Override
                            public void onResponseMessage(String code) {
                                if("成功".equals(code)){
                                    inviteDialog();
                                }else {
                                    CommonUtils.toastMessage(code);
                                }
                            }
                        });
                break;
            case R.id.ll_add_member_type://
                if(mPopWindow !=null && mPopWindow.isShowing()){
                    mPopWindow.dismiss();
                }else {
                    showPopupWindow();
                }
                break;
            case R.id.tv_add_member_pop_1:
                mPopWindow.dismiss();
                tv_type.setText(type1.getText().toString());
                break;
            case R.id.tv_add_member_pop_2:
                mPopWindow.dismiss();
                tv_type.setText(type2.getText().toString());
                break;
        }
    }

    private void inviteDialog() {
        final AlertDialog mDialog;
        AlertDialog.Builder builder = new AlertDialog.Builder(this); //先得到构造器
        mDialog = builder.create();
        mDialog.show();
        View view = View.inflate(this, R.layout.activity_dialog_invite_success, null);
        mDialog.getWindow().setContentView(view);
        mDialog.setCanceledOnTouchOutside(false);
        TextView tv_text = (TextView) view.findViewById(R.id.tv_invite_success);
        tv_text.setText("你的家庭邀请已发送给"+et_phone.getText().toString()+"，请等待对方接受邀请");
        view.findViewById(R.id.tv_invite_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDialog.dismiss();
                finish();
            }
        });
    }

    private PopupWindow mPopWindow;
    TextView type1;
    TextView type2;
    private void showPopupWindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_add_member_pop, null);
        mPopWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopWindow.setContentView(contentView);
        type1 = (TextView) contentView.findViewById(R.id.tv_add_member_pop_1);
        type2 = (TextView) contentView.findViewById(R.id.tv_add_member_pop_2);
        type1.setOnClickListener(this);
        type2.setOnClickListener(this);
        mPopWindow.showAsDropDown(ll_type);
    }
    @Override
    public void onBackPressed() {
        if(mPopWindow !=null && mPopWindow.isShowing()){
            mPopWindow.dismiss();
        }else {
            super.onBackPressed();
        }
    }
}
