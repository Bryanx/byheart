package nl.bryanderidder.byheart.card

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.delay
import kotlinx.coroutines.runBlocking
import nl.bryanderidder.byheart.card.persistence.CardRepository
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.shared.database.CardDatabase
import nl.bryanderidder.byheart.util.CoroutineTestProvider
import nl.bryanderidder.byheart.util.CoroutineTestRule
import nl.bryanderidder.byheart.util.getOrAwaitValue
import nl.bryanderidder.byheart.util.observeForTesting
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class CardViewModelTest {

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
    fun setUp(): Unit = runBlocking {
        val context = ApplicationProvider.getApplicationContext<Context>() as Application
        // override normal DB with in memory db.
        db = Room.inMemoryDatabaseBuilder(context, CardDatabase::class.java).build()
        CardDatabase.INSTANCE = db
        cardVM = CardViewModel(
            context,
            CardRepository(db.cardDao())
        )
        // override coroutine provider so all tests are dispatched on the same thread.
        cardVM.coroutineProvider = CoroutineTestProvider()
        db.pileLocalDao().insert(Pile("testPile1"))
        db.pileLocalDao().insert(Pile("testPile2"))
        // wait for allCards LiveData to be initialized before running tests
        cardVM.allCards.getOrAwaitValue()
    }

    @Test
    fun getByPileId(): Unit = runBlocking {
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
    fun insert(): Unit = runBlocking {
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

    @Test
    fun insertAll() = runBlocking<Unit> {
        val londonCard = Card("United Kindom", "London", 1)
        val lisbonCard = Card("Portugal", "Lisbon", 1)
        cardVM.insertAllAsync(listOf(londonCard, lisbonCard)).await()
        assertThat(cardVM.allCards.getOrAwaitValue()[0]).isEqualTo(londonCard)
        assertThat(cardVM.allCards.getOrAwaitValue()[1]).isEqualTo(lisbonCard)
    }

    @Test
    fun update(): Unit = runBlocking {
        val londonCard = Card("United Kindom", "London", 1)
        db.cardDao().insert(londonCard.apply { id = 1 })

        londonCard.answer = "Liverpool"
        cardVM.updateAsync(londonCard).await()
        val cards = cardVM.allCards.getOrAwaitValue()
        assertThat(cards[0].question).isEqualTo("United Kindom")
        assertThat(cards[0].answer).isEqualTo("Liverpool")
    }

    @Test
    fun updateAll(): Unit = runBlocking {
        val londonCard = Card("United Kindom", "London", 1).apply { id = 1 }
        val lisbonCard = Card("Portugal", "Lisbon", 1).apply { id = 2 }
        val parisCard = Card("France", "Paris", 1).apply { id = 3 }
        db.cardDao().insertAll(listOf(londonCard, lisbonCard, parisCard))
        cardVM.allCards.getOrAwaitValue()

        londonCard.answer = "Liverpool"
        lisbonCard.question = "Test"
        cardVM.updateAll(listOf(londonCard, lisbonCard))
        delay(100)

        val cards = cardVM.allCards.getOrAwaitValue()
        assertThat(cards[0]).isEqualTo(londonCard)
        assertThat(cards[1]).isEqualTo(lisbonCard)
    }

    @Test
    fun delete(): Unit = runBlocking {
        val londonCard = Card("United Kindom", "London", 1).apply { id = 1 }
        val lisbonCard = Card("Portugal", "Lisbon", 1).apply { id = 2 }
        val parisCard = Card("France", "Paris", 1).apply { id = 3 }
        cardVM.insertAllAsync(listOf(londonCard, lisbonCard, parisCard)).await()
        cardVM.allCards.getOrAwaitValue()
        cardVM.deleteAsync(lisbonCard).await()
        val cards = cardVM.allCards.getOrAwaitValue()
        assertThat(cards).doesNotContain(lisbonCard)
        assertThat(cards.map { it.listIndex }).containsExactly(0, 1)
        assertThat(cards.size).isEqualTo(2)
    }

}