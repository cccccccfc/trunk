package com.base.project.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTabHost;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;

import com.base.project.R;
import com.base.project.activity.voice.ActivityVoiceHome;
import com.base.project.base.BaseApplication;
import com.base.project.fragment.FragmentHome;
import com.base.project.fragment.FragmentMyself;
import com.base.project.severs.HomeService;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.Constants;
import com.base.project.utils.PermissionActivity;
import com.base.project.utils.SpTools;

import cn.hugeterry.updatefun.UpdateFunGO;
import cn.hugeterry.updatefun.config.UpdateKey;


/**
 * @author xqb
 */
public class MainActivityTabHost extends FragmentActivity {
    public static Context contexts;
    public static FragmentTabHost mTabHost;
    private LayoutInflater mLayoutInflater;
    String apkPath = "http://icfile.lchtime.cn:8001/app-debug.apk";

    private Class mFragmentArray[] = {FragmentHome.class, FragmentMyself.class};

    private int mImageArray[] = {R.drawable.tab_home_btn, R.drawable.tab_myself_btn};

    private String mTextArray[] = CommonUtils.getResource().getStringArray(R.array.tab_titles);
    private static final int REQUEST_CODE = 0; // 请求码
    // 所需的全部权限
    static final String[] PERMISSIONS = new String[]{
            Manifest.permission.RECORD_AUDIO,
            Manifest.permission.CAMERA,
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.READ_EXTERNAL_STORAGE
    };

    static LinearLayout ll_home;
    ImageView iv_voice;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tabhost);
        UpdateKey.API_TOKEN = "e18582f172e1985abe722d900e853de0";
        UpdateKey.APP_ID = "5a0968a8959d6978560003d6";
        //下载方式:
        UpdateKey.DialogOrNotification = UpdateKey.WITH_DIALOG;  //通过Dialog来进行下载
        //UpdateKey.DialogOrNotification=UpdateKey.WITH_NOTIFITION;通过通知栏来进行下载(默认)

        UpdateFunGO.init(this);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            window.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }
        ll_home = (LinearLayout) findViewById(R.id.ll_taghost_home);
        iv_voice = (ImageView) findViewById(R.id.iv_home_voice);
        initView();

        contexts = this;
        activityTabHost = this;
        iv_voice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(getContext(), ActivityVoiceHome.class);
                startActivity(intent);
            }
        });

        Intent intent = new Intent(getContext(), HomeService.class);
        startService(intent);
        //动态申请权限
        int sdk = Build.VERSION.SDK_INT;
        if (sdk >= 23) {
			Intent inten = new Intent(this, PermissionActivity.class);
			inten.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			Bundle bundle = new Bundle();
			bundle.putStringArray("permission", PERMISSIONS);
            PermissionActivity.startActivityForResult(this, REQUEST_CODE, PERMISSIONS);
        }

    }

    /**
     * 初始化组件
     */
    private void initView() {
        mLayoutInflater = LayoutInflater.from(this);

        // 找到TabHost
        mTabHost = (FragmentTabHost) findViewById(android.R.id.tabhost);
        mTabHost.setup(this, getSupportFragmentManager(), R.id.realtabcontent);
        mTabHost.getTabWidget().setDividerDrawable(null);//去掉中间间隔线
        // 得到fragment的个数
        int count = mFragmentArray.length;
        for (int i = 0; i < count; i++) {
            // 给每个Tab按钮设置图标、文字和内容
            TabSpec tabSpec = mTabHost.newTabSpec(mTextArray[i]).setIndicator(getTabItemView(i));
            // 将Tab按钮添加进Tab选项卡中
            mTabHost.addTab(tabSpec, mFragmentArray[i], null);
            // 设置Tab按钮的背景
            mTabHost.getTabWidget().getChildAt(i).setBackgroundResource(R.drawable.selector_tab_background);
//			mTabHost.getTabWidget().getChildAt(i).getLayoutParams().height = CommonUtils.dip2px(this,56);//设置高度

        }
//		mTabHost.getTabWidget().getChildTabViewAt(2).setOnClickListener(new View.OnClickListener() {
//			@Override
//			public void onClick(View v) {
//
//				Intent intent = new Intent(getContext(), AsrActivity.class);
//				startActivity(intent);
//			}
//		});
    }

    private String TAG = "qaz";

    /**
     * 给每个Tab按钮设置图标和文字
     */
    private View getTabItemView(int index) {
        View view = mLayoutInflater.inflate(R.layout.tab_item_view, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.imageview);
        imageView.setImageResource(mImageArray[index]);
        TextView textView = (TextView) view.findViewById(R.id.textview);
        textView.setText(mTextArray[index]);
        return view;
    }

    private long exitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        PopupWindow mPopWindow = FragmentHome.getPopWindow();
        if (mPopWindow != null && mPopWindow.isShowing()) {
            mPopWindow.dismiss();
            return true;
        }
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                Toast.makeText(getApplicationContext(), "再按一次退出程序", Toast.LENGTH_SHORT).show();
                exitTime = System.currentTimeMillis();
            } else {
                BaseApplication.getThreadPool().shutdownNow();//关闭线程池
                finish();
                System.exit(0);
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public static int index;

    public static void setTabHost(int index) {
        mTabHost.setCurrentTab(index);
    }

    public static void setIndex(int num) {
        index = num;
    }

    public static int getIndex() {
        return index;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    public static Context getContext() {
        return contexts;
    }

    private static MainActivityTabHost activityTabHost;

    public static MainActivityTabHost getInstance() {
        return activityTabHost;
    }

    public static void changeBackgroud(int i) {
        ll_home.setBackgroundResource(i);
    }

    public void finishActivity() {
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();


        /**
         * 删除家庭的时候回到这里，同时还有刷新FragmentMyself中刷新
         */
        if (SpTools.getBoolean(this, Constants.deletehome, false)) {
            setTabHost(0);
        }
        UpdateFunGO.onResume(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        UpdateFunGO.onStop(this);
    }



}
