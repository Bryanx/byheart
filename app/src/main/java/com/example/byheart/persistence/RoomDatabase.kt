package com.example.byheart.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.byheart.qa.Qa
import com.example.byheart.qa.QaDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(entities = [Qa::class], version = 1)
abstract class QaDatabase : RoomDatabase() {
    abstract fun qaDao(): QaDao

    companion object {
        @Volatile
        private var INSTANCE: QaDatabase? = null

        //Singleton
        fun getDatabase(context: Context, scope: CoroutineScope): QaDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room
                    .databaseBuilder(context.applicationContext, QaDatabase::class.java, "qa")
                    .addCallback(QaDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                return instance
            }
        }

        private class QaDatabaseCallback(private val scope: CoroutineScope) : RoomDatabase.Callback() {
            override fun onOpen(db: SupportSQLiteDatabase) {
                super.onOpen(db)
                INSTANCE?.let { database ->
                    scope.launch(Dispatchers.IO) {
                        populateDatabase(database.qaDao())
                    }
                }
            }
        }

        fun populateDatabase(qaDao: QaDao) {
//            qaDao.deleteAll()
//            val qa = Qa( "Hello", "Bonjour")
//            qaDao.insert(qa)
        }
    }
}