package com.base.project.activity.facility;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.Constants;
import com.base.project.utils.SpTools;
import com.base.utilslibrary.internet.GetControlHistoryRequestUtils;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityBindbluecard extends AppCompatActivity {

    @BindView(R.id.iv_base_backto)
    ImageView ivBaseBackto;
    @BindView(R.id.tv_base_title)
    TextView tvBaseTitle;
    @BindView(R.id.edit_bind)
    EditText editBind;
    @BindView(R.id.iv_base_member_add)
    ImageView ivBaseMemberAdd;
    @BindView(R.id.texe_bind)
    TextView texeBind;
    @BindView(R.id.iv_base_add)
    ImageView ivBaseAdd;
    private String ub_id;
    private String dd_ui_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bindbluecard);
        ButterKnife.bind(this);
        ivBaseAdd.setVisibility(View.GONE);
        ivBaseBackto.setVisibility(View.VISIBLE);
        tvBaseTitle.setVisibility(View.VISIBLE);
        tvBaseTitle.setText("设置");
        dd_ui_id = String.valueOf(getIntent().getIntExtra("dd_ui_id", 0));
        ub_id = SpTools.getString(this, Constants.userId, "0");
        Log.i("qaz", "onCreate: " + dd_ui_id +"-----"+ub_id);
        if (!dd_ui_id.equals(ub_id)) {
            texeBind.setVisibility(View.GONE);
        }
        initListener();
        bindbluecard(dd_ui_id, "", "3");
    }

    private void initListener() {
        ivBaseBackto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        texeBind.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("qaz", "gggggggggggggg");
                if (!TextUtils.isEmpty(editBind.getText().toString())) {
                    if (texeBind.getText().toString().equals("绑定")) {
                        bindbluecard(dd_ui_id, editBind.getText().toString(), "1");
                    } else {
                        bindbluecard(dd_ui_id, editBind.getText().toString(), "2");
                    }
                }
            }
        });
    }

    private void bindbluecard(String ui_id, String cardID, final String type) {
        GetControlHistoryRequestUtils.getbindbluecard(ui_id, cardID, type, new GetControlHistoryRequestUtils.BindbluecardListener() {
            @Override
            public void onResponseMessage(String error, String errno, String cardid) {


                if ("1".equals(type)) {
                    if ("0".equals(errno)) {
                        Log.i("qaz", "绑定1返回的数据: "+errno +"====="+error+"====="+cardid);
                        CommonUtils.toastMessage(error);
                        if (error.equals("成功")) {
                            if (texeBind.getText().toString().equals("绑定")) {
                                texeBind.setText("解绑");
                            } else {
                                texeBind.setText("绑定");
                            }
                        }
                    }else {
                        CommonUtils.toastMessage("绑定失败，请重试");
                        Log.i("qaz", "绑定2返回的数据: "+errno +"====="+error+"====="+cardid);
                    }
                } else if ("3".equals(type)) {

                    if ("0".equals(errno)) {
                        CommonUtils.toastMessage("成功");
                        Log.i("qaz", "查询3返回的数据: "+errno +"====="+error+"====="+cardid);
                        if (error.equals("成功")) {
                            editBind.setText(cardid);
                            texeBind.setText("解绑");
                            editBind.setSelection(cardid.length());
                            editBind.setFocusable(false);
                            editBind.setFocusableInTouchMode(false);
                            //editBind.setCursorVisible(false);
                        }
                    } else {

                        CommonUtils.toastMessage("查询失败，请重试");
                        Log.i("qaz", "查询4返回的数据: "+errno +"====="+error+"====="+cardid);
                    }
                }else if ("2".equals(type)){
                    if ("0".equals(errno)) {
                        CommonUtils.toastMessage(error);
                        if (error.equals("成功")) {
                            Log.i("qaz", "解绑5返回的数据: "+errno +"====="+error+"====="+cardid);
                            if (texeBind.getText().toString().equals("绑定")) {
                                texeBind.setText("解绑");
                                editBind.setHint("请输入识别码");
                            } else {
                                editBind.setHint("请输入识别码");
                                texeBind.setText("绑定");
                            }
                        }
                    }else {
                        CommonUtils.toastMessage("解绑失败，请重试");
                        Log.i("qaz", "解绑6返回的数据: "+errno +"====="+error+"====="+cardid);
                    }
                }else{
                    Log.i("qaz", "错误错误错误错误错误错误错误 ");
                }

            }
        });
    }
}
