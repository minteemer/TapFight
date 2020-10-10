package minteemer.tapfight.ui.view

import android.annotation.SuppressLint
import android.content.Context
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View

class TapAreaView @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attributeSet, defStyleAttr, defStyleRes) {

    init {
        isClickable = true
    }

    @SuppressLint("ClickableViewAccessibility")
    fun setOnTapListener(onTap: (view: View) -> Unit) =
        setOnTouchListener { view, event ->
            val maskedAction = event.actionMasked
            if (maskedAction == MotionEvent.ACTION_DOWN || maskedAction == MotionEvent.ACTION_POINTER_DOWN) {
                onTap(view)
            }

            false // touch event is not consumed
        }

}
