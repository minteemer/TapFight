package minteemer.tapfight.util.extensions.viewbinding

import android.os.Handler
import android.os.Looper
import android.view.View
import androidx.annotation.MainThread
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author t.valiev
 */
class FragmentViewBindingDelegate<VB : ViewBinding>(
    private val viewBinder: (View) -> VB
) : ReadOnlyProperty<Fragment, VB>, LifecycleObserver {

    private var viewBinding: VB? = null

    @MainThread
    override fun getValue(thisRef: Fragment, property: KProperty<*>): VB = viewBinding ?: initViewBinding(thisRef)

    private fun initViewBinding(fragment: Fragment): VB {
        val view = fragment.requireView()
        fragment.viewLifecycleOwner.lifecycle.addObserver(this)

        return viewBinder(view).also { viewBinding = it }
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy(owner: LifecycleOwner) {
        owner.lifecycle.removeObserver(this)

        Handler(Looper.getMainLooper()).post {
            viewBinding = null
        }
    }
}

fun <VB : ViewBinding> Fragment.viewBinding(viewBinder: (View) -> VB): ReadOnlyProperty<Fragment, VB> =
    FragmentViewBindingDelegate(viewBinder)
