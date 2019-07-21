package nl.bryanderidder.byheart.pile

import android.graphics.Rect
import android.os.Bundle
import android.os.Handler
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.NO_ID
import kotlinx.android.synthetic.main.content_piles.view.*
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.about.AboutFragment
import nl.bryanderidder.byheart.pile.edit.PileEditFragment
import nl.bryanderidder.byheart.shared.*
import nl.bryanderidder.byheart.shared.Preferences.KEY_DARK_MODE

/**
 * Fragment that contains a list of all piles and a button to add a new pile.
 * @author Bryan de Ridder
 */
class PileFragment : Fragment(), IOnBackPressed {

    private lateinit var pileVM: PileViewModel
    private lateinit var sessionVM: SessionViewModel
    private lateinit var layout: View
    private var backPressedTwice = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = inflater.inflate(R.layout.content_piles, container, false)
        pileVM = ViewModelProviders.of(this).get(PileViewModel::class.java)
        sessionVM = ViewModelProviders.of(activity!!).get(SessionViewModel::class.java)
        val recyclerView = layout.findViewById<RecyclerView>(R.id.recyclerview_piles)
        val adapter = PileListAdapter(layout.context)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(activity!!, 2)
        recyclerView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
                val itemWidth = resources.getDimensionPixelSize(R.dimen.pile_dimen)
                val deviceWidth = getDeviceWidth(activity!!)
                val space = (deviceWidth - itemWidth * 2) / 3
                val halfSpace = space / 2
                if (parent.paddingLeft != halfSpace) {
                    parent.setPadding(halfSpace, halfSpace, halfSpace, halfSpace)
                    parent.clipToPadding = false
                }
                outRect.set(halfSpace, halfSpace, halfSpace, halfSpace)
            }
        })
        addToolbar(hasBackButton = false, title = resources.getString(R.string.app_name))
        addEventHandlers(adapter)
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
            activity?.recreate()
            true
        }
        R.id.action_about -> startFragment(AboutFragment()).run { true }
        else -> super.onOptionsItemSelected(item)
    }

    private fun addEventHandlers(adapter: PileListAdapter) {
        pileVM.allPiles.observe(this, Observer { piles ->
            // Update the cached copy of the words in the adapter.
            piles?.let { adapter.setPiles(it) }
        })
        layout.addPileBtn.setOnClickListener {
            sessionVM.pileId.postValue(NO_ID)
            startFragment(PileEditFragment())
        }
    }

    override fun onBackPressed(): Boolean {
        if (backPressedTwice) return false
        this.backPressedTwice = true
        Toast.makeText(context, "Press back again to exit", Toast.LENGTH_SHORT).show()
        Handler().postDelayed({ backPressedTwice = false }, 2000)
        return true
    }
}
