package nl.bryanderidder.byheart.settings

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_settings.*
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.auth.AuthViewModel
import nl.bryanderidder.byheart.auth.LoginFragment
import nl.bryanderidder.byheart.auth.REQUEST_SIGN_IN
import nl.bryanderidder.byheart.shared.Preferences
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Activity containing all settings of the application.
 * @author Bryana de Ridder
 */
class SettingsActivity : AppCompatActivity() {

    private val authVM: AuthViewModel by viewModel()

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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_SIGN_IN)
            authVM.onActivityResult(this, requestCode, data) {
                supportFragmentManager.fragments
                    .filterIsInstance<LoginFragment>()
                    .firstOrNull()?.onAfterLogin()
            }
    }

    override fun onBackPressed() {
        setResult(RESULT_CANCELED)
        super.onBackPressed()
    }
}