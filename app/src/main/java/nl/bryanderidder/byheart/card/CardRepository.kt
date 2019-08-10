package nl.bryanderidder.byheart.card

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 * Manages queries and allows you to use multiple backend's.
 * Decides whether to fetch data from a network or use results cached in a local database.
 * @author Bryan de Ridder
 */
class CardRepository(private val cardDao: CardDao) {

    val allCards: LiveData<List<Card>> = cardDao.getAll()

    @WorkerThread
    fun insert(card: Card) = cardDao.insert(card)

    @WorkerThread
    fun insertAll(cards: List<Card>) = cardDao.insertAll(cards)

    @WorkerThread
    suspend fun delete(card: Card) = withContext(Dispatchers.Default){
        cardDao.delete(card)
    }

    @WorkerThread
    fun update(card: Card) = cardDao.update(card)

    @WorkerThread
    fun getCount(pileId: Long) = cardDao.getCount(pileId)

    @WorkerThread
    suspend fun updateAll(cards: List<Card>) = withContext(Dispatchers.Default){
        cardDao.updateAll(cards)
    }
}