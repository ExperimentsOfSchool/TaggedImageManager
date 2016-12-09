package homework.taggedimagemanager;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.ListAdapter;

import java.util.List;

import homework.taggedimagemanager.model.Image;

/**
 * Created by rtnelo on 16-12-9.
 */

public class ImageListView extends GridView {
    public ImageListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private class ImageListAdapter extends BaseAdapter {
        private List<Image> images;

        public ImageListAdapter(List<Image> images) {
            super();
            this.images = images;
        }

        public void addImage(Image image) {
            images.add(image);
            this.notifyDataSetChanged();
        }

        public List<Image> getImages() {
            return images;
        }

        private class ImageItemView extends ImageView {
            private Image image;

            public ImageItemView(Context context, Image image) {
                super(context);
                this.setImage(image);
                this.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            public Image getImage() {
                return image;
            }

            public void setImage(Image image) {
                this.image = image;
                this.setImageURI(image.getUri());
            }
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public Object getItem(int i) {
            return images.get(i);
        }

        @Override
        public long getItemId(int i) {
            return images.get(i).getId();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.i("AdapterPosition", "" + position);
            if (convertView == null) {
                convertView = new ImageItemView(parent.getContext(), (Image)getItem(position));
                convertView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, ((ImageListView)parent).getColumnWidth()));
            } else {
                ((ImageItemView)convertView).setImage((Image)getItem(position));
            }
            return convertView;
        }
    }

    public interface EventHandler {
        void onSelect(Image image, long position);
    }

    private EventHandler eventHandler;

    public void setEventHandler(EventHandler eventHandler) {
        this.eventHandler = eventHandler;
        this.setOnItemClickListener(new OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Image image = ((ImageListAdapter.ImageItemView)view).getImage();
                eventHandler.onSelect(image, i);
            }
        });
    }

    public void setImages(List<Image> images) {
        this.setAdapter(new ImageListAdapter(images));
    }

    public List<Image>getImages() {
        ImageListAdapter adapter = (ImageListAdapter)this.getAdapter();
        return adapter.getImages();
    }

    public void addImage(Image image) {
        ImageListAdapter adapter = (ImageListAdapter)this.getAdapter();
        adapter.addImage(image);
    }
}
