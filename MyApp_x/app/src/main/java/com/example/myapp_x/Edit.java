package com.example.myapp_x;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.r0adkll.slidr.Slidr;

import java.io.ByteArrayOutputStream;
import java.io.File;

public class Edit extends AppCompatActivity {
    EditText editTextFirstName, editTextLastName, editTextNamePrefix, editTextNameSuffix,
            editTextNumber, editTextEmail, editTextWebsite, editTextFacebook, editTextInstagram;
    File directory;
    DBHelper dbHelper;
    Integer REQUEST_CAMERA = 1, SELECT_FILE = 0;
    Bitmap bmp;
    private ImageView ivImage;
    final String LOG_TAG = "Edit activity";
    int id,position;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ivImage = findViewById(R.id.imageView);
        editTextFirstName = findViewById(R.id.editTextFirstName);
        editTextLastName = findViewById(R.id.editTextLastName);
        editTextNamePrefix = findViewById(R.id.editTextNamePrefix);
        editTextNameSuffix = findViewById(R.id.editTextNameSuffix);
        editTextNumber = findViewById(R.id.editTextNumber);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextWebsite = findViewById(R.id.editTextWebsite);
        editTextFacebook = findViewById(R.id.editTextFacebook);
        editTextInstagram = findViewById(R.id.editTextInstagram);
        createDirectory();
        dbHelper = new DBHelper(this);
        onSelect();

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
        AlertDialog.Builder builder = new AlertDialog.Builder(Edit.this);
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
        } else {
            BitmapDrawable drawable = (BitmapDrawable) ivImage.getDrawable();
            bmp = drawable.getBitmap();
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

    public void onSelect() {
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        id = intent.getIntExtra("id", 0);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        try {
            Cursor cursor = database.query("contacts",
                    null, null,
                    null,
                    null, null, null);
            cursor.moveToPosition(position);
            Log.d(LOG_TAG, "Id = " + position);
            int picture = cursor.getColumnIndex("picture");
            int firstName = cursor.getColumnIndex("firstName");
            int lastName = cursor.getColumnIndex("lastName");
            int namePrefix = cursor.getColumnIndex("namePrefix");
            int nameSuffix = cursor.getColumnIndex("nameSuffix");
            int number = cursor.getColumnIndex("number");
            int email = cursor.getColumnIndex("email");
            int website = cursor.getColumnIndex("website");
            int facebook = cursor.getColumnIndex("facebook");
            int instagram = cursor.getColumnIndex("instagram");
            String pic = cursor.getString(picture);
            String fir = cursor.getString(firstName);
            String las = cursor.getString(lastName);
            String pre = cursor.getString(namePrefix);
            String suf = cursor.getString(nameSuffix);
            String num = cursor.getString(number);
            String ema = cursor.getString(email);
            String web = cursor.getString(website);
            String fac = cursor.getString(facebook);
            String ins = cursor.getString(instagram);
            byte[] decodedString = Base64.decode(pic, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivImage.setImageBitmap(decodedByte);
            editTextFirstName.setText(fir);
            editTextLastName.setText(las);
            editTextNamePrefix.setText(pre);
            editTextNameSuffix.setText(suf);
            editTextNumber.setText(num);
            editTextEmail.setText(ema);
            editTextWebsite.setText(web);
            editTextFacebook.setText(fac);
            editTextInstagram.setText(ins);
            cursor.close();
        } catch (Exception e) {
            Log.d(LOG_TAG, "біда");
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onClickButton(View v) {
        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        final String idIndex = Integer.toString(id);

        String firstName = editTextFirstName.getText().toString();
        String lastName = editTextLastName.getText().toString();
        String namePrefix = editTextNamePrefix.getText().toString();
        String nameSuffix = editTextNameSuffix.getText().toString();
        String number = editTextNumber.getText().toString();
        String email = editTextEmail.getText().toString();
        String website = editTextWebsite.getText().toString();
        String facebook = editTextFacebook.getText().toString();
        String instagram = editTextInstagram.getText().toString();
        final ContentValues contentValues = new ContentValues();
        if (bmp != null) {
            ByteArrayOutputStream stream = new ByteArrayOutputStream();
            bmp.compress(Bitmap.CompressFormat.JPEG, 100, stream);
            byte[] byteArray = stream.toByteArray();
            String strBase64 = Base64.encodeToString(byteArray, 0);
            contentValues.put(DBHelper.KEY_PICTURE, strBase64);
        }
        contentValues.put(DBHelper.KEY_FIRST_NAME, firstName);
        contentValues.put(DBHelper.KEY_LAST_NAME, lastName);
        contentValues.put(DBHelper.KEY_NAME_PREFIX, namePrefix);
        contentValues.put(DBHelper.KEY_NAME_SUFFIX, nameSuffix);
        contentValues.put(DBHelper.KEY_NUMBER, number);
        contentValues.put(DBHelper.KEY_EMAIL, email);
        contentValues.put(DBHelper.KEY_WEBSITE, website);
        contentValues.put(DBHelper.KEY_FACEBOOK, facebook);
        contentValues.put(DBHelper.KEY_INSTAGRAM, instagram);
        database.update(DBHelper.TABLE_CONTACTS, contentValues, DBHelper.KEY_ID + "= ?", new String[]{idIndex});
        dbHelper.close();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

    }

    public void onClickDelete(View v){
        final SQLiteDatabase database = dbHelper.getWritableDatabase();
        final String idIndex = Integer.toString(id);

        DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                switch (which){
                    case DialogInterface.BUTTON_POSITIVE:
                        //Yes button clicked
                        database.delete(DBHelper.TABLE_CONTACTS,  DBHelper.KEY_ID + "="+idIndex,null);
                        dbHelper.close();
                        startActivity(new Intent(Edit.this,MainActivity.class));
                        break;

                    case DialogInterface.BUTTON_NEGATIVE:
                        //No button clicked
                        break;
                }
            }
        };

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Ви дійсно бажаєте видалити візитку?").setPositiveButton("Так", dialogClickListener)
                .setNegativeButton("Ні", dialogClickListener).show();

    }
}
