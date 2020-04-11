package com.example.myapp_x;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.r0adkll.slidr.Slidr;

public class Show extends AppCompatActivity {
    DBHelper dbHelper;
    int id;
    final String LOG_TAG = "Show activity";
    ImageView ivImage;
    TextView textView1, textView2;
    LinearLayout linearLayout;
    int[] imageViews = new int[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show);
        dbHelper = new DBHelper(this);
        ivImage = findViewById(R.id.imageView2);
        textView1 = findViewById(R.id.textView2);
        textView2 = findViewById(R.id.textView3);
        linearLayout = findViewById(R.id.layoutShow);
        imageViews[0] = R.drawable.phone;
        imageViews[1] = R.drawable.email;
        imageViews[2] = R.drawable.website;
        imageViews[3] = R.drawable.facebook;
        imageViews[4] = R.drawable.instagram;
        onSelect();
        Slidr.attach(this);
    }

    public void onSelect() {
        Intent intent = getIntent();
        id = intent.getIntExtra("id", 0);
        SQLiteDatabase database = dbHelper.getWritableDatabase();
        try {


            Cursor cursor = database.query("contacts",
                    null, null,
                    null,
                    null, null, null);
            cursor.moveToPosition(id);
            Log.d(LOG_TAG, "" + id);
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
            final String num = cursor.getString(number);
            final String ema = cursor.getString(email);
            final String web = cursor.getString(website);
            final String fac = cursor.getString(facebook);
            final String ins = cursor.getString(instagram);
            String[] hallo = new String[]{num, ema, web, fac, ins};
            byte[] decodedString = Base64.decode(pic, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            ivImage.setImageBitmap(decodedByte);
            textView1.setText(fir + " " + las);
            textView2.setText(pre + " " + suf);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams viewParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            LinearLayout.LayoutParams imageParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            for (int i = 0; i < 5; i++) {
                if (!hallo[i].equals("")) {
                    LinearLayout layout = new LinearLayout(this);
                    layout.setOrientation(LinearLayout.HORIZONTAL);
                    ImageView imageView = new ImageView(this);
                    imageView.setImageResource(imageViews[i]);
                    imageParams.setMargins(30, 10, 0, 20);
                    //image
                    layout.addView(imageView, imageParams);
                    final TextView textView = new TextView(this);
                    textView.setText(hallo[i]);
                    textView.setTextColor(Color.WHITE);
                    textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 20);
                    textParams.setMargins(20,0,100,0);
                    layout.addView(textView, textParams);
                    linearLayout.addView(layout, layoutParams);
                    LinearLayout secondLayout = new LinearLayout(this);
                    layout.setOrientation(LinearLayout.HORIZONTAL);
                    View view = new View(this);
                    viewParams.height=3;
                    view.setBackgroundResource(R.drawable.line);
                    viewParams.setMargins(0,0,100,40);
                    secondLayout.addView(view,viewParams);
                    linearLayout.addView(secondLayout,layoutParams);
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (textView.getText().toString().equals(num)){
                                Uri u = Uri.parse("tel:" + textView.getText().toString());
                                Intent i = new Intent(Intent.ACTION_DIAL, u);
                                startActivity(i);
                            }
                            else if (textView.getText().toString().equals(ema)){
                                Intent email = new Intent(Intent.ACTION_SEND);
                                email.putExtra(Intent.EXTRA_EMAIL, new String[]{ ema});
                                email.setType("message/rfc822");
                                startActivity(Intent.createChooser(email, "Виберіть проштовий сервіс :"));
                            }
                            else if (textView.getText().toString().equals(web)){
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://"+web)));
                            }
                            else if (textView.getText().toString().equals(fac)){
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://facebook.com/"+fac)));
                            }
                            else if (textView.getText().toString().equals(ins)){
                                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://instagram.com/"+ins)));
                            }
                        }
                    });

                }
            }
            cursor.close();
        } catch (Exception e) {
            Log.d(LOG_TAG, " біда");


        }


    }

}
