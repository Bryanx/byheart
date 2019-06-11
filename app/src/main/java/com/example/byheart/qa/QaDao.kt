package com.example.byheart.qa

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface QaDao {
    @Query("SELECT * FROM qa")
    fun getAll(): LiveData<List<Qa>>

    @Query("SELECT * FROM qa WHERE id IN (:qaIds)")
    fun loadAllByIds(qaIds: IntArray): LiveData<List<Qa>>

    @Query("SELECT * FROM qa WHERE question LIKE :question LIMIT 1")
    fun findByQuestion(question: String): Qa

    @Insert
    fun insert(qa: Qa)

    @Delete
    fun delete(qa: Qa)

    @Query("DELETE FROM qa")
    fun deleteAll()
}