package nl.bryanderidder.byheart.settings

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import nl.bryanderidder.byheart.BaseActivity.Companion.RESULT_CSV
import nl.bryanderidder.byheart.BaseActivity.Companion.RESULT_JSON
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.about.AboutActivity
import nl.bryanderidder.byheart.card.CardViewModel
import nl.bryanderidder.byheart.pile.PileViewModel
import nl.bryanderidder.byheart.shared.utils.IoUtils
import org.koin.android.viewmodel.ext.android.sharedViewModel


/**
 * Fragment for settings up general settings.
 * @author Bryan de Ridder
 */
class SettingsFragment : PreferenceFragmentCompat() {

    private val cardVM: CardViewModel by sharedViewModel()
    private val pileVM: PileViewModel by sharedViewModel()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        addEventHandlers()
    }

    private fun addEventHandlers() {
        onClick("exportBackup") { exportBackup() }
        onClick("importBackup") { requestFile(RESULT_JSON) }
        onClick("importCSV") { requestFile(RESULT_CSV) }
        onClickGoToUrl("bugreport", getString(R.string.url_github_issue))
        onClickGoToUrl("chat", getString(R.string.url_chat))
        onClickGoToUrl("rate", getString(R.string.play_store_url))
        onClick("about") { startActivity(Intent(context, AboutActivity::class.java)) }
    }

    private fun exportBackup() {
        pileVM.allPiles.observe(this, Observer {piles ->
            cardVM.allCards.observe(this, Observer {cards ->
                piles.forEach { it.cards.addAll(cards.filter { c -> c.pileId == it.id }) }
                IoUtils.createJson(context!!, piles.toTypedArray(), "Byheart-backup.byheart")
            })
        })
    }

    private fun requestFile(requestCode: Int) {
        activity?.setResult(requestCode)
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
}