package com.base.project.activity.facility;

import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.base.project.R;
import com.base.project.bean.WeatherBean;
import com.base.project.fragment.FragmentHome;
import com.github.mikephil.charting.charts.HorizontalBarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ActivityTemperature extends AppCompatActivity implements OnChartValueSelectedListener {

    @BindView(R.id.iv_base_backto)
    ImageView ivBaseBackto;
    @BindView(R.id.iv_base_back)
    ImageView ivBaseBack;
    @BindView(R.id.tv_base_title)
    TextView tvBaseTitle;
    @BindView(R.id.iv_base_edit)
    ImageView ivBaseEdit;
    @BindView(R.id.iv_base_add)
    ImageView ivBaseAdd;
    @BindView(R.id.iv_base_member_add)
    ImageView ivBaseMemberAdd;
    @BindView(R.id.tv_base_flour)
    TextView tvBaseFlour;
    @BindView(R.id.tv_base_confirm)
    TextView tvBaseConfirm;
    @BindView(R.id.img_temp_more)
    ImageView imgTempMore;
    @BindView(R.id.text_temp_shinei)
    TextView textTempShinei;
    @BindView(R.id.temp_horizon_barchart)
    HorizontalBarChart mHorizontalBarChart;
    @BindView(R.id.img_temp_more1)
    ImageView imgTempMore1;
    @BindView(R.id.text_temp_shinei1)
    TextView textTempShinei1;
    @BindView(R.id.temp_horizon_barchart1)
    HorizontalBarChart tempHorizonBarchart1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temperature);
        ButterKnife.bind(this);
        initListener();

        tvBaseTitle.setText("温度");
        ivBaseEdit.setVisibility(View.GONE);
        ivBaseAdd.setVisibility(View.GONE);
        ivBaseMemberAdd.setVisibility(View.GONE);
        tvBaseFlour.setVisibility(View.GONE);
        if (TextUtils.isEmpty(getIntent().getStringExtra("temp"))) {
            textTempShinei.setText("℃");
        }else{
            textTempShinei.setText(getIntent().getStringExtra("temp") + "℃");
        }
        if (TextUtils.isEmpty(getIntent().getStringExtra("wendu"))) {
            textTempShinei1.setText("0℃");
        }else{
            textTempShinei1.setText(getIntent().getStringExtra("wendu") + "℃");
        }


        if (!TextUtils.isEmpty(getIntent().getStringExtra("ch2o")) && !TextUtils.isEmpty(getIntent().getStringExtra("co2"))
                && !TextUtils.isEmpty(getIntent().getStringExtra("pm25"))
                && !TextUtils.isEmpty(getIntent().getStringExtra("light"))&&
                !TextUtils.isEmpty(getIntent().getStringExtra("noise"))&&
                !TextUtils.isEmpty(getIntent().getStringExtra("humi"))) {

            setmHorizontalBarChart(getIntent().getStringExtra("ch2o"), getIntent().getStringExtra("co2"),
                    getIntent().getStringExtra("pm25"), getIntent().getStringExtra("light"),
                    getIntent().getStringExtra("noise"), getIntent().getStringExtra("humi"));
        }

        setmHorizontalBarChartTwo(getIntent().getIntExtra("pm2_5", 0), getIntent().getIntExtra("aqi", 0),
                getIntent().getDoubleExtra("co", 0), getIntent().getIntExtra("pm10", 0),
                getIntent().getIntExtra("so2", 0), getIntent().getIntExtra("no2", 0));
    }

    private void initListener() {
        ivBaseBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        FragmentHome.setOnTempChangeListener(new FragmentHome.OnTempChangeListener() {
            @Override
            public void onTempChange(final String temp, final String ch2o,final String co2,final String pm25
                    , final String light,final String noise, final String humi) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textTempShinei.setText(temp + "℃");
                        if (temp != null && ch2o != null && co2
                                != null && pm25 != null && light != null
                                && noise != null && humi != null) {
                            Log.i("qaz", "zhuzhuangtu -----: ");
                            setmHorizontalBarChart(ch2o, co2,
                                    pm25,light, noise, humi);

                        }

                    }
                });

            }


        });
        FragmentHome.setOnTempOutdoorListener(new FragmentHome.OnTempOutdoorListener() {
            @Override
            public void onTempOutdoor(final WeatherBean infoBean ,final String wendu) {

                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textTempShinei1.setText(wendu + "℃");

                            setmHorizontalBarChartTwo(infoBean.pm2_5, infoBean.aqi,
                                    infoBean.co, infoBean.pm10, infoBean.so2, infoBean.no2);

                    }
                });
            }
        });
    }

    private void setmHorizontalBarChart(String ch2o, String co2, String pm25, String light, String noise, String humi) {
      //  Log.i("qaz", "zhuzhuangtu 1: ");
        float a = Float.parseFloat(ch2o); //可燃气体
        float b = Float.parseFloat(co2);    //二氧化碳
        float c = Float.parseFloat(pm25);
        float d = Float.parseFloat(light);  //光照
        float e = Float.parseFloat(noise);  //噪音
        float f = Float.parseFloat(humi);   // 湿度
        //设置相关属性
        // mHorizontalBarChart.setOnChartValueSelectedListener(this);
        // mHorizontalBarChart.setNoDataTextColor(R.color.quitRed);
        //mHorizontalBarChart.setNoDataText("正在加载数据，请稍候");
        mHorizontalBarChart.setDrawBarShadow(false); //如果设置为true，会在各条 bar 后面绘制 “灰色全 bar”，用以指示最大值。 启用会降低性能约 40％ 。默认：false
        mHorizontalBarChart.setDrawValueAboveBar(true); //如果设置为true，所有值都高于其 bar 的，而不是低于其顶部
        mHorizontalBarChart.getDescription().setEnabled(false);
        mHorizontalBarChart.setMaxVisibleValueCount(8);
        mHorizontalBarChart.setPinchZoom(false);
        mHorizontalBarChart.setDrawGridBackground(false);
        mHorizontalBarChart.setTouchEnabled(true);
        mHorizontalBarChart.setDragEnabled(true); //: 启用/禁用拖动（平移）图表。
        mHorizontalBarChart.setScaleEnabled(true); //: 启用/禁用缩放图表上的两个轴。
        mHorizontalBarChart.setScaleXEnabled(false); //: 启用/禁用缩放在x轴上。
        mHorizontalBarChart.setScaleYEnabled(false); //: 启用/禁用缩放在y轴。
        // mHorizontalBarChart.setPinchZoom(true); //: 如果设置为true，捏缩放功能。 如果false，x轴和y轴可分别放大。
        mHorizontalBarChart.setDoubleTapToZoomEnabled(false); //: 设置为false以禁止通过在其上双击缩放图表。
        mHorizontalBarChart.setHighlightPerDragEnabled(false);// : 设置为true，允许每个图表表面拖过，当它完全缩小突出。 默认值：true
        mHorizontalBarChart.setHighlightPerTapEnabled(false); //:设置为false，以防止值由敲击姿态被突出显示。 值仍然可以通过拖动或编程方式突出显示。 默认值：true
       // Log.i("qaz", "zhuzhuangtu 2: ");
        Legend legend = mHorizontalBarChart.getLegend();

        legend.setEnabled(false);
        //x轴
        XAxis xl = mHorizontalBarChart.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setEnabled(true);
        xl.setDrawAxisLine(false);
        xl.setDrawGridLines(false);

        //xl.setGranularity(10f);


        //y轴
        YAxis yl = mHorizontalBarChart.getAxisLeft();
        yl.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yl.setEnabled(false);
        yl.setDrawAxisLine(false);
        yl.setDrawGridLines(false);
        yl.setAxisMinimum(0f);
        //y轴
        YAxis yr = mHorizontalBarChart.getAxisRight();
        yr.setEnabled(false);
        yr.setDrawAxisLine(false);
        yr.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yr.setDrawGridLines(false);

        // yr.setAxisMinimum(0f);


        final String lable[] = {"可燃气体", "二氧化碳", "PM2.5", "光照", "噪音", "湿度"};
        xl.setLabelCount(lable.length);
        xl.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //  Log.i("qaz", "getFormattedValue: " + value);
                return lable[(int) value];
            }
        });
       // Log.i("qaz", "zhuzhuangtu 3: ");
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, a));
        entries.add(new BarEntry(1f, b));
        entries.add(new BarEntry(2f, c));
        entries.add(new BarEntry(3f, d));
        entries.add(new BarEntry(4f, e));
        entries.add(new BarEntry(5f, f));

        BarDataSet set = new BarDataSet(entries, "");
        // set.setBarBorderColor(R.drawable.temp_wheather_color);
        // set.setBarShadowColor(R.drawable.temp_wheather_color);
        //set.setColor(R.color.tempshinei);
        //set.setColors(R.drawable.temp_wheather_color);

        //  Color.rgb(255, 128, 0);

        set.setColor(Color.rgb(50, 205, 50));
        set.setDrawValues(true);
        //  set.setBarBorderColor(R.drawable.temp_wheather_color);
        //set.setHighLightColor(R.drawable.temp_wheather_color);
        // set.setColor(ColorTemplate.getHoloBlue());

        BarData data = new BarData(set);
        // data.setBarWidth(0.9f); //设置自定义条形宽度
        //Log.i("qaz", "zhuzhuangtu 4: ");
        mHorizontalBarChart.setData(data);
        mHorizontalBarChart.setFitBars(true); //使x轴完全适合所有条形
        mHorizontalBarChart.invalidate();
        mHorizontalBarChart.notifyDataSetChanged();
        //   mHorizontalBarChart.animateX(3000);
        //  mHorizontalBarChart.animateY(2500);


    }

    private void setmHorizontalBarChartTwo(int pm2_5, int aqi, double co, int pm10, int so2, int no2) {
        //Log.i("qaz", "zhuzhuangtu 1: ");
        float a = pm2_5; //
        float b = aqi;    //空气质量
        float c = (float) co; //一氧化碳
        float d = pm10;  //
        float e = so2;  //二氧化硫
        float f = no2;   // 二氧化氮
        //设置相关属性
        // mHorizontalBarChart.setOnChartValueSelectedListener(this);
        // mHorizontalBarChart.setNoDataTextColor(R.color.quitRed);
        //mHorizontalBarChart.setNoDataText("正在加载数据，请稍候");
        tempHorizonBarchart1.setDrawBarShadow(false); //如果设置为true，会在各条 bar 后面绘制 “灰色全 bar”，用以指示最大值。 启用会降低性能约 40％ 。默认：false
        tempHorizonBarchart1.setDrawValueAboveBar(true); //如果设置为true，所有值都高于其 bar 的，而不是低于其顶部
        tempHorizonBarchart1.getDescription().setEnabled(false);
        tempHorizonBarchart1.setMaxVisibleValueCount(8);
        tempHorizonBarchart1.setPinchZoom(false);
        tempHorizonBarchart1.setDrawGridBackground(false);
        tempHorizonBarchart1.setTouchEnabled(true);
        tempHorizonBarchart1.setDragEnabled(true); //: 启用/禁用拖动（平移）图表。
        tempHorizonBarchart1.setScaleEnabled(true); //: 启用/禁用缩放图表上的两个轴。
        tempHorizonBarchart1.setScaleXEnabled(false); //: 启用/禁用缩放在x轴上。
        tempHorizonBarchart1.setScaleYEnabled(false); //: 启用/禁用缩放在y轴。
        // mHorizontalBarChart.setPinchZoom(true); //: 如果设置为true，捏缩放功能。 如果false，x轴和y轴可分别放大。
        tempHorizonBarchart1.setDoubleTapToZoomEnabled(false); //: 设置为false以禁止通过在其上双击缩放图表。
        tempHorizonBarchart1.setHighlightPerDragEnabled(false);// : 设置为true，允许每个图表表面拖过，当它完全缩小突出。 默认值：true
        tempHorizonBarchart1.setHighlightPerTapEnabled(false); //:设置为false，以防止值由敲击姿态被突出显示。 值仍然可以通过拖动或编程方式突出显示。 默认值：true
      //  Log.i("qaz", "zhuzhuangtu 2: ");
        Legend legend = tempHorizonBarchart1.getLegend();

        legend.setEnabled(false);
        //x轴
        XAxis xl = tempHorizonBarchart1.getXAxis();
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setEnabled(true);
        xl.setDrawAxisLine(false);
        xl.setDrawGridLines(false);

        //xl.setGranularity(10f);


        //y轴
        YAxis yl = tempHorizonBarchart1.getAxisLeft();
        yl.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yl.setEnabled(false);
        yl.setDrawAxisLine(false);
        yl.setDrawGridLines(false);
        yl.setAxisMinimum(0f);
        //y轴
        YAxis yr = tempHorizonBarchart1.getAxisRight();
        yr.setEnabled(false);
        yr.setDrawAxisLine(false);
        yr.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);
        yr.setDrawGridLines(false);

        // yr.setAxisMinimum(0f);


        final String lable[] = {"PM2.5", "空气质量", "一氧化碳", "PM10", "二氧化硫", "二氧化氮"};
        xl.setLabelCount(lable.length);
        xl.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                //  Log.i("qaz", "getFormattedValue: " + value);
                return lable[(int) value];
            }
        });
       // Log.i("qaz", "zhuzhuangtu 3: ");
        List<BarEntry> entries = new ArrayList<>();
        entries.add(new BarEntry(0f, a));
        entries.add(new BarEntry(1f, b));
        entries.add(new BarEntry(2f, c));
        entries.add(new BarEntry(3f, d));
        entries.add(new BarEntry(4f, e));
        entries.add(new BarEntry(5f, f));

        BarDataSet set = new BarDataSet(entries, "");
        // set.setBarBorderColor(R.drawable.temp_wheather_color);
        // set.setBarShadowColor(R.drawable.temp_wheather_color);
        //set.setColor(R.color.tempshinei);
        //set.setColors(R.drawable.temp_wheather_color);

        //  Color.rgb(255, 128, 0);

        set.setColor(Color.rgb(255, 128, 0));
        set.setDrawValues(true);
        //  set.setBarBorderColor(R.drawable.temp_wheather_color);
        //set.setHighLightColor(R.drawable.temp_wheather_color);
        // set.setColor(ColorTemplate.getHoloBlue());

        BarData data = new BarData(set);
        // data.setBarWidth(0.9f); //设置自定义条形宽度
       // Log.i("qaz", "zhuzhuangtu 4: ");
        tempHorizonBarchart1.setData(data);
        tempHorizonBarchart1.setFitBars(true); //使x轴完全适合所有条形
        tempHorizonBarchart1.invalidate();
        tempHorizonBarchart1.notifyDataSetChanged();
        //   mHorizontalBarChart.animateX(3000);
        //  mHorizontalBarChart.animateY(2500);


    }

    @Override
    public void onValueSelected(Entry e, Highlight h) {

    }

    @Override
    public void onNothingSelected() {

    }
}
