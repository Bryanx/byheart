package com.example.byheart

import android.os.Bundle
import android.view.View
import android.widget.RelativeLayout
import androidx.appcompat.app.AppCompatActivity
import com.example.byheart.shared.Preferences

open class BaseActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        Preferences.init(applicationContext)
        changeStatusBar()
        super.onCreate(savedInstanceState)
    }

    private fun changeStatusBar() {
        // make status bar white if device has required api version
    }
}