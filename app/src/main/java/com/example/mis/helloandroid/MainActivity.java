package com.example.mis.helloandroid;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;


public class MainActivity extends AppCompatActivity {

    TextView txtWindow;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void HTTPConnection(View view) throws IOException {

        String output = "";
        URL url = getURL();

        output = readFromStream(makeHttpRequest(url));
        showTxt(output);
    }

    public URL getURL() {

        URL url = null;

        String url_string = ((EditText)findViewById(R.id.url_entry)).getText().toString();

        try{
            url = new URL(url_string);
        } catch (MalformedURLException e) {
            Toast malUrlErr = Toast.makeText(MainActivity.this, "Malformed URL", Toast.LENGTH_SHORT);
            malUrlErr.setGravity(Gravity.CENTER, 0 , 0);
            malUrlErr.show();
            return null;
        }

        return url;
    }

    public void showTxt(String txt) {
        txtWindow = (TextView) findViewById(R.id.response_view);
        txtWindow.setText(txt);
    }

    public void showMsg(String txt){
        Toast toast = Toast.makeText(MainActivity.this, txt, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    public HttpURLConnection makeHttpRequest(URL url) throws IOException {
        if (url == null)
            return null;
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(15000);
            urlConnection.connect();
            if(urlConnection.getResponseCode() != 200){
                throw IOException;
            }
        } catch(IOException) {
            showMsg("went wrong");
        } finally {
            if(urlConnection != null) {
                urlConnection.disconnect();
            }
        }
        return urlConnection;
    }

    public String readFromStream(HttpURLConnection urlConnection) throws IOException{
        InputStream inputStream = urlConnection.getInputStream();
        StringBuilder output = new StringBuilder();
        if(inputStream != null){
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while(line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}