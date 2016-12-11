package homework.taggedimagemanager;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
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

import java.io.File;
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

            public ImageItemView(Context context) {
                super(context);
                this.setScaleType(ImageView.ScaleType.CENTER_CROP);
            }

            public Image getImage() {
                return image;
            }

            public void setImage(Image image, int size) {
                this.image = image;
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 3;
                Bitmap bitmap = BitmapFactory.decodeFile(image.getUri().getPath(), options);
                if (bitmap.getHeight() > size * 2 || bitmap.getWidth() > size * 2) {
                    bitmap = ThumbnailUtils.extractThumbnail(bitmap, size, size);
                }
                this.setImageBitmap(bitmap);
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
            ImageItemView imageItemView = (ImageItemView)convertView;
            int size = ((ImageListView)parent).getColumnWidth();
            if (imageItemView == null) {
                imageItemView = new ImageItemView(parent.getContext());
                imageItemView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, size));
            }
            imageItemView.setImage((Image)getItem(position), size);

            return imageItemView;
        }
    }

    public interface EventHandler {
        void onSelect(Image image, int position);
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
