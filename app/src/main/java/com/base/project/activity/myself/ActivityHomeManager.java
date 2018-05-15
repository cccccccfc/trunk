package com.base.project.activity.myself;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.activity.MainActivityTabHost;
import com.base.project.activity.login.ActivityLoginCreateHome;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.Constants;
import com.base.project.utils.SpTools;
import com.base.project.view.adapter.HomeHomeListAdapter;
import com.base.utilslibrary.internet.PersonalHomeManagerRequestUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityHomeManager extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_add)
    ImageView add;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.ll_home_manager_house)
    LinearLayout ll_house;
    @BindView(R.id.ll_home_manager_member)
    LinearLayout ll_member;
    @BindView(R.id.ll_home_manager_quit)
    LinearLayout ll_quit;
    @BindView(R.id.ll_home_manager_name)
    LinearLayout ll_name;

    @BindView(R.id.tv_home_manager_power)
    TextView tv_power;
    @BindView(R.id.tv_home_manager_name)
    TextView tv_name;
    @BindView(R.id.tv_home_manager_quit)
    TextView tv_quit;
    private String mg_id;
    private String ub_id;
    private String role;
    private HomeHomeListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_manager);
        ButterKnife.bind(this);
        title.setText("家庭管理");

        initdata();
        initListener();
    }

    private void initListener() {
        back.setOnClickListener(this);
        ll_house.setOnClickListener(this);
        ll_member.setOnClickListener(this);
        ll_quit.setOnClickListener(this);
        ll_name.setOnClickListener(this);
        add.setOnClickListener(this);
    }

    private void initdata() {
        mg_id = getIntent().getStringExtra("mg_id");
        ub_id = getIntent().getStringExtra("ub_id");
        PersonalHomeManagerRequestUtils.getRoleInfo(ub_id, mg_id, new PersonalHomeManagerRequestUtils.ForResultListener() {
            @Override
            public void onResponseMessage(String code) {
                if (!TextUtils.isEmpty(code)) {
                    role = code;
                } else {
                    role = getIntent().getStringExtra("role");
                }
                tv_name.setText(getIntent().getStringExtra("name"));
                if ("2".equals(role)) {
                    tv_power.setText("管理员");
                    tv_quit.setText("删除家庭");
                    ll_member.setVisibility(View.VISIBLE);
                    ll_house.setVisibility(View.VISIBLE);
                } else if ("0".equals(role)) {
                    tv_power.setText("家庭成员");
                    tv_quit.setText("退出家庭");
                } else if ("1".equals(role)) {
                    tv_power.setText("管理员");
                    ll_house.setVisibility(View.VISIBLE);
                    tv_quit.setText("退出家庭");
                }
            }
        });
    }

    private PopupWindow mPopWindow;

    ListView listView;

    @Override
    public void onClick(View v) {
        final Intent intent;
        switch (v.getId()) {
            case R.id.ll_home_manager_house://房间管理

                intent = new Intent(this, ActivityHouseManager.class);
                intent.putExtra("mg_id", mg_id);
                intent.putExtra("ub_id", ub_id);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
            case R.id.ll_home_manager_member://成员与权限
                intent = new Intent(this, ActivityMemberAndPower.class);
                intent.putExtra("mg_id", mg_id);
                intent.putExtra("ub_id", ub_id);
                startActivity(intent);
                overridePendingTransition(0, 0);
                break;
            case R.id.ll_home_manager_quit://退出家庭
                AlertDialog.Builder builder_dialog = new AlertDialog.Builder(this);
                builder_dialog.setTitle("是否" + tv_quit.getText().toString())
                        .setPositiveButton("是", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if ("2".equals(role)) {
                                    deleteHome();
                                } else {
                                    quitHome();
                                }
                            }
                        })
                        .setNegativeButton("否", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();
                break;
            case R.id.iv_base_back://退出
                finish();
                break;
            case R.id.iv_base_add://创建家庭
                showPopupWindow();
                break;
            case R.id.ll_home_manager_name://修改家庭名称
                intent = new Intent(this, ActivityCreateHome.class);
                intent.putExtra("type", 10);
                intent.putExtra("mg_id", mg_id);
                intent.putExtra("ub_id", ub_id);
                intent.putExtra("name", getIntent().getStringExtra("name"));
                startActivityForResult(intent, 20);
                overridePendingTransition(0, 0);
                break;
        }
    }

    private void showPopupWindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_manger_page, null);
        mPopWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT, 220);
        mPopWindow.setContentView(contentView);
        TextView text1 = (TextView) contentView.findViewById(R.id.lv_home_one);
        TextView text2 = (TextView) contentView.findViewById(R.id.lv_home_two);
        mPopWindow.setBackgroundDrawable(new BitmapDrawable(getResources()));
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.showAsDropDown(add);
        text1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ActivityJoinFamliy.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                mPopWindow.dismiss();
            }
        });
        text2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ActivityCreateHome.class);
                intent.putExtra("mg_id", mg_id);
                intent.putExtra("ub_id", ub_id);
                startActivity(intent);
                overridePendingTransition(0, 0);
                mPopWindow.dismiss();
            }
        });




    }

    private void deleteHome() {
        PersonalHomeManagerRequestUtils.deleteHomeGroup(this, ub_id, mg_id, new PersonalHomeManagerRequestUtils.ForResultListener() {
            @Override
            public void onResponseMessage(String code) {
                resultForCode(code);
            }
        });
    }

    private void quitHome() {
        String type = "";
        if ("1".equals(role)) {
            type = "1";
        } else if ("0".equals(role)) {
            type = "2";
        }
        PersonalHomeManagerRequestUtils.quitHomeGroup(this, ub_id, mg_id, type, new PersonalHomeManagerRequestUtils.ForResultListener() {
            @Override
            public void onResponseMessage(String code) {
                resultForCode(code);
            }
        });
    }

    private void resultForCode(String code) {
        if (!TextUtils.isEmpty(code)) {
            if (Integer.parseInt(code) > 0) {//还有家庭组，退出到首页重新刷新设备
                //新建家庭或退出家庭,通知主页更新信息，暂时先用sp通知
                SpTools.setBoolean(ActivityHomeManager.this, Constants.deletehome, true);
                Intent intent = new Intent(ActivityHomeManager.this, MainActivityTabHost.class);
                startActivity(intent);
                overridePendingTransition(0, 0);
                finish();
            } else {//没有家庭组，去创建家庭页面
                Intent intent1 = new Intent(ActivityHomeManager.this, ActivityLoginCreateHome.class);
                startActivity(intent1);
                overridePendingTransition(0, 0);
                SpTools.setBoolean(this, Constants.createhome, false);
                finish();
            }
        } else {
            CommonUtils.toastMessage("失败，请重试");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String homeName = data.getStringExtra("name");
        switch (resultCode) {
            case 21:
                tv_name.setText(homeName);
                SpTools.setString(this, Constants.homename, homeName);
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if(keyCode==KeyEvent.KEYCODE_BACK){
            if (mPopWindow != null &&mPopWindow.isShowing()) {
                return true;//不执行父类点击事件
            }
        }
        return super.onKeyDown(keyCode, event);//继续执行父类其他点击事件
    }
}

