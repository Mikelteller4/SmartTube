# YouTube for Android TV: reference and acceptance record

Goal remains open. Building successfully is not evidence of visual equivalence.

## Reference set

Use the coherent screenshots published by Google LLC on the Google Play listing for `com.google.android.youtube.tv`, retrieved 2026-09-06. These provide home, music, search and playback from the same visual generation. The listing's update date does not establish when its screenshots were captured. Do not mix the older playback arrangement in these screenshots with the September/December 2025 player redesign.

Source: https://play.google.com/store/apps/details?id=com.google.android.youtube.tv&hl=en

Analysis copies at the workspace level: `work/references/play-0.png` (home), `play-1.png` (music), `play-2.png` (search), `play-3.png` (player). They are reference material, not bundled app assets.

## Observed geometry at 1920 × 1080

- Canvas: #0F0F0F. Persistent navigation icons centered near x=78.
- Avatar centered near (78,102). Search icon around (78,230), then vertical destinations about 78 pixels apart.
- Shelves start at x=156. First row title around y=188; thumbnails at y=249.
- Home lead row: 720 × 405 cards; subsequent shelves use 528 × 297 cards, all with 36-pixel gutters. Selected images have an outer white border and metadata stays on the canvas.
- Music album tiles use square artwork when the service reports square source dimensions; ordinary video tiles retain 16:9 geometry.
- Header microphone: 72-pixel circle at x=156, y=66. Search pill starts x=246, width about 630, height 72. Wordmark near the upper-right, approximately 195 pixels wide.
- Titles: bold; selected white, others grey. Author and views/age are separate metadata lines.
- Search: persistent rail, input at top, history chips to the left of an alphabet keyboard; video results below.
- Playback: title and metadata above seek bar on the left; action icons to the right; red-to-pink progress, white seek knob; related videos below.

## Verification requirements

Compare actual screenshots at the same resolution, allowing different video content, recommendation order and account-specific data. Verify focus, selection, expanded/collapsed sidebar, search editing/submission, playback controls and back navigation. Record missing or unverified states explicitly.

## Outstanding differences

- Exact typeface rasterization and the geometry of hand-authored rail/action glyphs remain approximate.
- Movies & TV is backed by a real search because SmartTube has no paid Movies service; the lowest reference rail glyph is ambiguous and maps to LIVE.
- Search's globe opens an alternate-character page rather than a reference-verified language menu; speech recognition depends on a recognizer installed on the target device.
- Signed-in multi-profile account placement, unavailable-content screens, settings and other secondary states have incomplete public references.
- Live service content cannot match the static screenshots, and full playback, audio output and sign-in have not been verified end to end.

No completion claim is justified while these differences remain.

## Search implementation and validation (2026-09-07)

Search now uses an in-app alphabet keyboard, editable query pill, compact navigation rail, microphone and wordmark. Up to five real history/suggestion chips occupy the left column. The existing search provider and result pipeline supply content. Keyboard actions support selection replacement, code-point deletion, space, clear, submit, numbers and accented letters. The system keyboard is suppressed for this screen. Outdated suggestion callbacks are discarded and chip focus is retained across replacement.

Measured emulator positions at 1920×1080: query pill (246,66), 1026×72; first letter center (737,197); first suggestion (156,176); first result thumbnail (156,634), 528×297. Results have a separate clipped viewport below the keyboard. These positions agree closely with play-2.png; typography and icons remain approximate.

Manual checks: D-pad and center entered AB with focus moving from A to B; UI hierarchy confirmed query AB and focused key B. Clear, delete and search submission were exercised; real CAT and abc song results loaded. History persisted across installation. Screenshots: work/search-refined.png and work/search-results-refined.png. No AndroidRuntime exception observed during these checks. This does not verify playback or authentication.

## Implemented and observed in this iteration

