package com.scau.keshe.sharespace.dealImage;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;

import com.scau.keshe.sharespace.R;
import com.scau.keshe.sharespace.welcome.MainActivity;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.util.LruCache;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ImageView;

public class ImageLoader {

	private static ImageLoader instance;
	/**
	 * ͼƬ������Ķ��� keyΪͼƬ·��
	 */
	private LruCache<String, Bitmap> lruCache;

	/**
	 * Ĭ���߳�Ϊһ��
	 */
	private ExecutorService threadPool = MainActivity.threadPool;

	/**
	 * ���еĵ��ȷ�ʽ
	 */
	// private Type type = Type.LIFO;

	/**
	 * taskQuery��LinkedList�����������ʺ�Ƶ���ط����м��β��
	 */
	// private LinkedList<Runnable> taskQueue;
	/**
	 * ��̨��ѯ�߳�
	 */
	// private Thread poolThread;

	// private Handler poolThreadHandler;
	private Handler UIHandler;

	// private Semaphore semaphoreUIHandler = new Semaphore(0);
	// private Semaphore semaphoreThreadPool;

	private ImageLoader() {
		init();
	}

	/**
	 * ��ʼ������
	 * 
	 * @param threadCount
	 * @param type
	 */
	private void init() {
		// ��̨��ѯ�߳�
		/*
		 * poolThread = new Thread() {
		 * 
		 * @Override public void run() { Looper.prepare();
		 * 
		 * poolThreadHandler = new Handler() {
		 * 
		 * @Override public void handleMessage(Message msg) { // �̳߳�ȥȡ��һ���������ִ��
		 * threadPool.execute(getTask()); try { semaphoreThreadPool.acquire(); }
		 * catch (InterruptedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } }
		 * 
		 * }; //poolThreadHandler��ʼ����ϣ��ͷ��ź���
		 * semaphorePoolThreadHandler.release(); Looper.loop(); } };
		 * 
		 * poolThread.start();
		 */

		// ��ȡӦ�õ��������ڴ�
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheMemory = maxMemory / 8;

		lruCache = new LruCache<String, Bitmap>(cacheMemory) {
			protected int sizeOf(String key, Bitmap value) {
				// ����ÿ��bitmap��ռ�ݵ��ڴ棬ÿһ��ռ�ݵ��ֽ������߶�
				return value.getRowBytes() * value.getHeight();
			}
		};

		// �����̳߳�
		// taskQueue = new LinkedList<Runnable>();
		// this.type = type;

		// semaphoreThreadPool = new Semaphore(threadCount);
	}

	/**
	 * ���������ȡ��һ������
	 * 
	 * @return
	 */
	/*
	 * private Runnable getTask() {
	 * 
	 * if (type == Type.FIFO) { return taskQueue.removeFirst(); } else if (type
	 * == Type.LIFO) { return taskQueue.removeLast(); }
	 * 
	 * return null; }
	 */

	/**
	 * ����ģʽ����������ֻ����һ�����洦��
	 * 
	 * @return
	 */

	public static ImageLoader getInstance() {
		if (instance == null) {
			synchronized (ImageLoader.class) {
				if (instance == null) {
					instance = new ImageLoader();
				}
			}
		}
		return instance;
	}

