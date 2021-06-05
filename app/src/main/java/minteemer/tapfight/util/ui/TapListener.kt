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
        if (maskedAction == MotionEvent.ACTION_DOWN || maskedAction == MotionEvent.ACTION_POINTER_DOWN) {
            return onTap(view)
        }

        return false // touch event is not consumed
    }
}

fun View.setOnTapHandler(onTap: OnTapHandler) = setOnTouchListener(TapDetector(onTap))