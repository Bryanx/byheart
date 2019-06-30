package com.example.byheart.card.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.byheart.MainActivity
import com.example.byheart.R
import com.example.byheart.card.Card
import com.example.byheart.card.CardFragment
import com.example.byheart.card.CardViewModel
import com.example.byheart.shared.startFragment
import kotlinx.android.synthetic.main.content_card_edit.*

class CardEditFragment : Fragment() {

    private lateinit var cardViewModel: CardViewModel
    private lateinit var layout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = inflater.inflate(R.layout.content_card_edit, container, false)
        cardViewModel = ViewModelProviders.of(this).get(CardViewModel::class.java)
        (activity as MainActivity).closeDrawer()
        (activity as MainActivity).setToolbarTitle("Add card")
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addEventHandlers()
    }

    private fun addEventHandlers() {
        cardEditBtnSave.setOnClickListener {
            val q = cardEditQuestion.text.toString()
            val a = cardEditAnswer.text.toString()
            if (q.isEmpty()) cardEditQuestionLayout.error = "You need to enter a cardEditQuestion"
            if (a.isEmpty()) cardEditAnswerLayout.error = "You need to enter an cardEditAnswer"
            if (q.isNotEmpty() && a.isNotEmpty()) {
                cardEditQuestion.clearFocus()
                cardEditAnswer.clearFocus()
                cardViewModel.insert(Card(q, a, (activity as MainActivity).pileId.toLong()))
                fragmentManager?.startFragment(CardFragment())
            }
        }
    }
}