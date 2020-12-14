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
    @ColumnInfo(name = "question") var question: String?,
    @ColumnInfo(name = "answer") var answer: String?,
    @Exclude @ColumnInfo(name = "pile_id") val pileId: Long = 1
) {
    @Exclude @PrimaryKey(autoGenerate = true) var id: Long = 0
    @ColumnInfo(name = "listIndex") var listIndex: Int = -1
    @ColumnInfo(name = "amountCorrect") var amountCorrect: Int = 0
    @ColumnInfo(name = "amountFalse") var amountFalse: Int = 0

    constructor() : this("", "")
    constructor(q: String, a: String): this(q, a, -1)

    fun getCorrectPercentage(): Int {
        val correct = amountCorrect
        if (correct == 0) return 0
        val wrong = amountFalse
        val percentage = correct / (wrong + correct).toFloat() * 100
        return percentage.toInt()
    }

    companion object {
        fun from(map: Map<String, Any>): Card {
            return Card(
                map["question"] as String,
                map["answer"] as String,
                map["pileId"] as Long
            )
        }
    }
}