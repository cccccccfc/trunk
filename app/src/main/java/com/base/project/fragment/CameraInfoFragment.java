package com.base.project.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.base.project.R;
import com.base.project.base.BaseFragment;
import com.base.project.utils.CommonUtils;
import com.base.utilslibrary.internet.GetUserContextualModelRequestUtils;
import com.ezvizuikit.open.EZUIError;
import com.ezvizuikit.open.EZUIKit;
import com.ezvizuikit.open.EZUIPlayer;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2017/12/25.
 */

public class CameraInfoFragment extends BaseFragment {

    @BindView(R.id.ez_player_ui)
    EZUIPlayer mMPlayer;
    Unbinder unbinder;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new, container, false);
        unbinder = ButterKnife.bind(this, view);
        getdata();
        return view;
    }

    public void getdata() {
        //获取EZUIPlayer实例
        Log.i("qaz", "getdata: 1111111111");
        GetUserContextualModelRequestUtils.getaccesstoken(new GetUserContextualModelRequestUtils.ModifySceneListener() {
            @Override
            public void onResponseMessage(String message, String appurl, String appkey) {
                //  Log.i("qaz", "onResponseMessage:1 " +"--token--"+message+"--url--"+appurl+"--key--"+appkey);
                if (!TextUtils.isEmpty(message) && !TextUtils.isEmpty(appurl) && !TextUtils.isEmpty(appkey)) {
                    //初始化EZUIKit
                    EZUIKit.initWithAppKey(getActivity().getApplication(), appkey);
                    //设置授权token
                    EZUIKit.setAccessToken(message);
                    //设置播放参数
                    mMPlayer.setUrl(appurl);
                    //开始播放
                    mMPlayer.startPlay();
                }
            }
        });

        //设置播放回调callback
        mMPlayer.setCallBack(new EZUIPlayer.EZUIPlayerCallBack() {
            @Override
            public void onPlaySuccess() {
                CommonUtils.logMes("-----onPlaySuccess---");
            }

            @Override
            public void onPlayFail(EZUIError ezuiError) {
                CommonUtils.logMes("------" + ezuiError.getErrorString());
//                mMPlayer.removeAllViews();
            }

            @Override
            public void onVideoSizeChange(int i, int i1) {
            }

            @Override
            public void onPrepared() {
                mMPlayer.startPlay();
            }

            @Override
            public void onPlayTime(Calendar calendar) {
            }

            @Override
            public void onPlayFinish() {
            }
        });


    }

    @Override
    public void initListener() {
        super.initListener();
    }

//    @Override
//    public void onStop() {
//        mMPlayer.stopPlay();
//        super.onStop();
//    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mMPlayer.stopPlay();
        mMPlayer.releasePlayer();
        unbinder.unbind();

    }
//
//    @Override
//    public void onDestroy() {
//        super.onDestroy();
////        mMPlayer.stopPlay();
////        mMPlayer.releasePlayer();
//    }




}
