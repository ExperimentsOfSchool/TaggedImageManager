package homework.taggedimagemanager;

import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.ImageSwitcher;
import android.widget.ImageView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifError;
import pl.droidsonroids.gif.GifIOException;


/**
 * Created by rtnelo on 16-12-5.
 */

class OnSwipeTouchListener implements View.OnTouchListener {

    private final GestureDetector gestureDetector;

    public OnSwipeTouchListener (Context ctx){
        gestureDetector = new GestureDetector(ctx, new GestureListener());
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        return gestureDetector.onTouchEvent(event);
    }

    private final class GestureListener extends GestureDetector.SimpleOnGestureListener {

        private static final int SWIPE_THRESHOLD = 100;
        private static final int SWIPE_VELOCITY_THRESHOLD = 100;

        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
            boolean result = false;
            try {
                float diffY = e2.getY() - e1.getY();
                float diffX = e2.getX() - e1.getX();
                if (Math.abs(diffX) > Math.abs(diffY)) {
                    if (Math.abs(diffX) > SWIPE_THRESHOLD && Math.abs(velocityX) > SWIPE_VELOCITY_THRESHOLD) {
                        if (diffX > 0) {
                            onSwipeRight();
                        } else {
                            onSwipeLeft();
                        }
                    }
                    result = true;
                }
                else if (Math.abs(diffY) > SWIPE_THRESHOLD && Math.abs(velocityY) > SWIPE_VELOCITY_THRESHOLD) {
                    if (diffY > 0) {
                        onSwipeBottom();
                    } else {
                        onSwipeTop();
                    }
                    result = true;
                } else {
                    result = false;
                }
            } catch (Exception exception) {
                exception.printStackTrace();
            }
            return result;
        }

        @Override
        public boolean onSingleTapUp(MotionEvent e) {
            OnSwipeTouchListener.this.onClick();
            return true;
        }
    }

    public void onSwipeRight() {
    }

    public void onSwipeLeft() {
    }

    public void onSwipeTop() {
    }

    public void onSwipeBottom() {
    }

    public void onClick() {
    }
}


public class ImageSwitchViewer extends ImageSwitcher {
    private ArrayList<Uri> uris;
    private int currentIndex;
    private Uri currentUri;

    public interface OnChangeCallback {
        public void callback(Uri uri);
    }

    private OnChangeCallback onChange;

    public ImageSwitchViewer(final Context context, AttributeSet attrs) {
        super(context, attrs);
        uris = null;
        currentIndex = 0;
        setFactory(new ViewFactory() {
            @Override
            public View makeView() {
                ImageView view = new ImageView(context);
                view.setScaleType(ImageView.ScaleType.CENTER_CROP);
                view.setLayoutParams(new
                        ImageSwitcher.LayoutParams(LayoutParams.MATCH_PARENT,
                        LayoutParams.MATCH_PARENT));
                return view;
            }
        });

        setOnTouchListener(new OnSwipeTouchListener(context) {
            @Override
            public void onSwipeLeft() {
                ImageSwitchViewer.this.switchForward();
            }

            @Override
            public void onSwipeRight() {
                ImageSwitchViewer.this.switchBackward();
            }

            @Override
            public void onClick() {
                ImageSwitchViewer.this.callOnClick();
            }
        });
    }

    public ArrayList<Uri> setUris(ArrayList<Uri> uris) {
        ArrayList<Uri> origin = this.uris;
        this.uris = uris;
        return origin;
    }

    public int setCurrentIndex(int index) {
        int origin = this.currentIndex;
        this.currentIndex = index;
        this.setImageURI(uris.get(currentIndex));
        return  origin;
    }

    public int getCurrentIndex() {
        return this.currentIndex;
    }

    public Uri getCurrentUri() {
        return currentUri;
    }

    private void callOnChange(Uri uri) {
        if (this.onChange != null) {
            this.onChange.callback(uri);
        }
    }

    public void switchForward() {
        if (uris != null) {
            setOutAnimation(AnimationUtils.loadAnimation(this.getContext(), R.anim.slide_out_left));
            setInAnimation(AnimationUtils.loadAnimation(this.getContext(), R.anim.slide_in_right));

            Uri newUri = uris.get(++currentIndex % uris.size());
            this.callOnChange(newUri);
            this.setImageURI(newUri);
        }
    }

    public void switchBackward() {
        if (uris != null) {
            setOutAnimation(AnimationUtils.loadAnimation(this.getContext(), R.anim.slide_out_right));
            setInAnimation(AnimationUtils.loadAnimation(this.getContext(), R.anim.slide_in_left));

            Uri newUri = uris.get(--currentIndex % uris.size());
            this.callOnChange(newUri);
            this.setImageURI(newUri);
        }
    }

    public void setOnChange(OnChangeCallback onChage) {
        this.onChange = onChage;
    }

    @Override
    public void setImageURI(Uri uri) {
        currentUri = uri;
        try {
            GifDrawable drawable = new GifDrawable(new File(uri.getPath()));
            this.setImageDrawable(drawable);
        } catch (IOException e) {
            super.setImageURI(uri);
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
