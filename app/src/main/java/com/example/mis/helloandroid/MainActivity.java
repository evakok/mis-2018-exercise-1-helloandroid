package com.example.mis.helloandroid;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Patterns;
import android.view.Gravity;
import android.view.View;
import android.webkit.URLUtil;
import android.widget.Button;
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


public class MainActivity extends AppCompatActivity {

    TextView txtWindow;
    Button button;
    int errorcode = 0;
    int respo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        button = (Button) findViewById(R.id.button);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new HTTPConnection().execute();
            }
        });
    }

    // reference1: http://www.codexpedia.com/android/asynctask-and-httpurlconnection-sample-in-android/
    // reference2: http://androidpala.com/android-httpurlconnection-post-get/
    private class HTTPConnection extends AsyncTask <Void, Void, String> {

        @Override
        protected String doInBackground(Void... params) {
            URL url = null;
            InputStream stream = null;
            String result = "";

            url = getURL();

            if(url != null) {
                stream = makeConnection(url);
                if (stream != null)
                    result = convertStreamToString(stream);
                else
                    errorcode = 2;
            }

            return result;
        }

        // reference: https://stackoverflow.com/questions/2837676/how-to-raise-a-toast-in-asynctask-i-am-prompted-to-used-the-looper?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            showResult(result);

            if(errorcode == 1)
                showMsg("Wrong URL");
            else if(errorcode == 2)
                showMsg("Connection Error");
            else
                showMsg("Response: " + String.valueOf(respo));
        }

    }

    // reference1: https://developer.android.com/reference/java/net/HttpURLConnection.html
    // reference2: https://stackoverflow.com/questions/4905075/how-to-check-if-url-is-valid-in-android?utm_medium=organic&utm_source=google_rich_qa&utm_campaign=google_rich_qa
    public URL getURL() {

        URL url = null;

        String urlInput = ((EditText) findViewById(R.id.url_entry)).getText().toString();
        String prefix = "http://";

        if(urlInput.contains(prefix) == false)
            urlInput = prefix + urlInput;


        if(URLUtil.isValidUrl(urlInput) == false || Patterns.WEB_URL.matcher(urlInput).matches() == false){
            errorcode = 1;
            return null;
        }

        try {
            url = new URL(urlInput);
        } catch (MalformedURLException e) {
            errorcode = 1;
            return null;
        }

        return url;
    }

    public void showResult(String txt) {
        txtWindow = (TextView) findViewById(R.id.response_view);
        txtWindow.setText(txt);
    }

    public void showMsg(String txt) {
        Toast toast = Toast.makeText(MainActivity.this, txt, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER, 0, 0);
        toast.show();
    }

    // reference1: http://www.codexpedia.com/android/asynctask-and-httpurlconnection-sample-in-android/
    // reference2: http://androidpala.com/android-httpurlconnection-post-get/
    public InputStream makeConnection(URL url) {

        HttpURLConnection conn = null;
        InputStream stream = null;

        try {
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(5000);
            conn.setConnectTimeout(5000);
            //conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();

            // redirection reference: http://www.mkyong.com/java/java-httpurlconnection-follow-redirect-example/
            boolean redirect = false;

            // normally, 3xx is redirect
            int status = conn.getResponseCode();
            if (status != HttpURLConnection.HTTP_OK) {
                if (status == HttpURLConnection.HTTP_MOVED_TEMP
                        || status == HttpURLConnection.HTTP_MOVED_PERM
                        || status == HttpURLConnection.HTTP_SEE_OTHER)
                    redirect = true;
            }

            if (redirect) {

                // get redirect url from "location" header field
                String newUrl = conn.getHeaderField("Location");
                // open the new connnection again
                conn = (HttpURLConnection) new URL(newUrl).openConnection();
                System.out.println("Redirect to URL : " + newUrl);

            }
            respo = conn.getResponseCode();
            stream = conn.getInputStream();

        } catch (Exception e) {
            e.printStackTrace();
    } finally {
            if (stream != null) {
                try {
                    stream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (conn != null)
                conn.disconnect();
        }
        return stream;
    }

    // reference: http://androidpala.com/android-httpurlconnection-post-get/
    // we used the exact same function from the reference for this part.
    String convertStreamToString(InputStream stream) {

        InputStreamReader isr = new InputStreamReader(stream);
        BufferedReader reader = new BufferedReader(isr);
        StringBuilder response = new StringBuilder();

        String line = null;
        try {

            while ((line = reader.readLine()) != null) {
                response.append(line);
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {

            try {
                stream.close();

            } catch (IOException e) {
                e.printStackTrace();
            } catch (Exception e) {
                e.printStackTrace();            }
        }
        return response.toString();
    }

}