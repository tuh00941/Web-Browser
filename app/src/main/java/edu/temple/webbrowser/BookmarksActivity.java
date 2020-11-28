package edu.temple.webbrowser;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import java.util.ArrayList;

public class BookmarksActivity extends AppCompatActivity {

    public ListView bookmarkList;
    public Button closeButton;

    public ArrayList<String> titles;
    public ArrayList<String> urls;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookmarks);

        bookmarkList = findViewById(R.id.bookmarkList);
        closeButton = findViewById(R.id.closeButton);

        titles = new ArrayList<>();
        urls = new ArrayList<>();

        SharedPreferences sharedPreferences = getSharedPreferences("bookmarks", MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

        int bookmarkCount = sharedPreferences.getInt("bookmarkCount", 0);

        for(int i = 1; i <= bookmarkCount; i++) {
            titles.add(sharedPreferences.getString("title" + i, null));
            urls.add(sharedPreferences.getString("url" + i, null));
        }

        /*titles.add("help");
        urls.add("me");*/

        BookmarkListAdapter adapter = new BookmarkListAdapter(BookmarksActivity.this, titles, urls);
        bookmarkList.setAdapter(adapter);

        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}