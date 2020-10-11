package minteemer.tapfight.ui.view.bubbles

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import kotlin.math.min

class BubbleView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    init {
        isClickable = true
    }

    private var centerX = 0f
    private var centerY = 0f
    private var radius = 0f
    private var paint = Paint().apply {
        style = Paint.Style.FILL
    }

    var color: Int
        @ColorInt get() = paint.color
        set(@ColorInt value) {
            paint.color = value
            invalidate()
        }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)

        centerX = (width / 2).toFloat()
        centerY = (height / 2).toFloat()
        radius = (min(width, height) / 2).toFloat()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        canvas.drawCircle(centerX, centerY, radius, paint)
    }
}