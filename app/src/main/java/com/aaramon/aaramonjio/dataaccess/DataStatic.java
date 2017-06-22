package com.aaramon.aaramonjio.dataaccess;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.util.Base64;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class DataStatic {

//	public static StoreInfoModel sm = new StoreInfoModel();

	public static int ACTIVITY_SELECT_IMAGE = 1001;
	public static int MEDIA_TYPE_IMAGE = 1;
	public static int ACTIVITY_CAMERA_IMAGE = 1003;
	public static Set<String> setProductId = new HashSet<String>();
	public static Set<String> setUncheckedProductId = new HashSet<String>();
	
	
	
	
	public static ArrayList<String> setProductIdNew = new ArrayList<String>();
	public static ArrayList<String> setUncheckedProductIdNew = new ArrayList<String>();
	public static ArrayList<String> setProductSellingPriceNew = new ArrayList<String>();
	public static ArrayList<String> setProductPriceNew = new ArrayList<String>();
	

	public static String getDataColumn(Context context, Uri uri,
                                       String selection, String[] selectionArgs) {

		Cursor cursor = null;
		final String column = "_data";
		final String[] projection = { column };

		try {
			cursor = context.getContentResolver().query(uri, projection,
					selection, selectionArgs, null);
			if (cursor != null && cursor.moveToFirst()) {
				final int column_index = cursor.getColumnIndexOrThrow(column);
				return cursor.getString(column_index);
			}
		} finally {
			if (cursor != null)
				cursor.close();
		}
		return null;
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is ExternalStorageProvider.
	 */
	public static boolean isExternalStorageDocument(Uri uri) {
		return "com.android.externalstorage.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is DownloadsProvider.
	 */
	public static boolean isDownloadsDocument(Uri uri) {
		return "com.android.providers.downloads.documents".equals(uri
				.getAuthority());
	}

	/**
	 * @param uri
	 *            The Uri to check.
	 * @return Whether the Uri authority is MediaProvider.
	 */
	public static boolean isMediaDocument(Uri uri) {
		return "com.android.providers.media.documents".equals(uri
				.getAuthority());
	}

	// ******************Test Internet Connection********************

	public static boolean isInternetAvailable(Context context) {
		boolean isAvailable = false;
		ConnectivityManager cm = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] networkInfo = cm.getAllNetworkInfo();
		for (NetworkInfo netInfo : networkInfo) {
			if (netInfo.getTypeName().equalsIgnoreCase("WIFI")) {
				if (netInfo.isConnected()) {
					isAvailable = true;
				}
			} else if (netInfo.getTypeName().equalsIgnoreCase("MOBILE")) {
				if (netInfo.isConnected()) {
					isAvailable = true;
				}
			}
		}
		return isAvailable;
	}
	// public static ActivityOptionsCompat makeSceneTransitionAnimation(Activity
	// activity,
	// View sharedElement, String sharedElementName) {
	// if (Build.VERSION.SDK_INT >= 21) {
	// return new ActivityOptionsCompat.ActivityOptionsImpl21(
	// ActivityOptionsCompat21.makeSceneTransitionAnimation(activity,
	// sharedElement, sharedElementName));
	// }
	// return new ActivityOptionsCompat();
	// }
	
	 public static String ShrinkBitmap(String file, int width, int height) {

	        BitmapFactory.Options bmpFactoryOptions = new BitmapFactory.Options();
	        bmpFactoryOptions.inJustDecodeBounds = true;
	        Bitmap bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);

	        int heightRatio = (int) Math.ceil(bmpFactoryOptions.outHeight / (float) height);
	        int widthRatio = (int) Math.ceil(bmpFactoryOptions.outWidth / (float) width);

	        if (heightRatio > 1 || widthRatio > 1) {
	            if (heightRatio > widthRatio) {
	                bmpFactoryOptions.inSampleSize = heightRatio;
	            } else {
	                bmpFactoryOptions.inSampleSize = widthRatio;
	            }
	        }

	        bmpFactoryOptions.inJustDecodeBounds = false;
	        bitmap = BitmapFactory.decodeFile(file, bmpFactoryOptions);
	        
	        
	        ByteArrayOutputStream baos=new ByteArrayOutputStream();
	        bitmap.compress(Bitmap.CompressFormat.PNG,100, baos);
	        byte [] b=baos.toByteArray();
	        String temp= Base64.encodeToString(b, Base64.DEFAULT);
	        return temp;
	    }

}
