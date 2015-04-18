package com.parse.starter;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
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
import android.widget.Spinner;
import android.widget.TextView;

import com.parse.ParseFile;
import com.parse.ParseGeoPoint;

import java.io.ByteArrayOutputStream;

import static com.parse.starter.R.id.postButton;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_make_new_post);

        //fruit, veggies, meat
        Spinner spinner = (Spinner) findViewById(R.id.categories);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.categories, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        choosePrice = (EditText) findViewById(R.id.editText);
        chooseQuantity = (EditText) findViewById(R.id.editText);

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
                price = Double.parseDouble(choosePrice.getText().toString());
                quantity = Integer.parseInt(chooseQuantity.getText().toString());
                newPost = new PostObject();
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
                
            }
        });
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
        }
    }

    @Override
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
}
