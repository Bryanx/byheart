package nl.bryanderidder.byheart.store.persistence

import com.google.firebase.firestore.FirebaseFirestore
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.shared.database.FirestoreLiveData
import java.util.*

class PileRemoteDao {

    val db = FirebaseFirestore.getInstance()

    private fun collection() = db.collection("piles")

    fun getAll(): FirestoreLiveData<List<Pile>> = FirestoreLiveData(collection(), Pile::class)

    fun insert(pile: Pile): String {
        val id = collection().document().id
        val cards = pile.cards.toMutableList()
        pile.cardCount = pile.cards.size
        pile.cards = mutableListOf()
        pile.creationDate = Date()
        pile.listIndex = -1
        db.collection("piles").document(id).set(pile)
        db.collection("cards").document(id).set(mapOf("cards" to cards))
        return id
    }

    fun update(pile: Pile) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun updateAll(pile: List<Pile>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun delete(pile: Pile) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun deleteAll() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    fun getCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}