	/**
	 * ����pathΪimageView����ͼƬ imageView���ظ�ʹ�õģ�Ϊ������ʾ���ң�imageView��ҪsetTag()
	 * �����������ص�ͼƬ��path��ҪΪ�գ������Զ�����byte[]data��ͬʱdata��Ϊ����imageId ��= -1.
	 * 
	 * @param path
	 * @param imageView
	 */
	public void loadImage(final boolean isCompress, final String path,
			final byte[] data, final int imageId, final ImageView imageView) {

		final boolean isInternet; // ��־����������������
		// ��������������
		if (path != null && path.length() != 0) {
			imageView.setTag(path);
			isInternet = false;
		} else {
			imageView.setTag("._NET" + imageId);
			isInternet = true;
		}
//		if (UIHandler == null) {
			UIHandler = new Handler() {
				public void handleMessage(Message msg) {
					ImgBeanHolder holder = (ImgBeanHolder) msg.obj;
					if (msg.what == 0x000) {
						ImageView imageView = holder.imageView;
						imageView.setImageResource(R.drawable.ic_launcher);
						return;
					} else if (msg.what == 0x001) {
						// ��ȡ�õ���ͼƬ������
						
						Bitmap bm = holder.bitmap;
						ImageView imageView = holder.imageView;
						String path = holder.path;
						int _id = holder.imageId;

						if (!isInternet
								&& imageView.getTag().toString().equals(path)) {
							imageView.setImageBitmap(bm);
						} else if (isInternet
								&& imageView.getTag().toString()
										.equals("._NET" + _id)) {
							imageView.setImageBitmap(bm);
						}
					}

				};
			};
//		}

		if ((path == null || path.length() == 0)
				&& (data == null || data.length == 0 || imageId == -1)) {
			Message msg = Message.obtain();
			ImgBeanHolder holder = new ImgBeanHolder();
			holder.imageView = imageView;
			msg.obj = holder;
			msg.what = 0x000;
			UIHandler.sendMessage(msg);
			return;
		}

		Bitmap bm = null;
		if (!isInternet) {
			bm = this.getBitmapFromLruCache(path);
		} else {
			bm = this.getBitmapFromLruCache("._NET" + imageId);
		}

		if (bm != null) {
			reFrashBitmap(path, data, imageId, imageView, bm);
		} else {
			// addTasks(new Runnable() {
			threadPool.execute(new Runnable() {
				@Override
				public void run() {

					// ����ͼƬ,ʵ��ͼƬ��ѹ��
					// 1�����ͼƬ��Ҫ��ʾ�Ĵ�С
					ImageSize imageSize = getImageViewSize(imageView);
					// 2��ѹ��ͼƬ
					Bitmap bm = decodeSampleBitmapFromPath(isCompress,
							imageSize.width, imageSize.height, path, data);
					// 3����ͼƬ���뵽����
					addBitmapToLruCache(path, imageId, bm);
					reFrashBitmap(path, data, imageId, imageView, bm);

					// semaphoreThreadPool.release();
				}

			});
		}
	}

	/**
	 * ��UIHandler����ImageBeanHolder���󣬲�֪ͨUI����
	 * 
	 * @param path
	 * @param imageView
	 * @param bm
	 */
	private void reFrashBitmap(final String path, byte[] data, int _id,
			final ImageView imageView, Bitmap bm) {
		Message message = Message.obtain();
		ImgBeanHolder holder = new ImgBeanHolder();
		holder.bitmap = bm;
		holder.path = path;
		holder.imageId = _id;
		holder.imageByte = data;
		holder.imageView = imageView;

		message.obj = holder;
		message.what = 0x001;
		UIHandler.sendMessage(message);

	}

	/**
	 * ��ͼƬ���뻺��LruCache
	 * 
	 * @param path
	 * @param bm
	 */
	protected void addBitmapToLruCache(String path, int imageId, Bitmap bm) {
		if (path != null && getBitmapFromLruCache(path) == null) {
			if (bm != null) {
				lruCache.put(path, bm);
			}
		} else if (imageId != -1
				&& getBitmapFromLruCache("_NET" + imageId) == null) {

			if (bm != null) {
				lruCache.put("_NET" + imageId, bm);
			}

		}
	}

	/**
	 * ����ͼƬ��Ҫ��ʾ�Ŀ�͸߽���ѹ�� ���path �� dataͬʱ��Ϊ�գ���data��Ч�����ͬʱΪ�շ���null
	 * 
	 * @param width
	 * @param height
	 * @param path
	 * @return
	 */
	protected Bitmap decodeSampleBitmapFromPath(boolean isCompress, int width,
			int height, String path, byte[] data) {

		if ((path == null || path.length() == 0)
				&& (data == null || data.length == 0)) {
			return null;
		}

		BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;

		if (path != null && path.length() != 0) {
			// Ϊ��ȡͼƬ�Ŀ�͸ߣ�������ͼƬ���ص��ڴ���,��Ҫ����injustDecodeBoundΪtrue
			BitmapFactory.decodeFile(path, options); // option��ÿ��

		} else {
			BitmapFactory.decodeByteArray(data, 0, data.length, options);
		}

		// �����ѹ��������ѹ��������ԭ������ʾ
		if (isCompress) {
			options.inSampleSize = caculateInSampleSize(options, width, height);
		} else {
			options.inSampleSize = 1;
		}

		// ʹ�û�õ�InSampleSize�ٴν���ͼƬ����ͼƬ���ص��ڴ�
		options.inJustDecodeBounds = false;

		if (path != null && path.length() != 0) {
			return BitmapFactory.decodeFile(path, options);
		} else {
			return BitmapFactory.decodeByteArray(data, 0, data.length, options);

			// ������
			/*InputStream input = new ByteArrayInputStream(data);
			SoftReference softRef = new SoftReference(
					BitmapFactory
							.decodeByteArray(data, 0, data.length, options));

			try {
				if (data != null) {
					data = null;
				}
				if (input != null) {
					input.close();
				}
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return (Bitmap) softRef.get();*/
		}
	}

