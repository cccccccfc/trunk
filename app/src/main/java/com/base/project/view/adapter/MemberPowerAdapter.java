package com.base.project.view.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.project.R;
import com.base.utilslibrary.bean.ProjectUrlInfo;
import com.base.utilslibrary.bean.UserPowerInfo;
import com.bumptech.glide.Glide;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class MemberPowerAdapter extends RecyclerView.Adapter<MemberPowerAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<UserPowerInfo> mDatas;

    public interface MyItemClickListener {
        void onItemClick(View v, int position,UserPowerInfo info);
    }

    public MemberPowerAdapter(Context context, List<UserPowerInfo> datas) {
        mContext = context;
        mDatas = datas;
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_menber_power,null);
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

    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        private TextView name;
        private TextView phone;
        private ImageView img;
        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            name = (TextView) itemView.findViewById(R.id.tv_item_member_power_name);
            phone = (TextView) itemView.findViewById(R.id.tv_item_member_power_phone);
            img = (ImageView) itemView.findViewById(R.id.iv_item_member_power_img);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition(),mDatas.get(getPosition()));
            }
        }

        public void setRefreshData(UserPowerInfo bean, int position) {
            name.setText(bean.list.getUi_nc());
            String role = "";
            if("2".equals(bean.role)){
                role = "管理员";
            }else if("1".equals(bean.role)){
                role = "管理员";
            }else{
                role = "成员";
            }
            phone.setText(role+":"+bean.list.getUi_phone());
            Glide.with(mContext).load(ProjectUrlInfo.phtot_address +bean.list.getUi_pic())
                    .placeholder(R.drawable.img_default_person)
                    .bitmapTransform(new CropCircleTransformation(mContext))
                    .into(img);
        }
    }
}
