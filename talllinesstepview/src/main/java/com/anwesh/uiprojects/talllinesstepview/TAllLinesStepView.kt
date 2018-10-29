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

    data class Animator(var view : View, var animated : Boolean = false) {

        fun animate(cb : () -> Unit) {
            if (!animated) {
                cb()
                try {
                    Thread.sleep(50)
                    view.invalidate()
                } catch(ex : Exception) {

                }
            }
        }

        fun start()  {
            if (!animated) {
                animated = true
                view.postInvalidate()
            }
        }

        fun stop() {
            if (animated) {
                animated = false
            }
        }
    }

    data class TALSNode(var i : Int, val state : State = State()) {

        private var prev : TALSNode? = null

        private var next : TALSNode? = null

        init {
            addNeighbor()
        }

        fun addNeighbor(){
            if (i < nodes - 1) {
                next = TALSNode(i + 1)
                next?.prev = this
            }
        }

        fun draw(canvas : Canvas, paint : Paint) {
            canvas.drawTALSNode(i, state.scale, paint)
            prev?.draw(canvas, paint)
        }

        fun update(cb : (Int, Float) -> Unit) {
            state.update {
                cb(i, it)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            state.startUpdating(cb)
        }

        fun getNext(dir : Int, cb : () -> Unit) : TALSNode {
            var curr : TALSNode? = prev
            if (dir == 1) {
                curr = next
            }
            if (curr != null) {
                return curr
            }
            cb()
            return this
        }
    }

    data class TALLLinesStep(var i : Int) {
        private var curr : TALSNode = TALSNode(0)

        private var dir : Int = 1

        fun draw(canvas : Canvas, paint : Paint) {
            curr.draw(canvas, paint)
        }

        fun update(cb : (Int, Float) -> Unit) {
            curr.update {i, scl ->
                curr = curr.getNext(dir) {
                    dir *= -1
                }
                cb(i, scl)
            }
        }

        fun startUpdating(cb : () -> Unit) {
            curr.startUpdating(cb)
        }
    }
}