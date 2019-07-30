package nl.bryanderidder.byheart.settings

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import nl.bryanderidder.byheart.R

/**
 * Fragment for settings up general settings.
 * @author Bryan de Ridder
 */
class SettingsFragment : PreferenceFragmentCompat() {
    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.root_preferences, rootKey)
    }
}