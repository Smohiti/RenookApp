package com.androidtutorialshub.loginregister.activities;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceActivity;
import android.support.v4.app.NotificationCompat.Action;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.NotificationCompat;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.androidtutorialshub.loginregister.R;
import com.androidtutorialshub.loginregister.model.Book;
import com.androidtutorialshub.loginregister.model.BookClient;
import com.androidtutorialshub.loginregister.sql.ProfileDbHelper;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by smohi on 8/11/2017.
 */
public class BookDetailActivity extends ActionBarActivity {

    private ImageView ivBookCover;
    private TextView tvTitle;
    private TextView tvAuthor;
    private TextView tvPublisher;
    private TextView tvPageCount;
    private BookClient client;
    ProfileDbHelper myDB;
    TextView editText;


    private static final int NOTIFY_ID = 100;
    private static final String YES_ACTION = "com.tinbytes.simplenotificationapp.YES_ACTION";
    private static final String MAYBE_ACTION = "com.tinbytes.simplenotificationapp.MAYBE_ACTION";
    private static final String NO_ACTION = "com.tinbytes.simplenotificationapp.NO_ACTION";

    private NotificationManager notificationManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_detail);
        // Fetch views
        ivBookCover = (ImageView) findViewById(R.id.ivBookCover);
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        tvAuthor = (TextView) findViewById(R.id.tvAuthor);
        tvPublisher = (TextView) findViewById(R.id.tvPublisher);
        tvPageCount = (TextView) findViewById(R.id.tvPageCount);
        // Use the book to populate the data into our views
        Book book = (Book) getIntent().getSerializableExtra(BookListActivity.BOOK_DETAIL_KEY);
        loadBook(book);

        myDB = new ProfileDbHelper(this);
        //set a button handler for a3_button
        Button a3_button = (Button) findViewById(R.id.button2);
        //set an onclickhandler for the button
        a3_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title = tvTitle.getText().toString();
                if (tvTitle.length() != 0) {

                    if (myDB.ifExists(title)) {

                        Toast.makeText(BookDetailActivity.this, "Already Rented", Toast.LENGTH_SHORT).show();

                    } else {

                        AddData(title);
                        tvTitle.setText("");
                        showActionButtonsNotification();
                    }

                } else {
                    Toast.makeText(BookDetailActivity.this, "The Book Should Have Title!", Toast.LENGTH_LONG).show();
                }
                // button2_click_handler();
                Intent intent = new Intent(BookDetailActivity.this, Profile.class);
                intent.putExtra("title", title);

                //display a toast for debugging - show the user...
                //A toast is a popup window that canâ€™t be interacted with, and disappears after a short while
                //Toast.makeText(this, "You Successfully Rented This Book", Toast.LENGTH_LONG).show();
                startActivity(intent);
            }
        });

        notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        processIntentAction(getIntent());
        getSupportActionBar().hide();
    }


    private Intent getNotificationIntent() {
        Intent intent = new Intent(this, BookDetailActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        return intent;
    }

    private void showActionButtonsNotification() {
        Intent yesIntent = getNotificationIntent();
        yesIntent.setAction(YES_ACTION);

       // Intent maybeIntent = getNotificationIntent();
       // maybeIntent.setAction(MAYBE_ACTION);

        Intent noIntent = getNotificationIntent();
        noIntent.setAction(NO_ACTION);

        Notification notification = new NotificationCompat.Builder(this)
                .setContentIntent(PendingIntent.getActivity(this, 0, getNotificationIntent(), PendingIntent.FLAG_UPDATE_CURRENT))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setTicker("Action Buttons Notification Received")
                .setContentTitle("Hi there!")
                .setContentText("This is even more text.")
                .setWhen(System.currentTimeMillis())
                .setAutoCancel(true)
                .addAction(new Action(
                        R.mipmap.ic_thumb_up_black_36dp,
                        getString(R.string.Gotit),
                        PendingIntent.getActivity(this, 0, yesIntent, PendingIntent.FLAG_UPDATE_CURRENT)))
/*                .addAction(new Action(
                        R.mipmap.ic_thumbs_up_down_black_36dp,
                        getString(R.string.maybe),
                        PendingIntent.getActivity(this, 0, maybeIntent, PendingIntent.FLAG_UPDATE_CURRENT)))*/
                .addAction(new Action(
                        R.mipmap.ic_thumb_down_black_36dp,
                        getString(R.string.Dismiss),
                        PendingIntent.getActivity(this, 0, noIntent, PendingIntent.FLAG_UPDATE_CURRENT)))
                .build();

        notificationManager.notify(NOTIFY_ID, notification);
    }

    @Override
    protected void onNewIntent(Intent intent) {
        processIntentAction(intent);
        super.onNewIntent(intent);
    }

    private void processIntentAction(Intent intent) {
        if (intent.getAction() != null) {
            switch (intent.getAction()) {
                case YES_ACTION:
                    Toast.makeText(this, "Got it :)", Toast.LENGTH_SHORT).show();
                    break;
/*                case MAYBE_ACTION:
                    Toast.makeText(this, "Maybe :|", Toast.LENGTH_SHORT).show();
                    break;*/
                case NO_ACTION:
                    Toast.makeText(this, "Dismiss :(", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    }


    public void AddData(String newEntry) {

        boolean insertData = myDB.addData(newEntry);

        if (insertData == true) {
            Toast.makeText(this, "You Successfully Rented This Book", Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Something went wrong :(.", Toast.LENGTH_LONG).show();
        }
    }

    // Populate data for the book
    private void loadBook(Book book) {
        //change activity title
        this.setTitle(book.getTitle());
        // Populate data
        Picasso.with(this).load(Uri.parse(book.getLargeCoverUrl())).error(R.drawable.ic_nocover).into(ivBookCover);
        tvTitle.setText(book.getTitle());
        tvAuthor.setText(book.getAuthor());
        // fetch extra book data from books API
        client = new BookClient();
        client.getExtraBookDetails(book.getOpenLibraryId(), new JsonHttpResponseHandler() {

            public void onSuccess(int statusCode, PreferenceActivity.Header[] headers, JSONObject response) {
                try {
                    if (response.has("publishers")) {
                        // display comma separated list of publishers
                        final JSONArray publisher = response.getJSONArray("publishers");
                        final int numPublishers = publisher.length();
                        final String[] publishers = new String[numPublishers];
                        for (int i = 0; i < numPublishers; ++i) {
                            publishers[i] = publisher.getString(i);
                        }
                        tvPublisher.setText(TextUtils.join(", ", publishers));
                    }
                    if (response.has("number_of_pages")) {
                        tvPageCount.setText(Integer.toString(response.getInt("number_of_pages")) + " pages");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_book_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_share) {
            setShareIntent();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setShareIntent() {
        ImageView ivImage = (ImageView) findViewById(R.id.ivBookCover);
        final TextView tvTitle = (TextView) findViewById(R.id.tvTitle);
        // Get access to the URI for the bitmap
        Uri bmpUri = getLocalBitmapUri(ivImage);
        // Construct a ShareIntent with link to image
        Intent shareIntent = new Intent();
        // Construct a ShareIntent with link to image
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("*/*");
        shareIntent.putExtra(Intent.EXTRA_TEXT, (String) tvTitle.getText());
        shareIntent.putExtra(Intent.EXTRA_STREAM, bmpUri);
        // Launch share menu
        startActivity(Intent.createChooser(shareIntent, "Share Image"));

    }

    // Returns the URI path to the Bitmap displayed in cover imageview
    public Uri getLocalBitmapUri(ImageView imageView) {
        // Extract Bitmap from ImageView drawable
        Drawable drawable = imageView.getDrawable();
        Bitmap bmp = null;
        if (drawable instanceof BitmapDrawable) {
            bmp = ((BitmapDrawable) imageView.getDrawable()).getBitmap();
        } else {
            return null;
        }
        // Store image to default external storage directory
        Uri bmpUri = null;
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(
                    Environment.DIRECTORY_DOWNLOADS), "share_image_" + System.currentTimeMillis() + ".png");
            file.getParentFile().mkdirs();
            FileOutputStream out = new FileOutputStream(file);
            bmp.compress(Bitmap.CompressFormat.PNG, 90, out);
            out.close();
            bmpUri = Uri.fromFile(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bmpUri;
    }
}
