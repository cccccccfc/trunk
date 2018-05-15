package com.base.project.view.adapter;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.activity.myself.ActivityHouseManager;
import com.base.project.activity.myself.ActivityModifyHouseName;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.Constants;
import com.base.project.utils.SpTools;
import com.base.utilslibrary.bean.GroupInfoBean;
import com.base.utilslibrary.internet.PersonalHomeManagerRequestUtils;

import java.util.List;

/**
 * @author Admin
 * @time 2017/3/29 9:37
 * @des ${TODO}
 */

public class FlourManagerAdapter extends RecyclerView.Adapter<FlourManagerAdapter.MyHolder> {
    private Context mContext;
    private MyItemClickListener mItemClickListener = null;
    private List<GroupInfoBean> mDatas;
    private String ubID;

    public interface MyItemClickListener {
        void onItemClick(View v, int position);
    }

    public FlourManagerAdapter(Context context, List<GroupInfoBean> datas, String ub_id) {
        mContext = context;
        mDatas = datas;
        ubID = ub_id;
        /**
         * 创建房间的回调
         */
        ActivityHouseManager.setFlourAddHouseListener(new ActivityHouseManager.FlourAddHouseListener() {
            @Override
            public void onResult(String name, final int position) {
                PersonalHomeManagerRequestUtils.addHouse(mContext,ubID, mDatas.get(position).mg_parent_id_dir + "," + mDatas.get(position).mg_id
                        , name, new PersonalHomeManagerRequestUtils.ForResultDataListener() {
                            @Override
                            public void onResponseMessage(GroupInfoBean info) {
                                if (info != null) {
                                    //房间管理变动的时候(删除或增加楼层或房间),通知主页更新信息，暂时先用sp通知
                                    SpTools.setBoolean(CommonUtils.getContext(), Constants.housechange,true);
                                    mDatas.get(position).child.add(info);
                                    notifyDataSetChanged();
                                } else {
                                    CommonUtils.toastMessage("创建房间失败，请重试");
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
                CommonUtils.logMes("----mg_id--"+mDatas.get(list_id).child.get(pos).getMg_id()+"----"+list_id);
                mDatas.get(list_id).child.get(pos).setMg_name(name);
                notifyDataSetChanged();
            }
        });
    }

    public void setOnItemClickListener(MyItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    @Override
    public MyHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = View.inflate(mContext, R.layout.item_flour_manager,null);
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
    class MyHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        private TextView add;
        private TextView name;
        private RecyclerView mRecyclerView;
        private ImageView iv_up;
        private ImageView iv_down;
        private RelativeLayout rl_hide;//隐藏楼层下面的房间信息
        private LinearLayout linearLayout;
        private LinearLayout ll_delete;
        private boolean isHide = true;//隐藏楼层下面的房间信息，默认为隐藏

        HouseManagerAdapter houseManagerAdapter;
        private GridLayoutManager manager;

        public MyHolder(View itemView, MyItemClickListener listener) {
            super(itemView);
            this.mListener = listener;
            add = (TextView) itemView.findViewById(R.id.tv_item_add_house_manager);
            name = (TextView) itemView.findViewById(R.id.tv_item_flour_name);
            mRecyclerView = (RecyclerView) itemView.findViewById(R.id.rv_item_flour_manager);
            iv_up = (ImageView) itemView.findViewById(R.id.iv_item_flour_up);
            iv_down = (ImageView) itemView.findViewById(R.id.iv_item_flour_down);
            rl_hide = (RelativeLayout) itemView.findViewById(R.id.rl_item_flour_manager);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.ll_item_flour_manager);
            ll_delete = (LinearLayout) itemView.findViewById(R.id.ll_item_flour_manager_delete);

            rl_hide.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(mDatas.get(getPosition()).isIdHide()){
                        if(getPosition() == 0){
                            ll_delete.setVisibility(View.GONE);
                        }else {
                            ll_delete.setVisibility(View.VISIBLE);
                        }
                        linearLayout.setVisibility(View.VISIBLE);
                        iv_down.setVisibility(View.INVISIBLE);
                        iv_up.setVisibility(View.VISIBLE);
                    }else {
                        linearLayout.setVisibility(View.GONE);
                        ll_delete.setVisibility(View.GONE);
                        iv_up.setVisibility(View.INVISIBLE);
                        iv_down.setVisibility(View.VISIBLE);
                    }
                    isHide = !isHide;
                    mDatas.get(getPosition()).setIdHide(isHide);
                }
            });
            /**
             * 删除楼层
             */
            ll_delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    PersonalHomeManagerRequestUtils.deleteGroup(mContext,ubID, mDatas.get(getPosition()).getMg_id(), new PersonalHomeManagerRequestUtils.ForResultListener() {
                        @Override
                        public void onResponseMessage(String code) {
                            if("成功".equals(code)){
                                //房间管理变动的时候(删除或增加楼层或房间),通知主页更新信息，暂时先用sp通知
                                SpTools.setBoolean(CommonUtils.getContext(), Constants.housechange,true);
                                mDatas.remove(getPosition());
                                notifyItemRemoved(getPosition());
                                if (getPosition() != mDatas.size()) {
                                    notifyItemRangeChanged(getPosition(), mDatas.size() - getPosition());
                                }
                                CommonUtils.toastMessage("删除楼层成功");
                            }else {
                                CommonUtils.toastMessage("删除失败，请重试");
                            }
                        }
                    });
                }
            });
            add.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }

        public void setRefreshData(GroupInfoBean bean, int position) {
            if(position == 0){
                ll_delete.setVisibility(View.GONE);
            }else {
                ll_delete.setVisibility(View.VISIBLE);
            }

            if(!bean.isIdHide()){
                if(getPosition() == 0){
                    ll_delete.setVisibility(View.GONE);
                }else {
                    ll_delete.setVisibility(View.VISIBLE);
                }
                linearLayout.setVisibility(View.VISIBLE);
                iv_down.setVisibility(View.INVISIBLE);
                iv_up.setVisibility(View.VISIBLE);
            }else {
                linearLayout.setVisibility(View.GONE);
                ll_delete.setVisibility(View.GONE);
                iv_up.setVisibility(View.INVISIBLE);
                iv_down.setVisibility(View.VISIBLE);
            }

            if(manager == null){
                manager = new GridLayoutManager(mContext, 2);
            }
            mRecyclerView.setLayoutManager(manager);
            houseManagerAdapter = new HouseManagerAdapter(mContext, bean.child,ubID,getPosition());
            mRecyclerView.setAdapter(houseManagerAdapter);
            houseManagerAdapter.setOnItemClickListener(new HouseManagerAdapter.MyItemClickListener() {
                @Override
                public void onItemClick(View v, final int position, GroupInfoBean bean) {
                    /**
                     * 删除房间
                     */
                    PersonalHomeManagerRequestUtils.deleteGroup(mContext,ubID, bean.getMg_id(), new PersonalHomeManagerRequestUtils.ForResultListener() {
                        @Override
                        public void onResponseMessage(String code) {
                            if ("成功".equals(code)) {
                                //房间管理变动的时候(删除或增加楼层或房间),通知主页更新信息，暂时先用sp通知
                                SpTools.setBoolean(CommonUtils.getContext(), Constants.housechange,true);
                                houseManagerAdapter.remove(position);
                                CommonUtils.toastMessage("删除房间成功");
                            } else {
                                CommonUtils.toastMessage("删除失败，请重试");
                            }
                        }
                    });
                }
            });
            name.setText(bean.mg_name);
        }
    }
}
