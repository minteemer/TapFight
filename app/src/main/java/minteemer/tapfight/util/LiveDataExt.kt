package minteemer.tapfight.util

import androidx.lifecycle.MutableLiveData

fun <T> MutableLiveData<T>.updateValue(value: T) {
    if (value != this.value) {
        this.value = value
    }
}