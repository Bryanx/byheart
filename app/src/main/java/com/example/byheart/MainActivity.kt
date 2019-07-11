package com.example.byheart

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.byheart.pile.PileFragment
import com.example.byheart.shared.IOnBackPressed
import com.example.byheart.shared.Preferences
import com.example.byheart.shared.Preferences.DARK_MODE
import com.example.byheart.shared.Preferences.NOT_FIRST_START
import com.example.byheart.shared.Preferences.REHEARSAL_MEMORY
import com.example.byheart.shared.startFragment
import kotlinx.android.synthetic.main.activity_main.*

/**
 * Main entry point of the application.
 * @author Bryan de Ridder
 */
class MainActivity : AppCompatActivity() {

    var pileId: String = ""
    var pileName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        Preferences.init(applicationContext)
        super.onCreate(savedInstanceState)
        handleFirstStart()
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

    private fun handleFirstStart() {
        if (!Preferences.read(NOT_FIRST_START)) {
            Preferences.write(NOT_FIRST_START, true)
            Preferences.write(REHEARSAL_MEMORY, true)
        }
    }

    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.main_container)
        if (!(fragment as? IOnBackPressed)?.onBackPressed()!!) {
            super.onBackPressed()
        }
    }
}
