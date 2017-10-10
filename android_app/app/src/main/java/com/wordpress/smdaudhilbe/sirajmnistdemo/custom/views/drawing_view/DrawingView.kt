package com.wordpress.smdaudhilbe.sirajmnistdemo.custom.views.drawing_view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

/**
 *
 * Created by mohammed-2284 on 09/10/17.
 */

//  tutorial @ https://code.tutsplus.com/tutorials/android-sdk-create-a-drawing-app-touch-interaction--mobile-19202

class DrawingView : View {
    constructor(context: Context) : super(context) {
        setUpDrawing()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        setUpDrawing()
    }

    lateinit var drawPaint: Paint
    lateinit var canvasPaint: Paint
    lateinit var drawPath: Path
    lateinit var canvasBitmap: Bitmap
    lateinit var drawCanvas: Canvas

    //  set up method
    fun setUpDrawing() {
        drawPath = Path()
        drawPaint = Paint()
        drawPaint.color = 0xFF660000.toInt()
        drawPaint.isAntiAlias = true
//        drawPaint.strokeWidth = 20f
        drawPaint.strokeWidth = 50f
        drawPaint.style = Paint.Style.STROKE
        drawPaint.strokeJoin = Paint.Join.ROUND
        drawPaint.strokeCap = Paint.Cap.ROUND
        canvasPaint = Paint(Paint.DITHER_FLAG)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        canvasBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        drawCanvas = Canvas(canvasBitmap)

    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(canvasBitmap, 0f, 0f, canvasPaint)
        canvas?.drawPath(drawPath, drawPaint)
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val touchX = event?.x
        val touchY = event?.y

        when (event?.action) {
            MotionEvent.ACTION_DOWN -> {
                drawPath.moveTo(touchX!!, touchY!!)
            }

            MotionEvent.ACTION_MOVE -> {
                drawPath.lineTo(touchX!!, touchY!!)
            }

            MotionEvent.ACTION_UP -> {
                drawCanvas.drawPath(drawPath, drawPaint)
                drawPath.reset()
            }
            else -> {
                return false
            }
        }
        invalidate()
        return true
    }

    //  clearing the canvas
    fun clearDrawing() {
        drawCanvas.drawColor(0, PorterDuff.Mode.CLEAR)
        invalidate()
    }
}