package nl.bryanderidder.byheart

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import nl.bryanderidder.byheart.card.Card
import nl.bryanderidder.byheart.card.CardViewModel
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.pile.PileFragment
import nl.bryanderidder.byheart.pile.PileViewModel
import nl.bryanderidder.byheart.shared.IoUtils
import nl.bryanderidder.byheart.shared.SessionViewModel
import nl.bryanderidder.byheart.shared.getExtension
import nl.bryanderidder.byheart.shared.startFragment

/**
 * Basefragment contains basic functionally of all activities.
 * @author Bryan de Ridder
 */
open class BaseActivity : AppCompatActivity() {

    private val sessionVm: SessionViewModel by lazy {
        ViewModelProviders.of(this).get(SessionViewModel::class.java)
    }
    private val cardVM: CardViewModel by lazy {
        ViewModelProviders.of(this).get(CardViewModel::class.java)
    }
    private val pileVM: PileViewModel by lazy {
        ViewModelProviders.of(this).get(PileViewModel::class.java)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        when {
            requestCode == REQUEST_FILE -> openDocumentPicker()
            requestCode == OPEN_FILE -> handleFileOpening(intent?.data)
        }
    }

    private fun openDocumentPicker() {
        val chooseFile = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        val intent = Intent.createChooser(chooseFile, "Choose a file")
        startActivityForResult(intent, OPEN_FILE)
    }

    fun handleFileOpening(uri: Uri?, isImport: Boolean = false) {
        if (uri == null) return
        when (uri.toString().getExtension()) {
            "csv" -> {
                val pile = Pile("CSV import")
                insertPileWithCards(pile, IoUtils.readCSV(contentResolver?.openInputStream(uri)), isImport)
            }
            else -> {
                val pile = IoUtils.readJson(contentResolver?.openInputStream(uri))
                insertPileWithCards(pile, pile.cards.map { Card(it.question, it.answer) }, isImport)
            }
        }
    }

    private fun insertPileWithCards(pile: Pile, cards: List<Card>, isImport: Boolean) = GlobalScope.launch {
        val id = pileVM.insert(pile)
        val newCards = cards.map { Card(it.question, it.answer, id) }
        cardVM.insertAll(newCards)
        sessionVm.pileId.postValue(id)
        sessionVm.pileName.postValue(pile.name)
        runOnUiThread {
            val bar = findViewById<ProgressBar>(R.id.progressBar)
            bar.visibility = View.GONE
            if (isImport) startFragment(PileFragment())
        }
    }

    fun startFragment(fragment: Fragment) {
        findViewById<ProgressBar>(R.id.progressBar).visibility = View.GONE
        val activeFragments = supportFragmentManager.fragments
        when {
            activeFragments.isEmpty() -> supportFragmentManager.startFragment(fragment)
            else -> supportFragmentManager.startFragment(activeFragments[0])
        }
    }

    companion object {
        const val REQUEST_FILE = 1
        const val OPEN_FILE = 2
    }
}