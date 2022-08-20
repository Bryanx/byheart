package nl.bryanderidder.byheart.card.edit

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import android.view.inputmethod.EditorInfo
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView.NO_ID
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.content_card_edit.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.card.Card
import nl.bryanderidder.byheart.card.CardFragment
import nl.bryanderidder.byheart.card.CardViewModel
import nl.bryanderidder.byheart.pile.PileViewModel
import nl.bryanderidder.byheart.shared.*
import nl.bryanderidder.byheart.shared.utils.showSnackBar
import org.koin.android.viewmodel.ext.android.sharedViewModel

/**
 * Fragment that is shown when editing or creating a card.
 * @author Bryan de Ridder
 */
class CardEditFragment : Fragment(), IOnBackPressed {

    private lateinit var cards: List<Card>
    private val cardVM: CardViewModel by sharedViewModel()
    private val sessionVM: SessionViewModel by sharedViewModel()
    private val pileVM: PileViewModel by sharedViewModel()
    private lateinit var layout: View
    private var editMode: Boolean = false
    private var currentCard: Card? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = inflater.inflate(R.layout.content_card_edit, container, false)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        llCardEdit.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        editMode = sessionVM.cardId.value != NO_ID
        addEventHandlers()
        cardVM.getByPileId(sessionVM.pileId).observe(viewLifecycleOwner, Observer {
            cards = it
            if (editMode) {
                currentCard = cards.find { card: Card ->
                    sessionVM.cardId.value?.toLong() == card.id
                }
            }
            updateView()
        })
        updateColors()
        if (editMode) addToolbar(title = resources.getString(R.string.edit_card))
        else addToolbar(title = resources.getString(R.string.add_card))
        etCardFront.showKeyboard()
    }

    private fun updateColors() {
        pileVM.allPiles.observe(viewLifecycleOwner) {
            val pile = it.find { pile -> pile.id == sessionVM.pileId.value }
            pile?.color?.let { color ->
                if (Preferences.DARK_MODE) btnAddAnotherCard.tvText.setTextColor(color)
                else btnAddAnotherCard.tvText.setTextColor(color.setBrightness(0.55F))
                progressBarCardEdit.setCircleColor(color)
            }
        }
    }

    private fun updateView() {
        when {
            editMode -> {
                btnAddAnotherCard.visibility = GONE
                etCardFront.setText(currentCard?.question.toString())
                etCardBack.setText(currentCard?.answer.toString())
                etCardFront.setSelection(etCardFront.text?.length ?: 0)
            }
            else -> {
                btnAddAnotherCard.visibility = VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_card_menu, menu)
        menu.findItem(R.id.action_move_card)?.isVisible = showMoveOption()
        menu.findItem(R.id.action_delete_card)?.isVisible = editMode
        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun showMoveOption(): Boolean {
        return editMode && pileVM.allPiles.value?.size ?: 0 > 1
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_info_edit_card -> {
            requireContext().dialog()
                .setTitle(getString(R.string.info))
                .setIcon(R.drawable.ic_info_outline_black_24dp)
                .setMessage(getString(R.string.card_edit_info_dialog))
                .setPositiveButton(getString(R.string.ok)) { _, _ ->  }
                .setAnimation(R.style.SlidingDialog)
                .show()
            true
        }
        R.id.action_confirm_edit_card -> {
            addOrUpdateCard { startFragment(CardFragment()) }.run { true }
        }
        R.id.action_move_card -> {
            MoveCardFragment().also { it.show(requireActivity().supportFragmentManager, it.tag) }
            true
        }
        R.id.action_delete_card -> true.apply { deleteCard() }
        R.drawable.ic_nav_back -> startFragment(CardFragment()).run { true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun deleteCard() = lifecycleScope.launch {
        cardVM.deleteByIdAsync(sessionVM.cardId.value!!).await()
        activity?.runOnUiThread {
            startFragment(CardFragment())
        }
    }

    private fun addOrUpdateCard(onSuccess: () -> Unit) {
        showProgressBar(true)
        lifecycleScope.launch {
            val q = etCardFront.text.toString()
            val a = etCardBack.text.toString()
            val frontCorrect = checkInput(q, "question", cardFrontLayout)
            val backCorrect = checkInput(a, "answer", cardBackLayout)
            if (frontCorrect && backCorrect) {
                etCardFront.clearFocus()
                etCardBack.clearFocus()
                if (editMode) {
                    val card = cardVM.getCards(sessionVM.pileId.value ?: NO_ID).find { it.id == sessionVM.cardId.value }
                    card?.question = q
                    card?.answer = a
                    card?.let { cardVM.updateAsync(it).await() }
                    showMessage(getString(R.string.updated_card))
                } else {
                    val card = Card(q, a, sessionVM.pileId.value ?: NO_ID)
                    cardVM.insertAsync(card).await()
                    showMessage(getString(R.string.created_card))
                }
                delay(100L)
            }
            activity?.runOnUiThread {
                showProgressBar(false)
                if (frontCorrect && backCorrect) onSuccess.invoke()
            }
        }
    }

    private fun showProgressBar(show: Boolean) {
        progressBarCardEdit.alpha = if (show) 1F else 0F
    }

    private fun showMessage(msg: String) {
        activity?.runOnUiThread {
            activity?.let { showSnackBar(it, msg) }
        }
    }

    private fun checkInput(q: String, property: String, layout: TextInputLayout): Boolean {
        var isCorrect = true
        when {
            q.isEmpty() -> {
                layout.isErrorEnabled = true
                layout.error = resources.getString(R.string.field_may_not_be_blank)
                isCorrect = false
            }
            else -> layout.isErrorEnabled = false
        }
        return isCorrect
    }

    private fun addEventHandlers() {
        btnAddAnotherCard.setOnClickListener {
            addOrUpdateCard { startFragment(CardEditFragment()) }
        }
        etCardFront.addTextChangedListener {
            checkInput(etCardFront.string, "question", cardFrontLayout)
        }
        etCardFront.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) checkInput(etCardFront.string, "question", cardFrontLayout)
        }
        etCardBack.addTextChangedListener {
            checkInput(etCardBack.string, "answer", cardBackLayout)
        }
        etCardBack.setOnFocusChangeListener { _, hasFocus ->
            if (!hasFocus) checkInput(etCardBack.string, "answer", cardBackLayout)
        }
        etCardBack.setOnEditorActionListener{_, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE)
                addOrUpdateCard { startFragment(CardEditFragment()) }
            false
        }
        etCardFront.setOnEditorActionListener{_, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_NEXT) {
                etCardBack.requestFocus()
                etCardBack.setSelection(etCardBack.text?.length ?: 0)
            }
            true
        }
    }

    override fun onBackPressed(): Boolean = startFragment(CardFragment()).run { true }

}