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
import nl.bryanderidder.byheart.BaseActivity
import nl.bryanderidder.byheart.BaseActivity.Companion.REQUEST_PICK_FILE
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.card.CardViewModel
import nl.bryanderidder.byheart.pile.edit.PileEditFragment
import nl.bryanderidder.byheart.settings.SettingsActivity
import nl.bryanderidder.byheart.shared.*
import nl.bryanderidder.byheart.shared.Preferences.KEY_DARK_MODE
import nl.bryanderidder.byheart.shared.utils.doAfterAnimations
import nl.bryanderidder.byheart.shared.utils.showSnackBar
import nl.bryanderidder.byheart.shared.views.GridAutofitLayoutManager

/**
 * Fragment that contains a list of all piles and a button to add a new pile.
 * @author Bryan de Ridder
 */
class PileFragment : Fragment(), IOnBackPressed {

    private val cardVM: CardViewModel by lazy { (activity!! as BaseActivity).cardVM }
    private val sessionVM: SessionViewModel by lazy { (activity!! as BaseActivity).sessionVm }
    private val pileVM: PileViewModel by lazy { (activity!! as BaseActivity).pileVM }
    private lateinit var layout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = inflater.inflate(R.layout.content_piles, container, false)
        cardVM.allCards.observe(this, Observer {
            val recyclerView = layout.findViewById<RecyclerView>(R.id.recyclerview_piles)
            val adapter = PileListAdapter(context!!, it.toMutableList(), sessionVM, pileVM, this)
            recyclerView.adapter = adapter
            recyclerView.layoutManager = GridAutofitLayoutManager(activity!!, 500)
            ItemTouchHelper(DragAndDropCallback(adapter)).attachToRecyclerView(recyclerView)
            addEventHandlers(adapter)
        })
        addToolbar(hasBackButton = false, title = resources.getString(R.string.app_name))
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
        else -> super.onOptionsItemSelected(item)
    }

    private fun addEventHandlers(adapter: PileListAdapter) {
        pileVM.allPiles.observe(this, Observer { piles ->
            piles?.let {
                recyclerview_piles.doAfterAnimations {
                    adapter.setPiles(piles.toMutableList())
                }
            }
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
