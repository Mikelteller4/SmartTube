package com.liskovsoft.smartyoutubetv2.tv.ui.widgets.browse;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.bumptech.glide.Glide;
import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.mediaserviceinterfaces.oauth.Account;
import com.liskovsoft.smartyoutubetv2.common.app.presenters.dialogs.AccountSelectionPresenter;
import com.liskovsoft.smartyoutubetv2.common.app.presenters.settings.AccountSettingsPresenter;
import com.liskovsoft.smartyoutubetv2.common.misc.MediaServiceManager;
import com.liskovsoft.smartyoutubetv2.tv.R;

/** Full-screen TV account chooser based on YouTube's 2024 Android TV layout. */
public final class TvAccountPickerDialog extends Dialog {
    public interface Listener { void onNavigate(int destination); }

    private final float density;
    private final Listener listener;
    private final boolean accountPicker;
    private final int selectedDestination;

    public TvAccountPickerDialog(Context context, Listener listener) {
        this(context, listener, true, TvNavigationRail.ACCOUNT);
    }

    private TvAccountPickerDialog(Context context, Listener listener, boolean accountPicker,
                                  int selectedDestination) {
        super(context, android.R.style.Theme_Black_NoTitleBar_Fullscreen);
        this.listener = listener;
        this.accountPicker = accountPicker;
        this.selectedDestination = selectedDestination;
        density = context.getResources().getDisplayMetrics().density;
        setContentView(createContent());

        Window window = getWindow();
        if (window != null) {
            window.setBackgroundDrawable(new ColorDrawable(accountPicker ? 0xFF0F0F0F : Color.TRANSPARENT));
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    public static TvAccountPickerDialog showNavigation(Context context, Listener listener,
                                                       int selectedDestination) {
        TvAccountPickerDialog dialog = new TvAccountPickerDialog(
                context, listener, false, selectedDestination);
        dialog.show();
        return dialog;
    }

    @Override
    public void show() {
        super.show();
        Window window = getWindow();
        if (window != null) {
            window.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        }
    }

    private View createContent() {
        FrameLayout root = new FrameLayout(getContext());
        root.setBackgroundColor(accountPicker ? 0xFF0F0F0F : Color.TRANSPARENT);
        if (!accountPicker) {
            View panel = new View(getContext());
            panel.setBackgroundColor(0xFF0F0F0F);
            add(root, panel, 0, 0, 250, 540);
            View titleMask = new View(getContext());
            titleMask.setBackgroundColor(0xFF0F0F0F);
            add(root, titleMask, 250, 0, 220, 80);
        }

        LinearLayout account = createNavigationRow(R.drawable.browse_title_account,
                accountName(), 16, 40, 226, 42,
                accountPicker || selectedDestination == TvNavigationRail.ACCOUNT,
                accountPicker ? null : () -> {
                    dismiss();
                    new TvAccountPickerDialog(getContext(), listener).show();
                });
        root.addView(account);

        int y = 105;
        addNavigation(root, R.drawable.tv_rail_search, getContext().getString(androidx.leanback.R.string.lb_search_bar_hint),
                TvNavigationRail.SEARCH, y);
        addNavigation(root, R.drawable.tv_rail_home, getContext().getString(R.string.header_home),
                MediaGroup.TYPE_HOME, y += 38);
        addNavigation(root, R.drawable.tv_rail_movies, getContext().getString(R.string.tv_navigation_movies_tv),
                TvNavigationRail.MOVIES, y += 38);
        addNavigation(root, R.drawable.icon_gaming, getContext().getString(R.string.header_gaming),
                MediaGroup.TYPE_GAMING, y += 38);
        addNavigation(root, R.drawable.tv_rail_music, getContext().getString(R.string.header_music),
                MediaGroup.TYPE_MUSIC, y += 38);
        addNavigation(root, R.drawable.tv_rail_subscriptions, getContext().getString(R.string.header_subscriptions),
                MediaGroup.TYPE_SUBSCRIPTIONS, y += 38);
        addNavigation(root, R.drawable.tv_rail_library, getContext().getString(R.string.tv_navigation_library),
                MediaGroup.TYPE_USER_PLAYLISTS, y += 38);
        addNavigation(root, R.drawable.tv_rail_more, getContext().getString(R.string.tv_navigation_more),
                TvNavigationRail.MORE, y += 38);
        addNavigation(root, R.drawable.tv_rail_settings, getContext().getString(R.string.header_settings),
                MediaGroup.TYPE_SETTINGS, 477);

        if (!accountPicker) {
            account.post(account::requestFocus);
            return root;
        }

        TextView title = text(getContext().getString(R.string.tv_account_whos_watching), 25, Color.WHITE, true);
        // The 2024 TV layout centers this heading over the profile area rather than
        // aligning it with the navigation column.
        add(root, title, 567, 92, 400, 50);

        Account selected = MediaServiceManager.instance().getSelectedAccount();
        int profileX = selected == null ? 744 : 575;
        if (selected != null) {
            addProfile(root, selected.getName(), selected.getAvatarImageUrl(), profileX, 168, false, () -> dismiss());
            profileX += 210;
        }
        addProfile(root, getContext().getString(R.string.tv_account_guest), null, profileX, 168, true, () -> {
            AccountSelectionPresenter.instance(getContext()).selectAccount(null);
            dismiss();
        });

        LinearLayout addAccount = new LinearLayout(getContext());
        addAccount.setGravity(Gravity.CENTER);
        addAccount.setOrientation(LinearLayout.HORIZONTAL);
        addAccount.setFocusable(true);
        addAccount.setBackground(roundRect(0xFF566B9D, 17));
        ImageView addIcon = new ImageView(getContext());
        addIcon.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.tv_account_add));
        addAccount.addView(addIcon, new LinearLayout.LayoutParams(dp(18), dp(18)));
        TextView addLabel = text(getContext().getString(R.string.tv_account_add), 16, Color.WHITE, true);
        LinearLayout.LayoutParams addLabelParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addLabelParams.leftMargin = dp(8);
        addAccount.addView(addLabel, addLabelParams);
        addAccount.setOnClickListener(v -> {
            dismiss();
            AccountSettingsPresenter.instance(getContext()).show();
        });
        add(root, addAccount, 589, 447, 135, 33);

        account.post(account::requestFocus);
        return root;
    }

