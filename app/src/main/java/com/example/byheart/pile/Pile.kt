package com.example.byheart.pile

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Pile(@ColumnInfo(name = "name") val name: String?) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}