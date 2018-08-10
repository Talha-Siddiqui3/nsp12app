package com.btb.nixorstudentapplication.Sharks_on_cloud.Misc;

import android.graphics.Bitmap;

public interface ASync_Listener {
    void onResponseReceive(String filename, Bitmap compressedImage, Bitmap thumbnailImage);

}
