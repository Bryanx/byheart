package nl.bryanderidder.byheart.card

import android.os.Bundle
import android.view.*
import android.view.View.GONE
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView.NO_ID
import kotlinx.android.synthetic.main.content_card.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.auth.AuthViewModel
import nl.bryanderidder.byheart.auth.LoginFragment
import nl.bryanderidder.byheart.card.edit.CardEditFragment
import nl.bryanderidder.byheart.card.share.ShareFragment
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.pile.PileFragment
import nl.bryanderidder.byheart.pile.PileViewModel
import nl.bryanderidder.byheart.pile.edit.PileEditFragment
import nl.bryanderidder.byheart.rehearsal.setup.RehearsalSetupFragment
import nl.bryanderidder.byheart.shared.*
import nl.bryanderidder.byheart.shared.Preferences.USER_ID
import nl.bryanderidder.byheart.shared.firestore.FireStoreViewModel
import nl.bryanderidder.byheart.shared.touchhelpers.SwipeLeftToDeleteCallback
import nl.bryanderidder.byheart.shared.touchhelpers.SwipeRightToEditCallback
import nl.bryanderidder.byheart.shared.utils.IoUtils
import nl.bryanderidder.byheart.shared.utils.showSnackBar
import nl.bryanderidder.byheart.shared.views.GridAutofitLayoutManager
import org.koin.android.viewmodel.ext.android.sharedViewModel

/**
 * Fragment that displays all cards in a pile.
 * @author Bryan de Ridder
 */
class CardFragment : Fragment(), IOnBackPressed {

