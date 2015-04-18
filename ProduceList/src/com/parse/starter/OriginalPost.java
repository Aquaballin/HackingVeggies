package com.parse.starter;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
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
    ListView listView2;


    TextView categoryTextView2, priceTextView2, quantityTextView2, userTextView2, locationTextView2;

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
                    v = View.inflate(getContext(), R.layout.post_item2, null);
                }
                categoryTextView2 = (TextView) v.findViewById(R.id.categoryTextView2);
                priceTextView2 = (TextView) v.findViewById(R.id.priceTextView2);
                quantityTextView2 = (TextView) v.findViewById(R.id.quantityTextView2);
                userTextView2 = (TextView) v.findViewById(R.id.userTextView2);
                locationTextView2 = (TextView) v.findViewById(R.id.locationTextView2);

                categoryTextView2.setText(object.getCategory());
                priceTextView2.setText(String.valueOf(object.getDouble("Price")));
                quantityTextView2.setText(String.valueOf(object.getInt("Quantity")));
                userTextView2.setText(object.getUser().toString());
                locationTextView2.setText("Harrisonburg, VA");

                ParseImageView postImage = (ParseImageView) v.findViewById(R.id.icon2);
                ParseFile imageFile = object.getParseFile("Image");

                if (imageFile != null) {
                    postImage.setParseFile(imageFile);
                    postImage.loadInBackground();
                }
                return v;
            }
        };
        listView2 = (ListView) findViewById(R.id.ghettoListView);
        queryAdapter.setPaginationEnabled(true);
        queryAdapter.setTextKey("title");
        queryAdapter.setImageKey("Image");
        queryAdapter.loadObjects();
        listView2.setAdapter(queryAdapter);
        queryAdapter.loadObjects();




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
