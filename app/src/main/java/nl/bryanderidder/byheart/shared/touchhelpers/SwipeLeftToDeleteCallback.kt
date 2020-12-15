package nl.bryanderidder.byheart.shared.touchhelpers

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.item_card.view.*
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.card.CardListAdapter
import nl.bryanderidder.byheart.shared.color

/**
 * Contains all logic for the cards in the card fragment to be swiped left to be deleted.
 * It also draws an icon behind the card.
 * @author Bryan de Ridder
 */
class SwipeLeftToDeleteCallback(
    private val adapter: CardListAdapter,
    private val icon: Drawable? = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_delete_white_36),
    private val background: ColorDrawable = ColorDrawable(adapter.getContext().color(R.color.red_300))
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    var isEnabled: Boolean = true

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.UP or ItemTouchHelper.DOWN
        val swipeFlags = if (isItemViewSwipeEnabled) ItemTouchHelper.START else 0
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        if (viewHolder.itemViewType != target.itemViewType) return false
        val from = viewHolder.adapterPosition
        val to = target.adapterPosition
        val pile = adapter.cards.find { it.listIndex == from }
        adapter.cards.filter { it.listIndex in from..to || it.listIndex in to..from }.forEach {
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
        val position = viewHolder.adapterPosition
        adapter.deleteItem(position)
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
                viewHolder.itemView.cvCard.cardElevation = 15F
            } else {
                val itemView = viewHolder.itemView
                val backgroundCornerOffset = 20 //so background is behind the rounded corners of itemView

                val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
                val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
                val iconBottom = iconTop + icon.intrinsicHeight

                if (dX < 0) { // Swiping to the left
                    val iconLeft = itemView.right - iconMargin - icon.intrinsicWidth
                    val iconRight = itemView.right - iconMargin
                    icon.setBounds(iconLeft, iconTop, iconRight, iconBottom)

                    background.setBounds(
                        itemView.right + dX.toInt() - backgroundCornerOffset,
                        itemView.top, itemView.right, itemView.bottom
                    )
                } else { // view is unSwiped
                    background.setBounds(0, 0, 0, 0)
                }

                background.draw(c)
                icon.draw(c)
            }
        }
        viewHolder.itemView.cvCard.cardElevation = 2F
    }

    override fun isItemViewSwipeEnabled(): Boolean = isEnabled
}