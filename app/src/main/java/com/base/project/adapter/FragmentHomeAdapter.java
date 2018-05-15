package com.base.project.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.activity.facility.ActivityFacilityInfo;
import com.base.project.base.BaseApplication;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.Constants;
import com.base.project.utils.SpTools;
import com.base.utilslibrary.bean.FragmentHomeShowBean;
import com.google.gson.Gson;

import java.util.List;

/**
 * Created by Administrator on 2017/11/3.
 */

public class FragmentHomeAdapter extends RecyclerView.Adapter<FragmentHomeAdapter.ViewHolder> implements RecyclerView.OnItemTouchListener {

    private final Gson gson;
    private List<FragmentHomeShowBean> mInfo;
    private Context mContext;
    private Intent intent;
    private int dd_ui_id;
    public FragmentHomeAdapter(List<FragmentHomeShowBean> info, Context instance) {
        this.mInfo = info;
        this.mContext = instance;
        gson = new Gson();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_home_control, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        // Log.i("qaz", "列表长度: " + mInfo.size());
        //Log.i("qaz", "开关状态" + mInfo.get(position).di_sw + "---是否在线---" + mInfo.get(position).di_status + "---设备ID---" + mInfo.get(position).db_dt_id);
        // Log.i("qaz", "是否在线" + mInfo.get(0).changestate);
        holder.bind(mInfo.get(position), position ,mInfo.get(0).changestate);
        holder.setIsRecyclable(false);
    }
    @Override
    public int getItemCount() {
        return mInfo == null ? 0 : mInfo.size();
    }
    @Override
    public boolean onInterceptTouchEvent(RecyclerView recyclerView, MotionEvent e) {
        return false;
    }
    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }
    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }
    //自定义ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout imgHomeMore;
        ImageView imgLightClose;
        TextView textLightName, textLightOnoff;
        RelativeLayout telativeHomeLight;
        private String db_sw = "0";
        private String macaddr;
        private int db_sbID;
        private int di_clusterID;
        private String clustername;
        private String di_status;
        private String db_dt_id;
        private boolean change;
        private int positions;
        private List<FragmentHomeShowBean.ListBean> mInfoResult;

        public ViewHolder(View view) {
            super(view);
            //三个点进入详情
            imgHomeMore = (LinearLayout) itemView.findViewById(R.id.img_home_more);
            //设备图标
            imgLightClose = (ImageView) itemView.findViewById(R.id.img_light_close);
            //显示设备开还是关
            textLightOnoff = (TextView) itemView.findViewById(R.id.text_light_onoff);
            //设备名称
            textLightName = (TextView) itemView.findViewById(R.id.text_light_name);
            //显示对应设备
            telativeHomeLight = (RelativeLayout) itemView.findViewById(R.id.telative_home_light);
            telativeHomeLight.setOnClickListener(this);
            //imgLightClose.setOnClickListener(this);
            imgHomeMore.setOnClickListener(this);
        }
        @Override
        public void onClick(View v) {
            if (!CommonUtils.isNetworkAvailable(mContext)) {
                CommonUtils.toastMessage("当前无网络");
                return;
            }
            switch (v.getId()) {
                case R.id.telative_home_light:

                case R.id.img_home_more:
                    if (!change) {
                        CommonUtils.toastMessage("连接服务器失败，请重启APP");
                        return;
                    }
                    String jsonString = gson.toJson(mInfoResult);
                    intent = new Intent(mContext, ActivityFacilityInfo.class);
                    intent.putExtra("jsonString", jsonString);
                    intent.putExtra("clustername", clustername);
                    intent.putExtra("db_sbID", db_sbID);
                    intent.putExtra("db_dt_id", db_dt_id); //di_clusterID
                    intent.putExtra("di_clusterID", di_clusterID);
                    mContext.startActivity(intent);
                    break;
            }
        }
        /**
         * 设置数据
         * @param bean
         * @param position
         * @param
         */
        public void bind(FragmentHomeShowBean bean, int position ,boolean changestate) {
            change = changestate;
            clustername = bean.di_clustername;
            macaddr = bean.dd_macaddr;
            db_dt_id = String.valueOf(bean.db_dt_id);
            db_sbID = bean.di_db_sbID;
            dd_ui_id = bean.dd_ui_id;
            di_clusterID = bean.di_clusterID;
            mInfoResult = bean.list;
            if ("61".equals(db_dt_id)) {  //传感器
                SpTools.setString(BaseApplication.getContext(), Constants.sbid, String.valueOf(bean.di_db_sbID));
            }
            setState(db_dt_id);
        }
        /**
         * 设置显示状态
         * @param db_dt_id
         *
         */
        private void setState(String db_dt_id) {
            telativeHomeLight.setVisibility(View.VISIBLE);
            textLightName.setText(clustername);
            telativeHomeLight.setEnabled(true);
            imgHomeMore.setEnabled(true);
            textLightOnoff.setText("");
            setImage(db_dt_id);
        }

        /**
         * 设置显示图片
         * @param db_dt_id
         * @param
         */
        private void setImage(String db_dt_id) {

            if ("100".equals(db_dt_id)) {
            } else if ("42".equals(db_dt_id)) {  //电视
                    imgLightClose.setImageResource(R.drawable.img_tv_close);
            } else if ("49".equals(db_dt_id)) { //空调
                    imgLightClose.setImageResource(R.drawable.img_air_close);
            } else if ("61".equals(db_dt_id)) { //红外转发器
                imgLightClose.setImageResource(R.drawable.img_body_close);
            } else if ("76".equals(db_dt_id)) { //传感器
                imgLightClose.setImageResource(R.drawable.img_sensor_close);
            } else if ("73".equals(db_dt_id)) { //门锁
                    imgLightClose.setImageResource(R.drawable.img_lock_close);
            } else if ("46".equals(db_dt_id)) {   //空气净化器
                    imgLightClose.setImageResource(R.drawable.img_trend_close);
            } else if ("9".equals(db_dt_id)) {  //窗帘
                    imgLightClose.setImageResource(R.drawable.img_curtain_close);
            } else if ("75".equals(db_dt_id)) {  //煤气阀门
                    imgLightClose.setImageResource(R.drawable.img_valve_close);
            } else if ("78".equals(db_dt_id)) {//灯
                    imgLightClose.setImageResource(R.drawable.img_light_close);
            } else if ("77".equals(db_dt_id)||"79".equals(db_dt_id)) {//定位
                    imgLightClose.setImageResource(R.drawable.img_pattern_dingwei);
            } else if ("7".equals(db_dt_id)) {//摄像头
                    imgLightClose.setImageResource(R.drawable.img_shexiangt);
            } else { //灯
                    imgLightClose.setImageResource(R.drawable.img_light_close);

            }
        }
    }


}



