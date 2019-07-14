package nl.bryanderidder.byheart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import nl.bryanderidder.byheart.card.CardViewModel
import nl.bryanderidder.byheart.pile.PileFragment
import nl.bryanderidder.byheart.shared.IOnBackPressed
import nl.bryanderidder.byheart.shared.Preferences
import nl.bryanderidder.byheart.shared.Preferences.DARK_MODE
import nl.bryanderidder.byheart.shared.startFragment

/**
 * Main entry point of the application.
 * @author Bryan de Ridder
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        ViewModelProviders.of(this).get(CardViewModel::class.java)
        Preferences.init(applicationContext)
        super.onCreate(savedInstanceState)
        setupTheme()
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportFragmentManager.startFragment(PileFragment())
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    private fun setupTheme() {
        val darkMode = Preferences.read(DARK_MODE)
        if (darkMode) setTheme(R.style.AppThemeDark)
        else setTheme(R.style.AppTheme)
    }

    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.main_container)
        if (!(fragment as? IOnBackPressed)?.onBackPressed()!!) {
            super.onBackPressed()
        }
    }
}
