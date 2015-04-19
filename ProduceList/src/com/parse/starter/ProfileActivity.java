package com.parse.starter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.parse.ParseFile;

/**
 * Created by liam on 4/19/2015.
 */
public class ProfileActivity extends Activity {
    String usernametxt;
    String passwordtxt;;
    ParseFile profilepic;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from main.xml
        setContentView(R.layout.profile);


    }
}
