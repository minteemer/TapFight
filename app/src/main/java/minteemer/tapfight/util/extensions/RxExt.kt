package minteemer.tapfight.util.extensions

import io.reactivex.Observable
import java.util.concurrent.TimeUnit

fun <T> Observable<T>.completeAfter(delay: Long, timeUnit: TimeUnit) =
    takeUntil(Observable.empty<T>().delay(delay, timeUnit))