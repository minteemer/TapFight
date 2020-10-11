package minteemer.tapfight.ui.view.bubbles

import android.content.Context
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import androidx.annotation.ColorInt
import androidx.annotation.Px
import androidx.core.view.children
import androidx.core.view.postDelayed
import minteemer.tapfight.R
import minteemer.tapfight.ui.util.OnTapListener
import minteemer.tapfight.ui.util.setOnTapListener
import kotlin.random.Random

class BubblesSpawnerView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : ViewGroup(context, attributeSet, defStyleAttr, defStyleRes) {

    @ColorInt
    private val bubbleColor: Int

    @Px
    private val bubbleRadius: Int

    private val bubbleAreaWidth: Int
        get() = width - paddingLeft - paddingRight - bubbleRadius * 2

    private val bubbleAreaHeight: Int
        get() = height - paddingTop - paddingBottom - bubbleRadius * 2

    private val bubbleLocations: MutableMap<BubbleView, BubbleLocation> = mutableMapOf()
    private var onBubbleTap: OnTapListener? = null

    // TODO control spawn rate and lifetime from game logic / config
    var bubbleSpawnDelayMills: Long = 200
    var bubbleLifetimeMills: Long = 1000

    init {
        val attr = context.obtainStyledAttributes(attributeSet, R.styleable.BubblesSpawnerView, defStyleAttr, defStyleRes)

        bubbleColor = attr.getColor(R.styleable.BubblesSpawnerView_bubbleColor, context.getColor(R.color.primary))
        bubbleRadius = attr.getDimensionPixelSize(R.styleable.BubblesSpawnerView_bubbleRadius, 1)

        attr.recycle()
    }

    fun startSpawning() {
        post(::spawnBubble)
    }

    fun setOnBubbleTapListener(onTap: OnTapListener) {
        onBubbleTap = onTap
    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val bubbleAreaLeftBorder = paddingLeft + bubbleRadius
        val bubbleAreaTopBorder = paddingTop + bubbleRadius

        children.forEach { bubble ->
            bubbleLocations[bubble]?.let { location ->
                bubble.layout(
                    location.centerX + bubbleAreaLeftBorder - bubbleRadius,
                    location.centerY + bubbleAreaTopBorder - bubbleRadius,
                    location.centerX + bubbleAreaLeftBorder + bubbleRadius,
                    location.centerY + bubbleAreaTopBorder + bubbleRadius
                )
            }
        }
    }

    private fun spawnBubble() {
        // TODO BubbleView recycling?
        val bubbleView = BubbleView(context).apply {
            color = bubbleColor
            setOnTapListener(::onBubbleTap)
        }
        bubbleLocations[bubbleView] = BubbleLocation.random(bubbleAreaWidth, bubbleAreaHeight)
        addView(bubbleView)

        // TODO removeCallbacks ?
        postDelayed(bubbleSpawnDelayMills, ::spawnBubble)
        postDelayed(bubbleLifetimeMills) {
            removeView(bubbleView)
            bubbleLocations.remove(bubbleView)
        }
    }

    private fun onBubbleTap(view: View) {
        removeView(view)
        onBubbleTap?.invoke(view)
    }


    private class BubbleLocation(
        var centerX: Int = 0,
        var centerY: Int = 0
    ) {
        companion object {
            fun random(areaWidth: Int, areaHeight: Int) = BubbleLocation(
                centerX = Random.nextInt(0, areaWidth),
                centerY = Random.nextInt(0, areaHeight)
            )
        }
    }
}
