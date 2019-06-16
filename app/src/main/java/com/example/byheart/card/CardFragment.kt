package com.example.byheart.card

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
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.byheart.R

class CardFragment : Fragment() {

    private lateinit var cardViewModel: CardViewModel
    private lateinit var layout: View
    private var pileId: String? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = inflater.inflate(R.layout.content_card, container, false)
        cardViewModel = ViewModelProviders.of(this).get(CardViewModel::class.java)
        getBundle(layout)
        val recyclerView = layout.findViewById<RecyclerView>(R.id.recyclerview)
        val adapter = CardListAdapter(layout.context)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(layout.context)
        addEventHandlers(adapter)
        return layout
    }

    private fun getBundle(layout: View) {
        val name = arguments!!.getString(NAME_KEY)
        pileId = arguments!!.getString(ID_KEY)
        layout.findViewById<TextView>(R.id.content_card_title).text = name
    }

    private fun addEventHandlers(adapter: CardListAdapter) {
        cardViewModel.allCards.observe(this, Observer { cards ->
            // Update the cached copy of the words in the adapter.
            cards?.filter {
                it.pileId.toString() == pileId
            }?.let { adapter.setCards(it) }
        })
        layout.findViewById<Button>(R.id.buttonAdd)?.setOnClickListener {
            val etInput = EditText(activity)
            layout.findViewById<LinearLayout>(R.id.llBottom)?.addView(etInput)
            etInput.requestFocus()
            etInput.setOnFocusChangeListener { _, hasFocus ->
                if (!hasFocus) layout.findViewById<LinearLayout>(R.id.llBottom)?.removeAllViews()
            }
            etInput.setOnKeyListener { view, keyCode, event ->
                if (event.action == KeyEvent.ACTION_DOWN && keyCode == KeyEvent.KEYCODE_ENTER) {
                    cardViewModel.insert(Card(etInput.text.toString(), "bla", pileId?.toLong() ?: 0))
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

    companion object {
        private const val NAME_KEY = "name"
        private const val ID_KEY = "id"

        // factory method
        fun newInstance(name: String, pileId: String): CardFragment {
            val fragment = CardFragment()
            val args = Bundle()
            args.putString(NAME_KEY, name)
            args.putString(ID_KEY, pileId)
            fragment.arguments = args
            return fragment
        }
    }
}