Persistent compact rail survives shelf browsing and hides when the existing expanded menu opens. A protected margin hook in the local Leanback module reserves 22dp only for BrowseFragment; its default remains zero. Content starts at x=156. Header search pill now opens SearchPresenter, and the account action lives in the rail. Card zoom and shelf shadows are disabled. Default video cards are 528×297 at density 2, with 36-pixel gaps and bold white/grey titles.

Observed emulator screenshots: `work/tv-rail.png`, `work/tv-expanded.png`, `work/tv-refined.png`, `work/tv-search-before.png`. Expanded/collapsed navigation and opening search worked without an AndroidRuntime exception. The search-before screenshot records the original UI prior to the implementation described above.

Remaining measured corrections: current thumbnail y≈261 versus reference y≈249. Reference border extends outside the thumbnail while the implementation draws inside. Shelf title still dims through Leanback select-level handling. The Movies position in the compact rail is not implemented; do not claim an exact rail match. Focused title auto-scrolling also changes the reference's two-line treatment.

## Card alignment and playback baseline (2026-09-07)

Overriding lb_basic_card_info_padding_horizontal removes the 22-pixel metadata inset; retain the separate lb_basic_card_info_padding resource because custom layouts still reference it. Browse header padding is now 33dp, placing the search pill at y=66, and the microphone icon is 16dp. Video metadata splits a verified author prefix from following visits/date using a newline while retaining spans; other summaries are unchanged.

Build build-card-alignment-2.log passed, APK installed. work/card-aligned.png and work/card-aligned.xml confirm metadata x=156/720/1284 and author/detail on separate lines. No AndroidRuntime exception observed.

Playback was opened normally from an abc song search result using D-pad center. The Finny The Shark video rendered and advanced to 0:22 of 2:00. This verifies initial playback, not completion, seeking, audio or sign-in. work/player-controls-before.png records the current controls; work/player-controls.xml records initial hierarchy. The player still has an oversized title, two action rows, right-aligned combined elapsed/total, quality/date and remaining-time labels. These differ from play-3.png. Entry layout: smarttubetv/src/main/res/layout/lb_playback_transport_controls_row.xml. This baseline now makes player layout validation possible.

## Player restructuring in progress

The transport layout now places the description at the left and a single six-action row at the right, followed by progress and independent elapsed/total labels. Channel, captions, like, dislike, save and quality actions remain bound to their existing handlers. The quality action still uses the HQ icon/menu rather than the reference settings gear. Legacy primary actions remain bound in a hidden container; center on the seek bar must continue to toggle pause. This behavior requires runtime verification before claiming a working replacement.

The first build (work/build-tv-player.log) succeeded and installed. work/player-tv-first.png showed that the initial title was clipped by Leanback text resizing and the seek center was y=912 rather than y=807. Refinement adds an 80.5dp bottom anchor and disables automatic title resizing. Progress is red-to-pink with a persistent white knob and guards zero duration during geometry calculation. Refinement compilation and runtime verification are pending; this section is a development record, not acceptance evidence.

## Verified player progress (2026-09-08)

build-tv-player-refined-2.log and build-player-shelves.log passed and installed. Controls-focused screenshots show the seek center at y=807, matching play-3.png. Elapsed and total occupy opposite ends. The title uses 20sp bold with 24dp line spacing, constrained to 320dp. Recommendations no longer show a row heading; their top remains about y=952 rather than reference y=898. The row still needs a 16-pixel horizontal correction and action icons/spacing still differ. The continuous track no longer draws segment colors, while segment data and handlers remain available.

Manual runtime evidence: center-pre.xml records 21:10 with seek bar focused; two center presses separated by one second produced 21:12 in center-after-a.xml, still 21:12 in center-after-b.xml after a further three seconds. This verifies pause/resume through D-pad center. player-action-focus.xml showed Open channel focused after Up from the bar; Right/center opened the Subtitles dialog (player-captions.xml). Right/Right changed preview from 21:12 to 21:32 (seek-final.xml); after confirmation the video rendered and reached 21:53 (player-verified.xml). No AndroidRuntime exception was observed.

