package nl.bryanderidder.byheart.card

import androidx.annotation.Nullable
import androidx.recyclerview.widget.DiffUtil

/**
 * Callback to check the difference between two lists of cards.
 * Used by the cardListAdapter.
 * @author Bryan de Ridder
 */
class CardsDiffCallback<T : DataObject>(private var newCards: List<T>, private var oldCards: List<T>) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldCards.size

    override fun getNewListSize(): Int = newCards.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCards[oldItemPosition].id == newCards[newItemPosition].id
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        return oldCards[oldItemPosition] == newCards[newItemPosition]
    }

    @Nullable
    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }
}

interface DataObject {
    var id: Long
}
