package com.example.byheart.rehearsal

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.graphics.PorterDuff
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.speech.tts.TextToSpeech
import android.view.*
import android.widget.TextView
import androidx.core.view.forEachIndexed
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.byheart.MainActivity
import com.example.byheart.R
import com.example.byheart.card.Card
import com.example.byheart.card.CardFragment
import com.example.byheart.card.CardViewModel
import com.example.byheart.pile.Pile
import com.example.byheart.pile.PileViewModel
import com.example.byheart.shared.*
import com.example.byheart.shared.Preferences.REHEARSAL_REVERSE
import com.example.byheart.shared.Preferences.REHEARSAL_SHUFFLE
import kotlinx.android.synthetic.main.content_rehearsal.*
import java.util.*


abstract class RehearsalFragment : Fragment(), IOnBackPressed {

    private lateinit var textToSpeech: TextToSpeech
    private lateinit var cardViewModel: CardViewModel
    private lateinit var pileViewModel: PileViewModel
    private lateinit var pileId: String
    private lateinit var flipIn: AnimatorSet
    private lateinit var flipOut: AnimatorSet
    protected lateinit var languageCardFront: Locale
    protected lateinit var languageCardBack: Locale
    protected lateinit var layout: View
    protected lateinit var pile: Pile
    protected lateinit var cards: MutableList<Card>
    protected lateinit var menu: Menu
    protected lateinit var correctSound: MediaPlayer
    protected lateinit var wrongSound: MediaPlayer
    protected val handler: Handler = Handler()
    private var backOfCardIsVisible = false
    protected var cardIndex = 0

    abstract fun addEventHandlers()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        cardViewModel = ViewModelProviders.of(this).get(CardViewModel::class.java)
        pileViewModel = ViewModelProviders.of(this).get(PileViewModel::class.java)
        pileId = (activity as MainActivity).pileId
        getCards()
        loadAnimations()
        addToolbar(true, "", true)
        textToSpeech = TextToSpeech(activity?.applicationContext, TextToSpeech.OnInitListener {
            if (it == TextToSpeech.SUCCESS) {
                pileViewModel.allPiles.observe(this, Observer {piles ->
                    pile = piles.first { pile -> pile.id == pileId.toLong() }
                    languageCardFront = Locale.getAvailableLocales().first { loc -> loc.displayName == pile.languageCardFront }
                    languageCardBack = Locale.getAvailableLocales().first { loc -> loc.displayName == pile.languageCardBack }
                    textToSpeech.language = languageCardBack
                })
            }
        })
        correctSound = MediaPlayer.create(context, R.raw.correct)
        wrongSound = MediaPlayer.create(context, R.raw.incorrect)
        return layout
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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
                fragmentManager?.startFragment(RehearsalTypedFragment()).run { true }
            }
            R.id.rehearsal_memory -> {
                hideOtherViews(item)
                fragmentManager?.startFragment(RehearsalMemoryFragment()).run { true }
            }
            R.id.rehearsal_multiple_choice -> {
                if (cards.size < 5) context!!.dialog()
                    .setMessage("You need at least 5 cards.")
                    .setCancelable(false)
                    .setPositiveButton("Ok") { _, _ -> }
                    .create()
                    .show()
                else {
                    hideOtherViews(item)
                    fragmentManager?.startFragment(RehearsalMultipleChoiceFragment())
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
                doAfterGetData()
            }
            updateView()
        })
    }

    open fun doAfterGetData() {
        addEventHandlers()
    }

    open fun onRestart(startFromBeginning: Boolean, doAfter: (() -> Unit)?): Boolean {
        handler.removeMessages(0)
        if (startFromBeginning) cardIndex = 0
        if (backOfCardIsVisible) flipCard()
        if (Preferences.read(REHEARSAL_SHUFFLE)) cards.shuffle()
        Handler().postDelayed(({
            updateView()
            doAfter?.invoke()
        }), resources.getInteger(R.integer.half_flip_duration).toLong())
        return true
    }

    private fun addVoiceButton() {
        ivPronounce.setOnClickListener {
            if (backOfCardIsVisible) {
                speakCard(cardBack, languageCardBack)
                println("pronounce back, lang:${languageCardBack.displayName}")
            } else {
                speakCard(cardFront, languageCardFront)
                println("pronounce front, lang:${languageCardFront.displayName}")
            }
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

    protected fun speakCard(tv: TextView, language: Locale) {
        when {
            !Preferences.read(REHEARSAL_REVERSE) -> textToSpeech.language = language
            tv == cardBack -> textToSpeech.language = languageCardFront
            else -> textToSpeech.language = languageCardBack
        }
        textToSpeech.pronounce(tv.string)

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
        handler.postDelayed({ ivPronounce.setTint(R.color.grey_500, PorterDuff.Mode.SRC_IN) }, 200)
    }

    protected fun flipCard() {
        flipY(ivPronounce, 0f, 90f).onAnimateEnd { flipY(ivPronounce, -90f, 0f) }
        backOfCardIsVisible = if (!backOfCardIsVisible) {
            handler.postDelayed({ ivPronounce.setTint(R.color.colorPrimaryDark, PorterDuff.Mode.SRC_IN) }, 150)
            flipIn.setTarget(cardFront)
            flipOut.setTarget(cardBack)
            flipIn.start()
            flipOut.start()
            true
        } else {
            handler.postDelayed({ ivPronounce.setTint(R.color.grey_500, PorterDuff.Mode.SRC_IN) }, 150)
            flipIn.setTarget(cardBack)
            flipOut.setTarget(cardFront)
            flipIn.start()
            flipOut.start()
            false
        }
    }

    override fun onBackPressed(): Boolean {
        handler.removeMessages(0) // clear timers
        context?.let {
            it.dialog().setMessage("Are you sure you want to quit?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ -> fragmentManager?.startFragment(CardFragment()) }
            .setNegativeButton("No") { _, _ ->  }
            .show()
        }
        return true
    }
}
