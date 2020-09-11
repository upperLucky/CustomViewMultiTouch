package com.upperlucky.customviewmultitouch.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.upperlucky.customviewmultitouch.dp
import com.upperlucky.customviewmultitouch.getAvator

/**
 * created by yunKun.wen on 2020/9/11
 * desc: 协作
 */

private val BITMAP_WIDTH = 200.dp

class MultiTouchView2(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getAvator(resources, BITMAP_WIDTH.toInt())

    private var downX = 0f
    private var downY = 0f
    private var offsetX = 0f
    private var offsetY = 0f
    private var originalOffsetX = 0f
    private var originalOffsetY = 0f

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        var focusX: Float
        var focusY: Float
        var sumX = 0f
        var sumY = 0f

        var pointerCount = event.pointerCount

        val actionPointUp = event.actionMasked == MotionEvent.ACTION_POINTER_UP

        for (i in 0 until pointerCount) {
            if (!(actionPointUp && i == event.actionIndex)) {
                sumX += event.getX(i)
                sumY += event.getY(i)
            }
        }

        if (actionPointUp) {
            pointerCount--
        }

        focusX = sumX / pointerCount
        focusY = sumY / pointerCount

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN, MotionEvent.ACTION_POINTER_UP -> {
                downX = focusX
                downY = focusY
                originalOffsetX = offsetX
                originalOffsetY = offsetY
            }
            MotionEvent.ACTION_MOVE -> {
                offsetX = focusX - downX + originalOffsetX
                offsetY = focusY - downY + originalOffsetY
                invalidate()
            }
        }
        return true
    }
}