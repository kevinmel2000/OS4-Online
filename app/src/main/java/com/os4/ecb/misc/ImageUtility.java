package com.os4.ecb.misc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import android.content.Context;
import android.database.Cursor;
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
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import com.os4.ecb.Constant;
import com.os4.ecb.beans.FileTransferInfo;

public class ImageUtility {

	public static void saveImageFile(FileTransferInfo fileTransferInfo){
		File file = new File(Constant.IMAGES_FILE_PATH,fileTransferInfo.getName());
		try{
			FileOutputStream out = new FileOutputStream(file);
			byte[] buffer = Base64.decode(fileTransferInfo.getBase64());
			out.write(buffer);
			out.close();
		}catch(Exception e){
			file.delete();
			e.printStackTrace();
		}
	}

	public static String saveImageFile(String base64){
		File file = new File(Constant.IMAGES_FILE_PATH,"IMG_"+Format.formatFileDate()+".jpg");
		try{
			FileOutputStream out = new FileOutputStream(file);
			byte[] buffer = Base64.decode(base64);
			out.write(buffer);
			out.close();
		}catch(Exception e){
			file.delete();
			e.printStackTrace();
		}
		return "file://"+file.getAbsolutePath();
	}

	public static String saveImageFile(Bitmap bm){
		File file = new File(Constant.IMAGES_FILE_PATH,"IMG_"+Format.formatFileDate()+".jpg");
		try{
			FileOutputStream out = new FileOutputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			boolean ret = bm.compress(CompressFormat.JPEG, 85 /*ignored for PNG*/, bos);
			byte[] buffer = bos.toByteArray();
			bos.flush();
			bos.close();
			out.write(buffer);
			out.close();
		}catch(Exception e){
			file.delete();
			e.printStackTrace();
		}
		return "file://"+file.getAbsolutePath();
	}

	public static String createThumbnailImageFile(FileTransferInfo fileTransferInfo){
		File file = new File(Constant.IMAGES_FILE_PATH,fileTransferInfo.getName());
		return createThumbnailImageFile(file);
	}

	public static String createThumbnailImageFile(String filename){
		File file = new File(Uri.parse(filename).getPath());
		return createThumbnailImageFile(file);
	}

