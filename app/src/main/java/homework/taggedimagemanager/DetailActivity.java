package homework.taggedimagemanager;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import homework.taggedimagemanager.model.AbstractTag;
import homework.taggedimagemanager.model.DBManager;
import homework.taggedimagemanager.model.Image;

public class DetailActivity extends AppCompatActivity {
    private boolean newImage;
    private final int IMAGE_CHOOSE_CODE = 1;
    private final int TAG_CHOOSE_CODE = 2;

    private int resultCode;
    private ImageSwitchViewer imageSwitchViewer;
    private EditText descriptionText;
    private TagHorizontalListView tagList;

    private DBManager db;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.w("onActivityResult", String.valueOf(requestCode) + " " + String.valueOf(resultCode));
        if (this.resultCode != RESULT_OK) {
            this.resultCode = resultCode;
        }
        switch (requestCode) {
            case IMAGE_CHOOSE_CODE:
                if (resultCode == RESULT_OK) {
                    imageSwitchViewer.setImageURI(data.getData());
                }
                break;
            case TAG_CHOOSE_CODE:
                if (resultCode == RESULT_OK) {
                    addTag((AbstractTag)data.getSerializableExtra("tag"));
                }
        }
    }

    private void addTag(AbstractTag tag) {
        tagList.addTag(tag);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        resultCode = RESULT_CANCELED;
        imageSwitchViewer = (ImageSwitchViewer)findViewById(R.id.imageSwitcher);
        descriptionText = (EditText)findViewById(R.id.descriptionText);
        tagList = (TagHorizontalListView)findViewById(R.id.tagList);

        db = ImageDatabaseManager.getInstance();

        resultCode = RESULT_CANCELED;

        Intent arguments = this.getIntent();

        imageSwitchViewer.setOnChange(new ImageSwitchViewer.OnChangeCallback() {
            @Override
            public void callback(Uri uri) {
                onChange(uri);
            }
        });

        newImage = arguments.getBooleanExtra("newImage", false);
        if (!newImage) {
            List<Image> images = (List<Image>) arguments.getSerializableExtra("images");

            ArrayList<Uri> uris = new ArrayList<Uri>();

            for (Image image : images) {
                uris.add(image.getUri());
            }
            imageSwitchViewer.setUris(uris);
            imageSwitchViewer.setCurrentIndex((arguments.getIntExtra("currentIndex", uris.size() / 2)));
        } else {
            chooseImage();
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
        tagList.setTags(image.getTags());
        Log.w("OnChange", "onChange" + image.getTags().toString());
    }

    public void onSave(View view) {
        if (newImage) {
            Intent result = new Intent();
            if (resultCode == RESULT_OK) {
                Log.w("DetailOnSave", "startInsert");
                Image newImage = db.insertImage(imageSwitchViewer.getCurrentUri(), descriptionText.getText().toString(), tagList.getTags());
                Log.w("DetailOnSave", "startSerialize");
                result.putExtra("newImage", newImage);
            }
            setResult(resultCode, result);
            this.finish();
            Log.w("DetailOnSave", "finish");
        } else {
            Image currentImage = db.getImageByUri(imageSwitchViewer.getCurrentUri());
            String description = descriptionText.getText().toString();
            List<AbstractTag> tags = tagList.getTags();

            if (!description.equals(currentImage.getDescription())) {
                db.updateImageDescription(currentImage.getId(), description);
            }
            if (!currentImage.getTags().equals(tags)) {
                db.updateImageTags(currentImage.getId(), tags);
            }
        }
    }

    public void addTagActivity() {
        Intent intent = new Intent(this, TagActivity.class);
        intent.putExtra("children", (Serializable)ImageDatabaseManager.getInstance().searchTag(""));
        startActivityForResult(intent, TAG_CHOOSE_CODE);
    }
}
