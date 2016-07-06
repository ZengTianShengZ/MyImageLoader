package utils;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.util.HashMap;


import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Handler;
import android.os.Looper;
import android.support.v4.util.LruCache;
import android.text.TextUtils;
import android.util.Log;
import android.widget.ImageView;

public class ImageLoader {

	private ThreadPoolUtils threadPoolUtils = new ThreadPoolUtils();
	private LruCache<String, Bitmap> mLruCache;

	int maxMemory = (int) Runtime.getRuntime().maxMemory();

	public Handler handler = new Handler();

	public ImageLoader() {

		// 取应用内存的 8/1 作为 图片缓存用
		int cacheSize = maxMemory / 8;
		// 得到 LruCache
		mLruCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getByteCount();
			}
		};

	}

	// final 的使用 ：http://blog.csdn.net/salahg/article/details/7529091

	/**
	 * 显示 图片
	 *
	 * @param context
	 *            ： 上下文
	 * @param imageView
	 *            ： ImageView 控件
	 * @param sourcePath
	 *            ： 图片 地址
	 * @param r_Id
	 *            ： 默认 图片 id ，R.drowable.id;
	 * @param callback
	 *            ：图片显示 回调
	 */
	public void displayBmp(final Context context, final ImageView imageView, final String sourcePath, final int r_Id,
						   final ImageCallback callback) {

		final String path;

		if (!TextUtils.isEmpty(sourcePath)) {
			path = sourcePath;

		} else {
			return;
		}
		// 先 试着 从 缓存 得到 图片 ， path 作为 图片的 key
		Bitmap bmp = mLruCache.get(path);

		if (bmp != null) {
			if (callback != null) {
				// 回调 图片 显示
				callback.imageLoad(imageView, bmp, sourcePath);
			}
			// imageView.setImageBitmap(bmp);
			return;
		}
		// 如果 bmp == null ，给 imageView 显示默认图片
		imageView.setImageResource(r_Id);
		// 启动 线程池
		threadPoolUtils.getExecutorService().execute(new Runnable() {
			Bitmap bitmap = null;

			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {
					// 加载 图片 地址 对应 的 缩略图
					bitmap = revitionImageSize(imageView, sourcePath);
				} catch (Exception e) {

				}
				if (bitmap == null) {
					try {
						// 如果 缩略图 没加载成功 显示 默认 设置的图片
						bitmap = BitmapFactory.decodeResource(context.getResources(), r_Id);
					} catch (Exception e) {
					}
				}
				if (path != null && bitmap != null) {
					// 将 缩略图 放进 缓存 ， path 作为 key
					putBitmapToLruCache(path, bitmap);
				}

				if (callback != null) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							// 回调 图片 显示
							callback.imageLoad(imageView, bitmap, sourcePath);

						}
					});
				}

			}
		});

	}

	public Bitmap revitionImageSize(ImageView imageView, String path) throws IOException {

		// 得到 布局  ImageView 的 宽高
		int img_width = imageView.getWidth();
		int img_height = imageView.getHeight();

		BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(path)));
		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		BitmapFactory.decodeStream(in, null, options);
		in.close();

		int height = options.outHeight;
		int width = options.outWidth;

		Bitmap bitmap = null;

		int inSampleSize = 1;

		// 计算出实际宽高和目标宽高的比率
		final int heightRatio = Math.round((float) height / (float) img_height);
		final int widthRatio = Math.round((float) width / (float) img_width);
		// 选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
		// 一定都会大于等于目标的宽和高。
		inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		// 调用上面定义的方法计算inSampleSize值
		options.inSampleSize = inSampleSize;

		options.inJustDecodeBounds = false;
		in = new BufferedInputStream(new FileInputStream(new File(path)));
		bitmap = BitmapFactory.decodeStream(in, null, options);

		in.close();
		return bitmap;
	}

	/**
	 * 将图片存储到LruCache
	 */
	public void putBitmapToLruCache(String key, Bitmap bitmap) {
		if (getBitmapFromLruCache(key) == null && mLruCache != null) {
			mLruCache.put(key, bitmap);
		}
	}

	/**
	 * 从LruCache缓存获取图片
	 */
	public Bitmap getBitmapFromLruCache(String key) {
		return mLruCache.get(key);
	}

	/**
	 * 显示图片回调
	 *
	 * @author Administrator
	 *
	 */
	public interface ImageCallback {
		public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params);
	}

}
