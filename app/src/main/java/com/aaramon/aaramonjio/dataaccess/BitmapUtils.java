package com.aaramon.aaramonjio.dataaccess;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.media.ExifInterface;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

import static android.os.Build.VERSION.SDK_INT;

/**
 * Utility class for Bitmap related methods.
 * 
 * @author kapil.vij
 */
public abstract class BitmapUtils {

	private static String LOG_TAG	= "BitmapUtils";

	/**
	 * @param pSourceBmp
	 * @param pNewWidth
	 * @param pNewHeight
	 * @return scaled bitmap, which is not less than required minimum dimensions
	 */
	public static Bitmap getScaledBitmap(Bitmap pSourceBmp, int pNewWidth, int pNewHeight, boolean pIsUniform) {
		if (pSourceBmp == null || pNewWidth <= 0 || pNewHeight <= 0) {
			return null;
		}
		int actualWidth = pSourceBmp.getWidth();
		int actualHeight = pSourceBmp.getHeight();

		/**
		 * Check if scaling is not required.
		 */
		if (actualWidth == pNewWidth && actualHeight == pNewHeight) {
			return pSourceBmp;
		}

		int scalingToWidth, scalingToHeight;

		/**
		 * If uniform scaling is required, then scaled Bitmap might not be of
		 * pNewWidth and pNewHeight. Any one dimension will be as per respective
		 * new dimension and other will be >= respective new dimension
		 */
		if (pIsUniform) {
			// If scaling as per pNewWidth will be enough
			scalingToWidth = pNewWidth;
			scalingToHeight = actualHeight * scalingToWidth / actualWidth;

			// If scaling as per pNewHeight will be enough
			if (scalingToHeight < pNewHeight) {
				scalingToHeight = pNewHeight;
				scalingToWidth = actualWidth * scalingToHeight / actualHeight;
			}
		} else {
			scalingToWidth = pNewWidth;
			scalingToHeight = pNewHeight;
		}

		try {
			return Bitmap.createScaledBitmap(pSourceBmp, scalingToWidth, scalingToHeight, true);
		} catch (Throwable tr) {
			Log.e(LOG_TAG, "Error in scaling upto required width and/or height", tr);
			return null;
		}
	}

	public static Bitmap getScaledBitmap(File f, int maxImageSize) {
		System.gc();

		FileInputStream fstream = null;
		FileDescriptor fd = null;
		try {
			fstream = new FileInputStream(f);
			fd = fstream.getFD();
			if (fd != null) {
				return BitmapUtils.decodeSampledBitmapFromDescriptor(fd, maxImageSize);
			} else {
				return BitmapUtils.decodeSampledBitmapFromStream(fstream, maxImageSize);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (fstream != null) {
				try {
					fstream.close();
				} catch (IOException e) {
				}
			}
		}
		return null;
	}

	public static Bitmap decodeSampledBitmapFromStream(InputStream stream, int maxImageSize) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inDither = false; // Disable Dithering mode
		options.inPurgeable = true; // Tell to gc that whether it needs free
									// memory, the Bitmap can be cleared
		options.inInputShareable = true; // Which kind of reference will be used
											// to recover the Bitmap data after
											// being clear, when it will be used
											// in the future
		options.inTempStorage = new byte[32 * 1024];
		BitmapFactory.decodeStream(stream, null, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, maxImageSize);

		// If we're running on Honeycomb or newer, try to use inBitmap
		if (SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			addInBitmapOptions(options);
		}

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(stream, null, options);
	}

