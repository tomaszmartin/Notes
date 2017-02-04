package pl.codeinprogress.notes.util.image;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;

import com.squareup.picasso.Transformation;

public class ImageTransformation implements Transformation {

    private AppCompatActivity activity;

    public ImageTransformation(AppCompatActivity activity) {
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