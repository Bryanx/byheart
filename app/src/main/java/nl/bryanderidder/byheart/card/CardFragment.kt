package nl.bryanderidder.byheart.card

import android.os.Bundle
import android.view.*
import android.view.View.GONE
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_ID
import kotlinx.android.synthetic.main.content_card.*
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.card.edit.CardEditFragment
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.pile.PileFragment
import nl.bryanderidder.byheart.pile.PileViewModel
import nl.bryanderidder.byheart.pile.edit.PileEditFragment
import nl.bryanderidder.byheart.rehearsal.RehearsalMemoryFragment
import nl.bryanderidder.byheart.rehearsal.RehearsalMultipleChoiceFragment
import nl.bryanderidder.byheart.rehearsal.RehearsalTypedFragment
import nl.bryanderidder.byheart.shared.*
import nl.bryanderidder.byheart.shared.Preferences.KEY_REHEARSAL_MEMORY
import nl.bryanderidder.byheart.shared.Preferences.KEY_REHEARSAL_MULTIPLE_CHOICE
import nl.bryanderidder.byheart.shared.utils.IoUtils
import nl.bryanderidder.byheart.shared.views.GridAutofitLayoutManager


/**
 * Fragment that displays all cards in a pile.
 * @author Bryan de Ridder
 */
class CardFragment : Fragment(), IOnBackPressed {

    private var pile: Pile? = null
    private lateinit var adapter: CardListAdapter
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
        addToolbar()
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundle()
        setUpAdapter()
        addEventHandlers()
        changeColors()
    }

    private fun changeColors() {
        pileVM.allPiles.observe(this, Observer { piles ->
            pile = piles.find { it.id == sessionVM.pileId.value }
            val buttons = arrayOf(buttonShare, buttonAdd, buttonPlay)
            pile?.color?.let { color ->
                if (Preferences.DARK_MODE) {
                    buttons.forEach { it.setIconColor(color) }
                    btnAddCardPlaceholder.textColor = color
                } else {
                    buttons.forEach {
                        it.setIconColor(color.setBrightness(.55F))
                        it.setColor(context!!.color(R.color.grey_100))
                    }
                    btnAddCardPlaceholder.textColor = color.setBrightness(0.55F)
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.pile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_edit_pile -> startFragment(PileEditFragment()).run { true }
        R.id.action_delete_pile -> startDeleteDialog().run { true }
        R.id.action_export -> exportAsCSV().run { true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setUpAdapter() {
        val recyclerView = layout.findViewById<RecyclerView>(R.id.recyclerview)
        adapter = CardListAdapter(layout.context, this)
        recyclerView.adapter = adapter
        val layoutManager = GridAutofitLayoutManager(layout.context, 850)
        recyclerView.layoutManager = layoutManager
        ItemTouchHelper(SwipeLeftToDeleteCallback(adapter)).attachToRecyclerView(recyclerView)
        ItemTouchHelper(SwipeRightToEditCallback(adapter)).attachToRecyclerView(recyclerView)
    }

    private fun getBundle() {
        val pileName = sessionVM.pileName.value
        pileId = sessionVM.pileId.value ?: NO_ID
        content_card_title.text = pileName
    }

    private fun addEventHandlers() {
        btnAddCardPlaceholder.setOnClickListener { startEditFragment() }
        buttonAdd.setOnClickListener { startEditFragment() }
        buttonShare.setOnClickListener { share() }
        cardVM.allCards.observe(this, Observer { cards ->
            // Update the cached copy of the words in the adapter.
            cards?.filter {
                it.pileId == pileId
            }?.let {
                clProgressBar.visibility = GONE
                adapter.setCards(it)
                if (it.isEmpty()) placeholderNoCards.visibility = View.VISIBLE
                else placeholderNoCards.visibility = GONE
                buttonPlay.isEnabled = it.isNotEmpty()
            }
        })
        buttonPlay.setOnClickListener {
            // If the user has MP as a preference but less than 5 cards, overwrite preference.
            if (adapter.cards.size < 5 && Preferences.REHEARSAL_MULTIPLE_CHOICE) {
                Preferences.write(KEY_REHEARSAL_MULTIPLE_CHOICE, false)
                Preferences.write(KEY_REHEARSAL_MEMORY, true)
            }
            sessionVM.pileId.value = pileId
            startFragment(when {
                Preferences.REHEARSAL_MEMORY -> RehearsalMemoryFragment()
                Preferences.REHEARSAL_TYPED -> RehearsalTypedFragment()
                else -> RehearsalMultipleChoiceFragment()
            })
        }
    }

    fun startEditFragment(id: Long = NO_ID) {
        sessionVM.cardId.value = id
        startFragment(CardEditFragment())
    }

    private fun startDeleteDialog() {
        context!!.dialog()
            .setMessage(getString(R.string.delete_confirm_stack))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.delete)) { _, _ ->
                pileVM.delete(sessionVM.pileId.value)
                startFragment(PileFragment())
            }
            .setNegativeButton(getString(R.string.cancel), null)
            .setAnimation(R.style.SlidingDialog)
            .show()
    }

    private fun exportAsCSV() {
        IoUtils.createCSV(context!!, adapter.cards, "Byheart-${pile?.name}.csv")
    }

    private fun share() {
        pile?.let {
            it.cards.addAll(adapter.cards)
            IoUtils.createJson(context!!, arrayOf(it), "Byheart-${pile?.name}.byheart")
        }
    }

    fun removeCard(card: Card) = cardVM.delete(card)

    /**
     * This method is called by the MainActivity.
     */
    override fun onBackPressed(): Boolean {
        return startFragment(PileFragment()).run { true }
    }
}
