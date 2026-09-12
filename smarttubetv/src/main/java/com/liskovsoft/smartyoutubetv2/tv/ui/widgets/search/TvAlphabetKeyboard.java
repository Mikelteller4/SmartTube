package com.liskovsoft.smartyoutubetv2.tv.ui.widgets.search;

import android.content.Context;
import android.text.Editable;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.TextView;
import com.liskovsoft.smartyoutubetv2.tv.R;
import java.util.ArrayList;
import java.util.List;

/** In-app D-pad keyboard. Edits the existing search field so its result pipeline is preserved. */
public final class TvAlphabetKeyboard extends FrameLayout {
    private static final String[] PAGES = {
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ-'",
            "1234567890@#$%&*()!?.,:;/+=_- ",
            "ÁÉÍÓÚÜÑÀÈÌÒÙÂÊÎÔÛÄËÏÖÜÇÆŒß-'"
    };
    private final EditText editor;
    private final List<TextView> letters = new ArrayList<>();
    private int page;

    public TvAlphabetKeyboard(Context context, EditText editor, Runnable submit) {
        super(context);
        this.editor = editor;
        for (int i = 0; i < 28; i++) {
            final int index = i;
            TextView key = key("", (i % 7) * 33, (i / 7) * 33, 33, 33);
            key.setOnClickListener(v -> {
                String values = PAGES[page];
                if (index < values.length()) replaceSelection(String.valueOf(values.charAt(index)));
            });
            letters.add(key);
        }
        TextView delete = key("⌫", 242, 0, 33, 33);
        delete.setContentDescription(context.getString(R.string.tv_keyboard_delete));
        delete.setOnClickListener(v -> deletePrevious());
        TextView numbers = key("&123", 242, 33, 33, 33);
        numbers.setTextSize(12);
        numbers.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
        numbers.setContentDescription(context.getString(R.string.tv_keyboard_numbers));
        numbers.setOnClickListener(v -> { page = page == 1 ? 0 : 1; updateLetters(); });
        TextView accents = key("🌐", 242, 66, 33, 33);
        accents.setContentDescription(context.getString(R.string.tv_keyboard_accents));
        accents.setOnClickListener(v -> { page = page == 2 ? 0 : 2; updateLetters(); });
        action(R.string.tv_keyboard_space, 10, () -> replaceSelection(" "));
        action(R.string.tv_keyboard_clear, 82, () -> editor.setText(""));
        action(R.string.tv_keyboard_search, 154, submit);
        updateLetters();
    }

    private void updateLetters() {
        String values = PAGES[page];
        for (int i = 0; i < letters.size(); i++) {
            letters.get(i).setText(i < values.length() ? String.valueOf(values.charAt(i)) : "");
        }
    }

    private void action(int label, int left, Runnable action) {
        TextView key = key(getContext().getString(label), left, 135, 66, 27);
        key.setTextSize(12);
        key.setTypeface(android.graphics.Typeface.DEFAULT_BOLD);
        key.setBackgroundResource(R.drawable.tv_keyboard_action);
        key.setOnClickListener(v -> action.run());
    }

    private TextView key(String label, int left, int top, int width, int height) {
        boolean deleteIcon = "⌫".equals(label);
        boolean globeIcon = "🌐".equals(label);
        TextView key = new TextView(getContext()) {
            private final android.graphics.Paint iconPaint = new android.graphics.Paint(android.graphics.Paint.ANTI_ALIAS_FLAG);
            private final android.graphics.Path iconPath = new android.graphics.Path();
            @Override protected void onDraw(android.graphics.Canvas canvas) {
                super.onDraw(canvas);
                if (!deleteIcon && !globeIcon) return;
                float size = (deleteIcon ? 14 : 12) * getResources().getDisplayMetrics().density;
                canvas.save();
                canvas.translate((getWidth() - size) / 2, (getHeight() - size) / 2);
                canvas.scale(size / 24, size / 24);
                iconPaint.setColor(getCurrentTextColor());
                iconPaint.setStyle(android.graphics.Paint.Style.STROKE);
                iconPaint.setStrokeWidth(deleteIcon ? 1.3f : 1.7f);
                iconPaint.setStrokeJoin(android.graphics.Paint.Join.ROUND);
                if (deleteIcon) {
                    iconPath.reset();
                    iconPath.moveTo(6, 3); iconPath.lineTo(22, 3); iconPath.lineTo(22, 21);
                    iconPath.lineTo(6, 21); iconPath.lineTo(0, 12); iconPath.close();
                    canvas.drawPath(iconPath, iconPaint);
                    canvas.drawLine(9, 8, 17, 16, iconPaint);
                    canvas.drawLine(9, 16, 17, 8, iconPaint);
                } else {
                    canvas.drawCircle(12, 12, 10, iconPaint);
                    canvas.drawOval(new android.graphics.RectF(7, 2, 17, 22), iconPaint);
                    canvas.drawLine(2, 12, 22, 12, iconPaint);
                }
                canvas.restore();
            }
        };
        key.setId(generateViewId());
        key.setText(deleteIcon || globeIcon ? "" : label);
        key.setTextSize(16);
        key.setTextColor(getResources().getColorStateList(R.color.tv_search_text));
        key.setGravity(Gravity.CENTER);
        key.setFocusable(true);
        key.setBackgroundResource(R.drawable.tv_keyboard_key);
        LayoutParams params = new LayoutParams(dp(width), dp(height));
        params.leftMargin = dp(left);
        params.topMargin = dp(top);
        addView(key, params);
        return key;
    }

    private void replaceSelection(String value) {
        Editable text = editor.getText();
        int a = editor.getSelectionStart();
        int b = editor.getSelectionEnd();
        int start = a < 0 || b < 0 ? text.length() : Math.min(a, b);
        int end = a < 0 || b < 0 ? text.length() : Math.max(a, b);
        text.replace(start, end, value);
        editor.setSelection(start + value.length());
    }

    private void deletePrevious() {
        Editable text = editor.getText();
        int a = editor.getSelectionStart();
        int b = editor.getSelectionEnd();
        int start = a < 0 || b < 0 ? text.length() : Math.min(a, b);
        int end = a < 0 || b < 0 ? text.length() : Math.max(a, b);
        if (start == end && start > 0) start = Character.offsetByCodePoints(text, start, -1);
        text.delete(start, end);
        editor.setSelection(start);
    }

    private int dp(int value) { return Math.round(value * getResources().getDisplayMetrics().density); }
}
