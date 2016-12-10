package homework.taggedimagemanager;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
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
    private ImageDBHelper dbHelper;
    private int START_DETAIL_ACTIVITY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        dbHelper = new ImageDBHelper(getApplicationContext());

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startNewImageActivity();
            }
        });

        imageList = (ImageListView)findViewById(R.id.imageList);
        imageList.setEventHandler(new ImageListView.EventHandler() {
            @Override
            public void onSelect(Image image, int position) {
                startDetailActivity(image, position);
            }
        });
        imageList.setImages(DatabaseManager.getInstance().searchImage(""));

        ImageSearcher search = (ImageSearcher)findViewById(R.id.search);
        search.setOnQueryTextListener(new ImageSearcher.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String s) {
                ImageListActivity.this.searchImage(s);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    private void searchImage(String keywords) {
        imageList.setImages(DatabaseManager.getInstance().searchImage(keywords));
    }

    private void startDetailActivity(Image image, int position) {
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == START_DETAIL_ACTIVITY) {
            if (resultCode == RESULT_OK) {
                Image newImage = (Image)data.getSerializableExtra("newImage");
                Log.w("OnActivityResult", "UnSerializableFinished" + newImage.getTags().size());
                this.imageList.addImage(newImage);
            }
        }
    }

}
