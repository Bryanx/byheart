package nl.bryanderidder.byheart.pile

import android.content.Intent
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_ID
import kotlinx.android.synthetic.main.content_piles.view.*
import nl.bryanderidder.byheart.BaseActivity.Companion.REQUEST_PICK_FILE
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.card.CardViewModel
import nl.bryanderidder.byheart.pile.edit.PileEditFragment
import nl.bryanderidder.byheart.settings.SettingsActivity
import nl.bryanderidder.byheart.shared.*
import nl.bryanderidder.byheart.shared.Preferences.KEY_DARK_MODE
import nl.bryanderidder.byheart.shared.views.GridAutofitLayoutManager

/**
 * Fragment that contains a list of all piles and a button to add a new pile.
 * @author Bryan de Ridder
 */
class PileFragment : Fragment(), IOnBackPressed {

    private lateinit var pileVM: PileViewModel
    private lateinit var sessionVM: SessionViewModel
    private lateinit var layout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = inflater.inflate(R.layout.content_piles, container, false)
        pileVM = ViewModelProviders.of(activity!!).get(PileViewModel::class.java)
        sessionVM = ViewModelProviders.of(activity!!).get(SessionViewModel::class.java)
        val cardVM = ViewModelProviders.of(activity!!).get(CardViewModel::class.java)
        cardVM.allCards.observe(this, Observer {
            val recyclerView = layout.findViewById<RecyclerView>(R.id.recyclerview_piles)
            val adapter = PileListAdapter(context!!, it.toMutableList(), sessionVM, pileVM)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = GridAutofitLayoutManager(activity!!, 500)
            ItemTouchHelper(DragAndDropCallback(adapter)).attachToRecyclerView(recyclerView)
            addEventHandlers(adapter)
        })
        addToolbar(hasBackButton = false, title = resources.getString(R.string.app_name))
        return layout
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
        if (Preferences.DARK_MODE)
            menu.findItem(R.id.action_dark_mode).title = resources.getString(R.string.light_mode)
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
        else -> super.onOptionsItemSelected(item)
    }

    private fun addEventHandlers(adapter: PileListAdapter) {
        pileVM.allPiles.observe(this, Observer { piles ->
            // Update the cached copy of the words in the adapter.
            piles?.let { adapter.setPiles(it.toMutableList()) }
        })
        layout.addPileBtn.setOnClickListener {
            sessionVM.pileId.postValue(NO_ID)
            startFragment(PileEditFragment())
        }
    }

    override fun onBackPressed(): Boolean {
        activity?.finish()
        return true
    }
}
