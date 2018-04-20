package com.zihao.radar;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.zihao.radar.view.RadarView;

public class MainActivity extends AppCompatActivity {

    private RadarView mRadarView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
    }

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