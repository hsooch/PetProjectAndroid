package com.example.nahcoos.petproject.board.ogae.write;

import android.os.AsyncTask;
import android.util.Log;

import com.example.nahcoos.petproject.fragments.OgaeFragment;

import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class WriteAsync extends AsyncTask<String, Void, String> {
    String TAG = getClass().getName();
    URL url;
    HttpURLConnection con;
    BufferedWriter buffw;
    String charset = "utf-8";
    String boundary = Long.toHexString(System.currentTimeMillis());
    String CRLF = "\r\n"; // Line separator required by multipart/form-data.
    InputStream is;  //유저가 선택한 갤러리의 이미지에 대한 스트림
    String para[] = {
            "name", "whatKind", "registNumber", "address",
            "contactPoint", "isRegularCheck", "isOperation", "sex"};

    //String param = "value";
    //File binaryFile = new File("D:/jsp_workspace/PhotoServer/WebContent/data/Jellyfish.jpg");

    public WriteAsync(InputStream is) {
        this.is = is;
    }

    protected String doInBackground(String... params) {
        try {
            Log.d(TAG, "doInBackground실행되엇습니다.");
            url = new URL(params[0]);
            con = (HttpURLConnection) url.openConnection();
            con.setDoOutput(true);
            con.setDoInput(true);
            con.setUseCaches(false);
            con.setRequestMethod("POST");
            con.setRequestProperty("Connection", "keep-alive");
            con.setRequestProperty("Cache-Control", "max-age=0");
            con.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            con.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + boundary);


            OutputStream output = con.getOutputStream();
            Log.d(TAG, "output = con.getOutputStream(); 실행되었습니다.." + output);

            con.connect();
            Log.d(TAG, "con.connect();" + con);

            PrintWriter writer = new PrintWriter(new OutputStreamWriter(output, charset), true);
            Log.d(TAG, "writer는" + writer);

          /*  for (int i = 0; i < para.length; i++) {*/
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"" + para[0] + "\"").append(CRLF);
            writer.append("Content-Type: text/plain; charset=" + charset).append(CRLF);
            writer.append(CRLF).append(params[1]).append(CRLF).flush();
            /*}*/
            Log.d(TAG, "writer.append끝입니다.");


// Send binary file.
            writer.append("--" + boundary).append(CRLF);
            writer.append("Content-Disposition: form-data; name=\"filename\"; filename=\"" + params[9] + "\"").append(CRLF); //myFile로 수정됨
            writer.append("Content-Type: " + HttpURLConnection.guessContentTypeFromName(params[9])).append(CRLF);
            writer.append("Content-Transfer-Encoding: binary").append(CRLF);
            writer.append(CRLF).flush();
            Log.d(TAG, "Send binary file끝입니다.");

            // 얻어온 입력스트림의 데이터를 output 스트림에 편승시키자!
            byte[] buff = new byte[1024 * 4];
            int read = 1;
            while (true) {
                read = is.read(buff);  //배열을 사용하여 한꺼번에 읽어들이는 경우 반환되는 데이터는 읽어들인 데이터가 아니다

                if (read == -1) break;
                output.write(buff, 0, read);
            }
            is.close();


            output.flush(); // Important before continuing with writer!
            writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.


            // End of multipart/form-data.
            writer.append("--" + boundary + "--").append(CRLF).flush();
            writer.close();

            int code = 0;
            code = con.getResponseCode();
            Log.d(TAG, "con.getResponseCode()의 값은 " + code);

            //System.out.println(code);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    protected void onPostExecute(String s) {
        super.onPostExecute(s);
        OgaeFragment.myAdapter.loadData();
        OgaeFragment.myAdapter.notifyDataSetChanged();
    }
}
/*




            //얻어온 입력스트림의 데이터를 output 스트림에 편승시키자!!
            byte[] buff = new byte[1024 * 4];

            int read = -1;
            while (true) {
                read = is.read(buff); //배열을 사용하여 한꺼번에 읽어들이는 경우 반환되는
                //데이터는 읽어들인 데이터가 아니다!!
                if (read == -1) break;
                output.write(buff, 0, read);
            }
            is.close();
            output.flush(); // Important before continuing with writer!
            writer.append(CRLF).flush(); // CRLF is important! It indicates end of boundary.


            // End of multipart/form-data.
            writer.append("--" + boundary + "--").append(CRLF).flush();
            writer.close();

            int code = 0;
            code = con.getResponseCode();
            //System.out.println(code);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    protected void onPostExecute(String s) {
        super.onPostExecute(s);
    }
}*/
