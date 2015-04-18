package com.parse.starter;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseImageView;
import com.parse.ParseQuery;
import com.parse.ParseQueryAdapter;
import com.parse.ParseUser;

import android.app.Activity;
import android.content.Intent;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DecimalFormat;

import static com.google.android.gms.common.api.GoogleApiClient.*;

public class LoginSuccess extends Activity implements OnConnectionFailedListener, ConnectionCallbacks {

    Button logout, post, profile;
    private ParseQueryAdapter<PostObject> queryAdapter;
    private ParseQueryAdapter.QueryFactory<PostObject> queryRequirements;
    private int RANGE = 40;
    double latitude, longitude;
    private Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    private static final String TAG = LoginSuccess.class.getSimpleName();
    PullToRefreshListView refreshListView;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_success);
        ParseUser currentUser = ParseUser.getCurrentUser();
        String struser = currentUser.getUsername().toString();
        TextView txtuser = (TextView) findViewById(R.id.txtuser);
        txtuser.setText("You are logged in as " + struser);
        logout = (Button) findViewById(R.id.logout);
        post = (Button) findViewById(R.id.postButton);
        profile = (Button) findViewById(R.id.profileButton);

        latitude = 38.4339305;
        longitude = -78.8629357;

        post.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginSuccess.this, MakeNewPost.class));
            }
        });

        profile.setOnClickListener(new OnClickListener() {
            @Override

            public void onClick(View v){
                Intent myIntent = new Intent(LoginSuccess.this, ProfileActivity.class);
                //myIntent.putExtra("key", hello); //Optional parameters
                LoginSuccess.this.startActivity(myIntent);
            }
        });

        if (checkPlayServices()) {
            buildGoogleApiClient();
        }


        queryRequirements = new ParseQueryAdapter.QueryFactory<PostObject>() {
            @Override
            public ParseQuery<PostObject> create() {
                ParseQuery query = new ParseQuery("PostObject");
                query.orderByDescending("createdAt");

                return query;
            }
        };

        queryAdapter = new ParseQueryAdapter<PostObject>(this, queryRequirements) {
            @Override
            public View getItemView(final PostObject object, View v, ViewGroup parent) {
                if (v == null) {
                    v = View.inflate(getContext(), R.layout.post_item, null);
                }
                Button originalPostButton = (Button) v.findViewById(R.id.viewPostButton);
                originalPostButton.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(LoginSuccess.this,OriginalPost.class);
                        intent.putExtra("objectID",object.getObjectId());

                        startActivity(intent);
                    }
                });
                ParseImageView postImage = (ParseImageView) v.findViewById(R.id.icon);
                ParseFile imageFile = object.getParseFile("Picture");
                TextView categoryText = (TextView) v.findViewById(R.id.categoryTextView);
                categoryText.setText(object.getString("Category"));
                TextView quantityText = (TextView) v.findViewById(R.id.quantityTextView);
                quantityText.setText("Quantity: " + String.valueOf(object.getInt("Quantity")));
                TextView priceText = (TextView) v.findViewById(R.id.priceTextView);
                priceText.setText("$" + String.valueOf(object.getDouble("Price")) + " per unit");
                if (imageFile != null) {
                    postImage.setParseFile(imageFile);
                    postImage.loadInBackground();
                }
                return v;
            }
        };


        queryAdapter.setPaginationEnabled(true);
        queryAdapter.setTextKey("title");
        queryAdapter.setImageKey("Image");
        queryAdapter.loadObjects();
        refreshListView = (PullToRefreshListView) findViewById(R.id.list);
        refreshListView.setAdapter(queryAdapter);
        refreshListView.setOnRefreshListener(new PullToRefreshListView.OnRefreshListener() {
            @Override
            public void onRefresh() {
                queryAdapter.loadObjects();
                refreshListView.onRefreshComplete();
            }
        });
        queryAdapter.loadObjects();


        logout.setOnClickListener(new OnClickListener() {

            public void onClick(View arg0) {
                ParseUser.logOut();
                finish();
            }
        });
    }

    private static String formatTheDateString(String string) {

        String return_string = ""; //string that will be returned
        String[] tokens = string.split("\\s+"); //initial getCreatedAt() string
        String[] military_date_split = tokens[3].split(":"); //split the military time

        if (Integer.parseInt(military_date_split[0]) == 00) {
            return_string = tokens[0] + " " + tokens[1] + " " + tokens[2] + " "
                    + 12 + ":" + military_date_split[1] + "AM"
                    + " " + tokens[4] + " " + tokens[5];
        } else if (Integer.parseInt(military_date_split[0]) == 24) {
            return_string = tokens[0] + " " + tokens[1] + " " + tokens[2] + " "
                    + 12 + ":" + military_date_split[1] + "AM"
                    + " " + tokens[4] + " " + tokens[5];
        } else if (Integer.parseInt(military_date_split[0]) == 12) {
            return_string = tokens[0] + " " + tokens[1] + " " + tokens[2] + " "
                    + 12 + ":" + military_date_split[1] + "PM"
                    + " " + tokens[4] + " " + tokens[5];
        } else if ((Integer.parseInt(military_date_split[0]) <= 12)) {//if its less than 12 its AM
            return_string = tokens[0] + " " + tokens[1] + " " + tokens[2] + " "
                    + military_date_split[0] + ":" + military_date_split[1] + "AM"
                    + " " + tokens[4] + " " + tokens[5];
            //Sat Mar 28
        } else if (Integer.parseInt(military_date_split[0]) > 12) { //if its >= 12 then its pm
            return_string = tokens[0] + " " + tokens[1] + " " + tokens[2] + " "
                    + (Integer.parseInt(military_date_split[0])%12) + ":" + military_date_split[1] + "PM"
                    + " " + tokens[4] + " " + tokens[5];
        }
        return return_string;
    }


    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    /** ON START MAKE SURE THE CLIENT CONNECTS
     *
     */
    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        Log.i(TAG, "Connection failed: ConnectionResult.getErrorCode() = "
                + result.getErrorCode());
    }
    @Override
    public void onConnected(Bundle arg0) {

        // Once connected with google api, get the location
        displayTheApplicationPictures();
    }
    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
    }
    private void displayTheApplicationPictures() {
        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();
        } else {
        }
    }
}
