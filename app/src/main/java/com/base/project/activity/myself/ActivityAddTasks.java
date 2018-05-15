package com.base.project.activity.myself;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.adapter.AddTasksAdapter;
import com.base.project.utils.CommonUtils;
import com.base.project.view.adapter.HomeHomeListAdapter;
import com.base.utilslibrary.bean.AddtaskslistBean;
import com.base.utilslibrary.bean.CtrlListBean;
import com.base.utilslibrary.internet.GetUserContextualModelRequestUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityAddTasks extends AppCompatActivity implements View.OnClickListener{

    @BindView(R.id.iv_base_back)
    ImageView ivBaseBack;
    @BindView(R.id.tv_base_title)
    TextView tvBaseTitle;
    @BindView(R.id.iv_base_add)
    ImageView ivBaseAdd;
    @BindView(R.id.tv_base_select)
    TextView tvBaseSelect;
    @BindView(R.id.rv_house_manager_flour)
    RecyclerView rvHouseManagerFlour;
    @BindView(R.id.iv_base_down)
    ImageView ivBaseDown;
    @BindView(R.id.rv_house_manager_two)
    RecyclerView rvHouseManagerTwo;
    private String mg_id;
    private String ub_id;
    private List<AddtaskslistBean> mListTask;
    private AddTasksAdapter addtasksAdapter;

    private AddtaskslistBean addtasks;
    private boolean closeIsopen = true;
    private AddtaskslistBean addtaskslist;
    private String type;
    private List<AddtaskslistBean> mInfoResult;
    private String jsonString;
    private ArrayList newList;
    private ArrayList<String> mListName;
    private PopupWindow mPopWindow;
    ListView listView;
    private List<AddtaskslistBean> mArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_tasks);
        ButterKnife.bind(this);

        tvBaseTitle.setText("所有房间");
        ivBaseAdd.setVisibility(View.GONE);
        tvBaseSelect.setVisibility(View.VISIBLE);
        ivBaseDown.setVisibility(View.VISIBLE);
        mg_id = getIntent().getStringExtra("mg_id");
        ub_id = getIntent().getStringExtra("ub_id");
        type = getIntent().getStringExtra("type");
        jsonString = getIntent().getStringExtra("jsonString");
        initdata();
        initListener();
    }

    /**
     * 设置 按钮状态
     *
     * @param jsonString
     * @param lists
     */
    private void Setcheckbox(List<AddtaskslistBean> lists, String jsonString) {
        Log.i("wsx", "onClick: -------");
        final Gson gson = new Gson();
        Type type = new TypeToken<List<AddtaskslistBean>>() {
        }.getType();
        if (!TextUtils.isEmpty(jsonString)) {
            List<AddtaskslistBean> studentList = gson.fromJson(jsonString, type);
            for (int i = 0; i < lists.size(); i++) {
                for (int j = 0; j < studentList.size(); j++) {
                    if (lists.get(i).getDi_id() == studentList.get(j).di_id) {
                        lists.get(i).setIsshow(true);
                    }
                }
              //  Log.i("wsx", "保存选中的状态: " + lists.get(i).isshow());
            }
            addtasksAdapter.notifyDataSetChanged();
        } else {
        }
    }

    private void initListener() {
        /**
         * 点击返回 将选择的数据返回
         */
        ivBaseBack.setOnClickListener(this);
        //判断是否全选
        tvBaseSelect.setOnClickListener(this);
        /**
         * 设备筛选
         */
        tvBaseTitle.setOnClickListener(this);
        ivBaseDown.setOnClickListener(this);
    }

    private void showPopupWindow() {
        View contentView = LayoutInflater.from(this).inflate(R.layout.activity_home_page_pop, null);
        mPopWindow = new PopupWindow(contentView,
                LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
        mPopWindow.setContentView(contentView);
        listView = (ListView) contentView.findViewById(R.id.lv_home_home);
        if (newList == null) {
            CommonUtils.toastMessage("暂无数据");
            return;
        }
        HomeHomeListAdapter adapter = new HomeHomeListAdapter(this, newList);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
               // SpTools.setInt(getApplication(), Constants.home_position, position);
                mArrayList = new ArrayList<AddtaskslistBean>();
                for (int i = 0; i < mListTask.size(); i++) {
                    if (newList.get(position).equals(mListTask.get(i).getMg_name())) {
                        mArrayList.add(mListTask.get(i));
                    }
                }
                rvHouseManagerTwo.setVisibility(View.VISIBLE);
                rvHouseManagerFlour.setVisibility(View.GONE);
                addtasksAdapter = new AddTasksAdapter(mArrayList, getApplication());
                rvHouseManagerTwo.setLayoutManager(new GridLayoutManager(getApplication(), 1));
                rvHouseManagerTwo.setAdapter(addtasksAdapter);
                mPopWindow.dismiss();
            }
        });
