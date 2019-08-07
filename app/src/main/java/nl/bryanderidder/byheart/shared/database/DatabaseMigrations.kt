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

    // DB Migration version 2 to 3
    val MIGRATION_2_3: Migration = object : Migration(2, 3) {
        override fun migrate(database: SupportSQLiteDatabase) {
            database.execSQL("ALTER TABLE Pile ADD COLUMN listIndex INTEGER DEFAULT -1 NOT NULL")
            database.execSQL("UPDATE Pile SET listIndex = id-1")
        }
    }
}