The post-seek screenshot exposed another title-clipping defect: chapter text inserted into the weighted description layout squeezed the title. The next correction removes weights, keeps chapter text in seek preview only, makes buffered progress grey and aligns knob center with the played track independently of focus. Verify this correction at runtime before closing the defect. Overall fidelity remains incomplete.

Chapter correction verified: work/build-player-chapter-fix.log passed (1m15s), installed through Probar.ps1; apksigner verify and git diff --check passed. After seeking, work/chapter-verified.xml shows the full two-line title at [96,619][736,714] and subtitle at [96,715][736,743], with no chapter body squeezing either field. work/chapter-verified.png confirms the visual result, continuous red/pink progress, grey buffer and white knob at 22:34. Saved user preview: outputs/player-preview.png. Playback references still differ in action glyphs/spacing/avatar size, exact title/subtitle offsets, settings-menu behavior, suggestion position/left margin and background shading. Goal remains active.

## Shelf text correction (2026-09-08)

CustomListRowPresenter now creates RowHeaderPresenter with animateSelect=false, leaving shelf headings white while focus moves. VideoCardPresenter disables the scrolling title variant so a focused card preserves the two-line bold title. Playback continues to opt out of row headers explicitly.

work/build-shelf-titles.log passed (1m13s); installed with Probar.ps1. work/shelf-titles.png shows white Recommended and For the family headers. After Right/Left and a three-second pause, work/shelf-title-focus.xml confirms the selected title remains a standard TextView at [156,572][684,664] with the original full text. No AndroidRuntime exception observed. outputs/smarttube-preview.png and APK hashes are updated. This resolves the previously recorded dim shelf titles and automatic marquee difference; other geometry and navigation differences remain.

## Compact rail icon correction (2026-09-08)

Home now uses an outlined vector and More uses three horizontal strokes matching the coherent Play reference. More has its own English/Spanish accessibility label rather than Channels. Its existing navigation callback is retained. work/build-rail-icons.log passed (1m4s), APK installed. work/rail-icons.png verifies the drawings; tapping (78,776) expanded the menu, recorded in work/more-open.png and work/more-menu.xml. The expanded category order, icon treatment, dimensions and focus treatment still differ; this does not claim menu equivalence. No AndroidRuntime exception observed. Updated preview and APK hashes are in outputs.

## Expanded-menu reference audit

Image search surfaced TechHive's March 2024 youtubehome-1.jpg and older 2022/2020 menus, but the 2024 image URL returned HTTP 404 and the article returned HTTP 410. Search-result descriptions also give a different category order from the Play reference. These results are insufficient to fix exact expanded-menu dimensions or establish that it is the same UI generation. Do not mix them into the accepted reference set. Official functional documentation remains https://support.google.com/youtube/answer/7583931?hl=en; it is not pixel evidence for the chosen historical layout.

The existing More tap was observed to expose the menu without focusing a header. A scoped pending-focus flag now requests focus on the header grid after a rail-triggered expansion completes. Runtime validation is pending.

Rail-triggered menu focus is now verified. The final change requests focus on the selected header item with requestFocusFromTouch after the transition; this handles a mouse/touch click that otherwise leaves Android in touch mode. work/build-rail-focus-touch.log passed (1m3s) and installed. work/more-focused-item.xml confirms Home itself focused immediately after tapping More; a single Down focuses Shorts in work/more-next-item.xml. No AndroidRuntime exception observed and diff --check passed. The expanded visual layout remains unverified against a coherent reference and differs from the compact menu arrangement.

## Shelf geometry correction

