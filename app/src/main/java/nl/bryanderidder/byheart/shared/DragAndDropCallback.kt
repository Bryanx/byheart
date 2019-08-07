package nl.bryanderidder.byheart.shared

import android.graphics.Canvas
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_pile.view.*
import nl.bryanderidder.byheart.pile.PileListAdapter


class DragAndDropCallback(private val adapter: PileListAdapter) : ItemTouchHelper.Callback() {
    override fun isLongPressDragEnabled() = true
    override fun isItemViewSwipeEnabled() = false

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        val swipeFlags = if (isItemViewSwipeEnabled) ItemTouchHelper.START or ItemTouchHelper.END else 0
        return makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
        if (isCurrentlyActive) {
            if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
                viewHolder.itemView.cvPile.cardElevation = 15F
            }
        } else {
            viewHolder.itemView.cvPile.cardElevation = 2F
        }
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        if (viewHolder.itemViewType != target.itemViewType)
            return false
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition
        val pile = adapter.piles.find { it.listIndex == from }
        adapter.piles.filter { it.listIndex in from..to || it.listIndex in to..from }.forEach {
            if (from > to) it.listIndex++
            else it.listIndex--
        }
        pile?.listIndex = to
        adapter.notifyItemMoved(from, to)
        return true
    }

    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)
        adapter.doAfterMovingPiles()
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        // Maybe later...
    }

}