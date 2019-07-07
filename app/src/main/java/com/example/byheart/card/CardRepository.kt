package com.example.byheart.card

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

// Manages queries and allows you to use multiple backends.
// Decides whether to fetch data from a network or use results cached in a local database.
class CardRepository(private val cardDao: CardDao) {

    val allCards: LiveData<List<Card>> = cardDao.getAll()

    @WorkerThread
    suspend fun insert(card: Card) {
        cardDao.insert(card)
    }

    @WorkerThread
    suspend fun delete(card: Card) {
        cardDao.delete(card)
    }

    @WorkerThread
    suspend fun update(card: Card) {
        cardDao.update(card)
    }
}