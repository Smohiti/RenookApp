package com.androidtutorialshub.loginregister.activities;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.support.v7.widget.AppCompatTextView;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtutorialshub.loginregister.R;
import com.androidtutorialshub.loginregister.adapters.UsersGridAdapter;

/**
 * Created by smohi on 8/11/2017.
 */
public class UsersListActivity extends Activity {
    GridView grid;
    String[] web = {
            "Search","Profile","Tour","About us","Feedback"

    } ;
    int[] imageId = {
            R.drawable.image1,
            R.drawable.image2,
            R.drawable.image3,
            R.drawable.image4,
            R.drawable.image5,


    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_list);
        final TextView textViewName = (AppCompatTextView) findViewById(R.id.textViewName);
        String emailFromIntent = getIntent().getStringExtra("EMAIL");
        textViewName.setText(emailFromIntent);
        UsersGridAdapter adapter = new UsersGridAdapter(UsersListActivity.this, web, imageId);
        grid=(GridView)findViewById(R.id.GridViewUsers);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {


                    //if(web[position].equals("Search")) {
                    if(position==0){
                    Intent i = new Intent(UsersListActivity.this, BookListActivity.class);

                        startActivity(i);
                        //finish();
                    }
                if(position==1){
                    Intent i = new Intent(UsersListActivity.this, Profile.class);
                    i.putExtra("EMAIL",textViewName.getText().toString());
                    startActivity(i);
                    //finish();
                }


                if(position==3){
                    Intent j = new Intent(UsersListActivity.this, AboutUs.class);

                    startActivity(j);
                    //finish();
                }
                if(position==4){
                    Intent j = new Intent(UsersListActivity.this, FeedbackActivity.class);

                    startActivity(j);
                    //finish();
                }

                Toast.makeText(UsersListActivity.this, "You Clicked at " +web[+ position], Toast.LENGTH_SHORT).show();

            }
        });

    }

}
