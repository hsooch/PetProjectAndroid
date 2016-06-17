package com.example.nahcoos.petproject.board.ogae.list;

import android.content.Context;
import android.support.v7.widget.AppCompatImageView;
import android.support.v7.widget.AppCompatTextView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.example.nahcoos.petproject.R;

import java.util.ArrayList;

/**
 * Created by KwonHyungsub on 2016-06-03.
 */
public class MyAdapter extends BaseAdapter {
    Context context;
    LayoutInflater layoutInflater;
    String TAG = getClass().getName();
    int posting[] = new int[6];
    MyAsync myAsync;
    public ArrayList<PetOwner> list = new ArrayList<PetOwner>();

    public MyAdapter(Context context) {

        this.context = context;
        Log.d(TAG, "MyAdapter()실행중!!");

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        posting[0] = R.drawable.animal1;
        posting[1] = R.drawable.animal2;
        posting[2] = R.drawable.animal3;
        posting[3] = R.drawable.animal4;
        posting[4] = R.drawable.animal5;
        posting[5] = R.drawable.animal6;

        loadData();

    }

    public void loadData() {
        myAsync = new MyAsync(this);
        myAsync.execute();
        Log.d(TAG, "myAsync.execute();실행중?");
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return list.get(position).getPetOwner_id();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = null;
        Log.d(TAG, "RadioButton까지 메모리에 등록");
        if (convertView == null) {
            view = layoutInflater.inflate(R.layout.posting, parent, false);
        } else {
            view = convertView;
        }

        AppCompatImageView imageView = (AppCompatImageView) view.findViewById(R.id.photo);
        AppCompatTextView name = (AppCompatTextView) view.findViewById(R.id.name);
        AppCompatTextView whatKind = (AppCompatTextView) view.findViewById(R.id.whatKind);
        AppCompatTextView registNumber = (AppCompatTextView) view.findViewById(R.id.registNumber);
        AppCompatTextView address = (AppCompatTextView) view.findViewById(R.id.address);
        AppCompatTextView contactPoint = (AppCompatTextView) view.findViewById(R.id.contactPoint);
        // CheckBox isOperation = (CheckBox) view.findViewById(R.id.isOperation);
        //CheckBox isRegularCheck = (CheckBox) view.findViewById(R.id.isRegularCheck);
        //RadioButton boy = (RadioButton) view.findViewById(R.id.boy);
        // RadioButton girl = (RadioButton) view.findViewById(R.id.girl);

        int index = position % 6;
        imageView.setImageResource(posting[index]);
        // *중요* ViewBounds를 true로 설정한 후, view들이 (자신의 높이에 맞게) 자신의 밑에 있는 view들과 빈 여백없이 나열되었다.
        imageView.setAdjustViewBounds(true);

        name.setText(list.get(position).getName());
        whatKind.setText(list.get(position).getWhatKind());
        registNumber.setText(list.get(position).getRegistNumber());
        address.setText(list.get(position).getAddress());
        contactPoint.setText(list.get(position).getContactPoint());

      /*  if(list.get(position).isOperation!=null) {
            isOperation.setChecked(true);
            isOperation.setActivated(false);
        }
        if(list.get(position).isRegularCheck!=null){
            isRegularCheck.setChecked(true);
            isRegularCheck.setActivated(false);
        }
        if (list.get(position).getSex().equals("암컷")) {
            girl.setChecked(true);
            girl.setActivated(false);
        } else if (list.get(position).getSex().equals("수컷")) {
            boy.setChecked(true);
            boy.setActivated(false);
        }*/

        return view;
    }
}