	public static Bitmap decodeSampledBitmapFromStream(InputStream stream, int reqWidth, int reqHeight) {

		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inDither = false; // Disable Dithering mode
		options.inPurgeable = true; // Tell to gc that whether it needs free
									// memory, the Bitmap can be cleared
		options.inInputShareable = true; // Which kind of reference will be used
											// to recover the Bitmap data after
											// being clear, when it will be used
											// in the future
		options.inTempStorage = new byte[32 * 1024];
		BitmapFactory.decodeStream(stream, null, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

		// If we're running on Honeycomb or newer, try to use inBitmap
		if (SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			addInBitmapOptions(options);
		}

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(stream, null, options);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	private static void addInBitmapOptions(BitmapFactory.Options options) {
		// inBitmap only works with mutable bitmaps so force the decoder to
		// return mutable bitmaps.
		options.inMutable = true;
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		if (height > reqHeight || width > reqWidth) {

			final int halfHeight = height / 2;
			final int halfWidth = width / 2;

			// Calculate the largest inSampleSize value that is a power of 2 and
			// keeps both
			// height and width larger than the requested height and width.
			while ((halfHeight / inSampleSize) > reqHeight && (halfWidth / inSampleSize) > reqWidth) {
				inSampleSize *= 2;
			}

			// This offers some additional logic in case the image has a strange
			// aspect ratio. For example, a panorama may have a much larger
			// width than height. In these cases the total pixels might still
			// end up being too large to fit comfortably in memory, so we should
			// be more aggressive with sample down the image (=larger
			// inSampleSize).
			long totalPixels = halfHeight * halfWidth / inSampleSize;

			// Anything more than 2x the requested pixels we'll sample down
			// further
			final long totalReqPixelsCap = reqWidth * reqHeight * 2;

			while (totalPixels > totalReqPixelsCap) {
				inSampleSize *= 2;
				totalPixels /= 2;
			}
		}
		return inSampleSize;
	}

	public static Bitmap decodeSampledBitmapFromDescriptor(FileDescriptor fileDescriptor, int maxFileSize) {
		// First decode with inJustDecodeBounds=true to check dimensions
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inDither = false; // Disable Dithering mode
		options.inPurgeable = true; // Tell to gc that whether it needs free
									// memory, the Bitmap can be cleared
		options.inInputShareable = true; // Which kind of reference will be used
											// to recover the Bitmap data after
											// being clear, when it will be used
											// in the future
		options.inTempStorage = new byte[32 * 1024];
		options.inPreferredConfig = Bitmap.Config.ARGB_8888;
		BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);

		// Calculate inSampleSize
		options.inSampleSize = calculateInSampleSize(options, maxFileSize);

		// Decode bitmap with inSampleSize set
		options.inJustDecodeBounds = false;

		// If we're running on Honeycomb or newer, try to use inBitmap
		if (SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			addInBitmapOptions(options);
		}

		return BitmapFactory.decodeFileDescriptor(fileDescriptor, null, options);
	}

	public static int calculateInSampleSize(BitmapFactory.Options options, int maxImageSize) {
		// Raw height and width of image
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 1;

		inSampleSize = (int) Math.pow(2.0, (int) Math.round(Math.log(maxImageSize / (double) Math.max(height, width)) / Math.log(0.5)));

		return inSampleSize;
	}

	public static int getMaxSize(Context context) {
		int screenWidth = context.getResources().getDisplayMetrics().widthPixels;
		int maxSize = Math.max((screenWidth - 75), ((int) (screenWidth * 0.7f)));
		return maxSize;
	}

	/**
	 * @param pSourceBmp
	 * @param pNewWidth
	 * @param pNewHeight
	 * @return
	 */
	public static Bitmap getScaledCroppedBitmap(Bitmap pSourceBmp, int pNewWidth, int pNewHeight) {
		Bitmap uniScaledBitmap = getScaledBitmap(pSourceBmp, pNewWidth, pNewHeight, true);
		if (uniScaledBitmap == null) {
			return null;
		}
		int currentWidth = uniScaledBitmap.getWidth();
		int currentHeight = uniScaledBitmap.getHeight();
		int cropDiff;

		/**
		 * Crop from left and right edges
		 */
		if (currentWidth > pNewWidth) {
			cropDiff = currentWidth - pNewWidth;
			try {
				return Bitmap.createBitmap(uniScaledBitmap, cropDiff / 2, 0, currentWidth - cropDiff, currentHeight);
			} catch (Throwable tr) {
				Log.e(LOG_TAG, "Error in horizontal cropping", tr);
				return null;
			}
		}

		/**
		 * Crop from top and bottom edges
		 */
		if (currentHeight > pNewHeight) {
			cropDiff = currentHeight - pNewHeight;
			try {
				return Bitmap.createBitmap(uniScaledBitmap, 0, cropDiff / 2, currentWidth, currentHeight - cropDiff);
			} catch (Throwable tr) {
				Log.e(LOG_TAG, "Error in horizontal cropping", tr);
				return null;
			}
		}

		return uniScaledBitmap;
	}

	/**
	 * @param pSourceBmp
	 * @param radiusX
	 * @return
	 */
	public static Bitmap getRoundedCornerBitmap(Bitmap pSourceBmp, int radiusX, int radiusY) {
		if (pSourceBmp == null || radiusX <= 0 || radiusY <= 0) {
			return pSourceBmp;
		}
		Bitmap outputBmp = null;

		try {

			outputBmp = Bitmap.createBitmap(pSourceBmp.getWidth(), pSourceBmp.getHeight(), Config.ARGB_8888);

			Canvas canvas = new Canvas(outputBmp);

			int color = 0xffffffff;
			Paint paint = new Paint();
			Rect rect = new Rect(0, 0, pSourceBmp.getWidth(), pSourceBmp.getHeight());
			RectF rectF = new RectF(rect);
			float roundPx = radiusX;

			paint.setAntiAlias(true);

			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(pSourceBmp, rect, rect, paint);
		} catch (Throwable tr) {
			Log.e(LOG_TAG, "Error in creating rounded bitmap", tr);
		}

		return outputBmp;
	}

	/**
	 * @param pSourceBmp
	 * @return
	 */
	public static Bitmap getRoundedCircularBitmap(Bitmap pSourceBmp, int radius) {
		if (pSourceBmp == null || radius <= 0) {
			return pSourceBmp;
		}
		Bitmap outputBmp = null;

		try {

			outputBmp = Bitmap.createBitmap(pSourceBmp.getWidth(), pSourceBmp.getHeight(), Config.ARGB_8888);

			Canvas canvas = new Canvas(outputBmp);

			int color = 0xffffffff;
			Paint paint = new Paint();
			Rect rect = new Rect(0, 0, pSourceBmp.getWidth(), pSourceBmp.getHeight());
			paint.setAntiAlias(true);
			canvas.drawARGB(0, 0, 0, 0);
			paint.setColor(color);
			canvas.drawCircle(rect.centerX(), rect.centerY(), radius, paint);
			paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
			canvas.drawBitmap(pSourceBmp, rect, rect, paint);
		} catch (Throwable tr) {
			Log.e(LOG_TAG, "Error in creating rounded bitmap", tr);
		}

		return outputBmp;
	}

	/**
	 * @param pView
	 * @param pWidth
	 * @param pHeight
	 * @return
	 */
	public static Bitmap getBitmapFromView(View pView, int pWidth, int pHeight) {
		int widthMeasureSpec = MeasureSpec.makeMeasureSpec(pWidth, MeasureSpec.EXACTLY);
		int heightMeasureSpec = MeasureSpec.makeMeasureSpec(pHeight, MeasureSpec.EXACTLY);

		pView.measure(widthMeasureSpec, heightMeasureSpec);
		pView.layout(0, 0, pView.getMeasuredWidth(), pView.getMeasuredHeight());

		pView.setDrawingCacheEnabled(true);
		try {
			return pView.getDrawingCache();
		} catch (Throwable tr) {
			Log.e(LOG_TAG, "Error in getDrawingCache", tr);
			return null;
		}
	}

	public static Bitmap getCroppedBitmap(Bitmap bitmap, int margin) {
		int rad = bitmap.getWidth() <= bitmap.getHeight() ? bitmap.getWidth() : bitmap.getHeight();
		Bitmap output = Bitmap.createBitmap(rad, rad, Config.ARGB_8888);
		Canvas canvas = new Canvas(output);

		final int color = 0xff424242;
		final Paint paint = new Paint();

		final Rect rect = new Rect(0, 0, rad, rad);

		paint.setAntiAlias(true);
		canvas.drawARGB(0, 0, 0, 0);
		paint.setColor(color);

		canvas.drawCircle(rad / 2, rad / 2, rad / 2 - margin, paint);

		paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
		canvas.drawBitmap(bitmap, rect, rect, paint);

		return output;
	}

	/**
	 * @param imageBitmap
	 * @return compressed bytes of imageBitmap
	 */
	public static byte[] getPNGImageData(Bitmap imageBitmap) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		boolean isSuccess = imageBitmap.compress(CompressFormat.PNG, 0, os);
		byte[] imageData = null;
		if (isSuccess) {
			imageData = os.toByteArray();
		}
		try {
			os.close();
		} catch (Exception e) { /* ignore */
		}
		return imageData;
	}

