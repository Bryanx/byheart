package nl.bryanderidder.byheart.shared.database

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase

object DatabaseMigrations {
    // DB Migration version 1 to 2
    val MIGRATION_1_2: Migration = object : Migration(1, 2) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE Pile ADD COLUMN color INTEGER")
        }
    }
}