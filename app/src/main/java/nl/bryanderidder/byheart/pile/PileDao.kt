package nl.bryanderidder.byheart.pile

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Main data access object for the Pile entity. All queries are listed here.
 * Note that the Room library is used for generating the queries.
 * @author Bryan de Ridder
 */
@Dao
interface PileDao : DataSource {

    @Query("SELECT * FROM pile")
    override fun getAll(): LiveData<List<Pile>>

    @Insert
    override fun insert(pile: Pile): Long

    @Update
    override fun update(pile: Pile)

    @Update(onConflict = OnConflictStrategy.REPLACE)
    override fun updateAll(pile: List<Pile>)

    @Delete
    override fun delete(pile: Pile)

    @Query("DELETE FROM pile")
    override fun deleteAll()

    @Query("SELECT COUNT(*) FROM pile")
    override fun getCount(): Int
}