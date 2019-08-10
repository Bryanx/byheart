package nl.bryanderidder.byheart

import android.content.Intent
import android.net.Uri
import android.view.View
import android.widget.ProgressBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import nl.bryanderidder.byheart.card.Card
import nl.bryanderidder.byheart.card.CardViewModel
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.pile.PileFragment
import nl.bryanderidder.byheart.pile.PileViewModel
import nl.bryanderidder.byheart.shared.SessionViewModel
import nl.bryanderidder.byheart.shared.getExtension
import nl.bryanderidder.byheart.shared.startFragment
import nl.bryanderidder.byheart.shared.utils.IoUtils

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
    private var resultCode = 0

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        println("requestCode = $requestCode, resultCode= $resultCode")
        when (resultCode) {
            RESULT_CANCELED -> {
                startFragment(PileFragment())
                return
            }
            RESULT_CSV -> this.resultCode = RESULT_CSV
            RESULT_JSON -> this.resultCode = RESULT_JSON
        }
        when (requestCode) {
            REQUEST_OPEN_FILE -> handleFileOpening(intent?.data)
            REQUEST_PICK_FILE -> openDocumentPicker()
            else -> startFragment(PileFragment())
        }
    }

    private fun openDocumentPicker() {
        val chooseFile = Intent(Intent.ACTION_GET_CONTENT).apply {
            addCategory(Intent.CATEGORY_OPENABLE)
            type = "*/*"
        }
        val intent = Intent.createChooser(chooseFile, "Choose a file")
        startActivityForResult(intent, REQUEST_OPEN_FILE)
    }

    fun handleFileOpening(uri: Uri?, viaShare: Boolean = false) {
        if (uri == null) return
        when {
            uri.toString().getExtension() == "csv" || resultCode == RESULT_CSV -> {
                val pile = Pile("CSV import")
                pile.cards = IoUtils.readCSV(contentResolver?.openInputStream(uri))
                insertPileWithCards(listOf(pile), viaShare)
            }
            else -> try {
                val piles = IoUtils.readJson(contentResolver?.openInputStream(uri))
                insertPileWithCards(piles.toList(), viaShare)
            } catch (e: JsonSyntaxException) {
                //TODO: Add snackbar...
//                Snackbar.make(currentFocus!!, "File could not be read.", Snackbar.LENGTH_SHORT)
                e.printStackTrace()
            }
        }
    }

    private fun insertPileWithCards(piles: List<Pile>, viaShare: Boolean) = GlobalScope.launch {
        piles.forEach { pile ->
            val id = pileVM.insert(pile)
            val newCards = pile.cards.map { Card(it.question, it.answer, id) }
            pile.cards.clear()
            cardVM.insertAll(newCards)
            sessionVm.pileId.postValue(id)
            sessionVm.pileName.postValue(pile.name)
        }
        runOnUiThread {
            val bar = findViewById<ProgressBar>(R.id.progressBar)
            bar.visibility = View.GONE
            if (viaShare) startFragment(PileFragment())
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
        const val RESULT_CSV = 1
        const val RESULT_JSON = 2
        const val REQUEST_OPEN_FILE = 3
        const val REQUEST_PICK_FILE = 4
    }
}