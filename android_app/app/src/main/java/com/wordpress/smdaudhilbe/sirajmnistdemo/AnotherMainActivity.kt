package com.wordpress.smdaudhilbe.sirajmnistdemo

import android.graphics.Bitmap
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.wordpress.smdaudhilbe.sirajmnistdemo.models.TensorFlowClassifier
import com.wordpress.smdaudhilbe.sirajmnistdemo.util.Utils
import kotlinx.android.synthetic.main.activity_another_main.*

/**
 *
 * Created by mohammed-2284 on 09/10/17.
 */

class AnotherMainActivity : AppCompatActivity() {

    val PIXEL_WIDTH = 28
    lateinit var tfModelClassifier: TensorFlowClassifier

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_another_main)

        clear_btn.setOnClickListener {
            drawView.clearDrawing()
            prediction_text_view.text = ""
        }

        detect_btn.setOnClickListener {
            val resizedBitmap = Bitmap.createScaledBitmap(drawView.canvasBitmap, 28, 28, true)
            val pixelArrays = Utils.getPixelsOfBitmap(resizedBitmap as Bitmap?)
            pixelArrays?.let {
                val res = tfModelClassifier.recognize(pixelArrays)
                prediction_text_view.text = String.format("You wrote : %s, and prediction confidence : %f", res.label, res.conf)
            }
        }
        loadTensorFlowModel()
    }

    //  loading a tensorflow model
    fun loadTensorFlowModel() {
        Thread(Runnable {
            try {
                tfModelClassifier = TensorFlowClassifier.create(assets, "TensorFlow",
                        "opt_mnist_for_android_convnet.pb", "labels.txt", PIXEL_WIDTH,
                        "input", "output", true)
            } catch (e: Exception) {
                //if they aren't found, throw an error!
                throw RuntimeException("Error initializing classifiers!", e)
            }
        }).start()
    }
}