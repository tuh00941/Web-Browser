package edu.temple.webbrowser;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import static android.content.Context.MODE_PRIVATE;

public class BookmarkListAdapter extends BaseAdapter {

    final Context context;
    public ArrayList<String> titles;
    public ArrayList<String> urls;

    public BookmarkListAdapter(Context context, ArrayList<String> titles, ArrayList<String> urls) {
        this.context = context;
        this.titles = titles;
        this.urls = urls;
    }

    @Override
    public int getCount() {
        return urls.size();
    }

    @Override
    public Object getItem(int position) {
        return urls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.bookmark, null);

        TextView textView = view.findViewById(R.id.title);
        textView.setText(titles.get(position));

        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.putExtra("position", position + 1);
                ((BookmarksActivity) context).setResult(Activity.RESULT_OK, intent);

                ((BookmarksActivity) context).finish();
            }
        });

        ImageButton delete = view.findViewById(R.id.delete);

        delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);

                //  Set alert dialog title
                alertDialogBuilder.setTitle("Delete Bookmark");

                //  Set alert dialog message
                alertDialogBuilder
                        .setMessage(titles.get(position) + "\n\nAre you sure you want to delete this bookmark?")
                        .setCancelable(false)
                        .setPositiveButton("Delete", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                SharedPreferences sharedPreferences = ((BookmarksActivity) context).getSharedPreferences("bookmarks", MODE_PRIVATE);
                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                int bookmarkCount = sharedPreferences.getInt("bookmarkCount", 0);

                                for(int i = position + 1; i <= bookmarkCount; i++) {
                                    editor.putString("title" + i, sharedPreferences.getString("title" + (i + 1), null)).commit();
                                    editor.putString("url" + i, sharedPreferences.getString("url" + (i + 1), null)).commit();
                                }

                                bookmarkCount--;
                                editor.putInt("bookmarkCount", bookmarkCount).commit();

                                urls.remove(position);
                                titles.remove(position);
                                notifyDataSetChanged();

                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }
                        });

                //  Create dialog from Builder class
                AlertDialog alertDialog = alertDialogBuilder.create();

                //  Display the dialog
                alertDialog.show();
            }
        });

        return view;
    }
}