	public static String createThumbnailImageFile(File imageFile){
		File file = new File(Constant.THUMBNAILS_PATH,imageFile.getName());
		if(file.exists()) return "file://"+file.getAbsolutePath();

		try{
			FileOutputStream out = new FileOutputStream(file);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			Bitmap bm = BitmapFactory.decodeFile(imageFile.getPath());
			int x = bm.getWidth();int y = bm.getHeight();int z = (int) imageFile.length();

			/* Portrait */
			if (y>x) {
				Bitmap bmo = Bitmap.createScaledBitmap(bm, 470, 600, false);
				if(z>1024*1024) {
					boolean ret = bmo.compress(CompressFormat.JPEG, 25 /*ignored for PNG*/, bos);
				}else if(imageFile.length()<512*512){
					boolean ret = bm.compress(CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
				}else{
					boolean ret = bm.compress(CompressFormat.JPEG, 50 /*ignored for PNG*/, bos);
				}
			}
			/* Landscape */
			else{
				Bitmap bmo = Bitmap.createScaledBitmap(bm, 600, 470, false);
				if(z>1024*1024) {
					boolean ret = bmo.compress(CompressFormat.JPEG, 25 /*ignored for PNG*/, bos);
				}else if(imageFile.length()<512*512){
					boolean ret = bm.compress(CompressFormat.JPEG, 100 /*ignored for PNG*/, bos);
				}else{
					boolean ret = bm.compress(CompressFormat.JPEG, 50 /*ignored for PNG*/, bos);
				}
			}

			byte[] buffer = bos.toByteArray();
			bos.flush();
			bos.close();
			out.write(buffer);
			out.close();
		}catch(Exception e){
			file.delete();
			e.printStackTrace();
		}
		return "file://"+file.getAbsolutePath();
	}

	public static Bitmap getFileBitmap(String localPath, String fileName){
		if(fileName==null || fileName.length()==0) return null;
		File sdcardPath = Environment.getExternalStorageDirectory();
		File beetroidPath = new File(sdcardPath,localPath);
		File file = new File(beetroidPath,fileName);		
		try{
	        Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
	        return bm;
		}catch(Exception e){
			e.printStackTrace();
        	return null;
		}
	}
	
	public static String getFileBase64(Context context,String localPath, String fileName){
		File sdcardPath = Environment.getExternalStorageDirectory();
		File beetroidPath = new File(sdcardPath,localPath);
		File file = new File(beetroidPath,fileName);
		try{
	        Bitmap bm = BitmapFactory.decodeFile(file.getAbsolutePath());
	        if(bm!=null)
	        	return toBase64(bm,10);
	        else
	        	return null;
		}catch(Exception e){
			e.printStackTrace();
        	return null;
		}
	}

	public static String toBase64(String attachment){
		try{
			ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
			byte[] buffer = bos.toByteArray();
			String base64 = Base64.encodeBytes(buffer);
			bos.close();	
			return base64;
		}catch(Exception e){e.printStackTrace();return null;}
	}
	
	public static String toBase64(Bitmap bm){
		return toBase64(bm,0);
	}
	
	public static String toBase64(Bitmap bm,int persent){
		try{
			ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
			boolean ret = bm.compress(CompressFormat.JPEG, persent /*ignored for PNG*/, bos);
			if(ret){
				byte[] buffer = bos.toByteArray();
				String base64 = Base64.encodeBytes(buffer);
				return base64;
			}
			bos.close();		
			return null;
		}catch(Exception e){e.printStackTrace();return null;}
	}	

    public static String digetsCode(String in,String typeDigest) {
        MessageDigest digest;
        try {
            digest = MessageDigest.getInstance(typeDigest);
            digest.reset();
            digest.update(in.getBytes());
            byte[] a = digest.digest();
            int len = a.length;
            StringBuilder sb = new StringBuilder(len << 1);
            for (int i = 0; i < len; i++) {
                sb.append(Character.forDigit((a[i] & 0xf0) >> 4, 16));
                sb.append(Character.forDigit(a[i] & 0x0f, 16));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) { e.printStackTrace(); }
        return null;
    }    
	
	public static Bitmap toCompress(Bitmap bm,int persent){
		try{
			ByteArrayOutputStream bos = new ByteArrayOutputStream(); 
			boolean ret = bm.compress(CompressFormat.JPEG, persent /*ignored for PNG*/, bos); 
			if(ret){
				byte[] buffer = bos.toByteArray();
				bm = BitmapFactory.decodeByteArray(buffer, 0, buffer.length);
				return bm;
			}
			bos.close();		
			return null;
		}catch(Exception e){e.printStackTrace();return null;}
	}		
	
	public static Bitmap getRoundedCornerBitmap(Bitmap bitmap) {
		bitmap = ImageUtility.getSquareBitmap(bitmap);
	    Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),bitmap.getHeight(), Config.ARGB_8888);
	    Canvas canvas = new Canvas(output);
	    
	    final int color = 0xff424242;
	    final Paint paint = new Paint();
	    final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
	    final RectF rectF = new RectF(rect);
	    final float roundPx = 10;

	    paint.setAntiAlias(true);
	    canvas.drawARGB(0, 0, 0, 0);
	    paint.setColor(color);
	    canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

	    paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));
	    canvas.drawBitmap(bitmap, rect, rect, paint);

	    return output;
	  }

	public static Bitmap getSquareBitmap(Bitmap bitmap){

//		Matrix matrix = new Matrix();
//		matrix.postScale(0.5f, 0.5f);
//		Bitmap croppedBitmap = Bitmap.createBitmap(bitmap, 100, 100, bitmap., 100, matrix, true);
//		return croppedBitmap;
		
		int sourceWidth = bitmap.getWidth();
	    int sourceHeight = bitmap.getHeight();
	    int squareSize;
	    if(sourceWidth == sourceHeight){
	    	return bitmap;
	    }else if(sourceWidth < sourceHeight){
	    	squareSize = sourceWidth;
	    }else{
	    	squareSize = sourceHeight;	    	
	    }
	 
	    // Compute the scaling factors to fit the new height and width, respectively.
	    // To cover the final image, the final scaling will be the bigger
	    // of these two.
	    float xScale = (float) squareSize / sourceWidth;
	    float yScale = (float) squareSize / sourceHeight;
	    float scale = Math.max(xScale, yScale);
	 
	    // Now get the size of the source bitmap when scaled
	    float scaledWidth = scale * sourceWidth;
	    float scaledHeight = scale * sourceHeight;
	 
	    // Let's find out the upper left coordinates if the scaled bitmap
	    // should be centered in the new size give by the parameters
	    float left = (squareSize - scaledWidth) / 2;
	    float top = (squareSize - scaledHeight) / 2;
	 
	    // The target rectangle for the new, scaled version of the source bitmap will now
	    // be
	    RectF targetRect = new RectF(left, top, left + scaledWidth, top + scaledHeight);
	 
	    // Finally, we create a new bitmap of the specified size and draw our new,
	    // scaled bitmap onto it.
	    Bitmap dest = Bitmap.createBitmap(squareSize, squareSize, bitmap.getConfig());
	    Canvas canvas = new Canvas(dest);
	    canvas.drawBitmap(bitmap, null, targetRect, null);
	 
	    return dest;
	}
	
	public static byte[] serialize(Object obj) throws IOException {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os = new ObjectOutputStream(out);
	    os.writeObject(obj);
	    return out.toByteArray();
	}
	public static Object deserialize(byte[] data) throws IOException, ClassNotFoundException {
	    ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is = new ObjectInputStream(in);
	    return is.readObject();
	}
	
	public static Bitmap scaleImage(Context context, Uri photoUri) throws IOException {
		float  MAX_IMAGE_DIMENSION = 1000.0f;
        InputStream is = context.getContentResolver().openInputStream(photoUri);
        BitmapFactory.Options dbo = new BitmapFactory.Options();
        dbo.inJustDecodeBounds = true;
        BitmapFactory.decodeStream(is, null, dbo);
        is.close();

        int rotatedWidth, rotatedHeight;
        int orientation = getOrientation(context, photoUri);
	
        if (orientation == 90 || orientation == 270) {
            rotatedWidth = dbo.outHeight;
            rotatedHeight = dbo.outWidth;
        } else {
            rotatedWidth = dbo.outWidth;
            rotatedHeight = dbo.outHeight;
        }
	
        Bitmap srcBitmap;
        is = context.getContentResolver().openInputStream(photoUri);
        if (rotatedWidth > MAX_IMAGE_DIMENSION || rotatedHeight > MAX_IMAGE_DIMENSION) {
            float widthRatio = ((float) rotatedWidth) / ((float) MAX_IMAGE_DIMENSION);
            float heightRatio = ((float) rotatedHeight) / ((float) MAX_IMAGE_DIMENSION);
            float maxRatio = Math.max(widthRatio, heightRatio);
	
            // Create the bitmap from file
	        BitmapFactory.Options options = new BitmapFactory.Options();
	        options.inSampleSize = (int) maxRatio;
	        srcBitmap = BitmapFactory.decodeStream(is, null, options);
	    } else {
	        srcBitmap = BitmapFactory.decodeStream(is);
	    }
	    is.close();
	
	    /*
	     * if the orientation is not 0 (or -1, which means we don't know), we
	     * have to do a rotation.
	     */
	    if (orientation > 0) {
	        Matrix matrix = new Matrix();
	        matrix.postRotate(orientation);
	
	        srcBitmap = Bitmap.createBitmap(srcBitmap, 0, 0, srcBitmap.getWidth(),
	                srcBitmap.getHeight(), matrix, true);
	    }
	
	    String type = context.getContentResolver().getType(photoUri);
	    ByteArrayOutputStream baos = new ByteArrayOutputStream();
	    if (type.equals("image/png")) {
	        srcBitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
	    } else if (type.equals("image/jpg") || type.equals("image/jpeg")) {
	        srcBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
	    }
	    byte[] bMapArray = baos.toByteArray();
	    baos.close();
	    return BitmapFactory.decodeByteArray(bMapArray, 0, bMapArray.length);
	}
	
	public static int getOrientation(Context context, Uri photoUri) {
	    /* it's on the external media. */
	    Cursor cursor = context.getContentResolver().query(photoUri,
	            new String[] { MediaStore.Images.ImageColumns.ORIENTATION }, null, null, null);
	
	    if (cursor.getCount() != 1) {
	        return -1;
	    }
	
	    cursor.moveToFirst();
	    return cursor.getInt(0);
	}	
}
