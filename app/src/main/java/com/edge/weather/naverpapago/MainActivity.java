package com.edge.weather.naverpapago;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.github.kevinsawicki.http.HttpRequest;
import com.google.gson.JsonObject;


import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
public class MainActivity extends AppCompatActivity {
    private TextView papago;
    String clientId = "IEJ7Z2kFZQnsY939cUe1";//애플리케이션 클라이언트 아이디값";
    String clientSecret = "MB0B_KlrKF";//애플리케이션 클라이언트 시크릿값";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        papago = (TextView) findViewById(R.id.papago);
        UpdatePatient patient=new UpdatePatient();
        patient.execute();

    }
    private class UpdatePatient extends AsyncTask<String,String,String> {
        String clientId = "IEJ7Z2kFZQnsY939cUe1";//애플리케이션 클라이언트 아이디값";
        String clientSecret = "MB0B_KlrKF";//애플리케이션 클라이언트 시크릿값";
        String result;
        @Override
        protected String doInBackground(String... params) {
            try {
                String text = URLEncoder.encode("감사합니다.", "UTF-8");
                String apiURL = "https://openapi.naver.com/v1/papago/n2mt";
                URL url = new URL(apiURL);
                HttpURLConnection con = (HttpURLConnection)url.openConnection();
                con.setRequestMethod("POST");
                con.setRequestProperty("X-Naver-Client-Id", clientId);
                con.setRequestProperty("X-Naver-Client-Secret", clientSecret);
                // post request
                String postParams = "source=ko&target=en&text=" + text;
                con.setDoOutput(true);
                DataOutputStream wr = new DataOutputStream(con.getOutputStream());
                wr.writeBytes(postParams);
                wr.flush();
                wr.close();
                int responseCode = con.getResponseCode();
                BufferedReader br;
                if(responseCode==200) { // 정상 호출

                    br = new BufferedReader(new InputStreamReader(con.getInputStream()));
                } else {  // 에러 발생
                    Log.d("에러발생","에러발생");
                    br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
                }
                String inputLine;
                StringBuffer response = new StringBuffer();
                while ((inputLine = br.readLine()) != null) {
                    response.append(inputLine);
                }
                br.close();
                JSONObject json=new JSONObject(response.toString());
                result=json.getJSONObject("message").getJSONObject("result").get("translatedText").toString();
                Log.d("결과",result);

            } catch (Exception e) {
                System.out.println(e);
            }
            return result;

        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            papago.setText(s+"");
        }

    }
}

