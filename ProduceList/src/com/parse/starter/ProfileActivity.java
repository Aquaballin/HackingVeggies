package com.parse.starter;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
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
    TextView email;
    TextView location;
    String locationtxt = "Harrisonburg, VA";
    ParseFile profilepic;
    ArrayList userdata = new ArrayList<String>();
    ParseFile profileimg;



    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Get the view from main.xml
        setContentView(R.layout.profile);
        ParseUser currentUser = ParseUser.getCurrentUser();
        usernametxt = currentUser.getUsername().toString();
        emailtxt = currentUser.getEmail().toString();
        Object loc = currentUser.get("location");
        if (loc != null){
            locationtxt = loc.toString();

        }





        username = (TextView) findViewById(R.id.username);
        email = (TextView) findViewById(R.id.email);
        location = (TextView) findViewById(R.id.location);

        username.setText(usernametxt);
        email.setText(emailtxt);
        location.setText(locationtxt);





    }
}
