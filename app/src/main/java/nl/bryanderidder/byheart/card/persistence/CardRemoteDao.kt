package nl.bryanderidder.byheart.card.persistence

import android.util.Log
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import nl.bryanderidder.byheart.card.Card
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.shared.database.FirestoreLiveData
import nl.bryanderidder.byheart.shared.utils.IoUtils
import java.util.*

class CardRemoteDao(private val db: FirebaseFirestore) {

    private fun collection() = db.collection("cards")

    fun getAll(): FirestoreLiveData<List<Pile>> = FirestoreLiveData(collection(), Card::class)

    fun insertAllAsync(remotePileId: String, cards: List<Card>) = GlobalScope.async {
        collection().document(remotePileId).set(mapOf("cards" to cards)).await()
    }

    fun findAllForPileIdAsync(remotePileId: String): Deferred<List<Card>> = GlobalScope.async {
        val snapshot = collection().document(remotePileId).get().await()
        val cardMaps = snapshot.data!!["cards"] as List<Map<String, Any>>
        cardMaps.map { Card.from(it) }.toList()
    }

    fun deleteAsync(remotePileId: String) = GlobalScope.async {
        collection().document(remotePileId).delete().await()
    }
}
