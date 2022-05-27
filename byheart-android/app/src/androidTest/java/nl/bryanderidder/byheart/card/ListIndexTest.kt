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
class ListIndexTest {

    private lateinit var db: CardDatabase
    private lateinit var cardVM: CardViewModel
    private lateinit var londonCard: Card
    private lateinit var lisbonCard: Card
    private lateinit var parisCard: Card
    private lateinit var berlinCard: Card

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
        londonCard = Card("United Kindom", "London", 1).apply { id = 1 }
        lisbonCard = Card("Portugal", "Lisbon", 1).apply { id = 2 }
        parisCard = Card("France", "Paris", 1).apply { id = 3 }
        berlinCard = Card("Germany", "Berlin", 1).apply { id = 4 }
        cardVM.insertAllAsync(listOf(londonCard, lisbonCard, parisCard, berlinCard)).await()
        cardVM.allCards.getOrAwaitValue()
    }

    @Test
    fun listIndexShouldRemainOrderedAfterDeletingOneCardFromTheMiddle(): Unit = runBlocking {
        cardVM.deleteAsync(lisbonCard).await()
        val cards = cardVM.allCards.getOrAwaitValue()
        assertThat(cards).containsExactly(londonCard, parisCard, berlinCard)
        assertThat(cards.map { it.listIndex }).isStrictlyOrdered()
    }

    @Test
    fun listIndexShouldRemainOrderedAfterDeletingTwoCardsFromTheMiddle(): Unit = runBlocking {
        cardVM.deleteByIdAsync(lisbonCard.id).await()
        cardVM.deleteByIdAsync(parisCard.id).await()
        val cards = cardVM.allCards.getOrAwaitValue()
        assertThat(cards).containsExactly(londonCard, berlinCard)
        assertThat(cards.map { it.listIndex }).isStrictlyOrdered()
    }

    @Test
    fun listIndexShouldRemainOrderedAfterDeletingTheFirstCard(): Unit = runBlocking {
        cardVM.deleteAsync(londonCard).await()
        val cards = cardVM.allCards.getOrAwaitValue()
        assertThat(cards).containsExactly(lisbonCard, parisCard, berlinCard)
        assertThat(cards.map { it.listIndex }).isStrictlyOrdered()
    }

    @Test
    fun listIndexShouldRemainOrderedAfterDeletingTheLastCard(): Unit = runBlocking {
        cardVM.deleteAsync(berlinCard).await()
        val cards = cardVM.allCards.getOrAwaitValue()
        assertThat(cards).containsExactly(londonCard, lisbonCard, parisCard)
        assertThat(cards.map { it.listIndex }).isStrictlyOrdered()
    }

    @Test
    fun listIndexShouldRemainOrderedAfterInsertingOneCard(): Unit = runBlocking {
        val moscowCard = Card("Russia", "Moscow", 1).apply { id = 5 }
        cardVM.insertAsync(moscowCard).await()
        val cards = cardVM.allCards.getOrAwaitValue()
        assertThat(cards).containsExactly(londonCard, lisbonCard, parisCard, berlinCard, moscowCard)
        assertThat(cards.map { it.listIndex }).isStrictlyOrdered()
    }

    @Test
    fun listIndexShouldRemainOrderedAfterInsertingTwoCards(): Unit = runBlocking {
        val moscowCard = Card("Russia", "Moscow", 1).apply { id = 5 }
        val madridCard = Card("Russia", "Madrid", 1).apply { id = 6 }
        cardVM.insertAsync(moscowCard).await()
        cardVM.insertAsync(madridCard).await()
        val cards = cardVM.allCards.getOrAwaitValue()
        assertThat(cards).containsAllOf(moscowCard, madridCard)
        assertThat(cards.map { it.listIndex }).isStrictlyOrdered()
        assertThat(cards.size).isEqualTo(6)
    }
}