    private void addNavigation(FrameLayout root, int icon, String label, int destination, int top) {
        Runnable action = destination == TvNavigationRail.MORE && !accountPicker
                ? () -> setContentView(createMoreContent())
                : () -> {
                    dismiss();
                    listener.onNavigate(destination);
                };
        root.addView(createNavigationRow(icon, label, 16, top, 226, 38,
                !accountPicker && selectedDestination == destination, action));
    }

    private View createMoreContent() {
        FrameLayout root = new FrameLayout(getContext());
        View panel = new View(getContext());
        panel.setBackgroundColor(0xFF0F0F0F);
        add(root, panel, 0, 0, 250, 540);
        View titleMask = new View(getContext());
        titleMask.setBackgroundColor(0xFF0F0F0F);
        add(root, titleMask, 250, 0, 220, 80);

        LinearLayout back = createNavigationRow(R.drawable.tv_rail_more,
                getContext().getString(R.string.tv_navigation_more), 16, 40, 226, 42,
                true, () -> setContentView(createContent()));
        root.addView(back);
        int y = 105;
        addExtraNavigation(root, R.drawable.tv_rail_sports, getContext().getString(R.string.header_sports),
                MediaGroup.TYPE_SPORTS, y);
        addExtraNavigation(root, R.drawable.tv_rail_tv, getContext().getString(R.string.badge_live),
                MediaGroup.TYPE_LIVE, y += 38);
        addExtraNavigation(root, R.drawable.icon_news, getContext().getString(R.string.header_news),
                MediaGroup.TYPE_NEWS, y += 38);
        addExtraNavigation(root, R.drawable.icon_channels, getContext().getString(R.string.header_channels),
                MediaGroup.TYPE_CHANNEL_UPLOADS, y += 38);
        addExtraNavigation(root, R.drawable.icon_history, getContext().getString(R.string.header_history),
                MediaGroup.TYPE_HISTORY, y += 38);
        addExtraNavigation(root, R.drawable.icon_playlist, getContext().getString(R.string.my_videos),
                MediaGroup.TYPE_MY_VIDEOS, y += 38);
        back.post(back::requestFocus);
        return root;
    }

    private void addExtraNavigation(FrameLayout root, int icon, String label, int destination, int top) {
        root.addView(createNavigationRow(icon, label, 16, top, 226, 38, false, () -> {
            dismiss();
            listener.onNavigate(destination);
        }));
    }

