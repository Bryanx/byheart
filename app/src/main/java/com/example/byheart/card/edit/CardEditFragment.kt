package com.example.byheart.card.edit

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.byheart.MainActivity
import com.example.byheart.R
import com.example.byheart.card.Card
import com.example.byheart.card.CardFragment
import com.example.byheart.card.CardViewModel
import com.example.byheart.shared.addToolbar
import com.example.byheart.shared.focus
import com.example.byheart.shared.startFragment
import kotlinx.android.synthetic.main.content_card_edit.*


class CardEditFragment : Fragment() {

    private lateinit var cardViewModel: CardViewModel
    private lateinit var layout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = inflater.inflate(R.layout.content_card_edit, container, false)
        cardViewModel = ViewModelProviders.of(this).get(CardViewModel::class.java)
        addToolbar(activity!!, true, "Add card", true) {
            fragmentManager?.startFragment(CardFragment())
        }
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addEventHandlers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_pile_menu, menu)
    }


    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_confirm_edit_pile -> {
            addCard()
            fragmentManager?.startFragment(CardFragment())
            true
        }
        R.drawable.ic_nav_back -> {
            fragmentManager?.startFragment(CardFragment())
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun addCard() {
        val q = cardEditQuestion.text.toString()
        val a = cardEditAnswer.text.toString()
        if (q.isEmpty()) cardEditQuestionLayout.error = "You need to enter a question"
        if (a.isEmpty()) cardEditAnswerLayout.error = "You need to enter an answer"
        if (q.isNotEmpty() && a.isNotEmpty()) {
            cardEditQuestion.clearFocus()
            cardEditAnswer.clearFocus()
            cardViewModel.insert(Card(q, a, (activity as MainActivity).pileId.toLong()))
        }
    }

    private fun addEventHandlers() {
        btnAddAnotherCard.setOnClickListener {
            addCard()
            fragmentManager?.startFragment(CardEditFragment())
        }
    }
}