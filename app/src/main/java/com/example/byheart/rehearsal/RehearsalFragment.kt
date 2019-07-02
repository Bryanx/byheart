package com.example.byheart.rehearsal

import android.animation.AnimatorInflater
import android.animation.AnimatorSet
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.byheart.MainActivity
import com.example.byheart.R
import com.example.byheart.card.Card
import com.example.byheart.card.CardFragment
import com.example.byheart.card.CardViewModel
import com.example.byheart.shared.*
import kotlinx.android.synthetic.main.content_rehearsal.*
import android.speech.tts.TextToSpeech
import java.util.*


class RehearsalFragment : Fragment() {

    private lateinit var ttobj: TextToSpeech
    private lateinit var layout: View
    private lateinit var cardViewModel: CardViewModel
    private lateinit var pileId: String
    private lateinit var cards: List<Card>
    private var flipIn: AnimatorSet? = null
    private var flipOut: AnimatorSet? = null
    private var backIsVisible = false
    private var cardIndex = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        layout = container!!.inflate(R.layout.content_rehearsal)
        cardViewModel = ViewModelProviders.of(this).get(CardViewModel::class.java)
        pileId = (activity as MainActivity).pileId
        getCards()
        loadAnimations()
        addToolbar(activity!!, true, "", true) {
            fragmentManager?.startFragment(CardFragment())
        }
        ttobj = TextToSpeech(activity?.applicationContext, TextToSpeech.OnInitListener {})
        ttobj.language = Locale.UK
        return layout
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.rehearsal_menu, menu)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        addEventHandlers()
        changeCameraDistance()
        addVoiceButton()
        super.onViewCreated(view, savedInstanceState)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean = when (item.itemId) {
        R.id.action_restart_rehearsal -> {
            onRestart()
            true
        }
        else -> super.onOptionsItemSelected(item)
    }

    private fun onRestart() {
        cardIndex = 0
        updateView()
    }

    private fun addVoiceButton() {
        ivPronounce.setOnClickListener {
            when {
                backIsVisible -> ttobj.speak(cards[cardIndex].answer, TextToSpeech.QUEUE_FLUSH, null)
                else -> ttobj.speak(cards[cardIndex].question, TextToSpeech.QUEUE_FLUSH, null)
            }
        }
    }

    private fun getCards() {
        cardViewModel.allCards.observe(this, Observer { cardsFromDb ->
            cardsFromDb?.filter { it.pileId.toString() == pileId }
                ?.let { cards = it }
            updateView()
        })
    }

    private fun changeCameraDistance() {
        val distance = 8000
        val scale = resources.displayMetrics.density * distance
        cardFront.cameraDistance = scale
        cardBack.cameraDistance = scale
    }

    private fun loadAnimations() {
        flipIn = AnimatorInflater.loadAnimator(activity, R.animator.animate_out_flip) as AnimatorSet
        flipOut = AnimatorInflater.loadAnimator(activity, R.animator.animate_in_flip) as AnimatorSet
    }

    private fun addEventHandlers() {
        cardFront.setOnClickListener { flipCard() }
        cardBack.setOnClickListener { flipCard() }
        cardBtnCorrect.setOnClickListener { nextQuestion() }
        cardBtnFalse.setOnClickListener { nextQuestion() }
    }

    private fun updateView() {
        cardFront.text = cards[cardIndex].question
        cardBack.text = cards[cardIndex].answer
        rehearsalCounter.text = "${cardIndex+1}/${cards.size}"
    }

    private fun nextQuestion() {
        enableButtons(false)
        val screenWidth = getScreenWidth(activity?.windowManager)
        moveX(cardFront, 0F, -screenWidth)
        moveX(cardBack, 0F, -screenWidth).onAnimateEnd {
            if (cardIndex + 1 < cards.size) {
                resetCardPosition()
                nextCard()
                moveX(cardFront, screenWidth, 0F)
                moveX(cardBack, screenWidth, 0F).onAnimateEnd {
                    enableButtons(true)
                }
            } else {
                fragmentManager?.startFragment(CardFragment())
            }
        }
    }

    private fun enableButtons(bool: Boolean) {
        cardBtnCorrect.isEnabled = bool
        cardBtnFalse.isEnabled = bool
    }

    private fun nextCard() {
        cardIndex++
        if (cardIndex < cards.size) updateView()
    }

    private fun resetCardPosition() {
        cardFront.alpha = 1F
        cardFront.rotationY = 0F
        backIsVisible = false
        cardBack.alpha = 0F
    }

    private fun flipCard() {
        backIsVisible = if (!backIsVisible) {
            flipIn!!.setTarget(cardFront)
            flipOut!!.setTarget(cardBack)
            flipIn!!.start()
            flipOut!!.start()
            true
        } else {
            flipIn!!.setTarget(cardBack)
            flipOut!!.setTarget(cardFront)
            flipIn!!.start()
            flipOut!!.start()
            false
        }
    }
}