	/**
	 * ��������Ŀ�͸��Լ�ʵ�ʵĿ�͸߼���sampleSize,��ѹ����
	 * 
	 * @param options
	 * @param width
	 * @param height
	 * @return
	 */
	private int caculateInSampleSize(Options options, int reqWidth,
			int reqHeight) {

		int width = options.outWidth;
		int height = options.outHeight;

		int inSampleSize = 1;

		if (width > reqWidth || height > reqHeight) {
			int widthRadio = Math.round(width * 1.0f / reqWidth);
			int heightRadio = Math.round(height * 1.0f / reqHeight);

			inSampleSize = Math.max(widthRadio, heightRadio);
		}
		return inSampleSize;
	}

	/**
	 * ����ImageVIew��ȡ�ʵ��Ŀ�� ����ǹ̶�ֵ����ֱ�ӻ�ȡͼƬ��� ���������wrap_content�ȣ�����ҪgetLayoutParams
	 * 
	 * @param imageView
	 * @return
	 */
	protected ImageSize getImageViewSize(ImageView imageView) {

		ImageSize imageSize = new ImageSize();

		DisplayMetrics metrics = imageView.getContext().getResources()
				.getDisplayMetrics();

		LayoutParams ip = imageView.getLayoutParams();

		int width = imageView.getWidth(); // ��ȡʵ�ʿ��
		if (width <= 0) {
			width = ip.width; // ��ȡ��layout�������Ŀ��
		}

		if (width <= 0) {
			width = getImageFieldValue(imageView, "mMaxWidth");
			; // ������ֵ
		}

		if (width <= 0) {
			width = metrics.widthPixels;
		}

		int height = imageView.getWidth(); // ��ȡʵ�ʸ߶�
		if (height <= 0) {
			height = ip.width; // ��ȡ��layout�������Ŀ��
		}

		if (height <= 0) {
			height = getImageFieldValue(imageView, "mMaxHeight"); // ������ֵ
		}

		if (height <= 0) {
			height = metrics.heightPixels;
		}

		imageSize.height = height;

		imageSize.width = width;

		return imageSize;
	}

	/**
	 * ͨ��������ĳ������ֵ
	 * 
	 * @param object
	 * @return
	 */
	private static int getImageFieldValue(Object object, String fieldName) {
		int value = 0;

		try {
			Field field = ImageView.class.getDeclaredField(fieldName);
			field.setAccessible(true);
			int fieldValue = field.getInt(object);
			if (fieldValue > 0 && fieldValue < Integer.MAX_VALUE) {
				value = fieldValue;
			}
		} catch (NoSuchFieldException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return value;
	}

	/**
	 * ���������У�����ͼƬѹ���ĺ�ʱ����
	 * 
	 * @param runnable
	 */

	/*
	 * private synchronized void addTasks(Runnable runnable) {
	 * taskQueue.add(runnable);
	 * 
	 * try { if(poolThreadHandler == null) {
	 * semaphorePoolThreadHandler.acquire(); } } catch (InterruptedException e)
	 * { // TODO Auto-generated catch block e.printStackTrace(); }
	 * 
	 * poolThreadHandler.sendEmptyMessage(0x110); }
	 */

	/**
	 * ����key�ڻ����л�ȡbitmap
	 * 
	 * @param key
	 * @return
	 */
	private Bitmap getBitmapFromLruCache(String key) {
		return lruCache.get(key);
	}
	
	/**
	 * �Ƴ�����
	 * @param key
	 */
	public synchronized void removeImageCache(String key){
		if(key != null) {
			if(lruCache != null) {
				Bitmap bm = lruCache.remove(key);
				if(bm != null) {
					bm.recycle();
				}
			}
		}
	}
	
	/**
	 * ������
	 * @author ShouLun
	 *
	 */
	public void clearCache() {
		if(lruCache != null) {
			if(lruCache.size() > 0) {
				lruCache.evictAll();
			}
		}
		lruCache = null;
	}
	private class ImageSize {
		int width;
		int height;
	}

	private class ImgBeanHolder {
		Bitmap bitmap;
		ImageView imageView;
		String path;
		byte[] imageByte;
		int imageId;
	}
}
