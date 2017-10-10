package com.wordpress.smdaudhilbe.sirajmnistdemo.models

import android.content.res.AssetManager
import org.tensorflow.contrib.android.TensorFlowInferenceInterface
import java.io.BufferedReader
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.util.ArrayList

/**
 *
 * Created by mohammed-2284 on 08/10/17.
 */

class TensorFlowClassifier : Classifier {

    private var tfHelper: TensorFlowInferenceInterface? = null

    private var name: String? = null
    lateinit var inputName: String
    private var outputName: String? = null
    private var inputSize: Long = 0
    private var feedKeepProb: Boolean = false

    private var labels: List<String>? = null
    private var output: FloatArray? = null
    private var outputNames: Array<String>? = null

    override fun name(): String {
        return name!!
    }

    override fun recognize(pixels: FloatArray): Classification {

        //using the interface
        //give it the input name, raw pixels from the drawing,
        //input size
        tfHelper!!.feed(inputName, pixels, 1, inputSize, inputSize, 1)

        //probabilities
        if (feedKeepProb) {
            tfHelper!!.feed("keep_prob", floatArrayOf(1f))
        }
        //get the possible outputs
        tfHelper!!.run(outputNames)

        //get the output
        tfHelper!!.fetch(outputName, output)

        // Find the best classification
        //for each output prediction
        //if its above the threshold for accuracy we predefined
        //write it out to the view
        val ans = Classification()
        for (i in output!!.indices) {
            if (output!![i] > THRESHOLD && output!![i] > ans.conf) {
                ans.update(output!![i], labels!![i])
            }
        }
        return ans
    }

    companion object {

        // Only returns if at least this confidence
        //must be a classification percetnage greater than this
        private val THRESHOLD = 0.1f

        //given a saved drawn model, lets read all the classification labels that are
        //stored and write them to our in memory labels list
        @Throws(IOException::class)
        private fun readLabels(am: AssetManager, fileName: String): List<String> {
            val labels = ArrayList<String>()
            val inputStream = am.open(fileName)
            inputStream.bufferedReader().useLines {
                lines ->
                lines.forEach {
                    labels.add(it)
                }
            }
            return labels
        }

        //given a model, its label file, and its metadata
        //fill out a classifier object with all the necessary
        //metadata including output prediction
        @Throws(IOException::class)
        fun create(assetManager: AssetManager,
                   name: String,
                   modelPath: String,
                   labelFile: String,
                   inputSize: Int,
                   inputName: String,
                   outputName: String,
                   feedKeepProb: Boolean): TensorFlowClassifier {
            //intialize a classifier
            val c = TensorFlowClassifier()

            //store its name, input and output labels
            c.name = name

            c.inputName = inputName
            c.outputName = outputName

            //read labels for label file
            c.labels = readLabels(assetManager, labelFile)

            //set its model path and where the raw asset files are
            c.tfHelper = TensorFlowInferenceInterface(assetManager, modelPath)
            val numClasses = 10

            //how big is the input?
            c.inputSize = inputSize.toLong()

            // Pre-allocate buffer.
            c.outputNames = arrayOf(outputName)
            c.outputName = outputName
            c.output = FloatArray(numClasses)

            c.feedKeepProb = feedKeepProb

            return c
        }
    }
}