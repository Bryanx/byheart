package nl.bryanderidder.byheart.card.persistence

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import nl.bryanderidder.byheart.card.Card
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.shared.Preferences
import nl.bryanderidder.byheart.shared.firestore.FirestoreLiveData

class CardRemoteDao(private val db: FirebaseFirestore) {

    private fun collection() = db.collection("cards")

    fun getAll(): FirestoreLiveData<List<Pile>> =
        FirestoreLiveData(
            collection(),
            Card::class
        )

    fun insertAllAsync(remotePileId: String, cards: List<Card>): Deferred<Void> {
        return collection().document(remotePileId).set(mapOf(
            "userId" to Preferences.USER_ID,
            "cards" to cards
        )).asDeferred()
    }

    fun findAllForPileIdAsync(remotePileId: String): Deferred<List<Card>> = CoroutineScope(Dispatchers.IO).async {
        val snapshot = collection().document(remotePileId).get().await()
        val cardMaps = snapshot.data!!["cards"] as List<Map<String, Any>>
        cardMaps.map { Card.from(it) }.toList()
    }

    fun deleteAsync(remotePileId: String): Deferred<Void> {
        return collection().document(remotePileId).delete().asDeferred()
    }
}
