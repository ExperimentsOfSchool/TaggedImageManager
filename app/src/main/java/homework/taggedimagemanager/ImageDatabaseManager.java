package homework.taggedimagemanager;

import android.net.Uri;
import homework.taggedimagemanager.model.*;

import java.util.List;

/**
 * Created by Lawrence on 10/12/2016.
 */
public class ImageDatabaseManager extends DBManager {
    public List<Image> searchImage(String keyword) {}
    public List<AbstractTag> searchTag(String keyword) {}
    public Tag getFullTag(AbstractTag abstractTag) {}


    public int insertTag(AbstractTag parent, AbstractTag newTag) {}
    public void updateTagTitle(int targetId, String title) {}
    public void deleteTag(int targetId) {}

    public int insertImage(Image image) {}
    public void updateImageUri(int targetId, Uri uri) {}
    public void updateImageDescription(int targetId, String description) {}
    public void updateImageTags(int targetId, List<AbstractTag> tags) {}
    public void deleteImage(int targetId) {}

    public DBManager getInstance() {}
}
