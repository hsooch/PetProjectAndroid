package com.example.nahcoos.petproject.board.olgae.write;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import com.example.nahcoos.petproject.R;
import com.example.nahcoos.petproject.fragments.OgaeFragment;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class Write extends AppCompatActivity {

    EditText edit_name, edit_whatKind, edit_registNumber, edit_address, edit_contactPoint;
    CheckBox edit_isOperation, edit_isRegularCheck;
    RadioButton edit_boy, edit_girl;
    RadioGroup edit_sex;

    static final int PICK_REQUEST = 100;
    ImageView edit_photo;
    InputStream is;
    File file;


    String TAG = getClass().getName();

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.write);
        edit_photo = (ImageView) findViewById(R.id.photo);
        edit_name = (EditText) findViewById(R.id.name);
        edit_whatKind = (EditText) findViewById(R.id.whatKind);
        edit_registNumber = (EditText) findViewById(R.id.registNumber);
        edit_address = (EditText) findViewById(R.id.address);
        edit_contactPoint = (EditText) findViewById(R.id.contactPoint);

        //반환형 확인 String화가 가능한지..
        edit_isOperation = (CheckBox) findViewById(R.id.isOperation);
        edit_isRegularCheck = (CheckBox) findViewById(R.id.isRegularCheck);
        edit_sex = (RadioGroup) findViewById(R.id.sex);
        edit_boy = (RadioButton) findViewById(R.id.boy);
        edit_girl = (RadioButton) findViewById(R.id.girl);
    }

    public void regist(View view) {
        WriteAsync writeAsync = new WriteAsync(is);
        Log.d(TAG, "writeAsync는는" + writeAsync);

        String path = "http://192.168.0.13:9090/device/pet/board";
        /*String photo=edit_photo.getText().toString(); */
        String name = edit_name.getText().toString();
        String whatKind = edit_whatKind.getText().toString();
        String registNumber = edit_registNumber.getText().toString();
        String address = edit_address.getText().toString();
        String contactPoint = edit_contactPoint.getText().toString();
        // boolean형으로 checkbox결과가 확인되어 if문으로 체크후 String에 값을 넣자.
        boolean r_isRegularCheck = edit_isRegularCheck.isChecked();
        boolean r_isOperation = edit_isOperation.isChecked();
        String isRegularCheck = null;
        String isOperation = null;
        String fileName = file.getName();

        if (r_isRegularCheck) {
            isRegularCheck = "true";
        } else {
            isRegularCheck = "false";
        }
        if (r_isOperation) {
            isOperation = "true";
        } else {
            isOperation = "false";
        }
        //Radio그룹에서 선택한 값이 암컷인지 수컷이지 확인.
        int result = edit_sex.getCheckedRadioButtonId();
        String sex = null;
        Log.d(TAG, "result는는" + result);
        if (result == 2131493008) {
            sex = "암컷";
        } else if (result == 2131493009) {
            sex = "수컷";
        }

        Log.d(TAG, "sex는" + sex);

        // [문제2] execute의 결과값을 반환하고 싶은데 어떻게?? String ss=writeAsync..로 하면 에러발생.
        writeAsync.execute(path, name, whatKind, registNumber, address, contactPoint, isRegularCheck, isOperation, sex, fileName);
        Log.d(TAG, "writeAsync.execute()실행되었습니다.!");

        OgaeFragment.myAdapter.notifyDataSetChanged();
        finish();
    }

    public void insert(View view) {
        // Intent의 암시적 선언 : 휴대폰의 사진첩에 접근하는 코드
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_REQUEST);  // Requestcode를 사용자가 정의, 보통 상수로 감.
        Log.d(TAG, "사진클릭했습니다!");
    }

    // 사진 선택시, 그 결과를 가져올 메서드
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_REQUEST && resultCode == RESULT_OK) {
            Log.d(TAG, "사진을 선택해서 가져옴");

            // intent는 데이터 저장매체 객체 따라서, 안에 담긴 이미지를 추출해내자.
            Uri uri = data.getData();

            edit_photo.setImageURI(uri);
            Log.d(TAG, "img.setImageURI(uri)호출됨");

            //파일명을 얻어오자.
            Log.d(TAG, "uri.getPath()= " + uri.getPath());
            Log.d(TAG, "uri.toString()= " + uri.getPath());

            //이미지 실제 경로 얻기
            String[] filePathColumn = {MediaStore.Images.Media.DATA};
            Cursor cursor = getContentResolver().query(uri, null, null, null, null);
            cursor.moveToFirst();
            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
            String filepath = cursor.getString(columnIndex);
            cursor.close();

            Log.d(TAG, "filepath는" + filepath);
            //실제 파일이름 추출
            file = new File(filepath);

            try {
                is = this.getContentResolver().openInputStream(uri);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

        }
    }
}





