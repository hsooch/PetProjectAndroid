package com.example.nahcoos.petproject;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;

import com.kakao.auth.KakaoSDK;

import java.lang.reflect.Field;

public class GlobalApplication extends Application {
    private static GlobalApplication mInstance;
    private static volatile Activity currentActivity = null;

    public static Activity getCurrentActivity() {
        Log.d("TAG", "++ currentActivity : " + (currentActivity != null ? currentActivity.getClass().getSimpleName() : ""));
        return currentActivity;
    }

    public static void setCurrentActivity(Activity currentActivity) {
        GlobalApplication.currentActivity = currentActivity;
    }

    /**
     * singleton
     *
     * @return singleton
     */
    public static GlobalApplication getGlobalApplicationContext() {
        if (mInstance == null)
            throw new IllegalStateException("this application does not inherit GlobalApplication");
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        KakaoSDK.init(new KakaoSDKAdapter());

        // 앱 디폴트 폰트 설정
        setDefaultFont(this, "DEFAULT", "BMJUA_ttf.ttf");
        setDefaultFont(this, "SANS_SERIF", "BMJUA_ttf.ttf");
        setDefaultFont(this, "SERIF", "BMJUA_ttf.ttf");
    }

    public static void setDefaultFont(Context ctx,
                                      String staticTypefaceFieldName, String fontAssetName) {
        final Typeface regular = Typeface.createFromAsset(ctx.getAssets(),
                fontAssetName);
        replaceFont(staticTypefaceFieldName, regular);
    }

    protected static void replaceFont(String staticTypefaceFieldName,
                                      final Typeface newTypeface) {
        try {
            final Field StaticField = Typeface.class
                    .getDeclaredField(staticTypefaceFieldName);
            StaticField.setAccessible(true);
            StaticField.set(null, newTypeface);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
