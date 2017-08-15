package com.os4.ecb.misc;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.nio.ByteBuffer;

import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Paint;
import android.os.Environment;

public class Decryptor {

    public static final String COMPANY_NAME = "OS4";
    public static final String MODULE_NAME = "Messenger";
    public static final String DIRECTORY_NAME = "Fingerprints";
	public static final String IMAGES_FILE_PATH = Environment.getExternalStorageDirectory().getPath() + File.separator + COMPANY_NAME + File.separator + MODULE_NAME + File.separator + DIRECTORY_NAME + File.separator;
	
	private int key;
	
    private class Block{
        public int flag;
        public int lines;
        public byte[] byteBlock;
    }
    
    private class DataCapture{
        public ByteBuffer byteHeader;
        public int tag;
        public int hdr_len;
        public int width;
        public int height;
        public Block[] blocks = new Block[8];
        
        public DataCapture(FileWriter writer,byte[] bytesHeader){
            
            byteHeader = ByteBuffer.wrap(bytesHeader);
            /* not working for android < 5.1
            byteHeader.order(ByteOrder.LITTLE_ENDIAN);
            */
            tag = (int) byteHeader.get();
            hdr_len = (int) byteHeader.get();
            width = 384;
            height = 289;
            
            byteHeader = ByteBuffer.wrap(bytesHeader,16,16);
            for(int i=0;i<blocks.length;i++){
                Block block = new Block();
                block.flag = (int) byteHeader.get();
                block.lines = (int) byteHeader.get();
                blocks[i] = block;
            }
        }
    }
	
    public byte[] decrypt(FileWriter writer, byte[] bytesCapture,File fileImage,int keyNumber){
        
    	key = keyNumber;
    	
        try{
        	writer.write("Key:"+Integer.toHexString(key)+"\n");
            ByteArrayInputStream bis = new ByteArrayInputStream(bytesCapture);
            int length = bis.available();
            byte[] bytesHeader = new byte[64];
            byte[] bytesBody = new byte[length-64];
            bis.read(bytesHeader);
            bis.read(bytesBody);
            bis.close();
                        
            DataCapture data = new DataCapture(writer,bytesHeader);            
            int pos = 0;
            for(Block block : data.blocks){
                int len = data.width * block.lines;
                
                if(bytesBody.length - (pos + len) < 0)
                    len = bytesBody.length - pos;
                
                block.byteBlock = ByteBuffer.allocate(len).put(bytesBody, pos, len).array();
                block.byteBlock = decode(block.flag, block.byteBlock);
                
                if(block.flag==0x00 || block.flag==0x02)
                    pos = pos + len;
            }
            
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            for(Block block : data.blocks){
                if(block.flag!=0x01)
                    bos.write(block.byteBlock);
            }
            bytesCapture = bos.toByteArray();
            bos.close();
            
            FileOutputStream fos = new FileOutputStream(fileImage);
            Bitmap grayScaledPic = createBitmap(bytesCapture, data.width, data.height);
            grayScaledPic.compress(CompressFormat.JPEG, 100, fos);
            fos.close();

            return bytesCapture;

        }catch(Exception e){
        	try{
        		writer.write("Exception : "+e.getMessage());
        	}catch(Exception ce){}
        	return null;
        }
    }
    
    private int update_key(int key)
    {
	/* linear feedback shift register
	 * taps at bit positions 1 3 4 7 11 13 20 23 26 29 32 */
	int bit = key & 0x9248144d;
	bit ^= bit << 16;
	bit ^= bit << 8;
	bit ^= bit << 4;
	bit ^= bit << 2;
	bit ^= bit << 1;        
	return (bit & 0x80000000) | (key >>> 1);
    }
    
    private byte[] decode(int flag, byte[] data){
        
        int num_bytes = data.length;
        int xorbyte;
        int i;
        if(flag==0x02){
	        for (i = 0; i < num_bytes-1; i++) {
	            xorbyte  = ((key >>  4) & 1) << 0;
	            xorbyte |= ((key >>  8) & 1) << 1;
	            xorbyte |= ((key >> 11) & 1) << 2;
	            xorbyte |= ((key >> 14) & 1) << 3;
	            xorbyte |= ((key >> 18) & 1) << 4;
	            xorbyte |= ((key >> 21) & 1) << 5;
	            xorbyte |= ((key >> 24) & 1) << 6;
	            xorbyte |= ((key >> 29) & 1) << 7;
	            int ubyte = data[i+1] & (0xff);
	            int decode = ubyte ^ xorbyte;
	            data[i] = Integer.valueOf(decode).byteValue();
	            key = update_key(key);
	        }        
	        data[i] = 0;
	        key = update_key(key);
        }
        else{
        	for (int j = 0; j < num_bytes; j++) key = update_key(key);
        }        	
        return data;
    }
    
    public static Bitmap createBitmap(byte[] greyBuffer, int width, int height)
    {
    	int pixCount = greyBuffer.length;
        int[] intGreyBuffer = new int[pixCount];
        
        for(int h=0;h<height;h++)
            for(int w=0;w<width;w++){
                int greyValue = 0xff - ((int)greyBuffer[h * width + w] & 0xff);
                intGreyBuffer[h * width + w] = 0xff000000 | (greyValue << 16) | (greyValue << 8) | greyValue;
            }
        
        Bitmap grayScaledPic = Bitmap.createBitmap(intGreyBuffer, width, height, Bitmap.Config.ARGB_8888);
        return grayScaledPic;
    }
    
    public Bitmap toGrayscale(Bitmap src)
    {
        int height = src.getHeight();
        int width = src.getWidth();

        Bitmap dest = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
        Canvas c = new Canvas(dest);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(src, 0, 0, paint);
        return dest;
    }
}
