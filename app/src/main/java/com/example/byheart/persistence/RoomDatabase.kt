package com.example.byheart.persistence

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.byheart.model.Qa

@Database(entities = [Qa::class], version = 1)
abstract class QaDatabase : RoomDatabase() {
    abstract fun qaDao(): QaDao

    companion object {
        @Volatile
        private var INSTANCE: QaDatabase? = null

        //Singleton
        fun getDatabase(context: Context): QaDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null) {
                return tempInstance
            }
            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    QaDatabase::class.java,
                    "qa_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }
}