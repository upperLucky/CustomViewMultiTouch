package com.upperlucky.customviewmultitouch.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.SparseArray
import android.view.MotionEvent
import android.view.View
import com.upperlucky.customviewmultitouch.dp

/**
 *  Created by yunkun.wen on 2020/9/11
 *  Desc: 各自为战
 */
class MultiTouchView3(context: Context?, attrs: AttributeSet?) : View(context, attrs) {

    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
    private val paths = SparseArray<Path>()

    init {
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.strokeJoin = Paint.Join.ROUND
        paint.strokeWidth = 3.dp
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        for (index in 0 until paths.size()) {
            val path = paths.valueAt(index)
            canvas.drawPath(path, paint)
        }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {

        when (event.actionMasked) {
            MotionEvent.ACTION_DOWN, MotionEvent.ACTION_POINTER_DOWN -> {
                val actionIndex = event.actionIndex
                val path = Path()
                path.moveTo(event.getX(actionIndex), event.getY(actionIndex))
                paths.put(event.getPointerId(actionIndex), path)
                invalidate()
            }
            MotionEvent.ACTION_MOVE -> {
                for (index in 0 until paths.size()) {
                    val pointerId = event.getPointerId(index)
                    val path = paths.get(pointerId)
                    path.lineTo(event.getX(index), event.getY(index))
                }
                invalidate()
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                val actionIndex = event.actionIndex
                val pointerId = event.getPointerId(actionIndex)
                paths.remove(pointerId)
                invalidate()
            }
        }
        return true
    }
}