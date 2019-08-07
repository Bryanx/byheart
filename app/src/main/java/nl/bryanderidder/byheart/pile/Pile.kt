package nl.bryanderidder.byheart.pile

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import nl.bryanderidder.byheart.card.Card
import nl.bryanderidder.byheart.shared.Exclude

/**
 * Class that holds the Pile entity, it has a one to many relationship with Card.
 * @author Bryan de Ridder
 */
@Entity
data class Pile(@ColumnInfo(name = "name") var name: String?) {
    @Exclude @PrimaryKey(autoGenerate = true) var id: Long = 0
    @ColumnInfo(name = "languageCardFront") var languageCardFront: String = "en"
    @ColumnInfo(name = "languageCardBack") var languageCardBack: String = "en"
    @ColumnInfo(name = "color") var color: Int? = -7288071
    @ColumnInfo(name = "listIndex") var listIndex: Int = -1

    // List for making json
    @Ignore var cards: MutableList<Card> = mutableListOf()
}