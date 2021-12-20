package nl.bryanderidder.byheart.rehearsal.setup

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.content_rehearsal_setup.*
import nl.bryanderidder.byheart.BaseBottomSheet
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.rehearsal.RehearsalMemoryFragment
import nl.bryanderidder.byheart.rehearsal.RehearsalMultipleChoiceFragment
import nl.bryanderidder.byheart.rehearsal.RehearsalTypedFragment
import nl.bryanderidder.byheart.shared.*
import nl.bryanderidder.byheart.shared.Preferences.KEY_REHEARSAL_MEMORY
import nl.bryanderidder.byheart.shared.Preferences.KEY_REHEARSAL_MULTIPLE_CHOICE
import nl.bryanderidder.byheart.shared.Preferences.KEY_REHEARSAL_PRONOUNCE
import nl.bryanderidder.byheart.shared.Preferences.KEY_REHEARSAL_REPEAT_WRONG
import nl.bryanderidder.byheart.shared.Preferences.KEY_REHEARSAL_REVERSE
import nl.bryanderidder.byheart.shared.Preferences.KEY_REHEARSAL_SHUFFLE
import nl.bryanderidder.byheart.shared.Preferences.KEY_REHEARSAL_TYPED
import org.koin.android.viewmodel.ext.android.sharedViewModel

@SuppressLint("ResourceAsColor")
class RehearsalSetupFragment : BaseBottomSheet() {

    private var pileColor: Int = R.color.colorPrimary
    private var moreThanFiveCards: Boolean = false
    private val sessionVM: SessionViewModel by sharedViewModel()
    private lateinit var layout: View

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = inflater.inflate(R.layout.content_rehearsal_setup, container, false)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        crsShuffle.isChecked = Preferences.REHEARSAL_SHUFFLE
        crsPronounce.isChecked = Preferences.REHEARSAL_PRONOUNCE
        crsReverse.isChecked = Preferences.REHEARSAL_REVERSE
        crsRepeatWrong.isChecked = Preferences.REHEARSAL_REPEAT_WRONG
        if (Preferences.REHEARSAL_MULTIPLE_CHOICE) toggleGroup.selectButton(btnMultipleChoice)
        if (Preferences.REHEARSAL_TYPED) toggleGroup.selectButton(btnTyped)
        if (Preferences.REHEARSAL_MEMORY) toggleGroup.selectButton(btnMemory)
        moreThanFiveCards = sessionVM.cardCount.value ?: 0 > 5
        pileColor = sessionVM.pileColor.value ?: requireContext().color(R.color.colorPrimary)
        setColors()
        updateModeDescription()
        addEventHandlers()
    }

    private fun setColors() {
        if (!Preferences.DARK_MODE) {
            toggleGroup.buttons.forEach { it.selectedBgColor = pileColor }
            crsModeDescription.setTextColor(pileColor.setBrightness(0.55F))
            crsStart.textColor = pileColor.setBrightness(0.55F)
        } else {
            toggleGroup.buttons.forEach { it.selectedBgColor = pileColor.setBrightness(0.65F) }
            crsModeDescription.setTextColor(pileColor)
            crsStart.textColor = pileColor
        }
        listOf(crsShuffle, crsPronounce, crsReverse, crsRepeatWrong).forEach {
            it.switchColor = pileColor
        }
    }

    private fun addEventHandlers() {
        crsShuffle.setOnCheckedChanged { checked -> Preferences.write(KEY_REHEARSAL_SHUFFLE, checked) }
        crsPronounce.setOnCheckedChanged { checked -> Preferences.write(KEY_REHEARSAL_PRONOUNCE, checked) }
        crsReverse.setOnCheckedChanged { checked -> Preferences.write(KEY_REHEARSAL_REVERSE, checked) }
        crsRepeatWrong.setOnCheckedChanged { checked -> Preferences.write(KEY_REHEARSAL_REPEAT_WRONG, checked) }
        crsStart.setOnClickListener { onStartPressed() }
        toggleGroup.setOnSelectListener {
            when (it) {
                btnMultipleChoice -> selectMode(KEY_REHEARSAL_MULTIPLE_CHOICE)
                btnTyped -> selectMode(KEY_REHEARSAL_TYPED)
                btnMemory -> selectMode(KEY_REHEARSAL_MEMORY)
            }
        }
    }

    private fun onStartPressed() {
        if (Preferences.read(KEY_REHEARSAL_MULTIPLE_CHOICE) && !moreThanFiveCards) return
        dismiss()
        startFragment(when {
            Preferences.REHEARSAL_MEMORY -> RehearsalMemoryFragment()
            Preferences.REHEARSAL_TYPED -> RehearsalTypedFragment()
            else -> RehearsalMultipleChoiceFragment()
        })
    }

    private fun selectMode(key: String) {
        listOf(KEY_REHEARSAL_MULTIPLE_CHOICE, KEY_REHEARSAL_TYPED, KEY_REHEARSAL_MEMORY).forEach {
            Preferences.write(it, key == it)
        }
        updateModeDescription()
    }

    private fun updateModeDescription() {
        crsModeDescription.text = when {
            Preferences.REHEARSAL_MULTIPLE_CHOICE && moreThanFiveCards -> resources.getString(R.string.multiple_choice_mode_description)
            Preferences.REHEARSAL_MULTIPLE_CHOICE && !moreThanFiveCards -> resources.getString(R.string.five_card_warning)
            Preferences.REHEARSAL_TYPED -> resources.getString(R.string.typed_mode_description)
            Preferences.REHEARSAL_MEMORY -> resources.getString(R.string.memory_mode_description)
            else -> "Choose a mode to continue."
        }
        if (Preferences.read(KEY_REHEARSAL_MULTIPLE_CHOICE) && !moreThanFiveCards)
            crsModeDescription.setTextColor(requireContext().color(R.color.red))
        else if (Preferences.DARK_MODE) crsModeDescription.setTextColor(pileColor)
        else crsModeDescription.setTextColor(pileColor.setBrightness(0.55F))
    }

}