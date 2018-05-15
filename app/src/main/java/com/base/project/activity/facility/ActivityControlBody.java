package com.base.project.activity.facility;

import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.BounceInterpolator;
import android.view.animation.Interpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.location.AMapLocationQualityReport;
import com.amap.api.maps2d.AMap;
import com.amap.api.maps2d.CameraUpdate;
import com.amap.api.maps2d.CameraUpdateFactory;
import com.amap.api.maps2d.MapView;
import com.amap.api.maps2d.Projection;
import com.amap.api.maps2d.model.BitmapDescriptorFactory;
import com.amap.api.maps2d.model.CameraPosition;
import com.amap.api.maps2d.model.LatLng;
import com.amap.api.maps2d.model.Marker;
import com.amap.api.maps2d.model.MarkerOptions;
import com.amap.api.services.core.AMapException;
import com.amap.api.services.core.LatLonPoint;
import com.amap.api.services.geocoder.GeocodeResult;
import com.amap.api.services.geocoder.GeocodeSearch;
import com.amap.api.services.geocoder.RegeocodeQuery;
import com.amap.api.services.geocoder.RegeocodeResult;
import com.base.project.R;
import com.base.project.adapter.BlueRecordAdapter;
import com.base.project.adapter.WriteTimeAdapter;
import com.base.project.utils.AMapUtil;
import com.base.project.utils.CommonUtils;
import com.base.project.utils.Constants;
import com.base.project.utils.DateUtil;
import com.base.project.utils.SpTools;
import com.base.utilslibrary.bean.BluerecordForuserBean;
import com.base.utilslibrary.internet.GetControlHistoryRequestUtils;

import org.feezu.liuli.timeselector.TimeSelector;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 人体红外探测
 */
