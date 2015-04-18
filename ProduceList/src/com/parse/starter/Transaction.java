package com.parse.starter;

import com.parse.ParseClassName;
import com.parse.ParseObject;
/**
 * Created by Nick Barrett on 4/17/15.
 */
@ParseClassName("Transaction")
public class Transaction extends ParseObject
{
    private String getBuyer()
    {
        return getString("buyer");
    }

    private void setBuyer(String buyer)
    {
        put("buyer", buyer);
    }

    private int getComplete()
    {
        return getInt("complete");
    }

    private void initialComplete()
    {
        put("complete", 0);
    }

    private void completed()
    {
        put("complete", 1);
    }

    private int getIgnore()
    {
        return getInt("ignore");
    }

    private void initialIgnore()
    {
        put("ignore", 0);
    }

    private void ignored()
    {
        put("ignore", 1);
    }

    private int getRating()
    {
        return getInt("rating");
    }

    private void setRating(int rating)
    {
        put("rating", rating);
    }

    private String getSeller()
    {
        return getString("seller");
    }

    private void setSeller(String seller)
    {
        put("seller", seller);
    }


}
