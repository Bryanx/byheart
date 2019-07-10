package com.example.byheart.card.edit

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.byheart.MainActivity
import com.example.byheart.R
import com.example.byheart.card.Card
import com.example.byheart.card.CardFragment
import com.example.byheart.card.CardViewModel
import com.example.byheart.shared.*
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.content_card_edit.*

/**
 * Fragment that is showed when editing or creating a card.
 * @author Bryan de Ridder
 */
class CardEditFragment : Fragment(), IOnBackPressed {

    private lateinit var cards: List<Card>
    private lateinit var cardViewModel: CardViewModel
    private lateinit var layout: View
    private var editMode: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = inflater.inflate(R.layout.content_card_edit, container, false)
        cardViewModel = ViewModelProviders.of(this).get(CardViewModel::class.java)
        addToolbar(true, "Add card", true)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        llCardEdit.layoutTransition.enableTransitionType(LayoutTransition.CHANGING)
        editMode = (activity as MainActivity).cardId.isNotEmpty()
        addEventHandlers()
        cardViewModel.allCards.observe(this, Observer { cardsFromDb ->
            cardsFromDb?.filter { it.pileId.toString() == (activity as MainActivity).pileId }?.let {
                cards = it
                updateView()
            }
        })
    }

    private fun updateView() {
        when {
            editMode -> {
                btnAddAnotherCard.visibility = GONE
                val card = getCurrentCard()
                etCardFront.setText(card?.question.toString())
                etCardBack.setText(card?.answer.toString())
            }
            else -> {
                btnAddAnotherCard.visibility = VISIBLE
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_pile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_confirm_edit_pile -> {
            if (addOrUpdateCard()) fragmentManager?.startFragment(CardFragment()).run { true }
            else false
        }
        R.drawable.ic_nav_back -> fragmentManager?.startFragment(CardFragment()).run { true }
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
            val card = Card(q, a, (activity as MainActivity).pileId.toLong())
            if (editMode) {
                cardViewModel.update(card.apply { id = (activity as MainActivity).cardId.toLong() })
            } else {
                cardViewModel.insert(card)
            }
        }
        return frontCorrect && backCorrect
    }

    private fun checkInput(q: String, property: String, layout: TextInputLayout): Boolean {
        var isCorrect = true
        when {
            q.isEmpty() -> {
                layout.isErrorEnabled = true
                layout.error = "Field may not be blank"
                isCorrect = false
            }
            q.toLowerCase() in cards.map { it.getAttr(property).toString().toLowerCase() } -> {
                if ((editMode && q != getCurrentCard()?.getAttr(property)) || !editMode) {
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
            if (addOrUpdateCard()) fragmentManager?.startFragment(CardEditFragment())
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

    private fun getCurrentCard(): Card? = cards.find {
        it.id == (activity as MainActivity).cardId.toLong()
    }

    override fun onBackPressed(): Boolean = fragmentManager?.startFragment(CardFragment()).run { true }

}