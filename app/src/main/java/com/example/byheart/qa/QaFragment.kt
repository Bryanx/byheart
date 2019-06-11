package com.example.byheart.qa

import android.content.Context
import android.os.Bundle
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.byheart.R


class QaFragment : Fragment() {

    private lateinit var qaViewModel: QaViewModel
    private lateinit var layout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = inflater.inflate(R.layout.content_qa, container, false)
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
        layout.findViewById<Button>(R.id.buttonAdd)?.setOnClickListener {
            val etInput = EditText(activity)
            layout.findViewById<LinearLayout>(R.id.llBottom)?.addView(etInput)
            etInput.requestFocus()
            etInput.setOnFocusChangeListener { view, hasFocus ->
                if (!hasFocus) layout.findViewById<LinearLayout>(R.id.llBottom)?.removeAllViews()
            }
            etInput.setOnKeyListener { view, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    qaViewModel.insert(Qa(etInput.text.toString(), "bla"))
                    view.clearFocus()
                    layout.findViewById<LinearLayout>(R.id.llBottom)?.removeAllViews()
                }
                true
            }
            showKeyboard(etInput)
        }
    }

    private fun showKeyboard(etInput: View) {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        imm!!.showSoftInput(etInput, InputMethodManager.SHOW_IMPLICIT)
    }
}
