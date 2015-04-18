package com.parse.starter;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseCrashReporting;
import com.parse.ParseUser;

public class ParseApplication extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    ParseCrashReporting.enable(this);
    Parse.enableLocalDatastore(this);
    Parse.initialize(this, "lxFtwdxcRgznLjyVjjsUHFlBrEIBFvmG86Dl6uFe", "SU385igXTsKSTcdx1H7BMO0n6kNf6YeZV4XhxpmJ");
    ParseUser.enableAutomaticUser();
    ParseACL defaultACL = new ParseACL();
    defaultACL.setPublicReadAccess(true);
    ParseACL.setDefaultACL(defaultACL, true);
  }
}