Reduced lb_browse_expanded_selected_row_top_padding from 16dp to 10dp and compensated search's row alignment offset from 31dp to 37dp. work/build-shelf-gap.log passed (54s), APK installed. work/shelf-gap.xml measures first browse images [156,249][684,546], [720,249][1248,546], matching the reference y=249 and 528×297 dimensions; row header remains [156,181][346,229]. work/gap-search.xml confirms search images still begin at x=156/720/1284 and y=634. No AndroidRuntime exception observed. This resolves the previous 12-pixel browse-image offset; card border, card text spacing, per-row sizes and secondary-row geometry still differ. Updated outputs/smarttube-preview.png and APK hashes.

## External card focus border (2026-09-09)

VideoCardPresenter now draws the selected outline after child rendering, outside the thumbnail rather than as its foreground. The stroke is 4.5dp, giving nine external pixels at density 2; thumbnail corners are clipped independently at 9dp on API 21+. Card ancestors up to the nearest grid allow this overflow when attached, retaining the outer shelf viewport. The first attempt clipped the top and sides; attachment-time configuration corrected that.

work/build-card-border-overflow.log passed (55s), installed through Probar.ps1. work/border-overflow.png and work/border-second.png show complete outlines and rounded lower corners. After Right, border-second.xml keeps thumbnail bounds [720,249][1248,546]. Pixel checks find the external stroke at x=711..719 and y=240..248, with background at x=710 and at the previous card's external border x=147. The border moves without resizing the image or retaining the previous selection.

The emulator's unfiltered AndroidRuntime log contains a crash from com.google.android.youtube.tv; the modified app's own PID-filtered log has no AndroidRuntime error. Do not attribute the other package's exception to SmartTube. The overall goal remains incomplete: per-row sizing, secondary shelf spacing, remaining icons, expanded navigation, secondary screens and player details still require work.

## Home lead shelf dimensions (2026-09-09)

MultipleRowsFragment has a separate lead-card presenter, enabled only by VideoRowsFragment for BrowsePresenter.isHomeSection(). The first non-Shorts home row uses 360 x 202.5dp, multiplied by the existing grid scale; ordinary cards and other sections retain their existing dimensions. Long-press handling is shared with the standard presenter.

work/build-home-lead.log passed in 2m5s (459 tasks), then Probar.ps1 installed successfully. work/home-lead.xml verifies thumbnails [156,249][876,654] and [912,249][1632,654]: 720 x 405px, matching Play home. The second shelf retains 528px width, beginning [156,976][684,1080] at the viewport edge. Right-navigation captured in home-lead-right.xml advances selection; the app's PID-filtered AndroidRuntime log is empty. outputs/smarttube-preview.png and APK hashes are refreshed.

Remaining: the second shelf begins at y=976 versus the reference's y=984; typography and metadata spacing differ, as do remaining icons, expanded menu, other shelf types and player details. This is not an exact completed clone.

## Compact rail vectors and Home selection (2026-09-09)

Replaced the compact rail's raster music, subscriptions and library icons and heavy Leanback search glyph with vectors following the accepted Play reference: thin search outline, concentric music circles, outlined subscription screen and stacked library screens. Home now uses a state-list drawable with a filled selected silhouette and an outlined default. The existing click destinations are retained.

work/build-rail-vectors.log passed in 1m4s; Probar.ps1 installed the resulting APK. work/rail-vectors.png shows the filled Home icon after launch; work/rail-search.png shows the outlined Home icon and white selected search icon after tapping Search. Both screenshots were inspected. Own-PID AndroidRuntime output is empty. Preview and APK checksums refreshed.

These hand-authored vectors improve fidelity but are not proven pixel-identical. Sports, settings, account, absent movie/extra-TV destinations and expanded navigation remain different. Overall goal remains active.

## Natural title and metadata height (2026-09-09)

ComplexImageCardView now sets a one-line minimum and the requested maximum instead of forcing exactly the maximum number of lines. This removes the empty title line before channel metadata on short titles, matching the reference's variable metadata positions.