public class ActivityControlBody extends AppCompatActivity implements View.OnClickListener,
        AMap.OnMarkerClickListener ,GeocodeSearch.OnGeocodeSearchListener {

    @BindView(R.id.iv_base_backto)
    ImageView ivBaseBackto;
    @BindView(R.id.iv_base_back)
    ImageView ivBaseBack;
    @BindView(R.id.tv_base_title)
    TextView tvBaseTitle;
    @BindView(R.id.mapview)
    MapView mMapView;
    @BindView(R.id.iv_base_add)
    ImageView ivBaseAdd;
    @BindView(R.id.iv_base_shezhi)
    ImageView ivBaseShezhi;
    @BindView(R.id.text_hint)
    TextView textHint;
    @BindView(R.id.img_facility_incident)
    ImageView imgFacilityIncident;
    private WriteTimeAdapter writetimeAdapter;
    private TimeSelector timeSelector;
    private String macaddr;
    private int db_sbID;
    private int di_clusterID;
    private String db_sw;
    private String di_status;
    private String clustername;
    private String ui_id;
    private int dd_ui_id;
    private AMap aMap;
    private MarkerOptions markerOption;

    private double One;
    private double Two;
    private int day;
    private BlueRecordAdapter bluerecordAdapter;
    private List<BluerecordForuserBean> listsize;
    private LatLonPoint latLonPoint;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_control_body);
        ButterKnife.bind(this);
        mMapView.onCreate(savedInstanceState);


        transferData();
        ivBaseBackto.setVisibility(View.VISIBLE);
        tvBaseTitle.setVisibility(View.VISIBLE);
        ivBaseAdd.setVisibility(View.GONE);
        tvBaseTitle.setText(clustername);
        ivBaseShezhi.setVisibility(View.VISIBLE);
        //  dd_ui_id  = getIntent().getIntExtra("dd_ui_id" , 0);
        initListener();
        //initLocation();
    }

    private void setUpMap(double One, double Two) {
        aMap.setOnMarkerClickListener(this);
        geocoderSearch = new GeocodeSearch(this);
        geocoderSearch.setOnGeocodeSearchListener(this);
        latLonPoint = new LatLonPoint(Two, One);
        addMarkersToMap(AMapUtil.convertToLatLng(latLonPoint),addressName);// 往地图上添加marker
        Log.i("qaz", "setUpMap: " +latLonPoint);
      //  getAddress(latLonPoint);  //查询坐标对应的位置

    }

    /**
     * 在地图上添加marker
     */
    private Marker marker;

    private void addMarkersToMap(LatLng latLng,String addressName ) {
        Log.i("qaz", "22222222onResponseMessage: " +latLng);

        String title = "";
        String message = addressName;
       // LatLng CHENGDU = new LatLng(Two, One);
        changeCamera(
                CameraUpdateFactory.newCameraPosition(new CameraPosition(
                        latLng, 30, 30, 15)));
        aMap.clear();
        markerOption = new MarkerOptions().icon(BitmapDescriptorFactory
                .defaultMarker(BitmapDescriptorFactory.HUE_ORANGE))
                .position(latLng)
                .draggable(true);
        marker = aMap.addMarker(markerOption);
        marker.showInfoWindow();
    }

    /**
     * 根据动画按钮状态，调用函数animateCamera或moveCamera来改变可视区域
     */
    private void changeCamera(CameraUpdate update) {
        aMap.moveCamera(update);
    }
    /**
     * 响应逆地理编码
     */
    public void getAddress(final LatLonPoint latLonPoint) {

        RegeocodeQuery query = new RegeocodeQuery(latLonPoint, 100,
                GeocodeSearch.AMAP);// 第一个参数表示一个Latlng，第二参数表示范围多少米，第三个参数表示是火系坐标系还是GPS原生坐标系
        geocoderSearch.getFromLocationAsyn(query);// 设置异步逆地理编码请求
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.img_facility_incident:  // 事件记录
                Intent intent = new Intent(this, ActivityIncidentbody.class);
                intent.putExtra("clustername", clustername);
                intent.putExtra("macaddr", macaddr);
                intent.putExtra("db_sbID", db_sbID);
                intent.putExtra("di_clusterID", di_clusterID);
                intent.putExtra("db_sw", db_sw);
                intent.putExtra("di_status", di_status);
                intent.putExtra("dd_ui_id", dd_ui_id);
                startActivity(intent);
                break;
        }
    }

    /**
     * 点击事件
     */
    private void initListener() {
        imgFacilityIncident.setOnClickListener(this);
        ivBaseBackto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // stopLocation();
                if (aMap != null) {
                    aMap.clear();
                }
                finish();
            }
        });

        ivBaseShezhi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplication(), ActivityBindbluecard.class);
                intent.putExtra("dd_ui_id", dd_ui_id);
                startActivity(intent);
                overridePendingTransition(0, 0);
            }
        });

    }

    /**
     * fragmenthome 传过来的参数
     */
    private void transferData() {

        ui_id = SpTools.getString(this, Constants.userId, "0");

        clustername = getIntent().getStringExtra("clustername");

        macaddr = getIntent().getStringExtra("macaddr");

        db_sbID = getIntent().getIntExtra("db_sbID", 0);

        di_clusterID = getIntent().getIntExtra("di_clusterID", 0);

        di_status = getIntent().getStringExtra("di_status");

        dd_ui_id = getIntent().getIntExtra("dd_ui_id", 0);
        //  Log.i("qaz", "接收到的参数 " + "dd_ui_id" + dd_ui_id + "macaddr" + macaddr + "db_sbID" + db_sbID + "di_clusterID" + di_clusterID + "db_sw" + db_sw + "di_status" + di_status);
        bluerecordforuser(String.valueOf(dd_ui_id), DateUtil.getCurrentTime_Assignformat("yyyy-MM-dd"));
    }




    @Override
    protected void onDestroy() {
        super.onDestroy();
        //在activity执行onDestroy时执行mMapView.onDestroy()，销毁地图
        mMapView.onDestroy();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //在activity执行onResume时执行mMapView.onResume ()，重新绘制加载地图
        if (listsize == null) {
            bluerecordforuser(String.valueOf(dd_ui_id), DateUtil.getCurrentTime_Assignformat("yyyy-MM-dd"));
            mMapView.onResume();
        } else {
            mMapView.onResume();
        }


    }

    @Override
    protected void onPause() {
        super.onPause();
        //在activity执行onPause时执行mMapView.onPause ()，暂停地图的绘制
        mMapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        //在activity执行onSaveInstanceState时执行mMapView.onSaveInstanceState (outState)，保存地图当前的状态
        mMapView.onSaveInstanceState(outState);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        if (aMap != null) {
            jumpPoint(marker);
        }
        // Toast.makeText(this, "您点击了Marker", Toast.LENGTH_LONG).show();
        return true;
    }

    /**
     * marker点击时跳动一下
     */
    public void jumpPoint(final Marker marker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = aMap.getProjection();
        final LatLng markerLatlng = marker.getPosition();
        Point markerPoint = proj.toScreenLocation(markerLatlng);
        markerPoint.offset(0, -100);
        final LatLng startLatLng = proj.fromScreenLocation(markerPoint);
        final long duration = 1500;

        final Interpolator interpolator = new BounceInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * markerLatlng.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * markerLatlng.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    handler.postDelayed(this, 16);
                }
            }
        });
    }

    private void bluerecordforuser(String dd_ui_id, String time) {
       // Log.i("qaz", "bluerecordforuser: " + time);
        GetControlHistoryRequestUtils.getbluerecordforuser(dd_ui_id, time, new GetControlHistoryRequestUtils.BluerecordforuserListener() {
            @Override
            public void onResponseMessage(List<BluerecordForuserBean> message) {
                if (message == null) {
                    CommonUtils.toastMessage("暂无数据");
                    textHint.setVisibility(View.VISIBLE);
                    textHint.setText("请点击右上角设置或点击事件记录查看");
                    mMapView.setVisibility(View.GONE);
                } else {
                    textHint.setVisibility(View.GONE);
                    mMapView.setVisibility(View.VISIBLE);
                    String locaton = message.get(0).pr_location;
                    String[] s = locaton.split(",");
                    One = Double.parseDouble(s[0]);
                    Two = Double.parseDouble(s[1]);
                   // Log.i("qaz", "11111onResponseMessage: " +One +"----"+Two);
                    if (aMap == null) {
                        aMap = mMapView.getMap();
                        setUpMap(One, Two);
                    }
                }
            }
        });

    }

    private Marker regeoMarker;
    private String addressName;
    private GeocodeSearch geocoderSearch;

    /**
     * 逆地理编码回调
     */
    @Override
    public void onRegeocodeSearched(RegeocodeResult result, int rCode) {
        if (rCode == AMapException.CODE_AMAP_SUCCESS) {
            if (result != null && result.getRegeocodeAddress() != null
                    && result.getRegeocodeAddress().getFormatAddress() != null) {
                addressName = result.getRegeocodeAddress().	getDistrict()
                        + "附近";
                aMap.animateCamera(CameraUpdateFactory.newLatLngZoom(
                        AMapUtil.convertToLatLng(latLonPoint), 15));
               // CommonUtils.toastMessage(addressName);
                addMarkersToMap(AMapUtil.convertToLatLng(latLonPoint),addressName);// 往地图上添加marker
                Log.i("qaz", "onRegeocodeSearched: " +addressName);
                regeoMarker.setPosition(AMapUtil.convertToLatLng(latLonPoint));

            } else {
               // CommonUtils.toastMessage("对不起，没有搜索到相关数据！");
            }
        } else {
           // ToastUtil.showerror(this, rCode);
           // CommonUtils.toastMessage(String.valueOf(rCode));
        }
    }



    /**
     * 地理编码查询回调
     */
    @Override
    public void onGeocodeSearched(GeocodeResult geocodeResult, int i) {

    }
}