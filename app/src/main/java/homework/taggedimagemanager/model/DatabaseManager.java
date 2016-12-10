package homework.taggedimagemanager.model;

import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by rtnelo on 16-12-6.
 */

public class DatabaseManager implements DBManager {
    private static DatabaseManager db;

    private ArrayList<Image> images;
    private ArrayList<AbstractTag> tags;

    private DatabaseManager() {
        images = new ArrayList<Image>();
        images.add(new Image(1, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3324634.gif"), "1"));
        images.add(new Image(3, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3336661.gif"), "3"));
        images.add(new Image(4, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3335544.gif"), "4"));
        images.add(new Image(6, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3326557.gif"), "6"));
        images.add(new Image(3, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3336661.gif"), "3"));
        images.add(new Image(4, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3335544.gif"), "4"));
        images.add(new Image(6, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3326557.gif"), "6"));
        images.add(new Image(1, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3324634.gif"), "1"));
        images.add(new Image(3, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3336661.gif"), "3"));
        images.add(new Image(4, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3335544.gif"), "4"));
        images.add(new Image(6, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3326557.gif"), "6"));
        images.add(new Image(1, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3324634.gif"), "1"));
        images.add(new Image(3, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3336661.gif"), "3"));
        images.add(new Image(4, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3335544.gif"), "4"));
        images.add(new Image(6, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3326557.gif"), "6"));
        images.add(new Image(1, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3324634.gif"), "1"));
        images.add(new Image(3, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3336661.gif"), "3"));
        images.add(new Image(4, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3335544.gif"), "4"));
        images.add(new Image(6, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3326557.gif"), "6"));
        images.add(new Image(1, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3324634.gif"), "1"));
        images.add(new Image(3, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3336661.gif"), "3"));
        images.add(new Image(4, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3335544.gif"), "4"));
        images.add(new Image(6, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3326557.gif"), "6"));
        images.add(new Image(1, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3324634.gif"), "1"));
        images.add(new Image(3, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3336661.gif"), "3"));
        images.add(new Image(4, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3335544.gif"), "4"));
        images.add(new Image(6, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3326557.gif"), "6"));
        images.add(new Image(1, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3324634.gif"), "1"));
        images.add(new Image(3, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3336661.gif"), "3"));
        images.add(new Image(4, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3335544.gif"), "4"));
        images.add(new Image(6, Uri.parse("file:///storage/sdcard1/Wowtu/Download/3326557.gif"), "6"));
    }

    public List<Image> searchImage(String keyword) {
        Log.i("DatabaseManager", "Search: " + keyword);
        if (keyword.length() > 4) {
            return images.subList(0, images.size() / 2);
        } else {
            return images;
        }
    }

    public Image getImageById(int targetId) {
        for (Image image : images) {
            if (image.getId() == targetId) {
                Log.i("DatabaseManager", "getImageById: " + String.valueOf(targetId));
                return image;
            }
        }
        Log.w("DatabaseManager", "getImageById: " + String.valueOf(targetId) + " failed");
        return null;
    }
    public Image getImageByUri(Uri targetUri) {
        for (Image image : images) {
            if (image.getUri().equals(targetUri)) {
                Log.i("DatabaseManager", "getImageByUri: " + targetUri.toString());
                return image;
            }
        }
        Log.w("DatabaseManager", "getImageByUri: " + targetUri.toString() + " failed");
        return null;
    }

    public List<AbstractTag> searchTag(String keyword) {
        Log.i("DatabaseManager", "searchTag: " + keyword);
        return new ArrayList<AbstractTag>();
    }

    public Tag getFullTag(AbstractTag abstractTag) {
        Log.i("DatabaseManager", "getFullTag: " + abstractTag.toString());
        return new Tag(1, "Tag1", new ArrayList<AbstractTag>());
    }


    public AbstractTag insertTag(AbstractTag parent, String title) {
        Log.i("DatabaseManager", "insertTag: " + title + " into " + parent.toString());
        return new AbstractTag(tags.size(), title);
    }

    public void updateTagTitle(int targetId, String title) {
        Log.i("DatabaseManager", "updateTag: " + String.valueOf(targetId) + " " + title);
        return;
    }

    public void deleteTag(int targetId) {
        Log.i("DatabaseManager", "deleteTag: " + String.valueOf(targetId));
        return;
    }

    public Image insertImage(Uri uri, String description, List<AbstractTag> tags) {
        Log.w("DatabaseManager", "insertImage new");
        Image image = new Image(images.size(), uri, description, new Date(), new ArrayList<AbstractTag>());
        Log.w("DatabaseManager", "insertImage: " + image.toString());
        images.add(image);
        return image;
    }

    public void updateImageUri(int targetId, Uri uri) {
        Log.i("DatabaseManager", "updateImageUri: " + String.valueOf(targetId) + " " + uri.toString());
        return;
    }

    public void updateImageDescription(int targetId, String description) {
        Log.i("DatabaseManager", "updateImageDescription: " + String.valueOf(targetId) + " " + description);
        return;
    }

    public void updateImageTags(int targetId, List<AbstractTag> tags) {
        Log.i("DatabaseManager", "updateImageTags: " + String.valueOf(targetId) + " " + tags.toString());
        return;
    }

    public void deleteImage(int targetId) {
        Log.i("DatabaseManager", "deleteImage: " + String.valueOf(targetId));
        return;
    }

    public static DatabaseManager getInstance() {
        if (db == null) {
            db = new DatabaseManager();
        }
        return db;
    }
}