work/build-card-natural-lines.log passed in 59s; Probar.ps1 installed successfully. work/natural-lines.xml shows the short title at [912,668][1632,722] and metadata starting y=726, versus the prior fixed title ending y=760 and metadata y=764. The long title retains [156,668][876,760]. The screenshot was inspected. After Right, natural-lines-focus.xml confirms the short card focused and thumbnails still at y=249..654. Own-PID AndroidRuntime output is empty. Updated preview and APK hashes.

Remaining text fidelity: line spacing, thumbnail-to-title gap, duration badge and secondary shelf offsets differ. No claim of overall exact fidelity.

## Thumbnail duration badges (2026-09-09)

text_badge_image_view now uses 10sp bold sans-serif, white text, 3dp horizontal/2dp vertical padding, and 6dp end spacing. A 2dp bottom margin above the existing 4dp progress footprint produces a 6dp bottom inset. ComplexImageView applies rounded backgrounds (2dp radius) while retaining the supplied normal/live colors.

work/build-duration-badge.log passed in 46s and Probar.ps1 installed successfully. work/duration-badge.xml verifies 26:09 at [802,610][864,642] inside thumbnail ending [876,654]: 32px badge height and 12px end/bottom inset. LIVE appears at [1567,610][1620,642] with red fill. Screenshot inspected; own-PID AndroidRuntime has no errors. Updated preview and APK hashes.

The badge now follows reference sizing/weight/insets; exact pixel identity is not established. Other typography, rail destinations, expanded navigation and player details remain incomplete.

## Sports and settings outlines (2026-09-09)

Compact rail now uses outlined trophy and gear vectors in place of filled raster icons, following the accepted Play screenshots. Existing sizing, positions and destinations remain. work/build-rail-sports-settings.log passed in 45s; Probar.ps1 installed successfully. work/rail-outlines.png was inspected: both outlines render completely. Own-PID AndroidRuntime output is empty; preview and APK hashes updated.

Horizontal focus scrolling was considered but not altered: static Play screenshots do not establish scrolling behavior for the second selected card. This remains unverified rather than being treated as a proven defect. Missing rail destinations, account appearance, expanded menu, typography and player details still prevent exact completion.

## Square music artwork (2026-09-09)

MultipleRowsFragment now selects a separate 148.5dp square presenter for music items and square source thumbnails in the Music section, retaining ordinary video dimensions and existing long-press/click handling. Initial TYPE_MUSIC-only detection did not cover current TV album tiles. WrapperMediaItem in the MediaServiceCore submodule now exposes validated thumbnail dimensions instead of inheriting the fixed 1280x720 fallback. Missing dimensions still use that fallback. This submodule has a local modification which must be preserved with the fork.

work/build-music-aspect.log passed in 2m5s; APK installed. music-aspect.xml shows the upper video shelf at 528x297 and the album shelf at 297px width. After Down then Right, music-square-focus.xml verifies complete square images [156,249][453,546], [489,249][786,546], [822,249][1119,546], with the second album focused. Both screenshots were inspected; the border follows the square artwork. Own-PID AndroidRuntime output is empty. outputs/music-preview.png and APK hashes updated.

The live feed differs from the reference in shelf content and ordering. Music shelf vertical spacing, metadata wrapping, selected Music icon, expanded menu, missing destinations and player details remain incomplete; this does not establish overall exact fidelity.

## Selected Music rail icon (2026-09-09)

Added a selected Music vector and state-list drawable: white outer disc with cut-out ring/play symbol, retaining the outline for other sections. work/build-music-icon-state.log passed in 1m21s, installed successfully. work/music-selected.png and work/music-unselected.png were inspected after entering Music and then Search, confirming the expected state change on Android TV 34. Own-PID AndroidRuntime output is empty; APK hashes refreshed. Exact glyph pixel identity and older Android rendering are not established. Other known fidelity gaps remain.

## Search keyboard glyphs and action weight (2026-09-09)

