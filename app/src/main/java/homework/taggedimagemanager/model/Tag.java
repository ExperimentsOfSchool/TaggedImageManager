package homework.taggedimagemanager.model;

import java.util.ArrayList;

/**
 * Created by rtnelo on 16-12-6.
 */

public class Tag extends AbstractTag {
    private ArrayList<AbstractTag> children;

    public ArrayList<AbstractTag> getChildren() {
        return children;
    }

    public void setChildren(ArrayList<AbstractTag> children) {
        this.children = children;
    }

    public void addChild(AbstractTag child) {
        this.children.add(child);
    }

    public Tag(int id, String title, ArrayList<AbstractTag> children) {
        super(id, title);
        this.children = children;
    }

    @Override
    public String toString() {
        return "Tag{" +
                "id=" + this.getId() +
                ", title=" + this.getTitle() +
                ", children=" + children +
                '}';
    }
}
