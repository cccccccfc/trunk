package com.base.project.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.project.R;
import com.base.utilslibrary.bean.ProjectUrlInfo;
import com.base.utilslibrary.bean.RecordListBean;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

/**
 * Created by Administrator on 2017/11/30.
 */

public class RecordListAdapter extends RecyclerView.Adapter<RecordListAdapter.ViewHolder> {

    private final List<RecordListBean> mInfo;
    private final Context mContext;

    public RecordListAdapter(List<RecordListBean> info, Context context) {
        this.mInfo = info;
        this.mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_alarm_images, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mInfo.get(position), position );
    }

    @Override
    public int getItemCount() {
        return mInfo.size();
    }

    //自定义ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        ImageView mImage;
        TextView mText;
        public ViewHolder(View itemView) {
            super(itemView);

            mImage = (ImageView) itemView.findViewById(R.id.iv_alarm_images);
            mText = (TextView) itemView.findViewById(R.id.tv_alarm_images);
        }

        @Override
        public void onClick(View v) {

        }

        public void bind(RecordListBean recordListBean, int position) {
              String  url = ProjectUrlInfo.phtot_address+recordListBean.getPic();
          //  Log.i("qaz", "bind: " +url);
            String time = recordListBean.getCi_time(); // BitmapFactory.decodeFile
            Glide.with(mContext).load(url).diskCacheStrategy(DiskCacheStrategy.ALL).into(mImage);
            mText.setText(time);
        }
    }
}