	/**
	 * @param pSourceBmp
	 * @return resized imageBitmap
	 */
	public static Bitmap resizeToFitInside(Bitmap pSourceBmp, int pMaxWidth, int pMaxHeight) {
		int imageWidth = pSourceBmp.getWidth();
		int imageHeight = pSourceBmp.getHeight();
		if (imageWidth < pMaxWidth && imageHeight < pMaxHeight) {
			return pSourceBmp;
		}
		double widthRatio = (double) pMaxWidth / imageWidth;
		double heightRatio = (double) pMaxWidth / imageHeight;
		if (widthRatio > heightRatio) {
			pMaxWidth = (int) (imageWidth * heightRatio);
		} else {
			pMaxHeight = (int) (imageHeight * widthRatio);
		}
		try {
			return Bitmap.createScaledBitmap(pSourceBmp, pMaxWidth, pMaxHeight, true);
		} catch (Throwable tr) {
			Log.e(LOG_TAG, "Error in scaling upto required width and/or height", tr);
			return null;
		}
	}

	/**
	 * @param originalBmp
	 * @param diameter
	 * @return
	 */
	public static Bitmap getCircularCenterCropBitmap(Bitmap originalBmp, int diameter) {
		Bitmap resizedBmp = BitmapUtils.getScaledCroppedBitmap(originalBmp, diameter, diameter);
		Bitmap circularBmp = BitmapUtils.getRoundedCircularBitmap(resizedBmp, diameter / 2);
		return circularBmp;
	}

	public static Bitmap getOrientatedScaledBitmap(File file, Context context) throws IOException {
		int maxImageSize = getMaxSize(context);
		Bitmap sourceBitmap = getScaledBitmap(file, maxImageSize);

		ExifInterface exif = new ExifInterface(file.getPath());
		int orientation = exif.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
		Matrix matrix = new Matrix();
		switch (orientation) {
		case ExifInterface.ORIENTATION_ROTATE_90:
			matrix.postRotate(90);
			break;
		case ExifInterface.ORIENTATION_ROTATE_180:
			matrix.postRotate(180);
			break;
		case ExifInterface.ORIENTATION_ROTATE_270:
			matrix.postRotate(270);
			break;
		}

		return Bitmap.createBitmap(sourceBitmap, 0, 0, sourceBitmap.getWidth(), sourceBitmap.getHeight(), matrix, true);
	}
}
