package com.example.taskmanager.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.net.Uri;
import android.os.ParcelFileDescriptor;


import java.io.IOException;

public class PictureUtils {
    public static Bitmap getScaledBitmap(String path, int destWidth, int destHeight) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;

        BitmapFactory.decodeFile(path, options);
        int srcWidth = options.outWidth;
        int srcHeight = options.outHeight;

        // Figure out how much to scale down by
        int inSampleSize = Math.min(srcWidth/destWidth, srcHeight/destHeight);

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;
        return BitmapFactory.decodeFile(path, options);
    }

    public static Bitmap getScaledBitmap(Uri path, Activity activity, BitmapFactory.Options options) throws IOException {
        ParcelFileDescriptor pfdInput = activity.getContentResolver().openFileDescriptor(path, "r");
        Bitmap bitmap = BitmapFactory.decodeFileDescriptor(pfdInput.getFileDescriptor(), null, options);
        pfdInput.close();
        return bitmap;
    }

    public static Bitmap getScaledBitmap(String path, Activity activity) {
        Point point = new Point();
        activity.getWindowManager().getDefaultDisplay().getSize(point);
        return getScaledBitmap(path, point.x, point.y);
    }
}
