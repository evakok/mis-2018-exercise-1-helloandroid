package com.example.mis.helloandroid;

import android.content.Intent;
import android.net.Uri;
import android.os.Parcel;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.text.TextUtils;

import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;


public class MainActivity extends AppCompatActivity {
    private EditText uriText;
    private Button connect;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        connect = (Button) findViewById(R.id.button);
        uriText = (EditText) findViewById(R.id.url_input);
        connect.setOnClickListener(new View.OnClickListener(){
            public void onClick(View View){
                openBrowser();
            }
            });
        uriText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View view, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER) {
                    openBrowser();
                    return true;
                }
                return false;
            }
        });
        }
        private void openBrowser() {
            String uri = uriText.getText().toString();
            if(uri.indexOf("http:/www.") < 0 ){
                uri = "http://www." + uri;
            }
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
            startActivity(intent);
        }



    //public void HTTPConnection(View view) {
        //URL url = new URL();
        //Toast toast = Toast.makeText(MainActivity.this, "hey there", Toast.LENGTH_SHORT);
        //toast.setGravity(Gravity.CENTER, 0 ,0);
        //toast.show();
    //}
}

