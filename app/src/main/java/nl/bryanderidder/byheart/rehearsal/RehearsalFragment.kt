package nl.bryanderidder.byheart.rehearsal

import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.view.*
import androidx.core.view.forEachIndexed
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView.NO_ID
import kotlinx.android.synthetic.main.content_rehearsal.*
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.card.Card
import nl.bryanderidder.byheart.card.CardFragment
import nl.bryanderidder.byheart.card.CardViewModel
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.pile.PileViewModel
import nl.bryanderidder.byheart.rehearsal.views.RehearsalCard
import nl.bryanderidder.byheart.shared.*
import nl.bryanderidder.byheart.shared.utils.getScreenWidth
import nl.bryanderidder.byheart.shared.utils.moveX
import java.util.*

/**
 * Abstract class that contains the base of a rehearsal fragment.
 * Each Rehearsal Fragment should extend this class.
 * @author Bryan de Ridder
 */
abstract class RehearsalFragment : Fragment(), IOnBackPressed {

    private lateinit var cardVM: CardViewModel
    private lateinit var pileVM: PileViewModel
    private lateinit var sessionVM: SessionViewModel
    protected lateinit var languageCardBack: Locale
    protected lateinit var layout: View
    protected lateinit var pile: Pile
    protected lateinit var cards: MutableList<Card>
    protected lateinit var menu: Menu
    protected lateinit var correctSound: MediaPlayer
    protected lateinit var wrongSound: MediaPlayer
    protected val handler: Handler = Handler()
    protected var cardIndex = 0
    private var pileId: Long = NO_ID

    open fun doAfterGetData(): Unit = addEventHandlers()

    abstract fun addEventHandlers()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        cardVM = ViewModelProviders.of(this).get(CardViewModel::class.java)
        pileVM = ViewModelProviders.of(this).get(PileViewModel::class.java)
        sessionVM = ViewModelProviders.of(activity!!).get(SessionViewModel::class.java)
        pileId = sessionVM.pileId.value ?: NO_ID
        getCards()
        addToolbar()
        pileVM.allPiles.observe(this, Observer { piles ->
            pile = piles.first { pile -> pile.id == pileId }
            layout.findViewById<RehearsalCard>(R.id.rehearsalCard).addPronounceLocale(
                Locale.getAvailableLocales().first { loc -> loc.code == pile.languageCardFront },
                Locale.getAvailableLocales().first { loc -> loc.code == pile.languageCardBack })
        })
        correctSound = MediaPlayer.create(context, R.raw.correct)
        wrongSound = MediaPlayer.create(context, R.raw.incorrect)
        return layout
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.rehearsal_menu, menu)
        this.menu = menu
        menu.forEachIndexed { _, item ->
            item.isChecked = Preferences.read(item.getName(resources))
            if (item.isChecked) hideOtherViews(item)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.rehearsal_restart -> onRestart(true, null)
            R.id.rehearsal_pronounce -> {
                toggleMenuItem(item, item.getName(resources)).run { true }
            }
            R.id.rehearsal_shuffle -> {
                toggleMenuItem(item, item.getName(resources))
                shuffleCards(item.isChecked)
                onRestart(false, null)
            }
            R.id.rehearsal_reverse -> {
                toggleMenuItem(item, item.getName(resources))
                onRestart(false, null)
            }
            R.id.rehearsal_typed -> {
                hideOtherViews(item)
                startFragment(RehearsalTypedFragment()).run { true }
            }
            R.id.rehearsal_memory -> {
                hideOtherViews(item)
                startFragment(RehearsalMemoryFragment()).run { true }
            }
            R.id.rehearsal_multiple_choice -> {
                if (cards.size < 5) context!!.dialog()
                    .setMessage(getString(R.string.five_card_warning))
                    .setCancelable(false)
                    .setPositiveButton(getString(R.string.ok)) { _, _ -> }
                    .setAnimation(R.style.SlidingDialog)
                    .show()
                else {
                    hideOtherViews(item)
                    startFragment(RehearsalMultipleChoiceFragment())
                }
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Toggles the other items in the menu, and shows the corresponding view.
    private fun hideOtherViews(item: MenuItem): Boolean {
        handler.removeMessages(0)
        listOf(R.id.rehearsal_typed, R.id.rehearsal_multiple_choice, R.id.rehearsal_memory).forEach { id ->
            if (id == item.itemId) {
                if (!item.isChecked) toggleMenuItem(item, item.getName(resources))
            } else {
                val otherItem = menu.findItem(id)
                if (otherItem.isChecked) toggleMenuItem(otherItem, otherItem.getName(resources))
            }
        }
        return true
    }

    private fun toggleMenuItem(item: MenuItem, id: String) {
        item.isChecked = !item.isChecked
        Preferences.write(id, item.isChecked)
    }

    private fun shuffleCards(shuffleIsOn: Boolean): Unit = when {
        shuffleIsOn -> cards.shuffle()
        else -> cards.sortBy { it.id }
    }

    private fun getCards() {
        cardVM.allCards.observe(this, Observer { cardsFromDb ->
            cardsFromDb?.filter { it.pileId == pileId }?.let {
                val tempCards = it.toMutableList()
                if (Preferences.REHEARSAL_SHUFFLE) tempCards.shuffle()
                cards = tempCards
                doAfterGetData()
            }
            updateView()
        })
    }

    open fun onRestart(startFromBeginning: Boolean, doAfter: (() -> Unit)?): Boolean {
        handler.removeMessages(0)
        if (startFromBeginning) cardIndex = 0
        rehearsalCard.turnToFront()
        if (Preferences.REHEARSAL_SHUFFLE) cards.shuffle()
        Handler().postDelayed(({
            updateView()
            doAfter?.invoke()
        }), resources.getInteger(R.integer.half_flip_duration).toLong())
        return true
    }

    private fun updateView() {
        if (Preferences.REHEARSAL_REVERSE) {
            rehearsalCard.frontText = cards[cardIndex].answer
            rehearsalCard.backText = cards[cardIndex].question
        } else {
            rehearsalCard.frontText = cards[cardIndex].question
            rehearsalCard.backText = cards[cardIndex].answer
        }
        rehearsalCounter.text = "${cardIndex + 1}/${cards.size}"
    }

    protected fun nextQuestion(doAfter: () -> Unit) {
        val screenWidth = getScreenWidth(activity?.windowManager)
        moveX(rehearsalCard, 0F, -screenWidth).onAnimateEnd {
            if (cardIndex + 1 < cards.size) {
                rehearsalCard.resetCard()
                nextCard()
                moveX(rehearsalCard, screenWidth, 0F).onAnimateEnd { doAfter() }
            } else {
                startFragment(CardFragment())
            }
        }
    }

    private fun nextCard() {
        cardIndex++
        if (cardIndex < cards.size) updateView()
    }

    override fun onBackPressed(): Boolean {
        handler.removeMessages(0) // clear timers
        startFragment(CardFragment())
        return true
    }
}
