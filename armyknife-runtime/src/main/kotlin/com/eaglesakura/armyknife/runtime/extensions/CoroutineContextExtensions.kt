package com.eaglesakura.armyknife.runtime.extensions

import java.time.Duration
import kotlin.coroutines.CoroutineContext
import kotlin.coroutines.coroutineContext
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Job
import kotlinx.coroutines.async

/**
 * Returns an job object from coroutines.
 *
 * @author @eaglesakura
 * @link https://github.com/eaglesakura/armyknife-runtime
 */
val CoroutineContext.job: Job
    get() = this[Job]!!

/**
 * Delay duration.
 */
suspend fun delay(duration: Duration) {
    kotlinx.coroutines.delay(duration.toMillis())
}

/**
 * run suspend function in child coroutine context.
 *
 * e.g.)
 * suspend fun example() {
 *      withChildContext(Dispatchers.IO) {
 *          try {
 *              // do heavy something...
 *          } catch(e: CancellationException) {
 *              log("cancel by parent coroutineContext")
 *              throw e
 *          }
 *      }
 * }
 */
@Deprecated("will remove this function")
suspend fun <T> withChildContext(
    context: CoroutineContext,
    block: suspend CoroutineScope.() -> T
): T {
    val scope = CoroutineScope(coroutineContext)
    val deferred: Deferred<Pair<Any?, Throwable?>> = scope.async(context) {
        try {
            Pair(block(this), null)
        } catch (e: Throwable) {
            Pair(Unit, e)
        }
    }

    try {
        return deferred.await().let {
            if (it.second != null) {
                throw it.second!!
            }
            it.first as T
        }
    } catch (e: CancellationException) {
        deferred.cancel(CancellationException("withContextChild(context=$context/$e)"))
        throw e
    }
}
