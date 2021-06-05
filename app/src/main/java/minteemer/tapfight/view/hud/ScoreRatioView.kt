package minteemer.tapfight.view.hud

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Rect
import android.util.AttributeSet
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.FloatRange
import minteemer.tapfight.R

class ScoreRatioView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    private val leftBarPaint: Paint
    private val rightBarPaint: Paint

    private val leftBarRect = Rect()
    private val rightBarRect = Rect()

    @FloatRange(from = 0.0, to = 1.0)
    private var _scoreRatio: Float = 0.5f

    var scoreRatio: Float
        get() = _scoreRatio
        set(@FloatRange(from = 0.0, to = 1.0) value) {
            _scoreRatio = value
            invalidate()
        }

    init {
        val attributes = context.obtainStyledAttributes(attributeSet, R.styleable.ScoreRatioView, defStyleAttr, defStyleRes)

        _scoreRatio = attributes.getFraction(R.styleable.ScoreRatioView_scoreRatio, 1, 0, 0.5f)
        leftBarPaint = getBarPaint(attributes.getColor(R.styleable.ScoreRatioView_leftColor, context.getColor(R.color.player1)))
        rightBarPaint = getBarPaint(attributes.getColor(R.styleable.ScoreRatioView_rightColor, context.getColor(R.color.player2)))

        attributes.recycle()
    }


    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val contentWidth = width - paddingLeft - paddingRight
        val contentHeight = height - paddingTop - paddingBottom
        val centerX = paddingLeft + (contentWidth * scoreRatio).toInt()
        val bottomY = contentHeight - paddingBottom

        leftBarRect.apply {
            left = paddingLeft
            right = centerX
            top = paddingTop
            bottom = bottomY
        }

        rightBarRect.apply {
            left = centerX
            right = contentWidth
            top = paddingTop
            bottom = bottomY
        }

        canvas.drawRect(leftBarRect, leftBarPaint)
        canvas.drawRect(rightBarRect, rightBarPaint)
    }

    private fun getBarPaint(@ColorInt color: Int) = Paint().apply {
        this.color = color
        style = Paint.Style.FILL
        isAntiAlias = true
    }
}
