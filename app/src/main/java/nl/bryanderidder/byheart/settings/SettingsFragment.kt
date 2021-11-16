package nl.bryanderidder.byheart.settings

import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.preference.Preference
import androidx.preference.PreferenceCategory
import androidx.preference.PreferenceFragmentCompat
import androidx.preference.SeekBarPreference
import nl.bryanderidder.byheart.BaseActivity.Companion.RESULT_CSV
import nl.bryanderidder.byheart.BaseActivity.Companion.RESULT_JSON
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.about.AboutActivity
import nl.bryanderidder.byheart.auth.AuthViewModel
import nl.bryanderidder.byheart.auth.LoginFragment
import nl.bryanderidder.byheart.card.CardViewModel
import nl.bryanderidder.byheart.pile.PileViewModel
import nl.bryanderidder.byheart.shared.Preferences
import nl.bryanderidder.byheart.shared.utils.IoUtils
import nl.bryanderidder.byheart.shared.utils.goToUrl
import org.koin.android.viewmodel.ext.android.sharedViewModel


/**
 * Fragment for settings up general settings.
 * @author Bryan de Ridder
 */
class SettingsFragment : PreferenceFragmentCompat() {

    private lateinit var signOutCategory: PreferenceCategory
    private lateinit var signOutPreference: Preference
    private lateinit var signInPreference: Preference
    private val cardVM: CardViewModel by sharedViewModel()
    private val pileVM: PileViewModel by sharedViewModel()
    private val authVM: AuthViewModel by sharedViewModel()

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
        signOutPreference = findPreference("sign_out")!!
        signInPreference = findPreference("sign_in")!!
        signOutCategory = findPreference("sign_out_category")!!
        if (authVM.isSignedIn.value != false) onSignedIn() else onSignedOut()
        addEventHandlers()
    }

    private fun addEventHandlers() {
        onSlideSeekBar("delayTime") { newValue ->
            Preferences.write(Preferences.KEY_REHEARSAL_DELAY_TIME, newValue as Int)
        }
        onClick("sign_in") { onClickSignIn() }
        onClick("exportBackup") { exportBackup() }
        onClick("importBackup") { requestFile(RESULT_JSON) }
        onClick("importCSV") { requestFile(RESULT_CSV) }
        onClickGoToUrl("bugreport", getString(R.string.url_github_issue))
        onClickGoToUrl("chat", getString(R.string.url_chat))
        onClickGoToUrl("rate", getString(R.string.play_store_url))
        onClick("about") { startActivity(Intent(context, AboutActivity::class.java)) }
        onClickGoToUrl("privacy_policy", getString(R.string.privacy_policy_url))
        onClickGoToUrl("terms_and_conditions", getString(R.string.terms_and_conditions_url))
        onClick("sign_out") { authVM.signOut(requireActivity()) }
        authVM.isSignedIn.observe(this, Observer { if (it) onSignedIn() else onSignedOut() })
    }

    private fun onClickSignIn() {
        LoginFragment()
            .also { authVM.loginMessage.value = getString(R.string.after_logging_in_you_will_be_able_to_share_cards) }
            .also { it.show(requireActivity().supportFragmentManager, it.tag) }
    }

    private fun onSignedIn() {
        signOutCategory.addPreference(signOutPreference)
        signInPreference.isEnabled = false
        signInPreference.title = getString(R.string.welcome_back_name, authVM.currentUser?.displayName)
        signInPreference.summary = "${authVM.currentUser?.email}"
    }

    private fun onSignedOut() {
        signOutCategory.removePreference(signOutPreference)
        signInPreference.isEnabled = true
        signInPreference.title = getString(R.string.common_signin_button_text_long)
        signInPreference.summary = getString(R.string.after_logging_in_you_will_be_able_to_share_cards)
    }

    private fun exportBackup() {
        pileVM.allPiles.observe(this, Observer {piles ->
            cardVM.allCards.observe(this, Observer {cards ->
                piles.forEach { it.cards.addAll(cards.filter { c -> c.pileId == it.id }) }
                IoUtils.createJson(requireContext(), piles.toTypedArray(), "Byheart-backup.byheart")
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

    private fun onSlideSeekBar(key: String, action: (Any) -> Unit) {
        findPreference<SeekBarPreference>(key)?.setOnPreferenceChangeListener { _, newValue: Any ->
            action.invoke(newValue).run { true }
        }
    }

    private fun onClickGoToUrl(key: String, url: String) {
        findPreference<Preference>(key)?.setOnPreferenceClickListener {
            goToUrl(requireActivity(), url)
            true
        }
    }
}