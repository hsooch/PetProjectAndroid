package com.example.nahcoos.petproject.mainview;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.nahcoos.petproject.R;
import com.example.nahcoos.petproject.helper.function.CompassView;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapPopup extends FragmentActivity {
    String TAG = this.getClass().getName();

    LocationManager manager = null;
    private boolean isGPSEnabled = false;
    private boolean isNetworkEnabled = false;

    private GoogleMap map;

    private RelativeLayout popup_map;
    private CompassView compassView;
    private SensorManager sensorManager;
    private boolean compassEnabled;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.popup_map);

        // 지도 레이아웃 객체 참조
        popup_map = (RelativeLayout) findViewById(R.id.popup_map);

        // 지도 객체 참조
        map = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();

        // 일부 단말의 문제로 인해 초기화 코드 추가
        try {
            MapsInitializer.initialize(this);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // 센서매니저 객체 참조
        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        // 나침반 표시할 뷰 생성
        boolean sideBottom = true;
        compassView = new CompassView(this);
        compassView.setVisibility(View.VISIBLE);

        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                RelativeLayout.LayoutParams.WRAP_CONTENT,
                RelativeLayout.LayoutParams.WRAP_CONTENT);
        params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
        params.addRule(sideBottom ? RelativeLayout.ALIGN_PARENT_BOTTOM : RelativeLayout.ALIGN_PARENT_TOP);

        //popup_map.addView(compassView, params);

        compassEnabled = true;

        // 위치 확인 후 표시
        startLocationService();

        checkDangerousPermissions();
    }

    private void checkDangerousPermissions() {
        String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.WRITE_EXTERNAL_STORAGE
        };

        int permissionCheck = PackageManager.PERMISSION_GRANTED;
        for (int i = 0; i < permissions.length; i++) {
            permissionCheck = ContextCompat.checkSelfPermission(this, permissions[i]);
            if (permissionCheck == PackageManager.PERMISSION_DENIED) {
                break;
            }
        }

        if (permissionCheck == PackageManager.PERMISSION_GRANTED) {
            //Toast.makeText(this, "권한 있음", Toast.LENGTH_LONG).show();
        } else {
            //Toast.makeText(this, "권한 없음", Toast.LENGTH_LONG).show();

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permissions[0])) {
                //Toast.makeText(this, "권한 설명 필요함.", Toast.LENGTH_LONG).show();
            } else {
                ActivityCompat.requestPermissions(this, permissions, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    //Toast.makeText(this, permissions[i] + " 권한이 승인됨.", Toast.LENGTH_LONG).show();
                } else {
                    //Toast.makeText(this, permissions[i] + " 권한이 승인되지 않음.", Toast.LENGTH_LONG).show();
                }
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        try {
            // 내 위치 자동 표시 enable
            map.setMyLocationEnabled(true);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        if (compassEnabled) {
            sensorManager.registerListener(seListener, sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER | Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onPause() {
        super.onPause();

        try {
            // 내 위치 자동 표시 disable
            map.setMyLocationEnabled(false);
        } catch (SecurityException e) {
            e.printStackTrace();
        }

        if (compassEnabled) {
            sensorManager.unregisterListener(seListener);
        }
    }

    // 위치 정보 확인을 위해 정의한 메소드
    private void startLocationService() {

        // 위치 관리자 객체 참조
        manager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        // 위치 정보를 받을 리스너 생성
        GPSListener gpsListener = new GPSListener();
        long minTime = 100000;
        float minDistance = 0;

        // GPS_PROVIDER: GPS를 통해 위치를 알려줌
        isGPSEnabled = manager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // NETWORK_PROVIDER: WI-FI 네트워크나 통신사의 기지국 정보를 통해 위치를 알려줌
        isNetworkEnabled = manager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        if (isGPSEnabled) {
            try {
                // GPS를 이용한 위치 요청
                manager.requestLocationUpdates(
                        LocationManager.GPS_PROVIDER,
                        minTime,
                        minDistance,
                        gpsListener);

                // 위치 확인이 안되는 경우에도 최근에 확인된 위치 정보 먼저 확인
                Location lastLocation = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                if (lastLocation != null) {
                    Double latitude = lastLocation.getLatitude();
                    Double longitude = lastLocation.getLongitude();

                    showCurrentLocation(latitude, longitude);
                    //Toast.makeText(getApplicationContext(), "Last Known Location : " + "Latitude : " + latitude + "\nLongitude:" + longitude, Toast.LENGTH_LONG).show();
                }
            } catch (SecurityException ex) {
                ex.printStackTrace();
            }
            //Toast.makeText(getApplicationContext(), "위치 확인이 시작되었습니다. 로그를 확인하세요.", Toast.LENGTH_SHORT).show();
        } else if (!isGPSEnabled && isNetworkEnabled) {
            // 네트워크를 이용한 위치 요청
            try {
                manager.requestLocationUpdates(
                        LocationManager.NETWORK_PROVIDER,
                        minTime,
                        minDistance,
                        gpsListener);

            } catch (SecurityException ex) {
                ex.printStackTrace();
            }

        } else if (!isGPSEnabled) {
            // gps 설정 확인후 꺼져있을 경우 팝업창 생
            alertbox("gps 상태!", "당신의 gps 상태 : off");
        }
    }

    // 리스너 클래스 정의
    private class GPSListener implements LocationListener {

        // 위치 정보가 확인될 때 자동 호출되는 메소드
        public void onLocationChanged(Location location) {
            Double latitude = location.getLatitude();
            Double longitude = location.getLongitude();

            String msg = "Latitude : " + latitude + "\nLongitude:" + longitude;
            Log.i("GPSListener", msg);

            // 현재 위치의 지도를 보여주기 위해 정의한 메소드 호출
            showCurrentLocation(latitude, longitude);

            //Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();

            /*// 도시명 구하기
            String cityName = null;
            Geocoder gcd = new Geocoder(getBaseContext(), Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
                if (addresses.size() > 0)
                    System.out.println(addresses.get(0).getLocality());
                cityName = addresses.get(0).getLocality();
            } catch (IOException e) {
                e.printStackTrace();
            }
            String s = longitude + " " + latitude + " 당신의 현재 도시명 : " + cityName;
            Toast.makeText(getApplicationContext(), "현재 당신의 위치는 " + cityName + " 입니다.", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "gps " + s);*/
        }

        public void onProviderDisabled(String provider) {
        }

        public void onProviderEnabled(String provider) {
        }

        public void onStatusChanged(String provider, int status, Bundle extras) {
        }
    }

    /**
     * 현재 위치의 지도를 보여주기 위해 정의한 메소드
     *
     * @param latitude
     * @param longitude
     */
    private void showCurrentLocation(Double latitude, Double longitude) {
        // 현재 위치를 이용해 LatLon 객체 생성
        LatLng curPoint = new LatLng(latitude, longitude);

        map.animateCamera(CameraUpdateFactory.newLatLngZoom(curPoint, 15));

        // 지도 유형 설정. 지형도인 경우에는 GoogleMap.MAP_TYPE_TERRAIN,
        // 위성 지도인 경우에는 GoogleMap.MAP_TYPE_SATELLITE
        map.setMapType(GoogleMap.MAP_TYPE_NORMAL);

        // 현재 위치 주위에 아이콘을 표시하기 위해 정의한 메소드
        showDolbomi(latitude, longitude);
    }

    protected void alertbox(String title, String mymessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("GPS 를 켜시겠습니까?")
                .setCancelable(false)
                .setTitle("GPS 설정")
                .setPositiveButton("위치정보 켜기", new DialogInterface.OnClickListener() {

                    // 폰 위치 설정 페이지로 넘어감
                    public void onClick(DialogInterface dialog, int id) {
                        Intent myIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                        startActivity(myIntent);
                        dialog.cancel();
                    }
                })
                .setNegativeButton("나중에..", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                        finish();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /**
     * 아이콘을 표시하기 위해 정의한 메소드
     */
    private void showDolbomi(Double latitude, Double longitude) {
        MarkerOptions marker = new MarkerOptions();
        MarkerOptions marker1 = new MarkerOptions();
        MarkerOptions marker2 = new MarkerOptions();
        MarkerOptions marker3 = new MarkerOptions();
        MarkerOptions marker4 = new MarkerOptions();
        MarkerOptions marker5 = new MarkerOptions();

        marker.position(new LatLng(latitude + 0.001, longitude + 0.001));
        marker1.position(new LatLng(latitude + 0.002, longitude - 0.002));
        marker2.position(new LatLng(latitude - 0.003, longitude + 0.003));
        marker3.position(new LatLng(latitude - 0.003, longitude - 0.003));
        marker4.position(new LatLng(latitude - 0.002, longitude + 0.002));
        marker5.position(new LatLng(latitude - 0.001, longitude - 0.001));

        /*marker.title("성웅자");
        marker.snippet("역삼동");
        marker.draggable(true);*/

        marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker));
        marker1.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker));
        marker2.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker));
        marker3.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker));
        marker3.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker));
        marker4.icon(BitmapDescriptorFactory.fromResource(R.drawable.map_marker));

        map.addMarker(marker);
        map.addMarker(marker1);
        map.addMarker(marker2);
        map.addMarker(marker3);
        map.addMarker(marker4);
        map.addMarker(marker5);

        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                Toast.makeText(getApplicationContext(), "마커 클릭됨", Toast.LENGTH_SHORT).show();
                infoPopup();
                return false;
            }
        });
    }

    public void infoPopup() {
        android.app.AlertDialog.Builder alert = new android.app.AlertDialog.Builder(this);

        alert.setTitle("성웅자 님");
        alert.setMessage("지역 : 역삼동 \n");

        final AppCompatTextView tv = new AppCompatTextView(this);

        tv.setText("  안녕하세요 웅자에요^^ \n   연락주세염~");

        alert.setView(tv);

        alert.show();
    }

    /**
     * 센서의 정보를 받기 위한 리스너 객체 생성
     */
    private final SensorEventListener seListener = new SensorEventListener() {
        private int iOrientation = -1;

        public void onAccuracyChanged(Sensor sensor, int accuracy) {

        }

        // 센서의 값을 받을 수 있도록 호출되는 메소드
        public void onSensorChanged(SensorEvent event) {
            if (iOrientation < 0) {
                iOrientation = ((WindowManager) getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getRotation();
            }

            compassView.setAzimuth(event.values[0] + 90 * iOrientation);
            compassView.invalidate();
        }

    };
}