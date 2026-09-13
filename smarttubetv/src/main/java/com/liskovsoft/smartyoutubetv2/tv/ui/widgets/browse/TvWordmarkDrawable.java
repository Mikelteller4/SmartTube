package com.liskovsoft.smartyoutubetv2.tv.ui.widgets.browse;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;

import androidx.core.graphics.PathParser;

/**
 * Resolution-independent 2024 YouTube wordmark used by the private TV interface.
 * Vector source: https://commons.wikimedia.org/wiki/File:YouTube_2024.svg
 */
public final class TvWordmarkDrawable extends Drawable {
    private static final float SOURCE_LEFT = 54.079375f;
    private static final float SOURCE_TOP = 137.39814f;
    private static final float SOURCE_WIDTH = 388.67627f;
    private static final float TARGET_WIDTH = 155.2f;
    private static final float TARGET_TOP = 3.2f;
    private static final Path ICON = path("m 114.84349,221.29866 c 0,0 38.11055,0 47.56362,-2.51702 5.32265,-1.42631 9.32554,-5.53743 10.73046,-10.61341 2.59756,-9.31296 2.59756,-28.90373 2.59756,-28.90373 0,0 0,-19.46492 -2.59756,-28.69398 -1.40492,-5.20183 -5.40781,-9.22906 -10.73046,-10.61341 -9.45307,-2.55897 -47.56362,-2.55897 -47.56362,-2.55897 0,0 -38.025522,0 -47.436056,2.55897 -5.237532,1.38435 -9.325383,5.41158 -10.815742,10.61341 -2.512317,9.22906 -2.512317,28.69398 -2.512317,28.69398 0,0 0,19.59077 2.512317,28.90373 1.490359,5.07598 5.57821,9.1871 10.815742,10.61341 9.410534,2.51702 47.436056,2.51702 47.436056,2.51702 z");
    private static final Path PLAY = path("m 133.78487,179.3484 -31.4627,-17.82886 v 35.65772 z");
    private static final Path[] LETTERS = {
            path("m 209.87592,216.68371 v -22.90484 l 14.55674,-47.57143 h -10.86511 l -5.53744,21.60439 c -1.2585,4.95013 -2.39116,10.11001 -3.23017,14.89217 h -0.6712 c -0.46145,-4.19486 -1.80386,-9.69034 -3.14627,-14.97607 l -5.36963,-21.52049 h -10.86512 l 14.34699,47.57143 v 22.90484 z"),
            path("m 239.08128,163.82655 c -12.71093,0 -17.07376,7.3413 -17.07376,23.15638 v 7.50909 c 0,14.17919 2.72677,23.07265 16.82205,23.07265 13.88554,0 16.86401,-8.47396 16.86401,-23.07265 v -7.50909 c 0,-14.13707 -2.85262,-23.15638 -16.6123,-23.15638 z m 5.41158,35.32195 c 0,6.87984 -1.21656,11.20072 -5.62134,11.20072 -4.32087,0 -5.53743,-4.36283 -5.53743,-11.20072 v -16.9479 c 0,-5.87287 0.83901,-11.11665 5.53743,-11.11665 4.95013,0 5.62134,5.53743 5.62134,11.11665 z"),
            path("m 273.35883,217.48076 c 6.12474,0 9.94221,-2.55896 13.08848,-7.17349 h 0.46146 l 0.46145,6.37644 h 8.34768 v -51.85035 h -11.07445 v 41.65644 c -1.17502,2.05556 -3.90179,3.56577 -6.46076,3.56577 -3.23017,0 -4.23655,-2.55897 -4.23655,-6.83789 V 164.83336 H 262.9128 v 38.88772 c 0,8.432 2.43353,13.75968 10.44603,13.75968 z"),
            path("m 317.63649,216.68371 v -61.87646 h 12.75288 v -8.59981 H 293.9346 v 8.59981 h 12.75287 v 61.87646 z"),
            path("m 338.80753,217.48076 c 6.12474,0 9.94221,-2.55896 13.08848,-7.17349 h 0.46145 l 0.46146,6.37644 h 8.3481 v -51.85035 h -11.07487 v 41.65644 c -1.17461,2.05556 -3.90138,3.56577 -6.46034,3.56577 -3.23017,0 -4.23698,-2.55897 -4.23698,-6.83789 v -38.38432 h -11.03292 v 38.88772 c 0,8.432 2.43312,13.75968 10.44562,13.75968 z"),
            path("m 392.51057,163.7007 c -5.36963,0 -9.22905,2.34922 -11.78802,6.16669 h -0.54535 c 0.3356,-5.03403 0.5873,-9.31296 0.5873,-12.71093 v -13.71773 h -10.69732 l -0.0419,45.05441 0.0419,28.19057 h 9.31296 l 0.79706,-5.03403 h 0.29365 c 2.47506,3.39797 6.29254,5.53743 11.36852,5.53743 8.432,0 12.03972,-7.25739 12.03972,-22.69509 v -8.0125 c 0,-14.43072 -1.63606,-22.77882 -11.36852,-22.77882 z m 0.37755,30.79132 c 0,9.64856 -1.4263,15.39575 -5.91498,15.39575 -2.09751,0 -4.99208,-1.00681 -6.29254,-2.89457 v -30.83327 c 1.13266,-2.93652 3.64967,-5.07598 6.46034,-5.07598 4.53063,0 5.74718,5.49548 5.74718,15.52142 z"),
            path("m 442.75566,185.64052 c 0,-12.50101 -1.25851,-21.77202 -15.64745,-21.77202 -13.54993,0 -16.57035,9.01931 -16.57035,22.27542 v 9.10321 c 0,12.92068 2.76871,22.31754 16.23475,22.31754 10.65536,0 16.15085,-5.32769 15.52159,-15.64745 l -9.4388,-0.5034 c -0.12586,6.37644 -1.59411,8.97735 -5.83109,8.97735 -5.32768,0 -5.57939,-5.07598 -5.57939,-12.62703 v -3.52382 h 21.31074 z m -15.89915,-14.80828 c 5.11793,0 5.49548,4.82428 5.49548,13.00442 v 4.23697 h -10.90707 v -4.23697 c 0,-8.09623 0.33561,-13.00442 5.41159,-13.00442 z")
    };

