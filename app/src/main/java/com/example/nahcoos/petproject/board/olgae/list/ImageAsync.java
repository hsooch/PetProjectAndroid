package com.example.nahcoos.petproject.board.olgae.list;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

/**
 * 이미지만을 다운받기 위한 AsyncTask객체
 * 이미지 하나당 doInBackground메서드가 호출된다!
 */
public class ImageAsync extends AsyncTask<String, Void, Bitmap> {
    URL url;
    ImageView imageView;
    MyAdapter myAdapter;
    Bitmap bitmap;
    Bitmap resizedBitmap;
    MyListView myListView;
    ArrayList<Bitmap> bitmapArrayList;

    public ImageAsync(ImageView imageView, MyAdapter myAdapter, MyListView myListView, ArrayList<Bitmap> bitmapArrayList) {
        this.imageView = imageView;
        this.myAdapter = myAdapter;
        this.myListView = myListView;
        this.bitmapArrayList = bitmapArrayList;
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        HttpURLConnection conn = null;
        try {
            URL myFileUrl = new URL(params[0]);
            conn = (HttpURLConnection) myFileUrl.openConnection();
            conn.setDoInput(true);
            conn.connect();

            InputStream is = conn.getInputStream();
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inPreferredConfig = Bitmap.Config.RGB_565;
            options.inSampleSize = 4;
            bitmap = BitmapFactory.decodeStream(is, null, options);
            is.close();

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (conn != null) {
                conn.disconnect();
            }
        }

        return bitmap;
    }

    protected void onPostExecute(Bitmap bitmap) {
        imageView.setImageBitmap(bitmap);
        //MainActivity.myAdapter.notifyDataSetChanged();
        // myAdapter.notifyDataSetInvalidated();
        myListView.invalidateViews();
        bitmapArrayList.add(bitmap);
    }
}