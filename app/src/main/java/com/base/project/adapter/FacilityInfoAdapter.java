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
import com.base.project.activity.facility.ActivityCameraInfo;
import com.base.project.activity.facility.ActivityControlAir;
import com.base.project.activity.facility.ActivityControlBody;
import com.base.project.activity.facility.ActivityControlCurtain;
import com.base.project.activity.facility.ActivityControlLight;
import com.base.project.activity.facility.ActivityControlLock;
import com.base.project.activity.facility.ActivityControlTrend;
import com.base.project.activity.facility.ActivityControlTv;
import com.base.project.activity.facility.ActivityControlValve;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.Constants;
import com.base.project.utils.SpTools;
import com.base.utilslibrary.bean.FragmentHomeShowBean;

import java.util.List;

import static com.base.project.activity.voice.OperatingDevice.operatingDeviceHand;

/**
 * Created by Administrator on 2017/12/14.
 */

public class FacilityInfoAdapter extends RecyclerView.Adapter<FacilityInfoAdapter.ViewHolder> implements RecyclerView.OnItemTouchListener {
    private List<FragmentHomeShowBean.ListBean> mInfo;
    private Context mContext;
    private Intent intent;
    private int dd_ui_id;

    public FacilityInfoAdapter(List<FragmentHomeShowBean.ListBean> info, Context instance) {
        this.mInfo = info;
        this.mContext = instance;
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
       // Log.i("qaz", "开关状态" + mInfo.get(position).di_sw + "---是否在线---" + mInfo.get(position).di_status + "---设备ID---" + mInfo.get(position).db_dt_id);
        // Log.i("qaz", "是否在线" + mInfo.get(0).changestate);
        holder.bind(mInfo.get(position), position, mInfo.get(0).changestate);
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


                    if (!change) {
                        CommonUtils.toastMessage("连接服务器失败，请重启APP");
                        return;
                    }
                    if ("100".equals(db_dt_id)) {

                    } else if ("42".equals(db_dt_id)) {  //电视
                        if (mOnControlStateListener != null) {
                            mOnControlStateListener.onControlState("1");
                        }
                        if ("0".equals(db_sw)) {
                           // Log.i("qaz", "onClick:电视 " +db_sw);
                            operatingDeviceHand(0, 0, Integer.parseInt(SpTools.getString(mContext, Constants.sbid, "")), macaddr, di_clusterID, 0, dd_ui_id);
                           // textLightOnoff.setText("正在控制，请稍候");
                            ctrlDevice(db_sw);
                           // telativeHomeLight.setEnabled(false);
                        } else {
                            operatingDeviceHand(0, 1, Integer.parseInt(SpTools.getString(mContext, Constants.sbid, "")), macaddr, di_clusterID, 0, dd_ui_id);
                          //  textLightOnoff.setText("正在控制，请稍候");
                            ctrlDevice(db_sw);
                            //telativeHomeLight.setEnabled(false);
                        }

                    } else if ("49".equals(db_dt_id)) { //空调
                        if (mOnControlStateListener != null) {
                            mOnControlStateListener.onControlState("1");
                        }
                     //   Log.i("qaz", "onClick:空调 " +db_sw);
                        if ("0".equals(db_sw)) {
                            operatingDeviceHand(1, 0, Integer.parseInt(SpTools.getString(mContext, Constants.sbid, "")), macaddr, di_clusterID, 0, dd_ui_id);
                            ctrlDevice(db_sw);
                        } else {
                            operatingDeviceHand(1, 1, Integer.parseInt(SpTools.getString(mContext, Constants.sbid, "")), macaddr, di_clusterID, 0, dd_ui_id);
                            ctrlDevice(db_sw);
                        }

                    } else if ("73".equals(db_dt_id)) { //门锁
                        if (mOnControlStateListener != null) {
                            mOnControlStateListener.onControlState("1");
                        }
                        if ("0".equals(db_sw)) {
                            operatingDeviceHand(3, 0, db_sbID, macaddr, di_clusterID, 0, dd_ui_id);
                            textLightOnoff.setText("正在控制，请稍候");
                            telativeHomeLight.setEnabled(false);
                        } else {
                            imgLightClose.setImageResource(R.drawable.img_lock_close);
                        }
                    } else if ("46".equals(db_dt_id)) {   //空气净化器
                        if ("0".equals(db_sw)) {
                            operatingDeviceHand(11, 0, db_sbID, macaddr, di_clusterID, 0, dd_ui_id);
                            ctrlDevice(db_sw);
                        } else {
                            operatingDeviceHand(11, 1, db_sbID, macaddr, di_clusterID, 0, dd_ui_id);
                            ctrlDevice(db_sw);
                        }

                    } else if ("9".equals(db_dt_id)) {  //窗帘
                        if ("0".equals(db_sw)) {
                            operatingDeviceHand(2, 0, db_sbID, macaddr, di_clusterID, 0, dd_ui_id);
                            ctrlDevice(db_sw);
                        } else {
                            operatingDeviceHand(2, 1, db_sbID, macaddr, di_clusterID, 0, dd_ui_id);
                            ctrlDevice(db_sw);
                        }

                    } else if ("75".equals(db_dt_id)) {  //煤气阀门
                        if (mOnControlStateListener != null) {
                            mOnControlStateListener.onControlState("1");
                        }

                        if ("0".equals(db_sw)) {
                            operatingDeviceHand(11, 0, db_sbID, macaddr, di_clusterID, 0, dd_ui_id);
                            ctrlDevice(db_sw);
                        } else {
                            operatingDeviceHand(11, 1, db_sbID, macaddr, di_clusterID, 0, dd_ui_id);
                            ctrlDevice(db_sw);
                        }
                    } else if ("78".equals(db_dt_id)) {//灯
                        if (mOnControlStateListener != null) {
                            mOnControlStateListener.onControlState("1");
                        }

                        if ("0".equals(db_sw)) {
                            operatingDeviceHand(4, 0, db_sbID, macaddr, di_clusterID, 0, dd_ui_id);
                            ctrlDevice(db_sw);
                        } else {
                            operatingDeviceHand(4, 1, db_sbID, macaddr, di_clusterID, 0, dd_ui_id);
                            ctrlDevice(db_sw);
                        }
                    } else if ("77".equals(db_dt_id) || "79".equals(db_dt_id)) {  //定位
                        intent = new Intent(mContext, ActivityControlBody.class);
                        intent.putExtra("clustername", clustername);
                        intent.putExtra("macaddr", macaddr);
                        intent.putExtra("db_sbID", db_sbID);
                        intent.putExtra("di_clusterID", di_clusterID);
                        intent.putExtra("db_sw", db_sw);
                        intent.putExtra("di_status", di_status);
                        intent.putExtra("dd_ui_id", dd_ui_id);
                        mContext.startActivity(intent);
                    } else if ("7".equals(db_dt_id)) {//摄像头
                        imgLightClose.setImageResource(R.drawable.img_shexiangt);
                        textLightOnoff.setText("");
                        intent = new Intent(mContext, ActivityCameraInfo.class);
                        intent.putExtra("db_sbID", db_sbID);
                        mContext.startActivity(intent);

                    } else { //灯
                        if ("0".equals(db_sw)) {
                            operatingDeviceHand(di_clusterID + 3, 0, db_sbID, macaddr, di_clusterID, 0, dd_ui_id);
                            ctrlDevice(db_sw);
                        } else {
                            operatingDeviceHand(di_clusterID + 3, 1, db_sbID, macaddr, di_clusterID, 0, dd_ui_id);
                            ctrlDevice(db_sw);
                        }
                    }
                    break;
                case R.id.img_home_more:
                    if (!change) {
                        CommonUtils.toastMessage("连接服务器失败，请稍后再试");
                        return;
                    }
                    putIntent();
                    break;
            }
        }

        /**
         * 跳转详情页
         */
        private void putIntent() {
            if ("100".equals(db_dt_id)) {
            } else if ("42".equals(db_dt_id)) {  //电视
                intent = new Intent(mContext, ActivityControlTv.class);
            } else if ("49".equals(db_dt_id)) { //空调
                intent = new Intent(mContext, ActivityControlAir.class);
            } else if ("73".equals(db_dt_id)) { //门锁
                intent = new Intent(mContext, ActivityControlLock.class);
            } else if ("46".equals(db_dt_id)) {   //空气净化器
                intent = new Intent(mContext, ActivityControlTrend.class);
            } else if ("9".equals(db_dt_id)) {  //窗帘
                intent = new Intent(mContext, ActivityControlCurtain.class);
            } else if ("75".equals(db_dt_id)) {  //煤气阀门
                intent = new Intent(mContext, ActivityControlValve.class);
            } else if ("77".equals(db_dt_id) || "79".equals(db_dt_id)) {  //定位
                intent = new Intent(mContext, ActivityControlBody.class);
            } else if ("78".equals(db_dt_id)) {//灯
                intent = new Intent(mContext, ActivityControlLight.class);
            } else if ("7".equals(db_dt_id)) {//摄像头
                intent = new Intent(mContext, ActivityCameraInfo.class);
            } else { //灯
                intent = new Intent(mContext, ActivityControlLight.class);
            }
            intent.putExtra("clustername", clustername);
            intent.putExtra("macaddr", macaddr);
            intent.putExtra("db_sbID", db_sbID);
            intent.putExtra("di_clusterID", di_clusterID);
            intent.putExtra("db_sw", db_sw);
            intent.putExtra("di_status", di_status);
            intent.putExtra("dd_ui_id", dd_ui_id);
            mContext.startActivity(intent);
        }

        /**
         * 显示控制状态
         *
         * @param db_sw
         */
        private void ctrlDevice(String db_sw) {
            if ("0".equals(db_sw)) {
                textLightOnoff.setText("正在打开，请稍候");
                telativeHomeLight.setEnabled(false);
            } else {
                textLightOnoff.setText("正在关闭，请稍候");
                telativeHomeLight.setEnabled(false);
            }
        }

        /**
         * 设置数据
         *
         * @param bean
         * @param position
         * @param changestate
         */
        public void bind(FragmentHomeShowBean.ListBean bean, int position, boolean changestate) {
            positions = position;
            change = changestate;
            clustername = bean.di_clustername;
            di_clusterID = bean.di_clusterID;
            macaddr = bean.dd_macaddr;
            db_dt_id = String.valueOf(bean.db_dt_id);
            db_sbID = bean.dd_db_sbID;
            db_sw = bean.di_sw;
          //  Log.i("qaz", "bind:adapter db_sw" + db_sw);
            di_status = String.valueOf(bean.di_status);
            dd_ui_id = bean.dd_ui_id;
            if ("100".equals(db_dt_id)) {
            } else if ("42".equals(db_dt_id)) {  //电视
                setState(db_dt_id, db_sw, di_status);
            } else if ("49".equals(db_dt_id)) { //空调
                setState(db_dt_id, db_sw, di_status);
            } else if ("73".equals(db_dt_id)) { //门锁
                setState(db_dt_id, db_sw, di_status);
            } else if ("46".equals(db_dt_id)) {   //空气净化器
                setState(db_dt_id, db_sw, di_status);
            } else if ("9".equals(db_dt_id)) {  //窗帘
                setState(db_dt_id, db_sw, di_status);
            } else if ("75".equals(db_dt_id)) {  //煤气阀门
                setState(db_dt_id, db_sw, di_status);
            } else if ("78".equals(db_dt_id)) {//灯
                setState(db_dt_id, db_sw, di_status);
            } else if ("7".equals(db_dt_id)) {//摄像头
                setState(db_dt_id, db_sw, di_status);
            } else { //灯
                setState(db_dt_id, db_sw, di_status);
            }
        }

        /**
         * 设置显示状态
         *
         * @param db_dt_id
         * @param db_sw
         * @param di_status
         */
        private void setState(String db_dt_id, String db_sw, String di_status) {
            telativeHomeLight.setVisibility(View.VISIBLE);
            textLightName.setText(clustername);

            if ("1".equals(di_status)) {
                telativeHomeLight.setEnabled(true);
                imgHomeMore.setEnabled(true);
                textLightOnoff.setText("关");
                if ("0".equals(db_sw)) {
                    textLightOnoff.setText("关");
                } else {
                    textLightOnoff.setText("开");
                }
                setImage(db_dt_id, db_sw);
            } else if ("2".equals(di_status)) {
                textLightOnoff.setText("在线");
                telativeHomeLight.setEnabled(true);
                imgHomeMore.setEnabled(true);
                setImage(db_dt_id, db_sw);
            } else if ("3".equals(di_status)) {
                textLightOnoff.setText("");
                telativeHomeLight.setEnabled(true);
                imgHomeMore.setEnabled(true);
                setImage(db_dt_id, db_sw);
            } else {
                telativeHomeLight.setEnabled(false);
                textLightOnoff.setText("离线");
                imgHomeMore.setEnabled(false);
                setImage(db_dt_id, db_sw);
            }
        }

        /**
         * 设置显示图片
         *
         * @param db_dt_id
         * @param db_sw
         */
        private void setImage(String db_dt_id, String db_sw) {


            if ("100".equals(db_dt_id)) {

            } else if ("42".equals(db_dt_id)) {  //电视
                //// textLightOnoff.setText("在线");
                if ("0".equals(db_sw)) {
                    imgLightClose.setImageResource(R.drawable.img_tv_close);
                } else {
                    imgLightClose.setImageResource(R.drawable.img_tv_open);
                }
            } else if ("49".equals(db_dt_id)) { //空调
                if ("0".equals(db_sw)) {
                    imgLightClose.setImageResource(R.drawable.img_air_close);
                } else {
                    imgLightClose.setImageResource(R.drawable.img_air_open);
                }
            } else if ("76".equals(db_dt_id)) { //传感器
                imgLightClose.setImageResource(R.drawable.img_sensor_close);
                telativeHomeLight.setEnabled(false);
                imgHomeMore.setEnabled(false);
            } else if ("73".equals(db_dt_id)) { //门锁
                textLightOnoff.setText("在线");
                if ("0".equals(db_sw)) {
                    imgLightClose.setImageResource(R.drawable.img_lock_close);
                } else {
                    imgLightClose.setImageResource(R.drawable.img_lock_open);
                }
            } else if ("46".equals(db_dt_id)) {   //空气净化器
                if ("0".equals(db_sw)) {
                    imgLightClose.setImageResource(R.drawable.img_trend_close);
                } else {
                    imgLightClose.setImageResource(R.drawable.img_trend_open);
                }
            } else if ("9".equals(db_dt_id)) {  //窗帘
                if ("0".equals(db_sw)) {
                    imgLightClose.setImageResource(R.drawable.img_curtain_close);
                } else {
                    imgLightClose.setImageResource(R.drawable.img_curtain_open);
                }
            } else if ("75".equals(db_dt_id)) {  //煤气阀门
                if ("0".equals(db_sw)) {
                    imgLightClose.setImageResource(R.drawable.img_valve_close);
                } else {
                    imgLightClose.setImageResource(R.drawable.img_valve_open);
                }
            } else if ("78".equals(db_dt_id)) {//灯
                if ("0".equals(db_sw)) {
                    imgLightClose.setImageResource(R.drawable.img_light_close);
                } else {
                    imgLightClose.setImageResource(R.drawable.img_light_open);
                }
            } else if ("77".equals(db_dt_id) || "79".equals(db_dt_id)) {//定位
                if ("0".equals(db_sw)) {
                    imgLightClose.setImageResource(R.drawable.img_pattern_dingwei);
                } else {
                    imgLightClose.setImageResource(R.drawable.img_pattern_dingwei);
                }
            } else if ("7".equals(db_dt_id)) {//摄像头

                if ("1".equals(db_sw)) {
                    textLightOnoff.setText("");
                    imgLightClose.setImageResource(R.drawable.img_baojingtupian);
                } else {
                    imgLightClose.setImageResource(R.drawable.img_shexiangt);
                }

            } else { //灯
                if ("0".equals(db_sw)) {
                    imgLightClose.setImageResource(R.drawable.img_light_close);
                } else {
                    imgLightClose.setImageResource(R.drawable.img_light_open);
                }
            }
        }
    }
        //   ControlState
    public interface OnControlStateListener {
        void onControlState(String string);
    }
    private static OnControlStateListener mOnControlStateListener;
    public static void setOnControlStateListener(OnControlStateListener listener) {
        mOnControlStateListener = listener;
    }
}
