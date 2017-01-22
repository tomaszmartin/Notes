package pl.codeinprogress.notes.view.image;

import android.graphics.Bitmap;
import android.util.DisplayMetrics;

import com.squareup.picasso.Transformation;

import pl.codeinprogress.notes.view.BaseActivity;

public class ImageTransformation implements Transformation {

    private BaseActivity activity;

    public ImageTransformation(BaseActivity activity) {
        this.activity = activity;
    }

    @Override
    public Bitmap transform(Bitmap source) {
        DisplayMetrics metrics = new DisplayMetrics();
        activity.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        float divider = 1.0f;

        int padding = 100;
        int minWidth = metrics.widthPixels - padding;
        if (source.getWidth() > minWidth) {
            divider = source.getWidth() / minWidth;
        }
        int x = Math.round(source.getWidth() / divider);
        int y = Math.round(source.getHeight() / divider);
        Bitmap result = Bitmap.createScaledBitmap(source, x, y, false);
        if (result != source) {
            source.recycle();
        }
        return result;
    }

    @Override
    public String key() {
        return ImageTransformation.class.getSimpleName();
    }

}