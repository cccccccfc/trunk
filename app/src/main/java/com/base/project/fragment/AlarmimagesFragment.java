package com.base.project.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.project.R;
import com.base.project.activity.facility.ActivityCameraInfo;
import com.base.project.adapter.RecordListAdapter;
import com.base.project.base.BaseFragment;
import com.base.project.utils.CommonUtils;
import com.base.utilslibrary.bean.RecordListBean;
import com.base.utilslibrary.internet.GetUserContextualModelRequestUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/12/25.
 */

public class AlarmimagesFragment extends BaseFragment {


    @BindView(R.id.rv_house_manager_flour)
    RecyclerView rvHouseManagerFlour;
    Unbinder unbinder;
    private int db_sbID;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_other, container, false);
        unbinder = ButterKnife.bind(this, view);
        getdata();
        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        db_sbID = ((ActivityCameraInfo) activity).getDb_sbID();
        //initdata();

    }

    public void getdata() {

       // Log.i("qaz", "initdata: 11111111111" + db_sbID);
        GetUserContextualModelRequestUtils.getcamerarecord(String.valueOf(db_sbID), "1", new GetUserContextualModelRequestUtils.DelexCuteListener() {
            @Override
            public void onResponseMessage(List<RecordListBean> message) {
                if (message == null) {
                    CommonUtils.toastMessage("暂无数据");
                } else {
                   // Log.i("qaz", "getdata: 3333333333333");
                    presentation(message);

                }
            }
        });
    }

    private void presentation(List<RecordListBean> message) {

        RecordListAdapter record = new RecordListAdapter(message, getContext());
        rvHouseManagerFlour.setLayoutManager(new GridLayoutManager(getContext(), 1));
        rvHouseManagerFlour.setAdapter(record);

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
