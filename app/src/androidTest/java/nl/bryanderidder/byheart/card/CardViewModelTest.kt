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
    private lateinit var cardViewModel: CardViewModel

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
        cardViewModel = TestUtil.getCardViewModel()
        db.pileDao().insert(Pile("testPile1"))
        db.pileDao().insert(Pile("testPile2"))
    }

    @After
    @Throws(IOException::class)
    fun tearDown() = db.close()

    @Test
    fun getAllById() {
        val londonCard = Card("United Kindom", "London", 1)
        val lisbonCard = Card("Portugal", "Lisbon", 1)
        db.cardDao().insert(londonCard)
        db.cardDao().insert(lisbonCard)
        val parisCard = Card("France", "Paris", 2)
        db.cardDao().insert(parisCard)

        val id = MutableLiveData<Long>().apply { this.value = 1 }
        val cards = cardViewModel.getByPileId(id).getOrAwaitValue()

        assertThat(cards).containsExactly(londonCard, lisbonCard)
    }

}