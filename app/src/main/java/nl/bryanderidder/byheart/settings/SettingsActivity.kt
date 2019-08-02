package nl.bryanderidder.byheart.settings

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.shared.Preferences

/**
 * Activity containing all settings of the application.
 * @author Bryana de Ridder
 */
class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setupTheme()
        setContentView(R.layout.activity_settings)
        setSupportActionBar(settingsToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        settingsToolbar.setNavigationOnClickListener { onBackPressed() }
        supportFragmentManager
            .beginTransaction()
            .replace(R.id.settings, SettingsFragment())
            .commit()
    }

    private fun setupTheme() {
        if (Preferences.DARK_MODE) setTheme(R.style.PreferenceThemeDark)
    }
}