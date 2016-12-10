package homework.taggedimagemanager;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import homework.taggedimagemanager.model.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lawrence on 10/12/2016.
 *
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
        List<Image> images = new ArrayList<>();
        while(cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex("id");
            int uriIndex = cursor.getColumnIndex("uri");
            int descriptionIndex = cursor.getColumnIndex("createTime");
            int createTimeIndex = cursor.getColumnIndex("description");
            int id = cursor.getInt(idIndex);
            String uri = cursor.getString(uriIndex);
            Date createTime = new Date(cursor.getInt(createTimeIndex)); //FIXME: Unknown return type.
            String description = cursor.getString(descriptionIndex);
            images.add(new Image(id, Uri.parse(uri), description, createTime));
        }
        cursor.close(); // Free resource.
        return images;
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
        List<AbstractTag> tags = new ArrayList<>();
        while(cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex("id");
            int titleIndex = cursor.getColumnIndex("title");
            int parentIdIndex = cursor.getColumnIndex("parentId");
            int id = cursor.getInt(idIndex);
            String title = cursor.getString(titleIndex);
//            int parentId = cursor.getInt(parentIdIndex); //FIXME: Which object will use it?
            tags.add(new AbstractTag(id, title));
        }
        cursor.close();
        return tags;
    }
    public Tag getFullTag(AbstractTag abstractTag) {

        //TODO: Find Tag Object and return it.
        /*
            1. find parent tagId
            2. find all children having this tagId.
            3. generate list<at>
            4. make tag
            5. return tag.
         */

        return null;
    }


    public int insertTag(AbstractTag parent, AbstractTag newTag) {
        String sql = "INSERT INTO Tag (id, title, parentId) VALUES (" + newTag.getId() + ", '" + newTag.getTitle() + "', " + parent.getId() + ");";
        db.execSQL(sql);
        return 0; // FIXME: Return what?
    }
    public void updateTagTitle(int targetId, String title) {
        String sql = "UPDATE Tag SET title = '" + title + "' WHERE id = " + targetId + ";";
        db.execSQL(sql);
    }
    public void deleteTag(int targetId) {
        String sql = "DELETE FROM Tag WHERE id = " + targetId + ";";
        db.execSQL(sql);
    }

    public int insertImage(Image image) {
        String sql = "INSERT INTO Image (uri, createTime, description) VALUES ('" + image.getUri().toString() + "', " + image.getCreateTime().getTime() + ", '" + image.getDescription() + "');";
        db.execSQL(sql);
        return 0; // FIXME: Return what?
    }
    public void updateImageUri(int targetId, Uri uri) {
        String sql = "UPDATE Image SET uri = '" + uri.toString() + "' WHERE id = " + targetId + ";";
        db.execSQL(sql);
    }
    public void updateImageDescription(int targetId, String description) {
        String sql = "UPDATE Image SET description = '" + description + "' WHERE id = " + targetId + ";";
        db.execSQL(sql);
    }
    public void updateImageTags(int targetId, List<AbstractTag> tags) {
        final String sqlStart = "INSERT INTO Belong (imageId, tagId) VALUES (";
        final String sqlEnd = ");";
        for(AbstractTag tag : tags) {
            String sql = sqlStart + targetId + ", " + tag.getId() + sqlEnd;
            db.execSQL(sql);
        }
    }
    public void deleteImage(int targetId) {
        String sql = "DELETE FROM Image WHERE id = " + targetId + ";";
        db.execSQL(sql);
    }

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
