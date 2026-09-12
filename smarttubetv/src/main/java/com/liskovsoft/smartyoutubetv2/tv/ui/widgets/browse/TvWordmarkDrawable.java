package com.liskovsoft.smartyoutubetv2.tv.ui.widgets.browse;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;

/** Resolution-independent wordmark for the private TV interface. */
public final class TvWordmarkDrawable extends Drawable {
    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    @Override public void draw(Canvas canvas) {
        int save = canvas.save();
        canvas.translate(getBounds().left, getBounds().top);
        canvas.scale(getBounds().width() / 156f, getBounds().height() / 40f);
        paint.setColor(0xFFFF0033);
        canvas.drawRoundRect(0, 6, 43, 34, 8, 8, paint);
        paint.setColor(0xFFFFFFFF);
        Path triangle = new Path();
        triangle.moveTo(17, 13);
        triangle.lineTo(17, 27);
        triangle.lineTo(29, 20);
        triangle.close();
        canvas.drawPath(triangle, paint);
        paint.setTypeface(Typeface.create("sans-serif-condensed", Typeface.BOLD));
        paint.setTextSize(30);
        canvas.drawText("YouTube", 49, 30, paint);
        canvas.restoreToCount(save);
    }

    @Override public int getIntrinsicWidth() { return 312; }
    @Override public int getIntrinsicHeight() { return 80; }
    @Override public void setAlpha(int alpha) { paint.setAlpha(alpha); invalidateSelf(); }
    @Override public void setColorFilter(ColorFilter filter) { paint.setColorFilter(filter); invalidateSelf(); }
    @Override public int getOpacity() { return PixelFormat.TRANSLUCENT; }
}