TvAlphabetKeyboard now draws fixed-size outlined delete and globe symbols using the current text/focus color instead of font-dependent glyphs. The globe retains the existing alternate alphabet action. Numbers and bottom actions use bold text matching the Play reference's weight.

work/build-keyboard-icons.log passed in 1m8s; Probar.ps1 installed. work/keyboard-icons.png was inspected and copied to outputs/search-preview.png. Tapping the globe then first key produces Á in the search editor (keyboard-accent.xml); tapping Delete returns the editor to the Search hint (keyboard-delete.xml). Own-PID AndroidRuntime output is empty. APK hashes refreshed.

This improves glyph consistency and action weight. Globe behavior remains an alternate-character page rather than a verified recreation of the official language menu; exact outline geometry, voice focus state, recommended initial search results and other overall fidelity gaps remain.

## Recent-search icon and inset (2026-09-09)

SearchTagsFragmentBase uses a thin vector history clock and the existing thin search vector for nonempty query suggestions. Chip start padding is 10dp and icon-to-text spacing 5dp, placing text at x=218px on the 1920px emulator (reference approximately x=219), replacing the prior x=228 inset.

work/build-search-history.log passed in 1m; Probar.ps1 installed. work/search-history.png was inspected and copied to outputs/search-preview.png. Tapping the CAT history chip populates the editor with CAT in history-search-result.xml. Own-PID AndroidRuntime output is empty. APK hashes refreshed. Initial recommended results and voice focus still differ; overall goal remains incomplete.

## Initial search recommendations and empty history (2026-09-10)

Removed SearchTagsFragment's explicit suppression of empty queries. Opening Search and clearing the editor now request the service's actual empty-query recommendations, using the existing result/disposal pipeline. Runtime exposed an empty saved-history chip; SearchTagStorage in MediaServiceCore now rejects null/blank saves and filters blank entries on restore.

Initial build-search-recommendations.log passed in 58s and confirmed real From recommended searches results at [156,634][684,931]. Final work/build-empty-history-fix.log passed in 2m50s and installed. empty-history-fixed.png was inspected: recommendations visible, CAT and abc song preserved, no empty history chip. recs-cat.xml records CAT in the editor; recs-cleared.xml records the Search hint and From recommended searches after CLEAR. Own-PID AndroidRuntime output is empty. Updated search preview and APK hashes. Preserve both local submodule changes (thumbnail dimensions and history storage).

This resolves the missing initial result shelf. Voice focus, exact text/glyph geometry, navigation destinations/expanded menu and player details still prevent full completion.

## Voice-search focus state (2026-09-10)

Search now gives the speech orb initial focus when the editor is empty. The selected orb remains 72 x 72px at `[156,66][228,138]`, uses the reference's near-white fill and a dark microphone, and changes to a dark orb with a muted microphone after focus moves to search history. The `Voice search` hint and speech-mode text colors follow the accepted Play screenshot. A state-list image tint avoids Leanback's asynchronous speech-state updates overwriting the focused icon color.

`work/build-voice-state-tint.log` passed in 1m2s and the APK was installed. `work/voice-state-tint.xml` confirms `lb_search_bar_speech_orb` focused at the expected bounds; `work/voice-state-tint.png` and `work/search-down.png` were inspected for both focus states. The app's PID-filtered AndroidRuntime output is empty. `outputs/search-preview.png` now shows the final focused state.

Speech recognition itself depends on a recognizer service available on the target Android TV device and is not established by the emulator's visual test. Search-result content and history remain account/service dependent.

## Missing compact-rail destinations (2026-09-10)

Added the two visible slots present in the accepted Play reference but absent from the prior compact rail. A thin clapperboard icon occupies `[38,424][118,504]` and opens a functional search for `movies`; a TV outline occupies `[38,956][118,1036]` and selects SmartTube's LIVE section. This completes the compact rail's visible slot count and vertical placement for the chosen reference. English and Spanish accessibility labels are included.

