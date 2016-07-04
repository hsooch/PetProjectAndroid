package com.example.nahcoos.petproject.board.olgae;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatDialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.nahcoos.petproject.R;
import com.example.nahcoos.petproject.board.olgae.list.MyAdapter;
import com.example.nahcoos.petproject.board.olgae.list.MyListView;
import com.example.nahcoos.petproject.board.olgae.list.PetOwner;
import com.example.nahcoos.petproject.board.olgae.list.ReadDetail;
import com.example.nahcoos.petproject.board.olgae.write.Write;
import com.example.nahcoos.petproject.material.FloatingActionButton;
import com.huewu.pla.lib.internal.PLA_AdapterView;

import java.util.ArrayList;

/*
* 다녀오개 게시판
* -- 맡아 줄 사람이 글쓰는 공간 (돌보미)
* */
public class OlgaeFragment extends AppCompatDialogFragment implements PLA_AdapterView.OnItemClickListener {
    MyListView myListView;
    public static MyAdapter myAdapter;
    ArrayList<Bitmap> bitmapArrayList = new ArrayList<Bitmap>();
    String TAG = getClass().getName();

    private static final String ARG_POSITION = "position";

    private int position;

    public static OlgaeFragment newInstance(int position) {
        OlgaeFragment f = new OlgaeFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);

        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        position = getArguments().getInt(ARG_POSITION);
        View rootView = inflater.inflate(R.layout.olgae_page, container, false);

        myListView = (MyListView) rootView.findViewById(R.id.parentBoard);

        FloatingActionButton fab = (FloatingActionButton) rootView.findViewById(R.id.fabButton);
        fab.setDrawableIcon(ContextCompat.getDrawable(getContext(), R.drawable.write));
        fab.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                write();
            }
        });

        myAdapter = new MyAdapter(getContext(), myListView, bitmapArrayList);

        myListView.setAdapter(myAdapter);
        myListView.setOnItemClickListener(this);

        return rootView;
    }

    public void write() {
        Intent intent = new Intent(getContext(), Write.class);
        startActivity(intent);
    }

    public void onItemClick(PLA_AdapterView<?> parent, View view, int position, long id) {
        Log.d(TAG, "onItemClick()실행중!!" + view);
        //primarykey받기
        int userPrimaryKey = (int) id;
        Bitmap bitmap = bitmapArrayList.get(position);

        PetOwner petOwner = myAdapter.list.get(position);

        Log.d(TAG, " int userPrimaryKey=(int)id는는" + userPrimaryKey);

        Intent intent = new Intent(getContext(), ReadDetail.class);
        intent.putExtra("PetOwner_id", petOwner.getPetOwner_id());
        intent.putExtra("photo", petOwner.getPhoto());
        intent.putExtra("name", petOwner.getName());
        intent.putExtra("whatKind", petOwner.getWhatKind());
        intent.putExtra("registNumber", petOwner.getRegistNumber());
        intent.putExtra("address", petOwner.getAddress());
        intent.putExtra("contactPoint", petOwner.getContactPoint());
        intent.putExtra("isOperation", petOwner.getIsOperation());
        intent.putExtra("isRegularCheck", petOwner.getIsRegularCheck());
        intent.putExtra("sex", petOwner.getSex());
        intent.putExtra("bitmap", bitmap);

        Log.d(TAG, "intent가동!!" + intent);

        startActivity(intent);
    }
}