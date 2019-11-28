package nl.bryanderidder.byheart.store

import com.google.firebase.firestore.FirebaseFirestore
import nl.bryanderidder.byheart.pile.DataSource
import nl.bryanderidder.byheart.pile.Pile
import nl.bryanderidder.byheart.shared.database.FirestoreLiveData

class StoreDao : DataSource {

    val db = FirebaseFirestore.getInstance()

    override fun getAll(): FirestoreLiveData<List<Pile>> = FirestoreLiveData(db.collection("piles"), Pile::class)

    override fun insert(pile: Pile): Long {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun update(pile: Pile) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun updateAll(pile: List<Pile>) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun delete(pile: Pile) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun deleteAll() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getCount(): Int {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}
