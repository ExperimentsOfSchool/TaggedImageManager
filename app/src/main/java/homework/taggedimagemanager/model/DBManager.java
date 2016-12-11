package homework.taggedimagemanager.model;

import android.net.Uri;

import java.util.List;

/**
 * Created by rtnelo on 16-12-6.
 */

public interface DBManager {
    List<Image> searchImage(String keyword); // 按关键字查找已经添加的图片

    Image getImageById(int targetId); // 按ID查找已经添加的图片

    Image getImageByUri(Uri targetUri); // 按URI查找已经添加的图片

    List<AbstractTag> searchTag(String keyword); // 按关键字查找图片标签

    List<AbstractTag> getImageTags(int imageId); // 按图片ID查找图片标签

    Tag getFullTag(AbstractTag abstractTag); // 查询完整的父标签

    AbstractTag insertTag(AbstractTag parent, String title); // 插入新的标签

    void updateTagTitle(int targetId, String title); // 更新标签标题

    void deleteTag(int targetId); // 删除标签

    Image insertImage(Uri uri, String description, List<AbstractTag> tags); // 插入图片

    void updateImageUri(int targetId, Uri uri); // 更新图片的URI

    void updateImageDescription(int targetId, String description); // 更新图片描述

    void updateImageTags(int targetId, List<AbstractTag> tags); // 更新图片标签

    void deleteImage(int targetId); // 删除图片
}
