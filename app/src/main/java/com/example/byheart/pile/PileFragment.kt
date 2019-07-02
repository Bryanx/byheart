package com.example.byheart.pile

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.byheart.MainActivity
import com.example.byheart.R
import com.example.byheart.pile.edit.PileEditFragment
import com.example.byheart.shared.Preferences
import com.example.byheart.shared.Preferences.DARK_MODE
import com.example.byheart.shared.addToolbar
import com.example.byheart.shared.getDeviceWidth
import com.example.byheart.shared.startFragment
import kotlinx.android.synthetic.main.content_piles.view.*


class PileFragment : Fragment() {

    private lateinit var layout: View
    private lateinit var pileViewModel: PileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = inflater.inflate(R.layout.content_piles, container, false)
        pileViewModel = ViewModelProviders.of(this).get(PileViewModel::class.java)
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
                    parent.setPadding(halfSpace, halfSpace, halfSpace, halfSpace);
                    parent.clipToPadding = false
                }
                outRect.set(halfSpace, halfSpace, halfSpace, halfSpace)
            }
        })
        addToolbar(activity!!, false, resources.getString(R.string.app_name), true, null)
        addEventHandlers(adapter)
        return layout
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_dark_mode -> {
            Preferences.toggle(DARK_MODE)
            startActivity(Intent(activity!!, MainActivity::class.java))
            true
        }
        R.id.action_settings -> {
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun addEventHandlers(adapter: PileListAdapter) {
        pileViewModel.allPiles.observe(this, Observer { piles ->
            // Update the cached copy of the words in the adapter.
            piles?.let { adapter.setPiles(it) }
        })
        layout.addPileBtn.setOnClickListener {
            val activity = (context as MainActivity)
            activity.pileId = ""
            activity.supportFragmentManager.startFragment(PileEditFragment())
        }
    }
}
