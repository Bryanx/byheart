package nl.bryanderidder.byheart

import android.content.ContentResolver.SCHEME_CONTENT
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import kotlinx.android.synthetic.main.activity_main.*
import nl.bryanderidder.byheart.pile.PileFragment
import nl.bryanderidder.byheart.shared.IOnBackPressed
import nl.bryanderidder.byheart.shared.Preferences

/**
 * Main entry point of the application.
 * @author Bryan de Ridder
 */
class MainActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Preferences.init(applicationContext)
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setupTheme()
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        intent?.let { checkFileOpening(it) }
    }

    private fun setupTheme() {
        if (Preferences.DARK_MODE) setTheme(R.style.AppThemeDark)
        else setTheme(R.style.AppTheme)
    }

    // Check if app was openened with a file
    private fun checkFileOpening(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW && intent.scheme == SCHEME_HTTPS)
            super.handleUrlOpening(intent.data)
        else if (intent.action == Intent.ACTION_VIEW && intent.scheme == SCHEME_BYHEART)
            super.handleUrlOpening(intent.data)
        else if (intent.action == Intent.ACTION_VIEW && intent.scheme == SCHEME_CONTENT) {
            intent.action = Intent.ACTION_MAIN
            super.handleFileOpening(intent.data, true)
        } else {
            startFragment(PileFragment())
        }
    }

    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.main_container)
        if (!(fragment as? IOnBackPressed)?.onBackPressed()!!) {
            super.onBackPressed()
        }
    }
    companion object {
        const val SCHEME_HTTPS = "https"
        const val SCHEME_BYHEART = "byheart"
    }
}
