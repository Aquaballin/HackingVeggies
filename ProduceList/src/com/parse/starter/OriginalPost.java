package com.parse.starter;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.text.DecimalFormat;


public class OriginalPost extends Activity {

    String objID;
    private ParseQueryAdapter<PostObject> queryAdapter;
    private ParseQueryAdapter.QueryFactory<PostObject> queryRequirements;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_page);

        objID = getIntent().getExtras().getString("objectID");
        queryRequirements = new ParseQueryAdapter.QueryFactory<PostObject>() {
            @Override
            public ParseQuery<PostObject> create() {
                ParseQuery query = new ParseQuery("PostObject");
                query.whereMatches("objectId", objID);
                return query;
            }
        };

        queryAdapter = new ParseQueryAdapter<PostObject>(this, queryRequirements) {
            @Override
            public View getItemView(final PostObject object, View v, ViewGroup parent) {
                if (v == null) {
                    v = View.inflate(getContext(), R.layout.post_page, null);
                }

                ParseImageView postImage = (ParseImageView) v.findViewById(R.id.icon);
                ParseFile imageFile = object.getParseFile("Image");

                if (imageFile != null) {
                    postImage.setParseFile(imageFile);
                    postImage.loadInBackground();
                }
                return v;
            }
        };



    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_original_post, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
