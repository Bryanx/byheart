package nl.bryanderidder.byheart.rehearsal

import android.os.Bundle
import android.os.Handler
import android.view.*
import android.view.animation.Animation
import androidx.core.view.forEachIndexed
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView.NO_ID
import kotlinx.android.synthetic.main.content_rehearsal.*
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.card.Card
import nl.bryanderidder.byheart.card.CardFragment
import nl.bryanderidder.byheart.card.CardViewModel
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.pile.PileViewModel
import nl.bryanderidder.byheart.rehearsal.result.RehearsalResultFragment
import nl.bryanderidder.byheart.rehearsal.views.RehearsalCard
import nl.bryanderidder.byheart.shared.*
import nl.bryanderidder.byheart.shared.utils.RehearsalSoundUtil
import nl.bryanderidder.byheart.shared.utils.getScreenWidth
import nl.bryanderidder.byheart.shared.utils.moveX
import org.koin.android.viewmodel.ext.android.sharedViewModel
import java.util.*


/**
 * Abstract class that contains the base of a rehearsal fragment.
 * Each Rehearsal Fragment should extend this class.
 * @author Bryan de Ridder
 */
abstract class RehearsalFragment : Fragment(), IOnBackPressed {

    private val cardVM: CardViewModel by sharedViewModel()
    private val sessionVM: SessionViewModel by sharedViewModel()
    private val pileVM: PileViewModel by sharedViewModel()
    private val rehearsalViewModel: RehearsalViewModel by sharedViewModel()
    protected lateinit var layout: View
    protected lateinit var pile: Pile
    protected lateinit var cards: MutableList<Card>
    protected lateinit var menu: Menu
    protected lateinit var sounds: RehearsalSoundUtil
    protected val handler: Handler = Handler()
    protected var cardIndex = 0
    private val animations: MutableList<Animation> = mutableListOf()
    private val pileId: Long get() = sessionVM.pileId.value ?: NO_ID
    protected val pileColor: Int get() = sessionVM.pileColor.value ?: R.color.colorPrimary

    open fun doAfterGetData(): Unit = addEventHandlers()

    abstract fun addEventHandlers()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        getCards()
        addToolbar()
        rehearsalViewModel.reset()
        pileVM.allPiles.observe(viewLifecycleOwner) { piles ->
            pile = piles.first { pile -> pile.id == pileId }
            val rehearsalCard = layout.findViewById<RehearsalCard>(R.id.rehearsalCard)
            rehearsalCard.setBackColor(pileColor)
            rehearsalCard.addPronounceLocale(
                Locale.getAvailableLocales().first { loc -> loc.code == pile.languageCardFront },
                Locale.getAvailableLocales().first { loc -> loc.code == pile.languageCardBack })
        }
        sounds = RehearsalSoundUtil(requireContext())
        return layout
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.rehearsal_menu, menu)
        this.menu = menu
        menu.forEachIndexed { _, item ->
            item.isChecked = Preferences.read(item.getId(resources))
            if (item.isChecked) hideOtherViews(item)
            if (item.itemId == R.id.rehearsal_mute) setVolumeIcon(item, true)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.rehearsal_mute -> {
                setVolumeIcon(item)
                toggleMenuItem(item, item.getId(resources)).run { true }
            }
            R.id.rehearsal_restart -> onRestart(true)
            R.id.rehearsal_pronounce -> toggleMenuItem(item, item.getId(resources)).run { true }
            R.id.rehearsal_repeat_wrong -> {
                cardIndex = 0
                toggleMenuItem(item, item.getId(resources)).run { true }
                getCards()
                onRestart(false)
            }
            R.id.rehearsal_shuffle -> {
                toggleMenuItem(item, item.getId(resources))
                shuffleCards(item.isChecked)
                onRestart(false)
            }
            R.id.rehearsal_reverse -> {
                toggleMenuItem(item, item.getId(resources))
                onRestart(false)
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
                if (cards.size < 5) requireContext().dialog()
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

    private fun setVolumeIcon(item: MenuItem, initial: Boolean = false) {
        val isChecked = if (initial) !item.isChecked else item.isChecked
        if (isChecked) item.setIcon(R.drawable.ic_volume_up_black_24dp)
        else item.setIcon(R.drawable.ic_volume_off_black_24)
    }

    // Toggles the other items in the menu, and shows the corresponding view.
    private fun hideOtherViews(item: MenuItem): Boolean {
        handler.removeMessages(0)
        listOf(R.id.rehearsal_typed, R.id.rehearsal_multiple_choice, R.id.rehearsal_memory).forEach { id ->
            if (id == item.itemId) {
                if (!item.isChecked) toggleMenuItem(item, item.getId(resources))
            } else {
                val otherItem = menu.findItem(id)
                if (otherItem.isChecked) toggleMenuItem(otherItem, otherItem.getId(resources))
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
        cardVM.getByPileId(sessionVM.pileId).observe(viewLifecycleOwner) {
            val tempCards = it.toMutableList()
            if (Preferences.REHEARSAL_SHUFFLE) tempCards.shuffle()
            else tempCards.sortBy { card -> card.listIndex }
            cards = tempCards
            doAfterGetData()
            updateView()
        }
    }

    open fun onRestart(startFromBeginning: Boolean, doAfter: (() -> Unit)? = null): Boolean {
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
        val moveX = moveX(rehearsalCard, 0F, -screenWidth)
        animations.add(moveX)
        moveX.onAnimateEnd {
            if (cardIndex + 1 >= cards.size)
                startFragment(RehearsalResultFragment())
            else {
                rehearsalCard.resetCard()
                nextCard()
                val move2 = moveX(rehearsalCard, screenWidth, 0F)
                animations.add(move2)
                move2.onAnimateEnd {
                    doAfter()
                    animations.clear()
                }
            }
        }
    }

    private fun nextCard() {
        cardIndex++
        if (cardIndex < cards.size) updateView()
    }

    open fun onCorrect() {
        sounds.playCorrect()
        rehearsalViewModel.incrementAmountCorrect()
        cards[cardIndex].amountCorrect++
    }

    open fun onFalse() {
        sounds.playFalse()
        rehearsalViewModel.incrementAmountFalse()
        cards[cardIndex].amountFalse++
        if (Preferences.REHEARSAL_REPEAT_WRONG) cards.add(cards[cardIndex])
    }

    override fun onBackPressed(): Boolean {
        //update amount false/corect
        animations.forEach { it.cancel() }
        cardVM.updateAll(cards.subList(0, cardIndex))
        handler.removeMessages(0) // clear timers
        startFragment(CardFragment())
        return true
    }

    override fun onDestroy() {
        super.onDestroy()
        sounds.destroy()
    }
}
