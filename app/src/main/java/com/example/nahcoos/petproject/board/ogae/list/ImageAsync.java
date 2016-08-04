package com.example.nahcoos.petproject.board.ogae.list;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class ImageAsync extends AsyncTask<String, Void, Bitmap> {

    URL url;
    ImageView imageView;
    MyAdapter myPetSitterAdapter;
    Bitmap bitmap;
    MyListView myPetSitterView;
    ArrayList<Bitmap> bitmapArrayList;

    public ImageAsync(ImageView imageView, MyAdapter myPetSitterAdapter, MyListView myPetSitterView, ArrayList<Bitmap> bitmapArrayList) {
        this.imageView = imageView;
        this.myPetSitterAdapter = myPetSitterAdapter;
        this.myPetSitterView = myPetSitterView;
        this.bitmapArrayList = bitmapArrayList;
    }

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
        myPetSitterView.invalidateViews();

        bitmapArrayList.add(bitmap);
        super.onPostExecute(bitmap);
    }
}


