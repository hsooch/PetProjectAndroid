package com.example.nahcoos.petproject.board.ogae.list;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import com.huewu.pla.lib.MultiColumnListView;

//동적으로 메모리에 올려 UI에 출력되는 PinterestView

public class MyListView extends MultiColumnListView {
    String TAG = getClass().getName();
    int height;

    public MyListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.d(TAG, "multiView페이지입니다.");
        setColumnPaddingLeft(30);
        setColumnPaddingRight(30);

    }

    public void setColumnPaddingLeft(int columnPaddingLeft) {
        super.setColumnPaddingLeft(columnPaddingLeft);
    }

    public void setColumnPaddingRight(int columnPaddingLeft) {
        super.setColumnPaddingRight(columnPaddingLeft);
    }


}
