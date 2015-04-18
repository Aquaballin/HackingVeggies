package com.parse.starter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.FusedLocationProviderApi;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import org.w3c.dom.Text;

import java.io.ByteArrayOutputStream;

import static com.parse.starter.R.id.Description;
import static com.parse.starter.R.id.img;
import static com.parse.starter.R.id.postButton;


public class MakeNewPost extends Activity implements AdapterView.OnItemSelectedListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    PostObject newPost;
    String foodCategory;
    double price;
    int quantity;
    ParseFile picture;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap image;
    EditText choosePrice;
    EditText chooseQuantity;
    EditText Description;
    TextView locationview;
    Spinner spinner;
    String userNameToCloud;
    String descriptionText;
    GoogleApiClient mGoogleApiClient;
    LocationRequest mLocationRequest;
    Location mLastLocation;
    String mLatitudeText;
    String mLongitudeText;
    LocationListener mlistener;
    private TextView mLocationView;
    private LocationRequest locationRequest;
    private final String TAG = "Logdata";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        locationview = (TextView) findViewById(R.id.locationview);
        setContentView(R.layout.activity_make_new_post);

        mGoogleApiClient = new GoogleApiClient.Builder(this) // this is a Context
                .addApi(LocationServices.API)
                .addConnectionCallbacks(this)  // this is a [GoogleApiClient.ConnectionCallbacks][1]
                .addOnConnectionFailedListener(this) //
                .build();




        //fruit, veggies, meat
        spinner = (Spinner) findViewById(R.id.categories);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        choosePrice = (EditText) findViewById(R.id.editText);
        chooseQuantity = (EditText) findViewById(R.id.editText2);
        Description = (EditText) findViewById(R.id.Description);

        Button take_picture = (Button) findViewById(R.id.addPicButton);
        take_picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        Button add_post_to_parse = (Button) findViewById(R.id.savePostButton);

        add_post_to_parse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userNameToCloud = ParseUser.getCurrentUser().toString();
                price = Double.parseDouble(choosePrice.getText().toString());
                quantity = Integer.parseInt(chooseQuantity.getText().toString());
                foodCategory = spinner.getSelectedItem().toString();
                descriptionText = Description.getText().toString();
                newPost = new PostObject();
                newPost.put("Username",userNameToCloud);
                newPost.put("Price",price);
                newPost.put("Quantity",quantity);
                newPost.put("Description", descriptionText);
                if (mLastLocation != null) {
                    newPost.put("Location", mLastLocation);
                }
                if (picture != null) {
                    newPost.put("Picture", picture);
                }
                if (foodCategory == null) {
                    foodCategory = "Other";
                }
                newPost.put("Category",foodCategory);
                newPost.saveInBackground();
                Toast.makeText(getBaseContext(),"POST CREATED",
                        Toast.LENGTH_LONG).show();
                finish();

            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        // Connect the client.
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        // Disconnecting the client invalidates it.
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onConnected(Bundle bundle) {
        mlistener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                mLastLocation = location;
            }
        };

        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(1000); // Update location every second

        LocationServices.FusedLocationApi.requestLocationUpdates(
                mGoogleApiClient, mLocationRequest, mlistener);
    }

    @Override
    public void onConnectionSuspended(int i) {
        Log.i(TAG, "GoogleApiClient connection has been suspend");
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        Log.i(TAG, "GoogleApiClient connection has failed");
    }




    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        byte[] image_byte_array;
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            ParseGeoPoint parseGeoPoint;
            Bundle extras = data.getExtras();
            image = (Bitmap) extras.get("data");
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            image.compress(Bitmap.CompressFormat.PNG, 100, stream);
            image_byte_array = stream.toByteArray();
            picture = new ParseFile("Picture", image_byte_array);
            picture.saveInBackground();
            ImageView img = (ImageView) findViewById(R.id.img);
            img.setImageBitmap(image);
            Button rotateButton = (Button) findViewById (R.id.rotateButton);




        }
    }

    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_make_new_post, menu);
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        foodCategory = (String)parent.getItemAtPosition(position);

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    public void Rotate(View v)
    {
        ImageView img = (ImageView)findViewById(R.id.img);
        Bitmap bmp = image;
// Getting width & height of the given image.
        int w = bmp.getWidth();
        int h = bmp.getHeight();
// Setting pre rotate to 90
        Matrix mtx = new Matrix();
        mtx.preRotate(90);
// Rotating Bitmap
        Bitmap rotatedBMP = Bitmap.createBitmap(bmp, 0, 0, w, h, mtx, true);
        BitmapDrawable bmd = new BitmapDrawable(rotatedBMP);
        img.setImageDrawable(bmd);
    }
}
