package com.example.nahcoos.petproject.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.example.nahcoos.petproject.R;
import com.example.nahcoos.petproject.api.ext.BackPressCloseHandler;
import com.example.nahcoos.petproject.api.ext.CircleTransform;
import com.example.nahcoos.petproject.api.ext.SlidingTabLayout;
import com.example.nahcoos.petproject.api.ext.ViewPagerAdapter;
import com.example.nahcoos.petproject.fragments.MapPopup;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.util.helper.log.Logger;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private BackPressCloseHandler backPressCloseHandler;    // 종료 도우미

    private DrawerLayout drawerLayout;              // 메인 페이지 드로어 레이아웃 (포갤수 있는 레이아웃)
    private ActionBarDrawerToggle drawerToggle;     // 드로어 버튼 애니메이션
    private LinearLayout drawerLinear;              // 슬라이드 메뉴 레이아웃
    private ListView drawerList;                    // 좌측 슬라이드메뉴 리스트
    private Toolbar toolbar;                        // 상단 툴바
    String title;                                   // 사용자 이름
    String profileUrl;                              // 사용자 사진
    AppCompatTextView profileNick;
    AppCompatImageView profileImg;
    SlidingTabLayout slidingTabLayout;              // 슬라이딩 탭
    private String[] titles = new String[]{"다녀오개", "다녀올개"};
    ViewPager pager;                                // 프래그먼트를 보여줄 페이저
    Intent intent;
    Intent map;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_page);

        intent = getIntent();
        title = intent.getExtras().getString("name");
        profileUrl = intent.getExtras().getString("profile");

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerLinear = (LinearLayout) findViewById(R.id.drawer_linear);
        drawerList = (ListView) findViewById(R.id.drawer_list);
        toolbar = (Toolbar) findViewById(R.id.toolbar);

        profileImg = (AppCompatImageView) findViewById(R.id.main_profile);
        profileNick = (AppCompatTextView) findViewById(R.id.main_nick);

        if (toolbar != null) {
            toolbar.setNavigationIcon(R.drawable.ic_ab_drawer);
            setSupportActionBar(toolbar);
            getSupportActionBar().setTitle("마음놓고 다녀오게");
        }

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.sliding_tabs);

        pager = (ViewPager) findViewById(R.id.viewpager);
        pager.setAdapter(new ViewPagerAdapter(getSupportFragmentManager(), titles));

        slidingTabLayout.setViewPager(pager);
        slidingTabLayout.setCustomTabColorizer(new SlidingTabLayout.TabColorizer() {
            @Override
            public int getIndicatorColor(int position) {
                return Color.WHITE;
            }
        });

        Picasso.with(this)
                .load(profileUrl)
                .fit()
                .transform(new CircleTransform())
                .into(profileImg);

        profileNick.setText(title);

        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.app_name);
        drawerLayout.addDrawerListener(drawerToggle);
        String[] values = new String[]{
                "개인정보", "이용약관", "환경설정", "회원탈퇴"
        };

        // Header, Footer Event 설정
        drawerLayout.findViewById(R.id.bt_setup).setOnClickListener(mClickListener);
        drawerLayout.findViewById(R.id.btn_footer).setOnClickListener(mClickListener);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this,
                android.R.layout.simple_list_item_1, android.R.id.text1, values);
        drawerList.setAdapter(adapter);
        drawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                switch (position) {
                    case 0:
                        drawerList.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.material_deep_teal_500));
                        toolbar.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.material_deep_teal_500));
                        slidingTabLayout.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.material_deep_teal_500));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 1:
                        drawerList.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.red));
                        toolbar.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.red));
                        slidingTabLayout.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.red));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 2:
                        drawerList.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.blue));
                        toolbar.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.blue));
                        slidingTabLayout.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.blue));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        break;
                    case 3:
                        drawerList.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.ripple_red));
                        toolbar.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.ripple_red));
                        slidingTabLayout.setBackgroundColor(ContextCompat.getColor(getBaseContext(), R.color.ripple_red));
                        drawerLayout.closeDrawer(GravityCompat.START);
                        onClickUnlink();
                        break;
                }

            }
        });

        // 뒤로가기 두번 눌러 종료하기
        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    // 카톡 로그아웃
    private void onClickLogout() {
        UserManagement.requestLogout(new LogoutResponseCallback() {
            @Override
            public void onCompleteLogout() {
                finish();
                Intent homepage = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(homepage);
            }
        });
    }

    // 카톡 회원탈퇴
    private void onClickUnlink() {
        final String appendMessage = getString(R.string.com_kakao_confirm_unlink);
        new AlertDialog.Builder(this)
                .setMessage(appendMessage)
                .setPositiveButton(getString(R.string.com_kakao_ok_button),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                UserManagement.requestUnlink(new UnLinkResponseCallback() {
                                    @Override
                                    public void onFailure(ErrorResult errorResult) {
                                        Logger.e(errorResult.toString());
                                    }

                                    @Override
                                    public void onSessionClosed(ErrorResult errorResult) {
                                        finish();
                                        Intent homepage = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(homepage);
                                    }

                                    @Override
                                    public void onNotSignedUp() {
                                        //redirectSignupActivity();
                                    }

                                    @Override
                                    public void onSuccess(Long userId) {
                                        finish();
                                        Intent homepage = new Intent(MainActivity.this, LoginActivity.class);
                                        startActivity(homepage);
                                    }
                                });
                                dialog.dismiss();
                            }
                        })
                .setNegativeButton(getString(R.string.com_kakao_cancel_button),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        }).show();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                break;
            case R.id.open_map:
                openMap();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.top_menu, menu);
        return true;
    }

    // 지도 열기
    public void openMap() {
        //Toast.makeText(this, "지도 열기", Toast.LENGTH_SHORT).show();
        map = new Intent(this, MapPopup.class);
        startActivity(map);
    }

    private View.OnClickListener mClickListener = new View.OnClickListener() {

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btn_footer) {
                onClickLogout();
            } else if (v.getId() == R.id.bt_setup) {
                Toast.makeText(getBaseContext(), "환경설정 열기", Toast.LENGTH_SHORT).show();
            }
        }
    };

    // 뒤로가기 두번 눌러 종료하는 메서드
    public void onBackPressed() {
        if (drawerLayout.isDrawerOpen(drawerLinear)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        backPressCloseHandler.onBackPressed();
    }

    // 텍스트 입력 가능한 얼럿창 띄우기
    public void textPopup(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Title");
        alert.setMessage("Message");

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String value = input.getText().toString();
                value.toString();
                // Do something with value!
            }
        });

        alert.setNegativeButton("Cancel",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        // Canceled.
                    }
                });

        alert.show();
    }
}
