package nl.bryanderidder.byheart.pile

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
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
        db = TestUtil.createDb()
        pileVM = TestUtil.getPileViewModel();
        db.pileDao().insert(Pile("test1").apply { this.id = 1; this.listIndex = 0 })
        db.pileDao().insert(Pile("test2").apply { this.id = 2; this.listIndex = 1 })
        db.pileDao().insert(Pile("test3").apply { this.id = 3; this.listIndex = 2 })
    }

    @After
    @Throws(IOException::class)
    fun tearDown() = db.close()

    @Test // Test if after removing a pile the listIndex remains correctly ordered.
    fun delete() {
        val firstPile = pileVM.allPiles.getOrAwaitValue()[0]
        pileVM.delete(firstPile.id).invokeOnCompletion {
            val piles = pileVM.allPiles.getOrAwaitValue()
            assertThat(piles).doesNotContain(firstPile)
            assertThat(piles.map { it.listIndex }).containsExactly(0, 1)
        }
    }
}