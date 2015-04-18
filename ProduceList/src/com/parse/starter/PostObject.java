package com.parse.starter;

import com.parse.ParseClassName;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;


/**
 * Created by davidcasey on 2/27/15.
 */
@ParseClassName("PostObject")
public class PostObject extends ParseObject {

    public ParseGeoPoint returnLocation() {
        return getParseGeoPoint("Location");
    }

    public ParseFile getParseImage() {
        return getParseFile("PostImage");
    }

    public static ParseQuery<PostObject> getQuery() {
        return ParseQuery.getQuery(PostObject.class);
    }

    public void setCategory(String category) {
        put("Category", category);
    }

    public String getCategory() {
        return getString("Category");
    }

    public ParseUser getUser() {
        return getParseUser("ParseUser");
    }



}
