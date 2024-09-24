package com.pnvpnvpnv.graphtesttask.view.graph

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.graphics.PointF
import android.graphics.RectF
import android.os.Parcelable
import android.util.AttributeSet
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import kotlinx.parcelize.Parcelize
import java.io.OutputStream

class GraphView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : View(context, attrs), GestureDetector.OnGestureListener,
    ScaleGestureDetector.OnScaleGestureListener {

    private var points: List<PointF>? = null
    private val path = Path()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 0.5f
    }

    private var scaleFactor = 3f
    private var translateX = 0f
    private var translateY = 0f

    private val minScaleFactor = 3f
    private val maxScaleFactor = 20f

    private val gestureDetector = GestureDetector(context, this)
    private val scaleGestureDetector = ScaleGestureDetector(context, this)

    private var needsInitialLayout = true

    /**
     * Sets the points to be drawn on the graph.
     */
    fun setPoints(points: List<PointF>?) {
        this.points = points
        rebuildPath()
        needsInitialLayout = true
        if (width > 0 && height > 0) {
            adjustInitialScaleAndTranslation()
            needsInitialLayout = false
        }
        invalidate()
    }

    /**
     * Saves the current view state to the provided output stream.
     */
    fun saveToStream(outputStream: OutputStream) {
        val bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bitmap)
        draw(canvas)
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, outputStream)
    }

    private fun rebuildPath() {
        path.reset()
        points?.let { pts ->
            if (pts.isNotEmpty()) {
                path.moveTo(pts[0].x, pts[0].y)
                if (pts.size == 2) {
                    path.lineTo(pts[1].x, pts[1].y)
                } else {
                    var prevPoint = pts[0]
                    for (i in 1 until pts.size) {
                        val currentPoint = pts[i]
                        val midPointX = (prevPoint.x + currentPoint.x) / 2
                        val midPointY = (prevPoint.y + currentPoint.y) / 2
                        path.quadTo(prevPoint.x, prevPoint.y, midPointX, midPointY)
                        prevPoint = currentPoint
                    }
                    path.lineTo(prevPoint.x, prevPoint.y)
                }
            }
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.save()
        canvas.translate(translateX, translateY)
        canvas.scale(scaleFactor, scaleFactor)
        canvas.drawPath(path, paint)
        canvas.restore()
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        val scaleGesture = scaleGestureDetector.onTouchEvent(event)
        val gesture = gestureDetector.onTouchEvent(event)
        return scaleGesture || gesture || super.onTouchEvent(event)
    }

    override fun onDown(e: MotionEvent): Boolean = true

    override fun onShowPress(e: MotionEvent) {
        // No action needed
    }

    override fun onSingleTapUp(e: MotionEvent): Boolean = false

    override fun onScroll(
        e1: MotionEvent?,
        e2: MotionEvent,
        distanceX: Float,
        distanceY: Float
    ): Boolean {
        translateX -= distanceX
        translateY -= distanceY
        invalidate()
        return true
    }

    override fun onLongPress(e: MotionEvent) {
        // No action needed
    }

    override fun onFling(
        e1: MotionEvent?,
        e2: MotionEvent,
        velocityX: Float,
        velocityY: Float
    ): Boolean = false

    override fun onScale(detector: ScaleGestureDetector): Boolean {
        scaleFactor *= detector.scaleFactor
        scaleFactor = scaleFactor.coerceIn(minScaleFactor, maxScaleFactor)
        invalidate()
        return true
    }

    override fun onScaleBegin(detector: ScaleGestureDetector): Boolean = true

    override fun onScaleEnd(detector: ScaleGestureDetector) {
        // No action needed
    }

    private fun adjustInitialScaleAndTranslation() {
        if (path.isEmpty) return

        val pathBounds = RectF()
        path.computeBounds(pathBounds, true)

        if (pathBounds.width() == 0f || pathBounds.height() == 0f) return

        val padding = 40f
        val scaleX = (width - padding * 2) / pathBounds.width()
        val scaleY = (height - padding * 2) / pathBounds.height()
        val scale = scaleX.coerceAtMost(scaleY)

        scaleFactor = scale.coerceIn(minScaleFactor, maxScaleFactor)
        translateX = (width / 2f) - ((pathBounds.left + pathBounds.right) / 2f) * scaleFactor
        translateY = (height / 2f) - ((pathBounds.top + pathBounds.bottom) / 2f) * scaleFactor
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (needsInitialLayout) {
            adjustInitialScaleAndTranslation()
            needsInitialLayout = false
        }
    }

    override fun onSaveInstanceState(): Parcelable {
        val superState = super.onSaveInstanceState()
        return SavedState(
            superState,
            points,
            scaleFactor,
            translateX,
            translateY
        )
    }

    override fun onRestoreInstanceState(state: Parcelable?) {
        if (state is SavedState) {
            super.onRestoreInstanceState(state.sState)
            points = state.points
            scaleFactor = state.scaleFactor
            translateX = state.translateX
            translateY = state.translateY
            needsInitialLayout = false
            rebuildPath()
            invalidate()
        } else {
            super.onRestoreInstanceState(state)
        }
    }

    @Parcelize
    class SavedState(
        val sState: Parcelable?,
        val points: List<PointF>?,
        val scaleFactor: Float,
        val translateX: Float,
        val translateY: Float
    ) : BaseSavedState(sState), Parcelable
}
