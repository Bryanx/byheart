package nl.bryanderidder.byheart

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import nl.bryanderidder.byheart.shared.color

/**
 * Activity that is shown on startup.
 * @author Bryan de Ridder
 */
class SplashScreenActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP)
            window.statusBarColor = color(R.color.colorPrimary)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}