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
 * desc: 接力
 */

private val BITMAP_WIDTH = 200.dp

class MultiTouchView1(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val bitmap = getAvator(resources, BITMAP_WIDTH.toInt())

    private var downX = 0f
    private var downY = 0f
    private var offsetX = 0f
    private var offsetY = 0f
    private var originalOffsetX = 0f
    private var originalOffsetY = 0f
    private var trackingPointerId = 0  // 跟踪起作用的那个手指的id

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawBitmap(bitmap, offsetX, offsetY, paint)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN -> {
                trackingPointerId = event.getPointerId(0)
                downX = event.x
                downY = event.y
                originalOffsetX = offsetX
                originalOffsetY = offsetY
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                val actionIndex = event.actionIndex
                trackingPointerId = event.getPointerId(actionIndex)
                downX = event.getX(actionIndex)
                downY = event.getY(actionIndex)
                originalOffsetX = offsetX
                originalOffsetY = offsetY

            }
            MotionEvent.ACTION_POINTER_UP -> {
                val actionIndex = event.actionIndex
                val pointId = event.getPointerId(actionIndex)
                if (trackingPointerId == pointId) {
                    // 当手指抬起时，这个序列中的 pointerCount 并不会减1，所以需要对这个情况特殊处理
                    val newIndex = if (actionIndex == event.pointerCount - 1) {
                        event.pointerCount - 2
                    } else {
                        event.pointerCount - 1
                    }
                    trackingPointerId = event.getPointerId(newIndex)
                    downX = event.getX(newIndex)
                    downY = event.getY(newIndex)
                    originalOffsetX = offsetX
                    originalOffsetY = offsetY
                }
            }
            MotionEvent.ACTION_MOVE -> {
                offsetX = event.getX(event.findPointerIndex(trackingPointerId)) - downX + originalOffsetX
                offsetY = event.getY(event.findPointerIndex(trackingPointerId)) - downY + originalOffsetY
                invalidate()
            }
        }
        return true
    }
}