package nl.bryanderidder.byheart.pile.edit

import android.os.Bundle
import android.speech.tts.TextToSpeech
import android.view.*
import android.widget.ArrayAdapter
import androidx.appcompat.widget.AppCompatSpinner
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView.NO_ID
import kotlinx.android.synthetic.main.content_pile_edit.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.card.CardFragment
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.pile.PileFragment
import nl.bryanderidder.byheart.pile.PileViewModel
import nl.bryanderidder.byheart.shared.*
import nl.bryanderidder.byheart.shared.Preferences.DARK_MODE
import java.util.*


/**
 * In this fragment piles are editing or created.
 * @author Bryan de Ridder
 */
class PileEditFragment : Fragment(), IOnBackPressed {

    private lateinit var piles: List<Pile>
    private lateinit var layout: View
    private lateinit var pileVM: PileViewModel
    private lateinit var sessionVM: SessionViewModel
    private lateinit var textToSpeech: TextToSpeech
    private lateinit var adapter: ArrayAdapter<String>
    private var localeList: MutableList<Locale> = mutableListOf()
    private var countries: MutableList<String> = mutableListOf()
    private var locales: Array<Locale> = Locale.getAvailableLocales()
    private var editMode: Boolean = false

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = container!!.inflate(R.layout.content_pile_edit)
        pileVM = ViewModelProviders.of(this).get(PileViewModel::class.java)
        sessionVM = ViewModelProviders.of(activity!!).get(SessionViewModel::class.java)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        getBundle()
        addToolbar(title = when {
                editMode -> resources.getString(R.string.edit_pile)
                else -> resources.getString(R.string.create_pile)
        })
        addEventHandlers()
    }

    private fun setUpTextToSpeech() {
        textToSpeech = TextToSpeech(activity?.applicationContext, TextToSpeech.OnInitListener {
            if (it == TextToSpeech.SUCCESS) {
                for (locale in locales) {
                    val res = textToSpeech.isLanguageAvailable(locale)
                    if (res == TextToSpeech.LANG_COUNTRY_AVAILABLE) {
                        if (!countries.contains(locale.displayName)) {
                            localeList.add(locale)
                            countries.add(locale.displayName)
                        }
                    }
                }
                if (Preferences.read(DARK_MODE)) {
                    adapter = ArrayAdapter(context!!, R.layout.spinner_dark_item, countries)
                    adapter.setDropDownViewResource(R.layout.spinner_dark_dropdown_item)
                } else {
                    adapter = ArrayAdapter(context!!, android.R.layout.simple_spinner_item, countries)
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
                }
                fillSpinner(spinnerCardFront, adapter, "languageCardFront")
                fillSpinner(spinnerCardBack, adapter, "languageCardBack")
            }
        })
    }

    private fun fillSpinner(spinner: AppCompatSpinner, adapter: ArrayAdapter<String>, attr: String) {
        spinner.adapter = adapter
        if (editMode) {
            val thisPile = piles.find { it.id == sessionVM.pileId.value }
            spinner.setSelection(localeList.indexOfFirst { it.code ==  thisPile?.getAttr(attr)})
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
        else -> super.onOptionsItemSelected(item)
    }

    private fun checkInput(): Boolean {
        var isCorrect = true
        val name = etPileName.string
        when {
            name.isEmpty() -> {
                pileNameLayout.isErrorEnabled = true
                pileNameLayout.error = "You need to enter a name"
                isCorrect = false
            }
            name.toLowerCase() in piles.map { it.name?.toLowerCase() } -> {
                if ((editMode && name != sessionVM.pileName.value) || !editMode) {
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
        etPileName.clearFocus()
        val pile = Pile(etPileName.string)
        pile.languageCardFront = getLocaleFromSpinner(spinnerCardFront)
        pile.languageCardBack = getLocaleFromSpinner(spinnerCardBack)
        if (editMode) {
            pile.id = sessionVM.pileId.value ?: NO_ID
            pileVM.update(pile)
            sessionVM.pileName.postValue(pile.name)
            startFragment(CardFragment())
        } else {
            sessionVM.pileId.postValue(pileVM.insert(pile))
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
        etPileName.onEnter {
            if (checkInput()) addOrUpdatePile()
            return@onEnter true
        }
        pileVM.allPiles.observe(this, Observer {
            piles = it
            setUpTextToSpeech()
        })
        etPileName.addTextChangedListener { checkInput() }
        etPileName.setOnFocusChangeListener { _, hasFocus -> if (!hasFocus) checkInput() }
    }

    override fun onBackPressed(): Boolean {
        return startFragment(PileFragment()).run { true }
    }
}