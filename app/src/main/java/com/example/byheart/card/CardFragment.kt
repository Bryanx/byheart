package com.example.byheart.card

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.byheart.MainActivity
import com.example.byheart.R
import com.example.byheart.card.edit.CardEditFragment
import com.example.byheart.pile.Pile
import com.example.byheart.pile.PileFragment
import com.example.byheart.pile.PileViewModel
import com.example.byheart.pile.edit.PileEditFragment
import com.example.byheart.rehearsal.RehearsalFragment
import com.example.byheart.shared.SwipeToDeleteCallback
import com.example.byheart.shared.addToolbar
import com.example.byheart.shared.startFragment
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_card.*


class CardFragment : Fragment() {

    private lateinit var cardViewModel: CardViewModel
    private lateinit var pileViewModel: PileViewModel
    private lateinit var layout: View
    private var pileId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.findViewById<Toolbar>(R.id.toolbar)?.setBackgroundColor(ContextCompat.getColor(context!!, android.R.color.transparent))
        layout = inflater.inflate(R.layout.content_card, container, false)
        cardViewModel = ViewModelProviders.of(this).get(CardViewModel::class.java)
        pileViewModel = ViewModelProviders.of(this).get(PileViewModel::class.java)
        addToolbar(activity!!, true, "", true) {
            fragmentManager?.startFragment(PileFragment())
        }
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundle()
        val adapter = setUpAdapter()
        addEventHandlers(adapter)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.pile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_edit_pile -> {
            fragmentManager?.startFragment(PileEditFragment())
            true
        }
        R.id.action_delete_pile -> {
            confirmDelete()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun confirmDelete() {
        AlertDialog.Builder(context!!)
            .setMessage("Are you sure you want to delete this pile?")
            .setCancelable(false)
            .setPositiveButton("Delete") { _, _ ->
                val pile = Pile((activity as MainActivity).pileName)
                pile.id = pileId!!.toLong()
                pileViewModel.delete(pile)
                fragmentManager?.startFragment(PileFragment())
            }
            .setNegativeButton("Cancel", null)
            .show()
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
        content_card_title.text = pileName
    }

    private fun addEventHandlers(adapter: CardListAdapter) {
        cardViewModel.allCards.observe(this, Observer { cards ->
            // Update the cached copy of the words in the adapter.
            cards?.filter {
                it.pileId.toString() == pileId
            }?.let {
                adapter.setCards(it)
                buttonPlay.isEnabled = it.isNotEmpty()
            }
        })
        buttonPlay.setOnClickListener {
            (activity as MainActivity).pileId = pileId!!
            fragmentManager?.startFragment(RehearsalFragment())
        }
        buttonAdd.setOnClickListener {
            (activity as MainActivity).pileId = pileId!!
            fragmentManager?.startFragment(CardEditFragment())
        }
    }

    fun removeCard(card: Card) {
        cardViewModel.delete(card)
    }
}
