package com.example.myapp_x;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.r0adkll.slidr.Slidr;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Info extends AppCompatActivity {

    File directory;
    DBHelper dbHelper;
    EditText editTextFirstName, editTextLastName, editTextNamePrefix, editTextNameSuffix,
            editTextNumber, editTextEmail, editTextWebsite, editTextFacebook, editTextInstagram;
    Bitmap bmp;
    private final static String LOG_TAG = "Activity Info";

    private ImageView ivImage;
    Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info);
        ivImage = findViewById(R.id.imageView);
        createDirectory();
        dbHelper = new DBHelper(this);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextNamePrefix = findViewById(R.id.editTextNamePrefix);
        editTextNameSuffix = findViewById(R.id.editTextNameSuffix);
        editTextNumber = findViewById(R.id.editTextNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextWebsite = findViewById(R.id.editTextWebsite);
        editTextFacebook = findViewById(R.id.editTextFacebook);
        editTextInstagram = findViewById(R.id.editTextInstagram);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        Slidr.attach(this);
    }

    private void selectImage() {
        final CharSequence[] items = {"Camera", "Gallery", "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(Info.this);
        builder.setTitle("Add Image");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                if (items[i].equals("Camera")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(intent, REQUEST_CAMERA);

                } else if (items[i].equals("Gallery")) {
                    Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(intent.createChooser(intent, "Select File"), SELECT_FILE);

                } else if (items[i].equals("Cancel")) {
                    dialogInterface.dismiss();
                }
            }
        });
        builder.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                Bundle bundle = data.getExtras();
                bmp = (Bitmap) bundle.get("data");
                ivImage.setImageBitmap(bmp);
            } else if (requestCode == SELECT_FILE) {
                Uri selectImageUri = data.getData();
                ivImage.setImageURI(selectImageUri);
                try {
                    ivImage.buildDrawingCache();
                    bmp = ivImage.getDrawingCache();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        }
    }

    private void createDirectory() {
        directory = new File(
                Environment
                        .getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),
                "MyFolder");
        if (!directory.exists())
            directory.mkdirs();
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onClickButton(View v) {
        SQLiteDatabase database = dbHelper.getWritableDatabase();

        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String namePrefix = editTextNamePrefix.getText().toString();
        String nameSuffix = editTextNameSuffix.getText().toString();
        String number = editTextNumber.getText().toString();
        String email = editTextEmail.getText().toString();
        String website = editTextWebsite.getText().toString();
        String facebook = editTextFacebook.getText().toString();
        String instagram = editTextInstagram.getText().toString();
        if (bmp != null) {

            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            String strBase64 = Base64.encodeToString(byteArray, 0);

            ContentValues contentValues = new ContentValues();
            contentValues.put(DBHelper.KEY_PICTURE, strBase64);
            contentValues.put(DBHelper.KEY_FIRST_NAME, firstName);
            contentValues.put(DBHelper.KEY_LAST_NAME, lastName);
            contentValues.put(DBHelper.KEY_NAME_PREFIX, namePrefix);
            contentValues.put(DBHelper.KEY_NAME_SUFFIX, nameSuffix);
            contentValues.put(DBHelper.KEY_NUMBER, number);
            contentValues.put(DBHelper.KEY_EMAIL, email);
            contentValues.put(DBHelper.KEY_WEBSITE, website);
            contentValues.put(DBHelper.KEY_FACEBOOK, facebook);
            contentValues.put(DBHelper.KEY_INSTAGRAM, instagram);
            database.insert(DBHelper.TABLE_CONTACTS, null, contentValues);
            dbHelper.close();
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), R.string.foto, Toast.LENGTH_SHORT).show();
        }
    }

}
