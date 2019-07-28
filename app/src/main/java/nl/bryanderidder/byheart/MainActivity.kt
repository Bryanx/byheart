package nl.bryanderidder.byheart

import android.content.ContentResolver
import android.content.Intent
import android.os.Bundle
import android.view.View.GONE
import android.view.WindowManager
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import nl.bryanderidder.byheart.card.Card
import nl.bryanderidder.byheart.card.CardFragment
import nl.bryanderidder.byheart.card.CardViewModel
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.pile.PileFragment
import nl.bryanderidder.byheart.pile.PileViewModel
import nl.bryanderidder.byheart.shared.*
import java.io.BufferedReader

/**
 * Main entry point of the application.
 * @author Bryan de Ridder
 */
class MainActivity : AppCompatActivity() {

    private lateinit var cardVM: CardViewModel
    private lateinit var pileVM: PileViewModel
    private lateinit var sessionVm: SessionViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        ViewModelProviders.of(this).get(CardViewModel::class.java)
        Preferences.init(applicationContext)
        super.onCreate(savedInstanceState)
        window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN)
        setupTheme()
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)
        toolbar.setNavigationOnClickListener { onBackPressed() }
        pileVM = ViewModelProviders.of(this).get(PileViewModel::class.java)
        cardVM = ViewModelProviders.of(this).get(CardViewModel::class.java)
        sessionVm = ViewModelProviders.of(this).get(SessionViewModel::class.java)
        intent?.let { checkFileOpening(it) }
    }

    private fun setupTheme() {
        if (Preferences.DARK_MODE) setTheme(R.style.AppThemeDark)
        else setTheme(R.style.AppTheme)
    }

    // Check if app was openened with a file
    private fun checkFileOpening(intent: Intent) {
        if (intent.action == Intent.ACTION_VIEW && (intent.scheme == ContentResolver.SCHEME_FILE
                    || intent.scheme == ContentResolver.SCHEME_CONTENT)) {
            intent.data?.let {
                contentResolver?.openInputStream(it)?.bufferedReader()
                    ?.use(BufferedReader::readText)?.let { data ->
                        insertPileWithCards(IoUtils.readJson(data))
                    }
            }
        } else {
            findViewById<ProgressBar>(R.id.progressBar).visibility = GONE
            supportFragmentManager.startFragment(PileFragment())
        }
    }

    private fun insertPileWithCards(pile: Pile) {
        GlobalScope.launch {
            val id = pileVM.insert(pile)
            val newCards = pile.cards.map { Card(it.question, it.answer, id) }
            cardVM.insertAll(newCards)
            sessionVm.pileId.postValue(id)
            sessionVm.pileName.postValue(pile.name)
            runOnUiThread {
                findViewById<ProgressBar>(R.id.progressBar).visibility = GONE
            }
            supportFragmentManager.startFragment(CardFragment())
        }
    }

    override fun onBackPressed() {
        val fragment = this.supportFragmentManager.findFragmentById(R.id.main_container)
        if (!(fragment as? IOnBackPressed)?.onBackPressed()!!) {
            super.onBackPressed()
        }
    }
}
