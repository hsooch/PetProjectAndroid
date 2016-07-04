package com.example.nahcoos.petproject.mainview;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nahcoos.petproject.R;

/**
 * Created by nahcoos on 2016. 6. 10..
 */
public class SettingFragment extends Fragment {

    private static final String ARG_POSITION = "position";
    private int position;

    public static SettingFragment newInstance(int position) {
        SettingFragment f = new SettingFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        position = getArguments().getInt(ARG_POSITION);
        View rootView = inflater.inflate(R.layout.setting_page, container, false);

        return rootView;
    }
}
