package com.example.nahcoos.petproject.login;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.AppCompatEditText;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nahcoos.petproject.R;
import com.example.nahcoos.petproject.mainview.MainActivity;
import com.example.nahcoos.petproject.helper.function.BackPressCloseHandler;
import com.example.nahcoos.petproject.helper.img.CircleTransform;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.squareup.picasso.Picasso;

import java.security.MessageDigest;

public class LoginActivity extends AppCompatActivity {
    String TAG = this.getClass().getName();

    Intent intent;

    private SessionCallback mKakaocallback;

    private ImageView user_profile;
    private AppCompatEditText user_id;
    private AppCompatEditText user_pwd;

    private ImageButton bt_kakao;   // 카톡 로그인 버튼

    private Button loginBt;

    private TextView test_id;
    private TextView test_nick;

    private String userId;      // 카톡 아이디
    private String userName;    // 카톡 닉네임
    private String profileUrl;  // 카톡 프사

    private BackPressCloseHandler backPressCloseHandler;    // 앱 종료 핸들러

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_page);

        // 앱 해시키를 가져온다
        getAppKeyHash();

        // 카톡정보 받아오기 테스트용 텍스트뷰
        test_id = (TextView) findViewById(R.id.test_id);
        test_nick = (TextView) findViewById(R.id.test_nick);

        user_profile = (ImageView) findViewById(R.id.user_profile);

        //user_id = (AppCompatEditText) findViewById(R.id.user_id);
        //user_pwd = (AppCompatEditText) findViewById(R.id.user_pwd);
        loginBt = (Button) findViewById(R.id.loginBt);

        bt_kakao = (ImageButton) findViewById(R.id.bt_kakao);
        // 카카오로그인 버튼에 리스너 달기
        bt_kakao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 카카오 로그인 요청
                isKakaoLogin();
            }
        });

        // 종료 도우미 객체 생성
        backPressCloseHandler = new BackPressCloseHandler(this);
    }

    private void isKakaoLogin() {
        // 카카오 세션을 오픈한다
        mKakaocallback = new SessionCallback();
        com.kakao.auth.Session.getCurrentSession().addCallback(mKakaocallback);
        com.kakao.auth.Session.getCurrentSession().checkAndImplicitOpen();
        com.kakao.auth.Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, LoginActivity.this);

        // 키보드 숨기기 (안해주면 뷰에 에딧텍스트가 있을경우 자동으로 키보드 띄워서 개구림)
        /*InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(user_id.getWindowToken(), 0);
        imm.hideSoftInputFromWindow(user_pwd.getWindowToken(), 0);*/
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

    //사용자의 상태를 알아 보기 위해 me API 호출을 한다.
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
            public void onSuccess(UserProfile userProfile) {    // 로그인 성공시
                profileUrl = userProfile.getProfileImagePath(); // 프사 경로
                userId = String.valueOf(userProfile.getId());   // 카톡이 부여해주는 앱용 아이디
                userName = userProfile.getNickname();           // 카톡 닉네임

                Log.d(TAG, "id: " + userId);
                Log.d(TAG, "nickname: " + userName);
                Log.d(TAG, "photo: " + profileUrl);

                setLayout();    // 뷰에 정보 보이기 -테스트
                loginBt();
            }

            @Override
            public void onNotSignedUp() {
                // 자동가입이 아닐경우 동의창
            }
        });
    }

    // 앱 해시키 가져오기 (여기서 가져온 키와 개발자가 설정한 키가 동일해야 함. 개중요함)
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

    // 로그인 성공시 각종 정보 불러오기 -테스트
    public void setLayout() {
        test_id.setText(userId);
        test_nick.setText(userName);

        Picasso.with(this)
                .load(profileUrl)
                .fit()
                .transform(new CircleTransform())
                .into(user_profile);
    }

    // 로그인 버튼- 로그인 성공시 메인 액티비티로~
    public void loginBt() {
        intent = new Intent(this, MainActivity.class);
        intent.putExtra("name", userName);
        intent.putExtra("profile", profileUrl);
        intent.putExtra("userid", userId);
        startActivity(intent);
        finish();
    }

    // 텍스트 얼럿창 띄우기
    public void textPopup(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Title");
        alert.setMessage("Message");

        final AppCompatEditText input = new AppCompatEditText(this);
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

    // 회원가입창 팝업 버튼
    public void registBt(View view) {
        Toast.makeText(this, "회원가입 폼 띄우기", Toast.LENGTH_SHORT).show();
        Intent intent = new Intent(LoginActivity.this, SearchAddr.class);

        startActivity(intent);
    }

    // 뒤로가기 두번 누르면 앱 종료
    public void onBackPressed() {
        backPressCloseHandler.onBackPressed();
    }

}