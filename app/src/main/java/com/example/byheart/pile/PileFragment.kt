package com.example.byheart.pile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.byheart.R

class PileFragment : Fragment() {

    private lateinit var layout: View
    private lateinit var pileViewModel: PileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = inflater.inflate(R.layout.content_piles, container, false)
        pileViewModel = ViewModelProviders.of(this).get(PileViewModel::class.java)
        val recyclerView = layout.findViewById<RecyclerView>(R.id.recyclerview_piles)
        val adapter = PileListAdapter(layout.context)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(layout.context)
        addEventHandlers(adapter)
        return layout
    }

    private fun addEventHandlers(adapter: PileListAdapter) {
        pileViewModel.allPiles.observe(this, Observer { piles ->
            // Update the cached copy of the words in the adapter.
            piles?.let { adapter.setPiles(it) }
        })
    }
}
