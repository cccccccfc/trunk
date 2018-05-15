package com.base.project.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.base.project.R;

/**
 * Created by Administrator on 2017/10/31.
 */

public class TextHintAdapter extends RecyclerView.Adapter<TextHintAdapter.ViewHolder> {
    private final String[] mString;
    private Context mContext;

    public TextHintAdapter(String[] string, Context context) {
        this.mString = string;
        this.mContext= context;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_text_hint, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mTv.setText(mString[position]);
    }
    @Override
    public int getItemCount() {
        return mString == null ? 0 : mString.length;

    }
    //自定义ViewHolder
    class ViewHolder  extends RecyclerView.ViewHolder {

        TextView mTv;

        public ViewHolder (View view) {
            super(view);
            mTv = (TextView) itemView.findViewById(R.id.text_hint_item);
        }
    }

}
