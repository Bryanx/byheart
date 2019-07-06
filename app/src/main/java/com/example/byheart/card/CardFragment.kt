package com.example.byheart.card

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.byheart.MainActivity
import com.example.byheart.R
import com.example.byheart.card.edit.CardEditFragment
import com.example.byheart.pile.Pile
import com.example.byheart.pile.PileFragment
import com.example.byheart.pile.PileViewModel
import com.example.byheart.pile.edit.PileEditFragment
import com.example.byheart.rehearsal.RehearsalMemoryFragment
import com.example.byheart.rehearsal.RehearsalMultipleChoiceFragment
import com.example.byheart.rehearsal.RehearsalTypedFragment
import com.example.byheart.shared.*
import com.example.byheart.shared.Preferences.REHEARSAL_MEMORY
import com.example.byheart.shared.Preferences.REHEARSAL_TYPED
import kotlinx.android.synthetic.main.content_card.*


class CardFragment : Fragment(), IOnBackPressed {

    private lateinit var cardViewModel: CardViewModel
    private lateinit var pileViewModel: PileViewModel
    private lateinit var layout: View
    private var pileId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.findViewById<Toolbar>(R.id.toolbar)?.setBackgroundColor(ContextCompat.getColor(context!!, android.R.color.transparent))
        layout = inflater.inflate(R.layout.content_card, container, false)
        cardViewModel = ViewModelProviders.of(this).get(CardViewModel::class.java)
        pileViewModel = ViewModelProviders.of(this).get(PileViewModel::class.java)
        addToolbar(true, "", true)
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
        R.id.action_edit_pile -> fragmentManager?.startFragment(PileEditFragment()).run { true }
        R.id.action_delete_pile -> startDeleteDialog().run { true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setUpAdapter(): CardListAdapter {
        val recyclerView = layout.findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = CardListAdapter(layout.context, this)
        recyclerView.adapter = adapter
        val layoutManager = ScrollingLinearLayoutManager(layout.context)
        recyclerView.layoutManager = layoutManager
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
                if (it.isEmpty()) placeholder_no_cards.visibility = View.VISIBLE
                else placeholder_no_cards.visibility = View.GONE
                buttonPlay.isEnabled = it.isNotEmpty()
            }
        })
        btnAddCardPlaceholder.setOnClickListener {
            (activity as MainActivity).pileId = pileId!!
            fragmentManager?.startFragment(CardEditFragment())
        }
        buttonPlay.setOnClickListener {
            (activity as MainActivity).pileId = pileId!!
            fragmentManager?.startFragment(when {
                Preferences.read(REHEARSAL_MEMORY) -> RehearsalMemoryFragment()
                Preferences.read(REHEARSAL_TYPED) -> RehearsalTypedFragment()
                else -> RehearsalMultipleChoiceFragment()
            })
        }
        buttonAdd.setOnClickListener {
            (activity as MainActivity).pileId = pileId!!
            fragmentManager?.startFragment(CardEditFragment())
        }
    }

    private fun startDeleteDialog() {
        context!!.dialog().setMessage("Are you sure you want to delete this pile?")
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

    fun removeCard(card: Card) {
        cardViewModel.delete(card)
    }

    override fun onBackPressed(): Boolean {
        return fragmentManager?.startFragment(PileFragment()).run { true }
    }
}
