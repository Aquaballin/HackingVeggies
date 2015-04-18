package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.parse.ParseUser;

/**
 * Activity which displays a login screen to the user, offering registration as well.
 */

import com.parse.ParseAnonymousUtils;

public class CheckUserActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (ParseAnonymousUtils.isLinked(ParseUser.getCurrentUser())) {
            Intent intent = new Intent(CheckUserActivity.this,
                    LoginSignupActivity.class);
            startActivity(intent);
            finish();
        } else {
            ParseUser currentUser = ParseUser.getCurrentUser();
            if (currentUser != null) {
                Intent intent = new Intent(CheckUserActivity.this, LoginSuccess.class);
                startActivity(intent);
                finish();
            } else {
                Intent intent = new Intent(CheckUserActivity.this,
                        LoginSignupActivity.class);
                startActivity(intent);
                finish();
            }
        }

    }
}