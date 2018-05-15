package com.base.project.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.project.R;
import com.base.utilslibrary.bean.AddtaskslistBean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/21.
 */

public class AddTasksAdapter extends RecyclerView.Adapter<AddTasksAdapter.ViewHolder> {

    private final List<AddtaskslistBean> mInfo;

    private final Context mContext;
    private boolean select;
    private boolean[] flag;//此处添加一个boolean类型的数组

    public AddTasksAdapter(List<AddtaskslistBean> info, Context context) {
        this.mInfo = info;
        this.mContext = context;
        flag = new boolean[mInfo.size()];

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_pattern_addtasks, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mInfo.get(position), position);

    }

    @Override
    public int getItemCount() {
        return mInfo.size();
    }

    //自定义ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final CheckBox mSwitch;
        private final LinearLayout mPattern;
        private final TextView mHint;
        ImageView mImage;
        TextView mText;
        private String di_clustername;
        private int di_clusterID;
        private String mg_name;
        private int db_dt_id;
        private int db_sbID;
        private int mg_id;
        private String mg_level;
        private boolean isshow;


        public ViewHolder(View view) {
            super(view);
            mImage = (ImageView) itemView.findViewById(R.id.iv_equipment_open_door);
            mText = (TextView) itemView.findViewById(R.id.tv_equipment_open_text);
            mSwitch = (CheckBox) itemView.findViewById(R.id.img_pattern_switch);
            mHint = (TextView) itemView.findViewById(R.id.iv_equipment_open_doo);
            mPattern = (LinearLayout) itemView.findViewById(R.id.ll_equipment_open);

            mSwitch.setOnClickListener(this);
            //mPattern.setOnClickListener(this);
        }

        public void bind(final AddtaskslistBean addtaskslistBean, final int position) {
            di_clustername = addtaskslistBean.di_clustername;
            di_clusterID = addtaskslistBean.di_clusterID;
            mg_name = addtaskslistBean.mg_name;
            db_dt_id = addtaskslistBean.db_dt_id;
            db_sbID = addtaskslistBean.di_db_sbID;
            mg_id = addtaskslistBean.mg_id;
            mg_level = addtaskslistBean.mg_level;
            isshow = addtaskslistBean.isshow();
            mSwitch.setChecked(isshow);
            // mSwitch.setOnCheckedChangeListener(null);
            mSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    isshow = isChecked;
                    if (isChecked) {
                        select = false;
                        flag[position] = isChecked;
                        Log.i("wsx", "onCheckedChanged: " + isChecked);
                        mInfo.get(getPosition()).setIsshow(isChecked);
//                        addtaskslistBean.setIsshow(isChecked);
                        //notifyDataSetChanged();
                    } else {
                        isshow = isChecked;
                        mInfo.get(getPosition()).setIsshow(isChecked);
//                        addtaskslistBean.setIsshow(isChecked);
                        // notifyDataSetChanged();

                    }

                }
            });
            mText.setText(di_clustername);
            mHint.setText(mg_level + mg_name);
            if ("100".equals(db_dt_id)) {

            } else if ("42".equals(db_dt_id)) {  //电视
                mImage.setImageResource(R.drawable.img_pattern_tv);
            } else if ("49".equals(db_dt_id)) { //空调
                mImage.setImageResource(R.drawable.img_pattern_air);
            } else if ("73".equals(db_dt_id)) { //门锁
                mImage.setImageResource(R.drawable.img_pattern_light);
            } else if ("46".equals(db_dt_id)) {   //空气净化器
                mImage.setImageResource(R.drawable.img_pattern_light);
            } else if ("9".equals(db_dt_id)) {  //窗帘
                mImage.setImageResource(R.drawable.img_pattern_light);
            } else if ("75".equals(db_dt_id)) {  //煤气阀门
                mImage.setImageResource(R.drawable.img_pattern_light);
            } else if ("78".equals(db_dt_id)) {//灯
                mImage.setImageResource(R.drawable.img_pattern_light);
            } else { //灯
                mImage.setImageResource(R.drawable.img_pattern_light);
            }

        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {


            }
        }
    }

    public interface OnStateChangeListener {
        void onStateChange(int ps);
    }

    private static OnStateChangeListener mOnStateChangeListener;


    public static void setOnStateChangeListener(OnStateChangeListener listener) {
        mOnStateChangeListener = listener;
    }
}