//        mPopWindow.setBackgroundDrawable(new BitmapDrawable(getResources()));
        mPopWindow.setOutsideTouchable(true);
        mPopWindow.setFocusable(true);
        mPopWindow.showAsDropDown(tvBaseTitle);
    }
    /**
     * 请求所以房间的设备
     */
    private void initdata() {

        GetUserContextualModelRequestUtils.getctrllists(this, ub_id, mg_id, "1", new GetUserContextualModelRequestUtils.CtrllistListener() {
            @Override
            public void onResponseMessage(List<List<CtrlListBean>> info) {

                if (info == null) {
                    CommonUtils.toastMessage("暂无数据");
                } else {
                    presentation(info);

                }
            }
        });
    }
    private void presentation(List<List<CtrlListBean>> info) {
        mListName = new ArrayList<String>();//家庭组列表
        mListTask = new ArrayList<>();
        for (int i = 0; i < info.size(); i++) {
            for (int j = 0; j < info.get(i).size(); j++) {
                addtasks = new AddtaskslistBean();
                addtasks.setDb_dt_id(info.get(i).get(j).db_dt_id);
                addtasks.setMg_id(Integer.parseInt(info.get(i).get(j).mg_id));
                addtasks.setDi_clustername(info.get(i).get(j).di_clustername);
                addtasks.setDi_clusterID(info.get(i).get(j).di_clusterID);
                addtasks.setDi_db_sbID(info.get(i).get(j).di_db_sbID);
                addtasks.setMg_level(info.get(i).get(j).mg_level);
                addtasks.setMg_name(info.get(i).get(j).mg_name);
                addtasks.setDi_id(info.get(i).get(j).di_id);
                mListTask.add(addtasks);

            }
        }
        for (int i = 0; i < mListTask.size(); i++) {
            mListName.add(mListTask.get(i).getMg_name());
        }
        //去重
        newList = new ArrayList(new HashSet(mListName));
        addtasksAdapter = new AddTasksAdapter(mListTask, this);
        rvHouseManagerFlour.setLayoutManager(new GridLayoutManager(this, 1));
        rvHouseManagerFlour.setAdapter(addtasksAdapter);
        Setcheckbox(mListTask, jsonString);
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.iv_base_back://点击返回
                mInfoResult = new ArrayList<>();
                if (mListTask != null) {
                    for (int i = 0; i < mListTask.size(); i++) {
                        if (mListTask.get(i).isshow()) {
                            addtaskslist = new AddtaskslistBean();
                            addtaskslist.setDb_dt_id(mListTask.get(i).db_dt_id);
                            addtaskslist.setMg_id(mListTask.get(i).mg_id);
                            addtaskslist.setDi_clustername(mListTask.get(i).di_clustername);
                            addtaskslist.setDi_clusterID(mListTask.get(i).di_clusterID);
                            addtaskslist.setDi_db_sbID(mListTask.get(i).di_db_sbID);
                            addtaskslist.setMg_level(mListTask.get(i).mg_level);
                            addtaskslist.setMg_name(mListTask.get(i).mg_name);
                            addtaskslist.setDi_id(mListTask.get(i).di_id);
                            addtaskslist.setIsshow(mListTask.get(i).isshow());
                            mInfoResult.add(addtaskslist);
                        }

                    }
                }
                //Log.i("wsx", "准备传输的数据 " + mInfoResult.size());
                final Gson gson = new Gson();
                String jsonString = gson.toJson(mInfoResult);
                Intent intent = new Intent();
                intent.putExtra("jsonString", jsonString);
                setResult(1, intent);
                finish();
                break;
            case R.id.tv_base_select://是否全选
                if (closeIsopen) {
                    for (int i = 0; i < mListTask.size(); i++) {
                        mListTask.get(i).setIsshow(true);
                    }
                    tvBaseSelect.setText("取消全选");
                    closeIsopen = !closeIsopen;
                    addtasksAdapter.notifyDataSetChanged();
                } else {
                    tvBaseSelect.setText("全选");
                    closeIsopen = !closeIsopen;
                    for (int i = 0; i < mListTask.size(); i++) {
                        mListTask.get(i).setIsshow(false);
                    }
                    addtasksAdapter.notifyDataSetChanged();
                }
                break;
            case R.id.iv_base_down://切换房间
            case R.id.tv_base_title://切换房间
                if (!CommonUtils.isNetworkAvailable(getApplication())) {
                    CommonUtils.toastMessage("当前无可用网络，在有网时刷新再试");
                    return;
                }
                if (mPopWindow != null && mPopWindow.isShowing()) {
                    mPopWindow.dismiss();
                } else {
                    showPopupWindow();
                }
                break;

        }
    }
}
