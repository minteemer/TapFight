package minteemer.tapfight.util.ui

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

/**
 * returns true if event is consumed, false to pass event to next listener
 */
typealias OnTapHandler = (view: View) -> Boolean

class TapDetector(private val onTap: OnTapHandler) : View.OnTouchListener {

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        val maskedAction = event.actionMasked
        // FIXME multiple fingers issue?
        return if (maskedAction == MotionEvent.ACTION_DOWN || maskedAction == MotionEvent.ACTION_POINTER_DOWN) {
            onTap(view)
        } else {
            false // touch event is not consumed
        }
    }
}

fun View.setTapHandler(onTap: OnTapHandler) = setOnTouchListener(TapDetector(onTap))
fun View.resetTapHandler() = setOnTouchListener(null)
