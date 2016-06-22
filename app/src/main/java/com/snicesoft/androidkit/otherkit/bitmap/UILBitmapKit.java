package com.snicesoft.androidkit.otherkit.bitmap;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.FailReason;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.snicesoft.basekit.BitmapKit;
import com.snicesoft.basekit.bitmap.BitmapConfig;
import com.snicesoft.basekit.bitmap.BitmapLoadListener;

/**
 * universalimageloader 图片加载
 *
 * @author zhuzhe
 * @note 不能离线
 */
public class UILBitmapKit extends BitmapKit {
    private ImageLoader imageLoader;

    public synchronized static BitmapKit getInstance(Context ctx) {
        if (instance == null) {
            instance = new UILBitmapKit(ctx);
        }
        return instance;
    }

    private UILBitmapKit(Context ctx) {
        initUIL(ctx);
    }

    private void initUIL(Context ctx) {
        imageLoader = ImageLoader.getInstance();
        ImageLoaderConfiguration.Builder builder = new ImageLoaderConfiguration.Builder(ctx);
        // 设置线程得优先级，
        builder.threadPriority(Thread.NORM_PRIORITY - 2);
        // 设置图片下载和显示的工作队列排序
        builder.tasksProcessingOrder(QueueProcessingType.LIFO);
        // 写日志
        builder.writeDebugLogs();
        // 线程池大小
        builder.threadPoolSize(3);
        // 当同一个Uri获取不同大小的图片，缓存到内存时，只缓存一个。默认会缓存多个不同的大小的相同图片
        builder.denyCacheImageMultipleSizesInMemory();
        // 缓存文件得最大个数
        builder.diskCacheFileCount(60);
        // 设置缓存文件的名字
        builder.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        imageLoader.init(builder.build());
    }

    @Override
    public <V extends View> void display(V v, String url) {
        if (v instanceof ImageView) {
            imageLoader.displayImage(url, (ImageView) v);
        }
    }

    @SuppressWarnings("deprecation")
    @Override
    public <V extends View> void display(V v, String url, BitmapConfig config) {
        if (v instanceof ImageView) {
            DisplayImageOptions.Builder builder = new DisplayImageOptions.Builder();
            if (config.getLoadingRes() != 0)
                builder.showImageOnLoading(v.getContext().getResources().getDrawable(config.getLoadingRes()));// 设置图片在下载期间显示的图片
            if (config.getFailRes() != 0)
                builder.showImageForEmptyUri(v.getContext().getResources().getDrawable(config.getFailRes()));// 设置图片Uri为空或是错误的时候显示的图片
            if (config.getFailRes() != 0)
                builder.showImageOnFail(v.getContext().getResources().getDrawable(config.getFailRes()));// 设置图片加载/解码过程中错误时候显示的图片
            builder.displayer(new RoundedBitmapDisplayer(20));
            builder.bitmapConfig(Bitmap.Config.RGB_565);
            imageLoader.displayImage(url, (ImageView) v, builder.build());
        }
    }

    @Override
    public <V extends View> void display(final V v, String url, final BitmapLoadListener<V> listener) {
        if (v instanceof ImageView) {
            imageLoader.displayImage(url, (ImageView) v, new ImageLoadingListener() {

                @Override
                public void onLoadingStarted(String arg0, View arg1) {

                }

                @Override
                public void onLoadingFailed(String arg0, View arg1, FailReason arg2) {
                    if (listener != null) {
                        listener.onLoadFailed(v, null);
                    }
                }

                @Override
                public void onLoadingComplete(String arg0, View container, Bitmap bitmap) {
                    if (listener != null) {
                        listener.onLoadCompleted(v, bitmap);
                    }
                }

                @Override
                public void onLoadingCancelled(String arg0, View arg1) {

                }
            });
        }
    }

    @Override
    public void clearMemoryCache() {
        imageLoader.clearMemoryCache();
    }

    @Override
    public void clearDiskCache() {
        imageLoader.clearDiskCache();
    }

    @Override
    public void pause() {
        imageLoader.pause();
    }

    @Override
    public void resume() {
        imageLoader.resume();
    }

    @Override
    public void cancel() {
        imageLoader.stop();
    }

}
