package nl.bryanderidder.byheart.pile

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_ID
import kotlinx.android.synthetic.main.content_piles.*
import kotlinx.android.synthetic.main.content_piles.view.*
import kotlinx.android.synthetic.main.item_pile.view.*
import nl.bryanderidder.byheart.BaseActivity
import nl.bryanderidder.byheart.BaseActivity.Companion.REQUEST_PICK_FILE
import nl.bryanderidder.byheart.MainActivity
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.card.CardFragment
import nl.bryanderidder.byheart.card.CardViewModel
import nl.bryanderidder.byheart.pile.edit.PileEditFragment
import nl.bryanderidder.byheart.settings.SettingsActivity
import nl.bryanderidder.byheart.shared.*
import nl.bryanderidder.byheart.shared.Preferences.KEY_DARK_MODE
import nl.bryanderidder.byheart.shared.utils.doAfterAnimations
import nl.bryanderidder.byheart.shared.utils.showSnackBar
import nl.bryanderidder.byheart.shared.views.GridAutofitLayoutManager
import org.koin.android.viewmodel.ext.android.sharedViewModel
import nl.bryanderidder.byheart.store.StoreFragment

/**
 * Fragment that contains a list of all piles and a button to add a new pile.
 * @author Bryan de Ridder
 */
class PileFragment : Fragment(), IOnBackPressed {

    private val cardVM: CardViewModel by sharedViewModel()
    private val sessionVM: SessionViewModel by sharedViewModel()
    private val pileVM: PileViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.content_piles, container, false)
        cardVM.allCards.observe(this, Observer {
            val adapter = PileListAdapter(context!!, it.toMutableList(), ::onClickPile, ::onAfterMovingPiles)
            recyclerviewPiles.adapter = adapter
            recyclerviewPiles.layoutManager = GridAutofitLayoutManager(activity!!, 500)
            ItemTouchHelper(DragAndDropCallback(adapter)).attachToRecyclerView(recyclerviewPiles)
            addEventHandlers(adapter)
        })
        addToolbar(hasBackButton = false, title = resources.getString(R.string.my_card_stacks))
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sessionVM.findMessage()?.let {
            showSnackBar(rootView, it)
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onPrepareOptionsMenu(menu: Menu) {
        menu.findItem(R.id.action_dark_mode).isChecked = Preferences.DARK_MODE
        super.onPrepareOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_dark_mode -> {
            Preferences.toggle(KEY_DARK_MODE)
            activity?.intent?.action = Intent.ACTION_MAIN
            activity?.recreate()
            true
        }
        R.id.action_settings -> {
            activity?.startActivityForResult(Intent(context, SettingsActivity::class.java), REQUEST_PICK_FILE).run { true }
        }
        R.id.action_store -> startFragment(StoreFragment()).run { true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun addEventHandlers(adapter: PileListAdapter) {
        pileVM.allPiles.observe(this, Observer { piles ->
            piles?.let {
                recyclerviewPiles.doAfterAnimations {
                    adapter.setPiles(piles.toMutableList())
                }
            }
        })
        addPileBtn.setOnClickListener {
            sessionVM.pileId.postValue(NO_ID)
            startFragment(PileEditFragment())
        }
    }

    private fun onAfterMovingPiles(piles: List<Pile>) {
        pileVM.updateAll(piles)
    }

    private fun onClickPile(id: Long, name: String, piles: List<Pile>) {
        sessionVM.pileId.postValue(id)
        sessionVM.pileName.postValue(name)
        sessionVM.pileColor.postValue(piles.first { it.id == id }.color)
        startFragment(CardFragment())
    }

    override fun onBackPressed(): Boolean {
        activity?.finish()
        return true
    }

}
