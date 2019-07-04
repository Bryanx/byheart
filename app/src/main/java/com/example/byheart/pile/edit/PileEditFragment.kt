package com.example.byheart.pile.edit

import android.graphics.PorterDuff
import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.byheart.MainActivity
import com.example.byheart.R
import com.example.byheart.card.CardFragment
import com.example.byheart.pile.Pile
import com.example.byheart.pile.PileFragment
import com.example.byheart.pile.PileViewModel
import com.example.byheart.shared.*
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
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundle()
        if ((activity as MainActivity).pileId.isNotEmpty()) {
            addToolbar(activity!!, true, "Edit pile", true) {
                    fragmentManager?.startFragment(CardFragment())
            }
        } else {
            addToolbar(activity!!, true, "Create pile", true) {
                fragmentManager?.startFragment(PileFragment())
            }
        }
        pileName.focus()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_pile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_confirm_edit_pile -> {
            addPile()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun addPile() {
        val name = pileName.string
        if (name.isEmpty()) {
            pileNameLayout.error = "You need to enter a name"
        } else {
            pileName.clearFocus()
            val pile = Pile(name)
            switchToNewPile(pile)
        }
    }

    private fun switchToNewPile(pile: Pile) = GlobalScope.launch(Dispatchers.Main) {
        val activity = activity as MainActivity
        if (activity.pileId.isNotEmpty()) {
            pile.id = activity.pileId.toLong()
            pileViewModel.update(pile)
            activity.pileName = pile.name!!
            fragmentManager?.startFragment(CardFragment())
        } else {
            activity.pileId = pileViewModel.insert(pile).await().toString()
            fragmentManager?.startFragment(PileFragment())
        }
    }

    private fun getBundle() {
        val activity = activity as MainActivity
        if (activity.pileId.isNotEmpty()) pileName.setText(activity.pileName)
    }
}
