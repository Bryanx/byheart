package com.example.byheart.pile

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Main data access object for the Pile entity. All queries are listed here.
 * Note that the Room library is used for generating the queries.
 * @author Bryan de Ridder
 */
@Dao
interface PileDao {

    @Query("SELECT * FROM pile")
    fun getAll(): LiveData<List<Pile>>

    @Insert
    fun insert(pile: Pile): Long

    @Update
    fun update(pile: Pile)

    @Delete
    fun delete(pile: Pile)

    @Query("DELETE FROM pile")
    fun deleteAll()
}