package nl.bryanderidder.byheart.pile.persistence

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.asDeferred
import kotlinx.coroutines.tasks.await
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.shared.Preferences
import nl.bryanderidder.byheart.shared.exceptions.ByheartException
import nl.bryanderidder.byheart.shared.firestore.FirestoreLiveData
import java.util.*

class PileRemoteDao(private val db: FirebaseFirestore) {

    private fun collection() = db.collection("piles")

    fun getAll(): FirestoreLiveData<List<Pile>> =
        FirestoreLiveData(
            collection(),
            Pile::class
        )

    fun insert(newPile: Pile): String {
        val pile = newPile.deepCopy()
        pile.creationDate = Date()
        pile.listIndex = -1
        pile.userId = Preferences.USER_ID
        val id = collection().document().id
        collection().document(id).set(pile)
        return id
    }

    fun findAsync(remotePileId: String): Deferred<Pile> = CoroutineScope(IO).async {
        val result = collection().document(remotePileId).get().await().data
            ?: throw ByheartException("Unfortunately this stack no longer exists in our database. Please ask the sender to generate a new link.")
        Pile.from(result)
    }

    fun deleteAsync(remotePileId: String): Deferred<Void> {
        return collection().document(remotePileId).delete().asDeferred()
    }

    fun updateAsync(updatePile: Pile): Deferred<Void> {
        val pile = updatePile.deepCopy()
        pile.lastUpdateDate = Date()
        pile.listIndex = -1
        pile.userId = Preferences.USER_ID
        return collection().document(pile.remoteId).set(pile).asDeferred()
    }
}
