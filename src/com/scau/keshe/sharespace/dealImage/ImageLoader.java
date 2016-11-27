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
	 * 图片缓存核心对象 key为图片路径
	 */
	private LruCache<String, Bitmap> lruCache;

	/**
	 * 默认线程为一个
	 */
	private ExecutorService threadPool = MainActivity.threadPool;

	/**
	 * 队列的调度方式
	 */
	// private Type type = Type.LIFO;

	/**
	 * taskQuery，LinkedList采用链表，且适合频繁地访问中间和尾部
	 */
	// private LinkedList<Runnable> taskQueue;
	/**
	 * 后台轮询线程
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
	 * 初始化操作
	 * 
	 * @param threadCount
	 * @param type
	 */
	private void init() {
		// 后台轮询线程
		/*
		 * poolThread = new Thread() {
		 * 
		 * @Override public void run() { Looper.prepare();
		 * 
		 * poolThreadHandler = new Handler() {
		 * 
		 * @Override public void handleMessage(Message msg) { // 线程池去取出一个任务进行执行
		 * threadPool.execute(getTask()); try { semaphoreThreadPool.acquire(); }
		 * catch (InterruptedException e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } }
		 * 
		 * }; //poolThreadHandler初始化完毕，释放信号量
		 * semaphorePoolThreadHandler.release(); Looper.loop(); } };
		 * 
		 * poolThread.start();
		 */

		// 获取应用的最大可用内存
		int maxMemory = (int) Runtime.getRuntime().maxMemory();
		int cacheMemory = maxMemory / 8;

		lruCache = new LruCache<String, Bitmap>(cacheMemory) {
			protected int sizeOf(String key, Bitmap value) {
				// 返回每个bitmap所占据的内存，每一行占据的字节数×高度
				return value.getRowBytes() * value.getHeight();
			}
		};

		// 创建线程池
		// taskQueue = new LinkedList<Runnable>();
		// this.type = type;

		// semaphoreThreadPool = new Semaphore(threadCount);
	}

	/**
	 * 从任务队列取出一个方法
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
	 * 单例模式，整个程序只能有一个缓存处理
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
	 * 根据path为imageView设置图片 imageView是重复使用的，为避免显示错乱，imageView需要setTag()
	 * 如果是网络加载的图片，path需要为空，否则自动忽略byte[]data；同时data不为空且imageId ！= -1.
	 * 
	 * @param path
	 * @param imageView
	 */
	public void loadImage(final boolean isCompress, final String path,
			final byte[] data, final int imageId, final ImageView imageView) {

		final boolean isInternet; // 标志加载网络数据命令
		// 错误数据输入检测
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
						// 获取得到的图片并设置
						
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

					// 加载图片,实现图片的压缩
					// 1、获得图片需要显示的大小
					ImageSize imageSize = getImageViewSize(imageView);
					// 2、压缩图片
					Bitmap bm = decodeSampleBitmapFromPath(isCompress,
							imageSize.width, imageSize.height, path, data);
					// 3、把图片加入到缓存
					addBitmapToLruCache(path, imageId, bm);
					reFrashBitmap(path, data, imageId, imageView, bm);

					// semaphoreThreadPool.release();
				}

			});
		}
	}

	/**
	 * 向UIHandler发送ImageBeanHolder对象，并通知UI更新
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
	 * 将图片加入缓存LruCache
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
	 * 根据图片需要显示的宽和高进行压缩 如果path 和 data同时不为空，则data无效；如果同时为空返回null
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
			// 为获取图片的宽和高，并不把图片加载到内存中,需要设置injustDecodeBound为true
			BitmapFactory.decodeFile(path, options); // option获得宽高

		} else {
			BitmapFactory.decodeByteArray(data, 0, data.length, options);
		}

		// 如果有压缩命令则压缩，否则按原比例显示
		if (isCompress) {
			options.inSampleSize = caculateInSampleSize(options, width, height);
		} else {
			options.inSampleSize = 1;
		}

		// 使用获得的InSampleSize再次解析图片并把图片加载到内存
		options.inJustDecodeBounds = false;

		if (path != null && path.length() != 0) {
			return BitmapFactory.decodeFile(path, options);
		} else {
			return BitmapFactory.decodeByteArray(data, 0, data.length, options);

			// 软引用
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
	 * 根据需求的宽和高以及实际的宽和高计算sampleSize,即压缩比
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
	 * 根据ImageVIew获取适当的宽高 如果是固定值，可直接获取图片宽高 如果设置上wrap_content等，则需要getLayoutParams
	 * 
	 * @param imageView
	 * @return
	 */
	protected ImageSize getImageViewSize(ImageView imageView) {

		ImageSize imageSize = new ImageSize();

		DisplayMetrics metrics = imageView.getContext().getResources()
				.getDisplayMetrics();

		LayoutParams ip = imageView.getLayoutParams();

		int width = imageView.getWidth(); // 获取实际宽度
		if (width <= 0) {
			width = ip.width; // 获取在layout中声明的宽度
		}

		if (width <= 0) {
			width = getImageFieldValue(imageView, "mMaxWidth");
			; // 检查最大值
		}

		if (width <= 0) {
			width = metrics.widthPixels;
		}

		int height = imageView.getWidth(); // 获取实际高度
		if (height <= 0) {
			height = ip.width; // 获取在layout中声明的宽度
		}

		if (height <= 0) {
			height = getImageFieldValue(imageView, "mMaxHeight"); // 检查最大值
		}

		if (height <= 0) {
			height = metrics.heightPixels;
		}

		imageSize.height = height;

		imageSize.width = width;

		return imageSize;
	}

	/**
	 * 通过反射获得某个属性值
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
	 * 添加任务队列，进行图片压缩的耗时操作
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
	 * 根据key在缓存中获取bitmap
	 * 
	 * @param key
	 * @return
	 */
	private Bitmap getBitmapFromLruCache(String key) {
		return lruCache.get(key);
	}
	
	/**
	 * 移除缓存
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
	 * 清理缓存
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
