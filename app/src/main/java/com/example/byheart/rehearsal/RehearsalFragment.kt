package com.example.byheart.rehearsal

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.view.*
import android.view.View.GONE
import android.view.View.VISIBLE
import androidx.core.view.forEachIndexed
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.byheart.MainActivity
import com.example.byheart.R
import com.example.byheart.card.Card
import com.example.byheart.card.CardFragment
import com.example.byheart.card.CardViewModel
import com.example.byheart.shared.*
import com.example.byheart.shared.Preferences.REHEARSAL_MEMORY
import com.example.byheart.shared.Preferences.REHEARSAL_PRONOUNCE
import com.example.byheart.shared.Preferences.REHEARSAL_REVERSE
import com.example.byheart.shared.Preferences.REHEARSAL_SHUFFLE
import com.example.byheart.shared.Preferences.REHEARSAL_TYPED
import kotlinx.android.synthetic.main.content_rehearsal.*
import java.util.*


abstract class RehearsalFragment : Fragment() {

    protected lateinit var textToSpeech: TextToSpeech
    protected lateinit var layout: View
    protected lateinit var cardViewModel: CardViewModel
    protected lateinit var pileId: String
    protected lateinit var cards: MutableList<Card>
    protected lateinit var flipIn: AnimatorSet
    protected lateinit var flipOut: AnimatorSet
    protected lateinit var menu: Menu
    protected var backOfCardIsVisible = false
    protected var cardIndex = 0

    abstract fun addEventHandlers()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        cardViewModel = ViewModelProviders.of(this).get(CardViewModel::class.java)
        pileId = (activity as MainActivity).pileId
        getCards()
        loadAnimations()
        addToolbar(activity!!, true, "", true) {
            fragmentManager?.startFragment(CardFragment())
        }
        textToSpeech = TextToSpeech(activity?.applicationContext, TextToSpeech.OnInitListener {})
        textToSpeech.language = Locale.UK
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        addEventHandlers()
        addVoiceButton()
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
            R.id.rehearsal_restart -> restartRehearsal()
            R.id.rehearsal_pronounce -> {
                toggleMenuItem(item, item.getName(resources))
                true
            }
            R.id.rehearsal_shuffle -> {
                toggleMenuItem(item, item.getName(resources))
                shuffleCards(item.isChecked)
                updateView()
                true
            }
            R.id.rehearsal_reverse -> {
                toggleMenuItem(item, item.getName(resources))
                updateView()
                true
            }
            R.id.rehearsal_typed -> {
                hideOtherViews(item)
                fragmentManager?.startFragment(RehearsalTypedFragment())
                true
            }
            R.id.rehearsal_memory -> {
                hideOtherViews(item)
                fragmentManager?.startFragment(RehearsalMemoryFragment())
                true
            }
            R.id.rehearsal_multiple_choice -> {
                if (cards.size < 5) context!!.dialog()
                    .setMessage("You need at least 5 cards.")
                    .setCancelable(false)
                    .setPositiveButton("Ok") { _, _ -> }
                    .create()
                    .show()
                else hideOtherViews(item)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    // Toggles the other items in the menu, and shows the corresponding view.
    private fun hideOtherViews(item: MenuItem): Boolean {
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

    private fun shuffleCards(shuffleIsOn: Boolean) {
        if (shuffleIsOn) cards.shuffle()
        else cards.sortBy { it.id }
    }

    private fun getCards() {
        cardViewModel.allCards.observe(this, Observer { cardsFromDb ->
            cardsFromDb?.filter { it.pileId.toString() == pileId }?.let {
                val tempCards = it.toMutableList()
                if (Preferences.read(REHEARSAL_SHUFFLE)) tempCards.shuffle()
                cards = tempCards
            }
            updateView()
        })
    }

    private fun restartRehearsal(): Boolean {
        cardIndex = 0
        if (backOfCardIsVisible) flipCard()
        Handler().postDelayed(({ updateView() }), resources.getInteger(R.integer.half_flip_duration).toLong())
        return true
    }

    private fun addVoiceButton() {
        ivPronounce.setOnClickListener {
            textToSpeech.pronounce(when {
                    backOfCardIsVisible -> cardBack.string
                    else -> cardFront.string
                }
            )
        }
    }

    private fun loadAnimations() {
        flipIn = AnimatorInflater.loadAnimator(activity, R.animator.animate_out_flip) as AnimatorSet
        flipOut = AnimatorInflater.loadAnimator(activity, R.animator.animate_in_flip) as AnimatorSet
    }


    private fun updateView() {
        if (Preferences.read(REHEARSAL_REVERSE)) {
            cardFront.text = cards[cardIndex].answer
            cardBack.text = cards[cardIndex].question
        } else {
            cardFront.text = cards[cardIndex].question
            cardBack.text = cards[cardIndex].answer
        }
        rehearsalCounter.text = "${cardIndex + 1}/${cards.size}"
    }

    protected fun pronounceAnswer() {
        textToSpeech.pronounce(cardBack.text.toString())
    }

    protected fun nextQuestion(doAfter: () -> Unit) {
        val screenWidth = getScreenWidth(activity?.windowManager)
        moveX(cardFront, 0F, -screenWidth)
        moveX(ivPronounce, 0F, -screenWidth)
        moveX(cardBack, 0F, -screenWidth).onAnimateEnd {
            if (cardIndex + 1 < cards.size) {
                resetCardPosition()
                nextCard()
                moveX(cardFront, screenWidth, 0F)
                moveX(ivPronounce, screenWidth, 0F)
                moveX(cardBack, screenWidth, 0F).onAnimateEnd { doAfter() }
            } else {
                fragmentManager?.startFragment(CardFragment())
            }
        }
    }

    private fun nextCard() {
        cardIndex++
        if (cardIndex < cards.size) updateView()
    }

    private fun resetCardPosition() {
        cardFront.alpha = 1F
        cardFront.rotationY = 0F
        backOfCardIsVisible = false
        cardBack.alpha = 0F
        Handler().postDelayed({ ivPronounce.setTint(R.color.grey_500, PorterDuff.Mode.SRC_IN) }, 200)
    }

    protected fun flipCard() {
        flipY(ivPronounce, 0f, 90f).onAnimateEnd { flipY(ivPronounce, -90f, 0f) }
        backOfCardIsVisible = if (!backOfCardIsVisible) {
            Handler().postDelayed({ ivPronounce.setTint(R.color.colorPrimaryDark, PorterDuff.Mode.SRC_IN) }, 150)
            flipIn.setTarget(cardFront)
            flipOut.setTarget(cardBack)
            flipIn.start()
            flipOut.start()
            true
        } else {
            Handler().postDelayed({ ivPronounce.setTint(R.color.grey_500, PorterDuff.Mode.SRC_IN) }, 150)
            flipIn.setTarget(cardBack)
            flipOut.setTarget(cardFront)
            flipIn.start()
            flipOut.start()
            false
        }
    }
}
