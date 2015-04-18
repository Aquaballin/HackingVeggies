package com.parse.starter;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.GetCallback;
import com.parse.ParseFile;
import com.parse.ParseImageView;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;

import java.text.DecimalFormat;
import java.text.ParseException;


public class OriginalPost extends Activity {


    String title;
    String uname;
    String location;
    String category;
    String price;
    ParseFile img;
    String objID;
    private ParseQueryAdapter<PostObject> queryAdapter;
    private ParseQueryAdapter.QueryFactory<PostObject> queryRequirements;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.post_page);

        objID = getIntent().getExtras().getString("objectID");
        ParseQuery<ParseObject> query = ParseQuery.getQuery("MyClass");
        query.getInBackground(objID, new GetCallback<ParseObject>() {
            @Override
            public void done(ParseObject object, com.parse.ParseException e) {
                if (e == null) {
                    Toast.makeText(getApplicationContext(), "QUERY", Toast.LENGTH_LONG);
                    title = object.get("Title").toString();
                    uname = object.get("Username").toString();
                    location = object.get("Location").toString();
                    category = object.get("Category").toString();
                    price = object.get("Price").toString();
                    img = object.getParseFile("Picture");
                    populateFields(title, uname, location, category, price, img);

                } else {
                    objectRetrievalFailed();
                }
            }

            private void objectRetrievalFailed() {
                Toast.makeText(getApplicationContext(), "NO ID FOR objID", Toast.LENGTH_LONG);
            }


        });
        /*
        queryAdapter = new ParseQueryAdapter<PostObject>(this, queryRequirements) {
            @Override
            public View getItemView(final PostObject object, View v, ViewGroup parent) {
                if (v == null) {
                    v = View.inflate(getContext(), R.layout.post_page, null);
                }

                ParseImageView postImage = (ParseImageView) v.findViewById(R.id.icon);
                ParseFile imageFile = object.getParseFile("Image");
                Toast.makeText(getApplicationContext(),
                        objID, Toast.LENGTH_LONG)
                        .show();
                finish();

                TextView username = (TextView) v.findViewById(R.id.username);
                username.setText(objID);

                if (imageFile != null) {
                    postImage.setParseFile(imageFile);
                    postImage.loadInBackground();
                }
                return v;
            }
        };
        */


    }

        private void populateFields(String title, String uname, String location, String category, String price, ParseFile img){
        TextView username = (TextView) findViewById(R.id.username);
        username.setText(uname);
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
