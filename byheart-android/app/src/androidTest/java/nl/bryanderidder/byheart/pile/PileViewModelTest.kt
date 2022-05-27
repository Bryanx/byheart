package nl.bryanderidder.byheart.pile

import android.app.Application
import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import nl.bryanderidder.byheart.pile.persistence.PileLocalRepository
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
        pileVM = PileViewModel(context, PileLocalRepository(db.pileLocalDao()))
        // override coroutine provider so all tests are dispatched on the same thread.
        pileVM.coroutineProvider = CoroutineTestProvider()
        db.pileLocalDao().insert(Pile("test1").apply { this.id = 1; this.listIndex = 0 })
        db.pileLocalDao().insert(Pile("test2").apply { this.id = 2; this.listIndex = 1 })
        db.pileLocalDao().insert(Pile("test3").apply { this.id = 3; this.listIndex = 2 })
    }

    @Test
    fun afterDeletingFirstPile_ListIndexShouldRemainCorrect(): Unit = runBlocking {
        var piles = pileVM.allPiles.getOrAwaitValue()
        assertThat(piles.size).isEqualTo(3)
        val pileToDelete = piles[0]

        pileVM.delete(pileToDelete.id)
        piles = pileVM.allPiles.getOrAwaitValue()
        assertThat(piles).doesNotContain(pileToDelete)
        assertThat(piles.map { it.listIndex }).containsExactly(0, 1)
        assertThat(piles.size).isEqualTo(2)
    }

    @Test
    fun afterDeletingMiddlePile_ListIndexShouldRemainCorrect(): Unit = runBlocking {
        var piles = pileVM.allPiles.getOrAwaitValue()
        assertThat(piles.size).isEqualTo(3)
        val pileToDelete = piles[1]

        pileVM.delete(pileToDelete.id)
        piles = pileVM.allPiles.getOrAwaitValue()
        assertThat(piles).doesNotContain(pileToDelete)
        assertThat(piles.map { it.listIndex }).containsExactly(0, 1)
        assertThat(piles.size).isEqualTo(2)
    }

    @Test
    fun afterDeletingLastPile_ListIndexShouldRemainCorrect(): Unit = runBlocking {
        var piles = pileVM.allPiles.getOrAwaitValue()
        assertThat(piles.size).isEqualTo(3)
        val pileToDelete = piles[2]

        pileVM.delete(pileToDelete.id)
        piles = pileVM.allPiles.getOrAwaitValue()
        assertThat(piles).doesNotContain(pileToDelete)
        assertThat(piles.map { it.listIndex }).containsExactly(0, 1)
        assertThat(piles.size).isEqualTo(2)
    }
}