package com.example.byheart.card

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.byheart.pile.Pile

@Entity(
    tableName = "card",
    foreignKeys = arrayOf(
        ForeignKey(
            entity = Pile::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("pile_id"),
            onDelete = ForeignKey.CASCADE
        )
    )
)
data class Card(
    @ColumnInfo(name = "question") val question: String?,
    @ColumnInfo(name = "answer") val answer: String?
) {
    @PrimaryKey(autoGenerate = true)
    var id: Long = 0
    @ColumnInfo(name = "pile_id")
    var pileId: Long = 1
}