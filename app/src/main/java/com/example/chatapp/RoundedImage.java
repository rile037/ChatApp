package com.example.chatapp;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatImageView;

public class RoundedImage extends AppCompatImageView {
    public RoundedImage(Context context) {
        super(context);
    }

    public RoundedImage(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        BitmapDrawable drawable = (BitmapDrawable) getDrawable();
        if (drawable == null) {
            super.onDraw(canvas);
            return;
        }

        Bitmap bitmap = drawable.getBitmap();
        if (bitmap == null) {
            super.onDraw(canvas);
            return;
        }

        int width = getWidth();
        int height = getHeight();
        int minSize = Math.min(width, height);
        int radius = minSize / 2;

        Bitmap roundedBitmap = Bitmap.createBitmap(minSize, minSize, Bitmap.Config.ARGB_8888);
        Canvas roundedCanvas = new Canvas(roundedBitmap);

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setColor(Color.WHITE);

        Path path = new Path();
        path.addCircle(minSize / 2f, minSize / 2f, radius, Path.Direction.CW);
        roundedCanvas.clipPath(path);

        // Skalirajte bitmapu kako biste je razvukli i popunili krug
        Matrix matrix = new Matrix();
        matrix.setRectToRect(new RectF(0, 0, bitmap.getWidth(), bitmap.getHeight()), new RectF(0, 0, minSize, minSize), Matrix.ScaleToFit.FILL);
        roundedCanvas.drawBitmap(bitmap, matrix, paint);

        canvas.drawBitmap(roundedBitmap, 0, 0, paint);
    }
}
