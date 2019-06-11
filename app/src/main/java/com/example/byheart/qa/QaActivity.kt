package com.example.byheart.qa

import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.byheart.BaseActivity
import com.example.byheart.R

class QaActivity : BaseActivity() {

    private lateinit var qaViewModel: QaViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_qa)
        qaViewModel = ViewModelProviders.of(this).get(QaViewModel::class.java)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = QaListAdapter(this)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        addEventHandlers(adapter)
    }

    private fun addEventHandlers(adapter: QaListAdapter) {
        qaViewModel.allQas.observe(this, Observer { qas ->
            // Update the cached copy of the words in the adapter.
            qas?.let { adapter.setQas(it) }
        })
    }
}
