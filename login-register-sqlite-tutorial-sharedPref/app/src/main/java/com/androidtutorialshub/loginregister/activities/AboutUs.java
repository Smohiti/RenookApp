package com.androidtutorialshub.loginregister.activities;

import android.graphics.Typeface;
import android.os.Bundle;
import android.text.Html;
import android.widget.ImageView;
import android.widget.TextView;

import com.androidtutorialshub.loginregister.R;

/**
 * Created by smohi on 8/11/2017.
 */

public class AboutUs extends UsersListActivity {

    private String getColoredSpanned(String text, String color) {
        String input = "<font color=" + color + ">" + text + "</font>";
        return input;
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);


        TextView text2 = (TextView) findViewById(R.id.textView8);
        String M4 = getColoredSpanned("The 'Renook' App was made as a final project in 'MAP524' Course." +
                " This is an app to search the book through the library database and rent the desired book while is generating the " +
                "'QRCode' there are different options such as see the list of rented bood in profile, sending due date notification and share" +
                "the book with friends." +
                "Handling sqlite, was the one of most challenging part in this project in terms of storing and retrieving " +
                "different data. Used 'JSON' to get the info through the open library for searching the books.   ", "#51d8c7");
        text2.setTextSize(20);
        text2.setTypeface(null, Typeface.ITALIC);
        text2.setText(Html.fromHtml(M4));

        ImageView imageView = (ImageView) findViewById(R.id.imageView2);
        imageView.setImageResource(R.drawable.saeed);

    }
}
