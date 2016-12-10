package homework.taggedimagemanager;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.CursorIndexOutOfBoundsException;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import homework.taggedimagemanager.model.*;

import java.security.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by Lawrence on 10/12/2016.
 *
 */
public class ImageDatabaseManager implements DBManager {
    private static ImageDatabaseManager singleton;
    private SQLiteDatabase db;
    private ImageDBHelper dbHelper;
    private ImageDatabaseManager(ImageDBHelper dbHelper) {
        // TODO: Constructor.
        this.dbHelper = dbHelper;
        this.db = dbHelper.getReadableDatabase();
    }
    public List<Image> searchImage(String keyword) {
        Cursor cursor;
        if (keyword.replace(" ", "").equals("")) {
            cursor = db.query("Image",
                    null,
                    null,
                    null,
                    null,
                    null,
                    null);
        } else {
            cursor = db.rawQuery("WITH RECURSIVE descend_of_target(id) as (\n" +
                    "SELECT Tag.id FROM Tag WHERE title LIKE '%" + keyword + "%'\n" +
                    "UNION ALL\n" +
                    "SELECT Tag.id FROM Tag JOIN descend_of_target ON Tag.parentId = descend_of_target.id \n" +
                    ")\n" +
                    "SELECT * FROM Image WHERE description LIKE '%" + keyword + "%'\n" +
                    "union\n" +
                    "SELECT Image.* FROM Image join Belong on Image.id = Belong.imageId\n" +
                    "WHERE tagId IN descend_of_target;", null);
        }


        List<Image> images = new ArrayList<>();
        while(cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex("id");
            int uriIndex = cursor.getColumnIndex("uri");
            int descriptionIndex = cursor.getColumnIndex("createTime");
            int createTimeIndex = cursor.getColumnIndex("description");
            int id = cursor.getInt(idIndex);
            String uri = cursor.getString(uriIndex);
            Date createTime = new Date(cursor.getLong(createTimeIndex)); //FIXME: Unknown return type.
            String description = cursor.getString(descriptionIndex);
            images.add(new Image(id, Uri.parse(uri), description, createTime, getImageTags(id)));
        }
        cursor.close(); // Free resource.
        return images;
    }
    public List<AbstractTag> searchTag(String keyword) {
        Cursor cursor = db.query(
                "Tag",
                null,
                keyword.equals("") ? "parentId IS NULL": "title like '%" + keyword + "%'",
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
            int parentId = cursor.getInt(parentIdIndex);
            tags.add(new AbstractTag(id, title));
        }
        cursor.close();
        return tags;
    }

    public AbstractTag getTagById(int tagId) {
        Cursor cursor  = db.query(
                "Tag",
                null,
                "id = " + tagId,
                null,
                null,
                null,
                null
        );

        while (cursor.moveToNext()) {
            return new AbstractTag(cursor.getInt(cursor.getColumnIndex("id")),
                    cursor.getString(cursor.getColumnIndex("title")));
        }
        return null;
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

        ArrayList<AbstractTag> tags = new ArrayList<>();
        Cursor cursor = db.query(
                "Tag",
                null,
                "parentId = " + abstractTag.getId(),
                null,
                null,
                null,
                null
        );
        while(cursor.moveToNext()) {
            int titleIndex = cursor.getColumnIndex("title");
            int idIndex = cursor.getColumnIndex("id");
            int id = cursor.getInt(idIndex);
            String title = cursor.getString(titleIndex);
            tags.add(new AbstractTag(id, title));
        }

        cursor.close();
        return new Tag(abstractTag.getId(), abstractTag.getTitle(), tags);
    }

    public ArrayList<AbstractTag> getImageTags(int imageId) {
        Cursor cursor = db.query(
                "Belong",
                null,
                "imageId = " + imageId,
                null,
                null,
                null,
                null
        );

        ArrayList<AbstractTag> result = new ArrayList<AbstractTag>();
        while (cursor.moveToNext()) {
            int tagId = cursor.getInt(cursor.getColumnIndex("tagId"));
            result.add(getTagById(tagId));
        }
        return result;
    }

    public Image getImageByUri(Uri targetUri) {
        Cursor cursor = db.query(
                "Image",
                null,
                "uri = '" + targetUri.toString() + "'",
                null,
                null,
                null,
                null
        );
        while (cursor.moveToNext()) {
            int idIndex = cursor.getColumnIndex("id");
            int uriIndex = cursor.getColumnIndex("uri");
            int descriptionIndex = cursor.getColumnIndex("description");
            int timeIndex = cursor.getColumnIndex("createTime");
            int id = cursor.getInt(idIndex);
            Date time = new Date(cursor.getLong(timeIndex));
            Uri uri = Uri.parse(cursor.getString(uriIndex));
            String description = cursor.getString(descriptionIndex);
            return new Image(id, uri, description, time, getImageTags(id));
        }
        return null;
    }

    public Image getImageById(int targetId) {
        Cursor cursor = db.query(
                "Image",
                null,
                "id = " + targetId,
                null,
                null,
                null,
                null
        );
        if (cursor.moveToNext()) {
            int imageId = cursor.getInt(cursor.getColumnIndex("id"));
            return new Image(imageId,
                    Uri.parse(cursor.getString(cursor.getColumnIndex("uri"))),
                    cursor.getString(cursor.getColumnIndex("description")),
                    null,
                    getImageTags(imageId)
                    );
        } else {
            return null;
        }
    }

    public AbstractTag insertTag(AbstractTag parent, String title) {
        ContentValues values = new ContentValues();
        values.put("title", title);
        values.put("parentId", parent != null? parent.getId() : null);
        long id = db.insert(
                "Tag",
                null,
                values
        );
        return new AbstractTag(Long.valueOf(id).intValue(), title);
    }
    public void updateTagTitle(int targetId, String title) {
        String sql = "UPDATE Tag SET title = '" + title + "' WHERE id = " + targetId + ";";
        db.execSQL(sql);
    }
    public void deleteTag(int targetId) {
        String sql = "DELETE FROM Tag WHERE id = " + targetId + ";";
        db.execSQL(sql);
    }

    public Image insertImage(Uri uri, String description, List<AbstractTag> tags) {
        ContentValues values = new ContentValues();
        values.put("uri", uri.toString());
        values.put("description", description);
        long id = db.insert(
                "Image",
                null,
                values
        );
        long time = 0;
        Cursor cursor = db.query(
                "Image",
                null,
                "id = " + id,
                null,
                null,
                null,
                null
        );
        while(cursor.moveToNext()) {
            int timeIndex = cursor.getColumnIndex("createTime");
            time = cursor.getLong(timeIndex);
        }
        for(AbstractTag tag : tags) {
            String sql = "INSERT INTO Belong (imageId, tagId) VALUES (" + id + ", " + tag.getId() + ");";
            db.execSQL(sql);
        }
        cursor.close();
        return new Image(Long.valueOf(id).intValue(), uri, description, new Date(time));
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

        db.execSQL("DELETE FROM Belong WHERE imageId = " + targetId + ";");

        for(AbstractTag tag : tags) {
            String sql = sqlStart + targetId + ", " + tag.getId() + sqlEnd;
            db.execSQL(sql);
        }
    }
    public void deleteImage(int targetId) {
        String sql = "DELETE FROM Image WHERE id = " + targetId + ";";
        db.execSQL(sql);
    }

    public static DBManager getInstance() {
        return singleton;
    }

    public static void initialize(ImageDBHelper dbHelper) {
        singleton = new ImageDatabaseManager(dbHelper);
    }
}
