package nl.bryanderidder.byheart.rehearsal.setup

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.content_rehearsal_setup.*
import nl.bryanderidder.byheart.BaseBottomSheet
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.pile.PileViewModel
import nl.bryanderidder.byheart.rehearsal.RehearsalMemoryFragment
import nl.bryanderidder.byheart.rehearsal.RehearsalMultipleChoiceFragment
import nl.bryanderidder.byheart.rehearsal.RehearsalTypedFragment
import nl.bryanderidder.byheart.shared.*
import nl.bryanderidder.byheart.shared.Preferences.KEY_REHEARSAL_MEMORY
import nl.bryanderidder.byheart.shared.Preferences.KEY_REHEARSAL_MULTIPLE_CHOICE
import nl.bryanderidder.byheart.shared.Preferences.KEY_REHEARSAL_PRONOUNCE
import nl.bryanderidder.byheart.shared.Preferences.KEY_REHEARSAL_REVERSE
import nl.bryanderidder.byheart.shared.Preferences.KEY_REHEARSAL_SHUFFLE
import nl.bryanderidder.byheart.shared.Preferences.KEY_REHEARSAL_TYPED

class RehearsalSetupFragment : BaseBottomSheet() {

    private lateinit var pile: Pile
    //    private lateinit var cardVM: CardViewModel
    private lateinit var sessionVM: SessionViewModel
    private lateinit var pileVM: PileViewModel
    private lateinit var layout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = inflater.inflate(R.layout.content_rehearsal_setup, container, false)
//        cardVM = ViewModelProviders.of(activity!!).get(CardViewModel::class.java)
        sessionVM = ViewModelProviders.of(activity!!).get(SessionViewModel::class.java)
        pileVM = ViewModelProviders.of(activity!!).get(PileViewModel::class.java)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crsShuffle.isChecked = Preferences.REHEARSAL_SHUFFLE
        crsPronounce.isChecked = Preferences.REHEARSAL_PRONOUNCE
        crsReverse.isChecked = Preferences.REHEARSAL_REVERSE
        btnMultipleChoice.isSelected = Preferences.REHEARSAL_MULTIPLE_CHOICE
        btnTyped.isSelected = Preferences.REHEARSAL_TYPED
        btnMemory.isSelected = Preferences.REHEARSAL_MEMORY
        setColors()
        updateModeDescription()
        addEventHandlers()
    }

    private fun setColors() {
        pileVM.allPiles.observe(this, Observer { piles ->
            pile = piles.find { it.id == sessionVM.pileId.value }!!
            if (!Preferences.DARK_MODE) {
                toggleGroup.highlightBgColor = pile?.color!!
                crsModeDescription.setTextColor(pile.color!!.setBrightness(0.55F))
                crsStart.textColor = pile.color!!.setBrightness(0.55F)
            } else {
                toggleGroup.highlightBgColor = context!!.getAttr(R.attr.dialogBackgroundButton)
                toggleGroup.highLightText = pile?.color!!
                crsModeDescription.setTextColor(pile.color!!)
                crsStart.textColor = pile.color!!
            }
            updateSwitchColors()
            toggleGroup.styleButtons()
        })
    }

    private fun addEventHandlers() {
        crsShuffle.setOnCheckedChangeListener { _, checked -> updateSwitch(KEY_REHEARSAL_SHUFFLE, checked) }
        crsPronounce.setOnCheckedChangeListener { _, checked -> updateSwitch(KEY_REHEARSAL_PRONOUNCE, checked) }
        crsReverse.setOnCheckedChangeListener { _, checked -> updateSwitch(KEY_REHEARSAL_REVERSE, checked) }
        btnMultipleChoice.setOnClickListener { selectMode(KEY_REHEARSAL_MULTIPLE_CHOICE) }
        btnTyped.setOnClickListener { selectMode(KEY_REHEARSAL_TYPED) }
        btnMemory.setOnClickListener { selectMode(KEY_REHEARSAL_MEMORY) }
        crsStart.setOnClickListener { onStartPressed() }
    }

    private fun onStartPressed() {
        dismiss()
        startFragment(when {
            Preferences.REHEARSAL_MEMORY -> RehearsalMemoryFragment()
            Preferences.REHEARSAL_TYPED -> RehearsalTypedFragment()
            else -> RehearsalMultipleChoiceFragment()
        })
    }

    private fun updateSwitch(key: String, checked: Boolean) {
        Preferences.write(key, checked)
        updateSwitchColors()
    }

    private fun updateSwitchColors() {
        listOf(crsShuffle, crsPronounce, crsReverse).forEach {
            it.checkedColor = pile.color!!
            if (!Preferences.DARK_MODE) it.unCheckedColor = context!!.color(R.color.grey_300)
            else it.unCheckedColor = context!!.color(R.color.white)
        }
    }

    private fun selectMode(key: String) {
        listOf(KEY_REHEARSAL_MULTIPLE_CHOICE, KEY_REHEARSAL_TYPED, KEY_REHEARSAL_MEMORY).forEach {
            if (key == it) Preferences.write(it, true)
            else Preferences.write(it, false)
        }
        updateModeDescription()
    }

    private fun updateModeDescription() {
        crsModeDescription.text = when {
            Preferences.read(KEY_REHEARSAL_MULTIPLE_CHOICE) -> resources.getString(R.string.multiple_choice_mode_description)
            Preferences.read(KEY_REHEARSAL_TYPED) -> resources.getString(R.string.typed_mode_description)
            Preferences.read(KEY_REHEARSAL_MEMORY) -> resources.getString(R.string.memory_mode_description)
            else -> "Choose a mode to continue."
        }
    }

}