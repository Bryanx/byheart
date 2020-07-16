package nl.bryanderidder.byheart.rehearsal.setup

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.animation.DecelerateInterpolator
import android.view.animation.OvershootInterpolator
import android.view.animation.ScaleAnimation
import kotlinx.android.synthetic.main.content_rehearsal_setup.*
import nl.bryanderidder.byheart.BaseActivity
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

@SuppressLint("ResourceAsColor")
class RehearsalSetupFragment : BaseBottomSheet() {

    private var pileColor: Int = R.color.colorPrimary
    private var moreThanFiveCards: Boolean = false
    private val sessionVM: SessionViewModel by lazy { (activity!! as BaseActivity).sessionVm }
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
        pileColor = sessionVM.pileColor.value ?: context!!.color(R.color.colorPrimary)
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
        updateSwitchColors()
    }

    private fun addEventHandlers() {
        crsShuffle.setOnCheckedChangeListener { _, checked -> updateSwitch(KEY_REHEARSAL_SHUFFLE, checked) }
        crsPronounce.setOnCheckedChangeListener { _, checked -> updateSwitch(KEY_REHEARSAL_PRONOUNCE, checked) }
        crsReverse.setOnCheckedChangeListener { _, checked -> updateSwitch(KEY_REHEARSAL_REVERSE, checked) }
        crsRepeatWrong.setOnCheckedChangeListener { _, checked -> updateSwitch(KEY_REHEARSAL_REPEAT_WRONG, checked) }
        btnMultipleChoice.setOnClickListener { selectMode(KEY_REHEARSAL_MULTIPLE_CHOICE) }
        btnTyped.setOnClickListener { selectMode(KEY_REHEARSAL_TYPED) }
        btnMemory.setOnClickListener { selectMode(KEY_REHEARSAL_MEMORY) }
        crsStart.setOnClickListener { onStartPressed() }
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

    private fun updateSwitch(key: String, checked: Boolean) {
        Preferences.write(key, checked)
        updateSwitchColors()
    }

    private fun updateSwitchColors() {
        listOf(crsShuffle, crsPronounce, crsReverse, crsRepeatWrong).forEach {
            if (!Preferences.DARK_MODE) {
                it.checkedColor = pileColor
                it.unCheckedColor = context!!.color(R.color.grey_300)
            } else {
                it.checkedColor = pileColor.setBrightness(0.65F)
                it.unCheckedColor = context!!.color(R.color.white)
            }
        }
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
            crsModeDescription.setTextColor(context!!.color(R.color.red))
        else if (Preferences.DARK_MODE) crsModeDescription.setTextColor(pileColor)
        else crsModeDescription.setTextColor(pileColor.setBrightness(0.55F))
    }

}