package com.example.nahcoos.petproject;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;

import java.security.MessageDigest;


public class LoginActivity extends Activity {
    String TAG = this.getClass().getName();

    private SessionCallback mKakaocallback;

    // view
    private ImageButton login_button;
    private EditText tv_user_id;
    private EditText tv_user_name;
    private ImageView iv_user_profile;

    private String userName;
    private String userId;
    //private String profileUrl;

    Intent intent;

    private BackPressCloseHandler backPressCloseHandler;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        backPressCloseHandler = new BackPressCloseHandler(this);

        // 헤쉬키를 가져온다
        getAppKeyHash();

        tv_user_id = (EditText) findViewById(R.id.tv_user_id);
        tv_user_name = (EditText) findViewById(R.id.tv_user_name);
        //iv_user_profile = (ImageView) findViewById(R.id.iv_user_profile);

        login_button = (ImageButton) findViewById(R.id.login_button);
        login_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 카카오 로그인 요청
                isKakaoLogin();
            }
        });
    }

    private void isKakaoLogin() {
        // 카카오 세션을 오픈한다
        mKakaocallback = new SessionCallback();
        com.kakao.auth.Session.getCurrentSession().addCallback(mKakaocallback);
        com.kakao.auth.Session.getCurrentSession().checkAndImplicitOpen();
        com.kakao.auth.Session.getCurrentSession().open(AuthType.KAKAO_TALK_EXCLUDE_NATIVE_LOGIN, LoginActivity.this);

        //키보드 숨기기
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(tv_user_id.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(tv_user_name.getWindowToken(), 0);
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            Log.d("TAG", "세션 오픈됨");
            // 사용자 정보를 가져옴, 회원가입 미가입시 자동가입 시킴
            KakaorequestMe();
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if (exception != null) {
                Log.d("TAG", exception.getMessage());
            }
        }
    }

    /**
     * 사용자의 상태를 알아 보기 위해 me API 호출을 한다.
     */
    protected void KakaorequestMe() {
        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                int ErrorCode = errorResult.getErrorCode();
                int ClientErrorCode = -777;

                if (ErrorCode == ClientErrorCode) {
                    Toast.makeText(getApplicationContext(), "카카오톡 서버의 네트워크가 불안정합니다. 잠시 후 다시 시도해주세요.", Toast.LENGTH_SHORT).show();
                } else {
                    Log.d("TAG", "오류로 카카오로그인 실패 ");
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d("TAG", "오류로 카카오로그인 실패 ");
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                //profileUrl = userProfile.getProfileImagePath();
                userId = String.valueOf(userProfile.getId());
                userName = userProfile.getNickname();

                Log.d(TAG, "id: " + userId);
                Log.d(TAG, "name: " + userName);

                //setLayoutText();
            }

            @Override
            public void onNotSignedUp() {
                // 자동가입이 아닐경우 동의창
            }
        });
    }

    private void setLayoutText() {
        tv_user_id.setText(userId);
        tv_user_name.setText(userName);

        /*Picasso.with(this)
                .load(profileUrl)
                .fit()
                .into(iv_user_profile);*/
    }

    private void getAppKeyHash() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(getPackageName(), PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                Log.d("Hash key", something);
            }
        } catch (Exception e) {
            Log.e("name not found", e.toString());
        }
    }

    // 로그인 버튼- 로그인 성공시 다음 액티비티로 넘어감
    public void loginBt(View view) {
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // 회원가입창 팝업 버튼
    public void registBt(View view) {
        Toast.makeText(this, "회원가입 폼 띄우기", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, RegistPopup.class);

        startActivity(intent);
    }

    // 텍스트 입력 가능한 얼럿창 띄우기
    public void textPopup(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Title");
        alert.setMessage("Message");

        // Set an EditText view to get user input
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

    // 뒤로가기 두번 누르면 앱 종료
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

}