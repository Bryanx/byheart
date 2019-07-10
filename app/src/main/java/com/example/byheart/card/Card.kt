package com.example.byheart.card

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.byheart.pile.Pile

/**
 * Main card entity.
 * It belongs to a pile (pile_id).
 * Typically consists of a question and answer.
 * The Id is automatically generated.
 * @author Bryan de Ridder
 */
@Entity(
    tableName = "card",
    foreignKeys = [ForeignKey(
        entity = Pile::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("pile_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class Card(
    @ColumnInfo(name = "question") val question: String?,
    @ColumnInfo(name = "answer") val answer: String?,
    @ColumnInfo(name = "pile_id") val pileId: Long = 1
) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}