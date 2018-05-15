package com.base.project.activity.myself;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.base.project.R;
import com.base.project.bean.address.City;
import com.base.project.bean.address.CityPickerDialog;
import com.base.project.bean.address.County;
import com.base.project.bean.address.Province;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.Constants;
import com.base.project.utils.SpTools;
import com.base.project.view.SelectPicPopupWindow;
import com.base.utilslibrary.bean.ProjectUrlInfo;
import com.base.utilslibrary.internet.PersonalInternetRequestUtils;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import org.json.JSONArray;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import jp.wasabeef.glide.transformations.CropCircleTransformation;

public class ActivityMyselfInformation extends AppCompatActivity implements View.OnClickListener{
    @BindView(R.id.iv_base_back)
    ImageView back;
    @BindView(R.id.iv_base_add)
    ImageView add;
    @BindView(R.id.tv_base_title)
    TextView title;
    @BindView(R.id.tv_base_confirm)
    TextView confirm;


    @BindView(R.id.ll_myself_information_date)
    LinearLayout date;
    @BindView(R.id.ll_myself_information_address)
    LinearLayout address;
    @BindView(R.id.ll_myself_information_name)
    LinearLayout name;
    @BindView(R.id.ll_myself_information_fale)
    LinearLayout fale;


    @BindView(R.id.tv_myself_information_date)
    TextView textDate;
    @BindView(R.id.tv_myself_information_address)
    TextView textAddress;
    @BindView(R.id.tv_myself_information_name)
    TextView textName;
    @BindView(R.id.tv_myself_information_fale)
    TextView textFale;
    @BindView(R.id.et_myself_information_name)
    EditText editName;

