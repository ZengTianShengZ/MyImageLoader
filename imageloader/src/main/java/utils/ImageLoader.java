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

		// ȡӦ���ڴ�� 8/1 ��Ϊ ͼƬ������
		int cacheSize = maxMemory / 8;
		// �õ� LruCache
		mLruCache = new LruCache<String, Bitmap>(cacheSize) {
			@Override
			protected int sizeOf(String key, Bitmap bitmap) {
				return bitmap.getByteCount();
			}
		};

	}

	// final ��ʹ�� ��http://blog.csdn.net/salahg/article/details/7529091

	/**
	 * ��ʾ ͼƬ
	 *
	 * @param context
	 *            �� ������
	 * @param imageView
	 *            �� ImageView �ؼ�
	 * @param sourcePath
	 *            �� ͼƬ ��ַ
	 * @param r_Id
	 *            �� Ĭ�� ͼƬ id ��R.drowable.id;
	 * @param callback
	 *            ��ͼƬ��ʾ �ص�
	 */
	public void displayBmp(final Context context, final ImageView imageView, final String sourcePath, final int r_Id,
						   final ImageCallback callback) {

		final String path;

		if (!TextUtils.isEmpty(sourcePath)) {
			path = sourcePath;

		} else {
			return;
		}
		// �� ���� �� ���� �õ� ͼƬ �� path ��Ϊ ͼƬ�� key
		Bitmap bmp = mLruCache.get(path);

		if (bmp != null) {
			if (callback != null) {
				// �ص� ͼƬ ��ʾ
				callback.imageLoad(imageView, bmp, sourcePath);
			}
			// imageView.setImageBitmap(bmp);
			return;
		}
		// ��� bmp == null ���� imageView ��ʾĬ��ͼƬ
		imageView.setImageResource(r_Id);
		// ���� �̳߳�
		threadPoolUtils.getExecutorService().execute(new Runnable() {
			Bitmap bitmap = null;

			@Override
			public void run() {
				// TODO Auto-generated method stub

				try {
					// ���� ͼƬ ��ַ ��Ӧ �� ����ͼ
					bitmap = revitionImageSize(imageView, sourcePath);
				} catch (Exception e) {

				}
				if (bitmap == null) {
					try {
						// ��� ����ͼ û���سɹ� ��ʾ Ĭ�� ���õ�ͼƬ
						bitmap = BitmapFactory.decodeResource(context.getResources(), r_Id);
					} catch (Exception e) {
					}
				}
				if (path != null && bitmap != null) {
					// �� ����ͼ �Ž� ���� �� path ��Ϊ key
					putBitmapToLruCache(path, bitmap);
				}

				if (callback != null) {
					handler.post(new Runnable() {
						@Override
						public void run() {
							// �ص� ͼƬ ��ʾ
							callback.imageLoad(imageView, bitmap, sourcePath);

						}
					});
				}

			}
		});

	}

	public Bitmap revitionImageSize(ImageView imageView, String path) throws IOException {

		// �õ� ����  ImageView �� ���
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

		// �����ʵ�ʿ�ߺ�Ŀ���ߵı���
		final int heightRatio = Math.round((float) height / (float) img_height);
		final int widthRatio = Math.round((float) width / (float) img_width);
		// ѡ���͸�����С�ı�����ΪinSampleSize��ֵ���������Ա�֤����ͼƬ�Ŀ�͸�
		// һ��������ڵ���Ŀ��Ŀ�͸ߡ�
		inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		// �������涨��ķ�������inSampleSizeֵ
		options.inSampleSize = inSampleSize;

		options.inJustDecodeBounds = false;
		in = new BufferedInputStream(new FileInputStream(new File(path)));
		bitmap = BitmapFactory.decodeStream(in, null, options);

		in.close();
		return bitmap;
	}

	/**
	 * ��ͼƬ�洢��LruCache
	 */
	public void putBitmapToLruCache(String key, Bitmap bitmap) {
		if (getBitmapFromLruCache(key) == null && mLruCache != null) {
			mLruCache.put(key, bitmap);
		}
	}

	/**
	 * ��LruCache�����ȡͼƬ
	 */
	public Bitmap getBitmapFromLruCache(String key) {
		return mLruCache.get(key);
	}

	/**
	 * ��ʾͼƬ�ص�
	 *
	 * @author Administrator
	 *
	 */
	public interface ImageCallback {
		public void imageLoad(ImageView imageView, Bitmap bitmap, Object... params);
	}

}
