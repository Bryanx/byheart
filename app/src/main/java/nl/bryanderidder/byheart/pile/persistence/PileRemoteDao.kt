package nl.bryanderidder.byheart.pile.persistence

import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.Deferred
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.async
import kotlinx.coroutines.tasks.await
import nl.bryanderidder.byheart.pile.Pile
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
        val id = collection().document().id
        collection().document(id).set(pile)
        return id
    }

    fun findAsync(remotePileId: String): Deferred<Pile> = GlobalScope.async {
        val pileSnapshot: DocumentSnapshot = collection().document(remotePileId).get().await()
        Pile.from(pileSnapshot.data!!)
    }

    fun deleteAsync(remotePileId: String) = GlobalScope.async {
        collection().document(remotePileId).delete().await()
    }
}
