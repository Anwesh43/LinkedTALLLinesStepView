package com.anwesh.uiprojects.talllinesstepview

/**
 * Created by anweshmishra on 29/10/18.
 */

import android.view.View
import android.view.MotionEvent
import android.graphics.Paint
import android.graphics.Canvas
import android.graphics.Color
import android.content.Context

val nodes : Int = 5

val purpleColor : Int = Color.parseColor("#3F51B5")

val lines : Int = 3

fun Paint.setPurpleThickLine(w : Float, h : Float) {
    strokeWidth = Math.min(w, h) / 60
    color = purpleColor
    strokeCap = Paint.Cap.ROUND
}

fun Canvas.drawTALSNode(i : Int, scale : Float, paint : Paint) {
    val w : Float = width.toFloat()
    val h : Float = height.toFloat()
    val gap : Float = h / (nodes + 1)
    val size : Float = gap / 3
    val osc : Float = 1f / lines
    paint.setPurpleThickLine(w, h)
    save()
    translate(w/2, gap + gap * i)
    for (j in 0..2) {
        val sc : Float = Math.min(osc, Math.max(0f, scale - osc * j)) * lines
        val lSize : Float = size * (1 + j % 2)
        save()
        translate(0f, -size/2)
        rotate(90f * j)
        drawLine(0f, 0f, lSize, 0f, paint)
        restore()
    }
    restore()
}

class TAllLinesStepView(ctx : Context) : View(ctx) {

    private val paint : Paint = Paint(Paint.ANTI_ALIAS_FLAG)

    override fun onDraw(canvas : Canvas) {

    }

    override fun onTouchEvent(event : MotionEvent) : Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {

            }
        }
        return true
    }

    data class State(var scale : Float = 0f, var prevScale : Float = 0f, var dir : Float = 0f) {

        fun update(cb : (Float) -> Unit) {
            scale += (0.1f / lines) * dir
            if (Math.abs(scale - prevScale) > 1) {
                scale = prevScale + dir
                dir = 0f
                prevScale = scale
                cb(prevScale)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            if (dir == 0f) {
                dir = 1f - 2 * prevScale
                cb()
            }
        }
    }
}