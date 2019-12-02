package nl.bryanderidder.byheart.card

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.shared.database.CardDatabase
import nl.bryanderidder.byheart.util.getOrAwaitValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class CardDaoTest {

    private lateinit var testCard: Card
    private lateinit var context: Context
    private lateinit var cardDao: CardDao
    private lateinit var db: CardDatabase

    // Run tasks synchronously
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        context = getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, CardDatabase::class.java).build()
        db.pileDao().insert(Pile("testPile").apply { this.id = 1 })
        cardDao = db.cardDao()
        testCard = Card("test_front", "test_back", 1)
        testCard.id = 1
        cardDao.insert(testCard)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() = db.close()

    @Test
    fun getAll() = assertThat(cardDao.getAll().getOrAwaitValue()).isNotNull()

    @Test
    fun insert() = assertThat(cardDao.getAll().getOrAwaitValue()).contains(testCard)

    @Test
    fun update() {
        testCard.question = "test2"
        cardDao.update(testCard)
        assertThat(cardDao.getAll().getOrAwaitValue()[0].question).isEqualTo("test2")
    }

    @Test
    fun updateAll() {
        cardDao.insert(Card("test2", "test2", 1))
        val piles = cardDao.getAll().getOrAwaitValue()
        piles.forEach { it.answer = "answer" }
        cardDao.updateAll(piles)
        cardDao.getAll().getOrAwaitValue().forEach {
            assertThat(it.answer).isEqualTo("answer")
        }
    }

    @Test
    fun delete() {
        cardDao.delete(testCard)
        assertThat(cardDao.getAll().getOrAwaitValue()).isEmpty()
    }

    @Test
    fun deleteAll() {
        cardDao.insert(Card("test2", "test2", 1))
        cardDao.deleteAll()
        assertThat(cardDao.getAll().getOrAwaitValue()).isEmpty()
    }

    @Test
    fun getCount() = assertThat(cardDao.getCount(1)).isEqualTo(1)
}