package com.example.nahcoos.petproject;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import net.daum.mf.map.api.MapPoint;
import net.daum.mf.map.api.MapView;


public class MainActivity extends Activity {

    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        // 뒤로가기 두번 눌러야 종료
        backPressCloseHandler = new BackPressCloseHandler(this);

        MapView mapView = new MapView(this);
        mapView.setDaumMapApiKey("7f9bb3ea788cb3465af03145bed53499");

        ViewGroup mapViewContainer = (ViewGroup) findViewById(R.id.map_view);
        mapViewContainer.addView(mapView);

        // 중심점 변
        mapView.setMapCenterPoint(MapPoint.mapPointWithGeoCoord(33.450701, 126.570667), true);

        // 줌 레벨 변경
        mapView.setZoomLevel(2, true);

        // 줌 인
        mapView.zoomIn(true);

        // 줌 아웃
        mapView.zoomOut(true);
    }

    public void goBoard(View view) {
        switch (view.getId()) {
            case R.id.board_pet:
                Toast.makeText(this, "다녀올개 게시판", Toast.LENGTH_SHORT).show();
                break;
            case R.id.board_hum:
                Toast.makeText(this, "다녀오개 게시판", Toast.LENGTH_SHORT).show();
                break;
        }
    }

    public void advertise(View view) {
        Toast.makeText(this, "광고 보기", Toast.LENGTH_SHORT).show();
    }

    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }
}
