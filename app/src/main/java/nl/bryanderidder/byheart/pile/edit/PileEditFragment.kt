package nl.bryanderidder.byheart.pile.edit

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView.NO_ID
import kotlinx.android.synthetic.main.content_pile_edit.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.card.CardFragment
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.pile.PileFragment
import nl.bryanderidder.byheart.pile.PileViewModel
import nl.bryanderidder.byheart.shared.*
import nl.bryanderidder.byheart.shared.utils.getColors
import nl.bryanderidder.byheart.shared.utils.showSnackBar
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.*


/**
 * In this fragment piles are edited or created.
 * @author Bryan de Ridder
 */
class PileEditFragment : Fragment(), IOnBackPressed {

    private var pileColor: Int = R.color.blue_200
    private lateinit var piles: List<Pile>
    private lateinit var layout: View
    private val sessionVM: SessionViewModel by sharedViewModel()
    private val pileVM: PileViewModel by sharedViewModel()
    private lateinit var adapter: ArrayAdapter<String>
    private var localeList: MutableList<Locale> = mutableListOf()
    private var countries: MutableList<String> = mutableListOf()
    private var locales: Array<Locale> = Locale.getAvailableLocales()
    private var editMode: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = container!!.inflate(R.layout.content_pile_edit)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setRandomColor()
        getBundle()
        if (editMode) addToolbar(title = resources.getString(R.string.edit_pile))
        else addToolbar(title = resources.getString(R.string.create_pile))
        etPileName.showKeyboard()
        addEventHandlers()
    }

    private fun setRandomColor() {
        pileColor = getColors(requireContext()).random()
    }

    private fun setUpTextToSpeech() {
        requireContext().resources.getStringArray(R.array.languages).forEach {code ->
            locales.find { it.code == code }?.let {locale ->
                localeList.add(locale)
                countries.add(locale.displayName)
            }
        }
        countries.sort()
        if (Preferences.DARK_MODE) {
            adapter = ArrayAdapter(requireContext(), R.layout.spinner_dark_item, countries)
            adapter.setDropDownViewResource(R.layout.spinner_dark_dropdown_item)
        } else {
            adapter = ArrayAdapter(requireContext(), android.R.layout.simple_spinner_item, countries)
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        fillSpinner(spinnerCardFront, adapter, "languageCardFront")
        fillSpinner(spinnerCardBack, adapter, "languageCardBack")
    }

    private fun fillSpinner(spinner: AppCompatSpinner, adapter: ArrayAdapter<String>, attr: String) {
        spinner.adapter = adapter
        if (editMode) {
            pileVM.allPiles.observe(viewLifecycleOwner, Observer {
                val thisPile = it.find { pile -> pile.id == sessionVM.pileId.value }
                spinner.setSelection(localeList.indexOfFirst { loc -> loc.code == thisPile[attr]})
            })
        } else {
            spinner.setSelection(localeList.indexOfFirst {
                    it.code == "en-GB"
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.edit_pile_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_confirm_edit_pile -> {
            if (checkInput()) addOrUpdatePile()
            activity?.hideKeyboard()
            true
        }
        R.id.action_colorpicker -> {
            val dialog = ColorPickerDialog.newInstance(R.string.go, pileColor, 4, 2)
            dialog.show(requireFragmentManager(), "picker")
            dialog.listener = {
                pileColor = it
            }
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun checkInput(): Boolean {
        var isCorrect = true
        val name = etPileName.string
        when {
            name.isEmpty() -> {
                pileNameLayout.isErrorEnabled = true
                pileNameLayout.error = resources.getString(R.string.field_may_not_be_blank)
                isCorrect = false
            }
            name.toLowerCase() in pileVM.allPiles.value!!.map { it.name?.toLowerCase() } -> {
                if ((editMode && name != sessionVM.pileName.value) || !editMode) {
                    pileNameLayout.isErrorEnabled = true
                    pileNameLayout.error = getString(R.string.pile_same_name)
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

    private fun addOrUpdatePile() = lifecycleScope.launch(Dispatchers.Main) {
        etPileName.clearFocus()
        if (editMode) {
            val pile = pileVM.allPiles.value?.find { it.id == sessionVM.pileId.value ?: NO_ID }
            pile?.apply {
                name = etPileName.string
                languageCardFront = getLocaleFromSpinner(spinnerCardFront)
                languageCardBack = getLocaleFromSpinner(spinnerCardBack)
                color = pileColor
            }
            pile?.let { pileVM.update(pile).invokeOnCompletion {
                sessionVM.pileName.postValue(pile.name)
                sessionVM.pileColor.postValue(pile.color)
                showSnackBar(requireActivity(), getString(R.string.updated_stack))
                startFragment(CardFragment())
            } }
        } else {
            val pile = Pile(etPileName.string)
            pile.languageCardFront = getLocaleFromSpinner(spinnerCardFront)
            pile.languageCardBack = getLocaleFromSpinner(spinnerCardBack)
            pile.color = pileColor
            pileVM.insertAsync(pile).await()
            sessionVM.message.value = getString(R.string.created_stack)
            startFragment(PileFragment())
        }
    }

    private fun getLocaleFromSpinner(spinnerCardFront: AppCompatSpinner): String {
        return localeList[spinnerCardFront.selectedItemPosition].code
    }

    private fun getBundle() {
        editMode = sessionVM.pileId.value != NO_ID
        if (editMode) etPileName.setText(sessionVM.pileName.value)
    }

    private fun addEventHandlers() {
        if (!editMode) setUpTextToSpeech()
        etPileName.onEnter {
            if (checkInput()) addOrUpdatePile()
            return@onEnter true
        }
        pileVM.allPiles.observe(viewLifecycleOwner, Observer { listPiles ->
            this.piles = listPiles
            val pile = piles.find { it.id == sessionVM.pileId.value }
            pileColor = when {
                editMode && pile?.color != null -> pile.color!!
                else -> getColors(requireContext()).random()
            }
            if (editMode) setUpTextToSpeech()
        })
        etPileName.addTextChangedListener { checkInput() }
        etPileName.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) checkInput() }
    }

    override fun onBackPressed(): Boolean = when {
        editMode -> startFragment(CardFragment()).run { true }
        else -> startFragment(PileFragment()).run { true }
    }
}
