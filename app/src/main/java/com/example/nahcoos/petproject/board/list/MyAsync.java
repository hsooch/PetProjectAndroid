package com.example.nahcoos.petproject.board.list;

import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by KwonHyungsub on 2016-06-09.
 */
public class MyAsync extends AsyncTask<String, Void, String> {
    MyAdapter myAdapter;
    URL url;
    HttpURLConnection con;
    BufferedReader buffr;
    StringBuffer sb;
    String TAG= getClass().getName();



    public MyAsync(MyAdapter myAdapter) {
        this.myAdapter= myAdapter;

    }

    protected void onPreExecute() {
        try {
            url= new URL("http://192.168.0.13:9090/device/pet/board");
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }
    @Override
    protected String doInBackground(String... params) {
        try {
            con=(HttpURLConnection) url.openConnection();
            con.setRequestMethod("GET");
            con.setRequestProperty("accept","application/json"); //output있을떄는 Contet-Type , input있을때는 accept를 적어라!
            con.setDoInput(true);
            int code=con.getResponseCode();

            Log.d(TAG, "con는는 "+con);
            Log.d(TAG, "톰캣의 응답코드는 "+code);

            buffr= new BufferedReader(new InputStreamReader(con.getInputStream(),"utf-8"));
            String data= null;

            sb= new StringBuffer();

            while(true){
                data=buffr.readLine();
                if(data==null){
                    break;
                }
                Log.d(TAG,"data는는"+data);
                sb.append(data);
            }

            return sb.toString();

        } catch (IOException e) {
            e.printStackTrace();
        }finally{
            if(con!=null){
                try {
                    con.disconnect();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if(buffr!=null){
                try {
                    buffr.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    @Override
    protected void onProgressUpdate(Void... values) {
        super.onProgressUpdate(values);
    }

    @Override
    protected void onPostExecute(String s) {
        try {
            // list 로 반환됨. JSONObject가 아니라. 중가로로 시작하면(object) 대가로로 시작하면 JSONArray
          JSONArray jsonArray= new JSONArray(s);
            Log.d(TAG,"jsonArray실행");
            ArrayList<PetOwner> list= new ArrayList<PetOwner>();

            Log.d(TAG,"jsonArray.length()전?"+jsonArray.length());

            for(int i=0;i<jsonArray.length();i++){
                JSONObject jsonObject= (JSONObject)jsonArray.get(i);
                Log.d(TAG,"jsonObject실행");
                Log.d(TAG,"jsonObject는?"+jsonObject);

                PetOwner petOwner= new PetOwner();
                Log.d(TAG,"1petOwnert실행");
                petOwner.setPetOwner_id(jsonObject.getInt("petOwner_id"));
                petOwner.setPhoto(jsonObject.getString("photo"));
                petOwner.setName(jsonObject.getString("name"));
                petOwner.setWhatKind(jsonObject.getString("whatKind"));
                petOwner.setRegistNumber(jsonObject.getString("registNumber"));
                petOwner.setAddress(jsonObject.getString("address"));
                petOwner.setContactPoint(jsonObject.getString("contactPoint"));
                petOwner.setBoy(jsonObject.getString("boy"));
                petOwner.setGirl(jsonObject.getString("girl"));
                petOwner.setSex(jsonObject.getString("sex"));
                petOwner.setIsOperation(jsonObject.getString("isOperation"));
                petOwner.setIsRegularCheck(jsonObject.getString("isRegularCheck"));

                list.add(petOwner);
                Log.d(TAG,"myAdapter.list=list;싸이즈는");
            }

            myAdapter.list=list;
            //Log.d(TAG,"myAdapter.list=list;싸이즈는"+myAdapter.list.size());

        } catch (JSONException e) {
            e.printStackTrace();
            Log.d(TAG,"catch실행");
        }

        myAdapter.notifyDataSetChanged();
        Log.d(TAG,"myAdapter.notifyDataSetChanged()실행중!");

    }
}
