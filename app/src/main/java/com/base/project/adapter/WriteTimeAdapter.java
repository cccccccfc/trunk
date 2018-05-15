package com.base.project.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.project.R;
import com.base.utilslibrary.bean.HistoryListBean;

import java.util.List;

/**
 * Created by Administrator on 2017/11/2.
 */

public class WriteTimeAdapter extends RecyclerView.Adapter<WriteTimeAdapter.ViewHolder>{

    private final Context mContext;
    private final List<HistoryListBean> mInfo;

    public WriteTimeAdapter(List<HistoryListBean> info, Context context) {
        this.mInfo = info;
        this.mContext= context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_write_time, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mTime.setText(mInfo.get(position).dhh_time);
        holder.mOnoff.setText(mInfo.get(position).mh_memo);
    }

    @Override
    public int getItemCount() {

        return mInfo== null ? 0 : mInfo.size();
    }
    //自定义ViewHolder
    class ViewHolder  extends RecyclerView.ViewHolder {

        TextView mTime;
        TextView mOnoff;

        public ViewHolder (View view) {
            super(view);
            mTime = (TextView) itemView.findViewById(R.id.item_text_time);
            mOnoff = (TextView) itemView.findViewById(R.id.item_text_onoff);
        }
    }
}
