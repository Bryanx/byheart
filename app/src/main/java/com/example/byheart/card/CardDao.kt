package com.example.byheart.card

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface CardDao {
    @Query("SELECT * FROM card")
    fun getAll(): LiveData<List<Card>>

    @Query("SELECT * FROM card WHERE id IN (:cardIds)")
    fun loadAllByIds(cardIds: IntArray): LiveData<List<Card>>

    @Query("SELECT * FROM card WHERE question LIKE :question LIMIT 1")
    fun findByQuestion(question: String): Card

    @Insert
    fun insert(card: Card)

    @Delete
    fun delete(card: Card)

    @Query("DELETE FROM card")
    fun deleteAll()
}