package nl.bryanderidder.byheart.shared.database

import android.util.Log
import androidx.annotation.Nullable
import androidx.lifecycle.LiveData
import com.google.firebase.firestore.*
import kotlin.reflect.KClass

@Suppress("UNCHECKED_CAST")
class FirestoreLiveData<T>(
    val colRef: CollectionReference,
    val clazz: KClass<*>
) : LiveData<T>() {

    private var registration: ListenerRegistration? = null

    private var eventListener: EventListener<QuerySnapshot> = object : EventListener<QuerySnapshot> {
        override fun onEvent(@Nullable queryDocumentSnapshots: QuerySnapshot?, @Nullable e: FirebaseFirestoreException?) {
            if (e != null) {
                Log.i(TAG, "Listen failed.", e)
                return
            }
            if (queryDocumentSnapshots != null && !queryDocumentSnapshots.isEmpty) {
                val itemList: MutableList<T?> = ArrayList()
                for (snapshot in queryDocumentSnapshots.documents) {
                    val item = snapshot.toObject(clazz.java) as T
                    itemList.add(item)
                }
                value = itemList as T
            }
        }
    }

    override fun onActive() {
        super.onActive()
        registration = colRef.addSnapshotListener(eventListener)
    }

    override fun onInactive() {
        super.onInactive()
        if (!hasActiveObservers()) {
            registration!!.remove()
            registration = null
        }
    }

    companion object {
        const val TAG = "debinf firestore"
    }
}