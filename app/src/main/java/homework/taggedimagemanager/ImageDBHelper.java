package homework.taggedimagemanager;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Lawrence on 09/12/2016.
 *
 */
public class ImageDBHelper extends SQLiteOpenHelper {
    private static final int DB_VERSION = 1;
    private static final String DB_NAME = "images.db";
    private static String IMAGE_TABLE_NAME = "Tag";
    private static String TAG_TABLE_NAME = "Image";
    private static String BELONG_TABLE_NAME = "Belong";

    public ImageDBHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTagTable = "CREATE TABLE IF NOT EXISTS Tag\n" +
                "(\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    title VARCHAR(45) NOT NULL,\n" +
                "    parentId INT,\n" +
                "    CONSTRAINT table_name_Image_id_fk FOREIGN KEY (parentId) REFERENCES Tag (id) ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ");";
        String createImageTable = "CREATE TABLE IF NOT EXISTS Image\n" +
                "(\n" +
                "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n" +
                "    uri VARCHAR(45) NOT NULL,\n" +
                "    description VARCHAR(45) NULL,\n" +
                "    createTime TIMESTAMP DEFAULT (datetime('now','localtime')) NOT NULL\n" +
                ");";
        String createBelongTable = "CREATE TABLE IF NOT EXISTS Belong\n" +
                "(\n" +
                "    imageId INTEGER,\n" +
                "    tagId INTEGER,\n" +
                "    CONSTRAINT table_name_Image_id_fk FOREIGN KEY (imageId) REFERENCES Image (id) ON DELETE CASCADE ON UPDATE CASCADE,\n" +
                "    CONSTRAINT table_name_Tag_id_fk FOREIGN KEY (tagId) REFERENCES Tag (id) ON DELETE CASCADE ON UPDATE CASCADE\n" +
                ");";
        db.execSQL(createImageTable);
        db.execSQL(createTagTable);
        db.execSQL(createBelongTable);

    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropImage = "DROP TABLE IF EXISTS " + IMAGE_TABLE_NAME;
        String dropTag = "DROP TABLE IF EXISTS " + TAG_TABLE_NAME;
        String dropBelong = "DROP TABLE IF EXISTS " + BELONG_TABLE_NAME;
        db.execSQL(dropBelong);
        db.execSQL(dropImage);
        db.execSQL(dropTag);
        onCreate(db);
    }

    @Override
    public void onOpen(SQLiteDatabase db) {
        db.execSQL("PRAGMA foreign_keys = ON;");
    }
}
