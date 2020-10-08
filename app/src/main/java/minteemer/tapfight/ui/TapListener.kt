package minteemer.tapfight.ui

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import android.view.View

@SuppressLint("ClickableViewAccessibility")
fun View.setOnTapListener(onTap: (view: View) -> Unit) {
    setOnTouchListener { view, event ->
        val maskedAction = event.actionMasked
        if (maskedAction == MotionEvent.ACTION_DOWN || maskedAction == MotionEvent.ACTION_POINTER_DOWN) {
            onTap(view)
        }

        false // touch event is not consumed
    }
}
