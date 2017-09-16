package com.androidtutorialshub.loginregister.activities;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtutorialshub.loginregister.R;
import com.androidtutorialshub.loginregister.adapters.BookAdapter;
import com.androidtutorialshub.loginregister.sql.ProfileDbHelper;

import java.util.ArrayList;
/**
 * Created by smohi on 8/11/2017.
 */

public class Profile extends UsersListActivity {

    ProfileDbHelper myDB;
    private ListView lvBooks;
    private BookAdapter bookAdapter;

    private AdapterView.OnItemClickListener myListItemClickHandler = new AdapterView.OnItemClickListener() {

        /*
        parent is the ListView, position stores the index of the item that was clicked
         */
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //get data from the view that was clicked...
            String data = "http://covernomics.org/main/images/qrcodes/alwaysopen-qr.png";
            //for now just show a Toast.. but really we could do anything we want with the data...
            // Toast.makeText(view.getContext(), "you clicked "+ data +" YUM", Toast.LENGTH_SHORT).show();
            // Toast.makeText(view.getContext(), data , Toast.LENGTH_SHORT).show();
            AlertDialog.Builder d = new AlertDialog.Builder(Profile.this);
            WebView w = new WebView(Profile.this);
            w.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.MATCH_PARENT, ActionBar.LayoutParams.MATCH_PARENT));
            w.loadData("<img style=\"width:100%\" src=\"" + data + "\" />", "text/html", "utf-8");
            d.setView(w).show();
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        setContentView(R.layout.profile);
        TextView textViewemail = (AppCompatTextView) findViewById(R.id.textViewEmail);
        String emailFromIntent = getIntent().getStringExtra("EMAIL");
        textViewemail.setText(emailFromIntent);


        ListView listView = (ListView) findViewById(R.id.lvBooks);
        myDB = new ProfileDbHelper(this);

        //populate an ArrayList<String> from the database and then view it
        ArrayList<String> theList = new ArrayList<>();
        Cursor data = myDB.getListContents();
        if (data.getCount() == 0) {
            Toast.makeText(this, "There are no contents in this list!", Toast.LENGTH_LONG).show();
        } else {
            while (data.moveToNext()) {
                theList.add(data.getString(1));
                ListAdapter listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, theList);
                listView.setAdapter(listAdapter);
                listView.setOnItemClickListener(myListItemClickHandler);

            }

        }
        final String Data1 = getIntent().getStringExtra("title");
        Button a4_button = (Button) findViewById(R.id.button4);
        //set an onclickhandler for the button
        a4_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                myDB.deleteItem();
                Toast.makeText(Profile.this, "Removed", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
