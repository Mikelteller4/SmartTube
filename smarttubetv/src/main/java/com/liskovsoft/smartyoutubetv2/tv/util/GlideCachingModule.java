package com.liskovsoft.smartyoutubetv2.tv.util;

import android.content.Context;
import android.app.ActivityManager;

import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.cache.InternalCacheDiskCacheFactory;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.module.AppGlideModule;

/**
 * https://bumptech.github.io/glide/doc/configuration.html#disk-cache<br/>
 * https://stackoverflow.com/questions/46108915/how-to-increase-the-cache-size-in-glide-android
 */
@GlideModule
public class GlideCachingModule extends AppGlideModule {
    private final static long CACHE_SIZE = 10 * 1024 * 1024; // 10 MB

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        //if (MyApplication.from(context).isTest())
        //    return; // NOTE: StatFs will crash on robolectric.

        // Limit cache size
        builder.setDiskCache(new InternalCacheDiskCacheFactory(context, CACHE_SIZE));
        ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memory = new ActivityManager.MemoryInfo();
        if (manager != null) {
            manager.getMemoryInfo(memory);
            // Some entry-level TVs do not advertise the Android low-RAM flag.
            if (manager.isLowRamDevice() || memory.totalMem <= 2L * 1024 * 1024 * 1024) {
                MemorySizeCalculator sizes = new MemorySizeCalculator.Builder(context)
                        .setMemoryCacheScreens(1f)
                        .setBitmapPoolScreens(1f)
                        .setMaxSizeMultiplier(0.15f)
                        .setLowMemoryMaxSizeMultiplier(0.15f)
                        .build();
                builder.setMemoryCache(new LruResourceCache(sizes.getMemoryCacheSize()));
                builder.setBitmapPool(new LruBitmapPool(sizes.getBitmapPoolSize()));
            }
        }
    }
}
