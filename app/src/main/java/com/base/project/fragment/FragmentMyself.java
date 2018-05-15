package com.base.project.fragment;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.activity.MainActivityTabHost;
import com.base.project.activity.myself.ActivityAboutUs;
import com.base.project.activity.myself.ActivityAdviseBack;
import com.base.project.activity.myself.ActivityEquipment;
import com.base.project.activity.myself.ActivityHomeManager;
import com.base.project.activity.myself.ActivityMyselfInformation;
import com.base.project.activity.myself.ActivityPattern;
import com.base.project.activity.myself.ActivitySystemInstall;
import com.base.project.base.BaseFragment;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.Constants;
import com.base.project.utils.SpTools;
import com.base.utilslibrary.bean.ProjectUrlInfo;
import com.base.utilslibrary.internet.PersonalInternetRequestUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * @author IMXU
 * @time 2017/5/3 13:21
 * @des 资讯首页
 * 邮箱：butterfly_xu@sina.com
 */
public class FragmentMyself extends BaseFragment implements View.OnClickListener {
    @BindView(R.id.ll_myself_pattern)
    LinearLayout ll_pattern;
    Unbinder unbinder;
    private View view;

    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_backto)
    ImageView backto;
    @BindView(R.id.iv_base_edit)
    ImageView edit;
    @BindView(R.id.tv_base_title)
    TextView title;

    @BindView(R.id.ll_myself_edit)
    LinearLayout ll_edit;
    @BindView(R.id.tv_myself_nickname)
    TextView tv_nickname;
    @BindView(R.id.iv_myself_img)
    ImageView img;
    @BindView(R.id.tv_myself_home_home)
    TextView tv_home;

    @BindView(R.id.ll_myself_home_ad)
    LinearLayout ll_home;
    @BindView(R.id.ll_myself_sb)
    LinearLayout ll_sb;
    @BindView(R.id.ll_myself_shezhi)
    LinearLayout ll_shezhi;
    @BindView(R.id.ll_myself_advise)
    LinearLayout ll_advise;
    @BindView(R.id.ll_myself_we)
    LinearLayout ll_we;
    private String ud_addr;
    private String ud_borth;
    private String ud_nickname;
    private String ud_photo_fileid;
    private String ud_sex;
    @BindView(R.id.srl_myself)
    SmartRefreshLayout smartRefreshLayout;
    private Bitmap cacheBitmap;
    private String ui_phone;
    private String mg_id;
    private String mg_name;
    private String mg_role;

    @Override
    public View initView(LayoutInflater inflater) {
        if (view == null) {
            view = inflater.inflate(R.layout.fragment_myself, null);
            internet();
        }
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        ButterKnife.bind(this, view);
        MainActivityTabHost.changeBackgroud(R.drawable.bg_myself);
        title.setText("我的");
        edit.setVisibility(View.INVISIBLE);
        backto.setVisibility(View.INVISIBLE);
        tv_nickname.getPaint().setFakeBoldText(true);//加粗

        ClassicsHeader header = new ClassicsHeader(getActivity());
        header.setPrimaryColors(this.getResources().getColor(R.color.colorNUll), Color.WHITE);
        smartRefreshLayout.setRefreshHeader(header);
        smartRefreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(RefreshLayout refreshlayout) {
                internet();
            }
        });
        return view;
    }

    @Override
    public void init() {
        super.init();
    }

    @Override
    public void initdata() {
        super.initdata();
    }

    private void internet() {
        CommonUtils.logMes("-----id----" + SpTools.getString(getActivity(), Constants.userId, "0"));
        PersonalInternetRequestUtils.getUserInformation(SpTools.getString(getActivity(), Constants.userId, "0")
                , new PersonalInternetRequestUtils.ForResultInfoListener() {
                    @Override
                    public void onResponseMessage(Map<String, String> map) {
                        if (map != null) {
                            ud_addr = map.get("ud_addr");
                            ud_borth = map.get("ud_borth");
                            ud_nickname = map.get("ud_nickname");
                            ud_photo_fileid = map.get("ud_photo_fileid");
                            ud_sex = map.get("ud_sex");
                            ui_phone = map.get("ui_phone");
                            cacheBitmap = CommonUtils.getCacheFile("myicon.jpg");
                            if (cacheBitmap == null) {
                                Glide.with(getActivity()).load(ProjectUrlInfo.phtot_address + ud_photo_fileid)
                                        .placeholder(R.drawable.img_default_person)
                                        .bitmapTransform(new CropCircleTransformation(getActivity()))
                                        .into(img);
                                Glide.with(getActivity()).load(ProjectUrlInfo.phtot_address + ud_photo_fileid).asBitmap().into(new SimpleTarget<Bitmap>() {
                                    @Override
                                    public void onResourceReady(Bitmap resource, GlideAnimation<? super Bitmap> glideAnimation) {
                                        CommonUtils.saveBitmapFile(resource, "myicon.jpg");//先保存文件到本地
                                    }
                                });
                            }else {
                                byte[] bytes = CommonUtils.getBitMapByteArray(cacheBitmap);
                                Glide.with(getActivity()).load(bytes)
                                        .placeholder(R.drawable.img_default_person)
                                        .bitmapTransform(new CropCircleTransformation(getActivity()))
                                        .into(img);
                            }
                            if (!TextUtils.isEmpty(SpTools.getString(getActivity(), Constants.home_name, ""))) {
                                mg_name = SpTools.getString(getActivity(), Constants.home_name, "");
                                mg_id = SpTools.getString(getActivity(), Constants.information, "");
                                mg_role = SpTools.getString(getActivity(), Constants.home_role, "");
                            } else {
                                mg_id = map.get("mg_id");
                                mg_name = map.get("mg_name");
                                mg_role = map.get("mg_role");
                            }

                            /**
                             * 这里要比较自己保存的家庭管理信息和默认信息，如果没有自己保存的(缓存被清)则需要用默认信息。
                             * 自己保存的信息在首页换家庭的时候保存，在自己刚进app创建的时候保存
                             */


                            tv_nickname.setText(ud_nickname);
                            tv_home.setText(mg_name);
                        } else {
                            CommonUtils.toastMessage("请求数据失败，请刷新重试");
                        }
                        smartRefreshLayout.finishRefresh(1000);
                    }
                });
    }

    @Override
    public void initListener() {
        super.initListener();
        ll_edit.setOnClickListener(this);
        ll_home.setOnClickListener(this);
        ll_sb.setOnClickListener(this);
        ll_shezhi.setOnClickListener(this);
        ll_advise.setOnClickListener(this);
        ll_we.setOnClickListener(this);
        ll_pattern.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        if (TextUtils.isEmpty(ud_nickname) || TextUtils.isEmpty(ui_phone)) {
            CommonUtils.toastMessage("加载信息失败，请刷新重试");
            return;
        }
        String ub_id = SpTools.getString(getActivity(), Constants.userId, "0");
        /**
         * ub_id为0是代表用户清空了缓存，需要重新登陆
         */
        if ("0".equals(ub_id)) {
            CommonUtils.toastMessage("请重新登陆");
            CommonUtils.deleteBitmap("myicon.jpg");//删除本地图片
            MainActivityTabHost.getInstance().finishActivity();
            return;
        }
        Intent intent;
        switch (v.getId()) {
            case R.id.ll_myself_edit://查看编辑个人信息
                intent = new Intent(getActivity(), ActivityMyselfInformation.class);
                intent.putExtra("addr", ud_addr);
                intent.putExtra("borth", ud_borth);
                intent.putExtra("nn", ud_nickname);
                intent.putExtra("photo", ud_photo_fileid);
                intent.putExtra("sex", ud_sex);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
                break;
            case R.id.ll_myself_home_ad://家庭管理

                intent = new Intent(getActivity(), ActivityHomeManager.class);

                intent.putExtra("ub_id", ub_id);
                intent.putExtra("mg_id", mg_id);
                intent.putExtra("role", mg_role);
                intent.putExtra("name", mg_name);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
                break;
            case R.id.ll_myself_pattern://情景模式
                intent = new Intent(getActivity(), ActivityPattern.class);
                intent.putExtra("ub_id", ub_id);
                intent.putExtra("mg_id", mg_id);
                intent.putExtra("role", mg_role);
                intent.putExtra("name", mg_name);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
                break;
            case R.id.ll_myself_sb://设备联动
                intent = new Intent(getActivity(), ActivityEquipment.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
                break;
            case R.id.ll_myself_shezhi://设置
                intent = new Intent(getActivity(), ActivitySystemInstall.class);
                intent.putExtra("phone", ui_phone);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
                break;
            case R.id.ll_myself_advise://建议
                intent = new Intent(getActivity(), ActivityAdviseBack.class);
                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
                break;
            case R.id.ll_myself_we://关于我们
                intent = new Intent(getActivity(), ActivityAboutUs.class);
                //intent = new Intent(getActivity(), ActivityCameraInfo.class);
                startActivity(intent);
//                intent = new Intent(getActivity(), VitamioVideoViewActivity.class);
//                intent.putExtra("movieUrl", "rtmp://rtmp.open.ys7.com/openlive/5b750e276ff44b2ab46dfaff1507fc86");
//                startActivity(intent);
                getActivity().overridePendingTransition(0, 0);
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (SpTools.getBoolean(CommonUtils.getContext(), Constants.editUser, false)) {
            internet();
            SpTools.setBoolean(CommonUtils.getContext(), Constants.editUser, false);
        }
        //是否有修改家庭名称
        if (!TextUtils.isEmpty(SpTools.getString(CommonUtils.getContext(), Constants.homename, ""))) {
            tv_home.setText(SpTools.getString(CommonUtils.getContext(), Constants.homename, ""));
            SpTools.setString(CommonUtils.getContext(), Constants.homename, "");
        }
        if (SpTools.getBoolean(CommonUtils.getContext(), Constants.deletehome, false)) {
            internet();
        }
        if (!TextUtils.isEmpty(SpTools.getString(getActivity(), Constants.information, ""))) {
            mg_name = SpTools.getString(getActivity(), Constants.home_name, "");
            mg_id = SpTools.getString(getActivity(), Constants.information, "");
            mg_role = SpTools.getString(getActivity(), Constants.home_role, "");
            tv_home.setText(mg_name);
        }
        PopupWindow mPopWindow = FragmentHome.getPopWindow();
        if (mPopWindow != null && mPopWindow.isShowing()) {
            mPopWindow.dismiss();
        }
    }

//    @Override
//    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        // TODO: inflate a fragment view
//        View rootView = super.onCreateView(inflater, container, savedInstanceState);
//        unbinder = ButterKnife.bind(this, rootView);
//        return rootView;
//    }
//
//    @Override
//    public void onDestroyView() {
//        super.onDestroyView();
//        unbinder.unbind();
//    }
}