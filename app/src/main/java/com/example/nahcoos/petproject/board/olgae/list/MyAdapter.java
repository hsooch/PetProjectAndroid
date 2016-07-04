package com.example.nahcoos.petproject.board.olgae.list;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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
    MyAdapter adapter;
    MyListView myListView;
    ArrayList<Bitmap> bitmapArrayList;

    String path = null;

    public MyAdapter(Context context, MyListView myListView, ArrayList<Bitmap> bitmapArrayList) {

        this.context = context;
        Log.d(TAG, "MyAdapter()실행중!!");
        this.myListView = myListView;
        this.bitmapArrayList = bitmapArrayList;

        layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

/*     posting[0]=R.drawable.animal1;
        posting[1]=R.drawable.animal2;
        posting[2]=R.drawable.animal3;
        posting[3]=R.drawable.animal4;
        posting[4]=R.drawable.animal5;*/

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

        ImageView imageView = (ImageView) view.findViewById(R.id.photo);
        TextView name = (TextView) view.findViewById(R.id.name);
        TextView whatKind = (TextView) view.findViewById(R.id.whatKind);
        TextView registNumber = (TextView) view.findViewById(R.id.registNumber);
        TextView address = (TextView) view.findViewById(R.id.address);
        TextView contactPoint = (TextView) view.findViewById(R.id.contactPoint);
        // CheckBox isOperation = (CheckBox) view.findViewById(R.id.isOperation);
        //CheckBox isRegularCheck = (CheckBox) view.findViewById(R.id.isRegularCheck);
        //RadioButton boy = (RadioButton) view.findViewById(R.id.boy);
        // RadioButton girl = (RadioButton) view.findViewById(R.id.girl);

        PetOwner petOwner = list.get(position);
        Log.d(TAG, "list.get(position) 호출: " + list.get(position));

        int seq = petOwner.getPetOwner_id();
        String filename = petOwner.getPhoto(); // getPhoto로 dto의 파일 이름을 호출
        String ext = filename.substring(filename.lastIndexOf(".") + 1, filename.length());

        //참고] Bitmap은 이미지가 아니라 이미지가 될 수 있는 최상위 객체다
        //이미지 로더를 호출하자!! (= ImageAsync)
        ImageAsync imageAsync = new ImageAsync(imageView, adapter, myListView, bitmapArrayList);
        imageAsync.execute("http://192.168.43.216:9090/photo/" + seq + "." + ext);

        // *중요* ViewBounds를 true로 설정한 후, view들이 (자신의 높이에 맞게) 자신의 밑에 있는 view들과 빈 여백없이 나열되었다.
        imageView.setAdjustViewBounds(true);

        name.setText(petOwner.getName());
        whatKind.setText(petOwner.getWhatKind());
        registNumber.setText(petOwner.getRegistNumber());
        address.setText(petOwner.getAddress());
        contactPoint.setText(petOwner.getContactPoint());

        return view;
    }
}