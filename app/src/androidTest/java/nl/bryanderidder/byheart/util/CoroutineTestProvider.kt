package nl.bryanderidder.byheart.util

import kotlinx.coroutines.Dispatchers
import nl.bryanderidder.byheart.shared.CoroutineProvider
import kotlin.coroutines.CoroutineContext

open class CoroutineTestProvider : CoroutineProvider() {
    override val Main: CoroutineContext by lazy { Dispatchers.Unconfined }
    override val IO: CoroutineContext by lazy { Dispatchers.Unconfined }
    override val Default: CoroutineContext by lazy { Dispatchers.Unconfined }
}