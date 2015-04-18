package com.parse.starter;

import com.parse.ParseClassName;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;

/**
 * Created by Nick Barrett on 4/17/15.
 */

@ParseClassName("User")
public class User extends ParseObject {


    private String getEmail()
    {
        return getString("email");
    }

    private void setEmail(String email)
    {
        put("email", email);
    }

    private String getName()
    {
        return getString("name");
    }

    private void setName(String name)
    {
        put("name", name);
    }

    private String getPhoneNumber()
    {
        return getString("phoneNumber");
    }

    private void setPhoneNumber(String phoneNumber)
    {
        put("phoneNumber", phoneNumber);
    }

    private String getLocation() { return getString("location");}

    private void setLocation(ParseGeoPoint location) {put ("location", location); }


    private int getRating()
    {
        return getInt("rating");
    }

    private void setRating(int rating)
    {
        int newRate;

        newRate = (getInt("rating") * getInt("transNum") + rating)/ getInt("transNum");

        put("rating", newRate);
    }

    private void incrementTransaction()
    {
        int newTrans;

        newTrans = getInt("transNum");

        newTrans++;

        put("transNum", newTrans);
    }

    private String getUsername()
    {
        return getString("username");
    }

    private void setUsername(String username)
    {
        put("username", username);
    }

    private String getPassword()
    {
        return getString("password");
    }

    private void setPassword(String password)
    {
        put("password", password);
    }
}
