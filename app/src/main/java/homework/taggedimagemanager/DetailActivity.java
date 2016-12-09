package homework.taggedimagemanager;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

import homework.taggedimagemanager.model.AbstractTag;
import homework.taggedimagemanager.model.DatabaseManager;
import homework.taggedimagemanager.model.Image;

public class DetailActivity extends AppCompatActivity {
    private boolean newImage;
    private final int IMAGE_CHOOSE_CODE = 1;

    private int resultCode;
    private ImageSwitchViewer imageSwitchViewer;
    private ListView tagList;
    private EditText descriptionText;

    private DatabaseManager db;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.w("onActivityResult", String.valueOf(requestCode) + " " + String.valueOf(resultCode));
        this.resultCode = resultCode;
        switch (requestCode) {
            case IMAGE_CHOOSE_CODE:
                if (resultCode == RESULT_OK) {
                    Log.w("onActivityResult", String.valueOf(imageSwitchViewer));
                    Log.w("onActivityResult", String.valueOf(data.getData()));
                    imageSwitchViewer.setImageURI(data.getData());
                }
                break;
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        imageSwitchViewer = (ImageSwitchViewer)findViewById(R.id.imageSwitcher);
        tagList = (ListView)findViewById(R.id.tagList);
        descriptionText = (EditText)findViewById(R.id.descriptionText);

        db = DatabaseManager.getInstance();

        Intent arguments = this.getIntent();
        newImage = arguments.getBooleanExtra("newImage", false);
        newImage = false;
        if (!newImage) {
            List<Image> images = (List<Image>) arguments.getSerializableExtra("images");

            ArrayList<Uri> uris = new ArrayList<Uri>();

            for (Image image : images) {
                uris.add(image.getUri());
            }
            imageSwitchViewer.setUris(uris);
            imageSwitchViewer.setCurrentIndex((arguments.getIntExtra("currentIndex", uris.size() / 2)));
        }
    }

    private void chooseImage() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        startActivityForResult(intent, IMAGE_CHOOSE_CODE);
    }

    private void detailImageView() {

    }

    public void imageSwitcherOnClick(View view) {
        if (newImage) {
            chooseImage();
        } else {
            detailImageView();
        }
    }

    private void onChange(Uri uri) {
        Image image = db.getImageByUri(uri);
        descriptionText.setText(image.getDescription());
    }

    public void onSave(View view) {
        if (newImage) {
            Intent result = new Intent();
            if (resultCode == RESULT_OK) {
                Image newImage = db.insertImage(imageSwitchViewer.getCurrentUri(), descriptionText.getText().toString(), new ArrayList<AbstractTag>());
                result.putExtra("newImage", newImage);
            }
            setResult(resultCode, result);
            this.finish();
        } else {
            Image currentImage = db.getImageByUri(imageSwitchViewer.getCurrentUri());
            String description = descriptionText.getText().toString();
            if (!description.equals(currentImage.getDescription())) {
                db.updateImageDescription(currentImage.getId(), description);
            }
        }
    }
}
