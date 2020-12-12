package nl.bryanderidder.byheart.store.persistence

import com.google.firebase.firestore.FirebaseFirestore
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.shared.database.FirestoreLiveData

class PileRemoteDao {

    val db = FirebaseFirestore.getInstance()

    fun getAll(): FirestoreLiveData<List<Pile>> = FirestoreLiveData(db.collection("piles"), Pile::class)

    fun insert(pile: Pile): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
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
