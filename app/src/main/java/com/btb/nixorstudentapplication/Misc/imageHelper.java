package com.btb.nixorstudentapplication.Misc;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

import id.zelory.compressor.Compressor;

public class imageHelper {


    static String TAG = "imagesHelper";
    public static Bitmap scaleBitmap(Bitmap bm, int maxHeight, int maxWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();

        Log.v(TAG, "Width and height are " + width + "--" + height);

        if (width > height) {
            // landscape
            float ratio = (float) width / maxWidth;
            width = maxWidth;
            height = (int)(height / ratio);
        } else if (height > width) {
            // portrait
            float ratio = (float) height / maxHeight;
            height = maxHeight;
            width = (int)(width / ratio);
        } else {
            // square
            height = maxHeight;
            width = maxWidth;
        }

        Log.v(TAG, "after scaling Width and height are " + width + "--" + height);

        bm = Bitmap.createScaledBitmap(bm, width, height, true);

        return bm;
    }

    public static byte[] compressScaledBitmap(Bitmap bm){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bm.compress(Bitmap.CompressFormat.JPEG, 75, baos);
        byte[] data = baos.toByteArray();


        return data;
    }


    public static Bitmap compressImage(Context context, File imageSelectedFile) throws IOException {
        Bitmap compressedImage = new Compressor(context)
                .setQuality(75)
                .setCompressFormat(Bitmap.CompressFormat.JPEG)
                .compressToBitmap(imageSelectedFile);
        return  compressedImage;
    }


}
