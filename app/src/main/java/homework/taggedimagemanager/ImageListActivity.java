package homework.taggedimagemanager;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.io.Serializable;

import homework.taggedimagemanager.model.DatabaseManager;
import homework.taggedimagemanager.model.Image;

public class ImageListActivity extends AppCompatActivity {
    private ImageListView imageList;

    private int START_DETAIL_ACTIVITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        imageList = (ImageListView)findViewById(R.id.imageList);
        imageList.setEventHandler(new ImageListView.EventHandler() {
            @Override
            public void onSelect(Image image, long position) {
                startDetailActivity(image, position);
            }
        });
        imageList.setImages(DatabaseManager.getInstance().searchImage(""));
    }

    private void startDetailActivity(Image image, long position) {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("images", (Serializable) imageList.getImages());
        intent.putExtra("currentIndex", position);
        startActivity(intent);
    }

    private void startNewImageActivity() {
        Intent intent = new Intent(this, DetailActivity.class);
        intent.putExtra("newImage", true);
        startActivityForResult(intent, START_DETAIL_ACTIVITY);
    }

    @Override
    public void on();
}
