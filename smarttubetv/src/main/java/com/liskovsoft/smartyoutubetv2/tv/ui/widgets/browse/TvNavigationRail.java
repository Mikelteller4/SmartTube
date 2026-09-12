package com.liskovsoft.smartyoutubetv2.tv.ui.widgets.browse;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.widget.FrameLayout;
import android.widget.ImageView;
import androidx.core.content.ContextCompat;
import com.liskovsoft.mediaserviceinterfaces.data.MediaGroup;
import com.liskovsoft.smartyoutubetv2.tv.R;
import java.util.HashMap;
import java.util.Map;

/** Compact navigation visible beside the video shelves, matching the TV reference. */
public final class TvNavigationRail extends FrameLayout {
    public static final int SEARCH = -10001;
    public static final int ACCOUNT = -10002;
    public static final int MORE = -10003;
    public static final int MOVIES = -10004;
    public interface Listener { void onNavigate(int destination); }
    private final Map<Integer, ImageView> icons = new HashMap<>();

    public TvNavigationRail(Context context, Listener listener) {
        super(context);
        setBackgroundColor(0xFF0F0F0F);
        addIcon(ACCOUNT, R.drawable.browse_title_account, R.string.settings_accounts, 31, listener);
        addIcon(SEARCH, R.drawable.tv_rail_search, androidx.leanback.R.string.lb_search_bar_hint, 95, listener);
        addIcon(MediaGroup.TYPE_HOME, R.drawable.tv_rail_home_state, R.string.header_home, 134, listener);
        addIcon(MediaGroup.TYPE_MUSIC, R.drawable.tv_rail_music_state, R.string.header_music, 173, listener);
        addIcon(MOVIES, R.drawable.tv_rail_movies, R.string.tv_navigation_movies, 212, listener);
        addIcon(MediaGroup.TYPE_SPORTS, R.drawable.tv_rail_sports, R.string.header_sports, 251, listener);
        addIcon(MediaGroup.TYPE_SUBSCRIPTIONS, R.drawable.tv_rail_subscriptions, R.string.header_subscriptions, 290, listener);
        addIcon(MediaGroup.TYPE_USER_PLAYLISTS, R.drawable.tv_rail_library, R.string.header_playlists, 329, listener);
        addIcon(MORE, R.drawable.tv_rail_more, R.string.tv_navigation_more, 368, listener);
        ImageView settings = addIcon(MediaGroup.TYPE_SETTINGS, R.drawable.tv_rail_settings, R.string.header_settings, 0, listener);
        LayoutParams params = (LayoutParams) settings.getLayoutParams();
        params.gravity = Gravity.BOTTOM | Gravity.START;
        params.bottomMargin = dp(62);
        settings.setLayoutParams(params);
        ImageView live = addIcon(MediaGroup.TYPE_LIVE, R.drawable.tv_rail_tv, R.string.badge_live, 0, listener);
        LayoutParams liveParams = (LayoutParams) live.getLayoutParams();
        liveParams.gravity = Gravity.BOTTOM | Gravity.START;
        liveParams.bottomMargin = dp(22);
        live.setLayoutParams(liveParams);
    }

    private ImageView addIcon(int destination, int drawable, int label, int top, Listener listener) {
        ImageView icon = new ImageView(getContext());
        icon.setImageDrawable(ContextCompat.getDrawable(getContext(), drawable));
        icon.setColorFilter(Color.WHITE);
        icon.setAlpha(0.7f);
        icon.setContentDescription(getContext().getString(label));
        icon.setScaleType(ImageView.ScaleType.FIT_CENTER);
        int padding = destination == ACCOUNT ? dp(7) : dp(12);
        icon.setPadding(padding, padding, padding, padding);
        // D-pad navigation expands the full menu through BrowseFrameLayout.
        icon.setFocusable(false);
        icon.setOnClickListener(v -> listener.onNavigate(destination));
        LayoutParams params = new LayoutParams(dp(40), dp(40));
        params.leftMargin = dp(19);
        params.topMargin = dp(top);
        addView(icon, params);
        icons.put(destination, icon);
        return icon;
    }

    public void select(int destination) {
        for (Map.Entry<Integer, ImageView> entry : icons.entrySet()) {
            entry.getValue().setAlpha(entry.getKey() == destination ? 1f : 0.7f);
            entry.getValue().setSelected(entry.getKey() == destination);
        }
    }

    private int dp(int value) { return Math.round(value * getResources().getDisplayMetrics().density); }
}
