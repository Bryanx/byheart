package nl.bryanderidder.byheart.shared.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.card.Card
import nl.bryanderidder.byheart.card.persistence.CardDao
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.pile.persistence.PileLocalDao
import nl.bryanderidder.byheart.shared.Preferences
import nl.bryanderidder.byheart.shared.Preferences.KEY_NOT_FIRST_START
import nl.bryanderidder.byheart.shared.Preferences.KEY_REHEARSAL_TYPED
import nl.bryanderidder.byheart.shared.color

/**
 * Room database class.
 * @author Bryan de Ridder
 */
@Database(entities = [Card::class, Pile::class], version = 3, exportSchema = true)
abstract class CardDatabase : RoomDatabase() {
    abstract fun cardDao(): CardDao
    abstract fun pileLocalDao(): PileLocalDao

    companion object {
        @Volatile
        var INSTANCE: CardDatabase? = null

        //Singleton
        fun getDatabase(context: Context): CardDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room
                    .databaseBuilder(context, CardDatabase::class.java, "card")
                    .addCallback(CardDatabaseCallback(context))
                    .addMigrations(DatabaseMigrations.MIGRATION_1_2)
                    .addMigrations(DatabaseMigrations.MIGRATION_2_3)
                    .build()
                INSTANCE = instance
                return instance
            }
        }

        private class CardDatabaseCallback(private val context: Context) : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                if (!isFirstStart()) return
                INSTANCE?.let { database ->
                    GlobalScope.launch {
                        populateDatabase(database.cardDao(), database.pileLocalDao(), context)
                    }
                }
            }
        }

        // Check first start
        private fun isFirstStart(): Boolean {
            if (!Preferences.NOT_FIRST_START) {
                Preferences.write(KEY_NOT_FIRST_START, true)
                Preferences.write(KEY_REHEARSAL_TYPED, true)
                return true
            }
            return false
        }

        fun populateDatabase(cardDao: CardDao, pileLocalDao: PileLocalDao, context: Context) {
            cardDao.deleteAll()
            pileLocalDao.deleteAll()
            val pile = InitialData.getFrench(context.color(R.color.teal_200))
            pileLocalDao.insert(pile)
            cardDao.insertAll(pile.cards)
            val pile2 = InitialData.getCapitals(context.color(R.color.orange_300))
            pileLocalDao.insert(pile2)
            cardDao.insertAll(pile2.cards)
            val pile3 = InitialData.getPeriodicTable(context.color(R.color.purple_200))
            pileLocalDao.insert(pile3)
            cardDao.insertAll(pile3.cards)
            val pile4 = InitialData.getPersonalPile(context.color(R.color.blue_200))
            pileLocalDao.insert(pile4)
            cardDao.insertAll(pile4.cards)
        }
    }
}