package nl.bryanderidder.byheart.pile

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import nl.bryanderidder.byheart.shared.database.CardDatabase
import nl.bryanderidder.byheart.util.CoroutineTestProvider
import nl.bryanderidder.byheart.util.CoroutineTestRule
import nl.bryanderidder.byheart.util.getOrAwaitValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class PileViewModelTest {

    private lateinit var db: CardDatabase
    private lateinit var pileVM: PileViewModel

    // Run tasks synchronously
    @Rule
    @JvmField
    val instantExecutorRule = InstantTaskExecutorRule()

    // This can be used to test things in blocking mode
    @get:Rule
    var mainCoroutineRule = CoroutineTestRule()

    @Before
    fun setUp() {
        val context = ApplicationProvider.getApplicationContext<Context>() as Application
        // override normal DB with in memory db.
        db = Room.inMemoryDatabaseBuilder(context, CardDatabase::class.java).build()
        CardDatabase.INSTANCE = db
        pileVM = PileViewModel(context)
        // override coroutine provider so all tests are dispatched on the same thread.
        pileVM.coroutineProvider = CoroutineTestProvider()
        db.pileLocalDao().insert(Pile("test1").apply { this.id = 1; this.listIndex = 0 })
        db.pileLocalDao().insert(Pile("test2").apply { this.id = 2; this.listIndex = 1 })
        db.pileLocalDao().insert(Pile("test3").apply { this.id = 3; this.listIndex = 2 })
    }

    @Test // Test if after removing a pile the listIndex remains correctly ordered.
    fun delete() {
        val testPile1 = Pile("test1").apply { this.id = 1; this.listIndex = 0 }
        val testPile2 = Pile("test1").apply { this.id = 2; this.listIndex = 1 }
        val testPile3 = Pile("test1").apply { this.id = 3; this.listIndex = 2 }
        db.pileLocalDao().insertAll(listOf(testPile1, testPile2, testPile3))

        pileVM.delete(1).invokeOnCompletion {
            val piles = pileVM.allPiles.getOrAwaitValue()
            assertThat(piles).doesNotContain(testPile1)
            assertThat(piles.map { it.listIndex }).containsExactly(0, 1)
        }
    }
}