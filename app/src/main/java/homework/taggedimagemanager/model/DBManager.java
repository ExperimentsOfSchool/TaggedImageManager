package homework.taggedimagemanager.model;

import android.net.Uri;

import java.util.List;

/**
 * Created by rtnelo on 16-12-6.
 */

public interface DBManager {
    List<Image> searchImage(String keyword);

    Image getImageById(int targetId);

    Image getImageByUri(Uri targetUri);

    List<AbstractTag> searchTag(String keyword);

    Tag getFullTag(AbstractTag abstractTag);

    AbstractTag insertTag(AbstractTag parent, String title);

    void updateTagTitle(int targetId, String title);

    void deleteTag(int targetId);

    Image insertImage(Uri uri, String description, List<AbstractTag> tags);

    void updateImageUri(int targetId, Uri uri);

    void updateImageDescription(int targetId, String description);

    void updateImageTags(int targetId, List<AbstractTag> tags);

    void deleteImage(int targetId);
}
