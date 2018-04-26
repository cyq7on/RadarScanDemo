package com.zihao.radar;

import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Pair;
import android.view.View;
import android.widget.Toast;

import com.zihao.radar.view.RadarView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RadarView mRadarView;
    private LocationManager locationManager;
    private String locationProvider;       //位置提供器
    private Location location;
    //平面坐标
    private Map<String, Double> firstLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        mRadarView.setMaxDinstance(14100);
        getLocation(getApplicationContext());
    }

    private void createData(Map<String, Double> location) {
        List<Pair<Float,Float>> list = new ArrayList<>();
        list.add(new Pair<>(-1000f, 1000f));
        list.add(new Pair<>(1000f, 1000f));
        list.add(new Pair<>(-1000f, -1000f));
        list.add(new Pair<>(1000f, -1000f));
        mRadarView.addPoint(list,location);
    }

    private void getLocation(Context context) {
        //1.获取位置管理器
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        //2.获取位置提供器，GPS或是NetWork
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是网络定位
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS定位
            locationProvider = LocationManager.GPS_PROVIDER;
        } else {
            Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return;
        }

        //3.获取上次的位置，一般第一次运行，此值为null
        location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            firstLocation = DistanceUtils.convertLL2MC(location.getLongitude(), location.getLatitude());
            mRadarView.postDelayed(new Runnable() {
                @Override
                public void run() {
                    createData(firstLocation);
                }
            },1000);

            LogUtil.d("纬度：" + location.getLatitude() + "经度：" + location.getLongitude());
            LogUtil.d("x：" + firstLocation.get("x") + "y：" + firstLocation.get("y"));
        } else {
            // 监视地理位置变化，第二个和第三个参数分别为更新的最短时间minTime和最短距离minDistace
            locationManager.requestLocationUpdates(locationProvider, 0, 0, mListener);
        }
    }


    LocationListener mListener = new LocationListener() {
        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
        }

        @Override
        public void onProviderEnabled(String provider) {
        }

        @Override
        public void onProviderDisabled(String provider) {
        }

        // 如果位置发生变化，重新显示
        @Override
        public void onLocationChanged(Location location) {

            Map<String, Double> map = null;
            if(location != null){
                map = DistanceUtils.convertLL2MC(location.getLongitude(), location.getLatitude());
            }
            if (MainActivity.this.location == null) {
                firstLocation = map;
            }
            createData(map);
            MainActivity.this.location = location;
            LogUtil.d("onLocationChanged--纬度：" + location.getLatitude() + "经度：" + location.getLongitude());

        }
    };

    /**
     * TODO <初始化视图/>
     */
    private void initView(){
        mRadarView = (RadarView) findViewById(R.id.radar_view);
        mRadarView.setSearching(true);
        mRadarView.addPoint();
        mRadarView.addPoint();
    }

    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.add:
                mRadarView.addPoint();
                break;
            default:
                mRadarView.clear();
                break;
        }
    }

}