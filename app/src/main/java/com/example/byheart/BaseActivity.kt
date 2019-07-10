package com.example.byheart

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.byheart.shared.IOnBackPressed
import com.example.byheart.shared.Preferences

/**
 * Base activity class.
 * @author Bryan de Ridder
 */
open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Preferences.init(applicationContext)
        super.onCreate(savedInstanceState)
    }

    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.main_container)
        if (!(fragment as? IOnBackPressed)?.onBackPressed()!!) {
            super.onBackPressed()
        }
    }
}