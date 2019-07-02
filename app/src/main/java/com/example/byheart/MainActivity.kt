package com.example.byheart

import android.os.Bundle
import com.example.byheart.pile.PileFragment
import com.example.byheart.shared.Preferences
import com.example.byheart.shared.Preferences.DARK_MODE
import com.example.byheart.shared.startFragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : BaseActivity() {

    var pileId: String = ""
    var pileName: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val darkMode = Preferences.read(DARK_MODE, false)
        if (darkMode) setTheme(R.style.AppThemeDark)
        else setTheme(R.style.AppTheme)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        supportFragmentManager.startFragment(PileFragment())
        toolbar.setNavigationOnClickListener { onBackPressed() }
    }

    override fun onBackPressed() {
        supportFragmentManager.startFragment(PileFragment())
    }
}
