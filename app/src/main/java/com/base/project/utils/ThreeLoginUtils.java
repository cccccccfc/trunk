package com.base.project.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.umeng.socialize.UMAuthListener;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;

import java.util.Map;
import java.util.Set;

/**
 * @author IMXU
 * @time 2017/9/4 9:24
 * @des ${TODO}
 * 邮箱：butterfly_xu@sina.com
 */

public class ThreeLoginUtils {

    UMShareAPI mShareAPI;//友盟
    String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE
            ,Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.CALL_PHONE
            ,Manifest.permission.READ_LOGS,Manifest.permission.READ_PHONE_STATE
            ,Manifest.permission.READ_EXTERNAL_STORAGE,Manifest.permission.SET_DEBUG_APP
            ,Manifest.permission.SYSTEM_ALERT_WINDOW,Manifest.permission.GET_ACCOUNTS
            ,Manifest.permission.WRITE_APN_SETTINGS};
    private SHARE_MEDIA platform;

    private static ThreeLoginUtils mUtils;
    public static ThreeLoginUtils getInstance(){
        if(mUtils == null){
            mUtils = new ThreeLoginUtils();
        }
        return mUtils;
    }

    private  Activity mContext;
    public void threeLogin(Activity context,SHARE_MEDIA platform,ThreeLoginBackListener listener){//如果不行可能就是缺少下面的activity回调
        mContext = context;
        mThreeLoginBackListener = listener;
        this.platform = platform;
        LoginPlatformInfo();
    }

    //activity回调
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
//    }
    //权限的回调
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
//        if (requestCode == 123) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                mShareAPI.getPlatformInfo(mContext, platform, authListener);
//            } else {
//                CommonUtils.toastMessage("未获得相应权限");
//            }
//        }
//    }

    //第三方平台登录
    private void LoginPlatformInfo() {
        if(Build.VERSION.SDK_INT >= 23){
            boolean isprimission= getisPermission();
            if (isprimission) {
                mShareAPI.getPlatformInfo(mContext,platform, authListener);
            } else {
                ActivityCompat.requestPermissions(mContext, mPermissionList, 123);
            }
        }else {
            mShareAPI.getPlatformInfo(mContext,platform, authListener);
        }
    }

    //判断是否获得权限
    private boolean getisPermission() {
        boolean quanxian=true;
        for (int i=0;i<mPermissionList.length;i++){
            if(ContextCompat.checkSelfPermission(mContext,mPermissionList[i]) != PackageManager.PERMISSION_GRANTED){
                quanxian=false;
            }
        }
        return quanxian;
    }

    UMAuthListener authListener = new UMAuthListener() {
        /**
         * @desc 授权开始的回调  @param platform 平台名称
         */
        @Override
        public void onStart(SHARE_MEDIA platform) {
        }

        /**
         * @desc 授权成功的回调  @param platform 平台名称 @param action 行为序号，开发者用不上 @param data 用户资料返回
         */
        @Override
        public void onComplete(SHARE_MEDIA platform, int action, Map<String, String> data) {
            Toast.makeText(mContext, "登录成功了", Toast.LENGTH_LONG).show();
            Set<String> keySet = data.keySet();
            //遍历循环，得到里面的key值----用户名，头像....
//            for (String string : keySet) {
//                CommonUtils.logMes("------data-----"+string+"-----key-----="+data.get(string));
//            }
            mThreeLoginBackListener.backInfo(data);
        }
        /**
         * @desc 授权失败的回调 @param platform 平台名称 @param action 行为序号，开发者用不上 @param t 错误原因
         */
        @Override
        public void onError(SHARE_MEDIA platform, int action, Throwable t) {

            Toast.makeText(mContext, "登录失败：" + t.getMessage(),Toast.LENGTH_LONG).show();
        }
        /**
         * @desc 授权取消的回调 @param platform 平台名称 @param action 行为序号，开发者用不上
         */
        @Override
        public void onCancel(SHARE_MEDIA platform, int action) {
            Toast.makeText(mContext, "登录取消了", Toast.LENGTH_LONG).show();
        }
    };

    /**
     * 三方登陆的接口回调
     */
    public interface ThreeLoginBackListener{
        void backInfo(Map<String, String> data);
    }
    private ThreeLoginBackListener mThreeLoginBackListener;
}
