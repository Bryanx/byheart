package com.example.byheart.rehearsal

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.example.byheart.card.Card
import java.util.*

/**
 * Entity that contains a single rehearsal statistic.
 * So each time a user does a single rehearsal a record of this entity should be added.
 * @author Bryan de Ridder
 */
@Entity(
    tableName = "rehearsal_statistic",
    foreignKeys = [ForeignKey(
        entity = Card::class,
        parentColumns = arrayOf("id"),
        childColumns = arrayOf("card_id"),
        onDelete = ForeignKey.CASCADE
    )]
)
data class RehearsalStatistic(
    @ColumnInfo(name = "card_id") private val cardId: Long,
    @ColumnInfo(name = "date") private val date: Long = Calendar.getInstance().timeInMillis,
    @ColumnInfo(name = "correct") private val correct: Boolean = false
) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
}