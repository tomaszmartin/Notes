package pl.tomaszmartin.stuff;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;


/**
 * Created by tomaszmartin on 22.06.2015.
 */

public class DrawingView extends View {

    private Path drawPath;
    private Paint drawPaint, canvasPaint;
    private int paintColor = getResources().getColor(android.R.color.black);
    private int backgroundColor = getResources().getColor(android.R.color.white);
    private float strokeSize = 4;
    private Canvas drawCanvas;
    private Bitmap canvasBitmap;

    public DrawingView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        setupDrawing();
    }

    private void setupDrawing() {
        drawPath = new Path();
        drawPaint = new Paint();
        drawPaint.setColor(paintColor);

        drawPaint.setAntiAlias(true);
        drawPaint.setStrokeWidth(strokeSize);
        drawPaint.setStyle(Paint.Style.STROKE);
        drawPaint.setStrokeJoin(Paint.Join.ROUND);
        drawPaint.setStrokeCap(Paint.Cap.ROUND);

        canvasPaint = new Paint(Paint.DITHER_FLAG);
    }

    @Override
    protected void onSizeChanged(int width, int height, int oldWidth, int oldHeight) {
        super.onSizeChanged(width, height, oldWidth, oldHeight);
        canvasBitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        drawCanvas = new Canvas(canvasBitmap);
        drawCanvas.drawColor(backgroundColor);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawBitmap(canvasBitmap, 0, 0, canvasPaint);
        canvas.drawPath(drawPath, drawPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float touchX = event.getX();
        float touchY = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // Set the beginning of next contour
                drawPath.moveTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_MOVE:
                // Add a line from the point in moveTo
                drawPath.lineTo(touchX, touchY);
                break;
            case MotionEvent.ACTION_UP:
                // Draw a line from moveTo to lineTo
                drawCanvas.drawPath(drawPath, drawPaint);
                break;
            default:
                return false;
        }


        invalidate();
        return true;
    }

    public void setColor(int colorId) {
        invalidate();
        paintColor = getResources().getColor(colorId);
        drawPaint.setColor(paintColor);
    }

    public void setBrushSize(int size) {
        float pixelSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP,
                size, getResources().getDisplayMetrics());
        strokeSize = pixelSize;
        drawPaint.setStrokeWidth(strokeSize);
    }

    public void setErasing() {
        //drawPaint.setColor(backgroundColor);
        drawPaint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.CLEAR));
    }

    public void setDrawing() {
        //drawPaint.setColor(paintColor);
        drawPaint.setXfermode(null);
    }

    public void clearDrawing() {
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR);
        invalidate();
    }

}
