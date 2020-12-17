package nl.bryanderidder.byheart.pile.persistence

import androidx.lifecycle.LiveData
import androidx.room.*
import nl.bryanderidder.byheart.pile.Pile

/**
 * Main data access object for the Pile entity. All queries are listed here.
 * Note that the Room library is used for generating the queries.
 * @author Bryan de Ridder
 */
@Dao
interface PileLocalDao {

    @Query("SELECT * FROM pile")
    fun getAll(): LiveData<List<Pile>>

    @Insert
    fun insert(pile: Pile): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(piles: List<Pile>)

    @Update
    fun update(pile: Pile)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun updateAll(pile: List<Pile>)

    @Delete
    fun delete(pile: Pile)

    @Query("DELETE FROM pile")
    fun deleteAll()

    @Query("SELECT COUNT(*) FROM pile")
    fun getCount(): Int
}