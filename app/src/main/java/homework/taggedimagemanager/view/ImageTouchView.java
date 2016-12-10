package homework.taggedimagemanager.view;

import android.content.Context;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.view.MotionEvent;
import android.widget.ImageView;

/**
 * Created by Lawrence on 11/12/2016.
 *
 */
public class ImageTouchView extends ImageView {

    private PointF startPoint = new PointF();
    private Matrix matrix = new Matrix();
    private Matrix currentMatrix = new Matrix();

    private int mode = 0;
    private static final int DRAG = 1;
    private static final int ZOOM = 2;
    private float startDis = 0;
    private PointF midPoint;

    public ImageTouchView(Context context) {
        super(context);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK) {
            case MotionEvent.ACTION_DOWN:
                mode = DRAG;
                currentMatrix.set(this.getImageMatrix());
                startPoint.set(event.getX(), event.getY());
                break;
            case MotionEvent.ACTION_MOVE:
                if(mode == DRAG) {
                    float dx = event.getX();
                    float dy = event.getY();
                    matrix.set(currentMatrix);
                    matrix.postTranslate(dx, dy);
                } else if (mode == ZOOM) {
                    float endDis = distance(event);
                    if(endDis > 10f) {
                        float scale = endDis / startDis;
                        matrix.set(currentMatrix);
                        matrix.postScale(scale, scale, midPoint.x, midPoint.y);
                    }
                }
            case MotionEvent.ACTION_UP:
                mode = 0;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mode = 0;
                break;
            case MotionEvent.ACTION_POINTER_DOWN:
                mode = ZOOM;
                startDis = distance(event);
                if(startDis > 10f) {
                    midPoint = mid(event);
                    currentMatrix.set(this.getImageMatrix());
                }
                break;
        }
        this.setImageMatrix(matrix);
        return true;
    }

    private static float distance(MotionEvent event) {
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        return (float)Math.sqrt(dx * dx + dy * dy);
    }
    private static PointF mid(MotionEvent event) {
        float midx = event.getX(1) - event.getX(0);
        float midy = event.getY(1) - event.getY(0);
        return new PointF(midx / 2, midy / 2);
    }
}
