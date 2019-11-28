package nl.bryanderidder.byheart.store

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.content_store.*
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.pile.PileFragment
import nl.bryanderidder.byheart.shared.IOnBackPressed
import nl.bryanderidder.byheart.shared.startFragment

/**
 * Fragment that contains all remote piles.
 * @author Bryan de Ridder
 */
class StoreFragment : Fragment(), IOnBackPressed {

    private lateinit var storeVM: StoreViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.content_store, container, false)
        storeVM = ViewModelProviders.of(activity!!).get(StoreViewModel::class.java)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val adapter = ArrayAdapter<String>(context!!, android.R.layout.simple_list_item_1)
        pileList.adapter = adapter
        storeVM.getPiles().observe(this, Observer { piles ->
            adapter.clear()
            adapter.addAll(piles.map { it.name })
        })
    }

    private fun addEventHandlers() {
    }

    override fun onBackPressed(): Boolean = startFragment(PileFragment()).run { true }

}