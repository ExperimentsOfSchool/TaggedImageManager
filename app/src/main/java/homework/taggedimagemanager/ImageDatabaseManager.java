package homework.taggedimagemanager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import homework.taggedimagemanager.model.*;

import java.util.List;

/**
 * Created by Lawrence on 10/12/2016.
 */
public class ImageDatabaseManager extends DBManager {
    private static ImageDatabaseManager singleton;
    private SQLiteDatabase db;
    private ImageDBHelper dbHelper;
    private ImageDatabaseManager(ImageDBHelper dbHelper) {
        // TODO: Constructor.
        this.dbHelper = dbHelper;
        this.db = dbHelper.getReadableDatabase();
    }
    public List<Image> searchImage(String keyword) {
        Cursor cursor = db.query("Image",
                null,
                "description like %" + keyword + "%",
                null,
                null,
                null,
                null
                );
    }
    public List<AbstractTag> searchTag(String keyword) {
        Cursor cursor = db.query("Tag",
                null,
                "title like %" + keyword + "%",
                null,
                null,
                null,
                null
        );

    }

    public Tag getFullTag(AbstractTag abstractTag) {
    }


    public int insertTag(AbstractTag parent, AbstractTag newTag) {}
    public void updateTagTitle(int targetId, String title) {}
    public void deleteTag(int targetId) {}

    public int insertImage(Image image) {}
    public void updateImageUri(int targetId, Uri uri) {}
    public void updateImageDescription(int targetId, String description) {}
    public void updateImageTags(int targetId, List<AbstractTag> tags) {}
    public void deleteImage(int targetId) {}

    public DBManager getInstance(ImageDBHelper dbHelper) {
        if(singleton == null) {
            singleton = new ImageDatabaseManager(dbHelper);
        } else {
            this.dbHelper = dbHelper;
            this.db = dbHelper.getReadableDatabase();
        }
        return singleton;
    }
}
