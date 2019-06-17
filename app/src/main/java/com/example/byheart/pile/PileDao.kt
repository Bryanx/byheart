package com.example.byheart.pile

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface PileDao {

    @Query("SELECT * FROM pile")
    fun getAll(): LiveData<List<Pile>>

    @Insert
    fun insert(pile: Pile): Long

    @Delete
    fun delete(pile: Pile)

    @Query("DELETE FROM pile")
    fun deleteAll()
}