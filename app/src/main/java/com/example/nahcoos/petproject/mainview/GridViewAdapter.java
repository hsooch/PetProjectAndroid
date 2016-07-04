package com.example.nahcoos.petproject.mainview;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;

import com.example.nahcoos.petproject.R;
import com.squareup.picasso.Picasso;

/**
 * Created by nahcoos on 2016. 6. 17..
 */
public class GridViewAdapter extends BaseAdapter {
    private Context mContext;

    public GridViewAdapter(Context c) {
        mContext = c;
    }

    public int getCount() {
        return mThumbIds.length;
    }

    public Object getItem(int position) {
        return null;
    }

    public long getItemId(int position) {
        return 0;
    }


    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView imageView;
        if (convertView == null) {
            imageView = new ImageView(mContext);
            imageView.setLayoutParams(new GridView.LayoutParams(300, 300));
            imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
            imageView.setPadding(8, 8, 8, 8);
        } else {
            imageView = (ImageView) convertView;
        }

        // 이미지 뷰에 사진 삽입
        //imageView.setImageResource(mThumbIds[position]);
        Picasso.with(mContext)
                .load(mThumbIds[position])
                .fit()
                .into(imageView);

        return imageView;
    }

    // 삽입할 사진 배열
    private Integer[] mThumbIds = {
            R.drawable.animal1, R.drawable.animal2,
            R.drawable.animal3, R.drawable.animal4,
            R.drawable.animal5, R.drawable.animal6,
            R.drawable.animal4, R.drawable.animal5,
            R.drawable.animal6, R.drawable.animal7
    };
}
