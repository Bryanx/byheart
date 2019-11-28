package nl.bryanderidder.byheart.pile

import androidx.lifecycle.LiveData

interface DataSource {

    fun getAll(): LiveData<List<Pile>>

    fun insert(pile: Pile): Long

    fun update(pile: Pile)

    fun updateAll(pile: List<Pile>)

    fun delete(pile: Pile)

    fun deleteAll()

    fun getCount(): Int
}