    private final Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private int alpha = 255;
    private ColorFilter colorFilter;

    private static Path path(String data) {
        return PathParser.createPathFromPathData(data);
    }

    @Override
    public void draw(Canvas canvas) {
        int save = canvas.save();
        canvas.translate(getBounds().left, getBounds().top);
        canvas.scale(getBounds().width() / 156f, getBounds().height() / 40f);
        canvas.translate(0, TARGET_TOP);
        float logoScale = TARGET_WIDTH / SOURCE_WIDTH;
        canvas.scale(logoScale, logoScale);
        canvas.translate(-SOURCE_LEFT, -SOURCE_TOP);

        paint.setStyle(Paint.Style.FILL);
        paint.setColor((alpha << 24) | 0x00FF0033);
        paint.setColorFilter(colorFilter);
        canvas.drawPath(ICON, paint);

        paint.setColor((alpha << 24) | 0x00FFFFFF);
        canvas.drawPath(PLAY, paint);
        int lettersSave = canvas.save();
        canvas.translate(442.75566f, 0);
        canvas.scale(0.99225f, 1f);
        canvas.translate(-442.75566f, 0);
        for (Path letter : LETTERS) {
            canvas.drawPath(letter, paint);
        }
        canvas.restoreToCount(lettersSave);
        canvas.restoreToCount(save);
    }

    @Override public int getIntrinsicWidth() { return 312; }
    @Override public int getIntrinsicHeight() { return 80; }
    @Override public void setAlpha(int value) { alpha = value; invalidateSelf(); }
    @Override public void setColorFilter(ColorFilter value) { colorFilter = value; invalidateSelf(); }
    @Override public int getOpacity() { return PixelFormat.TRANSLUCENT; }
}
