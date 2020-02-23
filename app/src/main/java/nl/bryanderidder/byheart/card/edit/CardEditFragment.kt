package nl.bryanderidder.byheart.card.edit

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView.NO_ID
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.content_card_edit.*
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.card.Card
import nl.bryanderidder.byheart.card.CardFragment
import nl.bryanderidder.byheart.card.CardViewModel
import nl.bryanderidder.byheart.pile.PileViewModel
import nl.bryanderidder.byheart.shared.*
import nl.bryanderidder.byheart.shared.utils.showSnackBar

/**
 * Fragment that is shown when editing or creating a card.
 * @author Bryan de Ridder
 */
class CardEditFragment : Fragment(), IOnBackPressed {

    private lateinit var cards: List<Card>
    private lateinit var cardVM: CardViewModel
    private lateinit var sessionVM: SessionViewModel
    private lateinit var pileVM: PileViewModel
    private lateinit var layout: View
    private var editMode: Boolean = false
    private var currentCard: Card? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = inflater.inflate(R.layout.content_card_edit, container, false)
        cardVM = ViewModelProviders.of(activity!!).get(CardViewModel::class.java)
        sessionVM = ViewModelProviders.of(activity!!).get(SessionViewModel::class.java)
        pileVM = ViewModelProviders.of(activity!!).get(PileViewModel::class.java)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        llCardEdit.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        editMode = sessionVM.cardId.value != NO_ID
        addEventHandlers()
        cardVM.getByPileId(sessionVM.pileId).observe(this, Observer { cards ->
//            cardsFromDb?.filter { it.pileId == sessionVM.pileId.value }?.let {
//                cards = it
                if (editMode) {
                    currentCard = cards.find { card: Card ->
                        sessionVM.cardId.value?.toLong() == card.id
                    }
                }
                updateView()
//            }
        })
        updateColors()
        if (editMode) addToolbar(title = resources.getString(R.string.edit_card))
        else addToolbar(title = resources.getString(R.string.add_card))
        etCardFront.showKeyboard()
    }

    private fun updateColors() {
        pileVM.allPiles.observe(this, Observer {
            val pile = it.find { pile -> pile.id == sessionVM.pileId.value }
            pile?.color?.let { color ->
                if (Preferences.DARK_MODE) btnAddAnotherCard.textColor = color
                else btnAddAnotherCard.textColor = color.setBrightness(0.55F)
            }
        })
    }

    private fun updateView() {
        when {
            editMode -> {
                btnAddAnotherCard.visibility = GONE
                etCardFront.setText(currentCard?.question.toString())
                etCardBack.setText(currentCard?.answer.toString())
            }
            else -> {
                btnAddAnotherCard.visibility = VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_card_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_confirm_edit_pile -> {
            if (addOrUpdateCard()) startFragment(CardFragment()).run { true }
            else false
        }
        R.drawable.ic_nav_back -> startFragment(CardFragment()).run { true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun addOrUpdateCard(): Boolean {
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
                cardVM.update(card!!)
                showMessage(getString(R.string.updated_card))
            } else {
                val card = Card(q, a, sessionVM.pileId.value ?: NO_ID)
                cardVM.insert(card)
                showMessage(getString(R.string.created_card))
            }
        }
        return frontCorrect && backCorrect
    }

    private fun showMessage(msg: String) {
        activity?.let { showSnackBar(it, msg) }
    }

    private fun checkInput(q: String, property: String, layout: TextInputLayout): Boolean {
        var isCorrect = true
        when {
            q.isEmpty() -> {
                layout.isErrorEnabled = true
                layout.error = resources.getString(R.string.field_may_not_be_blank)
                isCorrect = false
            }
            q.toLowerCase() in cards.map { it.getAttr(property).toString().toLowerCase() } -> {
                if ((editMode && q != currentCard?.getAttr(property)) || !editMode) {
                    layout.isErrorEnabled = true
                    layout.error = "You already have a card with the same text"
                    isCorrect = false
                }
            }
            else -> layout.isErrorEnabled = false
        }
        return isCorrect
    }

    private fun addEventHandlers() {
        btnAddAnotherCard.setOnClickListener {
            if (addOrUpdateCard()) startFragment(CardEditFragment())
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
    }

    override fun onBackPressed(): Boolean = startFragment(CardFragment()).run { true }

}