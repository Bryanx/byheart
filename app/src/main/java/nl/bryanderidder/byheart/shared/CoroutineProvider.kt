package nl.bryanderidder.byheart.shared

import kotlinx.coroutines.Dispatchers
import kotlin.coroutines.CoroutineContext

/**
 * Provides coroutines (threads) to viewmodels.
 * This class can be overwritten so i.e. tests can be run on a different thread.
 * @author Bryan de Ridder
 */
open class CoroutineProvider {

    // Main thread, used to manipulate UI objects.
    open val Main: CoroutineContext by lazy { Dispatchers.Main }

    // Input output thread, used to load data from external sources so it doesn't block the main thread.
    open val IO: CoroutineContext by lazy { Dispatchers.IO }

    // Default thread differs per VM.
    open val Default: CoroutineContext by lazy { Dispatchers.Default }

}