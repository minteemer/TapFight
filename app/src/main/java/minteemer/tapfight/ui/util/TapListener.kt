package minteemer.tapfight.ui.util

import android.annotation.SuppressLint
import android.view.MotionEvent
import android.view.View

typealias OnTapListener = (view: View) -> Unit

class TapDetector(private val onTap: OnTapListener) : View.OnTouchListener {

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouch(view: View, event: MotionEvent): Boolean {
        val maskedAction = event.actionMasked
        if (maskedAction == MotionEvent.ACTION_DOWN || maskedAction == MotionEvent.ACTION_POINTER_DOWN) {
            onTap(view)
        }

        return false // touch event is not consumed
    }
}

fun View.setOnTapListener(onTap: OnTapListener) = setOnTouchListener(TapDetector(onTap))