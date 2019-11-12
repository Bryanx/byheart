package nl.bryanderidder.byheart.about

import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_about.*
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.shared.color
import nl.bryanderidder.byheart.shared.getAttr

/**
 * In this fragment piles are editing or created.
 * @author Bryan de Ridder
 */
class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setTheme(R.style.AboutTheme)
        setContentView(R.layout.activity_about)
        setSupportActionBar(aboutToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)
        aboutToolbar.setNavigationOnClickListener { onBackPressed() }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            window.decorView.systemUiVisibility = View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv() and View.SYSTEM_UI_FLAG_VISIBLE
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = color(R.color.colorPrimary)
        }
        addEventHandlers()
    }

    private fun addEventHandlers() {
        rateUs.setOnClickListener { goToUrl(getString(R.string.play_store_url))}
        buyCoffee.setOnClickListener { goToUrl( getString(R.string.url_bunq))}
        createIssue.setOnClickListener { goToUrl( getString(R.string.url_github_issue))}
    }

    override fun onBackPressed() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window?.statusBarColor = getAttr(R.attr.mainBackgroundColor)
        }
        super.onBackPressed()
    }

    private fun goToUrl(url: String) {
        val uriUrl = Uri.parse(url)
        val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
        launchBrowser.addCategory(Intent.CATEGORY_BROWSABLE)
        startActivity(launchBrowser)
    }
}
