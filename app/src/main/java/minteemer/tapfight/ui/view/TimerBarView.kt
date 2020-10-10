package minteemer.tapfight.ui.view

import android.content.Context
import android.graphics.*
import android.graphics.drawable.GradientDrawable
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import minteemer.tapfight.R


class TimerBarView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    @ColorInt
    private val barColor: Int
    private val barDrawable: GradientDrawable
    private val barRect = Rect()

    @FloatRange(from = 0.0, to = 1.0)
    private var _timeLeft: Float = 1f

    private var contentWidth: Int = 0
    private var contentHeight: Int = 0

    var timeLeft: Float
        get() = _timeLeft
        set(@FloatRange(from = 0.0, to = 1.0) value) {
            _timeLeft = value
            invalidate()
        }

    init {
        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.TimerBarView, defStyleAttr, defStyleRes)

        barColor = attributes.getColor(R.styleable.TimerBarView_color, context.getColor(R.color.colorAccent))
        timeLeft = attributes.getFraction(R.styleable.TimerBarView_timeLeft, 1, 0, 1f)

        attributes.recycle()

        barDrawable = GradientDrawable().apply {
            shape = GradientDrawable.RECTANGLE
            gradientType = GradientDrawable.LINEAR_GRADIENT
            orientation = GradientDrawable.Orientation.LEFT_RIGHT
            colors = intArrayOf(Color.TRANSPARENT, barColor, barColor, barColor, barColor, Color.TRANSPARENT)
        }
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        super.onLayout(changed, l, t, r, b)

        contentWidth = width - paddingLeft - paddingRight
        contentHeight = height - paddingTop - paddingBottom

        barRect.apply {
            top = paddingTop
            bottom = paddingTop + contentHeight
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val barMargin = (contentWidth * (1 - timeLeft) / 2).toInt()

        barDrawable.bounds = barRect.apply {
            left = paddingLeft + barMargin
            right = width - paddingRight - barMargin
        }

        barDrawable.draw(canvas)
    }
}
