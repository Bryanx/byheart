package nl.bryanderidder.byheart.card

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import nl.bryanderidder.byheart.card.persistence.CardRepository
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.shared.database.CardDatabase
import nl.bryanderidder.byheart.util.CoroutineTestProvider
import nl.bryanderidder.byheart.util.CoroutineTestRule
import nl.bryanderidder.byheart.util.getOrAwaitValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CardViewModelTest {

    private lateinit var db: CardDatabase
    private lateinit var cardVM: CardViewModel

    // Run tasks synchronously
//    @Rule
//    @JvmField
//    val instantExecutorRule = InstantTaskExecutorRule()

    // This can be used to test things in blocking mode
    @get:Rule
    var mainCoroutineRule = CoroutineTestRule()

    @Before
    fun setUp() {
        mainCoroutineRule.runBlockingTest {
            val context = ApplicationProvider.getApplicationContext<Context>() as Application
            // override normal DB with in memory db.
            db = Room.inMemoryDatabaseBuilder(context, CardDatabase::class.java).build()
            CardDatabase.INSTANCE = db
            cardVM = CardViewModel(context,
                CardRepository(db.cardDao())
            )
            // override coroutine provider so all tests are dispatched on the same thread.
            cardVM.coroutineProvider = CoroutineTestProvider()
            db.pileLocalDao().insert(Pile("testPile1"))
            db.pileLocalDao().insert(Pile("testPile2"))
        }
    }

    @Test
    fun getByPileId() {

        mainCoroutineRule.runBlockingTest {

            val londonCard = Card("United Kindom", "London", 1)
            val lisbonCard = Card("Portugal", "Lisbon", 1)
            db.cardDao().insert(londonCard)
            db.cardDao().insert(lisbonCard)
            val parisCard = Card("France", "Paris", 2)
            db.cardDao().insert(parisCard)
            delay(1_000)
            val id = MutableLiveData<Long>().apply { this.value = 1 }
            val cards = cardVM.getByPileId(id).getOrAwaitValue()

            assertThat(cards).containsExactly(londonCard, lisbonCard)
        }
    }

    @Test
    fun insert() {
        mainCoroutineRule.runBlockingTest {

            delay(1_000)
            val londonCard = Card("United Kindom", "London", 1)
            cardVM.insertAsync(londonCard).invokeOnCompletion {
                val card = cardVM.allCards.getOrAwaitValue()[0]
                assertThat(card).isEqualTo(londonCard)
                assertThat(card.listIndex).isEqualTo(0)
            }
            val lisbonCard = Card("Portugal", "Lisbon", 1)
            cardVM.insertAsync(lisbonCard).invokeOnCompletion {
                assertThat(cardVM.allCards.getOrAwaitValue()[1].listIndex).isEqualTo(1)
            }
        }
    }

    @Test
    fun insertAll() = runBlocking<Unit> {
        mainCoroutineRule.runBlockingTest {
            delay(1_000)
            val londonCard = Card("United Kindom", "London", 1)
            val lisbonCard = Card("Portugal", "Lisbon", 1)
            cardVM.insertAllAsync(listOf(londonCard, lisbonCard))
            assertThat(cardVM.allCards.getOrAwaitValue()[0]).isEqualTo(londonCard)
            assertThat(cardVM.allCards.getOrAwaitValue()[1]).isEqualTo(lisbonCard)
        }
    }

    @Test
    fun update() {
        mainCoroutineRule.runBlockingTest {

            delay(1_000)
            val londonCard = Card("United Kindom", "London", 1)
            db.cardDao().insert(londonCard)

            londonCard.answer = "Liverpool"
            cardVM.updateAsync(londonCard).invokeOnCompletion {
                val cards = cardVM.allCards.getOrAwaitValue()
                assertThat(cards[0].question).isEqualTo("United Kindom")
                assertThat(cards[0].answer).isEqualTo("Liverpool")
            }
        }
    }

    @Test
    fun updateAll() {
        mainCoroutineRule.runBlockingTest {

            delay(1_000)
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
    }

    @Test // Test if after removing a card the listIndex remains correctly ordered.
    fun delete() {
        mainCoroutineRule.runBlockingTest {

            delay(1_000)
            val londonCard = Card("United Kindom", "London", 1).apply { this.listIndex = 1 }
            val lisbonCard = Card("Portugal", "Lisbon", 1).apply { this.listIndex = 2 }
            val parisCard = Card("France", "Paris", 1).apply { this.listIndex = 3 }
            db.cardDao().insertAll(listOf(londonCard, lisbonCard, parisCard))

            cardVM.deleteAsync(lisbonCard).invokeOnCompletion {
                val cards = cardVM.allCards.getOrAwaitValue()
                assertThat(cards).doesNotContain(lisbonCard)
                assertThat(cards.map { it.listIndex }).containsExactly(0, 1)
            }
        }
    }

}