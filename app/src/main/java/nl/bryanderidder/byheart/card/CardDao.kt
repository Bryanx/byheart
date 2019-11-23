package nl.bryanderidder.byheart.card

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * The main card database access object.
 * All queries are written here.
 * It uses the android Room library to generate queries.
 * @author Bryan de Ridder
 */
@Dao
interface CardDao {
    @Query("SELECT * FROM card")
    fun getAll(): LiveData<List<Card>>

    @Insert
    fun insert(card: Card)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cards: List<Card>)

    @Update
    fun update(card: Card)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(cards: List<Card>)

    @Delete
    fun delete(card: Card)

    @Query("DELETE FROM card")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM card WHERE pile_id = :pileId")
    fun getCount(pileId: Long): Int
}