    @BindView(R.id.iv_myself_information_img)
    ImageView img;
    @BindView(R.id.ll_myself_information_content)
    LinearLayout content;
    private SelectPicPopupWindow mPopupWindow;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myself_info);
        ButterKnife.bind(this);

        title.setText("个人信息");
        add.setVisibility(View.INVISIBLE);
        confirm.setVisibility(View.VISIBLE);

        initdata();
        initListener();
    }

    private void initListener() {
        back.setOnClickListener(this);
        confirm.setOnClickListener(this);
        date.setOnClickListener(this);
        address.setOnClickListener(this);
        name.setOnClickListener(this);
        fale.setOnClickListener(this);
        img.setOnClickListener(this);
        editName.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                switch(actionId){
                    case EditorInfo.IME_ACTION_DONE:
                        saveText();
                        break;
                }
                return false;
            }
        });
        editName.setOnClickListener(new View.OnClickListener() {//设置这个为了点击时光标在最右边
            @Override
            public void onClick(View v) {
                editName.requestFocus();
                editName.setSelection(editName.getText().length());
            }
        });
        editName.setOnTouchListener(new View.OnTouchListener() {//设置这个为了点击时光标在最右边
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                editName.requestFocus();
                editName.setSelection(editName.getText().length());
                return false;
            }
        });
    }

    private void initdata() {
        Bitmap cacheBitmap = CommonUtils.getCacheFile("myicon.jpg");
        if(cacheBitmap !=null){
            byte[] bytes=CommonUtils.getBitMapByteArray(cacheBitmap);
            Glide.with(this).load(bytes)
                    .placeholder(R.drawable.img_default_person)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(img);
        }else {
            Glide.with(this).load(ProjectUrlInfo.service_host_address+ getIntent().getStringExtra("ud_photo_fileid"))
                    .placeholder(R.drawable.img_default_person)
                    .bitmapTransform(new CropCircleTransformation(this))
                    .into(img);
        }

        String nickname = getIntent().getStringExtra("nn");
        if(TextUtils.isEmpty(nickname)){
            textName.setText("请输入您的昵称");
        }else {
            textName.setText(nickname);
        }
        String borth = getIntent().getStringExtra("borth");
        if(TextUtils.isEmpty(borth)){
            textDate.setText("请输入您的生日");
        }else {
            textDate.setText(borth);
        }
        String sex = getIntent().getStringExtra("sex");
        if(TextUtils.isEmpty(sex)){
            textFale.setText("男");
        }else {
            textFale.setText(sex);
        }
        String addr = getIntent().getStringExtra("addr");
        if(TextUtils.isEmpty(addr)){
            textAddress.setText("请输入您的所在地");
        }else {
            textAddress.setText(addr);
        }

    }
    @Override
    public void onClick(View v) {
        if (mPopupWindow != null) {
            mPopupWindow.dismiss();
        }
        switch (v.getId()){
            case R.id.iv_base_back://退出
                finish();
                break;
            case R.id.tv_base_confirm://保存个人信息
                if (TextUtils.isEmpty(editName.getText().toString())) {
                    editName.setText(textName.getText().toString());
                }
                PersonalInternetRequestUtils.editUserInformation(ActivityMyselfInformation.this,
                        SpTools.getString(ActivityMyselfInformation.this, Constants.userId, "0")
                        , editName.getText().toString(), textDate.getText().toString(), textAddress.getText().toString()
                        , textFale.getText().toString(), new PersonalInternetRequestUtils.ForResultListener() {
                            @Override
                            public void onResponseMessage(String code) {
                                if("成功".equals(code)){
                                    SpTools.setBoolean(CommonUtils.getContext(),Constants.editUser,true);
                                }
                            }
                        });
                break;
            case R.id.ll_myself_information_fale:
                final AlertDialog mDialog;
                AlertDialog.Builder builder = new AlertDialog.Builder(this); //先得到构造器
                mDialog = builder.create();
                mDialog.show();
                View view = View.inflate(this, R.layout.activity_dialog_fale, null);
                mDialog.getWindow().setContentView(view);
                view.findViewById(R.id.bt_fale_man).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textFale.setText("男");
                        mDialog.dismiss();
                    }
                });
                view.findViewById(R.id.bt_fale_woman).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        textFale.setText("女");
                        mDialog.dismiss();
                    }
                });
                break;
            case R.id.ll_myself_information_date:
                if(TextUtils.isEmpty(editName.getText().toString()) && textName.getText().toString().equals("请输入您的昵称")){
                    CommonUtils.toastMessage("请输入您的昵称，不能超过8个字");
                    return;
                }
                saveText();
                getTime();
                break;
            case R.id.ll_myself_information_address:
                if(TextUtils.isEmpty(editName.getText().toString()) && textName.getText().toString().equals("请输入您的昵称")){
                    CommonUtils.toastMessage("请输入您的昵称，不能超过8个字");
                    return;
                }
                saveText();
                getAddress();
                break;
            case R.id.ll_myself_information_name:
                getUserName(v);
                break;
            case R.id.iv_myself_information_img:
                mPopupWindow = getPicPopupWindow(this, this, content);
                break;
            case R.id.btn_pick_photo://本地
                PictureSelector.create(this).openGallery(PictureMimeType.ofImage())
                        .selectionMode(PictureConfig.SINGLE)//多选 or 单选
                        .enableCrop(true)//是否裁剪
                        .compress(true)//是否压缩
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                        .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)
                        //系统自带 or 鲁班压缩 PictureConfig.SYSTEM_COMPRESS_MODE or LUBAN_COMPRESS_MODE
                        .previewImage(false)// 是否可预览图片
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                break;
            case R.id.btn_take_photo://拍照
                PictureSelector.create(this).openCamera(PictureMimeType.ofImage())
                        .selectionMode(PictureConfig.SINGLE)//多选 or 单选
                        .enableCrop(true)//是否裁剪
                        .compress(true)//是否压缩
                        .freeStyleCropEnabled(true)// 裁剪框是否可拖拽
                        .compressMode(PictureConfig.LUBAN_COMPRESS_MODE)
                        .previewImage(false)// 是否可预览图片
                        .forResult(PictureConfig.CHOOSE_REQUEST);//结果回调onActivityResult code
                break;
            case R.id.btn_cancel://取消
                mPopupWindow.dismiss();
                break;
        }
    }
    public SelectPicPopupWindow getPicPopupWindow(Context context, View.OnClickListener itemsOnClick
            , View viewAttach) {
        //实例化SelectPicPopupWindow
        SelectPicPopupWindow menuWindow = new SelectPicPopupWindow(context, itemsOnClick);
        //显示窗口,设置layout在PopupWindow中显示的位置
        menuWindow.showAtLocation(viewAttach, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, 0);
        return menuWindow;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case PictureConfig.CHOOSE_REQUEST:
                    // 图片选择结果回调
                    LocalMedia media = PictureSelector.obtainMultipleResult(data).get(0);
                    // 例如 LocalMedia 里面返回三种path
                    // 1.media.getPath(); 为原图path
                    // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true
                    // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true
                    // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的
                    final Bitmap zoomBitMap = CommonUtils.getBitmap(media.getCompressPath());
                    if(zoomBitMap!=null){
                        //先保持到本地，在传到服务器
                        CommonUtils.saveBitmapFile(zoomBitMap,"myicon.jpg");//先保存文件到本地
                        PersonalInternetRequestUtils.uplodePicture(ActivityMyselfInformation.this,
                                SpTools.getString(ActivityMyselfInformation.this, Constants.userId, "0")
                                ,"myicon.jpg",new PersonalInternetRequestUtils.ForResultListener() {
                            @Override
                            public void onResponseMessage(String code) {
                                if("成功".equals(code)){
                                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                                    zoomBitMap.compress(Bitmap.CompressFormat.PNG, 100, baos);
                                    byte[] bytes=baos.toByteArray();
                                    Glide.with(ActivityMyselfInformation.this).load(bytes)
                                            .placeholder(R.drawable.img_default_person)
                                            .bitmapTransform(new CropCircleTransformation(ActivityMyselfInformation.this))
                                            .into(img);
                                    SpTools.setBoolean(CommonUtils.getContext(),Constants.editUser,true);
                                }else {
                                    return;
                                }
                            }
                        });
                    }else {
                        //本地图片显示
                        Bitmap cacheBitmap = CommonUtils.getCacheFile("myicon.jpg");
                        byte[] bytes=CommonUtils.getBitMapByteArray(cacheBitmap);
						Glide.with(ActivityMyselfInformation.this).load(bytes)
								.placeholder(R.drawable.img_default_person)
								.bitmapTransform(new CropCircleTransformation(ActivityMyselfInformation.this))
								.into(img);
                    }
                    break;
            }
        }
    }

    private boolean isText = false;
    private void saveText() {
        if(TextUtils.isEmpty(editName.getText().toString())&& textName.getText().toString().equals("请输入您的昵称")){
            CommonUtils.toastMessage("请输入您的昵称，不能超过8个字");
            return;
        }
        if(isText){
            isText = false;
            textName.setVisibility(View.VISIBLE);
            editName.setVisibility(View.GONE);
            textName.setText(editName.getText().toString());
        }
    }
    private void getUserName(View v) {
        isText = true;
        if(textName.isShown()){
            textName.setVisibility(View.GONE);
            editName.setVisibility(View.VISIBLE);
            if(textName.getText().toString().equals("请输入您的昵称")){
                editName.setText("");
            }else {
                editName.setText(textName.getText().toString());
            }
            editName.setFocusable(true);
            editName.setFocusableInTouchMode(true);
            editName.requestFocus();
            editName.findFocus();
            InputMethodManager imm = (InputMethodManager)v.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.toggleSoftInput(0,InputMethodManager.SHOW_FORCED);
        }
    }
    private void getTime(){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(System.currentTimeMillis());
        DatePickerDialog pickerDialog =new DatePickerDialog(ActivityMyselfInformation.this
                , R.style.MydateStyle, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                textDate.setText(year+"-"+(month+1)+"-"+dayOfMonth);
            }
        }, cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
        pickerDialog.show();
    }
    private ArrayList<Province> provinces = new ArrayList<Province>();
    public void getAddress() {
        if (provinces.size() > 0) {
            showAddressDialog();
        } else {
            new InitAreaTask(ActivityMyselfInformation.this).execute(0);
        }
    }
    private void showAddressDialog() {
        new CityPickerDialog(ActivityMyselfInformation.this, provinces, null, null, null,
                new CityPickerDialog.onCityPickedListener() {
                    @Override
                    public void onPicked(Province selectProvince,
                                         City selectCity, County selectCounty) {
                        StringBuilder address = new StringBuilder();
                        String provine = selectProvince != null ? selectProvince.getAreaName() : "";
                        String city = selectCity != null ? selectCity.getAreaName() : "";
                        String country = selectCounty != null ? selectCounty.getAreaName() : "";
                        if(TextUtils.isEmpty(country)){
                            address.append(provine).append("-").append(city);
                        }else{
                            address.append(provine).append("-").append(city).append("-").append(country);
                        }
//                        address.append(selectProvince != null ? selectProvince.getAreaName() : "")
//                                .append(selectCity != null ? selectCity.getAreaName() : "")
//                                .append(selectCounty != null ? selectCounty.getAreaName() : "");
                        textAddress.setText(address);
                    }
                }).show();
    }
    private class InitAreaTask extends AsyncTask<Integer, Integer, Boolean> {
        Context mContext;
        public InitAreaTask(Context context) {
            mContext = context;
        }
        @Override
        protected void onPreExecute() {
        }
        @Override
        protected void onPostExecute(Boolean result) {
            if (provinces.size()>0) {
                showAddressDialog();
            } else {
                Toast.makeText(mContext, "数据初始化失败", Toast.LENGTH_SHORT).show();
            }
        }
        @Override
        protected Boolean doInBackground(Integer... params) {
            String address;
            InputStream in = null;
            try {
                in = mContext.getResources().getAssets().open("address.txt");
                byte[] arrayOfByte = new byte[in.available()];
                in.read(arrayOfByte);
                address = new String(arrayOfByte, "UTF-8");
                JSONArray jsonList = new JSONArray(address);
                Gson gson = new Gson();
                for (int i = 0; i < jsonList.length(); i++) {
                    try {
                        provinces.add(gson.fromJson(jsonList.getString(i),Province.class));
                    } catch (Exception e) {
                    }
                }
                return true;
            } catch (Exception e) {
            } finally {
                if (in != null) {
                    try {
                        in.close();
                    } catch (IOException e) {
                    }
                }
            }
            return false;
        }

    }
}
