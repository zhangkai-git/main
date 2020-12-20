package com.example.ditu;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

import com.amap.api.maps.AMap;
import com.amap.api.maps.MapView;
import com.amap.api.maps.model.MyLocationStyle;
import com.amap.api.services.core.PoiItem;
import com.amap.api.services.poisearch.PoiResult;
import com.amap.api.services.poisearch.PoiSearch;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements PoiSearch.OnPoiSearchListener, AMap.OnMyLocationChangeListener {

    private AMap aMap;
    private MapView mapView;
    private EditText etMain;
    private Button btn;
    private PoiSearch.Query query;
    private int currentpage = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mapView = (MapView) findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);// 此方法必须重写
        aMap = mapView.getMap();
        init();
        initView();

    }

    private void init() {
        if (aMap == null) {
            aMap = mapView.getMap();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    private void initView() {
        etMain = (EditText) findViewById(R.id.et_name);
        btn = (Button) findViewById(R.id.btn_ok);

        MyLocationStyle myLocationStyle;
        myLocationStyle = new MyLocationStyle();//初始化定位蓝点样式类myLocationStyle.myLocationType(MyLocationStyle.LOCATION_TYPE_LOCATION_ROTATE);//连续定位、且将视角移动到地图中心点，定位点依照设备方向旋转，并且会跟随设备移动。（1秒1次定位）如果不设置myLocationType，默认也会执行此种模式。
        myLocationStyle.interval(2000); //设置连续定位模式下的定位间隔，只在连续定位模式下生效，单次定位模式下不会生效。单位为毫秒。
        aMap.setMyLocationStyle(myLocationStyle);//设置定位蓝点的Style
//aMap.getUiSettings().setMyLocationButtonEnabled(true);设置默认定位按钮是否显示，非必需设置。
        aMap.setMyLocationEnabled(true);// 设置为true表示启动显示定位蓝点，false表示隐藏定位蓝点并不进行定位，默认是false。

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String s = etMain.getText().toString();
                query = new PoiSearch.Query(s, "", "北京");
                query.setPageSize(10);
                query.setPageNum(currentpage);
                currentpage++;
                PoiSearch poiSearch = new PoiSearch(MainActivity.this, query);
                poiSearch.setOnPoiSearchListener(MainActivity.this);
                poiSearch.searchPOIAsyn();
            }
        });
    }


    @Override
    public void onPoiSearched(PoiResult poiResult, int i) {
        Log.e("TAG", "poi索引返回" + i);
        ArrayList<PoiItem> pois = poiResult.getPois();
        for (PoiItem item : pois) {
            Log.e("TAG", "当前返回打印地址: " + item.toString());
            Log.e("TAG", "当前返回打印地址: " + item.getAdName());
        }
        PoiOverlay poiOverlay = new PoiOverlay(aMap, pois);
        poiOverlay.removeFromMap();
        poiOverlay.addToMap();
        poiOverlay.zoomToSpan();
    }

    @Override
    public void onPoiItemSearched(PoiItem poiItem, int i) {

    }

    @Override
    public void onMyLocationChange(Location location) {

    }
}
