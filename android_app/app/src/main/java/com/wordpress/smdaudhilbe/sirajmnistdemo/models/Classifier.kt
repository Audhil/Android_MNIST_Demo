package com.wordpress.smdaudhilbe.sirajmnistdemo.models

/**
 *
 * Created by mohammed-2284 on 08/10/17.
 */

interface Classifier {
    fun name(): String
        fun recognize(pixels: FloatArray): Classification
}