package com.example.byheart.pile.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.byheart.MainActivity
import com.example.byheart.R
import com.example.byheart.card.CardFragment
import com.example.byheart.pile.Pile
import com.example.byheart.pile.PileViewModel
import com.example.byheart.shared.inflate
import com.example.byheart.shared.startFragment
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.content_pile_edit.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class PileEditFragment : Fragment() {

    private lateinit var layout: View
    private lateinit var pileViewModel: PileViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = container!!.inflate(R.layout.content_pile_edit)
        pileViewModel = ViewModelProviders.of(this).get(PileViewModel::class.java)
        (activity as MainActivity).closeDrawer()
        (activity as MainActivity).setToolbarTitle("Add pile")
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addEventHandlers()
    }

    private fun addEventHandlers() {
        btnSavePile.setOnClickListener {
            val name = pileName.text.toString()
            if (name.isEmpty()) pileNameLayout.error = "You need to enter a name"
            else {
                pileName.clearFocus()
                val pile = Pile(name)
                switchToNewPile(pile)
            }
        }
    }

    private fun switchToNewPile(pile: Pile) = GlobalScope.launch(Dispatchers.Main) {
        val activity = activity as MainActivity
        activity.pileId = pileViewModel.insert(pile).await().toString()
        activity.pileName = pile.name!!
        activity.findViewById<LinearLayout>(R.id.menuFabs)?.alpha = 1F
        fragmentManager?.startFragment(CardFragment())
    }
}
