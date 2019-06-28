package com.example.byheart.card

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.byheart.MainActivity
import com.example.byheart.R
import com.example.byheart.card.edit.CardEditFragment
import com.example.byheart.rehearsal.RehearsalFragment
import com.example.byheart.shared.SwipeToDeleteCallback
import com.example.byheart.shared.startFragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton

class CardFragment : Fragment() {

    private lateinit var btnPlay: FloatingActionButton
    private lateinit var btnAdd: FloatingActionButton
    private lateinit var pileTitle: TextView
    private lateinit var cardViewModel: CardViewModel
    private lateinit var layout: View
    private var pileId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = inflater.inflate(R.layout.content_card, container, false)
        cardViewModel = ViewModelProviders.of(this).get(CardViewModel::class.java)
        findViews()
        getBundle()
        val adapter = setUpAdapter()
        addEventHandlers(adapter)
        (activity as MainActivity).closeDrawer()
        return layout
    }

    private fun findViews() {
        pileTitle = activity?.findViewById(R.id.content_card_title)!!
        btnPlay = activity?.findViewById(R.id.buttonPlay)!!
        btnAdd = activity?.findViewById(R.id.buttonAdd)!!
    }

    private fun setUpAdapter(): CardListAdapter {
        val recyclerView = layout.findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = CardListAdapter(layout.context, this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(layout.context)
        val itemTouchHelper = ItemTouchHelper(SwipeToDeleteCallback(adapter))
        itemTouchHelper.attachToRecyclerView(recyclerView)
        return adapter
    }

    private fun getBundle() {
        val pileName = (activity as MainActivity).pileName
        pileId = (activity as MainActivity).pileId
        pileTitle.text = pileName
    }

    private fun addEventHandlers(adapter: CardListAdapter) {
        cardViewModel.allCards.observe(this, Observer { cards ->
            // Update the cached copy of the words in the adapter.
            cards?.filter {
                it.pileId.toString() == pileId
            }?.let {
                adapter.setCards(it)
                btnPlay.isEnabled = it.isNotEmpty()
            }
        })
        btnPlay.setOnClickListener {
            (activity as MainActivity).pileId = pileId!!
            fragmentManager?.startFragment(RehearsalFragment())
        }
        btnAdd.setOnClickListener {
            (activity as MainActivity).pileId = pileId!!
            fragmentManager?.startFragment(CardEditFragment())
        }
    }

    fun removeCard(card: Card) {
        cardViewModel.removeCard(card)
    }
}
