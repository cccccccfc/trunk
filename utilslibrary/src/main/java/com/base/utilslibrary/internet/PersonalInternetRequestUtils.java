package com.base.utilslibrary.internet;

import android.content.Context;
import android.os.CountDownTimer;
import android.text.TextUtils;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

import com.base.utilslibrary.MyDialog;
import com.base.utilslibrary.R;
import com.base.utilslibrary.ToolUtils;
import com.base.utilslibrary.bean.DataInfo;
import com.base.utilslibrary.bean.LoginResult;
import com.base.utilslibrary.bean.ProjectUrlInfo;
import com.google.gson.Gson;
import com.zhy.http.okhttp.OkHttpUtils;
import com.zhy.http.okhttp.callback.StringCallback;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

import okhttp3.Call;

/**
 * @author Admin
 * @time 2017/4/7 9:56
 * @des ${TODO}
 */

public class PersonalInternetRequestUtils {

    private static int index = 0;
    private static Context mContext;
    private static Gson mGson;
    private static ForResultListener mListener;
    private static ForResultDataListener dataListener;
    private static ForResultInfoListener mInfoListener;
    private static TimeCount timeCount;
    private static TextView mTextView;
    private static boolean isRun = false;
    static {
        mGson = new Gson();
        timeCount = new TimeCount(60000, 1000);
    }

    /**
     * 登陆
     * @param phonenumber 电话号码
     * @param password 密码
     * @param listener listener
     */
    public static void login(final Context context, String phonenumber, String password, ForResultDataListener listener){
        final MyDialog[] dialog = {MyDialog.showDialog(context)};
        dialog[0].show();
        dataListener = listener;
        if(!ToolUtils.isNetworkAvailable(context)){
            if (dialog[0] != null) {
                dialog[0].dismiss();
                dialog[0] = null;
            }

            ToolUtils.toastMessage("您当前无网络，请联网再试",context);
            dataListener.onResponseMessage(null);
            return;
        }
        if(TextUtils.isEmpty(phonenumber)){
            if (dialog[0] != null) {
                dialog[0].dismiss();
                dialog[0] = null;
            }
            ToolUtils.toastMessage("您输入的手机号为空",context);
            dataListener.onResponseMessage(null);
            return ;
        }else if(TextUtils.isEmpty(password)){
            if (dialog[0] != null) {
                dialog[0].dismiss();
                dialog[0] = null;
            }
            ToolUtils.toastMessage("您输入的密码为空",context);
            return;
        }

        if(!TextUtils.isEmpty(phonenumber)){

            if(!ToolUtils.isMobilePhone(phonenumber)){
                if (dialog[0] != null) {
                    dialog[0].dismiss();
                    dialog[0] = null;
                }
                ToolUtils.toastMessage("您输入的手机号有误",context);
                dataListener.onResponseMessage(null);
                return ;
            }
        }
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.getLogin;
        OkHttpUtils.post().url(url)
                .addParams("ui_phone",phonenumber)
                .addParams("ui_pwd",password)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                if (dialog[0] != null) {
                    dialog[0].dismiss();
                    dialog[0] = null;
                }
                ToolUtils.logMes(e+"");
                ToolUtils.toastMessage("您网络信号不稳定，请稍后再试",context);
            }

