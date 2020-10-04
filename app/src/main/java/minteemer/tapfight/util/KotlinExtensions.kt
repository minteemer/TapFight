package minteemer.tapfight.util

fun <T> lazyUnsafe(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE,initializer)
