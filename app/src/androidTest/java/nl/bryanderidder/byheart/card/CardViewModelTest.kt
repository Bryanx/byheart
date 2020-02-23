package nl.bryanderidder.byheart.card

import android.app.Application
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.shared.database.CardDatabase
import nl.bryanderidder.byheart.util.CoroutineTestRule
import nl.bryanderidder.byheart.util.TestUtil
import nl.bryanderidder.byheart.util.getOrAwaitValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.io.IOException

@ExperimentalCoroutinesApi
class CardViewModelTest {

    private lateinit var context: Application
    private lateinit var db: CardDatabase
    private lateinit var cardVM: CardViewModel

    // Run tasks synchronously
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    // This can be used to test things in blocking mode
    @get:Rule
    var mainCoroutineRule = CoroutineTestRule()

    @Before
    fun setUp() {
        db = TestUtil.createDb()
        cardVM = TestUtil.getCardViewModel()
        db.pileDao().insert(Pile("testPile1"))
        db.pileDao().insert(Pile("testPile2"))
    }

    @After
    @Throws(IOException::class)
    fun tearDown() = db.close()

    @Test
    fun getByPileId() {
        val londonCard = Card("United Kindom", "London", 1)
        val lisbonCard = Card("Portugal", "Lisbon", 1)
        db.cardDao().insert(londonCard)
        db.cardDao().insert(lisbonCard)
        val parisCard = Card("France", "Paris", 2)
        db.cardDao().insert(parisCard)

        val id = MutableLiveData<Long>().apply { this.value = 1 }
        val cards = cardVM.getByPileId(id).getOrAwaitValue()

        assertThat(cards).containsExactly(londonCard, lisbonCard)
    }

    @Test
    fun insert() {
        val londonCard = Card("United Kindom", "London", 1)
        cardVM.insert(londonCard).invokeOnCompletion {
            val card = cardVM.allCards.getOrAwaitValue()[0]
            assertThat(card).isEqualTo(londonCard)
            assertThat(card.listIndex).isEqualTo(0)
        }
        val lisbonCard = Card("Portugal", "Lisbon", 1)
        cardVM.insert(lisbonCard).invokeOnCompletion {
            assertThat(cardVM.allCards.getOrAwaitValue()[1].listIndex).isEqualTo(1)
        }
    }

    @Test
    fun update() {
        val londonCard = Card("United Kindom", "London", 1)
        db.cardDao().insert(londonCard)

        londonCard.answer = "Liverpool"
        cardVM.update(londonCard).invokeOnCompletion {
            val cards = cardVM.allCards.getOrAwaitValue()
            assertThat(cards[0].question).isEqualTo("United Kindom")
            assertThat(cards[0].answer).isEqualTo("Liverpool")
        }
    }

    @Test
    fun updateAll() {
        val londonCard = Card("United Kindom", "London", 1)
        val lisbonCard = Card("Portugal", "Lisbon", 1)
        val parisCard = Card("France", "Paris", 1)
        db.cardDao().insertAll(listOf(londonCard, lisbonCard, parisCard))

        londonCard.answer = "Liverpool"
        lisbonCard.question = "Test"
        cardVM.updateAll(listOf(londonCard, londonCard)).invokeOnCompletion {
            val cards = cardVM.allCards.getOrAwaitValue()
            assertThat(cards[0]).isEqualTo(londonCard)
            assertThat(cards[1]).isEqualTo(lisbonCard)
        }
    }

    @Test // Test if after removing a card the listIndex remains correctly ordered.
    fun delete() {
        val londonCard = Card("United Kindom", "London", 1).apply { this.listIndex = 1 }
        val lisbonCard = Card("Portugal", "Lisbon", 1).apply { this.listIndex = 2 }
        val parisCard = Card("France", "Paris", 1).apply { this.listIndex = 3 }
        db.cardDao().insertAll(listOf(londonCard, lisbonCard, parisCard))

        cardVM.delete(lisbonCard).invokeOnCompletion {
            val cards = cardVM.allCards.getOrAwaitValue()
            assertThat(cards).doesNotContain(lisbonCard)
            assertThat(cards.map { it.listIndex }).containsExactly(0, 1)
        }
    }

}