`work/build-rail-movies.log` passed in 1m13s and `work/build-rail-tv.log` passed in 53s. `work/rail-movies-clean.xml` records the `movies` editor value, related suggestions and `Search results for movies`; `work/rail-live.xml` records the LIVE header selected. `work/rail-final.png` and `work/rail-movies-clean.png` were inspected. The app's PID-filtered AndroidRuntime output is empty.

The movie destination is a search because SmartTube has no equivalent paid Movies & Shows browse service. The bottom reference glyph's exact product semantics cannot be established from the public screenshot; LIVE is the closest working local destination. Expanded navigation remains SmartTube's section list and is not visually equivalent to the compact YouTube TV rail.

## Player controls and recommendation alignment (2026-09-12)

Matched the accepted YouTube TV player reference's six-action rhythm by reducing the control spacing to 90px between centers. The playlist action now uses a distinct outlined square-plus glyph and the quality action uses the same thin gear language as the navigation rail while retaining SmartTube's advanced playback settings handler.

The recommendation row content is translated independently from Leanback's focus movement. Runtime capture `work/player-content-aligned-final.png` places its first card at `x=96, y=895`; the reference begins at approximately `x=96, y=897`. The seek bar remains centered at `y=807`, matching the reference. `work/player-settings-handler-2.png` confirms that selecting the new gear opens `Advanced playback settings`.

`work/build-player-actions.log` passed in 57s and `work/build-player-content-align.log` passed in 1m5s. The final x86 APK installed successfully on Android Emulator 36.6.11 (Android TV 34). Emulator 37.1.11 was retained as `work/android-sdk/emulator-37.1.11-backup` after it stopped exposing ADB; the replacement archive's published SHA-256 was verified before installation.

Player content depends on the live service and differs from the static public reference. Exact typeface rasterization, animation timing, expanded navigation, account state and service-specific destinations remain unverifiable or visibly different, so these measurements do not establish an exact pixel-for-pixel clone of every YouTube TV state.

## In-app account picker (2026-09-12)

The persistent profile icon now opens a full-screen YouTube TV-style account picker instead of SmartTube's generic settings sheet. Its left column follows the public October 2024 Android TV reference: selected account pill at `[32,80][484,164]`, then Search, Home, Movies & TV, Gaming, Music, Subscriptions, Library and More, with Settings anchored at the bottom. The main area contains `Who’s watching?`, the available profile or Guest, and an Add account button with an outlined user-plus glyph.

`work/build-account-picker.log` passed in 2m15s, the icon build `work/build-account-picker-icon.log` passed in 1m9s, and the clean final source state `work/build-final.log` passed in 53s. `work/account-picker-release.png`, `work/account-picker-final.png` and `work/account-picker-final.xml` record the rendered state. D-pad Down from the account pill focuses Search and opens the existing YouTube-style search screen; Add account opens SmartTube's functional sign-in/settings flow; More opens the secondary modern navigation page described below. The final APK was reinstalled successfully and the app's PID-filtered AndroidRuntime output is empty.

The accepted reference shows three configured profiles while the emulator has no signed-in account, so exact horizontal profile placement for a Guest-only in-app picker cannot be established. The public Guest-only image is an app-start screen without the navigation column and is documented separately rather than mixed into this state. Signed-in avatar loading is implemented but cannot be visually verified without a user account.

## Expanded navigation and More (2026-09-12)

Pressing Left from the first card now resolves to the same modern 250dp navigation panel used by the account reference instead of leaving SmartTube's long Leanback section list visible. The primary page contains Guest/account, Search, Home, Movies & TV, Gaming, Music, Subscriptions, Library, More and bottom-anchored Settings. The selected row occupies `[32,292][484,368]` for Home in `work/navigation-release-verified.png`; the content remains shifted and starts at `x=642`, so the first card is complete rather than covered by the panel. Back dismisses the menu and restores the compact rail, recorded in `work/navigation-back-verified.png`.

