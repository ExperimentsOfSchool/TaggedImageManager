package homework.taggedimagemanager.model;

/**
 * Created by rtnelo on 16-12-6.
 */

public class AbstractTag extends DataEntity {
    private int id;
    private String title;
    private long imageCount;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public long getImageCount() {
        return imageCount;
    }

    public void setImageCount(long imageCount) {
        this.imageCount = imageCount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AbstractTag)) return false;

        AbstractTag that = (AbstractTag) o;

        if (getId() != that.getId()) return false;
        return getTitle().equals(that.getTitle());

    }

    @Override
    public int hashCode() {
        int result = getId();
        result = 31 * result + getTitle().hashCode();
        return result;
    }

    public AbstractTag(int id, String title, int imageCount) {
        this.id = id;
        this.title = title;
        this.imageCount = imageCount;
    }

    public AbstractTag(int id, String title) {
        this(id, title, 0);
    }

    @Override
    public String toString() {
        return "AbstractTag{" +
                "id=" + id +
                ", title='" + title + '\'' +
                '}';
    }
}
