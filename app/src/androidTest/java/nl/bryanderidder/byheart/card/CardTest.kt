package nl.bryanderidder.byheart.card

import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.common.truth.Truth
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CardTest {

    @Test
    fun getCorrectPercentage() {
        val testCard = Card("test", "test")
        testCard.amountFalse = 50
        testCard.amountCorrect = 50
        Truth.assertThat(testCard.getCorrectPercentage()).isEqualTo(50)
    }
}