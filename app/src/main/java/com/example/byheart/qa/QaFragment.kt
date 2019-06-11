package com.example.byheart.qa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.byheart.BaseActivity
import com.example.byheart.R

class QaFragment : Fragment() {

    private lateinit var qaViewModel: QaViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val layout = inflater.inflate(R.layout.content_qa, container, false)
        qaViewModel = ViewModelProviders.of(this).get(QaViewModel::class.java)
        val recyclerView = layout.findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = QaListAdapter(layout.context)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(layout.context)
        addEventHandlers(adapter)
        return layout
    }

    private fun addEventHandlers(adapter: QaListAdapter) {
        qaViewModel.allQas.observe(this, Observer { qas ->
            // Update the cached copy of the words in the adapter.
            qas?.let { adapter.setQas(it) }
        })
    }
}
