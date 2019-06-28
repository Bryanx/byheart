package com.example.byheart.shared

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.byheart.card.Card
import com.example.byheart.card.CardDao
import com.example.byheart.pile.Pile
import com.example.byheart.pile.PileDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Card::class, Pile::class], version = 1)
abstract class CardDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun pileDao(): PileDao

    companion object {
        @Volatile
        private var INSTANCE: CardDatabase? = null

        //Singleton
        fun getDatabase(context: Context, scope: CoroutineScope): CardDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room
                    .databaseBuilder(context.applicationContext, CardDatabase::class.java, "card")
                    .addCallback(CardDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }

        private class CardDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
//                        populateDatabase(database.cardDao(), database.pileDao())
                    }
                }
            }
        }

        fun populateDatabase(cardDao: CardDao, pileDao: PileDao) {
            cardDao.deleteAll()
            pileDao.deleteAll()
            val pile = Pile("French")
            pile.id = 5
            val card = Card( "Hello", "Bonjour", 5)
            pileDao.insert(pile)
            cardDao.insert(card)
            val pile2 = Pile("German")
            val pile3 = Pile("Turkish")
            pileDao.insert(pile2)
            pileDao.insert(pile3)

        }
    }
}