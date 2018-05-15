package com.base.project.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.base.project.R;
import com.base.utilslibrary.bean.SceneListBean;

import java.util.List;

import static com.base.project.R.id.img_pattern_switch;
import static com.base.project.R.id.ll_pattern_background;

/**
 * Created by Administrator on 2017/11/20.
 */

public class ListToSceneAdapter extends RecyclerView.Adapter<ListToSceneAdapter.ViewHolder> {

    private final List<SceneListBean> mInfo;
    private final Context mContext;
    private final String mRole;
    private boolean[] flag;//此处添加一个boolean类型的数组

    public ListToSceneAdapter(List<SceneListBean> info, Context context, String role) {
        this.mInfo = info;
        this.mContext = context;
        this.mRole = role;
        flag = new boolean[mInfo.size()];
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(mContext).inflate(R.layout.item_pattern_model, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(mInfo.get(position), position ,mRole);
       // Log.i("qaz", "数据" + mInfo.get(position).getMs_name());
    }

    @Override
    public int getItemCount() {
        return mInfo.size();
    }

    //自定义ViewHolder
    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private final TextView mSwitch;
        private final LinearLayout mPattern;
        ImageView mImage;
        TextView mText;
        private boolean isshow;
        private boolean state;
        private String type;
        private String name;
        private int positions;
        private String id;

        public ViewHolder(View view) {
            super(view);
            mImage = (ImageView) itemView.findViewById(R.id.iv_pattern_image);
            mText = (TextView) itemView.findViewById(R.id.tv_pattern_text);
            mSwitch = (TextView) itemView.findViewById(img_pattern_switch);
            mPattern = (LinearLayout) itemView.findViewById(ll_pattern_background);
            mSwitch.setOnClickListener(this);
            mPattern.setOnClickListener(this);
        }

        public void bind(SceneListBean sceneListBean, final int position, String mRole) {
           // Log.i("wsx", "bind: " +mRole);

            positions = position;
            isshow = sceneListBean.isshow;
            type = sceneListBean.getMs_type();
            name = sceneListBean.getMs_name();
            id = sceneListBean.getMs_id();
            if (isshow) {
                mPattern.setEnabled(false);
                mSwitch.setVisibility(View.VISIBLE);
                flag[position] = isshow;
                state = isshow; // 判断checkbox 状态
            }else{
                mPattern.setEnabled(true);
                mSwitch.setVisibility(View.GONE);
            }
            mPattern.setVisibility(View.VISIBLE);
            mText.setText(name);
            if ("0".equals(mRole)) {
                mPattern.setEnabled(false);
            }else{
                mPattern.setEnabled(true);
            }
            if ("1".equals(type)) { //影音
                mImage.setImageResource(R.drawable.img_pattern_video);
            } else if ("2".equals(type)) {  //开灯
                mImage.setImageResource(R.drawable.img_pattern_openlight);
            } else if ("3".equals(type)) { //就餐
                mImage.setImageResource(R.drawable.img_pattern_repast);
            } else if ("4".equals(type)) { //娱乐
                mImage.setImageResource(R.drawable.img_pattern_recrr);
            } else if ("5".equals(type)) {   //休闲
                mImage.setImageResource(R.drawable.img_pattern_relx);
            } else if ("6".equals(type)) {  //睡眠
                mImage.setImageResource(R.drawable.img_pattern_sleep);
            } else if ("7".equals(type)) {  //起床
                mImage.setImageResource(R.drawable.img_pattern_getup);
            } else if ("8".equals(type)) {//在家
                mImage.setImageResource(R.drawable.img_pattern_home);
            } else if ("9".equals(type)) {//外出
                mImage.setImageResource(R.drawable.img_pattern_goout);
            } else { //关灯
                mImage.setImageResource(R.drawable.img_pattern_closelight);
            }
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case ll_pattern_background:
                    // 开启模式
                    //Log.i("qaz", "准备控制情景模式");
                    mText.setText(name+"  开启中。。");
                    mPattern.setEnabled(false);
                    if (mOnCtrlClickListener != null) {      //getTag获取的即是点击位置
                        mOnCtrlClickListener.onCtrlClick(v, positions ,type ,id);
                    }
                    break;
                case img_pattern_switch:
                    if (state) {
                        //选中 跳转详情
                        if (mOnPatternClickListener != null) {      //getTag获取的即是点击位置
                            mOnPatternClickListener.onPatternClick(v, positions ,type ,id);
                        }
                    }

                    break;
            }
        }
    }

    public interface OnPatternClickListener {

        void onPatternClick(View view, int position, String type, String id);
    }

    private static OnPatternClickListener mOnPatternClickListener;


    public static void setOnPatternClickListener(OnPatternClickListener listener) {
        mOnPatternClickListener = listener;
    }
    public interface OnCtrlClickListener {

        void onCtrlClick(View view, int position, String type, String id);
    }

    private static OnCtrlClickListener mOnCtrlClickListener;


    public static void setOnCtrlClickListener(OnCtrlClickListener listener) {
        mOnCtrlClickListener = listener;
    }
}
