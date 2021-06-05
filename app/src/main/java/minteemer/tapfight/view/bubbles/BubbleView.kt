package minteemer.tapfight.view.bubbles

import android.content.Context
import android.graphics.drawable.Animatable2.AnimationCallback
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import minteemer.tapfight.R
import minteemer.tapfight.util.ui.setOnTapHandler
import minteemer.tapfight.util.extensions.getDrawableCompat


class BubbleView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private var onTapListener: (() -> Unit)? = null,
    private val onTerminalStateReached: ((view: BubbleView) -> Unit)? = null
) : AppCompatImageView(context, attributeSet, defStyleAttr) {

    private val lifetimeDrawable: AnimatedVectorDrawable = context.getDrawableCompat(R.drawable.bubble_lifetime) as AnimatedVectorDrawable
    private val timeoutDrawable: AnimatedVectorDrawable = context.getDrawableCompat(R.drawable.bubble_timeout) as AnimatedVectorDrawable
    private val tapDrawable: AnimatedVectorDrawable = context.getDrawableCompat(R.drawable.bubble_tap) as AnimatedVectorDrawable

    init {
        setOnTapHandler { view ->
            onTapListener?.invoke()
            toTerminalState(tapDrawable)
            true
        }
    }

    fun startLifetimeAnimation() {
        isClickable = true
        setImageDrawable(lifetimeDrawable)
        lifetimeDrawable.start()
    }

    fun startTimeoutAnimation() {
        toTerminalState(timeoutDrawable)
    }

    private fun toTerminalState(animDrawable: AnimatedVectorDrawable) {
        onTapListener = null
        isClickable = false
        setImageDrawable(animDrawable)
        animDrawable.start()
        animDrawable.registerAnimationCallback(terminalStateAnimationCallback)
    }

    private val terminalStateAnimationCallback: AnimationCallback =
        object : AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                onTerminalStateReached?.invoke(this@BubbleView)
            }
        }
}