package nl.bryanderidder.byheart.shared

import android.graphics.Canvas
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import nl.bryanderidder.byheart.R
import nl.bryanderidder.byheart.card.CardListAdapter

/**
 * Contains all logic for the cards in the card fragment to be swiped right to be edited.
 * It also draws an icon behind the card.
 * @author Bryan de Ridder
 */
class SwipeRightToEditCallback(
    private val adapter: CardListAdapter,
    private val icon: Drawable? = ContextCompat.getDrawable(adapter.getContext(), R.drawable.ic_edit_white_24dp),
    private val background: ColorDrawable = ColorDrawable(adapter.getContext().color(R.color.orange_300))
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.RIGHT) {

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {
        return false
    }

    override fun getMovementFlags(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder): Int {
        val dragFlags = ItemTouchHelper.RIGHT
        val swipeFlags = if (isItemViewSwipeEnabled) ItemTouchHelper.END else 0
        return ItemTouchHelper.Callback.makeMovementFlags(dragFlags, swipeFlags)
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.adapterPosition
        adapter.editItem(position)
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
        val itemView = viewHolder.itemView
        val backgroundCornerOffset = 20 //so background is behind the rounded corners of itemView

        val iconMargin = (itemView.height - icon!!.intrinsicHeight) / 2
        val iconTop = itemView.top + (itemView.height - icon.intrinsicHeight) / 2
        val iconBottom = iconTop + icon.intrinsicHeight

        if (dX > 0) { // Swiping to the right
            val iconLeft = itemView.left + iconMargin + icon.intrinsicWidth
            val iconRight = itemView.left + iconMargin
            icon.setBounds(iconRight, iconTop, iconLeft, iconBottom)

            background.setBounds(
                itemView.left, itemView.top, itemView.left + dX.toInt() + backgroundCornerOffset,
                itemView.bottom
            )
        } else { // view is unSwiped
            background.setBounds(0, 0, 0, 0)
        }

        background.draw(c)
        icon.draw(c)
    }

}