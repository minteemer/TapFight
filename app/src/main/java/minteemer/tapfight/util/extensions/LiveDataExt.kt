package minteemer.tapfight.util.extensions

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.setValueIfChanged(value: T) {
    if (value != this.value) {
        this.value = value
    }
}