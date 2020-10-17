package minteemer.tapfight.ui.view.bubbles

import android.content.Context
import android.graphics.drawable.Animatable2.AnimationCallback
import android.graphics.drawable.AnimatedVectorDrawable
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import minteemer.tapfight.R
import minteemer.tapfight.ui.util.OnTapListener
import minteemer.tapfight.ui.util.setOnTapListener
import minteemer.tapfight.util.getDrawableCompat


class BubbleView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    private var onTapListener: OnTapListener? = null,
    private val onTerminalStateReached: ((view: BubbleView) -> Unit)? = null
) : AppCompatImageView(context, attributeSet, defStyleAttr) {

    private val lifetimeDrawable: AnimatedVectorDrawable = context.getDrawableCompat(R.drawable.bubble_lifetime) as AnimatedVectorDrawable
    private val timeoutDrawable: AnimatedVectorDrawable = context.getDrawableCompat(R.drawable.bubble_timeout) as AnimatedVectorDrawable
    private val tapDrawable: AnimatedVectorDrawable = context.getDrawableCompat(R.drawable.bubble_tap) as AnimatedVectorDrawable

    init {
        setOnTapListener { view ->
            onTapListener?.invoke(view)
            toTerminalState(tapDrawable)
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
        isClickable = false
        setImageDrawable(animDrawable)
        animDrawable.start()
        animDrawable.registerAnimationCallback(terminalStateAnimationCallback)
        onTapListener = null
    }

    private val terminalStateAnimationCallback: AnimationCallback =
        object : AnimationCallback() {
            override fun onAnimationEnd(drawable: Drawable?) {
                onTerminalStateReached?.invoke(this@BubbleView)
            }
        }
}