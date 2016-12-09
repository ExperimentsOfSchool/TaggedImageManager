package homework.taggedimagemanager.model;

import android.net.Uri;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.Date;

/**
 * Created by rtnelo on 16-12-6.
 */

public class Image extends DataEntity {
    private int id;
    private Uri uri;
    private String description;
    private Date createTime;
    private ArrayList<AbstractTag> tags;

    public Image(int id, Uri uri, String description, Date createTime, ArrayList<AbstractTag> tags) {
        this.id = id;
        this.uri = uri;
        this.description = description;
        this.createTime = createTime;
        this.tags = tags;
    }

    public Image(int id, Uri uri, String description, Date createTime) {
        this(id, uri, description, createTime, new ArrayList<AbstractTag>());
    }

    public Image(int id, Uri uri, String description) {
        this(id, uri, description, new Date());
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ArrayList<AbstractTag> getTags() {
        return tags;
    }

    public void setTags(ArrayList<AbstractTag> tags) {
        this.tags = tags;
    }

    public void addTag(AbstractTag tag) {
        this.tags.add(tag);
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Image)) return false;

        Image image = (Image) o;

        if (getId() != image.getId()) return false;
        return getUri().equals(image.getUri());

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getUri().hashCode();
        return result;
    }

    private void writeObject(ObjectOutputStream ostream) throws IOException {
        ostream.writeInt(getId());
        ostream.writeObject(getUri().toString());
        ostream.writeObject(getDescription());
        ostream.writeObject(getCreateTime());
        ostream.writeObject(tags);
    }

    private void readObject(ObjectInputStream istream) throws IOException, ClassNotFoundException {
        setId(istream.readInt());
        setUri(Uri.parse((String)istream.readObject()));
        setDescription((String)istream.readObject());
        setCreateTime((Date)istream.readObject());
        setTags((ArrayList<AbstractTag>)istream.readObject());
    }
}
