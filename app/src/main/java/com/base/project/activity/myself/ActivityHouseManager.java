package com.base.project.activity.myself;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.Constants;
import com.base.project.utils.SpTools;
import com.base.project.view.adapter.FlourManagerAdapter;
import com.base.project.view.adapter.HouseManagerAdapter;
import com.base.utilslibrary.bean.GroupInfoBean;
import com.base.utilslibrary.internet.PersonalHomeManagerRequestUtils;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityHouseManager extends AppCompatActivity implements View.OnClickListener {

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_add)
    ImageView add;
    @BindView(R.id.tv_base_title)
    TextView title;
    @BindView(R.id.tv_base_flour)
    TextView flour;

    @BindView(R.id.srl_house_manager)
    SmartRefreshLayout smartRefreshLayout;

    @BindView(R.id.ll_house_manager_house)
    LinearLayout ll_house;//房间管理
    @BindView(R.id.tv_house_manager)
    TextView tv_house_add;
    @BindView(R.id.rv_house_manager)
    RecyclerView mRecyclerView;
    HouseManagerAdapter houseManagerAdapter;
    private int requsetCode = 10;
    private int requsetCodeFlour = 9;

    @BindView(R.id.ll_house_manager_flour)
    LinearLayout ll_flour;//楼层管理
    @BindView(R.id.rv_house_manager_flour)
    RecyclerView mRecyclerView_flour;
    FlourManagerAdapter flourManagerAdapter;

    private boolean isHouse = true;//是否在房间管理
    private String mg_id;
    private String ub_id;
    private GridLayoutManager gridLayoutManager;
    private LinearLayoutManager layoutManager;
    private StringBuilder builder;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_house_manager);
        ButterKnife.bind(this);
        title.setText("房间管理");
        add.setVisibility(View.INVISIBLE);
        flour.setVisibility(View.VISIBLE);

        initdata();
        initListener();
    }

    private void initListener() {
        back.setOnClickListener(this);
        flour.setOnClickListener(this);
        tv_house_add.setOnClickListener(this);
    }

    private void initdata() {
        mg_id = getIntent().getStringExtra("mg_id");
        ub_id = getIntent().getStringExtra("ub_id");

        internet();

        ClassicsHeader header = new ClassicsHeader(this);
        header.setPrimaryColors(this.getResources().getColor(R.color.colorPrimary), Color.WHITE);
        smartRefreshLayout.setRefreshHeader(header);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                internet();
            }
        });

    }

    private void internet() {
        Log.i("wer", "internet: " +ub_id +"--------"+ mg_id);
        PersonalHomeManagerRequestUtils.houseManagerInfo(this, ub_id, mg_id,
                new PersonalHomeManagerRequestUtils.ForResultDataListener() {
                    @Override
                    public void onResponseMessage(GroupInfoBean info) {
                        if(info == null){
                            CommonUtils.toastMessage("暂无数据");
                        }else {
                            groupInfoBean = info;
                            /**
                             * GroupInfoBean内的child数量为1时，表示只有一个楼层
                             * 大于1时表示多个楼层
                             */
                            if(info.child.size() == 1){
                                tv_house_add.setVisibility(View.VISIBLE);
                                if(gridLayoutManager == null){
                                    gridLayoutManager = new GridLayoutManager(ActivityHouseManager.this,2);
                                    mRecyclerView.setLayoutManager(gridLayoutManager);
                                }
                                houseManagerAdapter = new HouseManagerAdapter(ActivityHouseManager.this,info.child.get(0).child,ub_id,0);
                                mRecyclerView.setAdapter(houseManagerAdapter);
                                houseManagerAdapter.setOnItemClickListener(new HouseManagerAdapter.MyItemClickListener() {
                                    @Override
                                    public void onItemClick(View v, final int position, GroupInfoBean bean) {
                                        PersonalHomeManagerRequestUtils.deleteGroup(ActivityHouseManager.this,ub_id, bean.getMg_id(), new PersonalHomeManagerRequestUtils.ForResultListener() {
                                            @Override
                                            public void onResponseMessage(String code) {
                                                if("成功".equals(code)){
                                                    //房间管理变动的时候(删除或增加楼层或房间),通知主页更新信息，暂时先用sp通知
                                                    SpTools.setBoolean(CommonUtils.getContext(), Constants.housechange,true);
                                                    houseManagerAdapter.remove(position);
                                                    CommonUtils.toastMessage("删除房间成功");
                                                }else {
                                                    CommonUtils.toastMessage("删除失败，请重试");
                                                }
                                            }
                                        });
                                    }
                                });
                                /**
                                 * 修改房间名称的回调
                                 */
                                ActivityModifyHouseName.setModifyNameListener(new ActivityModifyHouseName.ModifyNameListener() {
                                    @Override
                                    public void modifyName(String name, int pos, int list_id) {
                                        CommonUtils.logMes("----mg_id--"+groupInfoBean.child.get(list_id).child.get(pos).getMg_id()+"----"+list_id);
                                        groupInfoBean.child.get(list_id).child.get(pos).setMg_name(name);
                                        houseManagerAdapter.notifyDataSetChanged();
                                    }
                                });
                            }else {
                                if(isHouse){
                                    isHouse = !isHouse;
                                }
                                ll_house.setVisibility(View.INVISIBLE);
                                ll_flour.setVisibility(View.VISIBLE);
                                if(layoutManager == null){
                                    layoutManager = new LinearLayoutManager(ActivityHouseManager.this);
                                    mRecyclerView_flour.setLayoutManager(layoutManager);
                                }
                                if(flourManagerAdapter == null){
                                    flourManagerAdapter = new FlourManagerAdapter(ActivityHouseManager.this,info.child,ub_id);
                                    mRecyclerView_flour.setAdapter(flourManagerAdapter);
                                    flourManagerAdapter.setOnItemClickListener(new FlourManagerAdapter.MyItemClickListener() {
                                        @Override
                                        public void onItemClick(View v, int position) {
                                            addHousePos = position;
                                            Intent intent = new Intent(ActivityHouseManager.this,ActivityCreateHouse.class);
                                            intent.putExtra("code",requsetCodeFlour);
                                            startActivityForResult(intent,requsetCodeFlour);
                                            overridePendingTransition(0,0);
                                        }
                                    });
                                }
                            }
                        }
                        smartRefreshLayout.finishRefresh(1000);
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.iv_base_back://退出
                finish();
                break;
            case R.id.tv_base_flour://添加楼层
                if(isHouse){
                    isHouse = !isHouse;
                    ll_house.setVisibility(View.INVISIBLE);
                    ll_flour.setVisibility(View.VISIBLE);
                    if (groupInfoBean == null  ){
                        CommonUtils.toastMessage("暂无数据");
                        return;
                    }else{
                        PersonalHomeManagerRequestUtils.addHouse(this,ub_id, groupInfoBean.mg_parent_id_dir + "," + groupInfoBean.mg_id, "F2", new PersonalHomeManagerRequestUtils.ForResultDataListener() {
                            @Override
                            public void onResponseMessage(GroupInfoBean info) {
                                if(info !=null){
                                    //房间管理变动的时候(删除或增加楼层或房间),通知主页更新信息，暂时先用sp通知
                                    SpTools.setBoolean(CommonUtils.getContext(), Constants.housechange,true);
                                    internet();
                                }else {
                                    isHouse = !isHouse;
                                    ll_house.setVisibility(View.VISIBLE);
                                    ll_flour.setVisibility(View.INVISIBLE);
                                }
                            }

                        });
                    }


                }else {
                    if(groupInfoBean.child.size() > 4){
                        CommonUtils.toastMessage("最多只能建5层，不能在建了");
                        return;
                    }
                    if(builder == null){
                        builder = new StringBuilder();
                    }
                    for(int i = 0;i<groupInfoBean.child.size();i++){
                        builder.append(groupInfoBean.child.get(i).getMg_name());
                    }
                    String floor = chooseFlourName(builder.toString());
                    builder.setLength(0);
                    PersonalHomeManagerRequestUtils.addHouse(this,ub_id, groupInfoBean.mg_parent_id_dir + "," + groupInfoBean.mg_id, floor, new PersonalHomeManagerRequestUtils.ForResultDataListener() {
                        @Override
                        public void onResponseMessage(GroupInfoBean info) {
                            if(info !=null){
                                //房间管理变动的时候(删除或增加楼层或房间),通知主页更新信息，暂时先用sp通知
                                SpTools.setBoolean(CommonUtils.getContext(), Constants.housechange,true);
                                flourManagerAdapter.addDatas(info);
                                flourManagerAdapter.notifyDataSetChanged();
                            }else {
                                CommonUtils.toastMessage("创建楼层失败，请重试");
                            }
                        }
                    });
                }
                break;
            case R.id.tv_house_manager://添加房间
                Intent intent = new Intent(this,ActivityCreateHouse.class);
                intent.putExtra("code",requsetCode);
                startActivityForResult(intent,requsetCode);
                overridePendingTransition(0,0);
                break;
        }
    }

    private String chooseFlourName(String str) {
        if(!str.contains("F2")){
            return "F2";
        }else if(!str.contains("F3")){
            return "F3";
        }else if(!str.contains("F4")){
            return "F4";
        }
        return "F5";
    }

    private GroupInfoBean groupInfoBean;
    private int addHousePos = 0;
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        String houseName = data.getStringExtra("houseName");
        switch (resultCode){
            case 11:
                PersonalHomeManagerRequestUtils.addHouse(this,ub_id, groupInfoBean.child.get(0).mg_parent_id_dir + "," + groupInfoBean.child.get(0).mg_id
                        , houseName, new PersonalHomeManagerRequestUtils.ForResultDataListener() {
                            @Override
                            public void onResponseMessage(GroupInfoBean info) {
                                if(info != null){
                                    //房间管理变动的时候(删除或增加楼层或房间),通知主页更新信息，暂时先用sp通知
                                    SpTools.setBoolean(CommonUtils.getContext(), Constants.housechange,true);
                                    houseManagerAdapter.addDatas(info);
                                    houseManagerAdapter.notifyDataSetChanged();
                                }else {
                                    CommonUtils.toastMessage("创建房间失败，请重试");
                                }
                            }
                        });
                break;
            case 12:
                addHouseListener.onResult(houseName,addHousePos);
                break;
        }
    }

    public interface FlourAddHouseListener {
        void onResult(String name, int position);
    }
    private static FlourAddHouseListener addHouseListener;
    public static void setFlourAddHouseListener(FlourAddHouseListener listener){
        addHouseListener = listener;
    }
}
