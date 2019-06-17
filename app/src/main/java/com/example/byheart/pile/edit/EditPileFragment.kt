package com.example.byheart.pile.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.byheart.MainActivity
import com.example.byheart.R
import com.example.byheart.card.CardFragment
import com.example.byheart.pile.Pile
import com.example.byheart.pile.PileViewModel
import com.example.byheart.shared.inflate
import kotlinx.coroutines.*

class EditPileFragment : Fragment() {

    private lateinit var layout: View
    private lateinit var pileViewModel: PileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = container!!.inflate(R.layout.content_edit_pile)
        pileViewModel = ViewModelProviders.of(this).get(PileViewModel::class.java)
        addEventHandlers()
        (activity as MainActivity).closeDrawer()
        return layout
    }

    private fun addEventHandlers() {
        val submitButton = layout.findViewById<Button>(R.id.content_edit_pile_button_submit)
        submitButton.setOnClickListener {
            val editTextName = layout.findViewById<EditText>(R.id.content_edit_pile_edittext_name)
            editTextName.clearFocus()
            val pile = Pile(editTextName.text.toString())
            switchToNewPile(pile)
        }
    }

    private fun switchToNewPile(pile: Pile) = GlobalScope.launch(Dispatchers.Main) {
        pile.id = pileViewModel.insert(pile).await()
        activity!!.supportFragmentManager
            .beginTransaction()
            .replace(R.id.main_container,
                CardFragment.newInstance(pile.name!!, pile.id.toString()))
            .commit()
    }
}
