package nl.bryanderidder.byheart.store

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.content_store.*
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.pile.PileFragment
import nl.bryanderidder.byheart.pile.PileListAdapter
import nl.bryanderidder.byheart.shared.IOnBackPressed
import nl.bryanderidder.byheart.shared.startFragment
import nl.bryanderidder.byheart.shared.views.GridAutofitLayoutManager
import org.koin.android.viewmodel.ext.android.sharedViewModel

/**
 * Fragment that contains all remote piles.
 * @author Bryan de Ridder
 */
class StoreFragment : Fragment(), IOnBackPressed {

    private val storeVM: StoreViewModel by sharedViewModel()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.content_store, container, false)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = PileListAdapter(context!!, mutableListOf(), ::onClickPile, ::onAfterMovingPiles)
        recyclerviewStorePiles.adapter = adapter
        recyclerviewStorePiles.layoutManager = GridAutofitLayoutManager(activity!!, 500)
        addEventHandlers(adapter)
    }

    private fun addEventHandlers(adapter: PileListAdapter) {
        storeVM.allPiles.observe(this, Observer { piles ->
            adapter.setPiles(piles.toMutableList())
        })
    }

    private fun onAfterMovingPiles(piles: List<Pile>) {}

    private fun onClickPile(id: Long, name: String, piles: List<Pile>) {
    }

    override fun onBackPressed(): Boolean = startFragment(PileFragment()).run { true }

}