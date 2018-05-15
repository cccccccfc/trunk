package com.base.project.view.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.activity.myself.ActivityModifyHouseName;
import com.base.project.utils.CommonUtils;
import com.base.utilslibrary.bean.GroupInfoBean;

import java.util.List;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class HouseManagerAdapter extends RecyclerView.Adapter<HouseManagerAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<GroupInfoBean> mDatas;
    private String ub_id;
    private int list_id;
    public interface MyItemClickListener {
        void onItemClick(View v, int position,GroupInfoBean bean);
    }

    public HouseManagerAdapter(Context context, List<GroupInfoBean> datas,String id,int listid) {
        /**
         * datas为"一楼"层面的下的child
         */
        mContext = context;
        mDatas = datas;
        ub_id = id;
        list_id = listid;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_house_manager,null);
        MyHolder myHolder = new MyHolder(view , mItemClickListener);
        return myHolder;
    }

    @Override
    public void onBindViewHolder(MyHolder holder, int position) {
        holder.setRefreshData(mDatas.get(position),position);
    }

    @Override
    public int getItemCount() {
        return mDatas.size();
    }
    public void addDatas(GroupInfoBean bean){
        mDatas.add(bean);
    }
    public void remove(int position) {
        mDatas.remove(position);
        notifyItemRemoved(position);
        if (position != mDatas.size()) {
            notifyItemRangeChanged(position, mDatas.size() - position);
        }
        if(mDatas.size()==1){//剩最后一个的时候刷新，取消删除按钮
            notifyDataSetChanged();
        }
    }

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        private TextView name;
        private ImageView delete;
        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            name = (TextView) itemView.findViewById(R.id.tv_item_house_manager);
            delete = (ImageView) itemView.findViewById(R.id.lv_item_house_delete);
            delete.setOnClickListener(this);
            /**
             * 修改房间名称
             */
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CommonUtils.logMes("----mg_id--"+mDatas.get(getPosition()).getMg_id()+"----"+list_id);
                    Intent intent = new Intent(mContext, ActivityModifyHouseName.class);
                    intent.putExtra("name",mDatas.get(getPosition()).getMg_name());
                    intent.putExtra("ub_id",ub_id);
                    intent.putExtra("mg_id",mDatas.get(getPosition()).getMg_id());
                    intent.putExtra("pos",getPosition());
                    intent.putExtra("list_id",list_id);
                    mContext.startActivity(intent);
                }
            });
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition(),mDatas.get(getPosition()));
            }
        }

        public void setRefreshData(GroupInfoBean bean, int position) {
            if(mDatas.size() == 1){
                delete.setVisibility(View.INVISIBLE);
            }else {
                delete.setVisibility(View.VISIBLE);
            }
            name.setText(bean.mg_name);
        }
    }
}
