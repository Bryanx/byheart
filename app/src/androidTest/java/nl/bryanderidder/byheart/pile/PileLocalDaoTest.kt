package nl.bryanderidder.byheart.pile

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import nl.bryanderidder.byheart.pile.persistence.PileLocalDao
import nl.bryanderidder.byheart.shared.database.CardDatabase
import nl.bryanderidder.byheart.util.getOrAwaitValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class PileLocalDaoTest {

    private lateinit var pileLocalDao: PileLocalDao
    private lateinit var db: CardDatabase
    private lateinit var testPile: Pile

    // Run tasks synchronously
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>() as Application
        db = Room.inMemoryDatabaseBuilder(context, CardDatabase::class.java).build()
        CardDatabase.INSTANCE = db
        pileLocalDao = db.pileLocalDao()
        testPile = Pile("test")
        testPile.id = 1
        pileLocalDao.insert(testPile)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() = db.close()

    @Test
    fun getAll() = assertThat(pileLocalDao.getAll().getOrAwaitValue()).isNotNull()

    @Test
    fun insert() = assertThat(pileLocalDao.getAll().getOrAwaitValue()).contains(testPile)

    @Test
    fun update() {
        testPile.name = "test2"
        pileLocalDao.update(testPile)
        assertThat(pileLocalDao.getAll().getOrAwaitValue()[0].name).isEqualTo("test2")
    }

    @Test
    fun updateAll() {
        pileLocalDao.insert(Pile("test2"))
        val piles = pileLocalDao.getAll().getOrAwaitValue()
        piles.forEach { it.languageCardFront = "nl_NL" }
        pileLocalDao.updateAll(piles)
        pileLocalDao.getAll().getOrAwaitValue().forEach {
            assertThat(it.languageCardFront).isEqualTo("nl_NL")
        }
    }

    @Test
    fun delete() {
        pileLocalDao.delete(testPile)
        assertThat(pileLocalDao.getAll().getOrAwaitValue()).isEmpty()
    }

    @Test
    fun deleteAll() {
        pileLocalDao.insert(Pile("test2"))
        pileLocalDao.deleteAll()
        assertThat(pileLocalDao.getAll().getOrAwaitValue()).isEmpty()
    }

    @Test
    fun getCount() {
        pileLocalDao.deleteAll()
        pileLocalDao.insert(Pile("test1"))
        assertThat(pileLocalDao.getCount()).isEqualTo(1)
    }
}