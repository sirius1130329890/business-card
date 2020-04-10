package com.example.myapp_x;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.r0adkll.slidr.Slidr;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SelectAll extends AppCompatActivity {
    DBHelper dbHelper;
    final String LOG_TAG = "myLogs";
    LinearLayout layoutAllViews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_all);
        dbHelper = new DBHelper(this);
        layoutAllViews = findViewById(R.id.layoutAllViews);
        allViews();
        Slidr.attach(this);
    }

    public void allViews(){
        int i = 0, a = 1;
        LinearLayout.LayoutParams layoutAllViewsParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,280);
        LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.MATCH_PARENT);
        LinearLayout.LayoutParams firstParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        SQLiteDatabase database = dbHelper.getWritableDatabase();


        try {
            Cursor cursor = database.query("contacts",
                    null, null,
                    null,
                    null, null, null);
            cursor.moveToFirst();
            Log.d(LOG_TAG, "Все ок");
            do {
                final int id = cursor.getColumnIndex("_id");
                final String idIndex = cursor.getString(id);
                final int id_id = Integer.parseInt(idIndex);

                final int picture = cursor.getColumnIndex("picture");
                int firstName = cursor.getColumnIndex("firstName");
                int lastName = cursor.getColumnIndex("lastName");
                int namePrefix = cursor.getColumnIndex("namePrefix");
                int nameSuffix = cursor.getColumnIndex("nameSuffix");
                String pic = cursor.getString(picture);
                String fir = cursor.getString(firstName);
                String las = cursor.getString(lastName);
                String prf = cursor.getString(namePrefix);
                String suf = cursor.getString(nameSuffix);
                Log.d(LOG_TAG, "REST: subj: " + cursor.getString(picture));

                byte[] decodedString = Base64.decode(pic, Base64.DEFAULT);
                Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                LinearLayout layoutMyViews = new LinearLayout(this);
                layoutAllViewsParams.weight = 10;
                layoutAllViewsParams.setMargins(5,5,5,5);
                layoutMyViews.setOrientation(LinearLayout.HORIZONTAL);
                LinearLayout firstLayout = new LinearLayout(this);
                firstParams.weight = 1;
                firstParams.setMargins(20,20,20,20);
                firstLayout.setBackgroundResource(R.color.blue);
                imageParams.gravity = Gravity.CENTER;
                ImageView imageView = new ImageView(this);
                imageParams.setMargins(20,20,20,20);
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                imageView.setImageBitmap(decodedByte);
                firstLayout.addView(imageView,imageParams);
                layoutMyViews.addView(firstLayout,firstParams);
                LinearLayout linearLayout = new LinearLayout(this);
                linearLayout.setOrientation(LinearLayout.VERTICAL);
                layoutParams.weight=1;
                TextView textView = new TextView(this);
                textParams.gravity = Gravity.CENTER;
                textParams.weight=1;
                textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 15);
                textView.setText(fir+" "+las);
                textView.setTextColor(Color.WHITE);
                textParams.setMargins(5,25,0,0);
                linearLayout.addView(textView,textParams);
                TextView textView1 = new TextView(this);
                textView1.setText(prf+" "+suf);
                textView1.setTextColor(Color.WHITE);
                linearLayout.addView(textView1,textParams);
                layoutMyViews.addView(linearLayout,layoutParams);
                final ImageView imageViewButton = new ImageView(this);
                imageViewButton.setImageResource(R.drawable.edit);
                buttonParams.gravity = Gravity.CENTER;
                buttonParams.weight = 1;
                buttonParams.setMargins(100,100,100,100);
                layoutAllViewsParams.setMargins(5,10,5,5);
                layoutMyViews.addView(imageViewButton,buttonParams);
                layoutMyViews.setBackgroundResource(R.drawable.hape);
                layoutAllViews.addView(layoutMyViews,layoutAllViewsParams);

                imageViewButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int s =id_id - 1;
                        Intent intent = new Intent(SelectAll.this,Edit.class);
                        intent.putExtra("id",s);
                        startActivity(intent);
                        Log.d(LOG_TAG, "id button "+id_id);

                    }
                });
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        int s = id_id - 1;
                        Intent intent = new Intent(SelectAll.this, Show.class);
                        intent.putExtra("id", s);
                        startActivity(intent);

                    }
                });

                ++i;

            } while (cursor.moveToNext());

            cursor.close();
        }catch (Exception e) {
            Log.d(LOG_TAG, "чувак біда");
        }
    }

}