    private LinearLayout createNavigationRow(int iconRes, String label, int left, int top,
                                             int width, int height, boolean selected, Runnable action) {
        LinearLayout row = new LinearLayout(getContext());
        row.setOrientation(LinearLayout.HORIZONTAL);
        row.setGravity(Gravity.CENTER_VERTICAL);
        row.setPadding(dp(14), 0, dp(10), 0);
        row.setFocusable(true);
        row.setClickable(true);
        row.setBackground(selected ? roundRect(Color.WHITE, 12) : null);

        ImageView icon = new ImageView(getContext());
        icon.setImageDrawable(ContextCompat.getDrawable(getContext(), iconRes));
        icon.setColorFilter(selected ? 0xFF0F0F0F : 0xFFF1F1F1);
        row.addView(icon, new LinearLayout.LayoutParams(dp(16), dp(16)));

        TextView text = text(label, 16, selected ? 0xFF0F0F0F : 0xFFF1F1F1, true);
        LinearLayout.LayoutParams textParams = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
        textParams.leftMargin = dp(14);
        row.addView(text, textParams);

        row.setOnFocusChangeListener((v, focused) -> {
            row.setBackground(focused ? roundRect(Color.WHITE, 12) : null);
            int color = focused ? 0xFF0F0F0F : 0xFFF1F1F1;
            icon.setColorFilter(color);
            text.setTextColor(color);
        });
        if (action != null) row.setOnClickListener(v -> action.run());

        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(dp(width), dp(height));
        params.leftMargin = dp(left);
        params.topMargin = dp(top);
        row.setLayoutParams(params);
        return row;
    }

    private void addProfile(FrameLayout root, String name, String avatarUrl, int left, int top,
                            boolean guest, Runnable action) {
        LinearLayout profile = new LinearLayout(getContext());
        profile.setOrientation(LinearLayout.VERTICAL);
        profile.setGravity(Gravity.CENTER_HORIZONTAL);
        profile.setFocusable(true);
        profile.setClickable(true);

        ImageView avatar = new ImageView(getContext());
        avatar.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
        avatar.setPadding(dp(28), dp(28), dp(28), dp(28));
        avatar.setBackground(circle(guest ? 0xFF27304A : 0xFFB21E58));
        profile.addView(avatar, new LinearLayout.LayoutParams(dp(140), dp(140)));
        if (avatarUrl != null) {
            avatar.setPadding(0, 0, 0, 0);
            Glide.with(getContext()).load(avatarUrl).circleCrop().into(avatar);
        } else {
            avatar.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.browse_title_account));
            avatar.setColorFilter(0xFFC8D0DD);
        }

        TextView label = text(name != null ? name : getContext().getString(R.string.tv_account_guest),
                16, 0xFFF1F1F1, false);
        label.setGravity(Gravity.CENTER);
        LinearLayout.LayoutParams labelParams = new LinearLayout.LayoutParams(dp(180), dp(34));
        labelParams.topMargin = dp(10);
        profile.addView(label, labelParams);
        profile.setOnClickListener(v -> action.run());
        profile.setOnFocusChangeListener((v, focused) -> avatar.setBackground(circle(
                focused ? 0xFFF1F1F1 : (guest ? 0xFF27304A : 0xFFB21E58))));
        add(root, profile, left, top, 180, 190);
    }

    private String accountName() {
        Account account = MediaServiceManager.instance().getSelectedAccount();
        return account != null && account.getName() != null
                ? account.getName() : getContext().getString(R.string.tv_account_guest);
    }

    private TextView text(String value, int sp, int color, boolean medium) {
        TextView view = new TextView(getContext());
        view.setText(value);
        view.setTextColor(color);
        view.setTextSize(sp);
        view.setGravity(Gravity.CENTER_VERTICAL);
        view.setFontFeatureSettings("kern");
        view.setTypeface(android.graphics.Typeface.create("sans-serif", medium
                ? android.graphics.Typeface.BOLD : android.graphics.Typeface.NORMAL));
        return view;
    }

    private void add(FrameLayout parent, View child, int left, int top, int width, int height) {
        FrameLayout.LayoutParams params = new FrameLayout.LayoutParams(dp(width), dp(height));
        params.leftMargin = dp(left);
        params.topMargin = dp(top);
        parent.addView(child, params);
    }

    private GradientDrawable roundRect(int color, int radius) {
        GradientDrawable result = new GradientDrawable();
        result.setColor(color);
        result.setCornerRadius(dp(radius));
        return result;
    }

    private GradientDrawable circle(int color) {
        GradientDrawable result = new GradientDrawable();
        result.setShape(GradientDrawable.OVAL);
        result.setColor(color);
        return result;
    }

    private int dp(int value) { return Math.round(value * density); }
}
