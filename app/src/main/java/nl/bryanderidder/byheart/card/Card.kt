package nl.bryanderidder.byheart.card

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.shared.Exclude

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
    @Exclude @ColumnInfo(name = "pile_id") val pileId: Long = 1
) {
    @Exclude @PrimaryKey(autoGenerate = true) var id: Long = 0

    constructor(q: String, a: String): this(q, a, -1)
}