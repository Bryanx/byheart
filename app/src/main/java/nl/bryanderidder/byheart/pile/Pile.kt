package nl.bryanderidder.byheart.pile

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Class that holds the Pile entity, it has a many to one relationship with Card.
 * @author Bryan de Ridder
 */
@Entity
data class Pile(@ColumnInfo(name = "name") var name: String?) {
    @PrimaryKey(autoGenerate = true) var id: Long = 0
    @ColumnInfo(name = "languageCardFront") var languageCardFront: String = "en"
    @ColumnInfo(name = "languageCardBack") var languageCardBack: String = "en"
    @ColumnInfo(name = "color") var color: Int? = -7288071
}