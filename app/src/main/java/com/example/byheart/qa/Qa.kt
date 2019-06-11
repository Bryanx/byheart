package com.example.byheart.qa

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Qa(
    @ColumnInfo(name = "question") val question: String?,
    @ColumnInfo(name = "answer") val answer: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Int = 0
}