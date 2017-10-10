package com.wordpress.smdaudhilbe.sirajmnistdemo.util

import android.graphics.Bitmap

/**
 *
 * Created by mohammed-2284 on 09/10/17.
 */

object Utils {

//    fun getPixelsOfBitmap(bitmap: Bitmap?): IntArray? {
//        bitmap?.let {
//            val pixels = IntArray(bitmap.width * bitmap.height)
//            bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
//            return pixels
//        }
//        return null
//    }

    //    http://android-coding.blogspot.in/2011/12/copy-bitmap-using-getpixels-and.html
    fun getPixelsOfBitmap(bitmap: Bitmap?): FloatArray? {
        bitmap?.let {
            val pixels = IntArray(bitmap.width * bitmap.height)
            bitmap.getPixels(pixels, 0, bitmap.width, 0, 0, bitmap.width, bitmap.height)
            return convertToFloatArray(pixels)
        }
        return null
    }

    fun convertToFloatArray(pixels: IntArray): FloatArray {
        val pixelsFloatArray = FloatArray(pixels.size)
//        for (i in 0..pixels.size - 1) {
//            pixelsFloatArray[i] = pixels[i].toFloat()
//        }
        for (i in 0..pixels.size - 1) {
            if (pixels[i] == 0) {
                pixels[i] = -1
            }
        }
        for (i in 0..pixels.size - 1) {
            // Set 0 for white and 255 for black pixel
            val pix = pixels[i]
            val b = pix and 0xff
            pixelsFloatArray[i] = ((0xff - b) / 255.0).toFloat()
        }
        return pixelsFloatArray
    }
}