    private lateinit var layoutManager: GridAutofitLayoutManager
    private lateinit var swipeLeftToDelete: SwipeLeftToDeleteCallback
    private lateinit var swipeRightToEdit: SwipeRightToEditCallback
    private var pile: Pile? = null
    private lateinit var adapter: CardListAdapter
    private val cardVM: CardViewModel by sharedViewModel()
    private val sessionVM: SessionViewModel by sharedViewModel()
    private val pileVM: PileViewModel by sharedViewModel()
    private val fireStoreVM: FireStoreViewModel by sharedViewModel()
    private val authVM: AuthViewModel by sharedViewModel()
    private lateinit var layout: View
    private var pileId: Long = NO_ID
    var pileColor: Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = inflater.inflate(R.layout.content_card, container, false)
        addToolbar()
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundle()
        setUpAdapter()
        addEventHandlers()
        changeColors()
        sessionVM.findMessage()?.let {
            showSnackBar(requireActivity(), it)
        }
    }

    private fun changeColors() {
        pileVM.allPiles.observe(viewLifecycleOwner, Observer { piles ->
            pile = piles.find { it.id == sessionVM.pileId.value }
            val buttons = arrayOf(buttonShare, buttonAdd, buttonPlay)
            pile?.color?.let { color ->
                clProgressBar.setCircleColor(color)
                if (Preferences.DARK_MODE) {
                    buttons.forEach { it.setIconColor(color) }
                    btnAddCardPlaceholder.tvText.setTextColor(color)
                } else {
                    buttons.forEach {
                        it.setIconColor(color.setBrightness(.55F))
                        it.setColor(requireContext().color(R.color.grey_100))
                    }
                    btnAddCardPlaceholder.tvText.setTextColor(color.setBrightness(0.55F))
                }
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.pile_menu, menu)
        val sort = menu.findItem(R.id.action_sort)
        inflater.inflate(R.menu.sort_submenu, sort.subMenu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_edit_pile -> startFragment(PileEditFragment()).run { true }
        R.id.action_delete_pile -> startDeleteDialog().run { true }
        R.id.action_export -> exportAsCSV().run { true }
        R.id.action_sort_front -> adapter.sort<String>("question").run { true }
        R.id.action_sort_back -> adapter.sort<String>("answer").run { true }
        R.id.action_sort_score -> adapter.sort<Int>("CorrectPercentage").run { true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun setUpAdapter() {
        adapter = CardListAdapter(layout.context, this)
        recyclerview.adapter = adapter
        layoutManager = GridAutofitLayoutManager(layout.context, 850)
        recyclerview.layoutManager = layoutManager
        swipeLeftToDelete = SwipeLeftToDeleteCallback(adapter)
        swipeRightToEdit = SwipeRightToEditCallback(adapter)
        ItemTouchHelper(swipeLeftToDelete).attachToRecyclerView(recyclerview)
        ItemTouchHelper(swipeRightToEdit).attachToRecyclerView(recyclerview)
    }

    private fun getBundle() {
        pileId = sessionVM.pileId.value ?: NO_ID
        pileColor = sessionVM.pileColor.value ?: requireContext().color(R.color.colorPrimary)
        content_card_title.text = sessionVM.pileName.value
    }

    private fun addEventHandlers() {
        btnAddCardPlaceholder.setOnClickListener { startEditFragment() }
        buttonAdd.setOnClickListener { startEditFragment() }
        buttonShare.setOnClickListener {
            when {
                adapter.cards.isEmpty() -> showSnackBar(getString(R.string.you_dont_have_any_cards_yet), resources.getString(R.string.add_a_card), pileColor) { startEditFragment() }
                USER_ID.isEmpty() -> LoginFragment()
                    .also { authVM.loginMessage.value = getString(R.string.share_block_login_message) }
                    .also { it.show(requireActivity().supportFragmentManager, it.tag) }
                else -> ShareFragment().also { it.show(requireActivity().supportFragmentManager, it.tag) }
            }
        }
        cardVM.getByPileId(sessionVM.pileId).observeOnce(this, Observer { cards ->
            adapter.setCards(cards.toMutableList())
            showPlaceholder(cards)
        })
        buttonPlay.setOnClickListener {
            if (adapter.cards.isEmpty()) {
                showSnackBar(getString(R.string.you_dont_have_any_cards_yet), resources.getString(R.string.add_a_card), pileColor) { startEditFragment() }
            } else {
                sessionVM.pileId.value = pileId
                sessionVM.cardCount.value = adapter.cards.size
                sessionVM.pileColor.value = pile?.color
                RehearsalSetupFragment().also {
                    it.show(requireActivity().supportFragmentManager, it.tag)
                }

            }
        }
    }

    fun startEditFragment(id: Long = NO_ID) {
        sessionVM.cardId.value = id
        startFragment(CardEditFragment())
    }

    private fun startDeleteDialog() {
        requireContext().dialog()
            .setMessage(getString(R.string.delete_confirm_stack))
            .setCancelable(false)
            .setPositiveButton(getString(R.string.delete)) { _, _ -> deletePile() }
            .setNegativeButton(getString(R.string.cancel), null)
            .setAnimation(R.style.SlidingDialog)
            .show()
    }

    private fun deletePile() {
        clProgressBar.show()
        lifecycleScope.launch {
            delay(500L)
            if (pile?.remoteId?.isNotEmpty() == true)
                fireStoreVM.deleteAsync(pile?.remoteId ?: "").await()
            pileVM.delete(sessionVM.pileId.value)
            activity?.runOnUiThread {
                sessionVM.message.value = getString(R.string.deleted_stack)
                clProgressBar.hide()
                startFragment(PileFragment())
            }
        }
    }

    private fun exportAsCSV() = IoUtils.createCSV(requireContext(), adapter.cards, "Byheart-${pile?.name}.csv")

    fun updateReorderedCards(cards: List<Card>) {
        updateAllCards(cards)
        layoutManager.scrollToPosition(0)
    }

    fun removeCard(card: Card) = lifecycleScope.launch {
        swipeLeftToDelete.isEnabled = false
        cardVM.deleteAsync(card).await()
        activity?.runOnUiThread {
            adapter.cards.removeAt(card.listIndex)
            adapter.notifyItemRemoved(card.listIndex)
            showPlaceholder(adapter.cards)
        }
        showSnackBar(getString(R.string.removed_card), getString(R.string.undo), pileColor) {
            lifecycleScope.launch {
                cardVM.insertAtIndexAsync(card, card.listIndex).await()
                activity?.runOnUiThread {
                    adapter.cards.add(card.listIndex, card)
                    adapter.notifyItemInserted(card.listIndex)
                    showPlaceholder(adapter.cards)
                }
            }
        }
        swipeLeftToDelete.isEnabled = true
    }

    private fun showPlaceholder(cards: List<Card>) {
        if (cards.isEmpty()) placeholderNoCards.visibility = View.VISIBLE
        else placeholderNoCards.visibility = GONE
    }

    fun updateAllCards(cards: List<Card>) = cardVM.updateAll(cards)

    // This method is called by the MainActivity.
    override fun onBackPressed(): Boolean = startFragment(PileFragment()).run { true }

    override fun onPause() {
        super.onPause()
        sessionVM.pushCardListState(layoutManager.onSaveInstanceState())
    }

    override fun onResume() {
        super.onResume()
        layoutManager.onRestoreInstanceState(sessionVM.cardListState.value?.get(sessionVM.pileId.value))
    }
}
