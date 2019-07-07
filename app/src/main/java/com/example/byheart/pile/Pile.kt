package com.example.byheart.pile

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pile(@ColumnInfo(name = "name") var name: String?) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
    @ColumnInfo(name = "languageCardFront") var languageCardFront: String = "en"
    @ColumnInfo(name = "languageCardBack") var languageCardBack: String = "en"
}