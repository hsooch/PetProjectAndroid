package com.example.nahcoos.petproject.intro;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Window;

import com.example.nahcoos.petproject.R;
import com.example.nahcoos.petproject.login.LoginActivity;
import com.example.nahcoos.petproject.material.ProgressBarCircular;

/**
 * Intro Page Activity
 * Created by nahcoos on 2016. 6. 17..
 */
public class IntroActivity extends AppCompatActivity {

    private Handler handler;    // 핸들러 선언

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE); // 인트로화면이므로 타이틀바를 없앤다
        setContentView(R.layout.intro_page);

        handler = new Handler(); // 딜레이를 주기 위해 핸들러 생성
        handler.postDelayed(intro, 2400); // 딜레이 (러너블 객체는 mrun, 시간 2초)

        // 원형 프로그레스바
        ProgressBarCircular progressBarCircular = (ProgressBarCircular) findViewById(R.id.progress);
        progressBarCircular.setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
    }

    Runnable intro = new Runnable() {
        @Override
        public void run() {
            Intent intent = new Intent(IntroActivity.this, LoginActivity.class); // 인텐트 생성(현 액티비티, 새로 실행할 액티비티)
            startActivity(intent);
            finish();

            //overridePendingTransition 이란 함수를 이용하여 fade in,out 효과를줌. 순서가 중요
            //overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

            // 좌->우 이동 효과
            overridePendingTransition(R.anim.anim_slide_in_left, R.anim.anim_slide_out_right);
        }
    };

    /*어플리케이션이 종료되어도 실제 onDestory() 메서드가 호출 되어도,
    메모리 상에서 Handler가 PostDelayed 메서드를 실행시켜 어플리케이션이
    다시 실행되는 경우를 막기 위함*/
    @Override
    protected void onDestroy() {
        handler.removeCallbacks(intro);
        super.onDestroy();
    }

    // 인트로 중에 뒤로가기를 누를 경우 핸들러를 끊어버려 아무일 없게 만드는 부분
    // 미 설정시 인트로 중 뒤로가기를 누르면 인트로 후에 홈화면이 나옴.
    @Override
    public void onBackPressed() {
        handler.removeCallbacks(intro);
        super.onBackPressed();
    }

}