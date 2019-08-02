package nl.bryanderidder.byheart.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.about.AboutActivity
import nl.bryanderidder.byheart.card.CardViewModel
import nl.bryanderidder.byheart.pile.PileViewModel


/**
 * Fragment for settings up general settings.
 * @author Bryan de Ridder
 */
class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var pileVM: PileViewModel
    private lateinit var cardVM: CardViewModel

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        pileVM = ViewModelProviders.of(this).get(PileViewModel::class.java)
        cardVM = ViewModelProviders.of(activity!!).get(CardViewModel::class.java)
        addEventHandlers()
    }

    private fun addEventHandlers() {
        onClick("exportBackup") { exportBackup() }
        onClick("importBackup") { requestFile() }
        onClick("importCSV") { requestFile() }
        onClickGoToUrl("bugreport", getString(R.string.url_github_issue))
        onClickGoToUrl("coffeeLink", getString(R.string.url_bunq))
        onClickGoToUrl("rate", getString(R.string.play_store_url))
        onClick("about") { startActivity(Intent(context, AboutActivity::class.java)) }
    }

    private fun exportBackup() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    private fun requestFile() {
        activity?.setResult(REQUEST_FILE)
        activity?.finish()
    }

    private fun onClick(key: String, action: () -> Unit) {
        findPreference<Preference>(key)?.setOnPreferenceClickListener {
            action.invoke().run { true }
        }
    }

    private fun onClickGoToUrl(key: String, url: String) {
        findPreference<Preference>(key)?.setOnPreferenceClickListener {
            val uriUrl = Uri.parse(url)
            val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
            launchBrowser.addCategory(Intent.CATEGORY_BROWSABLE)
            startActivity(launchBrowser)
            true
        }
    }
    companion object {
        const val REQUEST_FILE = 1
    }
}