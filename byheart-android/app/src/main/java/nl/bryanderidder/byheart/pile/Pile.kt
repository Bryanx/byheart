package nl.bryanderidder.byheart.pile

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.firebase.firestore.Exclude
import com.google.firebase.firestore.IgnoreExtraProperties
import com.google.gson.Gson
import nl.bryanderidder.byheart.card.Card
import nl.bryanderidder.byheart.shared.ExcludeJson
import nl.bryanderidder.byheart.shared.code
import java.util.*

/**
 * Class that holds the Pile entity, it has a one to many relationship with Card.
 * @author Bryan de Ridder
 */
@IgnoreExtraProperties
@Entity
data class Pile(@ColumnInfo(name = "name") var name: String?) {
    @get:Exclude @ExcludeJson @PrimaryKey(autoGenerate = true) var id: Long = 0
    @ColumnInfo(name = "languageCardFront") var languageCardFront: String = Locale.getDefault().code
    @ColumnInfo(name = "languageCardBack") var languageCardBack: String = Locale.getDefault().code
    @ColumnInfo(name = "color") var color: Int? = -7288071
    @get:Exclude @ColumnInfo(name = "listIndex") var listIndex: Int = -1
    @get:Exclude @ColumnInfo(name = "remoteId") var remoteId: String = ""

    constructor() : this("")

    // List for making json
    @get:Exclude @Ignore var cards: MutableList<Card> = mutableListOf()

    // Normalized data for remote
    @ExcludeJson @Ignore var cardCount: Int = 0
    @ExcludeJson @Ignore var creationDate: Date = Date()
    @ExcludeJson @Ignore var lastUpdateDate: Date = Date()
    @ExcludeJson @Ignore var userId: String = ""

    fun deepCopy(): Pile {
        val JSON = Gson().toJson(this)
        return Gson().fromJson(JSON, Pile::class.java)
    }

    companion object {
        fun from(map: Map<String, Any>): Pile {
            val pile: Pile = Pile(map["name"] as String)
            map["languageCardFront"]?.let { pile.languageCardFront = it as String }
            map["languageCardBack"]?.let { pile.languageCardBack = it as String }
            map["color"]?.let { pile.color = (it as Long).toInt() }
            return pile
        }
    }
}