More opens a matching secondary page for SmartTube-only access to Sports, LIVE, News, Channels, History and My videos. `work/navigation-more-release.png` records that page, and selecting LIVE returns to the compact rail with real live content in `work/navigation-more-live-functional.png`. This preserves the extra service sections without placing them in the reference-backed primary menu.

`work/build-modern-navigation.log`, `work/build-modern-navigation-shift.log` and `work/build-modern-navigation-final.log` passed; the exact final source state passed again in `work/build-release-verified.log` in 51s and was installed. No PID-filtered AndroidRuntime error was observed. The public reference is a photographed account-switcher state rather than a lossless capture of the ordinary expanded Home state, so pixel identity of the content offset and focus transitions cannot be established.

## Full-screen player scrim (2026-09-12)

The controls overlay now dims the complete video frame instead of leaving the upper half unobscured. Its vertical gradient runs from 60% black at the top through 70% near the middle to 85% at the bottom, matching the full-frame attenuation visible in the accepted YouTube TV player reference while preserving stronger contrast behind the title, seek bar and recommendations.

`work/build-player-final.log` passed in 1m33s after a clean resource rebuild, and `outputs/Probar.ps1` installed and launched the resulting APK. `work/player-final-verified.png` records the final controls state; returning with Back was verified in `work/player-controls-hidden-verified.png`. No `AndroidRuntime` crash appears in the app's PID-filtered log. Live playback still emits upstream SABR diagnostics and unsigned-account token messages, which are unrelated to this visual change.

## Home shelf spacing (2026-09-13)

The accepted Home reference places the second shelf's thumbnails at `y=984`; the previous build placed them at `y=976`. A Browse-only 4dp gap now separates row containers. Runtime hierarchy `work/home-row-spacing.xml` confirms that the lead row remains at `[156,249][876,654]`, while the second shelf moves to `[156,984][684,1080]`. Search remains unchanged at `[156,634][684,931]` in `work/search-spacing-check.xml`.

`work/build-home-row-spacing.log` passed in 2m10s, the APK installed successfully, and `work/home-row-spacing.png` was visually inspected against `work/references/play-0.png`.

## Card title and metadata rhythm (2026-09-13)

Pixel-row analysis of the accepted Home reference found the selected two-line title at `y=687..718` and `733..763`, author at `781..798`, and views/age at `822..843`. The prior build compressed both title and metadata line spacing. Card titles now translate down 3dp with 4dp extra line spacing; metadata translates up 1.5dp with 6.5dp extra line spacing. Bottom padding is reduced by the same measured amount, keeping the total card and row height unchanged.

`work/card-line-spacing.png` measures the corresponding rows at `688..718`, `734..763`, `781..798`, and `823..844`, within one pixel of the reference. `work/card-line-spacing.xml` confirms the lead image remains `[156,249][876,654]` and the next shelf remains `[156,984][684,1080]`. `work/build-card-line-spacing.log` passed in 53s and the resulting APK installed successfully.

## Compact-rail pixel boxes (2026-09-13)

Thresholded pixel bounding boxes were measured independently for every visible compact-rail glyph in `play-0.png` and the emulator capture. Fractional view translations and vector path extents now align all ten boxes: Search `[65,218][90,243]`, Home `[66,296][89,322]`, Music `[63,372][92,401]`, Movies `[63,452][92,477]`, Sports `[65,528][90,557]`, Subscriptions `[63,608][92,633]`, Library `[65,686][90,711]`, More `[65,767][90,785]`, Settings `[63,900][90,929]`, and the bottom TV slot `[63,981][92,1002]`.

`work/rail-pixel-final.png` records the accepted result. `work/build-rail-pixel-final.log` passed in 51s and the APK installed successfully. Matching bounding boxes establish size and placement, not identical internal path pixels; Movies & TV and the bottom slot retain the functional substitutions documented above.
