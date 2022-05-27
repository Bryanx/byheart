package nl.bryanderidder.byheart

import android.app.Application
import nl.bryanderidder.byheart.shared.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

/**
 * Main Application
 * @author Bryan de Ridder
 */
class MainApplication : Application() {

    override fun onCreate() {
        super.onCreate()

        // start Koin context
        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MainApplication)
            modules(appModule)
        }
    }
}