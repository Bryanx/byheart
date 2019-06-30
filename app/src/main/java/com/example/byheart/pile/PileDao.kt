package com.example.byheart.pile

import androidx.lifecycle.LiveData
import androidx.room.*

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