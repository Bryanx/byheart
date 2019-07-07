package com.example.byheart.pile.edit

import android.animation.LayoutTransition
import android.os.Bundle
import android.view.*
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.byheart.MainActivity
import com.example.byheart.R
import com.example.byheart.card.CardFragment
import com.example.byheart.pile.Pile
import com.example.byheart.pile.PileFragment
import com.example.byheart.pile.PileViewModel
import com.example.byheart.shared.*
import kotlinx.android.synthetic.main.content_card_edit.*
import kotlinx.android.synthetic.main.content_pile_edit.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class PileEditFragment : Fragment(), IOnBackPressed {

    private lateinit var piles: List<Pile>
    private lateinit var layout: View
    private lateinit var pileViewModel: PileViewModel
    private var editMode: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = container!!.inflate(R.layout.content_pile_edit)
        pileViewModel = ViewModelProviders.of(this).get(PileViewModel::class.java)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundle()
        addToolbar(true, when {
                editMode -> "Edit pile"
                else -> "Create pile"
            }, true
        )
        etPileName.focus()
        addEventHandlers()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_pile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_confirm_edit_pile -> {
            if (checkInput()) addOrUpdatePile()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun checkInput(): Boolean {
        var isCorrect = true
        val activity = (activity as MainActivity)
        val name = etPileName.string
        when {
            name.isEmpty() -> {
                pileNameLayout.isErrorEnabled = true
                pileNameLayout.error = "You need to enter a name"
                isCorrect = false
            }
            name.toLowerCase() in piles.map { it.name?.toLowerCase() } -> {
                if ((editMode && name != activity.pileName) || !editMode) {
                    pileNameLayout.isErrorEnabled = true
                    pileNameLayout.error = "You already have a pile with the same name"
                    isCorrect = false
                }
            }
            else -> {
                pileNameLayout.isErrorEnabled = false
                isCorrect = true
            }
        }
        return isCorrect
    }

    private fun addOrUpdatePile() = GlobalScope.launch(Dispatchers.Main) {
        val activity = activity as MainActivity
        etPileName.clearFocus()
        val pile = Pile(etPileName.string)
        if (editMode) {
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
        editMode = activity.pileId.isNotEmpty()
        if (editMode) etPileName.setText(activity.pileName)
    }

    private fun addEventHandlers() {
        etPileName.onEnter {
            if (checkInput()) addOrUpdatePile()
            return@onEnter true
        }
        pileViewModel.allPiles.observe(this, Observer { piles = it })
        etPileName.addTextChangedListener { checkInput() }
        etPileName.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) checkInput() }
    }

    override fun onBackPressed(): Boolean {
        return fragmentManager?.startFragment(PileFragment()).run { true }
    }
}
