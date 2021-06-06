package minteemer.tapfight.util.extensions.viewbinding

import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.OnLifecycleEvent
import androidx.viewbinding.ViewBinding
import kotlin.properties.ReadOnlyProperty
import kotlin.reflect.KProperty

/**
 * @author t.valiev
 */
class ActivityViewBindingDelegate<VB : ViewBinding>(
    private val activity: AppCompatActivity,
    private val inflate: (LayoutInflater) -> VB
) : ReadOnlyProperty<AppCompatActivity, VB>, LifecycleObserver {

    private var viewBinding: VB? = null

    init {
        activity.lifecycle.addObserver(this)
    }

    override fun getValue(thisRef: AppCompatActivity, property: KProperty<*>): VB = getOrCreateBinding()

    @OnLifecycleEvent(Lifecycle.Event.ON_CREATE)
    fun onCreate() {
        activity.setContentView(getOrCreateBinding().root)
    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    fun onDestroy() {
        viewBinding = null
    }

    private fun getOrCreateBinding(): VB = viewBinding ?: inflate(activity.layoutInflater).also { viewBinding = it }
}

fun <VB : ViewBinding> AppCompatActivity.viewBinding(inflate: (LayoutInflater) -> VB): ReadOnlyProperty<AppCompatActivity, VB> =
    ActivityViewBindingDelegate(this, inflate)