            @Override
            public void onResponse(String response, int id) {
                if (dialog[0] != null) {
                    dialog[0].dismiss();
                    dialog[0] = null;
                }
                ToolUtils.logMes("login="+response);
                LoginResult result = mGson.fromJson(response, LoginResult.class);
                if ("0".equals(result.errno)) {
                    dataListener.onResponseMessage(result.data);
                } else {
                    ToolUtils.toastMessage(result.error,context);
                }
            }
        });
    }
    /**
     *  获取验证码
     * @param phoneNumber 电话号码
     * @param view 验证码的view
     * @param listener listener
     */
    public static void verificationCode(final Context context, String phoneNumber, TextView view, ForResultListener listener){
        mListener = listener;
        mTextView = view;
        if(!ToolUtils.isNetworkAvailable(context)){
            ToolUtils.toastMessage("您当前无网络，请联网再试",context);
            mListener.onResponseMessage("");
            return;
        }
        if(TextUtils.isEmpty(phoneNumber)){
            ToolUtils.toastMessage("您输入的手机号为空",context);
            mListener.onResponseMessage("");
            return ;
        }
        if(!TextUtils.isEmpty(phoneNumber)){
            if(!ToolUtils.isMobilePhone(phoneNumber)){
                ToolUtils.toastMessage("您输入的手机号有误",context);
                mListener.onResponseMessage("");
                return ;
            }
        }
        if(isRun){
            return ;
        }
        timeCount.start();
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.sendCode;
        OkHttpUtils.post().url(url)
                .addParams("ui_phone",phoneNumber)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToolUtils.toastMessage("您网络信号不稳定，请稍后再试",context);
                mListener.onResponseMessage("");
            }

            @Override
            public void onResponse(String response, int id) {
                ToolUtils.logMes("verificationCode="+response);
                LoginResult result = mGson.fromJson(response,LoginResult.class);
                if("0".equals(result.errno)){
                    mListener.onResponseMessage(result.data.code);
                }else{
                    ToolUtils.toastMessage(result.error,context);
                }
            }
        });
    }

    /**
     * 注册新用户
     * @param phoneNumber 电话号码
     * @param vc_code 获取的验证码
     * @param password 密码
     * @param code 用户输入的验证码
     * @param view 验证码的View
     * @param listener listener
     */
    public static void register(final Context context, String phoneNumber, String vc_code, String password, String code, TextView view, ForResultListener listener){
        mListener = listener;
        mTextView = view;
        if(!ToolUtils.isNetworkAvailable(context)){
            ToolUtils.toastMessage("您当前无网络，请联网再试",context);
            return;
        }
        if(TextUtils.isEmpty(phoneNumber)){
            ToolUtils.toastMessage("您输入的手机号为空",context);
            return;
        }else if(TextUtils.isEmpty(code)){
            ToolUtils.toastMessage("您输入的验证码为空",context);
            return;
        }else if(TextUtils.isEmpty(password)){
            ToolUtils.toastMessage("您输入的密码为空",context);
            return;
        }
        if(!TextUtils.isEmpty(phoneNumber)){
            if(!ToolUtils.isMobilePhone(phoneNumber)){
                ToolUtils.toastMessage("您输入的手机号有误",context);
                mListener.onResponseMessage("");
                return ;
            }
        }

        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.reg;
        OkHttpUtils.post().url(url)
                .addParams("ui_nc","")
                .addParams("ui_phone",phoneNumber)
                .addParams("ui_mail","")
                .addParams("ui_pwd",password)
                .addParams("vc_code",code)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToolUtils.toastMessage("您网络信号不稳定，请稍后再试",context);
            }
            @Override
            public void onResponse(String response, int id) {
                ToolUtils.logMes("register="+response);
                LoginResult result = mGson.fromJson(response, LoginResult.class);
                if("0".equals(result.errno)){
                    reset();
                    mListener.onResponseMessage("成功");
                }else {
                    ToolUtils.toastMessage(result.error,context);
                }
            }
        });

    }

    /**
     * 第三方登陆注册
     * @param phoneNumber 电话号码
     * @param password 密码
     * @param nextPassword 确认密码
     * @param openid 第三方获取的id
     * @param username 第三方获取的昵称
     * @param imgUrl 第三方获取的头像url等其他信息，用| 隔开
     * @param type 第三方类型 1微信 1qq 3新浪微博 4支付宝
     * @param passport 输入密码的Editext
     * @param nextPassport 再次输入密码的Editext
     * @param listener
     */
    public static void thirdRegister(Context context,String phoneNumber, String password, String nextPassword
            ,String openid,String username,String imgUrl,String type
            , final EditText passport, final EditText nextPassport, ForResultListener listener){
        mContext = context;
        mListener = listener;
        if(!ToolUtils.isNetworkAvailable(context)){
            ToolUtils.toastMessage("您当前无网络，请联网再试",context);
            return;
        }
        if(TextUtils.isEmpty(phoneNumber)){
            ToolUtils.toastMessage("您输入的手机号为空",context);
            return;
        }else if(TextUtils.isEmpty(password) | TextUtils.isEmpty(nextPassword)){
            ToolUtils.toastMessage("您输入的密码为空",context);
            passport.setText("");
            nextPassport.setText("");
            return;
        }else if(password.length()<6 |nextPassword.length()<6){
            ToolUtils.toastMessage("输入的密码长度不要小于6位",context);
            passport.setText("");
            nextPassport.setText("");
            return;
        }
        if(!TextUtils.isEmpty(phoneNumber)){
            if(!ToolUtils.isMobilePhone(phoneNumber)){
                ToolUtils.toastMessage("您输入的手机号有误",context);
                mListener.onResponseMessage("");
                return ;
            }
        }
        if(!password.equals(nextPassword)){
            ToolUtils.toastMessage("二次输入的密码不一致",context);
            passport.setText("");
            nextPassport.setText("");
            return;
        }
        String url = mContext.getResources().getString(R.string.service_host_address)
                .concat(mContext.getResources().getString(R.string.tparty));
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("index",(index++)+"")
                .addParams("tp_openid",openid)
                .addParams("tp_username",username)
                .addParams("tp_tparty_info",imgUrl)
                .addParams("tp_tparty_type",type)
                .addParams("type",1+"")
                .addParams("ub_phone",phoneNumber)
                .addParams("ud_pwd",password)
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToolUtils.logMes("thirdRegister="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                ToolUtils.logMes("thirdRegister="+response);
                LoginResult result = mGson.fromJson(response, LoginResult.class);
                String code = result.result.code;
                if (code.equals("10")) {
                    mListener.onResponseMessage("成功");
                }
            }
        });
    }

    /**
     * 第三方登录查询
     * @param openid 第三方获取的id
     * @param username 第三方获取的昵称
     * @param imgUrl 第三方获取的头像url等其他信息，用| 隔开
     * @param type 第三方类型 1微信 1qq 3新浪微博 4支付宝
     * @param listener
     */
    public static void thirdRegisterQuery(Context context,String openid,String username,String imgUrl,String type
            , ForResultListener listener){
        mContext = context;
        mListener = listener;
        String url = mContext.getResources().getString(R.string.service_host_address)
                .concat(mContext.getResources().getString(R.string.tparty));
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("index",(index++)+"")
                .addParams("tp_openid",openid)
                .addParams("tp_username",username)
                .addParams("tp_tparty_info",imgUrl)
                .addParams("tp_tparty_type",type)
                .addParams("type",1+"")
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToolUtils.logMes("thirdRegisterQuery="+e);
            }

            @Override
            public void onResponse(String response, int id) {
                ToolUtils.logMes("thirdRegisterQuery="+response);
                LoginResult result = mGson.fromJson(response, LoginResult.class);
                String code = result.result.code;
                if (code.equals("10")) {
                    mListener.onResponseMessage("成功");
                }else if(code.equals("20")){
                    mListener.onResponseMessage("");
                }
            }
        });
    }
    /**
     * 忘记密码
     * @param phoneNumber 电话号码
     * @param vc_code 获得到验证码
     * @param code 输入的验证码
     * @param password 输入的密码
     * @param nextPassword 再次输入的密码
     * @param view 验证码的View
     * @param passport 输入密码的Editext
     * @param nextPassport 再次输入密码的Editext
     * @param listener listener
     */
    public static void forgetPassword(final Context context, String phoneNumber, String vc_code, String code, String password, String nextPassword, TextView view, final EditText passport, final EditText nextPassport, ForResultListener listener){
        mListener = listener;
        mTextView = view;
        if(!ToolUtils.isNetworkAvailable(context)){
            ToolUtils.toastMessage("您当前无网络，请联网再试",context);
            return;
        }
        if(TextUtils.isEmpty(phoneNumber)){
            ToolUtils.toastMessage("您输入的手机号为空",context);
            return;
        }else if(TextUtils.isEmpty(code)){
            ToolUtils.toastMessage("您输入的验证码为空",context);
            return;
        }else if(TextUtils.isEmpty(password) | TextUtils.isEmpty(nextPassword)){
            ToolUtils.toastMessage("您输入的密码为空",context);
            passport.setText("");
            nextPassport.setText("");
            return;
        }else if(password.length()<6 |nextPassword.length()<6){
            ToolUtils.toastMessage("输入的密码长度不要小于6位",context);
            passport.setText("");
            nextPassport.setText("");
            return;
        }
        if(!TextUtils.isEmpty(phoneNumber)){
            if(!ToolUtils.isMobilePhone(phoneNumber)){
                ToolUtils.toastMessage("您输入的手机号有误",context);
                mListener.onResponseMessage("");
                return ;
            }
        }
        if(!password.equals(nextPassword)){
            ToolUtils.toastMessage("二次输入的密码不一致",context);
            passport.setText("");
            nextPassport.setText("");
            return;
        }
        if(!vc_code.equals(code)){
            ToolUtils.toastMessage("您输入的验证码错误",context);
            return;
        }

        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.resetpwd;
        OkHttpUtils.post().url(url)
                .addParams("ui_phone",phoneNumber)
                .addParams("ui_pwd",password)
                .addParams("vc_code",code)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToolUtils.toastMessage("您网络信号不稳定，请稍后再试",context);
            }
            @Override
            public void onResponse(String response, int id) {
                ToolUtils.logMes("forgetPassword="+response);
                LoginResult result = mGson.fromJson(response, LoginResult.class);
                if("0".equals(result.errno)){
                    reset();
                    passport.setText("");
                    nextPassport.setText("");
                    mListener.onResponseMessage("成功");
                }else {
                    ToolUtils.toastMessage(result.error,context);
                }
            }
        });

    }

    /**
     *  重置密码
     * @param password 原密码
     * @param newpassword 新密码
     * @param confirmpassword 新密码确认
     * @param editpass 原密码的Editext
     * @param editnewpass 新密码的Editext
     * @param editconfirm 确认密码的Editext
     * @param ub_id 用户id
     * @param listener listener
     */
    public static void reviseCode(final Context context, String password, String newpassword, String confirmpassword,
                                  final EditText editpass, final EditText editnewpass, final EditText editconfirm,
                                  String ub_id,ForResultListener listener) {
        mContext = context;
        if(!ToolUtils.isNetworkAvailable(context)){
            ToolUtils.toastMessage("您当前无网络，请联网再试",context);
            return;
        }
        mListener = listener;
        if(TextUtils.isEmpty(password) | TextUtils.isEmpty(newpassword) | TextUtils.isEmpty(confirmpassword)){
            ToolUtils.toastMessage("您输入的密码为空",context);
            editpass.setText("");
            editnewpass.setText("");
            editconfirm.setText("");
            return;
        }else if(password.length()<6 |confirmpassword.length()<6){
            ToolUtils.toastMessage("输入的新密码长度不要小于6位",context);
            editpass.setText("");
            editnewpass.setText("");
            editconfirm.setText("");
            return;
        }
        if(!newpassword.equals(confirmpassword)){
            ToolUtils.toastMessage("二次输入的新密码不一致",context);
            editnewpass.setText("");
            editconfirm.setText("");
            return;
        }
        String url = mContext.getResources().getString(R.string.service_host_address)
                .concat(mContext.getResources().getString(R.string.chpwd));
        OkHttpUtils.post().url(url)
                .addParams("sid","")
                .addParams("index",(index++)+"")
                .addParams("ub_id",ub_id)
                .addParams("uo_long","")
                .addParams("uo_lat","")
                .addParams("uo_high","")
                .addParams("ud_pwd",password)
                .addParams("nw_pwd",newpassword)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToolUtils.toastMessage("您网络信号不稳定，请稍后再试",context);
            }
            @Override
            public void onResponse(String response, int id) {
                ToolUtils.logMes("reviseCode="+response);
                LoginResult result = mGson.fromJson(response, LoginResult.class);
                String code = result.result.code;
                String info = result.result.info;
                if(code.equals("10")){
                    mListener.onResponseMessage("成功");
                }else if(code.equals("20")){
                    if(info.equals("原密码不正确")){
                        ToolUtils.toastMessage("您输入的原密码错误",context);
                        editpass.setText("");
                        editnewpass.setText("");
                        editconfirm.setText("");
                    }else if(info.equals("密码长度不够")){
                        ToolUtils.toastMessage("您输入的密码位数不足6位",context);
                    }
                }
            }
        });

    }

    /**
     * 获取个人信息
     * @param ui_id 用户id
     * @param listener
     */
    public static void getUserInformation(String ui_id,ForResultInfoListener listener) {
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.userinfo;
        mInfoListener = listener;
        final Map<String,String> map = new HashMap<>();
        OkHttpUtils.post().url(url)
                .addParams("ui_id",ui_id)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToolUtils.logMes("getUserInformation="+e);
                mInfoListener.onResponseMessage(null);
            }

            @Override
            public void onResponse(String response, int id) {
                ToolUtils.logMes("getUserInformation="+response);
                LoginResult result = mGson.fromJson(response, LoginResult.class);
                String ud_addr = result.data.user_info.getUi_addr();
                String ud_borth = result.data.user_info.getUi_borth();
                String ud_nickname = result.data.user_info.getUi_nc();
                String ud_photo_fileid = result.data.user_info.getUi_pic();
                String ud_sex = result.data.user_info.getUi_sex();
                String ui_phone = result.data.user_info.getUi_phone();
                String mg_id = result.data.group_info.getMg_id();
                String mg_name = result.data.group_info.getMg_name();
                String mg_role = result.data.group_info.getRole();
                map.put("ud_addr",ud_addr);
                map.put("ud_borth",ud_borth);
                map.put("ud_nickname",ud_nickname);
                map.put("ud_photo_fileid",ud_photo_fileid);
                map.put("ud_sex",ud_sex);
                map.put("ui_phone",ui_phone);
                map.put("mg_id",mg_id);
                map.put("mg_name",mg_name);
                map.put("mg_role",mg_role);
                mInfoListener.onResponseMessage(map);
            }
        });
    }

    /**
     * 上传头像
     * @param ub_id 用户id
     * @param picName  头像存储的名字
     * @param listener
     */
    public static void uplodePicture(final Context context, String ub_id,String picName, ForResultListener listener){
        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.modifydetail;
        mListener = listener;
        OkHttpUtils.post()
                .addFile("image[]",picName,new File(context.getFilesDir(),picName))
                .url(url)
                .addParams("ui_id", ub_id)
                .addParams("type", "1")
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToolUtils.logMes("uplodePicture   e  ="+e);
                mListener.onResponseMessage("");
            }
            @Override
            public void onResponse(String response, int id) {
                ToolUtils.logMes("uplodePicture="+response);
                LoginResult result = mGson.fromJson(response, LoginResult.class);
                if("0".equals(result.errno)){
                    mListener.onResponseMessage("成功");
                    ToolUtils.toastMessage("上传头像成功",context);
                }else{
                    ToolUtils.toastMessage(result.error,context);
                    mListener.onResponseMessage("");
                }
            }
        });
    }

    /**
     * 编辑个人信息
     * @param ub_id 用户id
     * @param name 昵称
     * @param date 时间
     * @param address 地址
     * @param sex 性别
     * @param listener
     */
    public static void editUserInformation(final Context context, String ub_id,String name, String date, String address, String sex, ForResultListener listener) {
        Log.i("qaz", "editUserInformation: " +ub_id +"sex"+name+"sex"+date+"sex"+address +"sex"+sex);
        mListener = listener;

        String url = ProjectUrlInfo.service_host_address + ProjectUrlInfo.modifyuser;
        Log.i("qaz", "editUserInformation: " +url);
        OkHttpUtils.post().url(url)
                .addParams("ui_id",ub_id)
                .addParams("ui_borth",date)
                .addParams("ui_mail","")
                .addParams("ui_addr",address)
                .addParams("ui_sex",sex)
                .addParams("ui_nc",name)
                .build().execute(new StringCallback() {
            @Override
            public void onError(Call call, Exception e, int id) {
                ToolUtils.logMes("editUserInformation="+e);
                ToolUtils.toastMessage("编辑个人信息失败，请重新再试",context);
            }
            @Override
            public void onResponse(String response, int id) {
                ToolUtils.logMes("editUserInformation="+response);
                LoginResult result = mGson.fromJson(response, LoginResult.class);
                if("0".equals(result.errno)){
                    mListener.onResponseMessage("成功");
                    ToolUtils.toastMessage("编辑个人信息成功",context);
                }else if("4".equals(result.errno)){
                    ToolUtils.toastMessage("编辑个人信息成功",context);
                }else {
                    ToolUtils.toastMessage("编辑个人信息失败，请重新再试",context);
                }
            }
        });

    }

    public interface ForResultListener{
        void onResponseMessage(String code);
    }
    public interface ForResultDataListener{
        void onResponseMessage(DataInfo info);
    }
    public interface ForResultInfoListener{
        void onResponseMessage(Map<String, String> map);
    }
    /**
     * 验证码倒计时timecount
     */
    static class TimeCount extends CountDownTimer {
        public TimeCount(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);// 参数依次为总时长,和计时的时间间隔
        }
        @Override
        public void onFinish() {// 计时完毕时触发
            mTextView.setText("获取验证码");
            mTextView.setClickable(true);
            isRun = false;
        }
        @Override
        public void onTick(long millisUntilFinished) {// 计时过程显示
            mTextView.setClickable(false);
            isRun = true;
            mTextView.setText("("+millisUntilFinished / 1000 + "秒)");
        }
    }

    private static void reset() {
        if(timeCount != null){
            timeCount.onFinish();
        }
    }
}
