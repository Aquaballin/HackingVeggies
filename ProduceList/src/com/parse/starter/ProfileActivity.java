package com.parse.starter;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by liam on 4/19/2015.
 */
public class ProfileActivity extends Activity {
    ImageView profile;
    String usernametxt;
    String emailtxt;
    String phonetxt;
    TextView username;
    String locationtxt;
    ParseFile profilepic;
    ArrayList userdata = new ArrayList<String>();


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from main.xml
        setContentView(R.layout.profile);
        ParseUser currentUser = ParseUser.getCurrentUser();
        usernametxt = currentUser.getUsername().toString();

        username = (TextView) findViewById(R.id.username);
        username.setText(usernametxt);


    }
}
