package com.liskovsoft.smartyoutubetv2.tv.ui.playback.actions;

import android.content.Context;
import androidx.leanback.widget.Action;
import androidx.core.content.ContextCompat;
import com.liskovsoft.smartyoutubetv2.tv.R;

/**
 * An action for displaying a HQ (High Quality) icon.
 */
public class HighQualityAction extends Action {
    public HighQualityAction(Context context) {
        super(R.id.lb_control_high_quality);
        // The YouTube TV transport surface exposes playback options with a gear.
        // Keep this action's existing quality/settings handler and change only its glyph.
        setIcon(ContextCompat.getDrawable(context, R.drawable.tv_rail_settings));
        setLabel1(context.getString(
                R.string.playback_settings));
    }
}
