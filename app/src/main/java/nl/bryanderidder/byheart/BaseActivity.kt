package nl.bryanderidder.byheart

import android.content.Intent
import android.net.Uri
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.google.firebase.crashlytics.FirebaseCrashlytics
import com.google.gson.JsonSyntaxException
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import nl.bryanderidder.byheart.auth.AuthViewModel
import nl.bryanderidder.byheart.auth.LoginFragment
import nl.bryanderidder.byheart.card.Card
import nl.bryanderidder.byheart.card.CardViewModel
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.pile.PileFragment
import nl.bryanderidder.byheart.pile.PileViewModel
import nl.bryanderidder.byheart.rehearsal.RehearsalViewModel
import nl.bryanderidder.byheart.shared.*
import nl.bryanderidder.byheart.shared.exceptions.ByheartException
import nl.bryanderidder.byheart.shared.firestore.FireStoreViewModel
import nl.bryanderidder.byheart.shared.utils.IoUtils
import org.koin.android.viewmodel.ext.android.viewModel

/**
 * Basefragment contains basic functionally of all activities.
 * @author Bryan de Ridder
 */
open class BaseActivity : AppCompatActivity() {

    private val sessionVM: SessionViewModel by viewModel()
    private val cardVM: CardViewModel by viewModel()
    private val pileVM: PileViewModel by viewModel()
    private val fireStoreVM: FireStoreViewModel by viewModel()
    private val authVM: AuthViewModel by viewModel()
    private val rehearsalViewModel: RehearsalViewModel by viewModel()
    private var resultCode = 0

    private val loginFragment: LoginFragment?
        get() = supportFragmentManager.fragments
            .filterIsInstance<LoginFragment>()
            .firstOrNull()

    override fun onActivityResult(requestCode: Int, resultCode: Int, intent: Intent?) {
        super.onActivityResult(requestCode, resultCode, intent)
        if (requestCode == REQUEST_SIGN_IN) {
            loginFragment?.onActivityResult(requestCode, resultCode, intent)
            return
        }
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

    fun handleUrlOpening(uri: Uri?) {
        showMainProgressBar(true)
        lifecycleScope.launch {
            uri?.lastPathSegment?.also { remotePileId ->
                try {
                    val pile = fireStoreVM.getPileAsync(remotePileId).await()
                    insertPileWithCards(listOf(pile), true).await()
                } catch (e: ByheartException) {
                    showErrorDialog(e)
                    FirebaseCrashlytics.getInstance().recordException(e)
                }
            }
        }
    }

    fun handleFileOpening(uri: Uri?, viaShare: Boolean = false) {
        if (uri == null) return
        showMainProgressBar(true)
        lifecycleScope.launch {
            when {
                uri.toString().getExtension() == "csv" || resultCode == RESULT_CSV -> {
                    val pile = Pile("CSV import")
                    pile.cards = IoUtils.readCSV(contentResolver?.openInputStream(uri))
                    insertPileWithCards(listOf(pile), viaShare).await()
                }
                else -> try {
                    val piles = IoUtils.readJson(contentResolver?.openInputStream(uri))
                    insertPileWithCards(piles.toList(), viaShare).await()
                } catch (e: JsonSyntaxException) {
                    //TODO: Add snackbar...
//                Snackbar.make(currentFocus!!, "File could not be read.", Snackbar.LENGTH_SHORT)
                    e.printStackTrace()
                }
            }
        }
    }

    private fun insertPileWithCards(piles: List<Pile>, viaShare: Boolean) = lifecycleScope.async {
        piles.forEach { pile ->
            val id = pileVM.insertAsync(pile).await()
            val newCards = pile.cards.map { Card(it.question, it.answer, id) }
            pile.cards.clear()
            cardVM.insertAllAsync(newCards).await()
            sessionVM.pileId.postValue(id)
            sessionVM.pileName.postValue(pile.name)
        }
        runOnUiThread {
            showMainProgressBar(false)
            if (viaShare) startFragment(PileFragment())
        }
    }

    fun startFragment(fragment: Fragment) {
        val activeFragments = supportFragmentManager.fragments
        if (activeFragments.isEmpty())
            supportFragmentManager.startFragment(fragment)
    }

    fun showMainProgressBar(visible: Boolean) {
        if (visible)
            findViewById<View>(R.id.mainProgressBar).alpha = 1F
        else
            findViewById<View>(R.id.mainProgressBar).animate().alpha(0F)
    }

    private fun showErrorDialog(e: ByheartException) = runOnUiThread {
        showMainProgressBar(false)
        startFragment(PileFragment())
        this.dialog()
            .setMessage(e.message ?: "Something went wrong")
            .setCancelable(false)
            .setPositiveButton(getString(R.string.ok)) { _, _ ->  }
            .setAnimation(R.style.SlidingDialog)
            .show()
    }

    companion object {
        const val RESULT_CSV = 1
        const val RESULT_JSON = 2
        const val REQUEST_OPEN_FILE = 3
        const val REQUEST_PICK_FILE = 4
        const val REQUEST_SIGN_IN = 5
    }
}