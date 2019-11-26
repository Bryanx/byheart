package nl.bryanderidder.byheart.shared

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

open class CoroutineProvider {
    open val Main: CoroutineContext by lazy { Dispatchers.Main }
    open val IO: CoroutineContext by lazy { Dispatchers.IO }
    open val Default: CoroutineContext by lazy { Dispatchers.Default }
}