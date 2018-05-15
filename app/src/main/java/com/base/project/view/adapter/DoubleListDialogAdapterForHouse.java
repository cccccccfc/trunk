package com.base.project.view.adapter;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.base.project.R;

import java.util.ArrayList;

/**
 * @author IMXU
 * @time 2017/11/8 9:48
 * @des ${TODO}
 * 邮箱：butterfly_xu@sina.com
 */

public class DoubleListDialogAdapterForHouse extends BaseAdapter {
    private Context context;
    private ArrayList<String> datas;

    private int mCurrentItem=0;
    private boolean isClick=false;


    public DoubleListDialogAdapterForHouse(Context context, ArrayList<String> data) {
        this.context = context;
        datas = data;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public Object getItem(int position) {
        return datas.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder = null;
        if(convertView == null){
            holder = new ViewHolder();
            convertView = View.inflate(context, R.layout.item_double_dialog_adapter,null);
            holder.name = (TextView) convertView.findViewById(R.id.tv_item_double_adapter);
            convertView.setTag(holder);
        }else{
            holder = (ViewHolder) convertView.getTag();
        }

        if (mCurrentItem == position&&isClick){
//            parent.setBackgroundColor(Color.parseColor("#3F51B5"));
            holder.name.setTextColor(Color.parseColor("#dc8d04"));
        }else{
//            parent.setBackgroundColor(Color.parseColor("#ffffff"));
            holder.name.setTextColor(Color.parseColor("#666666"));
        }
        holder.name.setText(datas.get(position).split(",")[0]);

        return convertView;
    }
    public void setCurrentItem(int currentItem){
        this.mCurrentItem=currentItem;
    }

    public void setClick(boolean click){
        this.isClick=click;
    }
    class ViewHolder{
        public TextView name;
    }
}
