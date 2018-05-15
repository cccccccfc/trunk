package com.base.project.view;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.view.adapter.DoubleListDialogAdapter;
import com.base.project.view.adapter.DoubleListDialogAdapterForHouse;

import java.util.ArrayList;

/**
 * @author IMXU
 * @time 2017/11/8 9:41
 * @des ${TODO}
 * 邮箱：butterfly_xu@sina.com
 */

public class DoubleListDialog extends Dialog {
    private Context context;
    private TextView mTvTitle1;//标题一
    private TextView mTvTitle2;//标题二
    private ListView mLvList1;
    private ListView mLvList2;
    public static View partOne;//listview的item

    private ArrayList<String> listData1;//list数据一
    private ArrayList<ArrayList<String>> AlllistData2;//list数据二
    private ArrayList<String> listData2=new ArrayList<String>();//list数据二
    private String data1;//选择的数据一
    private String data2;//选择的数据二

    private CallBack callBack;//点击后的数据返回

    public DoubleListDialog(Context context,ArrayList<String> listData1,ArrayList<ArrayList<String>> houseInfo) {
        super(context);
        this.context=context;
        this.listData1=listData1;
        this.AlllistData2=houseInfo;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // TODO Auto-generated method stub
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        initView();
    }
    public void setCallBackListener(CallBack callBack){

        this.callBack=callBack;
    }


    private void initView(){
        data1=listData1.get(0);
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.dialog_double_list, null);
        setContentView(view);

        //设置dialog的大小
        Window dialogWindow = getWindow();
        WindowManager.LayoutParams lp = dialogWindow.getAttributes();
        DisplayMetrics d = context.getResources().getDisplayMetrics(); // 获取屏幕宽、高用
        lp.width = (int) (d.widthPixels * 0.9); // 宽度设置为屏幕宽度的 90%
        lp.x = 0;
        lp.y = (int) -(d.heightPixels*0.2);
        dialogWindow.setAttributes(lp);

        mTvTitle1=(TextView)view.findViewById(R.id.tv_title1);
        mTvTitle2=(TextView)view.findViewById(R.id.tv_title2);

        mLvList1=(ListView)view.findViewById(R.id.lv_list1);
        mLvList2=(ListView)view.findViewById(R.id.lv_list2);
        final DoubleListDialogAdapter adapter1 =new DoubleListDialogAdapter(context, listData1);
        mLvList1.setAdapter(adapter1);

        adapter1.setCurrentItem(0);
        adapter1.setClick(true);
        adapter1.notifyDataSetChanged();


        listData2.addAll(AlllistData2.get(0));
        final DoubleListDialogAdapterForHouse adapter2 =new DoubleListDialogAdapterForHouse(context, listData2);
        mLvList2.setAdapter(adapter2);
        mLvList1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                // TODO Auto-generated method stub
                adapter1.setCurrentItem(arg2);
                adapter1.setClick(true);
                adapter1.notifyDataSetChanged();
                data1=listData1.get(arg2);
                listData2.clear();
                listData2.addAll(AlllistData2.get(arg2));
                adapter2.notifyDataSetChanged();
            }
        });
        mLvList2.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,long arg3) {
                // TODO Auto-generated method stub
                data2=listData2.get(arg2);
                callBack.clickResult(data1,data2.split(",")[1],data2.split(",")[0]);
            }
        });
    }

    public interface CallBack{
        void clickResult(String data1, String data2,String house);
    }
}
