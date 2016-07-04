package com.example.nahcoos.petproject.board.olgae.list;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.example.nahcoos.petproject.R;

public class ReadDetail extends AppCompatActivity {
    String TAG = getClass().getName();

    int petOwner_id;
    String photo_dto;
    String name_dto;
    String whatKind_dto;
    String registNumber_dto;
    String address_dto;
    String contactPoint_dto;
    String isOperation_dto;
    String isRegularCheck_dto;
    String sex_dto;
    Bitmap bitmap;

    ImageView photo;
    TextView name;
    TextView whatKind;
    TextView registNumber;
    TextView address;
    TextView contactPoint;
    CheckBox isOperation;
    CheckBox isRegularCheck;
    RadioButton boy;
    RadioButton girl;
    RadioGroup sex;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.readdetail);

        //xml에 위젯 id
        photo = (ImageView) findViewById(R.id.photo);
        name = (TextView) findViewById(R.id.name);
        whatKind = (TextView) findViewById(R.id.whatKind);
        registNumber = (TextView) findViewById(R.id.registNumber);
        address = (TextView) findViewById(R.id.address);
        contactPoint = (TextView) findViewById(R.id.contactPoint);
        isOperation = (CheckBox) findViewById(R.id.isOperation);
        isRegularCheck = (CheckBox) findViewById(R.id.isRegularCheck);
        boy = (RadioButton) findViewById(R.id.boy);
        girl = (RadioButton) findViewById(R.id.girl);
        sex = (RadioGroup) findViewById(R.id.sex);

        Intent intent = getIntent();
        int petOwner_id = intent.getIntExtra("PetOwner_id", 0);
        Log.d(TAG, "name.setText(number);가동중");

        // 서버에서 불러온 값들이 저장된 dto의 멤머필드들
        photo_dto = intent.getStringExtra("photo");
        name_dto = intent.getStringExtra("name");
        whatKind_dto = intent.getStringExtra("whatKind");
        registNumber_dto = intent.getStringExtra("registNumber");
        address_dto = intent.getStringExtra("address");
        contactPoint_dto = intent.getStringExtra("contactPoint");
        isOperation_dto = intent.getStringExtra("isOperation");
        isRegularCheck_dto = intent.getStringExtra("isRegularCheck");
        sex_dto = intent.getStringExtra("sex");
        bitmap = intent.getParcelableExtra("bitmap");

        // 서버의 값을 xml의 위젯에 담자!
        photo.setImageBitmap(bitmap);
        name.setText(name_dto);
        whatKind.setText(whatKind_dto);
        registNumber.setText(registNumber_dto);
        address.setText(address_dto);
        contactPoint.setText(contactPoint_dto);

        if (isOperation_dto.equals("true")) {
            isOperation.setChecked(true);
            isOperation.setClickable(false);
        } else {
            isOperation.setChecked(false);
            isOperation.setClickable(false);
        }
        if (isRegularCheck_dto.equals("true")) {
            isRegularCheck.setChecked(true);
            isRegularCheck.setClickable(false);
        } else {
            isRegularCheck.setChecked(false);
            isRegularCheck.setClickable(false);
        }

        if (sex_dto.equals("암컷")) {
            girl.setChecked(true);
            girl.setClickable(false);
            boy.setClickable(false);
        } else if (sex_dto.equals("수컷")) {
            boy.setChecked(true);
            boy.setClickable(false);
            girl.setClickable(false);
        }
        Log.d(TAG, "여기까지 나옴");

    }
}