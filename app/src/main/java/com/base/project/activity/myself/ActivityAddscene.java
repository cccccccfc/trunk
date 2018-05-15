package com.base.project.activity.myself;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.adapter.HousePatternAdapter;
import com.base.project.bean.NewdataBean;
import com.base.project.bean.NewidBean;
import com.base.project.bean.SetpushDate;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.Constants;
import com.base.project.utils.SpTools;
import com.base.utilslibrary.bean.AddtaskslistBean;
import com.base.utilslibrary.bean.CtrlListBean;
import com.base.utilslibrary.internet.GetUserContextualModelRequestUtils;
import com.bigkoo.pickerview.TimePickerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.base.project.utils.DateUtil.dateFormat;

public class ActivityAddscene extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_base_back)
    ImageView ivBaseBack;
    @BindView(R.id.tv_base_title)
    TextView tvBaseTitle;
    @BindView(R.id.tv_base_confirm)
    TextView tvBaseConfirm;
    @BindView(R.id.edit_add_hint)
    EditText editAddHint;
    @BindView(R.id.lineat_add_hint)
    RelativeLayout lineatAddHint;
    @BindView(R.id.image_add_hint)
    ImageView imageAddHint;
    @BindView(R.id.linear_pattern_right)
    LinearLayout linearPatternRight;
    @BindView(R.id.lineat_add_image)
    RelativeLayout lineatAddImage;
    @BindView(R.id.img_add_pattern)
    ImageView imgAddPattern;
    @BindView(R.id.lineat_execute_hint)
    RelativeLayout lineatExecuteHint;
    @BindView(R.id.ll_myself_information_content)
    LinearLayout llMyselfInformationContent;
    @BindView(R.id.rv_house_pattern)
    RecyclerView rvHousePattern;
    @BindView(R.id.iv_base_add)
    ImageView ivBaseAdd;
    @BindView(R.id.lineat_add_facility)
    RelativeLayout lineatAddFacility;
    @BindView(R.id.ll_parrrent_quit)
    LinearLayout llParrrentQuit;
    private String ms_type;
    private String mg_id;
    private String ub_id;
    private String jsonString;
    private HousePatternAdapter housePatternAdapter;
    private List<AddtaskslistBean> mAddTasks = new ArrayList<>();
    private String selectdate;

    private SetpushDate setpushDate;
    private List<SetpushDate.ListBean> mListPush;
    private SetpushDate.ListBean mListBean;
    private String transmit_id;
    private String transmit_type;
    private String transmit_name;
    private NewdataBean newdataBean;
    private List<NewdataBean> newdata;
    private NewidBean newidBean;
    private List<NewidBean> newid;
    private String statetype;
    private String type;
    private String name;
    private Gson gson;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addscene);
        ButterKnife.bind(this);
        gson = new Gson();
        tvBaseTitle.setText("情景模式");
        tvBaseConfirm.setVisibility(View.VISIBLE);
        ivBaseBack.setVisibility(View.VISIBLE);
        ivBaseAdd.setVisibility(View.GONE);

        initdata();
        initListener();
    }

    private void initdata() {
        mg_id = getIntent().getStringExtra("mg_id");
        ub_id = getIntent().getStringExtra("ub_id");
        statetype = getIntent().getStringExtra("statetype");
        transmit_id = getIntent().getStringExtra("transmit_id");
        transmit_name = getIntent().getStringExtra("transmit_name");
        transmit_type = getIntent().getStringExtra("transmit_type");
        type = getIntent().getStringExtra("type");
        llParrrentQuit.setOnClickListener(this);
       // Log.i("qaz", "删除按钮: " + statetype);
        if (!TextUtils.isEmpty(transmit_id) && "1".equals(statetype)) {
            editAddHint.setText(transmit_name);
            llParrrentQuit.setVisibility(View.VISIBLE);
            ms_type = transmit_type;
            getDate(ub_id, transmit_id);
        }
    }

    /**
     * 情景模式列表 进入情景模式详情页 网络请求
     *
     * @param ui_id
     * @param ms_id
     */
    public void getDate(String ui_id, String ms_id) {
        Log.i("qaz", "打印数据" + ui_id + "ui_id" + ms_id + "ms_id");
        GetUserContextualModelRequestUtils.getlisttoexecute(ui_id, ms_id, new GetUserContextualModelRequestUtils.ListToExecuteListener() {
            @Override
            public void onResponseMessage(List<List<CtrlListBean>> info, String message) {

                if ("0".equals(message)) {
                    //  Log.i("qaz", "返回的数据 成功1111111");
                    if (info == null) {
                        // Log.i("qaz", "暂无数据11111");
                    } else {
                        presentation(info);
                    }
                } else if ("1".equals(message)) {
                    // Log.i("qaz", "返回的数据 参数错误1111111");
                } else if ("2".equals(message)) {
                    //  Log.i("qaz", "返回的数据 没有数据111111111");
                } else {
                    // Log.i("qaz", "返回的数据 操作失败111111111");
                }

            }
        });
    }

    private AddtaskslistBean addtasks;
    private List<AddtaskslistBean> mInfoResult;

    private void presentation(List<List<CtrlListBean>> info) {

        mInfoResult = new ArrayList<>();
        for (int i = 0; i < info.size(); i++) {
            for (int j = 0; j < info.get(i).size(); j++) {
                addtasks = new AddtaskslistBean();
               // Log.i("qaz", "presentation: " + info.get(i).get(j).di_clusterID);
                addtasks.setSelect_time(String.valueOf(info.get(i).get(j).me_time));
                addtasks.setDi_clustername(info.get(i).get(j).me_di_clustername);
                addtasks.setDi_clusterID(info.get(i).get(j).me_di_clusterID);
                addtasks.setDi_db_sbID(info.get(i).get(j).me_di_db_sbID);
                addtasks.setOn_off(info.get(i).get(j).me_mh_type);
                addtasks.setMe_ms_id(info.get(i).get(j).me_ms_id);
                addtasks.setDd_macaddr(info.get(i).get(j).dd_macaddr);
                addtasks.setMg_level(info.get(i).get(j).mg_level);
                addtasks.setMg_name(info.get(i).get(j).mg_name);
                addtasks.setMg_id(Integer.parseInt(info.get(i).get(j).mg_id));
                addtasks.setDi_id(info.get(i).get(j).di_id);
                mInfoResult.add(addtasks);
            }
        }
        String jsonString = gson.toJson(mInfoResult);
        SetDate(jsonString);
    }
    private void initListener() {
        lineatAddFacility.setOnClickListener(this);
        ivBaseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        lineatExecuteHint.setOnClickListener(this);
        linearPatternRight.setOnClickListener(this);
        tvBaseConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("wsx", "点击保存" + editAddHint.getText().toString());
                if (TextUtils.isEmpty(editAddHint.getText().toString()) || TextUtils.isEmpty(ms_type)) {
                    CommonUtils.toastMessage("请填写情景名称，选择情景图标");
                } else if(mAddTasks.size() == 0){
                    CommonUtils.toastMessage("请填加控制的设备");
                }else {
                     name = editAddHint.getText().toString();
                    PushDate(name ,type ,transmit_id);
                }
            }
        });
        //设置时间
        HousePatternAdapter.setOnItemClickListener(new HousePatternAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {

                setTime(position);

            }
        });
        //设置开关状态
        HousePatternAdapter.setOnSexClickListener(new HousePatternAdapter.OnSexClickListener() {
            @Override
            public void onSexClick(View view, int position) {
                Showdialog(position);
            }
        });
    }

    /**
     * 根据position 判断哪个条目需要显示ialog
     * @param p
     */
    private void Showdialog(final int p) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this); //定义一个AlertDialog
        String[] strarr = {"开", "关"};
        builder.setItems(strarr, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface arg0, int arg1) {
                // 自动生成的方法存根
                if (arg1 == 0) {//开
                    mAddTasks.get(p).setOn_off(5);
                } else {//关

                    mAddTasks.get(p).setOn_off(6);
                }
                housePatternAdapter.notifyDataSetChanged();
            }
        });
        builder.show();
    }
    private boolean[] typetime = new boolean[]{false, false, false, false, false, true};//显示类型 默认全部显示
    /**
     * 时间选择器
     *
     * @param p
     */
    private void setTime(final int p) {
        TimePickerView mTime = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {
                selectdate = dateFormat("ss", date);
                mAddTasks.get(p).setSelect_time(selectdate);
                housePatternAdapter.notifyDataSetChanged();
            }
        }).setType(typetime).build();
        mTime.show();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.lineat_execute_hint:  // 创建情景模式添加执行任务
                Intent intent = new Intent(this, ActivityAddTasks.class);
                intent.putExtra("mg_id", mg_id);
                intent.putExtra("ub_id", ub_id);
                intent.putExtra("type", "1");
                startActivityForResult(intent, 1);
                overridePendingTransition(0, 0);
                break;
            case R.id.linear_pattern_right:  // 跳转选择图标
                Intent inte = new Intent(this, ActivityAddImage.class);
                startActivityForResult(inte, 2);
                overridePendingTransition(0, 0);
                break;
            case R.id.lineat_add_facility:  // 修改情景模式继续添加执行任务
                saveData();
                String jsonString = gson.toJson(mAddTasks);
                Intent intes = new Intent(this, ActivityAddTasks.class);
                intes.putExtra("mg_id", mg_id);
                intes.putExtra("ub_id", ub_id);
                intes.putExtra("type", "0");
                intes.putExtra("jsonString" , jsonString);
                startActivityForResult(intes, 1);
                overridePendingTransition(0, 0);
                break;
            case R.id.ll_parrrent_quit:  // 点击删除按钮
                type= "2";
                PushDate(name ,type ,transmit_id);
                break;
        }
    }

    /**
     * 保存设置的设备信息
     */
    private void saveData() {

        if (newdata == null) {
            newdata = new ArrayList<NewdataBean>();
        }
        if (newid == null) {
            newid = new ArrayList<NewidBean>();
        }
        for (int i = 0; i < mAddTasks.size(); i++) {
            newdataBean = new NewdataBean();
            newidBean = new NewidBean();
            newdataBean.setSelect_time(mAddTasks.get(i).getSelect_time());
            newdataBean.setOn_off(mAddTasks.get(i).getOn_off());
            newdataBean.setDi_id(mAddTasks.get(i).getDi_id());
            newidBean.setId(mAddTasks.get(i).getDi_id());
            newdata.add(newdataBean);
            newid.add(newidBean);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (resultCode) {
            case 1:
                jsonString = data.getStringExtra("jsonString");
                SetDate(jsonString);
                break;
            case 2:
                ms_type = data.getStringExtra("type");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    /**
     * 设置显示的设备
     *
     * @param string
     */
    public void SetDate(String string) {
        mAddTasks = gson.fromJson(string, new TypeToken<List<AddtaskslistBean>>() {}.getType());
        if (mAddTasks.size() == 0) {
            lineatExecuteHint.setVisibility(View.VISIBLE);
            rvHousePattern.setVisibility(View.GONE);
            lineatAddFacility.setVisibility(View.GONE);
            llParrrentQuit.setVisibility(View.GONE);
        } else {
            lineatExecuteHint.setVisibility(View.GONE);
            rvHousePattern.setVisibility(View.VISIBLE);
            lineatAddFacility.setVisibility(View.VISIBLE);
            for (int i = 0; i < mAddTasks.size(); i++) {
                if (newdata != null && newdata.size() > 0) {
                    for (int j = 0; j < newdata.size(); j++) {
                        if (mAddTasks.get(i).getDi_id() == newid.get(j).getId()) {
                            mAddTasks.get(i).setOn_off(newdata.get(j).getOn_off());
                            mAddTasks.get(i).setSelect_time(newdata.get(j).getSelect_time());
                        }
                    }
                }
            }
            housePatternAdapter = new HousePatternAdapter(mAddTasks, this);
            rvHousePattern.setLayoutManager(new LinearLayoutManager(this));
            rvHousePattern.setAdapter(housePatternAdapter);
            housePatternAdapter.notifyDataSetChanged();

        }
    }

    /**
     * 上传情景模式
     *
     * @param name
     */
    public void PushDate(String name ,String type ,String transmit_id) {
        if (setpushDate == null) {
            setpushDate = new SetpushDate();
        }
        if (mListPush == null) {
            mListPush = new ArrayList<SetpushDate.ListBean>();
        }
        mListPush.clear();
        for (int i = 0; i < mAddTasks.size(); i++) {
            if (mAddTasks.get(i).getOn_off() == 0) {
                CommonUtils.toastMessage("请填写完整的开和关信息");
                return;
            } else {
                mListBean = new SetpushDate.ListBean();
                mListBean.setMg_id(mAddTasks.get(i).mg_id);
                mListBean.setMe_di_clusterID(mAddTasks.get(i).di_clusterID);
                mListBean.setMe_time(Integer.parseInt(mAddTasks.get(i).select_time));
                mListBean.setMe_di_db_sbID(mAddTasks.get(i).di_db_sbID);
                mListBean.setMe_mh_type(mAddTasks.get(i).getOn_off());
                mListPush.add(mListBean);
            }
        }
        setpushDate.setList(mListPush);
        final Gson gson = new Gson();
        String jsonString = gson.toJson(mListPush);
        Log.i("qaz", "PushDate: " + jsonString.toString());
        GetUserContextualModelRequestUtils.setaddscene(ub_id, mg_id, name, jsonString, type, ms_type, transmit_id, new GetUserContextualModelRequestUtils.AddSceneListener() {
            @Override
            public void onResponseMessage(String message) {
                if ("0".equals(message)) {
                    // Log.i("qaz", "返回的数据 成功33333");
                    SpTools.setBoolean(CommonUtils.getContext(), Constants.editpanttern,true);
                    finish();
                } else if ("1".equals(message)) {
                    // Log.i("qaz", "返回的数据 参数错误33333");
                } else if ("2".equals(message)) {
                    // Log.i("qaz", "返回的数据 没有数据3333333");
                } else {
                    // Log.i("qaz", "返回的数据 操作失败33333333");
                }
            }
        });
    }
}
