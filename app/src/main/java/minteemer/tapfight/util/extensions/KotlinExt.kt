package minteemer.tapfight.util.extensions

/** Shortcut for using lazy delegate without thread safety */
fun <T> fastLazy(initializer: () -> T) = lazy(LazyThreadSafetyMode.NONE, initializer)
