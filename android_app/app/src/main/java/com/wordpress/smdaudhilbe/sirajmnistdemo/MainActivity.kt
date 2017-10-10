package com.wordpress.smdaudhilbe.sirajmnistdemo

import android.graphics.PointF
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import com.wordpress.smdaudhilbe.sirajmnistdemo.models.Classifier
import com.wordpress.smdaudhilbe.sirajmnistdemo.models.TensorFlowClassifier
import com.wordpress.smdaudhilbe.sirajmnistdemo.views.DrawModel
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity(), View.OnTouchListener, View.OnClickListener {

    override fun onClick(view: View?) {
        //when the user clicks something
        if (view?.id == R.id.btn_clear) {
            //if its the clear button
            //clear the drawing
            drawModel.clear()
            drawView.reset()
            drawView.invalidate()
            //empty the text view
            textView.text = ""
        } else if (view?.id == R.id.btn_class) {
            //if the user clicks the classify button
            //get the pixel data and store it in an array
//            val pixels = drawView.getPixelData()
            val pixels = drawView.pixelData

            //init an empty string to fill with the classification output
            var text = ""
            //for each classifier in our array
            for (classifier in mClassifiers) {
                //perform classification on the image
                val res = classifier.recognize(pixels as FloatArray)
                //if it can't classify, output a question mark
                if (res.label == null) {
                    text += classifier.name() + ": ?\n"
                } else {
                    //else output its name
                    text += String.format("%s: %s, %f\n", classifier.name(), res.label, res.conf)
                }
            }
            textView.text = text
        }
    }

    override fun onTouch(p0: View?, event: MotionEvent?): Boolean {
        //get the action and store it as an int
        val action = event?.action
        //actions have predefined ints, lets match
        //to detect, if the user has touched, which direction the users finger is
        //moving, and if they've stopped moving

        //if touched
        if (action == MotionEvent.ACTION_DOWN) {
            //begin drawing line
            processTouchDown(event)
            return true
            //draw line in every direction the user moves
        } else if (action == MotionEvent.ACTION_MOVE) {
            processTouchMove(event)
            return true
            //if finger is lifted, stop drawing
        } else if (action == MotionEvent.ACTION_UP) {
            processTouchUp()
            return true
        }
        return false
    }

    //draw line down

    private fun processTouchDown(event: MotionEvent) {
        //calculate the x, y coordinates where the user has touched
        mLastX = event.x
        mLastY = event.y
        //user them to calcualte the position
        drawView.calcPos(mLastX, mLastY, mTmpPiont)
        //store them in memory to draw a line between the
        //difference in positions
        val lastConvX = mTmpPiont.x
        val lastConvY = mTmpPiont.y
        //and begin the line drawing
        drawModel.startLine(lastConvX, lastConvY)
    }

    //the main drawing function
    //it actually stores all the drawing positions
    //into the drawmodel object
    //we actually render the drawing from that object
    //in the drawrenderer class
    private fun processTouchMove(event: MotionEvent) {
        val x = event.x
        val y = event.y

        drawView.calcPos(x, y, mTmpPiont)
        val newConvX = mTmpPiont.x
        val newConvY = mTmpPiont.y
        drawModel.addLineElem(newConvX, newConvY)

        mLastX = x
        mLastY = y
        drawView.invalidate()
    }

    private fun processTouchUp() {
        drawModel.endLine()
    }


    var mTmpPiont = PointF()

    var mLastX: Float = 0.toFloat()
    var mLastY: Float = 0.toFloat()

    val PIXEL_WIDTH = 28
    lateinit var drawModel: DrawModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        drawModel = DrawModel(PIXEL_WIDTH, PIXEL_WIDTH)
        drawView.setModel(drawModel)
        drawView.setOnTouchListener(this)
        btn_clear.setOnClickListener(this)
        btn_class.setOnClickListener(this)
        loadModel()
    }

    override fun onResume() {
        drawView.onResume()
        super.onResume()
    }

    override fun onPause() {
        drawView.onPause()
        super.onPause()
    }

    val mClassifiers = ArrayList<Classifier>()

    //creates a model object in memory using the saved tensorflow protobuf model file
    //which contains all the learned weights
    fun loadModel() {
        Thread(Runnable {
            try {
                //add 2 classifiers to our classifier arraylist
                //the tensorflow classifier and the keras classifier
                mClassifiers.add(TensorFlowClassifier.create(assets, "TensorFlow",
                        "opt_mnist_for_android_convnet.pb", "labels.txt", PIXEL_WIDTH,
                        "input", "output", true))
//                mClassifiers.add(TensorFlowClassifier.create(assets, "Keras",
//                        "opt_mnist_convnet-keras.pb", "labels.txt", PIXEL_WIDTH,
//                        "conv2d_1_input", "dense_2/Softmax", false))
            } catch (e: Exception) {
                //if they aren't found, throw an error!
                throw RuntimeException("Error initializing classifiers!", e)
            }
        }).start()
    }
}