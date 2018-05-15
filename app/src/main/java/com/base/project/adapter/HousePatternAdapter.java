package com.base.project.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.project.R;
import com.base.utilslibrary.bean.AddtaskslistBean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/21.
 */

public class HousePatternAdapter extends RecyclerView.Adapter<HousePatternAdapter.ViewHolder> {


    private final List<AddtaskslistBean> mInfo;
    private final Context mContext;
    private  String[] time;
    private  String[] onofftype;
    public HousePatternAdapter(List<AddtaskslistBean> info, Context context) {

        this.mInfo = info;
        this.mContext = context;
        if (null ==time) {
            time = new String[mInfo.size()];
        }
        if (null ==onofftype) {
            onofftype = new String[mInfo.size()];
        }

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_pattern_house, parent,false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mInfo.get(position) , position);
      //  Log.i("wsx", "传入的数据长度 " + mInfo.size());
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return mInfo.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        private final TextView tvPatternText;
        private final ImageView imgPatternLajit;
        private final TextView tvTimeText;
        private final RelativeLayout relativeTimeText;
        private final TextView tvOnoffText;
        private final RelativeLayout relativeOnoffText;
        private String di_clustername;
        private int di_clusterID;
        private String mg_name;
        private int db_dt_id;
        private int db_sbID;
        private int mg_id;
        private String mg_level;
        private int positions;
        private String select_time;
        private int onoff;


        public ViewHolder(View view) {
            super(view);
            tvPatternText = (TextView) itemView.findViewById(R.id.tv_pattern_text);
            imgPatternLajit = (ImageView) itemView.findViewById(R.id.img_pattern_lajit);
            tvTimeText = (TextView) itemView.findViewById(R.id.tv_time_text);
            relativeTimeText  = (RelativeLayout) itemView.findViewById(R.id.relative_time_text);
            tvOnoffText = (TextView) itemView.findViewById(R.id.tv_onoff_text);
            relativeOnoffText = (RelativeLayout) itemView.findViewById(R.id.relative_onoff_text);
            imgPatternLajit.setOnClickListener(this);
            relativeOnoffText.setOnClickListener(this);
            relativeTimeText.setOnClickListener(this);

        }

        public void bind(AddtaskslistBean addtaskslistBean, final int position) {
            positions = position;
            di_clustername = addtaskslistBean.di_clustername;
            di_clusterID = addtaskslistBean.di_clusterID;
            mg_name = addtaskslistBean.mg_name;
            db_dt_id = addtaskslistBean.db_dt_id;
            db_sbID = addtaskslistBean.di_db_sbID;
            mg_id = addtaskslistBean.mg_id;
            mg_level = addtaskslistBean.mg_level;
            onoff = addtaskslistBean.getOn_off();
            //Log.i("qaz", "bind: 位置" + position);
           // Log.i("qaz", "bind: 数组长度" + onofftype.length );
            if (0 !=onoff) {
                if (5 == onoff) {
                    onofftype[position] = "开";
                    tvOnoffText.setText(onofftype[position]);

                }else if(6 == onoff){
                    onofftype[position] = "关";
                    tvOnoffText.setText(onofftype[position]);
                }
            }else {
                tvOnoffText.setText("开还是关");
            }
            select_time = addtaskslistBean.getSelect_time();
          //  Log.i("wsx", "获取的时间" + select_time);
//            CommonUtils.logMes("----select_time-----"+select_time+"----position----"+position);
            if (!TextUtils.isEmpty(select_time)) {
                time[position] = select_time;
                tvTimeText.setText(time[position]+"秒");
            }
            tvPatternText.setText(mg_level+mg_name+"设置"+di_clustername);

        }

        @Override
        public void onClick(View v) {
         //   Log.i("wsx", "onClick: " +v.getId());
            switch (v.getId()) {

                case R.id.img_pattern_lajit:

                    mInfo.remove(positions);
                    notifyItemRemoved(positions);
                    notifyItemRangeChanged(positions, mInfo.size());

                    break;
                case R.id.relative_time_text:
                    if (mOnItemClickListener != null) {      //getTag获取的即是点击位置
                        mOnItemClickListener.onItemClick(v , positions);
                    }
                    break;
                case R.id.relative_onoff_text:
                    if (mOnSexClickListener != null) {      //getTag获取的即是点击位置
                        mOnSexClickListener.onSexClick(v , positions);
                    }

                    break;
            }
        }

    }

    public interface OnItemClickListener {

        void onItemClick(View view, int position);
    }

    private static OnItemClickListener mOnItemClickListener;


    public static void setOnItemClickListener(OnItemClickListener listener) {
        mOnItemClickListener = listener;
    }

    public interface OnSexClickListener {

        void onSexClick(View view, int position);
    }

    private static OnSexClickListener mOnSexClickListener;


    public static void setOnSexClickListener(OnSexClickListener listener) {
        mOnSexClickListener = listener;
    }
}
