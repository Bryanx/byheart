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
import androidx.recyclerview.widget.RecyclerView.NO_ID
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
import com.example.byheart.shared.Preferences.REHEARSAL_MULTIPLE_CHOICE
import com.example.byheart.shared.Preferences.REHEARSAL_TYPED
import kotlinx.android.synthetic.main.content_card.*

/**
 * Fragment that displays all cards in a pile.
 * @author Bryan de Ridder
 */
class CardFragment : Fragment(), IOnBackPressed {

    private lateinit var cardVM: CardViewModel
    private lateinit var sessionVM: SessionViewModel
    private lateinit var pileVM: PileViewModel
    private lateinit var layout: View
    private var pileId: Long = NO_ID

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        activity?.findViewById<Toolbar>(R.id.toolbar)?.setBackgroundColor(ContextCompat.getColor(context!!, android.R.color.transparent))
        layout = inflater.inflate(R.layout.content_card, container, false)
        cardVM = ViewModelProviders.of(activity!!).get(CardViewModel::class.java)
        sessionVM = ViewModelProviders.of(activity!!).get(SessionViewModel::class.java)
        pileVM = ViewModelProviders.of(this).get(PileViewModel::class.java)
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
        R.id.action_edit_pile -> startFragment(PileEditFragment()).run { true }
        R.id.action_delete_pile -> startDeleteDialog().run { true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setUpAdapter(): CardListAdapter {
        val recyclerView = layout.findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = CardListAdapter(layout.context, this)
        recyclerView.adapter = adapter
        val layoutManager = ScrollingLinearLayoutManager(layout.context)
        recyclerView.layoutManager = layoutManager
        val itemTouchHelper1 = ItemTouchHelper(SwipeLeftToDeleteCallback(adapter))
        val itemTouchHelper2 = ItemTouchHelper(SwipeRightToEditCallback(adapter))
        itemTouchHelper1.attachToRecyclerView(recyclerView)
        itemTouchHelper2.attachToRecyclerView(recyclerView)
        return adapter
    }

    private fun getBundle() {
        val pileName = sessionVM.pileName.value
        pileId = sessionVM.pileId.value ?: NO_ID
        content_card_title.text = pileName
    }

    private fun addEventHandlers(adapter: CardListAdapter) {
        btnAddCardPlaceholder.setOnClickListener { startEditFragment() }
        buttonAdd.setOnClickListener { startEditFragment() }
        buttonEdit.setOnClickListener { startFragment(PileEditFragment()) }
        cardVM.allCards.observe(this, Observer { cards ->
            // Update the cached copy of the words in the adapter.
            cards?.filter {
                it.pileId == pileId
            }?.let {
                adapter.setCards(it)
                if (it.isEmpty()) placeholder_no_cards.visibility = View.VISIBLE
                else placeholder_no_cards.visibility = View.GONE
                buttonPlay.isEnabled = it.isNotEmpty()
            }
        })
        buttonPlay.setOnClickListener {
            if (adapter.cards.size < 5 && Preferences.read(REHEARSAL_MULTIPLE_CHOICE)) {
                Preferences.write(REHEARSAL_MULTIPLE_CHOICE, false)
                Preferences.write(REHEARSAL_MEMORY, true)
            }
            sessionVM.pileId.value = pileId
            startFragment(when {
                Preferences.read(REHEARSAL_MEMORY) -> RehearsalMemoryFragment()
                Preferences.read(REHEARSAL_TYPED) -> RehearsalTypedFragment()
                else -> RehearsalMultipleChoiceFragment()
            })
        }
    }

    fun startEditFragment(id: Long = NO_ID) {
        sessionVM.cardId.value = id
        startFragment(CardEditFragment())
    }

    private fun startDeleteDialog() {
        context!!.dialog().setMessage("Are you sure you want to delete this pile?")
            .setCancelable(false)
            .setPositiveButton("Delete") { _, _ ->
                val pile = Pile(sessionVM.pileName.value)
                pile.id = pileId
                pileVM.delete(pile)
                startFragment(PileFragment())
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    fun removeCard(card: Card) {
        cardVM.delete(card)
    }

    /**
     * This method is called by the MainActivity.
     */
    override fun onBackPressed(): Boolean {
        return startFragment(PileFragment()).run { true }
    }
}
