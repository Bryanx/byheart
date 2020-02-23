package nl.bryanderidder.byheart.pile

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth.assertThat
import nl.bryanderidder.byheart.shared.database.CardDatabase
import nl.bryanderidder.byheart.util.TestUtil
import nl.bryanderidder.byheart.util.getOrAwaitValue
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class PileDaoTest {

    private lateinit var context: Context
    private lateinit var pileDao: PileDao
    private lateinit var db: CardDatabase
    private lateinit var testPile: Pile

    // Run tasks synchronously
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setUp() {
        db = TestUtil.createDb()
        pileDao = db.pileDao()
        testPile = Pile("test")
        testPile.id = 1
        pileDao.insert(testPile)
    }

    @After
    @Throws(IOException::class)
    fun tearDown() = db.close()

    @Test
    fun getAll() = assertThat(pileDao.getAll().getOrAwaitValue()).isNotNull()

    @Test
    fun insert() = assertThat(pileDao.getAll().getOrAwaitValue()).contains(testPile)

    @Test
    fun update() {
        testPile.name = "test2"
        pileDao.update(testPile)
        assertThat(pileDao.getAll().getOrAwaitValue()[0].name).isEqualTo("test2")
    }

    @Test
    fun updateAll() {
        pileDao.insert(Pile("test2"))
        val piles = pileDao.getAll().getOrAwaitValue()
        piles.forEach { it.languageCardFront = "nl_NL" }
        pileDao.updateAll(piles)
        pileDao.getAll().getOrAwaitValue().forEach {
            assertThat(it.languageCardFront).isEqualTo("nl_NL")
        }
    }

    @Test
    fun delete() {
        pileDao.delete(testPile)
        assertThat(pileDao.getAll().getOrAwaitValue()).isEmpty()
    }

    @Test
    fun deleteAll() {
        pileDao.insert(Pile("test2"))
        pileDao.deleteAll()
        assertThat(pileDao.getAll().getOrAwaitValue()).isEmpty()
    }

    @Test
    fun getCount() {
        pileDao.deleteAll()
        pileDao.insert(Pile("test1"))
        pileDao.insert(Pile("test2"))
        assertThat(pileDao.getCount()).isEqualTo(2)
    }
}