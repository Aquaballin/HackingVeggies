package com.parse.starter;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.provider.MediaStore;
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

import com.google.android.gms.common.api.Api;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.plus.Plus;
import com.parse.ParseFile;
import com.parse.ParseGeoPoint;
import com.parse.ParseUser;

import java.io.ByteArrayOutputStream;

import static com.parse.starter.R.id.img;
import static com.parse.starter.R.id.postButton;
import static com.parse.starter.R.id.titleEditText;


public class MakeNewPost extends Activity implements AdapterView.OnItemSelectedListener {

    PostObject newPost;
    String foodCategory;
    double price;
    int quantity;
    ParseFile picture;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    Bitmap image;
    EditText choosePrice;
    EditText chooseQuantity;
    EditText chooseTitle;
    Spinner spinner;
    String title;
    String userNameToCloud;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_new_post);

        //fruit, veggies, meat
        spinner = (Spinner) findViewById(R.id.categories);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);



        choosePrice = (EditText) findViewById(R.id.editText);
        chooseQuantity = (EditText) findViewById(R.id.editText2);
        chooseTitle = (EditText) findViewById(R.id.titleEditText);

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
                title = chooseTitle.getText().toString();
                price = Double.parseDouble(choosePrice.getText().toString());
                quantity = Integer.parseInt(chooseQuantity.getText().toString());
                foodCategory = spinner.getSelectedItem().toString();
                newPost = new PostObject();
                newPost.put("Username",userNameToCloud);
                newPost.put("Title",title);
                newPost.put("Price",price);
                newPost.put("Quantity",quantity);
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

        buildGoogleApiClient();
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

    protected synchronized void buildGoogleApiClient() {
        GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }
}
