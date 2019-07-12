package com.example.byheart.card

import androidx.annotation.WorkerThread
import androidx.lifecycle.LiveData

/**
 * Manages queries and allows you to use multiple backend's.
 * Decides whether to fetch data from a network or use results cached in a local database.
 * @author Bryan de Ridder
 */
class CardRepository(private val cardDao: CardDao) {

    val allCards: LiveData<List<Card>> = cardDao.getAll()

    @WorkerThread
    fun insert(card: Card) {
        cardDao.insert(card)
    }

    @WorkerThread
    fun delete(card: Card) {
        cardDao.delete(card)
    }

    @WorkerThread
    fun update(card: Card) {
        cardDao.update(card)
    }
}