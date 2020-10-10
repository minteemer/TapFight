package minteemer.tapfight.ui.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.annotation.FloatRange
import minteemer.tapfight.R

class TimerBarView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    private val paint: Paint
    private val barRect = Rect()

    @FloatRange(from = 0.0, to = 1.0)
    private var _timeLeft: Float = 1f

    var timeLeft: Float
        get() = _timeLeft
        set(@FloatRange(from = 0.0, to = 1.0) value) {
            _timeLeft = value
            invalidate()
        }

    init {
        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.TimerBarView, defStyleAttr, defStyleRes)

        timeLeft = attributes.getFraction(R.styleable.TimerBarView_timeLeft, 1, 0, 1f)
        paint = Paint().apply {
            color = attributes.getColor(R.styleable.TimerBarView_color, context.getColor(R.color.colorAccent))
            style = Paint.Style.FILL
        }

        attributes.recycle()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom
        val bottomY = contentHeight - paddingBottom
        val barMargin = (contentWidth * (1 - timeLeft) / 2).toInt()

        barRect.apply {
            bottom = bottomY
            top = paddingTop
            left = paddingLeft + barMargin
            right = width - paddingRight - barMargin
        }

        canvas.drawRect(barRect, paint)
    }

}
